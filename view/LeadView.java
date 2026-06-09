package view;

import java.util.Scanner;
import java.util.List;
import model.Lead;
import model.StatusLead;

public class LeadView {
    private Scanner scanner = new Scanner(System.in);

    public void exibirMensagemBoasVindas() {
        System.out.println("\n=== Bem-vindo ao Módulo de Leads (MVC) ===");
    }

    public int exibirMenuEPegarOpcao() {
        System.out.println("\n--- MENU DE OPÇÕES ---");
        System.out.println("1. Cadastrar novo Lead");
        System.out.println("2. Visualizar Leads (Por Categoria)");
        System.out.println("3. Editar o status de um Lead");
        System.out.println("4. Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // limpar o buffer
        return opcao;
    }

    public String pedirNomeNovoLead() {
        System.out.print("\nDigite o nome do novo cliente: ");
        return scanner.nextLine();
    }

    public void exibirPainelLeads(StatusLead[] statusValidos, List<Lead> leads) {
        System.out.println("\n=== PAINEL DE LEADS POR CATEGORIA ===");
        if (leads.isEmpty()) {
            System.out.println("Nenhum lead cadastrado ainda.");
            System.out.println("=====================================\n");
            return;
        }

        for (StatusLead status : statusValidos) {
            System.out.println("\n-- [" + status + "] --");
            boolean temLead = false;

            for (Lead lead : leads) {
                if (lead.getStatusLead() == status) {
                    System.out.println("   " + lead.toString());
                    temLead = true;
                }
            }

            if (!temLead) {
                System.out.println("   (Vazio)");
            }
        }
        System.out.println("\n=====================================\n");
    }

    public Long pedirIdEdicao() {
        System.out.print("\nDigite o ID do Lead que você quer editar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        return id;
    }

    public int pedirNovoStatus() {
        System.out.println("Para qual status você quer mudar?");
        System.out.println("1 - PROSPECCAO");
        System.out.println("2 - NEGOCIACAO");
        System.out.println("3 - FECHADO");
        System.out.println("4 - PERDIDO");
        System.out.print("Digite o número do novo status: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

    public void exibirMensagemSucessoCriacao(String nome) {
        System.out.println("-> SUCESSO: '" + nome + "' adicionado ao funil de prospecção.");
    }

    public void exibirMensagemSucessoEdicao(String nome, String statusAntigo, String statusNovo) {
        System.out.println("-> ATUALIZAÇÃO: '" + nome + "' avançou de " + statusAntigo + " para " + statusNovo);
    }

    public void exibirErro(String mensagem) {
        System.out.println("-> ERRO: " + mensagem);
    }
}