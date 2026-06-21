package br.upe.so.memory;

public class WSClock{
  private int pontRelogio;
  private long tau;

  public WSClock(long tau){
    this.pontRelogio = 0;
    this.tau = tau;
  }

  public nextPont(PhysicalMemory pm) {
    this.pontRelogio = (this.pontRelogio + 1) % pm.getTamanho();
  }

  public int selecionarVitima(VirtualMemory vm,PhysicalMemory pm, long tempoAtual){

    while (true){
      int pvFrame = pm.getPaginaOcupante(pontRelogio);
      Pagina candidata = vm.getPagina(pvFrame);

      if (candidata.getReferenciado()){
        candidata.getReferenciado(false);
        nextPont(pm);
      }

      long idade = tempoAtual - candidata.getLastUsed();

      if(idade > tau){
        int frameEscolhido = pontRelogio;
        nextPont(pm);
        return frameEscolhido;
      }

      nextPont(pm);
    }
  }
}
