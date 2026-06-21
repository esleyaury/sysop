package br.upe.so.memory;

public class {
  private int[] swap;
  private int tamanho;

  public Disk(){
    this.tamanho = 10;
    this.swap = new int[this.tamanho];
  }

  public int carregar(int pagina){
    return this.swap[pagina];
  }

  public void salvar(int pagina, int valor){
    this.swap[pagina] = valor;
  }

  public int getTamanho(){
    return this.tamanho;
  }
}
