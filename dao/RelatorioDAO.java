package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.DatabaseConnection;
import model.Gasto;
import model.Proposta;

public class RelatorioDAO {

    private Connection connection;

    public RelatorioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // faturamento: soma dos projetos cuja data_inicio cai na semana
    public double buscarFaturamentoPeriodo(Date inicio, Date fim) {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM projeto " +
                     "WHERE data_inicio BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(inicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fim.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar faturamento do período: " + e.getMessage());
        }
        return 0;
    }

    // gastos lançados na semana
    public List<Gasto> buscarGastosPeriodo(Date inicio, Date fim) {
        String sql = "SELECT * FROM gasto_mensal WHERE data BETWEEN ? AND ? ORDER BY data";
        List<Gasto> lista = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(inicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fim.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Gasto(
                    rs.getLong("id"),
                    rs.getString("descricao"),
                    rs.getDouble("valor"),
                    rs.getDate("data")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar gastos do período: " + e.getMessage());
        }
        return lista;
    }

    // propostas enviadas na semana
    public List<Proposta> buscarPropostasPeriodo(Date inicio, Date fim) {
        String sql = "SELECT * FROM proposta WHERE data BETWEEN ? AND ? ORDER BY data";
        List<Proposta> lista = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(inicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fim.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Proposta(
                    rs.getLong("id"),
                    rs.getString("nome_cliente"),
                    rs.getDouble("valor"),
                    rs.getDate("data")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar propostas do período: " + e.getMessage());
        }
        return lista;
    }

    // leads agrupados por status (snapshot atual)
    public Map<String, Integer> buscarLeadsPorStatus() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        mapa.put("PROSPECCAO", 0);
        mapa.put("NEGOCIACAO", 0);
        mapa.put("FECHADO", 0);
        mapa.put("PERDIDO", 0);
        String sql = "SELECT status_lead, COUNT(*) AS total FROM leads GROUP BY status_lead";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) mapa.put(rs.getString("status_lead"), rs.getInt("total"));
        } catch (SQLException e) {
            System.err.println("Erro ao buscar leads: " + e.getMessage());
        }
        return mapa;
    }

    // projetos agrupados por status (snapshot atual)
    public Map<String, Integer> buscarProjetosPorStatus() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        mapa.put("EM_PLANEJAMENTO", 0);
        mapa.put("EM_EXECUCAO", 0);
        mapa.put("FINALIZADO", 0);
        String sql = "SELECT status, COUNT(*) AS total FROM projeto GROUP BY status";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) mapa.put(rs.getString("status"), rs.getInt("total"));
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos: " + e.getMessage());
        }
        return mapa;
    }

    // nome da EJ vinculada ao usuário
    public String buscarNomeEJ(Long usuarioId) {
        String sql = "SELECT ej.nome FROM empresa_junior ej " +
                     "JOIN usuario u ON u.empresa_junior_id = ej.id WHERE u.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("nome");
        } catch (SQLException e) {
            System.err.println("Erro ao buscar nome da EJ: " + e.getMessage());
        }
        return "Não vinculada";
    }
}