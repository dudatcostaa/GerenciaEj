package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Cargo;
import model.DatabaseConnection;
import model.Evento;
import model.Usuario;

public class EventoDAO {

    private Connection connection;

    public EventoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // RF13 — cadastra um novo evento vinculado à EJ do usuário
    // o criador é automaticamente adicionado como convidado/participante
    public Evento cadastrar(String titulo, String descricao, Date data, Long empresaJuniorId, Long criadorId) {
        String sql = "INSERT INTO evento (titulo, descricao, data, empresa_junior_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, titulo);
            stmt.setString(2, descricao);
            stmt.setTimestamp(3, new Timestamp(data.getTime()));
            stmt.setLong(4, empresaJuniorId);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long eventoId = keys.getLong(1);
                convidar(eventoId, criadorId);
                return new Evento(eventoId, titulo, descricao, data, empresaJuniorId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar evento: " + e.getMessage());
        }
        return null;
    }

    // RF12 — lista os eventos em que o usuário é convidado/participante,
    // filtrados por mês e ano, ordenados por data
    public List<Evento> listarPorMes(Long usuarioId, int mes, int ano) {
        String sql = "SELECT e.* FROM evento e " +
                     "JOIN evento_convidado ec ON ec.evento_id = e.id " +
                     "WHERE ec.usuario_id = ? " +
                     "AND MONTH(e.data) = ? AND YEAR(e.data) = ? " +
                     "ORDER BY e.data";
        List<Evento> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evento e = mapear(rs);
                e.setConvidados(listarConvidados(e.getId()));
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar eventos: " + ex.getMessage());
        }
        return lista;
    }

    // busca um evento específico, garantindo que pertence à EJ informada
    public Evento buscarPorId(Long id, Long empresaJuniorId) {
        String sql = "SELECT * FROM evento WHERE id = ? AND empresa_junior_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setLong(2, empresaJuniorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Evento e = mapear(rs);
                e.setConvidados(listarConvidados(e.getId()));
                return e;
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao buscar evento: " + ex.getMessage());
        }
        return null;
    }

    // RF14 — convida um usuário para um evento
    public boolean convidar(Long eventoId, Long usuarioId) {
        String sql = "INSERT INTO evento_convidado (evento_id, usuario_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, eventoId);
            stmt.setLong(2, usuarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao convidar usuário: " + ex.getMessage());
            return false;
        }
    }

    // verifica se um usuário já foi convidado para o evento
    public boolean jaConvidado(Long eventoId, Long usuarioId) {
        String sql = "SELECT 1 FROM evento_convidado WHERE evento_id = ? AND usuario_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, eventoId);
            stmt.setLong(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.err.println("Erro ao verificar convite: " + ex.getMessage());
            return false;
        }
    }

    // lista os convidados de um evento
    public List<Usuario> listarConvidados(Long eventoId) {
        String sql = "SELECT u.* FROM usuario u " +
                     "JOIN evento_convidado ec ON ec.usuario_id = u.id " +
                     "WHERE ec.evento_id = ?";
        List<Usuario> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, eventoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar convidados: " + ex.getMessage());
        }
        return lista;
    }

    // RN04 — lista os membros da mesma EJ, candidatos a convite
    public List<Usuario> listarMembrosDaEmpresa(Long empresaJuniorId) {
        String sql = "SELECT * FROM usuario WHERE empresa_junior_id = ?";
        List<Usuario> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, empresaJuniorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar membros da empresa: " + ex.getMessage());
        }
        return lista;
    }

    // busca o id da empresa júnior à qual o usuário pertence (null se não vinculado)
    public Long buscarEmpresaJuniorId(Long usuarioId) {
        String sql = "SELECT empresa_junior_id FROM usuario WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("empresa_junior_id");
                return rs.wasNull() ? null : id;
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao buscar empresa do usuário: " + ex.getMessage());
        }
        return null;
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        return new Evento(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            rs.getTimestamp("data"),
            rs.getLong("empresa_junior_id")
        );
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("senha"),
            Cargo.valueOf(rs.getString("cargo"))
        );
    }
}