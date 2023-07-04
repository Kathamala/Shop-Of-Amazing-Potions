package main.java.com.imd.soapback.model;

public class Ingrediente {
    
    private Integer id;

    private Float valor;

    private String nome;

    private Integer tempoNecessario;

    public Ingrediente() {

    }

    public Ingrediente(Integer id, Float valor, String nome, Integer tempoNecessario) {
        this.id = id;
        this.valor = valor;
        this.nome = nome;
        this.tempoNecessario = tempoNecessario;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getValor() {
        return this.valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTempoNecessario() {
        return this.tempoNecessario;
    }

    public void setTempoNecessario(Integer tempoNecessario) {
        this.tempoNecessario = tempoNecessario;
    }    
}
