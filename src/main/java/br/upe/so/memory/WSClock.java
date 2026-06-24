package br.upe.so.memory;

public class WSClock{

  private int pontRelogio; // Ponteiro de marcar horas do relogio
  private long tau; // idade aceitavel de uma pagina

  public WSClock(long tau){
    this.pontRelogio = 0;
    this.tau = tau;
  }

  //Muda o ponteiro do relogio para a "Proxima Hora"
  public void proximaCandidato(PhysicalMemory pm) {

    // Avanca pro proximo valor, de forma circular
    this.pontRelogio = (this.pontRelogio + 1) % pm.getTamanhoFisico();
  }

  public int selecionarVitima(VirtualMemory vm,PhysicalMemory pm, long tempoAtual){

    while (true){
      int pvFrame = pm.getPaginaOcupante(pontRelogio);
      Pagina candidata = vm.getPagina(pvFrame);

      // Se ela foi lida.
      if (candidata.getReferenciado()){
        candidata.setReferenciado(false);
        proximaCandidato(pm);
      }

      long idade = tempoAtual - candidata.getLastUsed();

      if(idade > tau){
        int frameEscolhido = pontRelogio;
        proximaCandidato(pm);
        return frameEscolhido;
      }

      proximaCandidato(pm);
    }
  }
}
