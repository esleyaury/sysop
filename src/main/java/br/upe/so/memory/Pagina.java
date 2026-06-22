package br.upe.so.memory;

public class Pagina{
  private int paginaVirtual;
  private int numeroFrame;
  private boolean presente;
  private boolean referenciado;
  private boolean modificado;
  private long lastUsed;

  public int getNumeroFrame() { return numeroFrame; }
  public void setNumeroFrame(int numeroFrame){ this.numeroFrame = numeroFrame; }

  public boolean getPresente() { return presente; }
  public void setPresente(boolean presente){ this.presente = presente; }

  public boolean getReferenciado(){ return referenciado; }
  public void setReferenciado(boolean referenciado){ 
   this.referenciado = referenciado;
  }

  public boolean getModificado() { return modificado; }
  public boolean setModificado(boolean modificado){
    this.modificado = modificado;
  }

  public long getLastUsed(){ return lastUsed; }
  public void setLastUsed(long lastUsed){ this.lastUsed = lastUsed;}
}

