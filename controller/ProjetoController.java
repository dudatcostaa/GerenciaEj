package controller;

import dao.ProjetoDAO;
import model.Projeto;
import model.StatusProjeto;

import java.util.Date;
import java.util.List;

public class ProjetoController {

    private ProjetoDAO dao;

    public ProjetoController() {
        this.dao = new ProjetoDAO();
    }

    // UC — cadastra um novo projeto
    public Projeto cadastrar(String nome, String descricao, StatusProjeto status, double valor) {
        return dao.cadastrar(nome, descricao, status, valor);
    }

    // UC — lista todos os projetos existentes
    public List<Projeto> listarTodos() {
        return dao.listarTodos();
    }

    // UC — edita nome, descrição, status, data de início e/ou valor de um projeto
    public boolean editar(Long id, String novoNome, String novaDescricao,
                          StatusProjeto novoStatus, Date novaDataInicio, double novoValor) {

        Projeto p = new Projeto(id, novoNome, novaDescricao, novoStatus);
        p.setDataInicio(novaDataInicio);
        p.setValor(novoValor);
        return dao.atualizar(p);
    }

    // UC — exclui o projeto e, em cascata, seu quadro e todas as tarefas
    public boolean excluir(Long id) {
        return dao.excluir(id);
    }
}