package br.upe.so.memory;

import br.upe.so.process.LogSO;

public class WSClock{

  private int pontRelogio; // Ponteiro de marcar horas do relogio
  private long tau; // idade aceitavel de uma pagina

  public WSClock(long tau){
    this.pontRelogio = 0;
    this.tau = tau;
  }

  // Muda o ponteiro do relogio para a "Proxima Hora"
  public void proximaCandidato(PhysicalMemory pm) {
    this.pontRelogio = (this.pontRelogio + 1) % pm.getTamanhoFisico();
  }

  public int selecionarVitima(VirtualMemory vm, PhysicalMemory pm, long tempoAtual){
    int tamanho = pm.getTamanhoFisico();
    int primeiraCandidataEmergencia = -1;

    LogSO.imprimirLog("WSClock: iniciando busca por vítima a partir do frame " + pontRelogio);

    for (int passo = 0; passo < 2 * tamanho; passo++){

      int pvFrame = pm.getPaginaOcupante(pontRelogio);
      Pagina candidata = vm.getPagina(pvFrame);

      if (candidata.getReferenciado()){
        LogSO.imprimirLog("WSClock: Frame " + pontRelogio + " (página " + pvFrame + ") estava referenciada --> segunda chance, zerando bit");
        candidata.setReferenciado(false);
        candidata.setLastUsed(tempoAtual);
      } else {
        long idade = tempoAtual - candidata.getLastUsed();

        LogSO.imprimirLog("WSClock: Frame " + pontRelogio + " (página " + pvFrame + ") não referenciada, idade = " + idade + "ms (tau = " + tau + "ms)");

        if (idade > tau){
          LogSO.imprimirLog("WSClock: Frame " + pontRelogio + " (página " + pvFrame + ") está FORA do working set --> VÍTIMA escolhida");
          int frameEscolhido = pontRelogio;
          proximaCandidato(pm);
          return frameEscolhido;
        }

        if (primeiraCandidataEmergencia == -1){
          primeiraCandidataEmergencia = pontRelogio;
          LogSO.imprimirLog("WSClock: Frame " + pontRelogio + " guardada como candidata de emergência (dentro do WS, mas sem referência)");
        }
      }

      proximaCandidato(pm);
    }

    if (primeiraCandidataEmergencia != -1){
      LogSO.imprimirLog("WSClock: nenhuma página fora do working set --> usando candidata de emergência (frame " + primeiraCandidataEmergencia + ")");
      return primeiraCandidataEmergencia;
    }

    LogSO.imprimirLog("WSClock: pior caso, todas as páginas estavam referenciadas --> forçando vítima no frame " + pontRelogio);
    int frameEscolhido = pontRelogio;
    proximaCandidato(pm);
    return frameEscolhido;
  }
}