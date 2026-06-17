package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento {
    private Long id;
    private String titulo;
    private String descricao;
    private Date data; // inclui data e horário
    private Long empresaJuniorId;
    private List<Usuario> convidados;

    public Evento(Long id, String titulo, String descricao, Date data, Long empresaJuniorId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.empresaJuniorId = empresaJuniorId;
        this.convidados = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getData() {
        return data;
    }

    public Long getEmpresaJuniorId() {
        return empresaJuniorId;
    }

    public List<Usuario> getConvidados() {
        return convidados;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setEmpresaJuniorId(Long id) {
        this.empresaJuniorId = id;
    }

    public void setConvidados(List<Usuario> convidados) {
        this.convidados = convidados;
    }

    @Override
    public String toString() {
        String dataStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
        return String.format("[%d] %s - %s", id, titulo, dataStr);
    }
}