package dao;

import java.sql.*;
import model.DatabaseConnection;
import model.QuadroKamban;
import model.Projeto;

public class QuadroKambanDAO {

    private Connection connection;

    public QuadroKambanDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // busca o quadro de um projeto, ou cria um novo se ainda não existir
    public QuadroKamban buscarOuCriar(Projeto projeto) {
        String sql = "SELECT * FROM quadro_kamban WHERE projeto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, projeto.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // se ja existe, retorna o quadro salvo
                QuadroKamban q = new QuadroKamban(rs.getString("titulo"));
                q.setId(rs.getLong("id"));
                q.setContadorTarefas(rs.getLong("contador_tarefas"));
                return q;
            } else {
                // se não existe, cria um novo
                return criar(projeto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quadro: " + e.getMessage());
        }
        return null;
    }

    // cria um quadro novo para o projeto
    private QuadroKamban criar(Projeto projeto) {
        String titulo = "Kamban: " + projeto.getNome();
        String sql = "INSERT INTO quadro_kamban (titulo, projeto_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, titulo);
            stmt.setLong(2, projeto.getId());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                QuadroKamban q = new QuadroKamban(titulo);
                q.setId(keys.getLong(1));
                return q;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao criar quadro: " + e.getMessage());
        }
        return null;
    }

    // salva o contador de tarefas atualizado no banco
    public void atualizarContador(QuadroKamban quadro) {
        String sql = "UPDATE quadro_kamban SET contador_tarefas = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, quadro.getContadorTarefas());
            stmt.setLong(2, quadro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar contador: " + e.getMessage());
        }
    }
}