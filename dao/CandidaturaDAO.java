package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;

public class CandidaturaDAO {

    public boolean temCandidaturaAtivaOuPendente(Long usuarioId) {
        String sql = "SELECT COUNT(*) FROM candidatura_ej WHERE usuario_id = ? AND (status = 'PENDENTE' OR status = 'APROVADO')";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, usuarioId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar candidaturas: " + e.getMessage());
        }
        return false;
    }

    public void enviarSolicitacao(Long usuarioId, Long empresaJuniorId) {
        String sql = "INSERT INTO candidatura_ej (usuario_id, empresa_junior_id, status) VALUES (?, ?, 'PENDENTE')";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, usuarioId);
                stmt.setLong(2, empresaJuniorId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao enviar solicitação: " + e.getMessage());
        }
    }

    public List<String> listarCandidaturasPendentes(Long diretorId) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT c.id, u.nome AS usuario_nome, e.nome AS ej_nome " +
                     "FROM candidatura_ej c " +
                     "JOIN usuario u ON c.usuario_id = u.id " +
                     "JOIN empresa_junior e ON c.empresa_junior_id = e.id " +
                     "WHERE c.status = 'PENDENTE' AND c.usuario_id != ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, diretorId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String linha = "ID Solicitação: " + rs.getLong("id") + 
                                       " | Candidato: " + rs.getString("usuario_nome") + 
                                       " -> deseja entrar na EJ: " + rs.getString("ej_nome");
                        lista.add(linha);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notificações: " + e.getMessage());
        }
        return lista;
    }

    public void atualizarStatusCandidatura(Long candidaturaId, String novoStatus) {
        String sql = "UPDATE candidatura_ej SET status = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, novoStatus);
                stmt.setLong(2, candidaturaId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao responder solicitação: " + e.getMessage());
        }
    }
}