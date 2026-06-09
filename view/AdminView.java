package view;

import java.util.Scanner;

public class AdminView {
    private Scanner scanner = new Scanner(System.in);

    public void exibirDetalhesSolicitacao(String empresa, String solicitante, String documento) {
        System.out.println("=== PAINEL DO ADMINISTRADOR ===");
        System.out.println("Temos uma nova solicitação pendente!");
        System.out.println("Empresa: " + empresa);
        System.out.println("Solicitante: " + solicitante);
        System.out.println("Documento: " + documento);
        System.out.println("--------------------------------");
    }

    public int pedirDecisao() {
        System.out.println("Você deseja aprovar a criação desta EJ?");
        System.out.println("1 - SIM (Aprovar)");
        System.out.println("2 - NÃO (Reprovar e excluir arquivo)");
        System.out.print("Escolha: ");
        return scanner.nextInt();
    }

    public void exibirMensagemSucesso(String statusEJ, String nomeUsuario, String cargoUsuario) {
        System.out.println("-> SUCESSO! A Empresa Júnior foi APROVADA.");
        System.out.println("   Novo status da EJ: " + statusEJ);
        System.out.println("   Novo cargo da(o) " + nomeUsuario + ": " + cargoUsuario);
    }

    public void exibirMensagemReprovacao(String documentoUrl) {
        System.out.println("-> AVISO: A Empresa Júnior foi REPROVADA.");
        System.out.println("   [Sistema] O arquivo '" + documentoUrl + "' foi deletado permanentemente do servidor.");
    }
    
    public void exibirErro() {
        System.out.println("-> ERRO: Opção inválida.");
    }
}
