package model.state;

import model.StatusTarefa;
import model.Tarefa;

public class ProntoState implements EstadoTarefa {

    @Override
    public String avancar(Tarefa tarefa) {
        // não tem como avançar, como é o último
        return "[AVISO] A tarefa já está em PRONTO, não é possível avançar.";
    }

    @Override
    public String voltar(Tarefa tarefa) {
        tarefa.setEstado(new EmRevisaoState());
        return "[OK] Tarefa movida para EM_REVISAO.";
    }

    @Override
    public StatusTarefa getStatus() {
        return StatusTarefa.PRONTO;
    }
}