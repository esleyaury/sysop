package br.upe.so;

import br.upe.so.kernel.Mmu;
import br.upe.so.memory.VirtualMemorySystem;
import br.upe.so.process.Operations;
import br.upe.so.process.ProcessThread;
//import br.upe.so.process.LogSO;

public class Main {
    
    // Constantes
    private static final int VM_SIZE = 20;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("  SIMULADOR DE MEMÓRIA VIRTUAL - WSClock");
        System.out.println("=".repeat(70));
        
        // PASSO 1: Criar a fábrica e gerar as operações
        System.out.println("\n[PASSO 1] Gerando sequências de operações...");
        FabricaDeEntradas fabrica = new FabricaDeEntradas(VM_SIZE);
        String operacoes1String = fabrica.getNewEntrada();
        String operacoes2String = fabrica.getNewEntrada();
        
        System.out.println("\n--- Thread 1 ---");
        System.out.println(operacoes1String);
        System.out.println("\n--- Thread 2 ---");
        System.out.println(operacoes2String);
        
        // PASSO 2: Parsear as strings em arrays de Operations
        System.out.println("\n[PASSO 2] Parseando operações...");
        Operations[] operacoes1 = parseOperacoes(operacoes1String);
        Operations[] operacoes2 = parseOperacoes(operacoes2String);
        
        System.out.println("Thread 1: " + operacoes1.length + " operações");
        System.out.println("Thread 2: " + operacoes2.length + " operações");
        
        // PASSO 3: Criar o sistema de memória virtual
        System.out.println("\n[PASSO 3] Inicializando sistema de memória...");
        VirtualMemorySystem vmSystem = new VirtualMemorySystem();
        Mmu mmu = new Mmu(vmSystem);
        
        // PASSO 4: Criar as threads
        System.out.println("\n[PASSO 4] Criando threads de processo...");
        ProcessThread thread1 = new ProcessThread(1, operacoes1, mmu);
        ProcessThread thread2 = new ProcessThread(2, operacoes2, mmu);
        
        // PASSO 5: Executar as threads
        System.out.println("\n[PASSO 5] Iniciando execução...");
        System.out.println("=".repeat(70));
        
        long tempoInicio = System.currentTimeMillis();
        
        thread1.start();
        thread2.start();
        
        // Aguardar término das threads
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.err.println("Erro ao aguardar threads: " + e.getMessage());
        }
        
        long tempoFinal = System.currentTimeMillis();
        
        // PASSO 6: Exibir resultado
        System.out.println("=".repeat(70));
        System.out.println("\n[PASSO 6] Resultado da execução:\n");
        
        int totalPageFaults = thread1.getPageFaults() + thread2.getPageFaults();
        int totalOperacoes = operacoes1.length + operacoes2.length;
        
        System.out.println("--- Estatísticas da Thread 1 ---");
        System.out.println("  Operações: " + operacoes1.length);
        System.out.println("  Page Faults: " + thread1.getPageFaults());
        if (operacoes1.length > 0) {
            System.out.println("  Taxa de Fault: " + 
                String.format("%.2f%%", (thread1.getPageFaults() * 100.0) / operacoes1.length));
        }
        
        System.out.println("\n--- Estatísticas da Thread 2 ---");
        System.out.println("  Operações: " + operacoes2.length);
        System.out.println("  Page Faults: " + thread2.getPageFaults());
        if (operacoes2.length > 0) {
            System.out.println("  Taxa de Fault: " + 
                String.format("%.2f%%", (thread2.getPageFaults() * 100.0) / operacoes2.length));
        }
        
        System.out.println("\n--- Estatísticas Gerais ---");
        System.out.println("  Total de operações: " + totalOperacoes);
        System.out.println("  Total de page faults: " + totalPageFaults);
        if (totalOperacoes > 0) {
            System.out.println("  Taxa de fault global: " + 
                String.format("%.2f%%", (totalPageFaults * 100.0) / totalOperacoes));
        }
        System.out.println("  Tempo de execução: " + (tempoFinal - tempoInicio) + " ms");
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FIM DA SIMULAÇÃO");
        System.out.println("=".repeat(70));
    }
    
    /**
     * Parseia uma string de operações (formato: "endereco-R,endereco-W-valor,...")
     * e retorna um array de Operations
     */
    private static Operations[] parseOperacoes(String operacoesString) {
        String[] partes = operacoesString.split(",");
        Operations[] operacoes = new Operations[partes.length];
        
        for (int i = 0; i < partes.length; i++) {
            operacoes[i] = parseOperacao(partes[i].trim());
        }
        
        return operacoes;
    }
    
    /**
     * Parseia uma operação individual
     * Formato: "endereco-R" ou "endereco-W-valor"
     */
    private static Operations parseOperacao(String operacaoString) {
        String[] partes = operacaoString.split("-");
        
        int endereco = Integer.parseInt(partes[0]);
        
        if ("R".equals(partes[1])) {
            return new Operations(endereco, "READ");
        } else if ("W".equals(partes[1])) {
            int valor = Integer.parseInt(partes[2]);
            return new Operations(endereco, "WRITE", valor);
        } else {
            throw new IllegalArgumentException("Operação inválida: " + operacaoString);
        }
    }
}