package model;

import java.util.ArrayList;
import java.util.List;

public class EmpresaJunior {
    private Long id;
    private String nome;
    private String cnpj;
    private StatusEJ status;
    private List<Usuario> usuarios;

    public EmpresaJunior(Long id, String nome, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.status = StatusEJ.PENDENTE; // toda EJ começa pendente de validação
        this.usuarios = new ArrayList<>();
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public StatusEJ getStatus() {
        return status;
    }

    public void setStatus(StatusEJ status) {
        this.status = status;
    }
}