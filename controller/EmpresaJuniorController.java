package controller;

import model.EmpresaJunior;
import model.dao.EmpresaJuniorDAO;
import model.strategy.BuscaEJStrategy;

import java.util.List;

public class EmpresaJuniorController {
    private EmpresaJuniorDAO empresaJuniorDAO;

    public EmpresaJuniorController() {
        this.empresaJuniorDAO = new EmpresaJuniorDAO();
    }

    // O padrão Strategy brilha exatamente aqui: polimorfismo puro
    public List<EmpresaJunior> realizarBusca(BuscaEJStrategy estrategia, String termo) {
        return estrategia.buscar(termo, empresaJuniorDAO);
    }
}