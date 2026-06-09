package model;

import java.util.Date;

public class SolicitacaoEJ {
    private Long id;
    private StatusSolicitacao status;
    private String documentoUrl;
    private Date dataSolicitacao;
    private Usuario usuario; // Relação: criada por 1 Usuario
    private EmpresaJunior empresaJunior; // Relação: gera 0..1 EmpresaJunior

    // construtor completo alinhado ao diagrama de classes
    public SolicitacaoEJ(Long id, String documentoUrl, Usuario usuario, EmpresaJunior ej) {
        this.id = id;
        this.documentoUrl = documentoUrl;
        this.usuario = usuario;
        this.empresaJunior = ej;
        this.status = StatusSolicitacao.PENDENTE;
        this.dataSolicitacao = new Date(); // registra o momento da solicitação
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public String getDocumentoUrl() {
        return documentoUrl;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public EmpresaJunior getEmpresaJunior() {
        return empresaJunior;
    }
}