package controller;

import model.Cargo;
import model.SolicitacaoEJ;
import model.StatusEJ;
import model.StatusSolicitacao;
import model.dao.EmpresaJuniorDAO;
import model.dao.SolicitacaoDAO;
import model.dao.UsuarioDAO;
import view.AdminView;

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
}