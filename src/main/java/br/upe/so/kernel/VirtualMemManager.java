package br.upe.so.kernel;

public interface VirtualMemoryManager {
  int read(int enderecoVirtual);
  void write(int enderecoVirtual, int valor);
}
