package view;

import controller.FuncionalidadeController;
import model.Funcionalidade;
import model.Usuario;

import java.util.List;
import java.util.Scanner;

public class FuncionalidadeView {

    private FuncionalidadeController controller;
    private Scanner scanner;
    private Usuario usuarioLogado;

    public FuncionalidadeView(Scanner scanner, Usuario usuarioLogado) {
        this.controller = new FuncionalidadeController();
        this.scanner = scanner;
        this.usuarioLogado = usuarioLogado;
    }

    public void iniciar() {
        boolean rodando = true;
        while (rodando) {
            System.out.println("\n========================================");
            System.out.println("  GERENCIA EJ - FERRAMENTAS DE TRABALHO  ");
            System.out.println("========================================");
            System.out.println("1. Ver minhas ferramentas");
            System.out.println("2. Adicionar ferramenta");
            System.out.println("3. Remover ferramenta");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            switch (scanner.nextLine()) {
                case "1": verSelecionadas(); break;
                case "2": adicionar();       break;
                case "3": remover();         break;
                case "0": rodando = false;   break;
                default:
                    System.out.println("[ERRO] Opção inválida.");
            }
        }
    }

    // ─── ver ───────────────────────────────────────────────────────────────────

    private void verSelecionadas() {
        List<Funcionalidade> selecionadas = controller.listarSelecionadas(usuarioLogado.getId());

        System.out.println("\n--- SUAS FERRAMENTAS ---");
        if (selecionadas.isEmpty()) {
            System.out.println("  Nenhuma ferramenta selecionada ainda.");
            System.out.println("  Use a opção \"Adicionar ferramenta\" para configurar seu menu.");
        } else {
            selecionadas.forEach(f -> System.out.println("  - " + f.getDescricao()));
        }
    }

    // ─── adicionar ─────────────────────────────────────────────────────────────

    private void adicionar() {
        List<Funcionalidade> disponiveis = controller.listarDisponiveis(usuarioLogado.getId(), usuarioLogado.getCargo());

        System.out.println("\n--- ADICIONAR FERRAMENTA ---");
        if (disponiveis.isEmpty()) {
            System.out.println("  Você já selecionou todas as ferramentas disponíveis.");
            return;
        }

        for (int i = 0; i < disponiveis.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + disponiveis.get(i).getDescricao());
        }
        System.out.println("  0. Cancelar");
        System.out.print("Escolha: ");

        int escolha = lerOpcao(disponiveis.size());
        if (escolha == -1) return;

        Funcionalidade selecionada = disponiveis.get(escolha - 1);
        System.out.println(controller.adicionar(usuarioLogado.getId(), selecionada));
    }

    // ─── remover ───────────────────────────────────────────────────────────────

    private void remover() {
        List<Funcionalidade> selecionadas = controller.listarSelecionadas(usuarioLogado.getId());

        System.out.println("\n--- REMOVER FERRAMENTA ---");
        if (selecionadas.isEmpty()) {
            System.out.println("  Nenhuma ferramenta selecionada para remover.");
            return;
        }

        for (int i = 0; i < selecionadas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + selecionadas.get(i).getDescricao());
        }
        System.out.println("  0. Cancelar");
        System.out.print("Escolha: ");

        int escolha = lerOpcao(selecionadas.size());
        if (escolha == -1) return;

        Funcionalidade selecionada = selecionadas.get(escolha - 1);
        System.out.println(controller.remover(usuarioLogado.getId(), selecionada));
    }

    // ─── helpers ───────────────────────────────────────────────────────────────

    // lê uma opção entre 1 e max; retorna -1 para "0" (cancelar) ou entrada inválida
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