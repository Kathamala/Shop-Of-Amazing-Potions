package com.imd.soapfront.model;

public class NPC {
    
    private Integer id;

    private String nome;
    
    private Integer tempoDeEspera;

    private Integer verbaPeriodicidade;

    private Float verbaValorBase;

    private Float verbaMultiplicador;

    private Integer jogadorId;

    public NPC() {
    }

    public NPC(Integer id, String nome, Integer tempoDeEspera, Integer verbaPeriodicidade, Float verbaValorBase, Float verbaMultiplicador, Integer jogadorId) {
        this.id = id;
        this.nome = nome;
        this.tempoDeEspera = tempoDeEspera;
        this.verbaPeriodicidade = verbaPeriodicidade;
        this.verbaValorBase = verbaValorBase;
        this.verbaMultiplicador = verbaMultiplicador;
        this.jogadorId = jogadorId;
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

    public Integer getTempoDeEspera() {
        return this.tempoDeEspera;
    }

    public void setTempoDeEspera(Integer tempoDeEspera) {
        this.tempoDeEspera = tempoDeEspera;
    }

    public Integer getVerbaPeriodicidade() {
        return this.verbaPeriodicidade;
    }

    public void setVerbaPeriodicidade(Integer verbaPeriodicidade) {
        this.verbaPeriodicidade = verbaPeriodicidade;
    }

    public Float getVerbaValorBase() {
        return this.verbaValorBase;
    }

    public void setVerbaValorBase(Float verbaValorBase) {
        this.verbaValorBase = verbaValorBase;
    }

    public Float getVerbaMultiplicador() {
        return this.verbaMultiplicador;
    }

    public void setVerbaMultiplicador(Float verbaMultiplicador) {
        this.verbaMultiplicador = verbaMultiplicador;
    }

    public Integer getJogadorId() {
        return this.jogadorId;
    }

    public void setJogadorId(Integer jogadorId) {
        this.jogadorId = jogadorId;
    }
}
