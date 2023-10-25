package com.so.backend.controller;

import com.so.backend.models.Processo;

public class EscalonadoresController implements Runnable {
    private FilaController filaController = ProcessoController.filaController;
    private String algoritmo;
    private static int relogio = 0;
    private static boolean block_thread = false;
    private static int quantum = 2;

    @Override
    public void run() {
        Thread relogioThread = new Thread(() -> {
            System.out.println("Iniciando relogio...");
            relogio = 0;
            do {
                try {
                    Thread.sleep(1000);
                    relogio++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!filaController.isAllFinalizado());
            System.out.println("Relogio finalizado");
        });
        Thread escalonadorThread = new Thread(() -> {
            if (!block_thread) {
                block_thread = true;
                relogioThread.start();
                try {
                    System.out.println("Iniciando algoritmo " + algoritmo + "...");
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
                            RR();
                            break;
                        default:
                            System.out.println("Algoritmo não encontrado");
                            break;
                    }
                    System.out.println("Algoritmo " + algoritmo + " finalizado");
                    relogioThread.interrupt();
                    WebSocketService.broker("Finish");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                block_thread = false;
            } else {
                System.out.println("Já existe uma thread em execução");
            }
        });

        escalonadorThread.start();

        try {
            escalonadorThread.join();
            relogioThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getRelogio() {
        return relogio;
    }

    public static int getQuantum() {
        return quantum;
    }

    public static boolean isBlock_thread() {
        return block_thread;
    }

    public void setAlgoritmo(String run) {
        algoritmo = run;
    }

    public static void setQuantum(int arg_quantum) {
        quantum = arg_quantum;
    }

    public void sleep(int tempo) {
        try {
            Thread.sleep(1000 * tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void RR() {
        relogio = 0;
        do {
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                Processo processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    if (processo.getTempo_chegada() <= relogio) {
                        processo.setEstado("Executando");
                        processo.setInstante_inicial(relogio);
                        processo.setTempo_espera(relogio - processo.getTempo_chegada());
                        filaController.fila.putProcesso(processo);
                        if (processo.getTempo_restante() > getQuantum()) {
                            sleep(getQuantum());
                            processo.setTempo_restante(processo.getTempo_restante() - getQuantum());
                            processo.setTempo_executado(getQuantum());
                            processo.setEstado("Em espera");
                            processo.setInstante_final(relogio);
                            filaController.fila.putProcesso(processo);
                        } else {
                            sleep(processo.getTempo_restante());
                            processo.setTempo_executado(processo.getTempo_restante());
                            processo.setTempo_restante(0);
                            processo.setEstado("Finalizado");
                            processo.setInstante_final(relogio);
                            filaController.fila.putProcesso(processo);
                        }
                    }
                }
            }
        } while (!filaController.isAllFinalizado());
    }

    public void SJF() {
        relogio = 0;
        do {
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                Processo processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    if (processo.getTempo_chegada() <= relogio) {
                        processo.setTempo_espera(relogio);
                        processo.setEstado("Executando");
                        processo.setInstante_inicial(relogio);
                        processo.setTempo_executado(processo.getTempo_restante());
                        filaController.fila.putProcesso(processo);
                        sleep(processo.getTempo_restante());
                        processo.setTempo_restante(0);
                        processo.setEstado("Finalizado");
                        processo.setInstante_final(relogio);
                        filaController.fila.putProcesso(processo);
                    }
                }
                filaController.fila.ordenar("SJF");
            }
        } while (!filaController.isAllFinalizado());
    }

    public void PRIO() {
        relogio = 0;
        do {
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                Processo processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    if (processo.getTempo_chegada() <= relogio) {
                        processo.setTempo_espera(relogio);
                        processo.setEstado("Executando");
                        processo.setInstante_inicial(relogio);
                        processo.setTempo_executado(processo.getTempo_restante());
                        filaController.fila.putProcesso(processo);
                        sleep(processo.getTempo_restante());
                        processo.setTempo_restante(0);
                        processo.setEstado("Finalizado");
                        processo.setInstante_final(relogio);
                        filaController.fila.putProcesso(processo);
                    } else {
                        relogio = processo.getTempo_chegada();
                    }
                }
                filaController.fila.ordenar("PRIO");
            }
        } while (!filaController.isAllFinalizado());
    }

    public void SRTF() {
        relogio = 0;
        do {
            for (int i = 0; i < filaController.fila.getListaDeProcessos().size(); i++) {
                Processo processo = filaController.fila.getListaDeProcessos().get(i);
                if (processo.getEstado().equals("Finalizado")) {
                    continue;
                } else {
                    if (processo.getTempo_chegada() <= relogio) {
                        processo.setTempo_espera(relogio);
                        processo.setEstado("Executando");
                        processo.setInstante_inicial(relogio);
                        processo.setTempo_executado(processo.getTempo_restante());
                        filaController.fila.putProcesso(processo);
                        for (int j = 0; j < filaController.fila.getListaDeProcessos().size(); j++) {
                            Processo processo2 = filaController.fila.getListaDeProcessos().get(j);
                            if (processo2.getTempo_chegada() <= relogio && processo2.getEstado().equals("Novo")) {
                                if (processo2.getTempo_restante() < processo.getTempo_restante()) {
                                    processo.setEstado("Em espera");
                                    processo.setTempo_executado(relogio - processo.getInstante_inicial());
                                    processo.setTempo_restante(processo.getTempo_restante() - processo.getTempo_executado());
                                    sleep(processo2.getTempo_restante());
                                    processo.setInstante_final(relogio);
                                    filaController.fila.putProcesso(processo);
                                    processo = processo2;
                                    processo.setEstado("Executando");
                                    processo.setInstante_inicial(relogio);
                                    processo.setTempo_executado(processo.getTempo_restante());
                                    filaController.fila.putProcesso(processo);
                                }
                            }
                        }
                        sleep(processo.getTempo_restante());
                        processo.setTempo_restante(0);
                        processo.setEstado("Finalizado");
                        processo.setInstante_final(relogio);
                        filaController.fila.putProcesso(processo);
                    }
                }
                filaController.fila.ordenar("SRTF");
            }
        } while (!filaController.isAllFinalizado());
    }
    
    

}
