package br.upe.so.memory;

public class PhysicalMemory{

  private int[] frames; //Conteudo sendo "Rodado"
  private int[] paginaOcupante; //A pagina a qual tal conteudo pertence.
  private int tamanhoFisico;

  public PhysicalMemory(int tamanhoFisico){
    this.tamanhoFisico = tamanhoFisico;
    this.frames = new int[this.tamanhoFisico];
    this.paginaOcupante = new int[this.tamanhoFisico];
    java.util.Arrays.fill(paginaOcupante, -1);
    // precisa saber como verificar se o frames esta com espaco livre
  }

  public int read(int frame){
    return this.frames[frame];
  }

  public void write(int frame, int valor){
    this.frames[frame] = valor;
  }

  //Precisa ainda checar se o frame ta vazio.
  public boolean hasEmptyFrame(){
    for (int p : paginaOcupante){
      if (p == -1) return true;
    }
    return false;
  }

  public int proximoFrameLivre(){
    for (int i = 0; i < tamanhoFisico; i++){
      if (paginaOcupante[i] == -1) return i;
    }
    return -1;
  }

  // mapeamento inverso, pro WSClock saber, qual pagina retirar

  public int getPaginaOcupante(int frame){
    return paginaOcupante[frame];
  }
  
  public void ocupar(int frame, int paginaVirtual){
    this.paginaOcupante[frame] = paginaVirtual;
  }

  public void liberar(int frame){
    paginaOcupante[frame] = -1;
  }

  public int getTamanhoFisico(){
    return this.tamanhoFisico;
  }
}
