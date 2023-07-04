package main.java.com.imd.soapback.model;

public class Pocao {
    
    private Integer id;

    private String descricao;

    public Pocao() {

    }

    public Pocao(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
