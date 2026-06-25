package br.upe.so.kernel;

public class Mmu{
  private VirtualMemoryManager memory;

  public Mmu(VirtualMemoryManager memory){
    this.memory = memory;
  }

  public int ler(int enderecoVirtual){
    return memory.read(enderecoVirtual);
  }

  public void escrever(int enderecoVirtual, int valor){
    memory.write(enderecoVirtual, valor);
  }
}
