package br.upe.so.memory;
import java.util.concurrent.atomic.AtomicInteger;

import br.upe.so.kernel.VirtualMemoryManager;
import br.upe.so.process.LogSO;

public class VirtualMemorySystem implements VirtualMemoryManager{

  private VirtualMemory virtualMem;
  private PhysicalMemory physicalMem;
  private Disk disco;
  private WSClock wsclock;
  private int VMSIZE;
  private int PMSIZE; 
  private final static long TAU = 5000;

  // Contador real de page faults - thread-safe
  private AtomicInteger totalPageFaults = new AtomicInteger(0);

  public VirtualMemorySystem(int VMSIZE){
    this.virtualMem = new VirtualMemory(VMSIZE);
    this.physicalMem = new PhysicalMemory(PMSIZE);
    this.disco = new Disk(VMSIZE, gerarDadosPrograma());
    this.wsclock = new WSClock(TAU);
    this.VMSIZE = VMSIZE;
    this.PMSIZE = VMSIZE/2;
    startTable();
    LogSO.imprimirLog("VirtualMemorySystem inicializado com dados aleatórios");
  }

  private int[] gerarDadosPrograma() {
    int[] dados = new int[VMSIZE];
    java.util.Random r = new java.util.Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < VMSIZE; i++) {
      dados[i] = r.nextInt(100);
      sb.append(dados[i]).append(", ");
    }
    LogSO.imprimirLog("Dados iniciais do programa: " + sb.toString());
    return dados;
  }

  public void startTable(){
    for (int i = 0; i < VMSIZE; i ++){
      this.virtualMem.setPagina(i, new Pagina());
    }
  }

  // SINCRONIZADO: garante que só uma thread executa read() por vez
  @Override
  public synchronized int read(int enderecoVirtual){
    Pagina p = virtualMem.getPagina(enderecoVirtual);
    if (p.getPresente()){
      LogSO.imprimirLog("Endereço: "+enderecoVirtual+" presente --> Frame: "+ p.getNumeroFrame());
      return physicalMem.read(p.getNumeroFrame());
    }
    LogSO.imprimirLog("PAGE FAULT no endereço: " + enderecoVirtual);
    return handlePageFault(enderecoVirtual);
  }

  // SINCRONIZADO: garante que só uma thread executa write() por vez
  @Override
  public synchronized void write(int enderecoVirtual, int valor){
    Pagina p = virtualMem.getPagina(enderecoVirtual);
    if (!p.getPresente()){
      LogSO.imprimirLog("PAGE FAULT no endereço: "+enderecoVirtual+" durante escrita");
      handlePageFault(enderecoVirtual);
      p = virtualMem.getPagina(enderecoVirtual);
    }
    physicalMem.write(p.getNumeroFrame(), valor);
    p.setModificado(true);
    p.setReferenciado(true);
    p.setLastUsed(System.currentTimeMillis());
    LogSO.imprimirLog("Escrita no endereço: "+enderecoVirtual+" --> Frame: "+ p.getNumeroFrame()+" Valor: "+ valor);
  }

  // Não precisa de synchronized aqui - só é chamado de dentro de read()/write(),
  // que já estão sincronizados. Métodos privados chamados só por métodos
  // synchronized da mesma instância já estão protegidos.
  private int handlePageFault(int enderecoVirtual){
    totalPageFaults.incrementAndGet(); // CONTADOR REAL - incrementa aqui, no lugar certo

    int valor = disco.loadPage(enderecoVirtual);
    LogSO.imprimirLog("Página "+enderecoVirtual+" carregada do disco");

    if (physicalMem.hasEmptyFrame()){
      int frame = physicalMem.proximoFrameLivre();
      LogSO.imprimirLog("Frame livre encontrado --> Frame: "+frame);
      ocuparFrame(frame, enderecoVirtual, valor);
    } else {
      LogSO.imprimirLog("Memória cheia --> acionando WSClock");
      int frameVitima = wsclock.selecionarVitima(
        virtualMem, physicalMem, System.currentTimeMillis());

      int paginaVitima = physicalMem.getPaginaOcupante(frameVitima);
      Pagina vitima = virtualMem.getPagina(paginaVitima);

      if(vitima.getModificado()){
        disco.savePage(paginaVitima, physicalMem.read(frameVitima));
        LogSO.imprimirLog("Página: "+paginaVitima+" estava modificada -- > salva no disco");
      }else{
        LogSO.imprimirLog("Página: "+paginaVitima+" não modificada → descartada");
      }

      vitima.setPresente(false);
      physicalMem.liberar(frameVitima);
      LogSO.imprimirLog("Página: "+paginaVitima+" Removida do frame: "+frameVitima);

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
    p.setReferenciado(true);
    LogSO.imprimirLog("Página: "+paginaVirtual+" Ocupando frame: "+frame);
  }

  // Novo getter: pega o total real de page faults, de forma thread-safe
  public int getTotalPageFaults(){
    return totalPageFaults.get();
  }
}