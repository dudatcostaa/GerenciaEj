package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Proposta;

public class PropostaDAO {

    private Connection connection;

    public PropostaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra uma nova proposta
    public Proposta cadastrar(String nomeCliente, double valor, java.util.Date data) {
        String sql = "INSERT INTO proposta (nome_cliente, valor, data) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nomeCliente);
            stmt.setDouble(2, valor);
            stmt.setDate(3, new java.sql.Date(data.getTime()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new Proposta(keys.getLong(1), nomeCliente, valor, data);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar proposta: " + e.getMessage());
        }
        return null;
    }

    // lista todas as propostas do mês e ano informados
    public List<Proposta> listarPorMes(int mes, int ano) {
        String sql = "SELECT * FROM proposta " +
                     "WHERE MONTH(data) = ? AND YEAR(data) = ? " +
                     "ORDER BY data DESC";
        List<Proposta> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, ano);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar propostas: " + e.getMessage());
        }
        return lista;
    }

    // conta propostas do mês e ano informados
    public int contarPorMes(int mes, int ano) {
        String sql = "SELECT COUNT(*) AS total FROM proposta " +
                     "WHERE MONTH(data) = ? AND YEAR(data) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, ano);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.err.println("Erro ao contar propostas: " + e.getMessage());
        }
        return 0;
    }

    private Proposta mapear(ResultSet rs) throws SQLException {
        return new Proposta(
            rs.getLong("id"),
            rs.getString("nome_cliente"),
            rs.getDouble("valor"),
            rs.getDate("data")
        );
    }
}