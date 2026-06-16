package model.strategy;

import model.EmpresaJunior;
import model.dao.EmpresaJuniorDAO;

import java.util.List;

public interface BuscaEJStrategy {
    List<EmpresaJunior> buscar(String termo, EmpresaJuniorDAO dao);
}