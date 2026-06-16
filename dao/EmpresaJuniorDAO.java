package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.EmpresaJunior;
import model.StatusEJ;

public class EmpresaJuniorDAO {

    private Connection connection;

    public EmpresaJuniorDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

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

    // UC03
    public List<EmpresaJunior> listarTodas() {
        List<EmpresaJunior> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa_junior";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EmpresaJunior ej = new EmpresaJunior(rs.getLong("id"), rs.getString("nome"), rs.getString("cnpj"));
                empresas.add(ej);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empresas juniores: " + e.getMessage());
        }
        return empresas;
    }

    // UC03
    public List<EmpresaJunior> buscarPorNome(String nome) {
        List<EmpresaJunior> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa_junior WHERE nome LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmpresaJunior ej = new EmpresaJunior(rs.getLong("id"), rs.getString("nome"), rs.getString("cnpj"));
                    empresas.add(ej);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empresa por nome: " + e.getMessage());
        }
        return empresas;
    }
}
