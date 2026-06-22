package br.upe.so.memory;

public class VirtualMemory{
  private Pagina[] tabela;
  private int tamanho;

  public VirtualMemory(){
    this.tamanho = 10;
    this.tabela = new Pagina[this.tamanho];
  }

  public Pagina getPagina(int endereco){ return this.tabela[endereco]; }

  public void setPagina(int endereco, Pagina entrada){
    this.tabela[endereco] = entrada;
  }
}
