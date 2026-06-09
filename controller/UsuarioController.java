package controller;

import java.util.ArrayList;
import java.util.List;
import model.Cargo;
import model.Usuario;

public class UsuarioController {

    private List<Usuario> usuariosDoSistema = new ArrayList<>();
    private Long idContador = 1L;

    public UsuarioController() {
        // Mocks originais reestruturados
        usuariosDoSistema.add(new Usuario(idContador++, "Maria Eduarda", "maria@gerenciaej.com", "13579", Cargo.DIRETOR));
        usuariosDoSistema.add(new Usuario(idContador++, "Lorena", "lorena@gerenciaej.com", "02468", Cargo.MEMBRO));
        usuariosDoSistema.add(new Usuario(idContador++, "Sofia", "sofia@gerenciaej.com", "12345", Cargo.MEMBRO));
    }

    // >>> ADICIONE ESSE MÉTODO AQUI CORRIGIR O ERRO <<<
    public List<Usuario> getUsuariosDoSistema() {
        return this.usuariosDoSistema;
    }

    // RN10
    public boolean emailExiste(String email) {
        for (Usuario u : usuariosDoSistema) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // Lógica de Autenticação do UC02 (Regra de Negócio separada da View)
    public Usuario autenticarUsuario(String email, String senha) {
        for (Usuario u : usuariosDoSistema) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u; // Retorna o objeto encontrado para a View exibir os dados
            }
        }
        return null; // Caso não encontre nenhum correspondente
    }

    // RF01
    public Usuario cadastrarUsuario(String nome, String email, String senha, Cargo cargo) {
        Usuario novoUsuario = new Usuario(idContador++, nome, email, senha, cargo);
        usuariosDoSistema.add(novoUsuario);
        return novoUsuario;
    }

    // RF02
    public boolean excluirUsuario(String email, String senha) {
        Usuario usuarioParaRemover = null;
        for (Usuario u : usuariosDoSistema) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                usuarioParaRemover = u;
                break;
            }
        }

        if (usuarioParaRemover != null) {
            usuariosDoSistema.remove(usuarioParaRemover);
            return true;
        }
        return false;
    }
}