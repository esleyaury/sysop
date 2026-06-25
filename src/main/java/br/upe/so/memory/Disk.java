package br.upe.so.memory;

public class Disk {
  // Area de Swap, ou seja, parte do disco que eh usada como "RAM"
  private int[] swap;
  private int tamanhoDisco;

  public Disk(int tamanhoDisco){
    this.tamanhoDisco = tamanhoDisco;
    this.swap = new int[this.tamanhoDisco];
  }

  public Disk(int tamanhoDisco, int[] dadosIniciais){
    this.tamanhoDisco = tamanhoDisco;
    this.swap = dadosIniciais.clone();
  }

  public int loadPage(int pagina){
    return this.swap[pagina];
  }

  public void savePage(int pagina, int valor){
    this.swap[pagina] = valor;
  }

  public int getTamanhoDisco(){
    return this.tamanhoDisco;
  }
  // deletar Pagina no disco, faz sentido?
}
