package controller;

import java.util.List;

import dao.UsuarioDAO;
import model.Cargo;
import model.Usuario;

public class UsuarioController {

    private UsuarioDAO dao;

    public UsuarioController() {
        this.dao = new UsuarioDAO();
    }

    public List<Usuario> getUsuariosDoSistema() {
        return dao.listarTodos();
    }

    // RN10
    public boolean emailExiste(String email) {
        return dao.emailExiste(email);
    }

    // UC02
    public Usuario autenticarUsuario(String email, String senha) {
        return dao.autenticar(email, senha);
    }

    // RF01
    public Usuario cadastrarUsuario(String nome, String email, String senha, Cargo cargo) {
        return dao.cadastrar(nome, email, senha, cargo);
    }

    // RF02
    public boolean excluirUsuario(String email, String senha) {
        return dao.excluir(email, senha);
    }
}