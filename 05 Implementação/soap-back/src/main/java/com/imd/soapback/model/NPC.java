package main.java.com.imd.soapback.model;

public class NPC {
    
    private Integer id;

    private String nome;

    private Float dinheiro;
    
    private Integer tempoDeEspera;

    private Integer verbaPeriodicidade;

    private Float verbaValorBase;

    private float verbaMultiplicador;

    private Jogador jogador;

    public NPC() {
    }

    public NPC(Integer id, String nome, Float dinheiro, Integer tempoDeEspera, Integer verbaPeriodicidade, Float verbaValorBase, float verbaMultiplicador, Jogador jogador) {
        this.id = id;
        this.nome = nome;
        this.dinheiro = dinheiro;
        this.tempoDeEspera = tempoDeEspera;
        this.verbaPeriodicidade = verbaPeriodicidade;
        this.verbaValorBase = verbaValorBase;
        this.verbaMultiplicador = verbaMultiplicador;
        this.jogador = jogador;
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

    public Float getDinheiro() {
        return this.dinheiro;
    }

    public void setDinheiro(Float dinheiro) {
        this.dinheiro = dinheiro;
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

    public float getVerbaMultiplicador() {
        return this.verbaMultiplicador;
    }

    public void setVerbaMultiplicador(float verbaMultiplicador) {
        this.verbaMultiplicador = verbaMultiplicador;
    }

    public Jogador getJogador() {
        return this.jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}
