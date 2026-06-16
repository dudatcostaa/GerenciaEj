package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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

            // busca o usuario_id e empresa_junior_id antes de atualizar, para sincronizar
            // a tabela usuario quando a candidatura for aprovada
            Long usuarioId = null;
            Long empresaJuniorId = null;
            String sqlBusca = "SELECT usuario_id, empresa_junior_id FROM candidatura_ej WHERE id = ?";
            try (PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)) {
                stmtBusca.setLong(1, candidaturaId);
                try (ResultSet rs = stmtBusca.executeQuery()) {
                    if (rs.next()) {
                        usuarioId = rs.getLong("usuario_id");
                        empresaJuniorId = rs.getLong("empresa_junior_id");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, novoStatus);
                stmt.setLong(2, candidaturaId);
                stmt.executeUpdate();
            }

            // sincroniza usuario.empresa_junior_id de acordo com o novo status
            if (usuarioId != null) {
                if ("APROVADO".equals(novoStatus)) {
                    sincronizarEmpresaDoUsuario(conn, usuarioId, empresaJuniorId);
                } else if ("REPROVADO".equals(novoStatus) || "INATIVO".equals(novoStatus)) {
                    sincronizarEmpresaDoUsuario(conn, usuarioId, null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao responder solicitação: " + e.getMessage());
        }
    }

    // Atende ao RF05 e libera a RN06: Muda o vínculo ativo para INATIVO
    public boolean sairDaEmpresaJunior(Long usuarioId) {
        String sql = "UPDATE candidatura_ej SET status = 'INATIVO' WHERE usuario_id = ? AND status = 'APROVADO'";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, usuarioId);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    // limpa o vínculo direto na tabela usuario também
                    sincronizarEmpresaDoUsuario(conn, usuarioId, null);
                }

                return linhasAfetadas > 0; // Retorna true se o usuário realmente tinha um vínculo ativo e saiu
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao se desligar da empresa júnior: " + e.getMessage());
        }
    }

    // atualiza usuario.empresa_junior_id (usa null para desvincular)
    private void sincronizarEmpresaDoUsuario(Connection conn, Long usuarioId, Long empresaJuniorId) throws SQLException {
        String sql = "UPDATE usuario SET empresa_junior_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (empresaJuniorId == null) {
                stmt.setNull(1, Types.BIGINT);
            } else {
                stmt.setLong(1, empresaJuniorId);
            }
            stmt.setLong(2, usuarioId);
            stmt.executeUpdate();
        }
    }
    // Verifica se o usuário possui um vínculo atualmente ativo/aprovado em alguma EJ (RNF03)
    public boolean temVinculoAprovado(Long usuarioId) {
        String sql = "SELECT COUNT(*) FROM candidatura_ej WHERE usuario_id = ? AND status = 'APROVADO'";
        
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
            throw new RuntimeException("Erro ao verificar vínculo do usuário: " + e.getMessage());
        }
        return false;
    }
}