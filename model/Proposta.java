package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Proposta {
    private Long id;
    private String nomeCliente;
    private double valor;
    private Date data;

    public Proposta(Long id, String nomeCliente, double valor, Date data) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valor = valor;
        this.data = data;
    }

    public Long getId()             { return id; }
    public String getNomeCliente()  { return nomeCliente; }
    public double getValor()        { return valor; }
    public Date getData()           { return data; }

    public void setId(Long id)              { this.id = id; }
    public void setNomeCliente(String n)    { this.nomeCliente = n; }
    public void setValor(double v)          { this.valor = v; }
    public void setData(Date d)             { this.data = d; }

    @Override
    public String toString() {
        String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        return String.format("[%d] %-30s R$ %,.2f  (%s)", id, nomeCliente, valor, dataStr);
    }
}