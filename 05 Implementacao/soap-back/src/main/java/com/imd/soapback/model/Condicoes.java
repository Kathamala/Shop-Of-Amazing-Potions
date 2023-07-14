package main.java.com.imd.soapback.model;

public class Condicoes {
    
    private Integer id;

    private String nome;

    private String descricao;
    
    private Integer intensidade;

    public Condicoes() {

    }

    public Condicoes(Integer id, String nome, String descricao, Integer intensidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.intensidade = intensidade;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIntensidade() {
        return this.intensidade;
    }

    public void setIntensidade(Integer intensidade) {
        this.intensidade = intensidade;
    }
}
