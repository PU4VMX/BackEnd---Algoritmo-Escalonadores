package com.so.backend.controller;

import com.so.backend.models.Processo;

public class EscalonadoresController implements Runnable {
    private String algoritmo;
    private int contador = 0;
    private static int quantum = 2;

    private static int quant_threads = 0;

    private FilaController filaController = ProcessoController.filaController;

    public static int getQuant_threads() {
        return quant_threads;
    }

    public void run() {
        //thread para somar +1 no contador a cada 1 segundo
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    contador++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        
        if (quant_threads == 0) {
            quant_threads++;
            try {
                System.out.println("Iniciando thread");
                switch (algoritmo) {
                    case "PRIO":
                        PRIO();
                        break;
                    case "SRTF":
                        SRTF();
                        break;
                    case "SJF":
                        SJF();
                        break;
                    case "RR":
                        RoundRobin();
                        break;
                    default:
                        System.out.println("Algoritmo não encontrado");
                        break;
                }
                System.out.println("Fim da thread");
                WebSocketService.broker("Finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
            quant_threads--;
        } else {
            System.out.println("Já existe uma thread em execução");
        }
    }


    public static void setQuantum(int arg_quantum) {
        quantum = arg_quantum;
    }

    private void PRIO() throws Exception {
        contador = 0;
        filaController.fila.getListaDeProcessos().sort((p1, p2) -> p1.getPrioridade() - p2.getPrioridade());
        do {
            Processo processo = new Processo();
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    processo.setEstado("Executando");
                    filaController.fila.putProcesso(processo);
                    while (processo.getTempo_restante() > 0) {
                        
                        if (processo.getTempo_restante() > 0) {
                            processo.setTempo_restante(processo.getTempo_restante() - 1);
                            if (processo.getTempo_restante() == 0) {
                                processo.setEstado("Finalizado");
                            }
                        }

                        filaController.fila.putProcesso(processo);
                    }
                    processo.setEstado("Finalizado");
                    filaController.fila.putProcesso(processo);
                }
            }
        } while (!filaController.isAllFinalizado());
    }

    private void SRTF() throws Exception {
        contador = 0;
        do {
            Processo processo = new Processo();
            processo = filaController.fila.getListaDeProcessos().get(0);
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    processo.setEstado("Executando");
                    filaController.fila.putProcesso(processo);
                    while (processo.getTempo_restante() > 0) {
                        
                        if (processo.getTempo_restante() > 0) {
                            processo.setTempo_restante(processo.getTempo_restante() - 1);
                            if (processo.getTempo_restante() == 0) {
                                processo.setEstado("Finalizado");
                            }
                        }
                        filaController.fila.putProcesso(processo);
                    }
                    processo.setEstado("Finalizado");
                    filaController.fila.putProcesso(processo);
                }
                processo = filaController.fila.getListaDeProcessos().get(i);
            }
        } while (!filaController.isAllFinalizado());
    }

    private void SJF() throws Exception {
        contador = 0;
        // ordena a lista de processos por tempo de execução
        filaController.fila.getListaDeProcessos().sort((p1, p2) -> p1.getTempo_execucao() - p2.getTempo_execucao());
        do {
            Processo processo = new Processo();
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    processo.setEstado("Executando");
                    filaController.fila.putProcesso(processo);
                    while (processo.getTempo_restante() > 0) {
                        
                        if (processo.getTempo_restante() > 0) {
                            processo.setTempo_restante(processo.getTempo_restante() - 1);
                        }
                        filaController.fila.putProcesso(processo);
                    }

                    processo.setEstado("Finalizado");
                    filaController.fila.putProcesso(processo);
                }
            }
        } while (!filaController.isAllFinalizado());
    }

    public void RoundRobin() throws Exception {
        contador = 0;
        do {
            Processo processo = new Processo();
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    processo.setEstado("Executando");
                    filaController.fila.putProcesso(processo);
                    
                    if (processo.getTempo_restante() < quantum) {
                        for (int j = 0; j < processo.getTempo_restante(); j++) {
                            
                            processo.setTempo_restante(processo.getTempo_restante() - 1);
                            if (processo.getTempo_restante() == 0) {
                                processo.setEstado("Finalizado");
                            }
                            filaController.fila.putProcesso(processo);
                        }
                        processo.setEstado("Finalizado");
                        filaController.fila.putProcesso(processo);
                    } else {
                        for (int k = 0; k < quantum; k++) {
                            
                            processo.setTempo_restante(processo.getTempo_restante() - 1);
                            filaController.fila.putProcesso(processo);
                        }
                        if (processo.getTempo_restante() > 0) {
                            processo.setEstado("Em espera");
                        } else {
                            processo.setEstado("Finalizado");
                        }
                        filaController.fila.putProcesso(processo);
                    }
                    processo.setTempo_espera(contador);
                    filaController.fila.putProcesso(processo);
                }
            }
        } while (!filaController.isAllFinalizado());
        //calcular o tempo médio de espera
        int tempo_medio = 0;
        for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
            tempo_medio += filaController.fila.getListaDeProcessos().get(i).getTempo_espera();
        }
        tempo_medio = tempo_medio / filaController.fila.getListaDeProcessos().size();
        System.out.println("Tempo médio de espera: " + tempo_medio);
        
    }

    public void setAlgoritmo(String run) {
        algoritmo = run;
    }

}
