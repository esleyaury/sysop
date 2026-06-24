package br.upe.so.process;

public class Operations{
    private int endereco;
    private String tipo;
    private int valor;

    //Construtor Para Escrita
    public Operations(int endereco, String tipo, int valor){
        this.endereco = endereco;
        this.tipo = tipo;
        this.valor = valor;
    }
    //Construtor Para Leitura
    public Operations(int endereco, String tipo){
        this(endereco,tipo,0);
    }
    //getter para endereço
    public int getEndereco(){
        return this.endereco;
    }
    //getter para tipo da operação
    public String getTipo(){
        return this.tipo;
    }
    //getter para valor
    public int getValor(){
        return this.valor;
    }
    
}