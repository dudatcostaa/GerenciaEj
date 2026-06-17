package view;

import controller.BibliotecaController;
import controller.FuncionalidadeController;
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
import model.Funcionalidade;
import model.Projeto;
import model.SolicitacaoEJ;
import model.StatusProjeto;
import model.Usuario;

public class UsuarioView {
    private UsuarioController controller = new UsuarioController();
    private Scanner leitor = new Scanner(System.in);

    // Mocks antes do banco
    private List<Projeto> mockProjetos = new ArrayList<>();
    private BibliotecaService biblioService = new BibliotecaService();
    private List<Long> idsAtivos = Arrays.asList(1L, 2L, 3L);
    private controller.CandidaturaController candController = new controller.CandidaturaController();

    public UsuarioView() {
        mockProjetos.add(new Projeto(1L, "Teste A", "teste", StatusProjeto.EM_PLANEJAMENTO));
        mockProjetos.add(new Projeto(2L, "Teste B", "teste", StatusProjeto.EM_PLANEJAMENTO));
        mockProjetos.add(new Projeto(3L, "Teste C", "teste", StatusProjeto.EM_EXECUCAO));
        mockProjetos.add(new Projeto(4L, "Teste D", "teste", StatusProjeto.FINALIZADO));
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

        // verificação especial para o admin
        if (emailInformado.equals("admin@gerenciaej.com") && senhaInformada.equals("admin123")) {
            System.out.println("\n[SUCESSO] Bem-vindo, Administrador!");
            exibirMenuAdministrador(); 
            return;
        }

        Usuario usuarioLogado = controller.autenticarUsuario(emailInformado, senhaInformada);

        if (usuarioLogado != null) {
            System.out.println("\n[SUCESSO] Login realizado!");
            System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + " [" + usuarioLogado.getCargo() + "]");
            exibirMenuInternoEJ(usuarioLogado);
        } else {
            System.out.println("\n[ERRO] E-mail ou senha incorretos");
        }
    }

    private void exibirMenuAdministrador() {
        while (true) {
            System.out.println("\n=== PAINEL ADMINISTRATIVO ===");
            System.out.println("1. Avaliar solicitações de EJs");
            System.out.println("0. Fazer Logout");
            System.out.print("Escolha uma opção: ");
            String op = leitor.nextLine();

            if (op.equals("1")) {
                // 1. Instancia os DAOs
                dao.SolicitacaoDAO solDAO = new dao.SolicitacaoDAO();
                dao.UsuarioDAO userDAO = new dao.UsuarioDAO();
                dao.EmpresaJuniorDAO ejDAO = new dao.EmpresaJuniorDAO();

                // busca todos os dados necessários do banco
                List<Usuario> usuarios = userDAO.listarTodos();
                List<EmpresaJunior> empresas = ejDAO.listarTodas();

                // busca as solicitações pendentes passando as listas
                List<SolicitacaoEJ> pendentes = solDAO.listarPendentes(usuarios, empresas);

                // verifica se tem algo para avaliar
                if (pendentes.isEmpty()) {
                    System.out.println("\n[AVISO] Nenhuma solicitação pendente no momento.");
                } else {
                    System.out.println("\n[SUCESSO] Foram encontradas " + pendentes.size() + " solicitações pendentes.");
                    
                    // ==========================================================
                    // LISTAGEM CONSOLIDADA DE SOLICITAÇÕES (RF21)
                    // ==========================================================
                    System.out.println("\n--------------------------------------------------------------");
                    System.out.println("ID   | EMPRESA JÚNIOR             | SOLICITANTE");
                    System.out.println("--------------------------------------------------------------");
                    for (SolicitacaoEJ req : pendentes) {
                        // Imprime os dados formatados em colunas
                        System.out.printf("%-4d | %-26s | %s\n", 
                            req.getId(), 
                            req.getEmpresaJunior().getNome(), 
                            req.getUsuario().getNome());
                    }
                    System.out.println("--------------------------------------------------------------");

                    System.out.print("\nDigite o ID da solicitação que deseja avaliar (ou 0 para voltar): ");
                    String entrada = leitor.nextLine().trim();

                    if (!entrada.equals("0")) {
                        try {
                            long idEscolhido = Long.parseLong(entrada);
                            SolicitacaoEJ selecionada = null;
                            
                            // Procura na lista a solicitação com o ID digitado
                            for (SolicitacaoEJ req : pendentes) {
                                if (req.getId() == idEscolhido) {
                                    selecionada = req;
                                    break;
                                }
                            }

                            if (selecionada != null) {
                                // Envia APENAS a escolhida para o Controller avaliar
                                AdminView adminView = new AdminView();
                                SolicitacaoController solController = new SolicitacaoController(adminView);
                                solController.avaliarSolicitacao(selecionada);
                            } else {
                                System.out.println("\n[ERRO] ID não encontrado na lista de pendências.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\n[ERRO] Digite um número de ID válido!");
                        }
                    }
                }
            } else if (op.equals("0")) {
                System.out.println("Saindo do painel administrativo...");
                break;
            } else {
                System.out.println("[ERRO] Opção inválida!");
            }
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

    private void exibirMenuInternoEJ(Usuario usuarioLogado) {
        FuncionalidadeController funcController = new FuncionalidadeController();

        while (true) {
            boolean emEj = candController.temVinculoAprovado(usuarioLogado.getId());
            List<Funcionalidade> selecionadas = new ArrayList<>();
            try {
                selecionadas = funcController.listarSelecionadas(usuarioLogado.getId());
            } catch (Exception e) {
            }

            List<String> labels = new ArrayList<>();
            List<Runnable> acoes = new ArrayList<>();

            if (emEj && selecionadas.contains(Funcionalidade.KANBAN)) {
                labels.add("Acessar Quadros Kanban");
                acoes.add(() -> {
                    KambanView kView = new KambanView(leitor);
                    KambanController kController = new KambanController(kView);
                    kController.iniciar();
                });
            }

            if (emEj && selecionadas.contains(Funcionalidade.BIBLIOTECA)) {
                labels.add("Acessar Biblioteca");
                acoes.add(() -> {
                    BibliotecaView bView = new BibliotecaView();
                    BibliotecaController bController = new BibliotecaController(
                            biblioService, bView, usuarioLogado,
                            controller.getUsuariosDoSistema(), idsAtivos);
                    bController.iniciar();
                });
            }

            if (emEj && selecionadas.contains(Funcionalidade.LEADS)) {
                labels.add("Acessar Módulo de Leads");
                acoes.add(() -> {
                    LeadView leadView = new LeadView();
                    LeadController leadController = new LeadController(leadView);
                    leadController.iniciarModulo();
                });
            }

            // Painel Administrativo (Só para admins)
            labels.add("Painel Administrativo (Aprovar EJs)");
            acoes.add(() -> {
                executarModuloAdmin();
            });

            // Buscar Empresas Juniores: Só aparece para quem NÃO está em uma EJ ainda (uc03)
            if (!emEj) {
                labels.add("Buscar Empresas Juniores (Solicitar Ingresso)");
                acoes.add(() -> {
                    EmpresaJuniorView ejView = new EmpresaJuniorView();
                    ejView.exibirMenuBusca(usuarioLogado);
                });

                labels.add("Solicitar Cadastro de Nova EJ do Zero");
                acoes.add(() -> {
                    executarFluxoSolicitarCadastroEJ(usuarioLogado);
                });
            }

            // Painel do Diretor: exclusivo para quem tem cargo de DIRETOR (rn01)
            if (usuarioLogado.getCargo() == Cargo.DIRETOR) {
                labels.add("Painel do Diretor (Notificações de Ingresso)");
                acoes.add(() -> {
                    executarPainelDiretor(usuarioLogado);
                });
            }

            // Desvincular-se: Só aparece para quem tem vínculo ativo (UC05)
            if (emEj) {
                labels.add("Sair da Empresa Júnior");
                acoes.add(() -> {
                    executarFluxoSaidaEmpresa(usuarioLogado);
                });
            }

            // Só aparece se já estiver em Ej
            if (emEj && selecionadas.contains(Funcionalidade.METRICAS)) {
                labels.add("Métricas de Desempenho");
                acoes.add(() -> {
                    MetricasView metricasView = new MetricasView(leitor, usuarioLogado);
                    metricasView.iniciar();
                });
            }

            if (emEj && selecionadas.contains(Funcionalidade.RELATORIO)) {
                labels.add("Gerar Relatório Semanal");
                acoes.add(() -> {
                    RelatorioView relatorioView = new RelatorioView(leitor, usuarioLogado);
                    relatorioView.iniciar();
                });
            }

            if (emEj && selecionadas.contains(Funcionalidade.AGENDA)) {
                labels.add("Acessar Agenda e Eventos");
                acoes.add(() -> {
                    EventoView eventoView = new EventoView(leitor, usuarioLogado);
                    eventoView.iniciar();
                });
            }

            if (emEj && selecionadas.contains(Funcionalidade.PROJETOS) && usuarioLogado.getCargo() == Cargo.DIRETOR) {
                labels.add("Gerenciar Projetos");
                acoes.add(() -> {
                    ProjetoView projetoView = new ProjetoView(leitor);
                    projetoView.iniciar();
                });
            }

            // Configuração de ferramentas: Só disponível se já estiver em uma EJ (UC10 +
            // RNF03)
            if (emEj) {
                labels.add("Configurar Ferramentas de Trabalho");
                acoes.add(() -> {
                    FuncionalidadeView funcView = new FuncionalidadeView(leitor, usuarioLogado);
                    funcView.iniciar();
                });
            }

            System.out.println("\n========================================");
            System.out.println("      GERENCIA EJ - MENU INTERNO        ");
            System.out.println("========================================");

            for (int i = 0; i < labels.size(); i++) {
                System.out.println((i + 1) + ". " + labels.get(i));
            }
            System.out.println("0. Fazer Logout");
            System.out.print("Escolha uma opção: ");

            String entrada = leitor.nextLine().trim();

            if (entrada.equals("0")) {
                System.out.println("Efetuando logout... Voltando à tela inicial.");
                break;
            }

            try {
                int escolha = Integer.parseInt(entrada);
                if (escolha >= 1 && escolha <= acoes.size()) {
                    acoes.get(escolha - 1).run();
                } else {
                    System.out.println("\n[ERRO] Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Digite um número válido!");
            }
        }
    }

    private void executarPainelDiretor(Usuario usuarioLogado) {
        System.out.println("\n========================================");
        System.out.println("     PAINEL DO DIRETOR - NOTIFICAÇÕES   ");
        System.out.println("========================================");

        List<String> pendentes = candController.obterNotificacoesPendentes(usuarioLogado.getId());

        if (pendentes.isEmpty()) {
            System.out.println("Não há nenhuma solicitação de ingresso pendente de outros usuários no momento.");
            return;
        }

        System.out.println("Solicitações aguardando sua decisão:");
        for (String solicitacao : pendentes) {
            System.out.println(solicitacao);
        }

        System.out.println("----------------------------------------");
        System.out.print("Digite o ID da solicitação que deseja avaliar (ou 0 para voltar): ");

        while (!leitor.hasNextLong()) {
            System.out.println("[ERRO] Digite um número válido!");
            leitor.next();
        }
        long idEscolhido = leitor.nextLong();
        leitor.nextLine(); // Limpa buffer

        if (idEscolhido != 0) {
            System.out.print("Deseja [1] APROVAR ou [2] RECUSAR? ");
            while (!leitor.hasNextInt()) {
                System.out.println("[ERRO] Digite 1 ou 2!");
                leitor.next();
            }
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

    private void executarModuloAdmin() {
        System.out.println("\n[INFO] Painel Administrativo.");
    }

    private void executarFluxoSaidaEmpresa(Usuario usuarioLogado) {
        System.out.println("\n========================================");
        System.out.println("        DESVINCULAR DE EMPRESA JÚNIOR   ");
        System.out.println("========================================");
        System.out.print("Tem certeza absoluta que deseja sair da sua Empresa Júnior atual? (S/N): ");

        String confirmacao = leitor.nextLine().trim().toUpperCase();

        //UC05
        if (confirmacao.equals("S")) {
            boolean sucesso = candController.processarSaidaEmpresa(usuarioLogado.getId());

            if (sucesso) {
                System.out.println(
                        "\n[SUCESSO] Você se desligou da Empresa Júnior com sucesso! Seu vínculo agora está INATIVO.");
                System.out.println("[INFO] Você está livre para solicitar ingresso em outras empresas.");
            } else {
                System.out.println(
                        "\n[AVISO] Não foi possível concluir a ação. Você não possui nenhum vínculo ativo ('APROVADO') no sistema.");
            }
        } else {
            System.out.println("\nOperação cancelada. Você continua vinculado à sua Empresa Júnior.");
        }
    }

private void executarFluxoSolicitarCadastroEJ(Usuario usuarioLogado) {
        System.out.println("\n========================================");
        System.out.println("   SOLICITAR CADASTRO DE NOVA EJ (UC08) ");
        System.out.println("========================================");
        System.out.print("Nome da Empresa Júnior: ");
        String nomeEj = leitor.nextLine();
        System.out.print("CNPJ da Empresa Júnior: ");
        String cnpjEj = leitor.nextLine();

        System.out.println("Abrindo janela para selecionar o documento comprobatório (PDF/Ata)...");
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Selecione o documento comprobatório da EJ");
        
        int userSelection = fileChooser.showOpenDialog(null);
        String documentoUrl = "";
        
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File arquivoSelecionado = fileChooser.getSelectedFile();
            documentoUrl = arquivoSelecionado.getAbsolutePath();
            System.out.println("-> Arquivo anexado com sucesso: " + arquivoSelecionado.getName());
        } else {
            System.out.println("\n[ERRO] Nenhum documento anexado!");
            return;
        }

        System.out.println("\nEnviando dados para processamento...");
        AdminView adminView = new AdminView();
        SolicitacaoController solController = new SolicitacaoController(adminView);
        solController.criarSolicitacao(nomeEj, cnpjEj, documentoUrl, usuarioLogado);
    }}