package model;

import model.state.EstadoTarefa;
import model.state.EstadoTarefaFactory;
import model.state.PendenteState;

public class Tarefa {
    private Long id;
    private String titulo;
    private EstadoTarefa estado;

    // construtor
    public Tarefa(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.estado = new PendenteState(); // a tarefa é inicializada como pendente
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

    // mantém compatibilidade com o restante do sistema (persistência e exibição)
    public StatusTarefa getStatus() {
        return estado.getStatus();
    }

    // usado pelo TarefaDAO ao carregar a tarefa do banco
    public void setStatus(StatusTarefa status) {
        this.estado = EstadoTarefaFactory.criar(status);
    }

    public EstadoTarefa getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarefa estado) {
        this.estado = estado;
    }

    // ─── padrão State (UC13) ────────────────────────────────────────────────────

    // tenta mover a tarefa para a próxima coluna do quadro
    public String avancar() {
        return estado.avancar(this);
    }

    // tenta mover a tarefa para a coluna anterior do quadro
    public String voltar() {
        return estado.voltar(this);
    }

    @Override
    public String toString() {
        StatusTarefa status = getStatus();
        return String.format("[%d] %s%s%s - %s",
            id, status.getCor(), status.name(), StatusTarefa.RESET, titulo);
    }
}