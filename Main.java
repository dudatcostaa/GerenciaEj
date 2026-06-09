import java.util.*;

import controller.BibliotecaController;
import controller.KambanController;
import model.Arquivo;
import model.BibliotecaService;
import model.Cargo;
import model.Projeto;
import model.StatusProjeto;
import model.Usuario;
import view.BibliotecaView;
import view.KambanView;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // dados do kamban
        List<Projeto> mockProjetos = new ArrayList<>();
        mockProjetos.add(new Projeto(1L, "Teste A", "teste", StatusProjeto.EM_PLANEJAMENTO));
        mockProjetos.add(new Projeto(2L, "Teste B", "teste", StatusProjeto.EM_PLANEJAMENTO));
        mockProjetos.add(new Projeto(3L, "Teste C", "teste", StatusProjeto.EM_EXECUCAO));
        mockProjetos.add(new Projeto(4L, "Teste D", "teste", StatusProjeto.FINALIZADO));

        BibliotecaService biblioService = new BibliotecaService();
        
        // usuários mock
        Usuario dirMaria = new Usuario(1L, "Maria", Cargo.DIRETOR);
        Usuario memJoao = new Usuario(2L, "João", Cargo.MEMBRO);
        Usuario memAna = new Usuario(3L, "Ana", Cargo.MEMBRO);
        
        List<Usuario> usuariosDoSistema = Arrays.asList(dirMaria, memJoao, memAna);
        List<Long> idsAtivos = Arrays.asList(1L, 2L, 3L);

        // arquivos mock (do meu notebook, para realmente testar visualização)
        biblioService.adicionarArquivo(new Arquivo(101L, "Teste1", "/home/sofigazolla/formatacao.py", 2L));
        biblioService.adicionarArquivo(new Arquivo(102L, "Teste2", "/home/sofigazolla/resolucao_p1_2015.pdf", 1L));
        biblioService.adicionarArquivo(new Arquivo(103L, "Teste3", "/home/sofigazolla/Sapixels-diretores.png", 3L));
        biblioService.adicionarArquivo(new Arquivo(500L, "Teste4", "/home/sofigazolla/planodefundo.jpg", 99L));

        // usuario para simulação
        Usuario usuarioLogado = dirMaria;

        // --- 3. LOOP DO MENU INTEGRADOR ---
        while (true) {
            System.out.println("\n==============================");
            System.out.println("      GERENCIA EJ   ");
            System.out.println("==============================");
            System.out.println("1. Acessar Quadros Kanban");
            System.out.println("2. Acessar Biblioteca");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String entrada = scanner.nextLine();
            
            if (entrada.equals("1")) {
                // inicia kamban
                KambanView kView = new KambanView(scanner);
                KambanController kController = new KambanController(kView, mockProjetos);
                kController.iniciar();
            } 
            else if (entrada.equals("2")) {
                // inicia biblioteca
                BibliotecaView bView = new BibliotecaView();
                BibliotecaController bController = new BibliotecaController(
                    biblioService, 
                    bView, 
                    usuarioLogado, 
                    usuariosDoSistema, 
                    idsAtivos
                );
                bController.iniciar();
            } 
            else if (entrada.equals("0")) {
                System.out.println("Encerrando o sistema");
                break;
            } 
            else {
                System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }
}
