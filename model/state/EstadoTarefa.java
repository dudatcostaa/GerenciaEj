package model.state;

import model.StatusTarefa;
import model.Tarefa;

// padrão state (UC13 — operar quadro kamban).
// cada implementação representa uma coluna do quadro e sabe para quais estados a tarefa pode avançar ou voltar a partir dali
public interface EstadoTarefa {

    // move a tarefa para a próxima coluna
    String avancar(Tarefa tarefa);

    // move a tarefa para a coluna anterior
    String voltar(Tarefa tarefa);

    // status correspondente a este estado, para armazenar no banco
    StatusTarefa getStatus();
}