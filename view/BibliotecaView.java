package view;
import java.util.Scanner;

import model.Arquivo;
import model.Cargo;
import model.Usuario;

import java.util.List;

public class BibliotecaView {
    private Scanner scanner = new Scanner(System.in);

    // exibe o menu principal da biblioteca e retorna a opção escolhida
    public int mostrarMenu(String nomeUsuario, Cargo cargo) {
        System.out.println("\n--- GERENCIA EJ: BIBLIOTECA ---");
        System.out.println("LOGADO: " + nomeUsuario + " (" + cargo + ")"); // temporário, para testar
        System.out.println("1. Fazer Upload");
        System.out.println("2. Ver e Abrir Arquivos");
        System.out.println("3. Excluir Arquivo");
        System.out.println("0. Sair");
        System.out.print("Opção: ");
        return Integer.parseInt(scanner.nextLine());
    }

    // exibe a lista de arquivos com o nome do autor de cada um
    public void exibirLista(List<Arquivo> arquivos, List<Usuario> usuarios) {
        System.out.println("\n--- ARQUIVOS NA BIBLIOTECA ---");
        for (int i = 0; i < arquivos.size(); i++) {
            Arquivo arq = arquivos.get(i);
            // busca o nome do autor pelo id e caso o usuário tenha sido excluído exibe conta excluida
            String nomeAutor = usuarios.stream()
                .filter(u -> u.getId().equals(arq.getAutorId()))
                .map(Usuario::getNome)
                .findFirst()
                .orElse("CONTA EXCLUÍDA");
            System.out.printf("[%d] %-30s | Autor: %s\n", i, arq.getNome(), nomeAutor);
        }
    }

    // pede que o usuário escolha um arquivo pelo indice dele na lista
    public int pedirIndice(String acao) {
        System.out.print("\nNúmero para " + acao + " (ou -1 para cancelar): ");
        return Integer.parseInt(scanner.nextLine());
    }

    // exibe uma mensagem de retorno ao usuário
    public void mostrarMensagem(String msg) {
        System.out.println("-> " + msg);
    }
}
