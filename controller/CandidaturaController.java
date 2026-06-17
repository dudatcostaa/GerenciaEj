package controller;

import dao.CandidaturaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;

public class CandidaturaController {
    private CandidaturaDAO candidaturaDAO;
    private Connection connection; // Certifique-se de que essa linha existe aqui em cima

    public CandidaturaController() {
        // Inicializa o DAO para o menu interno funcionar
        this.candidaturaDAO = new CandidaturaDAO();
        
        // INCLUA ESSA LINHA: Inicializa a conexão para a busca de notificações funcionar
        this.connection = DatabaseConnection.getInstance().getConnection();
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
    public List<String> obterNotificacoesPendentes(long idDiretor) {
        List<String> notificacoes = new ArrayList<>();

        // A query mágica que linka o diretor à sua respectiva EJ
        String sql = "SELECT c.id AS cand_id, u.nome AS nome_candidato " +
                "FROM candidatura_ej c " +
                "JOIN usuario u ON c.usuario_id = u.id " +
                "WHERE c.status = 'PENDENTE' " +
                "AND c.empresa_junior_id = (SELECT empresa_junior_id FROM usuario WHERE id = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Passa o ID do diretor logado para o parâmetro da subquery
            stmt.setLong(1, idDiretor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String linha = "[ID: " + rs.getLong("cand_id") + "] " + rs.getString("nome_candidato")
                            + " solicitou entrada.";
                    notificacoes.add(linha);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar notificações: " + e.getMessage());
        }

        return notificacoes;
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