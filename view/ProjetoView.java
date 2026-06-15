package view;

import controller.ProjetoController;
import model.Projeto;
import model.StatusProjeto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ProjetoView {

    private ProjetoController controller;
    private Scanner scanner;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public ProjetoView(Scanner scanner) {
        this.controller = new ProjetoController();
        this.scanner = scanner;
    }

    // ponto de entrada do módulo
    public void iniciar() {
        System.out.println("\n========================================");
        System.out.println("       GERENCIA EJ - PROJETOS           ");
        System.out.println("========================================");

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n1. Cadastrar projeto");
            System.out.println("2. Listar projetos");
            System.out.println("3. Editar projeto");
            System.out.println("4. Excluir projeto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1": cadastrar();  break;
                case "2": listar();     break;
                case "3": editar();     break;
                case "4": excluir();    break;
                case "0": rodando = false; break;
                default:
                    System.out.println("[ERRO] Opção inválida.");
            }
        }
    }

    // ─── cadastrar ─────────────────────────────────────────────────────────────

    private void cadastrar() {
        System.out.println("\n--- CADASTRAR PROJETO ---");

        String nome = lerCampoObrigatorio("Nome do projeto: ");
        if (nome == null) return;

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        StatusProjeto status = lerStatus();
        if (status == null) return;

        Double valor = lerValor("Valor do projeto (ex: 1500.00): ");
        if (valor == null) return;

        Projeto criado = controller.cadastrar(nome, descricao, status, valor);

        if (criado != null) {
            System.out.printf("[SUCESSO] Projeto \"%s\" cadastrado com ID %d (Valor: R$ %.2f).%n",
                    criado.getNome(), criado.getId(), criado.getValor());
        } else {
            System.out.println("[ERRO] Não foi possível cadastrar o projeto.");
        }
    }

    // ─── listar ────────────────────────────────────────────────────────────────

    private void listar() {
        List<Projeto> projetos = controller.listarTodos();

        System.out.println("\n--- LISTA DE PROJETOS ---");

        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
            return;
        }

        System.out.printf("%-6s %-28s %-20s %-12s %-12s%n", "ID", "Nome", "Status", "Início", "Valor (R$)");
        System.out.println("-".repeat(82));

        for (Projeto p : projetos) {
            String dataStr = p.getDataInicio() != null ? SDF.format(p.getDataInicio()) : "-";
            System.out.printf("%-6d %-28s %-20s %-12s %,.2f%n",
                    p.getId(), p.getNome(), p.getStatus(), dataStr, p.getValor());
        }
    }

    // ─── editar ────────────────────────────────────────────────────────────────

    private void editar() {
        listar();

        if (controller.listarTodos().isEmpty()) return;

        System.out.print("\nID do projeto a editar: ");
        Long id = lerLong();
        if (id == null) return;

        // busca o projeto atual para mostrar valores existentes como referência
        Projeto atual = controller.listarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst().orElse(null);

        if (atual == null) {
            System.out.println("[ERRO] Projeto não encontrado.");
            return;
        }

        System.out.println("\nDeixe em branco para manter o valor atual.");

        // nome
        System.out.print("Novo nome [" + atual.getNome() + "]: ");
        String novoNome = scanner.nextLine();
        if (novoNome.isBlank()) novoNome = atual.getNome();

        // descrição
        System.out.print("Nova descrição [" + atual.getDescricao() + "]: ");
        String novaDescricao = scanner.nextLine();
        if (novaDescricao.isBlank()) novaDescricao = atual.getDescricao();

        // status
        System.out.println("Novo status [atual: " + atual.getStatus() + "] (ENTER para manter):");
        imprimirOpcoesStatus();
        System.out.print("Escolha: ");
        String entradaStatus = scanner.nextLine();
        StatusProjeto novoStatus = atual.getStatus();
        if (!entradaStatus.isBlank()) {
            StatusProjeto lido = parsearStatus(entradaStatus);
            if (lido != null) novoStatus = lido;
            else System.out.println("[AVISO] Status inválido — mantido o atual.");
        }

        // data de início
        String dataAtualStr = atual.getDataInicio() != null ? SDF.format(atual.getDataInicio()) : "-";
        System.out.print("Nova data de início (dd/MM/yyyy) [" + dataAtualStr + "]: ");
        String entradaData = scanner.nextLine();
        Date novaData = atual.getDataInicio();
        if (!entradaData.isBlank()) {
            Date lida = parsearData(entradaData);
            if (lida != null) novaData = lida;
            else System.out.println("[AVISO] Data inválida — mantida a atual.");
        }

        // valor
        System.out.printf("Novo valor (ex: 1500.00) [atual: R$ %.2f]: ", atual.getValor());
        String entradaValor = scanner.nextLine();
        double novoValor = atual.getValor();
        if (!entradaValor.isBlank()) {
            try {
                novoValor = Double.parseDouble(entradaValor.trim().replace(",", "."));
                if (novoValor < 0) {
                    System.out.println("[AVISO] Valor negativo não permitido — mantido o atual.");
                    novoValor = atual.getValor();
                }
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Valor inválido — mantido o atual.");
            }
        }

        boolean ok = controller.editar(id, novoNome, novaDescricao, novoStatus, novaData, novoValor);

        if (ok) {
            System.out.println("[SUCESSO] Projeto atualizado com sucesso.");
        } else {
            System.out.println("[ERRO] Não foi possível atualizar o projeto.");
        }
    }

    // ─── excluir ───────────────────────────────────────────────────────────────

    private void excluir() {
        listar();

        if (controller.listarTodos().isEmpty()) return;

        System.out.print("\nID do projeto a excluir: ");
        Long id = lerLong();
        if (id == null) return;

        System.out.print("Tem certeza? Isso apagará o quadro e todas as tarefas. (S/N): ");
        String confirmacao = scanner.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        boolean ok = controller.excluir(id);

        if (ok) {
            System.out.println("[SUCESSO] Projeto excluído com sucesso.");
        } else {
            System.out.println("[ERRO] Projeto não encontrado ou erro ao excluir.");
        }
    }

    // ─── helpers ───────────────────────────────────────────────────────────────

    private StatusProjeto lerStatus() {
        imprimirOpcoesStatus();
        System.out.print("Escolha: ");
        return parsearStatus(scanner.nextLine());
    }

    private void imprimirOpcoesStatus() {
        System.out.println("  1 - EM_PLANEJAMENTO");
        System.out.println("  2 - EM_EXECUCAO");
        System.out.println("  3 - FINALIZADO");
    }

    private StatusProjeto parsearStatus(String entrada) {
        switch (entrada.trim()) {
            case "1": return StatusProjeto.EM_PLANEJAMENTO;
            case "2": return StatusProjeto.EM_EXECUCAO;
            case "3": return StatusProjeto.FINALIZADO;
            default:
                System.out.println("[ERRO] Opção de status inválida.");
                return null;
        }
    }

    private String lerCampoObrigatorio(String prompt) {
        System.out.print(prompt);
        String valor = scanner.nextLine();
        if (valor.isBlank()) {
            System.out.println("[ERRO] Campo obrigatório.");
            return null;
        }
        return valor;
    }

    private Long lerLong() {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] ID inválido.");
            return null;
        }
    }

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
            System.out.println("[ERRO] Valor inválido. Use o formato: 1500.00");
            return null;
        }
    }

    private Date parsearData(String texto) {
        try {
            SDF.setLenient(false);
            return SDF.parse(texto.trim());
        } catch (ParseException e) {
            return null;
        }
    }
}