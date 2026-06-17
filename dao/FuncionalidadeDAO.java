package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Funcionalidade;

public class FuncionalidadeDAO {

    private Connection connection;

    public FuncionalidadeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // RN03 — verifica se o usuário ja selecionou essa funcionalidade
    public boolean possui(Long usuarioId, Funcionalidade funcionalidade) {
        String sql = "SELECT 1 FROM usuario_funcionalidade WHERE usuario_id = ? AND funcionalidade = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setString(2, funcionalidade.name());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar funcionalidade: " + e.getMessage());
            return false;
        }
    }

    // RF07 — adiciona uma funcionalidade ao conjunto do usuário
    public boolean adicionar(Long usuarioId, Funcionalidade funcionalidade) {
        String sql = "INSERT INTO usuario_funcionalidade (usuario_id, funcionalidade) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setString(2, funcionalidade.name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar funcionalidade: " + e.getMessage());
            return false;
        }
    }

    // RF08 — remove uma funcionalidade previamente selecionada
    public boolean remover(Long usuarioId, Funcionalidade funcionalidade) {
        String sql = "DELETE FROM usuario_funcionalidade WHERE usuario_id = ? AND funcionalidade = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setString(2, funcionalidade.name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover funcionalidade: " + e.getMessage());
            return false;
        }
    }

    // lista todas as funcionalidades selecionadas pelo usuário
    public List<Funcionalidade> listarSelecionadas(Long usuarioId) {
        String sql = "SELECT funcionalidade FROM usuario_funcionalidade WHERE usuario_id = ?";
        List<Funcionalidade> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    lista.add(Funcionalidade.valueOf(rs.getString("funcionalidade")));
                } catch (IllegalArgumentException ignored) {
                    // ignora valores que não existem mais no enum
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionalidades: " + e.getMessage());
        }
        return lista;
    }
}