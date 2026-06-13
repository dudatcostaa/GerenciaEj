package view;

import controller.GastoController;
import controller.MetricasController;
import controller.PropostaController;
import model.Gasto;
import model.Proposta;
import model.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MetricasView {

    private MetricasController controller;
    private GastoController gastoController;
    private PropostaController propostaController;
    private Scanner scanner;
    private Usuario usuarioLogado;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public MetricasView(Scanner scanner, Usuario usuarioLogado) {
        this.controller = new MetricasController();
        this.gastoController = new GastoController();
        this.propostaController = new PropostaController();
        this.scanner = scanner;
        this.usuarioLogado = usuarioLogado;
    }

    public void iniciar() {
        boolean rodando = true;
        while (rodando) {
            System.out.println("\n========================================");
            System.out.println("     GERENCIA EJ - MÉTRICAS             ");
            System.out.println("========================================");
            System.out.println("1. Ver todas as métricas");
            System.out.println("2. Faturamento");
            System.out.println("3. Membros");
            System.out.println("4. Projetos");
            System.out.println("5. Leads");
            System.out.println("6. Gastos mensais");
            System.out.println("7. Propostas");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1": exibirTodasMetricas();  break;
                case "2": exibirFaturamento();    break;
                case "3": exibirMembros();        break;
                case "4": exibirProjetos();       break;
                case "5": exibirLeads();          break;
                case "6": menuGastos();           break;
                case "7": menuPropostas();        break;
                case "0": rodando = false;        break;
                default:
                    System.out.println("[ERRO] Opção inválida.");
            }
        }
    }

    // ─── todas juntas ──────────────────────────────────────────────────────────

    private void exibirTodasMetricas() {
        Calendar agora = Calendar.getInstance();
        int mes = agora.get(Calendar.MONTH) + 1;
        int ano = agora.get(Calendar.YEAR);

        cabecalho("PAINEL COMPLETO DE MÉTRICAS");
        exibirNomeEJ();
        separador();
        exibirFaturamento();
        separador();
        exibirMembros();
        separador();
        exibirProjetos();
        separador();
        exibirLeads();
        separador();
        exibirResumoGastos(mes, ano);
        separador();
        exibirResumoPropostas(mes, ano);
    }

    // ─── seções individuais automáticas ───────────────────────────────────────

    private void exibirNomeEJ() {
        String nome = controller.buscarNomeEJ(usuarioLogado.getId());
        System.out.println("  Empresa Junior : " + (nome != null ? nome : "não vinculada"));
    }

    private void exibirFaturamento() {
        cabecalho("FATURAMENTO");
        int ano = Calendar.getInstance().get(Calendar.YEAR);
        System.out.printf("  Faturamento %d  : R$ %,.2f%n", ano, controller.buscarFaturamentoAno());
        System.out.printf("  Faturamento total : R$ %,.2f%n", controller.buscarFaturamentoTotal());
    }

    private void exibirMembros() {
        cabecalho("MEMBROS");
        System.out.println("  Total de membros : " + controller.buscarQuantidadeMembros());
    }

    private void exibirProjetos() {
        cabecalho("PROJETOS");
        Map<String, Integer> projetos = controller.buscarProjetosPorStatus();
        int total = projetos.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println("  Em planejamento : " + projetos.getOrDefault("EM_PLANEJAMENTO", 0));
        System.out.println("  Em execução     : " + projetos.getOrDefault("EM_EXECUCAO", 0));
        System.out.println("  Finalizados     : " + projetos.getOrDefault("FINALIZADO", 0));
        System.out.println("  Total           : " + total);
    }

    private void exibirLeads() {
        cabecalho("LEADS");
        Map<String, Integer> leads = controller.buscarLeadsPorStatus();
        int total = leads.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println("  Prospecção    : " + leads.getOrDefault("PROSPECCAO", 0));
        System.out.println("  Negociação    : " + leads.getOrDefault("NEGOCIACAO", 0));
        System.out.println("  Fechados      : " + leads.getOrDefault("FECHADO", 0));
        System.out.println("  Perdidos      : " + leads.getOrDefault("PERDIDO", 0));
        System.out.println("  Total         : " + total);
        System.out.printf( "  Tx. conversão : %.1f%%%n", controller.buscarTaxaConversao());
    }

    // ─── resumos usados no painel completo ─────────────────────────────────────

    private void exibirResumoGastos(int mes, int ano) {
        cabecalho("GASTOS — " + nomeMes(mes) + "/" + ano);
        double total = gastoController.totalPorMes(mes, ano);
        int qtd = gastoController.listarPorMes(mes, ano).size();
        System.out.println("  Lançamentos : " + qtd);
        System.out.printf( "  Total       : R$ %,.2f%n", total);
    }

    private void exibirResumoPropostas(int mes, int ano) {
        cabecalho("PROPOSTAS — " + nomeMes(mes) + "/" + ano);
        int qtd = propostaController.contarPorMes(mes, ano);
        System.out.println("  Enviadas no mês : " + qtd);
    }

    // ─── menu de gastos ────────────────────────────────────────────────────────

    private void menuGastos() {
        boolean rodando = true;
        while (rodando) {
            System.out.println("\n--- GASTOS MENSAIS ---");
            System.out.println("1. Adicionar gasto");
            System.out.println("2. Ver gastos do mês");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            switch (scanner.nextLine()) {
                case "1": adicionarGasto();  break;
                case "2": listarGastos();    break;
                case "0": rodando = false;   break;
                default: System.out.println("[ERRO] Opção inválida.");
            }
        }
    }

    private void adicionarGasto() {
        System.out.println("\n--- ADICIONAR GASTO ---");

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        if (descricao.isBlank()) {
            System.out.println("[ERRO] Descrição obrigatória.");
            return;
        }

        Double valor = lerValor("Valor (ex: 150.00): ");
        if (valor == null) return;

        Date data = lerData("Data (dd/MM/yyyy) [ENTER para hoje]: ");
        if (data == null) data = new Date();

        Gasto criado = gastoController.cadastrar(descricao, valor, data);
        if (criado != null) {
            System.out.printf("[SUCESSO] Gasto \"%s\" de R$ %,.2f registrado.%n", criado.getDescricao(), criado.getValor());
        } else {
            System.out.println("[ERRO] Não foi possível registrar o gasto.");
        }
    }

    private void listarGastos() {
        int[] mesAno = lerMesAno();
        int mes = mesAno[0], ano = mesAno[1];

        List<Gasto> gastos = gastoController.listarPorMes(mes, ano);
        double total = gastoController.totalPorMes(mes, ano);

        System.out.println("\n--- GASTOS DE " + nomeMes(mes).toUpperCase() + "/" + ano + " ---");

        if (gastos.isEmpty()) {
            System.out.println("  Nenhum gasto registrado.");
        } else {
            gastos.forEach(g -> System.out.println("  " + g));
        }

        System.out.printf("%n  TOTAL : R$ %,.2f%n", total);
    }

    // ─── menu de propostas ─────────────────────────────────────────────────────

    private void menuPropostas() {
        boolean rodando = true;
        while (rodando) {
            System.out.println("\n--- PROPOSTAS ---");
            System.out.println("1. Registrar proposta");
            System.out.println("2. Ver propostas do mês");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            switch (scanner.nextLine()) {
                case "1": adicionarProposta(); break;
                case "2": listarPropostas();   break;
                case "0": rodando = false;     break;
                default: System.out.println("[ERRO] Opção inválida.");
            }
        }
    }

    private void adicionarProposta() {
        System.out.println("\n--- REGISTRAR PROPOSTA ---");

        System.out.print("Nome do cliente: ");
        String nomeCliente = scanner.nextLine();
        if (nomeCliente.isBlank()) {
            System.out.println("[ERRO] Nome do cliente obrigatório.");
            return;
        }

        Double valor = lerValor("Valor proposto (ex: 3000.00): ");
        if (valor == null) return;

        Date data = lerData("Data de envio (dd/MM/yyyy) [ENTER para hoje]: ");
        if (data == null) data = new Date();

        Proposta criada = propostaController.cadastrar(nomeCliente, valor, data);
        if (criada != null) {
            System.out.printf("[SUCESSO] Proposta para \"%s\" de R$ %,.2f registrada.%n",
                    criada.getNomeCliente(), criada.getValor());
        } else {
            System.out.println("[ERRO] Não foi possível registrar a proposta.");
        }
    }

    private void listarPropostas() {
        int[] mesAno = lerMesAno();
        int mes = mesAno[0], ano = mesAno[1];

        List<Proposta> propostas = propostaController.listarPorMes(mes, ano);
        int total = propostaController.contarPorMes(mes, ano);

        System.out.println("\n--- PROPOSTAS DE " + nomeMes(mes).toUpperCase() + "/" + ano + " ---");

        if (propostas.isEmpty()) {
            System.out.println("  Nenhuma proposta registrada.");
        } else {
            propostas.forEach(p -> System.out.println("  " + p));
        }

        System.out.println("\n  Total de propostas : " + total);
    }

    // ─── helpers ───────────────────────────────────────────────────────────────

    private Double lerValor(String prompt) {
        System.out.print(prompt);
        try {
            double v = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            if (v < 0) {
                System.out.println("[ERRO] Valor não pode ser negativo.");
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Valor inválido.");
            return null;
        }
    }

    // retorna null se o usuário pressionar ENTER (sinaliza "usar hoje")
    private Date lerData(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        if (entrada.isBlank()) return null;
        try {
            SDF.setLenient(false);
            return SDF.parse(entrada);
        } catch (ParseException e) {
            System.out.println("[AVISO] Data inválida — usando data de hoje.");
            return null;
        }
    }

    // pede mês e ano para filtrar, usa o mês atual como padrão
    private int[] lerMesAno() {
        Calendar agora = Calendar.getInstance();
        int mesAtual = agora.get(Calendar.MONTH) + 1;
        int anoAtual = agora.get(Calendar.YEAR);

        System.out.printf("Mês (1-12) [ENTER para %d]: ", mesAtual);
        String entradaMes = scanner.nextLine().trim();
        int mes = entradaMes.isBlank() ? mesAtual : Integer.parseInt(entradaMes);

        System.out.printf("Ano [ENTER para %d]: ", anoAtual);
        String entradaAno = scanner.nextLine().trim();
        int ano = entradaAno.isBlank() ? anoAtual : Integer.parseInt(entradaAno);

        return new int[]{mes, ano};
    }

    private String nomeMes(int mes) {
        String[] nomes = {"", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                               "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return (mes >= 1 && mes <= 12) ? nomes[mes] : String.valueOf(mes);
    }

    private void cabecalho(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
    }

    private void separador() {
        System.out.println("  " + "-".repeat(36));
    }
}