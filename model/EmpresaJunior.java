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
        this.status = StatusEJ.PENDENTE;
        this.usuarios = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public StatusEJ getStatus() {
        return status;
    }

    public void setStatus(StatusEJ status) {
        this.status = status;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    
}