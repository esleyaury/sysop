package br.upe.so.memory;

public class Pagina{
  // Atributos - Indetificadores da pagina.
  private int numeroFrame; // Frame fisico a qual a pagina esta usando 
  private boolean presente; // Se esta na memoria fisica
  private boolean referenciado; // Se ele foi lido
  private boolean modificado; // Se ele foi alterado
  private long lastUsed; // TimeStamp, da ultima vez que foi usado (Modificado ou Lido);

  // Pegar o Conteudo Guardado pelo pagina, ou mudar ele
  public int getNumeroFrame() { return numeroFrame; }
  public void setNumeroFrame(int numeroFrame){ this.numeroFrame = numeroFrame; }

  // Pegar a informacao se ele esta em execucao ou nao, e mudar esse status
  public boolean getPresente() { return presente; }
  public void setPresente(boolean presente){ this.presente = presente; }

  // Pegar a info. de que ele foi lido, ou mudar esss status
  public boolean getReferenciado(){ return referenciado; }
  public void setReferenciado(boolean referenciado){ 
   this.referenciado = referenciado;
  }

  // Pegar a info. se ele foi modificado. e mudar esse estado
  public boolean getModificado() { return modificado; }
  public void setModificado(boolean modificado){
    this.modificado = modificado;
  }

  // Pegar o timestamp, e setar esse timestamp
  public long getLastUsed(){ return lastUsed; }
  public void setLastUsed(long lastUsed){ this.lastUsed = lastUsed;}
}

