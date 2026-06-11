package strategy;

import model.EmpresaJunior;
import dao.EmpresaJuniorDAO;
import java.util.List;

public class BuscarTodasStrategy implements BuscaEJStrategy {
    @Override
    public List<EmpresaJunior> buscar(String termo, EmpresaJuniorDAO dao) {
        // Ignora o termo de pesquisa e traz tudo do banco
        return dao.listarTodas();
    }
}