package controller;

import dao.FuncionalidadeDAO;
import model.Cargo;
import model.Funcionalidade;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FuncionalidadeController {

    private FuncionalidadeDAO dao;

    public FuncionalidadeController() {
        this.dao = new FuncionalidadeDAO();
    }

    // UC10 / RF07 — seleciona uma nova funcionalidade
    // RN03 — garante que cada funcionalidade seja adicionada apenas uma vez
    public String adicionar(Long usuarioId, Funcionalidade funcionalidade) {
        if (dao.possui(usuarioId, funcionalidade)) {
            return "[AVISO] \"" + funcionalidade.getDescricao() + "\" já está nas suas ferramentas.";
        }

        boolean ok = dao.adicionar(usuarioId, funcionalidade);
        return ok
            ? "[SUCESSO] \"" + funcionalidade.getDescricao() + "\" adicionada às suas ferramentas."
            : "[ERRO] Não foi possível adicionar a funcionalidade.";
    }

    // UC10 / RF08 — exclui uma funcionalidade
    public String remover(Long usuarioId, Funcionalidade funcionalidade) {
        if (!dao.possui(usuarioId, funcionalidade)) {
            return "[AVISO] \"" + funcionalidade.getDescricao() + "\" não está nas suas ferramentas.";
        }

        boolean ok = dao.remover(usuarioId, funcionalidade);
        return ok
            ? "[SUCESSO] \"" + funcionalidade.getDescricao() + "\" removida das suas ferramentas."
            : "[ERRO] Não foi possível remover a funcionalidade.";
    }

    // lista funcionalidades atualmente selecionadas pelo usuário
    public List<Funcionalidade> listarSelecionadas(Long usuarioId) {
        return dao.listarSelecionadas(usuarioId);
    }

    // funcionalidades ainda não selecionadas
    public List<Funcionalidade> listarDisponiveis(Long usuarioId, Cargo cargo) {
        List<Funcionalidade> selecionadas = dao.listarSelecionadas(usuarioId);
        return Arrays.stream(Funcionalidade.values())
                .filter(f -> !selecionadas.contains(f))
                .filter(f -> f != Funcionalidade.PROJETOS || cargo == Cargo.DIRETOR)
                .collect(Collectors.toList());
    }

    // RN02 — verifica se o usuário pode acessar a funcionalidade
    public boolean possuiAcesso(Long usuarioId, Funcionalidade funcionalidade) {
        return dao.possui(usuarioId, funcionalidade);
    }
}