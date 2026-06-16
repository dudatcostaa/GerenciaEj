package view;

import controller.CandidaturaController;
import controller.EmpresaJuniorController; // NOVO
import java.util.List;
import java.util.Scanner; // NOVO
import model.EmpresaJunior;
import model.Usuario;
import model.strategy.BuscaEJStrategy;
import model.strategy.BuscarPorNomeStrategy;
import model.strategy.BuscarTodasStrategy;

public class EmpresaJuniorView {
    private EmpresaJuniorController controller;
    private CandidaturaController candidaturaController; // NOVO
    private Scanner scanner;

    public EmpresaJuniorView() {
        this.controller = new EmpresaJuniorController();
        this.candidaturaController = new CandidaturaController(); // NOVO
        this.scanner = new Scanner(System.in);
    }

    // Alterado para receber o usuário logado do sistema
    public void exibirMenuBusca(Usuario usuarioLogado) {
        System.out.println("\n--- BUSCAR EMPRESAS JUNIORES ---");
        System.out.println("1. Listar todas as EJs");
        System.out.println("2. Buscar por nome específico");
        System.out.print("Escolha uma opção: ");
        
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer

        BuscaEJStrategy estrategia = null;
        String termo = "";

        if (opcao == 1) {
            estrategia = new BuscarTodasStrategy();
        } else if (opcao == 2) {
            System.out.print("Digite o nome ou parte do nome da EJ: ");
            termo = scanner.nextLine();
            estrategia = new BuscarPorNomeStrategy();
        } else {
            System.out.println("Opção inválida!");
            return;
        }

        List<EmpresaJunior> resultados = controller.realizarBusca(estrategia, termo);
        exibirResultados(resultados);

        // FLUXO DO UC04: Só oferece a candidatura se a busca trouxe algum resultado
        if (!resultados.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.print("Deseja solicitar entrada em alguma dessas EJs? (Digite o ID da empresa ou 0 para voltar): ");
            long idEjEscolhido = scanner.nextLong();
            scanner.nextLine(); // Limpa o buffer

            if (idEjEscolhido != 0) {
                // Dispara o controlador que aplica a RN06 e executa o RF19
                boolean sucesso = candidaturaController.processarSolicitacaoIngresso(usuarioLogado.getId(), idEjEscolhido);

                if (sucesso) {
                    System.out.println("\n[SUCESSO] Solicitação enviada! Aguarde a aprovação de um Diretor. (RN01/RF19)");
                } else {
                    System.out.println("\n[ERRO] Você não pode enviar esta solicitação. Regra de Negócio Violada: O usuário já participa de uma Empresa Júnior ou possui solicitação ativa. (RN06)");
                }
            }
        }
    }

    private void exibirResultados(List<EmpresaJunior> empresas) {
        if (empresas.isEmpty()) {
            System.out.println("Nenhuma Empresa Júnior encontrada.");
            return;
        }

        System.out.println("\n--- RESULTADO DA BUSCA ---");
        for (EmpresaJunior ej : empresas) {
            System.out.println("ID: " + ej.getId() + " | Nome: " + ej.getNome() + " | CNPJ: " + ej.getCnpj());
        }
    }
}