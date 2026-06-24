package br.upe.so.memory;
import br.upe.so.kernel.VirtualMemoryManager;

public class VirtualMemorySystem implements VirtualMemoryManager{

  private VirtualMemory virtualMem;
  private PhysicalMemory physicalMem;
  private Disk disco;
  private WSClock wsclock;
  private final static int VMSIZE = 20;
  private final static int PMSIZE = VMSIZE / 2;
  private final static long TAU = 1000000;

  // Construtor
  public VirtualMemorySystem(int[] dadosPrograma){
    this.virtualMem = new VirtualMemory(VMSIZE);
    this.physicalMem = new PhysicalMemory(PMSIZE);
    this.disco = new Disk(VMSIZE, dadosPrograma);
    this.wsclock = new WSClock(TAU);
    startTable();
  }

  public void startTable(){
    for (int i = 0; i < VMSIZE; i ++){
      this.virtualMem.setPagina(i, new Pagina());
    }
  }
  @Override
  public int read(int enderecoVirtual){
    Pagina p = virtualMem.getPagina(enderecoVirtual);
    if (p.getPresente()){
      return physicalMem.read(p.getNumeroFrame());
    }

    return handlePageFault(enderecoVirtual);
  }

  @Override
  public void write(int enderecoVirtual, int valor){
    Pagina p = virtualMem.getPagina(enderecoVirtual);
    if (p.getPresente()){
      physicalMem.write(p.getNumeroFrame(), valor);
      p.setModificado(true);
      p.setReferenciado(true);
     
     
     
      write(enderecoVirtual, valor);
    }
  }
  private int handlePageFault(int enderecoVirtual){
    int valor = disco.loadPage(enderecoVirtual);

    if (physicalMem.hasEmptyFrame()){
      int frame = physicalMem.proximoFrameLivre();
      ocuparFrame(frame, enderecoVirtual, valor);
    } else {

      int frameVitima = wsclock.selecionarVitima(
        virtualMem, physicalMem, System.currentTimeMillis());

      int paginaVitima = physicalMem.getPaginaOcupante(frameVitima);
      Pagina vitima = virtualMem.getPagina(enderecoVirtual);

      if(vitima.getModificado()){
        disco.savePage(paginaVitima, physicalMem.read(frameVitima));
      }

      vitima.setPresente(false);
      physicalMem.liberar(frameVitima);

      ocuparFrame(frameVitima, enderecoVirtual, valor);
    }
    return valor;

  }

  private void ocuparFrame(int frame, int paginaVirtual, int valor){
    physicalMem.write(frame, valor);
    physicalMem.ocupar(frame, paginaVirtual);

    Pagina p = virtualMem.getPagina(paginaVirtual);
    p.setPresente(true);
    p.setNumeroFrame(frame);
    p.setLastUsed(System.currentTimeMillis());
  }
}
