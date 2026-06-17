package controller;

import model.Cargo;
import model.SolicitacaoEJ;
import model.StatusEJ;
import model.StatusSolicitacao;
import dao.EmpresaJuniorDAO;
import dao.SolicitacaoDAO;
import dao.UsuarioDAO;
import view.AdminView;
import model.Usuario;


public class SolicitacaoController {

    private AdminView view;
    private SolicitacaoDAO solicitacaoDAO;
    private EmpresaJuniorDAO ejDAO;
    private UsuarioDAO usuarioDAO;

    public SolicitacaoController(AdminView view) {
        this.view = view;
        this.solicitacaoDAO = new SolicitacaoDAO();
        this.ejDAO = new EmpresaJuniorDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public void avaliarSolicitacao(SolicitacaoEJ solicitacao) {

        view.exibirDetalhesSolicitacao(
            solicitacao.getEmpresaJunior().getNome(),
            solicitacao.getUsuario().getNome(),
            solicitacao.getDocumentoUrl()
        );
        
        // UC08
        System.out.println("Tentando abrir o documento comprobatório para análise...");
        try {
            java.io.File arquivo = new java.io.File(solicitacao.getDocumentoUrl());
            
            // Verifica se o arquivo realmente existe nesse caminho do computador
            if (arquivo.exists() && java.awt.Desktop.isDesktopSupported()) {
                // Abre o arquivo usando o programa padrão do sistema (Chrome, Adobe Reader, etc.)
                java.awt.Desktop.getDesktop().open(arquivo);
            } else {
                System.out.println("[AVISO] Não foi possível abrir o arquivo automaticamente.");
                System.out.println("Caminho registrado: " + solicitacao.getDocumentoUrl());
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar abrir o arquivo: " + e.getMessage());
        }

        int opcao = view.pedirDecisao();
        System.out.println("\nProcessando a solicitação...");

        if (opcao == 1) { // Aprovado
            // atualiza os três objetos no banco
            solicitacao.setStatus(StatusSolicitacao.APROVADA);
            solicitacaoDAO.atualizarStatus(solicitacao.getId(), StatusSolicitacao.APROVADA);

            solicitacao.getEmpresaJunior().setStatus(StatusEJ.ATIVA);
            ejDAO.atualizarStatus(solicitacao.getEmpresaJunior().getId(), StatusEJ.ATIVA);

            solicitacao.getUsuario().setCargo(Cargo.DIRETOR);
            solicitacao.getUsuario().setEmpresaJunior(solicitacao.getEmpresaJunior());
            usuarioDAO.atualizarCargoEEmpresa(
                solicitacao.getUsuario().getId(),
                Cargo.DIRETOR,
                solicitacao.getEmpresaJunior().getId()
            );

            // ==========================================================
            // O PASSO MÁGICO PARA RESOLVER O VÍNCULO FANTASMA
            // ==========================================================
            dao.CandidaturaDAO candDAO = new dao.CandidaturaDAO(); 
            candDAO.criarVinculoDireto(solicitacao.getUsuario().getId(), solicitacao.getEmpresaJunior().getId(), "APROVADO");
            // ==========================================================

            view.exibirMensagemSucesso(
                solicitacao.getEmpresaJunior().getStatus().toString(),
                solicitacao.getUsuario().getNome(),
                solicitacao.getUsuario().getCargo().toString()
            );

        } else if (opcao == 2) { // Reprovado
            solicitacao.setStatus(StatusSolicitacao.REPROVADA);
            solicitacaoDAO.atualizarStatus(solicitacao.getId(), StatusSolicitacao.REPROVADA);

            String urlDocumento = solicitacao.getDocumentoUrl();
            excluirArquivo(urlDocumento);

            view.exibirMensagemReprovacao(urlDocumento);

        } else {
            view.exibirErro();
        }
    }

    private void excluirArquivo(String url) {
        // lógica real de deletar arquivo iria aqui
    }
    public void criarSolicitacao(String nomeEj, String cnpj, String documentoUrl, Usuario usuarioLogado) {
        
        // Instancia a Facade que esconde a complexidade do banco
        facade.CadastroEJFacade facade = new facade.CadastroEJFacade();
        
        // Tenta processar o cadastro completo
        boolean sucesso = facade.solicitarCadastroCompleto(nomeEj, cnpj, documentoUrl, usuarioLogado);

        if (sucesso) {
            System.out.println("-> SUCESSO: A solicitação de criação da Empresa Júnior foi enviada!");
            System.out.println("   Um administrador irá avaliar o documento em breve.");
        } else {
            System.out.println("-> ERRO: Ocorreu uma falha ao enviar a solicitação. Tente novamente.");
        }
    }
    
}
