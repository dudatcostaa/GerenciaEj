package view;

import controller.RelatorioController;
import model.Usuario;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class RelatorioView {

    private RelatorioController controller;
    private Scanner scanner;
    private Usuario usuarioLogado;

    public RelatorioView(Scanner scanner, Usuario usuarioLogado) {
        this.controller = new RelatorioController();
        this.scanner = scanner;
        this.usuarioLogado = usuarioLogado;
    }

    public void iniciar() {
        System.out.println("\n========================================");
        System.out.println("     GERENCIA EJ - RELATÓRIO SEMANAL    ");
        System.out.println("========================================");

        int semanaAtual = controller.semanaAtual();
        int anoAtual    = controller.anoAtual();

        // semana
        System.out.printf("Número da semana (1-52) [ENTER para semana atual: %d]: ", semanaAtual);
        String entradaSemana = scanner.nextLine().trim();
        int semana = semanaAtual;
        if (!entradaSemana.isBlank()) {
            try {
                semana = Integer.parseInt(entradaSemana);
                if (semana < 1 || semana > 52) {
                    System.out.println("[ERRO] Semana inválida. Usando semana atual.");
                    semana = semanaAtual;
                }
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Valor inválido. Usando semana atual.");
            }
        }

        // ano
        System.out.printf("Ano [ENTER para %d]: ", anoAtual);
        String entradaAno = scanner.nextLine().trim();
        int ano = anoAtual;
        if (!entradaAno.isBlank()) {
            try {
                ano = Integer.parseInt(entradaAno);
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Ano inválido. Usando ano atual.");
            }
        }

        // mostra o intervalo calculado antes de confirmar
        Date[] intervalo = controller.calcularIntervalo(semana, ano);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("%nPeríodo selecionado: %s a %s (Semana %d/%d)%n",
                sdf.format(intervalo[0]), sdf.format(intervalo[1]), semana, ano);
        System.out.print("Confirmar geração do relatório? (S/N): ");
        String confirmacao = scanner.nextLine().trim();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Geração cancelada.");
            return;
        }

        System.out.println("\nGerando relatório...");
        try {
            String caminho = controller.gerarRelatorio(semana, ano, usuarioLogado);
            System.out.println("[SUCESSO] Relatório gerado em: " + caminho);
        } catch (IOException e) {
            System.out.println("[ERRO] Não foi possível gerar o relatório: " + e.getMessage());
        }
    }
}