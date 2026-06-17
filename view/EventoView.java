package view;

import controller.EventoController;
import model.Evento;
import model.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EventoView {

    private EventoController controller;
    private Scanner scanner;
    private Usuario usuarioLogado;

    private static final SimpleDateFormat SDF_DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final String[] NOMES_MES = {
        "", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    public EventoView(Scanner scanner, Usuario usuarioLogado) {
        this.controller = new EventoController();
        this.scanner = scanner;
        this.usuarioLogado = usuarioLogado;
    }

    public void iniciar() {
        boolean rodando = true;
        while (rodando) {
            System.out.println("--- GERENCIA EJ: AGENDA ---");
            System.out.println("1. Ver calendário");
            System.out.println("2. Agendar evento");
            System.out.println("3. Convidar pessoas para um evento");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            switch (scanner.nextLine()) {
                case "1": exibirCalendario();  break;
                case "2": agendarEvento();     break;
                case "3": convidarPessoas();   break;
                case "0": rodando = false;     break;
                default:
                    System.out.println("[ERRO] Opção inválida.");
            }
        }
    }

    private void exibirCalendario() {
        int[] mesAno = lerMesAno();
        int mes = mesAno[0], ano = mesAno[1];

        List<Evento> eventos = controller.listarPorMes(usuarioLogado, mes, ano);

        imprimirGrade(mes, ano, eventos);
        imprimirListaEventos(mes, ano, eventos);
    }

    // desenha a grade do mês em ASCII, marcando com "*" os dias com evento
    private void imprimirGrade(int mes, int ano, List<Evento> eventos) {
        Set<Integer> diasComEvento = new HashSet<>();
        Calendar aux = Calendar.getInstance();
        for (Evento e : eventos) {
            aux.setTime(e.getData());
            diasComEvento.add(aux.get(Calendar.DAY_OF_MONTH));
        }

        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes - 1, 1);
        int diasNoMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int primeiroDiaSemana = cal.get(Calendar.DAY_OF_WEEK);

        System.out.println("\n        " + NOMES_MES[mes] + " " + ano);
        System.out.println("Dom Seg Ter Qua Qui Sex Sáb");

        StringBuilder linha = new StringBuilder();
        for (int i = 1; i < primeiroDiaSemana; i++) {
            linha.append("    ");
        }

        for (int dia = 1; dia <= diasNoMes; dia++) {
            String marcador = diasComEvento.contains(dia) ? "*" : " ";
            linha.append(String.format("%2d%s ", dia, marcador));

            int posicaoNaSemana = (primeiroDiaSemana - 1 + dia) % 7;
            if (posicaoNaSemana == 0) {
                System.out.println(linha.toString());
                linha = new StringBuilder();
            }
        }
        if (linha.length() > 0) {
            System.out.println(linha.toString());
        }

        System.out.println("(*) dia com evento agendado");
    }

    // lista os eventos do mês com detalhes (descrição e convidados)
    private void imprimirListaEventos(int mes, int ano, List<Evento> eventos) {
        System.out.println("\n--- EVENTOS DE " + NOMES_MES[mes].toUpperCase() + "/" + ano + " ---");

        if (eventos.isEmpty()) {
            System.out.println("  Nenhum evento agendado neste mês.");
            return;
        }

        for (Evento e : eventos) {
            System.out.printf("%n  [%d] %s - %s%n", e.getId(), e.getTitulo(), SDF_DATA_HORA.format(e.getData()));
            if (e.getDescricao() != null && !e.getDescricao().isBlank()) {
                System.out.println("      " + e.getDescricao());
            }
            if (e.getConvidados().isEmpty()) {
                System.out.println("      Convidados: nenhum");
            } else {
                String nomes = e.getConvidados().stream()
                        .map(Usuario::getNome)
                        .collect(Collectors.joining(", "));
                System.out.println("      Convidados: " + nomes);
            }
        }
    }

    private void agendarEvento() {
        System.out.println("\n--- AGENDAR EVENTO ---");

        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        if (titulo.isBlank()) {
            System.out.println("[ERRO] Título obrigatório.");
            return;
        }

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Data e horário (dd/MM/yyyy HH:mm): ");
        String entradaData = scanner.nextLine().trim();
        Date data;
        try {
            SDF_DATA_HORA.setLenient(false);
            data = SDF_DATA_HORA.parse(entradaData);
        } catch (ParseException e) {
            System.out.println("[ERRO] Data/horário inválido. Use o formato dd/MM/yyyy HH:mm.");
            return;
        }

        System.out.println(controller.cadastrar(usuarioLogado, titulo, descricao, data));
    }

    private void convidarPessoas() {
        System.out.println("\n--- CONVIDAR PESSOAS PARA UM EVENTO ---");

        int[] mesAno = lerMesAno();
        int mes = mesAno[0], ano = mesAno[1];

        List<Evento> eventos = controller.listarPorMes(usuarioLogado, mes, ano);
        if (eventos.isEmpty()) {
            System.out.println("  Nenhum evento agendado nesse mês.");
            return;
        }

        System.out.println("\nEventos:");
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            System.out.printf("  %d. [%s] %s%n", i + 1, SDF_DATA_HORA.format(e.getData()), e.getTitulo());
        }
        System.out.println("  0. Cancelar");
        System.out.print("Escolha o evento: ");

        int escolhaEvento = lerOpcao(eventos.size());
        if (escolhaEvento == -1) return;

        Evento evento = eventos.get(escolhaEvento - 1);
        List<Usuario> convidaveis = controller.listarConvidaveis(usuarioLogado, evento);

        if (convidaveis.isEmpty()) {
            System.out.println("  Não há membros da sua EJ disponíveis para convidar para esse evento.");
            return;
        }

        System.out.println("\nMembros da sua Empresa Júnior:");
        for (int i = 0; i < convidaveis.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + convidaveis.get(i).getNome());
        }
        System.out.println("  0. Cancelar");
        System.out.print("Escolha quem convidar: ");

        int escolhaUsuario = lerOpcao(convidaveis.size());
        if (escolhaUsuario == -1) return;

        Usuario convidado = convidaveis.get(escolhaUsuario - 1);
        System.out.println(controller.convidar(usuarioLogado, evento, convidado));
    }

    // pede mês e ano, usando o mês/ano atual como padrão
    private int[] lerMesAno() {
        Calendar agora = Calendar.getInstance();
        int mesAtual = agora.get(Calendar.MONTH) + 1;
        int anoAtual = agora.get(Calendar.YEAR);

        System.out.printf("Mês (1-12) [clique enter para o mês atual]: ", mesAtual);
        String entradaMes = scanner.nextLine().trim();
        int mes = mesAtual;
        if (!entradaMes.isBlank()) {
            try {
                mes = Integer.parseInt(entradaMes);
                if (mes < 1 || mes > 12) {
                    System.out.println("[AVISO] Mês inválido. Usando mês atual.");
                    mes = mesAtual;
                }
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Valor inválido. Usando mês atual.");
            }
        }

        System.out.printf("Ano [clique enter para o ano atual]: ", anoAtual);
        String entradaAno = scanner.nextLine().trim();
        int ano = anoAtual;
        if (!entradaAno.isBlank()) {
            try {
                ano = Integer.parseInt(entradaAno);
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Valor inválido. Usando ano atual.");
            }
        }

        return new int[]{mes, ano};
    }

    private int lerOpcao(int max) {
        String entrada = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(entrada);
            if (valor == 0) return -1;
            if (valor < 1 || valor > max) {
                System.out.println("[ERRO] Opção inválida.");
                return -1;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Opção inválida.");
            return -1;
        }
    }
}