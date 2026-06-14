package controller;

import java.util.List;
import model.Projeto;
import dao.ProjetoDAO;
import model.QuadroKamban;
import dao.QuadroKambanDAO;
import model.Tarefa;
import dao.TarefaDAO;
import view.KambanView;

public class KambanController {
    private KambanView view;
    private ProjetoDAO projetoDAO;
    private QuadroKambanDAO quadroDAO;
    private TarefaDAO tarefaDAO;

    public KambanController(KambanView view) {
        this.view = view;
        this.projetoDAO = new ProjetoDAO();
        this.quadroDAO = new QuadroKambanDAO();
        this.tarefaDAO = new TarefaDAO();
    }

    public void iniciar() {
        while (true) {
            int opcao = view.mostrarMenuPrincipal();
            if (opcao == 1) {
                List<Projeto> projetos = projetoDAO.listarAtivos();
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

    private void acessarKamban() {
        List<Projeto> projetos = projetoDAO.listarAtivos();

        if (projetos.isEmpty()) {
            view.mostrarMensagem("Nenhum projeto em planejamento ou execução.");
            return;
        }

        int escolha = view.escolherProjeto(projetos);

        if (escolha >= 0 && escolha < projetos.size()) {
            Projeto p = projetos.get(escolha);
            QuadroKamban quadro = quadroDAO.buscarOuCriar(p);
            if (quadro != null) {
                // carrega as tarefas do banco no quadro
                quadro.setTarefas(tarefaDAO.listarPorQuadro(quadro.getId()));
                menuKamban(quadro);
            }
        } else {
            view.mostrarMensagem("Projeto inválido.");
        }
    }

    private void menuKamban(QuadroKamban q) {
        while (true) {
            view.exibirQuadro(q);
            int opcao = view.mostrarMenuKamban(q.getTitulo());

            if (opcao == 1) {
                // cadastra tarefa nova no banco e adiciona ao quadro em memória
                String titulo = view.pedirTituloTarefa();
                Tarefa nova = tarefaDAO.cadastrar(titulo, q.getId());
                if (nova != null) {
                    q.adicionarTarefa(nova);
                    quadroDAO.atualizarContador(q);
                }
            } else if (opcao == 2) {
                // move tarefa entre colunas (padrão State) e salva no banco
                long id = view.pedirIdTarefa();
                Tarefa tarefa = q.getTarefas().stream()
                        .filter(t -> t.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (tarefa != null) {
                    int direcao = view.pedirDirecaoMovimento();
                    String resultado;

                    if (direcao == 1) {
                        resultado = tarefa.avancar();
                    } else if (direcao == 2) {
                        resultado = tarefa.voltar();
                    } else {
                        view.mostrarMensagem("Opção inválida.");
                        continue;
                    }

                    view.mostrarMensagem(resultado);
                    // persiste o status atual (pode não ter mudado, se a tarefa
                    // já estava na primeira ou última coluna)
                    tarefaDAO.atualizarStatus(tarefa.getId(), tarefa.getStatus());
                } else {
                    view.mostrarMensagem("Tarefa não encontrada.");
                }
            } else if (opcao == 0) {
                break;
            }
        }
    }
}