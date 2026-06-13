package controller;

import dao.PropostaDAO;
import model.Proposta;

import java.util.Date;
import java.util.List;

public class PropostaController {

    private PropostaDAO dao;

    public PropostaController() {
        this.dao = new PropostaDAO();
    }

    public Proposta cadastrar(String nomeCliente, double valor, Date data) {
        return dao.cadastrar(nomeCliente, valor, data);
    }

    public List<Proposta> listarPorMes(int mes, int ano) {
        return dao.listarPorMes(mes, ano);
    }

    public int contarPorMes(int mes, int ano) {
        return dao.contarPorMes(mes, ano);
    }
}