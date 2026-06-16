package model.dao;

import model.DatabaseConnection;
import model.EmpresaJunior;
import model.SolicitacaoEJ;
import model.StatusSolicitacao;
import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {

    private Connection connection;

    public SolicitacaoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra uma solicitação nova
    public SolicitacaoEJ cadastrar(String documentoUrl, Usuario usuario, EmpresaJunior ej) {
        String sql = "INSERT INTO solicitacao_ej (documento_url, usuario_id, empresa_junior_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, documentoUrl);
            stmt.setLong(2, usuario.getId());
            stmt.setLong(3, ej.getId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new SolicitacaoEJ(keys.getLong(1), documentoUrl, usuario, ej);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar solicitação: " + e.getMessage());
        }
        return null;
    }

    // retorna todas as solicitações pendentes
    public List<SolicitacaoEJ> listarPendentes(List<Usuario> usuarios, List<EmpresaJunior> empresas) {
        String sql = "SELECT * FROM solicitacao_ej WHERE status = 'PENDENTE'";
        List<SolicitacaoEJ> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long usuarioId = rs.getLong("usuario_id");
                Long ejId = rs.getLong("empresa_junior_id");

                Usuario usuario = usuarios.stream()
                        .filter(u -> u.getId().equals(usuarioId))
                        .findFirst().orElse(null);

                EmpresaJunior ej = empresas.stream()
                        .filter(e -> e.getId().equals(ejId))
                        .findFirst().orElse(null);

                if (usuario != null && ej != null) {
                    SolicitacaoEJ s = new SolicitacaoEJ(
                        rs.getLong("id"),
                        rs.getString("documento_url"),
                        usuario,
                        ej
                    );
                    lista.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar solicitações: " + e.getMessage());
        }
        return lista;
    }

    // atualiza o status da solicitação
    public void atualizarStatus(Long id, StatusSolicitacao status) {
        String sql = "UPDATE solicitacao_ej SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da solicitação: " + e.getMessage());
        }
    }
}