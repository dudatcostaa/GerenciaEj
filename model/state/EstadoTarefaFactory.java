package model.state;

import model.StatusTarefa;

/**
 * Converte o enum StatusTarefa no objeto de estado correspondente do padrão State.
 */
public class EstadoTarefaFactory {

    public static EstadoTarefa criar(StatusTarefa status) {
        switch (status) {
            case PENDENTE:     return new PendenteState();
            case EM_PROGRESSO: return new EmProgressoState();
            case EM_REVISAO:   return new EmRevisaoState();
            case PRONTO:       return new ProntoState();
            default:
                throw new IllegalArgumentException("Status desconhecido: " + status);
        }
    }
}