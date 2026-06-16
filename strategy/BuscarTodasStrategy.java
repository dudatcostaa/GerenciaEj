package strategy;

import dao.EmpresaJuniorDAO;
import java.util.List;
import model.EmpresaJunior;

// UC03
// Implementação da estratégia de busca de todas as empresas juniores

public class BuscarTodasStrategy implements BuscaEJStrategy {
    @Override
    public List<EmpresaJunior> buscar(String termo, EmpresaJuniorDAO dao) {
        return dao.listarTodas();
    }
}