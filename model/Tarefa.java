package model;

public class Tarefa {
    private Long id;
    private String titulo;
    private StatusTarefa status;

    // construtor
    public Tarefa(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.status = StatusTarefa.PENDENTE; // a tarefa é inicializada como pendente
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

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s%s%s - %s", 
            id, status.getCor(), status.name(), StatusTarefa.RESET, titulo);
    }
}