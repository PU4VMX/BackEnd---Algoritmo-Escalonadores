package com.so.backend.models;

import java.util.ArrayList;
import java.util.List;

import com.so.backend.controller.WebSocketService;

public class Fila {
    private List<Processo> listaDeProcessos = new ArrayList<>();

    public void addProcesso(Processo processo) {
        listaDeProcessos.add(processo);
    }

    public void removeProcesso(Processo processo) {
        listaDeProcessos.remove(processo);
    }

    public List<Processo> getListaDeProcessos() {
        return listaDeProcessos;
    }

    public void putProcesso(Processo processo){
        listaDeProcessos.set(listaDeProcessos.indexOf(processo), processo);
        WebSocketService.broker(processo);
    }

    //metodo em thread que atualiza o tempo de espera de todos os processos da fila
    public void updateTimeAllProcess(int relogio){
        for (int i = 0; i < listaDeProcessos.size(); i++) {
            Processo processo = listaDeProcessos.get(i);
            if (!processo.getEstado().equals("Finalizado")) {
                processo.setTempo_espera(relogio - processo.getTempo_chegada());
                listaDeProcessos.set(i, processo);
            }
        }
    }
    
    public Processo get_p_menor_tempo_rest() {
        Processo processo = new Processo();
        int menorTempo = 999999999;
        for (int i = 0; i < listaDeProcessos.size(); i++) {
            if (listaDeProcessos.get(i).getTempo_restante() < menorTempo) {
                menorTempo = listaDeProcessos.get(i).getTempo_restante();
                processo = listaDeProcessos.get(i);
            }
        }
        return processo;
    }

    public void setListaDeProcessos(List<Processo> listaDeProcessos) {
        this.listaDeProcessos = listaDeProcessos;
    }

}
