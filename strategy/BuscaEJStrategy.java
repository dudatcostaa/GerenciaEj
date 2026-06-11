package strategy;

import model.EmpresaJunior;
import dao.EmpresaJuniorDAO;
import java.util.List;

public interface BuscaEJStrategy {
    List<EmpresaJunior> buscar(String termo, EmpresaJuniorDAO dao);
}