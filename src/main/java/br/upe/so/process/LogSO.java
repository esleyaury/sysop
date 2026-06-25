package br.upe.so.process;

import java.io.FileWriter;
import java.io.PrintWriter;

public class LogSO{

    private static PrintWriter arquivo;

    static{
        try{
            arquivo = new PrintWriter(new FileWriter("log.txt", false));
        } catch (Exception e){
            System.err.println("Erro ao criar arquivo do log: "+ e.getMessage());
        }
    }

    public static void imprimirLog(String mensagem){
        String log = "[SO] " + mensagem;
        System.out.println(log);
        if (arquivo != null){
            arquivo.println(log);
            arquivo.flush();
        }
    }
    public static void fechar(){
        if (arquivo != null){
            arquivo.close();
        }
    }
}