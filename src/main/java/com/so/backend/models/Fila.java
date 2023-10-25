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

    public Processo getNewProcesso() {
        //verificar se há algum processo.estado = "Novo" e retornar o com menor tempo de execução
        Processo processo = null;
        for (Processo p : listaDeProcessos) {
            if (p.getEstado().equals("Novo")) {
                if (processo == null) {
                    processo = p;
                } else if (p.getTempo_execucao() < processo.getTempo_execucao()) {
                    processo = p;
                }
            }
        }
        return processo;
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


    public void setListaDeProcessos(List<Processo> listaDeProcessos) {
        this.listaDeProcessos = listaDeProcessos;
    }

    public void ordenar(String tipo) {
        switch(tipo){
            case "SJF":
                listaDeProcessos.sort((Processo p1, Processo p2) -> {
                    if (p1.getTempo_restante() < p2.getTempo_restante()) {
                        return -1;
                    } else if (p1.getTempo_restante() > p2.getTempo_restante()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                break;
            case "RR":
                listaDeProcessos.sort((Processo p1, Processo p2) -> {
                    if (p1.getTempo_chegada() < p2.getTempo_chegada()) {
                        return -1;
                    } else if (p1.getTempo_chegada() > p2.getTempo_chegada()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                break;
            case "PRIO":
                listaDeProcessos.sort((Processo p1, Processo p2) -> {
                    if (p1.getPrioridade() < p2.getPrioridade()) {
                        return -1;
                    } else if (p1.getPrioridade() > p2.getPrioridade()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                break;
            case "SRTF":
                listaDeProcessos.sort((Processo p1, Processo p2) -> {
                    if (p1.getTempo_restante() < p2.getTempo_restante()) {
                        return -1;
                    } else if (p1.getTempo_restante() > p2.getTempo_restante()) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                break;
            default:
                break;
        }
    }

}
