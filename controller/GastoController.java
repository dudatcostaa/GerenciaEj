package controller;

import dao.GastoDAO;
import model.Gasto;

import java.util.Date;
import java.util.List;

public class GastoController {

    private GastoDAO dao;

    public GastoController() {
        this.dao = new GastoDAO();
    }

    // permite cadastrar um gasto
    public Gasto cadastrar(String descricao, double valor, Date data) {
        return dao.cadastrar(descricao, valor, data);
    }

    // lista os gastos do mês
    public List<Gasto> listarPorMes(int mes, int ano) {
        return dao.listarPorMes(mes, ano);
    }

    // retorna o total gasto do mês
    public double totalPorMes(int mes, int ano) {
        return dao.totalPorMes(mes, ano);
    }
}