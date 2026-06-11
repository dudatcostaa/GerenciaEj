package controller;

import dao.EmpresaJuniorDAO;
import model.EmpresaJunior;
import strategy.BuscaEJStrategy;
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