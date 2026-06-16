package controller;

import model.dao.CandidaturaDAO;

public class CandidaturaController {
    private CandidaturaDAO candidaturaDAO;

    public CandidaturaController() {
        this.candidaturaDAO = new CandidaturaDAO();
    }

    /**
     * Aplica a RN06 e gerencia o envio da solicitação (RF19)
     * @return true se a solicitação foi enviada, false se foi barrada pela RN06
     */
    public boolean processarSolicitacaoIngresso(Long usuarioId, Long ejIdEscolhido) {
        // Validação da RN06: O usuário já participa ou está tentando participar de uma EJ?
        if (candidaturaDAO.temCandidaturaAtivaOuPendente(usuarioId)) {
            return false; // Barrado! Viola a regra de negócio.
        }

        // Se passou na regra, o RF19 é executado e o registro nasce como PENDENTE
        candidaturaDAO.enviarSolicitacao(usuarioId, ejIdEscolhido);
        return true; // Sucesso!
    }

    public java.util.List<String> obterNotificacoesPendentes(Long diretorId) {
        return candidaturaDAO.listarCandidaturasPendentes(diretorId);
    }

    public void responderSolicitacao(Long candidaturaId, boolean aprovada) {
        String novoStatus = aprovada ? "APROVADO" : "RECUSADO";
        candidaturaDAO.atualizarStatusCandidatura(candidaturaId, novoStatus);
    }

    public boolean processarSaidaEmpresa(Long usuarioId) {
        return candidaturaDAO.sairDaEmpresaJunior(usuarioId);
    }

    public boolean temVinculoAprovado(Long usuarioId) {
        return candidaturaDAO.temVinculoAprovado(usuarioId);
    }
}