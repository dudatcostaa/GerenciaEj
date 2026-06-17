package controller;

import dao.EmpresaJuniorDAO;
import java.util.List;
import model.EmpresaJunior;
import strategy.BuscaEJStrategy;

public class EmpresaJuniorController {
    private EmpresaJuniorDAO empresaJuniorDAO;

    public EmpresaJuniorController() {
        this.empresaJuniorDAO = new EmpresaJuniorDAO();
    }

    // UC03
    public List<EmpresaJunior> realizarBusca(BuscaEJStrategy estrategia, String termo) {
        return estrategia.buscar(termo, empresaJuniorDAO);
    }
}