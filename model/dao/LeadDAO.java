package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Lead;
import model.StatusLead;

public class LeadDAO {

    private Connection connection;

    public LeadDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra um lead novo
    public Lead cadastrar(String nomeCliente) {
        String sql = "INSERT INTO leads (nome_cliente) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nomeCliente);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Lead lead = new Lead(keys.getLong(1), nomeCliente);
                return lead;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar lead: " + e.getMessage());
        }
        return null;
    }

    // retorna todos os leads
    public List<Lead> listarTodos() {
        String sql = "SELECT * FROM leads ORDER BY data_criacao DESC";
        List<Lead> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Lead lead = new Lead(rs.getLong("id"), rs.getString("nome_cliente"));
                lead.setStatusLead(StatusLead.valueOf(rs.getString("status_lead")));
                lead.setDataCriacao(rs.getTimestamp("data_criacao"));
                lead.setDataUltimaModificacao(rs.getTimestamp("data_ultima_modificacao"));
                lista.add(lead);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar leads: " + e.getMessage());
        }
        return lista;
    }

    // atualiza o status de um lead
    public void atualizarStatus(Long id, StatusLead novoStatus) {
        String sql = "UPDATE leads SET status_lead = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novoStatus.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do lead: " + e.getMessage());
        }
    }
}