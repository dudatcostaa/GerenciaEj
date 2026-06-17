package strategy;

import dao.EmpresaJuniorDAO;
import java.util.List;
import model.EmpresaJunior;

// UC03
//Interface para definir a estratégia de busca de empresas juniores

public interface BuscaEJStrategy {
    List<EmpresaJunior> buscar(String termo, EmpresaJuniorDAO dao);
}