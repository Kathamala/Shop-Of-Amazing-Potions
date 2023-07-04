package entidade;

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
}
