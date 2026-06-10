package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Tarefa;
import model.StatusTarefa;

public class TarefaDAO {

    private Connection connection;

    public TarefaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // cadastra uma tarefa nova no quadro
    public Tarefa cadastrar(String titulo, Long quadroId) {
        String sql = "INSERT INTO tarefa (titulo, status, quadro_kamban_id) VALUES (?, 'PENDENTE', ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, titulo);
            stmt.setLong(2, quadroId);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return new Tarefa(keys.getLong(1), titulo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar tarefa: " + e.getMessage());
        }
        return null;
    }

    // retorna todas as tarefas de um quadro
    public List<Tarefa> listarPorQuadro(Long quadroId) {
        String sql = "SELECT * FROM tarefa WHERE quadro_kamban_id = ?";
        List<Tarefa> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, quadroId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tarefa t = new Tarefa(rs.getLong("id"), rs.getString("titulo"));
                t.setStatus(StatusTarefa.valueOf(rs.getString("status")));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas: " + e.getMessage());
        }
        return lista;
    }

    // atualiza o status de uma tarefa
    public void atualizarStatus(Long id, StatusTarefa status) {
        String sql = "UPDATE tarefa SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da tarefa: " + e.getMessage());
        }
    }
}