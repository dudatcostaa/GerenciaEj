package model;
import java.util.Date;

public class Projeto {
    private Long id;
    private String nome;
    private String descricao;
    private Date dataInicio;
    private StatusProjeto status;
    private QuadroKamban quadro;
    private double valor;

    // construtor
    public Projeto(Long id, String nome, String descricao, StatusProjeto status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = new Date();
        this.status = status;
        this.quadro = null;
        this.valor = 0.0;
    }

    // getters e setters
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public QuadroKamban getQuadro() {
        return quadro;
    }

    public void setQuadro(QuadroKamban quadro) {
        this.quadro = quadro;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", status, nome);
    }
}