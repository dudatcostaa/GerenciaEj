package dao;

import model.EmpresaJunior;
import model.StatusEJ;
import model.DatabaseConnection;
import java.sql.*;

public class EmpresaJuniorDAO {

    private Connection connection;

    public EmpresaJuniorDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra uma empresa junior nova
    public EmpresaJunior cadastrar(String nome, String cnpj) {
        String sql = "INSERT INTO empresa_junior (nome, cnpj) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setString(2, cnpj);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new EmpresaJunior(keys.getLong(1), nome, cnpj);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar empresa junior: " + e.getMessage());
        }
        return null;
    }

    // atualiza o status da empresa junior
    public void atualizarStatus(Long id, StatusEJ status) {
        String sql = "UPDATE empresa_junior SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da EJ: " + e.getMessage());
        }
    }
}