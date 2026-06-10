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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public String getDocumentoUrl() {
        return documentoUrl;
    }

    public void setDocumentoUrl(String documentoUrl) {
        this.documentoUrl = documentoUrl;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EmpresaJunior getEmpresaJunior() {
        return empresaJunior;
    }

    public void setEmpresaJunior(EmpresaJunior empresaJunior) {
        this.empresaJunior = empresaJunior;
    }

    // Getters e Setters

}