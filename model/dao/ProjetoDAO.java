package model.dao;

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
    public Projeto cadastrar(String nome, String descricao, StatusProjeto status, double valor) {
        String sql = "INSERT INTO projeto (nome, descricao, status, valor) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setString(3, status.name());
            stmt.setDouble(4, valor);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Projeto p = new Projeto(keys.getLong(1), nome, descricao, status);
                p.setValor(valor);
                return p;
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

    // retorna todos os projetos (usado no gerenciamento)
    public List<Projeto> listarTodos() {
        String sql = "SELECT * FROM projeto ORDER BY data_inicio DESC";
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

    // atualiza nome, descrição, status, data de início e valor de um projeto
    public boolean atualizar(Projeto projeto) {
        String sql = "UPDATE projeto SET nome = ?, descricao = ?, status = ?, data_inicio = ?, valor = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setString(3, projeto.getStatus().name());
            stmt.setDate(4, new java.sql.Date(projeto.getDataInicio().getTime()));
            stmt.setDouble(5, projeto.getValor());
            stmt.setLong(6, projeto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar projeto: " + e.getMessage());
        }
        return false;
    }

    // exclui projeto e, em cascade, seu quadro e tarefas
    public boolean excluir(Long id) {
        // as tarefas dependem do quadro, que depende do projeto;
        // se o banco tiver ON DELETE CASCADE configurado basta deletar o projeto.
        // caso contrário, fazemos a exclusão manual na ordem correta.
        String sqlTarefas  = "DELETE FROM tarefa WHERE quadro_kamban_id IN (SELECT id FROM quadro_kamban WHERE projeto_id = ?)";
        String sqlQuadro   = "DELETE FROM quadro_kamban WHERE projeto_id = ?";
        String sqlProjeto  = "DELETE FROM projeto WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement s1 = connection.prepareStatement(sqlTarefas)) {
                s1.setLong(1, id);
                s1.executeUpdate();
            }
            try (PreparedStatement s2 = connection.prepareStatement(sqlQuadro)) {
                s2.setLong(1, id);
                s2.executeUpdate();
            }
            try (PreparedStatement s3 = connection.prepareStatement(sqlProjeto)) {
                s3.setLong(1, id);
                int linhas = s3.executeUpdate();
                connection.commit();
                return linhas > 0;
            }
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignora */ }
            System.err.println("Erro ao excluir projeto: " + e.getMessage());
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { /* ignora */ }
        }
        return false;
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
        Projeto p = new Projeto(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("descricao"),
            StatusProjeto.valueOf(rs.getString("status"))
        );
        Date dataDb = rs.getDate("data_inicio");
        if (dataDb != null) p.setDataInicio(dataDb);
        p.setValor(rs.getDouble("valor"));
        return p;
    }
}