package controller;

import java.util.List;
import model.Lead;
import model.StatusLead;
import model.dao.LeadDAO;
import view.LeadView;

public class LeadController {

    private LeadView view;
    private LeadDAO dao;

    public LeadController(LeadView view) {
        this.view = view;
        this.dao = new LeadDAO();
    }

    public void iniciarModulo() {
        view.exibirMensagemBoasVindas();
        boolean rodando = true;

        while (rodando) {
            int opcao = view.exibirMenuEPegarOpcao();

            switch (opcao) {
                case 1:
                    cadastrarLead();
                    break;
                case 2:
                    visualizarLeads();
                    break;
                case 3:
                    editarLead();
                    break;
                case 4:
                    System.out.println("\nFechando Módulo de Leads...");
                    rodando = false;
                    break;
                default:
                    view.exibirErro("Opção inválida! Digite de 1 a 4.");
            }
        }
    }

    private void cadastrarLead() {
        String nome = view.pedirNomeNovoLead();
        Lead novoLead = dao.cadastrar(nome);
        if (novoLead != null) {
            view.exibirMensagemSucessoCriacao(nome);
        } else {
            view.exibirErro("Erro ao cadastrar lead.");
        }
    }

    private void visualizarLeads() {
        List<Lead> lista = dao.listarTodos();
        view.exibirPainelLeads(StatusLead.values(), lista);
    }

    private void editarLead() {
        Long id = view.pedirIdEdicao();
        List<Lead> lista = dao.listarTodos();

        Lead leadAlvo = null;
        for (Lead lead : lista) {
            if (lead.getId().equals(id)) {
                leadAlvo = lead;
                break;
            }
        }

        if (leadAlvo == null) {
            view.exibirErro("Lead ID " + id + " não encontrado.");
            return;
        }

        int opcaoStatus = view.pedirNovoStatus();
        StatusLead novoStatus = null;

        switch (opcaoStatus) {
            case 1: novoStatus = StatusLead.PROSPECCAO; break;
            case 2: novoStatus = StatusLead.NEGOCIACAO; break;
            case 3: novoStatus = StatusLead.FECHADO; break;
            case 4: novoStatus = StatusLead.PERDIDO; break;
        }

        if (novoStatus != null) {
            StatusLead statusAntigo = leadAlvo.getStatusLead();
            leadAlvo.setStatusLead(novoStatus);
            dao.atualizarStatus(id, novoStatus);
            view.exibirMensagemSucessoEdicao(leadAlvo.getNomeCliente(), statusAntigo.toString(), novoStatus.toString());
        } else {
            view.exibirErro("Status inválido!");
        }
    }
}