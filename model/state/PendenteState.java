package model.state;

import model.StatusTarefa;
import model.Tarefa;

public class PendenteState implements EstadoTarefa {

    @Override
    public String avancar(Tarefa tarefa) {
        tarefa.setEstado(new EmProgressoState());
        return "[OK] Tarefa movida para EM_PROGRESSO.";
    }

    @Override
    public String voltar(Tarefa tarefa) {
        // primeira coluna do quadro — não há estado anterior
        return "[AVISO] A tarefa já está em PENDENTE, não é possível voltar.";
    }

    @Override
    public StatusTarefa getStatus() {
        return StatusTarefa.PENDENTE;
    }
}