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

    public Gasto cadastrar(String descricao, double valor, Date data) {
        return dao.cadastrar(descricao, valor, data);
    }

    public List<Gasto> listarPorMes(int mes, int ano) {
        return dao.listarPorMes(mes, ano);
    }

    public double totalPorMes(int mes, int ano) {
        return dao.totalPorMes(mes, ano);
    }
}