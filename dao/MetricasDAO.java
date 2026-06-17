package dao;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import model.DatabaseConnection;

public class MetricasDAO {

    private Connection connection;

    public MetricasDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // nome da ej do usuário logado
    public String buscarNomeEJ(Long usuarioId) {
        String sql = "SELECT ej.nome FROM empresa_junior ej " +
                     "JOIN usuario u ON u.empresa_junior_id = ej.id " +
                     "WHERE u.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("nome");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar nome da EJ: " + e.getMessage());
        }
        return null;
    }

    // soma o valor de todos os projetos do ano
    public double buscarFaturamentoAno() {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM projeto " +
                     "WHERE YEAR(data_inicio) = YEAR(CURDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar faturamento do ano: " + e.getMessage());
        }
        return 0;
    }

    // soma o valor de todos os projetos de todos os tempos
    public double buscarFaturamentoTotal() {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM projeto";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar faturamento total: " + e.getMessage());
        }
        return 0;
    }

    // conta quantos usuários existem no sistema
    public int buscarQuantidadeMembros() {
        String sql = "SELECT COUNT(*) AS total FROM usuario";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar quantidade de membros: " + e.getMessage());
        }
        return 0;
    }

    // retorna a quantidade de projetos por status
    public Map<String, Integer> buscarProjetosPorStatus() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        // garante que a quantidade de todos começa em zero
        mapa.put("EM_PLANEJAMENTO", 0);
        mapa.put("EM_EXECUCAO", 0);
        mapa.put("FINALIZADO", 0);

        String sql = "SELECT status, COUNT(*) AS total FROM projeto GROUP BY status";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mapa.put(rs.getString("status"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos por status: " + e.getMessage());
        }
        return mapa;
    }

    // retorna a quantidade de leads por status
    public Map<String, Integer> buscarLeadsPorStatus() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        mapa.put("PROSPECCAO", 0);
        mapa.put("NEGOCIACAO", 0);
        mapa.put("FECHADO", 0);
        mapa.put("PERDIDO", 0);

        String sql = "SELECT status_lead, COUNT(*) AS total FROM leads GROUP BY status_lead";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mapa.put(rs.getString("status_lead"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar leads por status: " + e.getMessage());
        }
        return mapa;
    }

    // taxa de conversão: leads fechados / total de leads * 100
    public double buscarTaxaConversao() {
        String sql = "SELECT " +
                     "  COUNT(*) AS total, " +
                     "  SUM(CASE WHEN status_lead = 'FECHADO' THEN 1 ELSE 0 END) AS fechados " +
                     "FROM leads";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total   = rs.getInt("total");
                int fechados = rs.getInt("fechados");
                if (total == 0) return 0;
                return (fechados * 100.0) / total;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar taxa de conversão: " + e.getMessage());
        }
        return 0;
    }
}