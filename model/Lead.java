package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Lead {
    private Long id;
    private String nomeCliente;
    private StatusLead StatusLead;
    private Date dataCriacao;
    private Date dataUltimaModificacao;

    public Lead(Long id, String nomeCliente) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.StatusLead = model.StatusLead.PROSPECCAO;
        this.dataCriacao = new Date();
        this.dataUltimaModificacao = this.dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public StatusLead getStatusLead() {
        return StatusLead;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Date getDataUltimaModificacao() {
        return dataUltimaModificacao;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataUltimaModificacao(Date dataUltimaModificacao) {
        this.dataUltimaModificacao = dataUltimaModificacao;
    }

    public void setStatusLead(StatusLead StatusLead) {
        this.StatusLead = StatusLead;
        this.dataUltimaModificacao = new Date();
    }

    @Override
    public String toString() {
        // apenas com data
        SimpleDateFormat formataCriacao = new SimpleDateFormat("dd/MM/yyyy");
        String criacaoStr = formataCriacao.format(dataCriacao);

        // com data e hora
        SimpleDateFormat formataModificacao = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        String modStr = formataModificacao.format(dataUltimaModificacao);

        return "[ID: " + id + "] Cliente: " + nomeCliente +
                "\n      (Criado em: " + criacaoStr + " | Última alteração: " + modStr + ")";
    }
}