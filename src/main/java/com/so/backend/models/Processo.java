package com.so.backend.models;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.ToString
public class Processo {
    private int pid;
    private String estado;
    private String nome;
    private int tempo_espera;
    private int tempo_execucao;
    private int tempo_chegada;
    private int prioridade;
    private int tempo_restante;
    private int tempo_executado;
    private int instante_inicial;
    private int instante_final;
    private int trocas_contexto;
    private int tempo_medio_espera;

    public Processo(int pid, String estado, String nome, int tempo_espera, int tempo_execucao, int tempo_chegada,
            int prioridade, int tempo_restante, int tempo_executado, int instante_inicial, int instante_final, int trocas_contexto, int tempo_medio_espera) {
        this.pid = pid;
        this.estado = estado;
        this.nome = nome;
        this.tempo_espera = tempo_espera;
        this.tempo_execucao = tempo_execucao;
        this.tempo_chegada = tempo_chegada;
        this.prioridade = prioridade;
        this.tempo_restante = tempo_restante;
        this.tempo_executado = tempo_executado;
        this.instante_inicial = instante_inicial;
        this.instante_final = instante_final;
        this.trocas_contexto = trocas_contexto;
        this.tempo_medio_espera = tempo_medio_espera;
    }

    public Processo(Processo processo) {
        this.pid = processo.getPid();
        this.estado = processo.getEstado();
        this.nome = processo.getNome();
        this.tempo_espera = processo.getTempo_espera();
        this.tempo_execucao = processo.getTempo_execucao();
        this.tempo_chegada = processo.getTempo_chegada();
        this.prioridade = processo.getPrioridade();
        this.tempo_restante = processo.getTempo_restante();
        this.tempo_executado = processo.getTempo_executado();
        this.instante_inicial = processo.getInstante_inicial();
        this.instante_final = processo.getInstante_final();
        this.trocas_contexto = processo.getTrocas_contexto();
        this.tempo_medio_espera = processo.getTempo_medio_espera();
    }
}
