package controller;

import model.SolicitacaoEJ;
import view.AdminView;
import model.StatusSolicitacao;
import model.Cargo; // <-- Adicionei esse import (caso Cargo seja um enum separado)
import model.StatusEJ;

public class SolicitacaoController {
    private AdminView view;
    
    public SolicitacaoController(AdminView view) {
        this.view = view;
    }

    public void avaliarSolicitacao(SolicitacaoEJ solicitacao) {
        
        view.exibirDetalhesSolicitacao(
            solicitacao.getEmpresaJunior().getNome(),
            solicitacao.getUsuario().getNome(),
            solicitacao.getDocumentoUrl()
        );

        int opcao = view.pedirDecisao();
        System.out.println("\nProcessando a solicitação...");

        if (opcao == 1) { // Aprovado
            // CORRIGIDO: Removido o prefixo SolicitacaoEJ
            solicitacao.setStatus(StatusSolicitacao.APROVADA);
            
            // CORRIGIDO: Vai funcionar assim que você colocar 'public' no enum StatusEJ lá na classe EmpresaJunior
            solicitacao.getEmpresaJunior().setStatus(StatusEJ.ATIVA);
            
            // CORRIGIDO: Usando o Enum importado diretamente
            solicitacao.getUsuario().setCargo(Cargo.DIRETOR);
            
            solicitacao.getUsuario().setEmpresaJunior(solicitacao.getEmpresaJunior());
            
            view.exibirMensagemSucesso(
                solicitacao.getEmpresaJunior().getStatus().toString(), 
                solicitacao.getUsuario().getNome(), 
                solicitacao.getUsuario().getCargo().toString()
            );
        } else if (opcao == 2) { // Reprovado
            // CORRIGIDO: Removido o prefixo SolicitacaoEJ
            solicitacao.setStatus(StatusSolicitacao.REPROVADA);
            
            String urlDocumento = solicitacao.getDocumentoUrl();
            excluirArquivo(urlDocumento); 
            
            view.exibirMensagemReprovacao(urlDocumento);
                    
        } else {
            view.exibirErro();
        }
    }
    
    private void excluirArquivo(String url) {
        // lógica real de deletar arquivo iria aqui
    }
}