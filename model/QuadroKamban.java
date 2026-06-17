package model;
import java.util.*;

public class QuadroKamban {
    private static long contadorId = 1; // para iniciar o id dos quadros em 1
    private Long id;
    private String titulo;
    private List<Tarefa> tarefas; 
    private long contadorTarefas; // contador local para o id das tarefas
    private Projeto projeto; 

    // construtor
    public QuadroKamban(String titulo) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.tarefas = new ArrayList<>();
        this.contadorTarefas = 1; 
    }

    // getters e setters 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public long getContadorTarefas() {
        return contadorTarefas;
    }

    public void setContadorTarefas(long contadorTarefas) {
        this.contadorTarefas = contadorTarefas;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    // adiciona uma nova tarefa a lista de tarefas
    public void adicionarTarefa(Tarefa t) {
        tarefas.add(t);
    }

    // gera id para as tarefas incrementando de um em um 
    public long gerarIdTarefa() {
        return contadorTarefas++;
    }
}