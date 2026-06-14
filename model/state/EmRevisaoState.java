package model.state;

import model.StatusTarefa;
import model.Tarefa;

public class EmRevisaoState implements EstadoTarefa {

    @Override
    public String avancar(Tarefa tarefa) {
        tarefa.setEstado(new ProntoState());
        return "[OK] Tarefa movida para PRONTO.";
    }

    @Override
    public String voltar(Tarefa tarefa) {
        tarefa.setEstado(new EmProgressoState());
        return "[OK] Tarefa movida para EM_PROGRESSO.";
    }

    @Override
    public StatusTarefa getStatus() {
        return StatusTarefa.EM_REVISAO;
    }
}