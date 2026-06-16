package controller;

import dao.CandidaturaDAO;

public class CandidaturaController {
    private CandidaturaDAO candidaturaDAO;

    public CandidaturaController() {
        this.candidaturaDAO = new CandidaturaDAO();
    }

    // UC04
    public boolean processarSolicitacaoIngresso(Long usuarioId, Long ejIdEscolhido) {
        if (candidaturaDAO.temCandidaturaAtivaOuPendente(usuarioId)) {
            return false;
        }

        candidaturaDAO.enviarSolicitacao(usuarioId, ejIdEscolhido);
        return true;
    }

    // UC06
    public java.util.List<String> obterNotificacoesPendentes(Long diretorId) {
        return candidaturaDAO.listarCandidaturasPendentes(diretorId);
    }

    // UC04 e UC06
    public void responderSolicitacao(Long candidaturaId, boolean aprovada) {
        String novoStatus = aprovada ? "APROVADO" : "RECUSADO";
        candidaturaDAO.atualizarStatusCandidatura(candidaturaId, novoStatus);
    }

    // UC05
    public boolean processarSaidaEmpresa(Long usuarioId) {
        return candidaturaDAO.sairDaEmpresaJunior(usuarioId);
    }

    // UC04 e UC06
    public boolean temVinculoAprovado(Long usuarioId) {
        return candidaturaDAO.temVinculoAprovado(usuarioId);
    }
}