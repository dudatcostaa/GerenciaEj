package model.strategy;

import model.EmpresaJunior;
import model.dao.EmpresaJuniorDAO;

import java.util.List;

public class BuscarPorNomeStrategy implements BuscaEJStrategy {
    @Override
    public List<EmpresaJunior> buscar(String termo, EmpresaJuniorDAO dao) {
        // Passa o termo digitado para filtrar no SQL
        return dao.buscarPorNome(termo);
    }
}