package model;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private Double preco;
    private int quantidade;

    public Produto( String nome, Double preco, String descricao, int quantidade){
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public Produto( int id, String nome, Double preco, String descricao, int quantidade){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public int getId() {return id;}
    public void setId(int id) { this.id = id; }

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public Double getPreco() {return preco;}
    public void setPreco(Double preco) {this.preco = preco;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

    public String toString(){
        return " id:" + id + " - " + nome + " - " + descricao + " - R$" + preco + " qnt: " + quantidade;
    }


}
