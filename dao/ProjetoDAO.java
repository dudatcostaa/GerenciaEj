package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Projeto;
import model.StatusProjeto;

public class ProjetoDAO {

    private Connection connection;

    public ProjetoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra um projeto novo e retorna com o id gerado pelo banco
    public Projeto cadastrar(String nome, String descricao, StatusProjeto status) {
        String sql = "INSERT INTO projeto (nome, descricao, status) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setString(3, status.name());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new Projeto(keys.getLong(1), nome, descricao, status);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar projeto: " + e.getMessage());
        }
        return null;
    }

    // retorna todos os projetos em planejamento ou execução (os que aparecem no kamban)
    public List<Projeto> listarAtivos() {
        String sql = "SELECT * FROM projeto WHERE status IN ('EM_PLANEJAMENTO', 'EM_EXECUCAO')";
        List<Projeto> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar projetos: " + e.getMessage());
        }
        return lista;
    }

    // atualiza o status de um projeto
    public void atualizarStatus(Long id, StatusProjeto status) {
        String sql = "UPDATE projeto SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do projeto: " + e.getMessage());
        }
    }

    private Projeto mapear(ResultSet rs) throws SQLException {
        return new Projeto(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("descricao"),
            StatusProjeto.valueOf(rs.getString("status"))
        );
    }
}