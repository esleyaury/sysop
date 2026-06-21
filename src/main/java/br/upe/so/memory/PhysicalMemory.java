package br.upe.so.memory;

public class PhysicalMemory{

  private int[] frames;
  private int[] paginaOcupante;
  private int tamanho;

  public PhysicalMemory(){
    this.tamanho = 5;
    this.frames = new int[this.tamanho];
    this.paginaOcupante = new int[this.tamanho];
    java.util.Arrays.fill(paginaOcupante, -1);
  }

  public int ler(int frame){
    return this.frames[frame];
  }

  public int escrever(int frame, int valor){
    this.frames[frame] = valor;
  }

  //Precisa ainda checar se o frame ta vazio.

  // mapeamento inverso

  public int getPaginaOcupante(int frame){
    return paginaOcupante[frame];
  }
  
  public void ocupar(int frame, int paginaVirtual){
    this.paginaOcupante[frame] = paginaVirtual;
  }

  public void liberar(int frame){
    paginaOcupante[frame] = -1;
  }

  public int getTamanho(){
    return this.tamanho;
  }
}
