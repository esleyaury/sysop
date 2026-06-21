package br.upe.so.memory;

public class VirtualMemorySystem{

  private VirtualMemory virutalMem;
  private PhysicalMemory physicalMem;


  private void ocuparFrame(int frame, int pagvirt, int valor){
    physicalMem.write(frame, valor);
    physicalMem.ocupar(frame, pagvirt);

    Pagina p = virutalMem.getPagina(pagvirt);
    p.setPresente(true);
    p.setNumeroFrame(frame);
    p.setReferenciado(true);
    p.setLastUsed(System.currentTimeMillis());
  }
}
