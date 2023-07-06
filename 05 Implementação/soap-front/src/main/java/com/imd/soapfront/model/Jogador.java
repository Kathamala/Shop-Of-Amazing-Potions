package com.imd.soapfront.model;

public class Jogador {
    
    private Integer id;

    private String nome;

    private Float dinheiro;
    
    public Jogador() {
        
    }

    public Jogador(Integer id, String nome, Float dinheiro) {
        this.id = id;
        this.nome = nome;
        this.dinheiro = dinheiro;
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

    public static Jogador fromJson(String message) {
        Jogador obj = new Jogador();
        String current = message;
        obj.setId(Integer.parseInt(current.substring(current.indexOf("id")+4, current.indexOf(","))));
        current = current.substring(current.indexOf("nome"), current.length());
        obj.setNome(current.substring(current.indexOf("nome")+6, current.indexOf(",")));
        current = current.substring(current.indexOf("dinheiro"), current.length());
        obj.setDinheiro(Float.parseFloat(current.substring(current.indexOf("dinheiro")+10, current.indexOf("}")-1)));
        return obj;
    }
}
