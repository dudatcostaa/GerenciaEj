package controller;

import dao.MetricasDAO;
import java.util.Map;

public class MetricasController {

    private MetricasDAO dao;

    public MetricasController() {
        this.dao = new MetricasDAO();
    }

    public String buscarNomeEJ(Long usuarioId) {
        return dao.buscarNomeEJ(usuarioId);
    }

    public double buscarFaturamentoAno() {
        return dao.buscarFaturamentoAno();
    }

    public double buscarFaturamentoTotal() {
        return dao.buscarFaturamentoTotal();
    }

    public int buscarQuantidadeMembros() {
        return dao.buscarQuantidadeMembros();
    }

    public Map<String, Integer> buscarProjetosPorStatus() {
        return dao.buscarProjetosPorStatus();
    }

    public Map<String, Integer> buscarLeadsPorStatus() {
        return dao.buscarLeadsPorStatus();
    }

    public double buscarTaxaConversao() {
        return dao.buscarTaxaConversao();
    }
}