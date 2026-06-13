package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Gasto;

public class GastoDAO {

    private Connection connection;

    public GastoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra um novo gasto
    public Gasto cadastrar(String descricao, double valor, java.util.Date data) {
        String sql = "INSERT INTO gasto_mensal (descricao, valor, data) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, descricao);
            stmt.setDouble(2, valor);
            stmt.setDate(3, new java.sql.Date(data.getTime()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new Gasto(keys.getLong(1), descricao, valor, data);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar gasto: " + e.getMessage());
        }
        return null;
    }

    // lista todos os gastos do mês e ano informados
    public List<Gasto> listarPorMes(int mes, int ano) {
        String sql = "SELECT * FROM gasto_mensal " +
                     "WHERE MONTH(data) = ? AND YEAR(data) = ? " +
                     "ORDER BY data DESC";
        List<Gasto> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, ano);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar gastos: " + e.getMessage());
        }
        return lista;
    }

    // soma total dos gastos do mês e ano informados
    public double totalPorMes(int mes, int ano) {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM gasto_mensal " +
                     "WHERE MONTH(data) = ? AND YEAR(data) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, ano);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("Erro ao somar gastos: " + e.getMessage());
        }
        return 0;
    }

    private Gasto mapear(ResultSet rs) throws SQLException {
        return new Gasto(
            rs.getLong("id"),
            rs.getString("descricao"),
            rs.getDouble("valor"),
            rs.getDate("data")
        );
    }
}