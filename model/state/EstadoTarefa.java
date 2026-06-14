package model.state;

import model.StatusTarefa;
import model.Tarefa;

/**
 * Padrão State (UC13 — Operar quadro kamban).
 *
 * Cada implementação representa uma coluna do quadro (PENDENTE,
 * EM_PROGRESSO, EM_REVISAO, PRONTO) e sabe para quais estados a tarefa
 * pode avançar ou voltar a partir dali (RN08 — uma tarefa só pode estar
 * em uma coluna por vez, e a transição é sempre para uma coluna vizinha).
 */
public interface EstadoTarefa {

    // move a tarefa para a próxima coluna do fluxo; retorna uma mensagem
    // descrevendo o resultado (sucesso ou aviso de que não há próximo estado)
    String avancar(Tarefa tarefa);

    // move a tarefa para a coluna anterior do fluxo; retorna uma mensagem
    // descrevendo o resultado (sucesso ou aviso de que não há estado anterior)
    String voltar(Tarefa tarefa);

    // status correspondente a este estado, usado para persistência no
    // banco e para exibição (cores definidas pela RNF04)
    StatusTarefa getStatus();
}