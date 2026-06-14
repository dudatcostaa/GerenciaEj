package model.state;

import model.StatusTarefa;
import model.Tarefa;

public class EmProgressoState implements EstadoTarefa {

    @Override
    public String avancar(Tarefa tarefa) {
        tarefa.setEstado(new EmRevisaoState());
        return "[OK] Tarefa movida para EM_REVISAO.";
    }

    @Override
    public String voltar(Tarefa tarefa) {
        tarefa.setEstado(new PendenteState());
        return "[OK] Tarefa movida para PENDENTE.";
    }

    @Override
    public StatusTarefa getStatus() {
        return StatusTarefa.EM_PROGRESSO;
    }
}