package view;

import controller.BibliotecaController;
import controller.KambanController;
import controller.LeadController;
import controller.SolicitacaoController;
import controller.UsuarioController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import model.Arquivo;
import model.BibliotecaService;
import model.Cargo;
import model.EmpresaJunior;
import model.Projeto;
import model.SolicitacaoEJ;
import model.StatusProjeto;
import model.Usuario;

public class UsuarioView {
    private UsuarioController controller = new UsuarioController();
    private Scanner leitor = new Scanner(System.in);

    // Mocks originais do seu trio centralizados temporariamente na sessão da View
    private List<Projeto> mockProjetos = new ArrayList<>();
    private BibliotecaService biblioService = new BibliotecaService();
    private List<Long> idsAtivos = Arrays.asList(1L, 2L, 3L);
    private controller.CandidaturaController candController = new controller.CandidaturaController();

    public UsuarioView() {
        // Inicializa dados do kamban que estavam na Main da sua amiga
        mockProjetos.add(new Projeto(1L, "Teste A", "teste", StatusProjeto.EM_PLANEJAMENTO));
        mockProjetos.add(new Projeto(2L, "Teste B", "teste", StatusProjeto.EM_PLANEJAMENTO));
        mockProjetos.add(new Projeto(3L, "Teste C", "teste", StatusProjeto.EM_EXECUCAO));
        mockProjetos.add(new Projeto(4L, "Teste D", "teste", StatusProjeto.FINALIZADO));

        // Inicializa arquivos mock da biblioteca da sua amiga
        biblioService.adicionarArquivo(new Arquivo(101L, "Teste1", "/home/sofigazolla/formatacao.py", 2L));
        biblioService.adicionarArquivo(new Arquivo(102L, "Teste2", "/home/sofigazolla/resolucao_p1_2015.pdf", 1L));
        biblioService.adicionarArquivo(new Arquivo(103L, "Teste3", "/home/sofigazolla/Sapixels-diretores.png", 3L));
        biblioService.adicionarArquivo(new Arquivo(500L, "Teste4", "/home/sofigazolla/planodefundo.jpg", 99L));
    }

    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("       GERENCIA EJ - TELA INICIAL       ");
            System.out.println("========================================");
            System.out.println("1. Criar uma conta");
            System.out.println("2. Fazer Login");
            System.out.println("3. Solicitar exclusão da conta");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = leitor.nextLine();

