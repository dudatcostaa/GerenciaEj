package controller;

import java.util.List;
import model.Projeto;
import model.QuadroKamban;
import model.StatusTarefa;
import model.Tarefa;
import view.KambanView;

public class KambanController {
    private KambanView view; // a view é invetada pelo construtor
    private List<Projeto> projetos;

    // construtor
    public KambanController(KambanView view, List<Projeto> projetos) {
        this.view = view; 
        this.projetos = projetos;
    }

    // loop do kamban que estava no main agora fica no controller
    public void iniciar() {
        while (true) {
            int opcao = view.mostrarMenuPrincipal();
            if (opcao == 1) {
                view.listarProjetos(projetos);
            } else if (opcao == 2) {
                acessarKamban();
            } else if (opcao == 0) {
                break;
            } else {
                view.mostrarMensagem("Opção inválida.");
            }
        }
    }

    // verifica se há projetos e, se tiver, exibe eles para que o usuário possa escolher um
    private void acessarKamban() {
        // se não houver nenhum projeto em planejamento ou execução não há oq exibir
        if (projetos.isEmpty()) {
            view.mostrarMensagem("Nenhum projeto em planejamento ou execução");
            return;
        }

        // caso hajam projetos, exibe eles para que o usuário possa escolher um
        // a exibição e a leitura da escolha agora ficam na view, seguindo o padrão MVC
        int escolha = view.escolherProjeto(projetos);

        // identifica qual projeto o usuário escolheu para abrir o kamban certo
        if (escolha >= 0 && escolha < projetos.size()) {
            Projeto p = projetos.get(escolha);
            if (p.getQuadro() == null) {
                p.setQuadro(new QuadroKamban("Kamban: " + p.getNome()));
            }
            menuKamban(p.getQuadro()); // chama o método que realmente permite que mexa no kamban
        } else {
            view.mostrarMensagem("Projeto inválido.");
        }
    }

    // loop para mexer no kamban
    private void menuKamban(QuadroKamban q) {
        while (true) {
            view.exibirQuadro(q); // exibe os dados e as tarefas do kamban, usando as cores definidas no requisito nao funcional e no enum
            
            // chama o método correto de acordo com a opção escolhida pelo usuario
            // o menu e a leitura da opção agora ficam na view
            int opcao = view.mostrarMenuKamban(q.getTitulo());

            if (opcao == 1) {
                // cadastra uma tarefa nova
                String titulo = view.pedirTituloTarefa();
                q.adicionarTarefa(new Tarefa(q.gerarIdTarefa(), titulo));
            } else if (opcao == 2) {
                // move a tarefa entre colunas
                long id = view.pedirIdTarefa(); // pede que o usuário insira o id da tarefa, que é exibido em exibirQuadro()
                Tarefa tarefa = q.getTarefas().stream()
                        .filter(t -> t.getId() == id)
                        .findFirst() // procura pelo primeiro elemento que corresponde com o filtro
                        .orElse(null); // se nao achar retorna null

                // se ele encontrar uma tarefa
                if (tarefa != null) {
                    int novoStatus = view.pedirNovoStatus();
                    if (novoStatus >= 0 && novoStatus <= 3) { // verifica se é algum dos status válidos
                        tarefa.setStatus(StatusTarefa.values()[novoStatus]); // se for, seta o status novo
                    } else {
                        view.mostrarMensagem("Status inválido.");
                    }
                } else {
                    view.mostrarMensagem("Tarefa não encontrada.");
                }
            } else if (opcao == 0) {
                break;
            }
        }
    }
}
