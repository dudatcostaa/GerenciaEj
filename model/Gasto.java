package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Gasto {
    private Long id;
    private String descricao;
    private double valor;
    private Date data;

    public Gasto(Long id, String descricao, double valor, Date data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public Long getId()          { return id; }
    public String getDescricao() { return descricao; }
    public double getValor()     { return valor; }
    public Date getData()        { return data; }

    public void setId(Long id)             { this.id = id; }
    public void setDescricao(String d)     { this.descricao = d; }
    public void setValor(double v)         { this.valor = v; }
    public void setData(Date d)            { this.data = d; }

    @Override
    public String toString() {
        String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        return String.format("[%d] %-30s R$ %,.2f  (%s)", id, descricao, valor, dataStr);
    }
}