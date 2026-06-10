package model;
import java.util.Date;

public class Arquivo {
    private Long id;
    private String nome;
    private String url;
    private Long autorId;
    private Date dataUpload;

    // construtor
    public Arquivo(Long id, String nome, String url, Long autorId) {
        this.id = id;
        this.nome = nome;
        this.url = url;
        this.autorId = autorId;
        this.dataUpload = new Date();
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

    public Long getAutorId() {
        return autorId;
    }

    public Date getDataUpload() {
        return dataUpload;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }
}
