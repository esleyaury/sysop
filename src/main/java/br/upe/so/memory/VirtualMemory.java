package br.upe.so.memory;

public class VirtualMemory{
  private Pagina[] tabela;
  private int tamanhoVirtual;

  public VirtualMemory(int tamanhoVirtual){
    this.tamanhoVirtual = tamanhoVirtual;
    this.tabela = new Pagina[this.tamanhoVirtual];
  }

  public Pagina getPagina(int endereco){ return this.tabela[endereco]; }

  public void setPagina(int endereco, Pagina entrada){
    this.tabela[endereco] = entrada;
  }
}
