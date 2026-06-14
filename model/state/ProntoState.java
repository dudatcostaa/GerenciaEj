package model.state;

import model.StatusTarefa;
import model.Tarefa;

public class ProntoState implements EstadoTarefa {

    @Override
    public String avancar(Tarefa tarefa) {
        // última coluna do quadro — não há próximo estado
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