package br.upe.so.process;

import br.upe.so.kernel.Mmu;

public class ProcessThread extends Thread{
    private int id;
    private Operations[] operacoes;
    private Mmu mmu;
    private int pageFaults = 0;

    public ProcessThread(int id, Operations[] operacoes, Mmu mmu){
        this.id = id;
        this.operacoes = operacoes;
        this.mmu = mmu;
    }

    @Override
    public void run(){
        LogSO.imprimirLog("Thread " + id + " iniciada com " + operacoes.length + " operações");
        for (Operations op : operacoes){
            executeOp(op);
        }
        LogSO.imprimirLog("Thread " + id + " finalizada. Total de Page Faults: " + pageFaults);
    }

    public void executeOp(Operations op){
        try {
            if (op.getTipo().equals("READ")){
                LogSO.imprimirLog("Thread - "+id+" --> READ no endereço "+ op.getEndereco());
                int valor = mmu.ler(op.getEndereco());
                LogSO.imprimirLog("Thread - "+id+" --> valor lido: "+valor);
            }else{
                LogSO.imprimirLog("Thread - "+id+" --> WRITE no endereço: "+op.getEndereco()+" Valor: "+op.getValor());
                mmu.escrever(op.getEndereco(), op.getValor());
            }
        } catch (Exception e) {
            pageFaults++;
            LogSO.imprimirLog("Thread - "+id+" --> EXCEÇÃO: " + e.getMessage());
        }
    }

    public int getPageFaults(){
        return pageFaults;
    }

}