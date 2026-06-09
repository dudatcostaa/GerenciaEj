package model;

public class Usuario {
    private Long id;
    private String nome;
    private Cargo cargo;

    // construtor
    public Usuario(Long id, String nome, Cargo cargo) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Cargo getCargo() {
        return cargo;
    }
}
