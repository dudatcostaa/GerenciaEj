package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // RF01 — cadastra um usuário novo
    public Usuario cadastrar(String nome, String email, String senha, Cargo cargo) {
        String sql = "INSERT INTO usuario (nome, email, senha, cargo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, cargo.name());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long id = keys.getLong(1);
                return new Usuario(id, nome, email, senha, cargo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
        return null;
    }

    // UC02 — busca usuário por email e senha para autenticação
    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE LOWER(email) = LOWER(?) AND senha = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
        }
        return null;
    }

    // RF02 — remove usuário por email e senha
    public boolean excluir(String email, String senha) {
        String sql = "DELETE FROM usuario WHERE LOWER(email) = LOWER(?) AND senha = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
        }
        return false;
    }

    // RN10 — verifica se email já existe no sistema
    public boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM usuario WHERE LOWER(email) = LOWER(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar email: " + e.getMessage());
        }
        return false;
    }

    // retorna todos os usuários do sistema
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }
        return lista;
    }

    // converte uma linha do ResultSet em um objeto Usuario
    private Usuario mapear(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");
        Cargo cargo = Cargo.valueOf(rs.getString("cargo"));

        Usuario u = new Usuario(id, nome, email, senha, cargo);
        return u;
    }
}