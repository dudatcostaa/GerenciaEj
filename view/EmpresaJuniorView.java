package view;

import controller.EmpresaJuniorController;
import java.util.List;
import java.util.Scanner;
import model.EmpresaJunior;
import strategy.BuscaEJStrategy;
import strategy.BuscarPorNomeStrategy;
import strategy.BuscarTodasStrategy;

public class EmpresaJuniorView {
    private EmpresaJuniorController controller;
    private Scanner scanner;

    public EmpresaJuniorView() {
        this.controller = new EmpresaJuniorController();
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuBusca() {
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

        // Executa a busca usando a estratégia definida
        List<EmpresaJunior> resultados = controller.realizarBusca(estrategia, termo);
        exibirResultados(resultados);
    }

    private void exibirResultados(List<EmpresaJunior> empresas) {
        if (empresas.isEmpty()) {
            System.out.println("Nenhuma Empresa Júnior encontrada.");
            return;
        }

        System.out.println("\n--- RESULTADO DA BUSCA ---");
        for (EmpresaJunior ej : empresas) {
            // Ajuste os métodos de exibição de acordo com os getters da sua model EmpresaJunior
            System.out.println("ID: " + ej.getId() + " | Nome: " + ej.getNome() + " | CNPJ: " + ej.getCnpj());
        }
    }
}