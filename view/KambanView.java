package view;

import java.util.List;
import java.util.Scanner;
import model.Projeto;
import model.QuadroKamban;
import model.StatusTarefa;

public class KambanView {
    private Scanner scanner;

    // construtor
    public KambanView(Scanner scanner) {
        this.scanner = scanner;
    }

    // exibe o menu principal e retorna a opção escolhida pelo usuário
    public int mostrarMenuPrincipal() {
        System.out.println("\nQuadros Kamban");
        System.out.println("1. Listar Projetos");
        System.out.println("2. Acessar Kamban");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");
        return lerInteiro();
    }

    // lista todos os projetos para que o usuário possa escolher
    public void listarProjetos(List<Projeto> projetos) {
        System.out.println("\n--- LISTA DE PROJETOS ---");
        projetos.forEach(System.out::println);
    }

    // exibe os projetos para que o usuário possa escolher um
    public int escolherProjeto(List<Projeto> projetos) {
        System.out.println("\nEscolha o projeto para abrir o Kamban:");
        for (int i = 0; i < projetos.size(); i++) {
            System.out.println((i + 1) + ". " + projetos.get(i).getNome());
        }
        return lerInteiro() - 1;
    }

    // exibe o menu do kamban, para o usuário escolher o que fazer com a tarefa
    public int mostrarMenuKamban(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        System.out.println("1. Adicionar Tarefa");
        System.out.println("2. Mover Tarefa (avançar/voltar)");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");
        return lerInteiro();
    }

    public String pedirTituloTarefa() {
        System.out.print("Título da tarefa: ");
        return scanner.nextLine();
    }

    // pede o id da tarefa que o usuário quer mover, que é exibido em exibirQuadro()
    public long pedirIdTarefa() {
        System.out.print("ID da tarefa: ");
        return lerInteiro();
    }

    // pergunta se a tarefa deve avançar ou voltar no fluxo do quadro (padrão State)
    public int pedirDirecaoMovimento() {
        System.out.println("Mover tarefa para: 1.Avançar  2.Voltar");
        return lerInteiro();
    }

    // exibe os dados e as tarefas do kamban, usando as cores definidas no requisito nao funcional e no enum 
    public void exibirQuadro(QuadroKamban q) {
        System.out.println("\n" + q.getTitulo());
        for (StatusTarefa s : StatusTarefa.values()) {
            System.out.println(s.getCor() + s.name() + StatusTarefa.RESET + ":");
            q.getTarefas().stream()
                .filter(t -> t.getStatus() == s)
                .forEach(t -> System.out.println("  " + t));
        }
    }

    // exibe uma mensagem de retorno para o usuario
    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    // converte oq o usuário digitou para inteiro para que possamos ver qual opcao ele selecionou
    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
}