            if (opcao.equals("1")) {
                executarFluxoCadastro();
            } else if (opcao.equals("2")) {
                executarFluxoLogin();
            } else if (opcao.equals("3")) {
                executarFluxoExclusao();
            } else if (opcao.equals("0")) {
                System.out.println("Encerrando o sistema...");
                break;
            } else {
                System.out.println("[ERRO] Opção inválida!");
            }
        }
    }

    private void executarFluxoLogin() {
        System.out.println("\n--- Gerencia EJ: página de acesso ---");
        System.out.print("Digite seu e-mail: ");
        String emailInformado = leitor.nextLine();
        System.out.print("Digite sua senha: ");
        String senhaInformada = leitor.nextLine();

        Usuario usuarioLogado = controller.autenticarUsuario(emailInformado, senhaInformada);

        if (usuarioLogado != null) {
            System.out.println("\n[SUCESSO] Login realizado!");
            System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + " [" + usuarioLogado.getCargo() + "]");

            // Abre o menu interno do sistema passando o objeto do usuário que logou
            exibirMenuInternoEJ(usuarioLogado);
        } else {
            System.out.println("\n[ERRO] E-mail ou senha incorretos");
        }
    }

    private void executarFluxoCadastro() {
        System.out.println("\n--- Tela de Cadastro ---");
        System.out.print("Nome: ");
        String nome = leitor.nextLine();
        System.out.print("E-mail: ");
        String email = leitor.nextLine();
        System.out.print("Senha: ");
        String senha = leitor.nextLine();
        System.out.print("Cargo (1 para MEMBRO, 2 para DIRETOR): ");
        String cargoStr = leitor.nextLine();

        if (nome.isBlank() || email.isBlank() || senha.isBlank() || cargoStr.isBlank()) {
            System.out.println("\n[AVISO] Todos os campos são obrigatórios!");
            return;
        }

        Cargo cargoSelecionado = cargoStr.equals("2") ? Cargo.DIRETOR : Cargo.MEMBRO;

        if (controller.emailExiste(email)) {
            System.out.println("\n[ERRO] Email em uso, insira outro email.");
        } else {
            Usuario novoUsuario = controller.cadastrarUsuario(nome, email, senha, cargoSelecionado);
            System.out.println("\n[SUCESSO] Cadastro realizado com sucesso!");
        }
    }

    private void exibirMenuInternoEJ(Usuario usuarioLogado) {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("      GERENCIA EJ - MENU INTERNO        ");
            System.out.println("========================================");
            System.out.println("1. Acessar Quadros Kanban");
            System.out.println("2. Acessar Biblioteca");
            System.out.println("3. Acessar Módulo de Leads"); // NOVO
            System.out.println("4. Painel Administrativo (Aprovar EJs)"); // NOVO
            System.out.println("5. Buscar Empresas Juniores");
            System.out.println("6. Painel do Diretor (Notificações de Ingresso)");
            System.out.println("0. Fazer Logout");
            System.out.print("Escolha uma opção: ");

            String entrada = leitor.nextLine();

            if (entrada.equals("1")) {
                KambanView kView = new KambanView(leitor);
                KambanController kController = new KambanController(kView);
                kController.iniciar();
            } else if (entrada.equals("2")) {
                BibliotecaView bView = new BibliotecaView();
                BibliotecaController bController = new BibliotecaController(
                        biblioService, bView, usuarioLogado,
                        controller.getUsuariosDoSistema(), idsAtivos);
                bController.iniciar();
            } else if (entrada.equals("3")) { // NOVO
                LeadView leadView = new LeadView();
                LeadController leadController = new LeadController(leadView);
                leadController.iniciarModulo();
            } else if (entrada.equals("4")) { // NOVO
                executarModuloAdmin();
            } else if (entrada.equals("5")) {
                EmpresaJuniorView ejView = new EmpresaJuniorView();
                ejView.exibirMenuBusca(usuarioLogado);
            } else if (entrada.equals("6")) {
                if (usuarioLogado.getCargo() == model.Cargo.DIRETOR) {
                    executarPainelDiretor(usuarioLogado); // <--- ADICIONE O PARAMETRO AQUI
                } else {
                    System.out.println(
                            "\n[ERRO] Acesso negado! Apenas usuários com cargo de DIRETOR podem acessar este painel. (RN01)");
                }
            } else if (entrada.equals("0")) {
                System.out.println("Efetuando logout... Voltando à tela inicial.");
                break;
            } else {
                System.out.println("[ERRO] Opção inválida!");
            }
        }
    }

    // NOVO — copiado da Main2, mas como método privado da view
    private void executarModuloAdmin() {
        System.out.println("\n=== Iniciando Módulo Administrativo ===");

        // Dados de teste mockados (igual ao que estava na Main2)
        Usuario usuarioTeste = new Usuario(1L, "Lorena", "lorena@ufsc.br", "senha123", Cargo.MEMBRO);
        EmpresaJunior ejTeste = new EmpresaJunior(101L, "Tech Solutions EJ", "12.345.678/0001-99");
        SolicitacaoEJ solicitacao = new SolicitacaoEJ(501L, "estatuto_tech.pdf", usuarioTeste, ejTeste);

        AdminView adminView = new AdminView();
        SolicitacaoController solController = new SolicitacaoController(adminView);
        solController.avaliarSolicitacao(solicitacao);

        System.out.println("\n[Módulo Administrativo finalizado. Pressione ENTER para continuar]");
        leitor.nextLine();
    }

    private void executarFluxoExclusao() {
        System.out.print("\nTem certeza que deseja excluir uma conta? (Sim/Não): ");
        String confirmacao = leitor.nextLine();

        if (confirmacao.equalsIgnoreCase("Sim") || confirmacao.equalsIgnoreCase("S")) {
            System.out.print("Confirme o E-mail da conta a ser excluída: ");
            String emailConfirmar = leitor.nextLine();
            System.out.print("Confirme a Senha da conta: ");
            String senhaConfirmar = leitor.nextLine();

            boolean sucesso = controller.excluirUsuario(emailConfirmar, senhaConfirmar);

            if (sucesso) {
                System.out.println("\n[SUCESSO] Conta excluída com sucesso!");
            } else {
                System.out.println("\n[ERRO] Credenciais incorretas.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private void executarPainelDiretor(Usuario usuarioLogado) { // <--- RECEBE O PARAMETRO
        System.out.println("\n========================================");
        System.out.println("     PAINEL DO DIRETOR - NOTIFICAÇÕES   ");
        System.out.println("========================================");

        // Passa o ID do diretor para a busca filtrada
        List<String> pendentes = candController.obterNotificacoesPendentes(usuarioLogado.getId());

        if (pendentes.isEmpty()) {
            System.out.println("Não há nenhuma solicitação de ingresso pendente de outros usuários no momento.");
            return;
        }

        // ... resto do seu código igualzinho ...

        System.out.println("Solicitações aguardando sua decisão:");
        for (String solicitacao : pendentes) {
            System.out.println(solicitacao);
        }

        System.out.println("----------------------------------------");
        System.out.print("Digite o ID da solicitação que deseja avaliar (ou 0 para voltar): ");
        long idEscolhido = leitor.nextLong();
        leitor.nextLine(); // Limpa buffer

        if (idEscolhido != 0) {
            System.out.print("Deseja [1] APROVAR ou [2] RECUSAR? ");
            int decisao = leitor.nextInt();
            leitor.nextLine(); // Limpa buffer

            if (decisao == 1) {
                candController.responderSolicitacao(idEscolhido, true);
                System.out.println("\n[SUCESSO] Candidatura aprovada com sucesso!");
            } else if (decisao == 2) {
                candController.responderSolicitacao(idEscolhido, false);
                System.out.println("\n[AVISO] Candidatura recusada.");
            } else {
                System.out.println("\n[ERRO] Opção inválida.");
            }
        }
    }
}
