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

    public Processo(int pid, String estado, String nome, int tempo_espera, int tempo_execucao, int tempo_chegada,
            int prioridade, int tempo_restante) {
        this.pid = pid;
        this.estado = estado;
        this.nome = nome;
        this.tempo_espera = tempo_espera;
        this.tempo_execucao = tempo_execucao;
        this.tempo_chegada = tempo_chegada;
        this.prioridade = prioridade;
        this.tempo_restante = tempo_restante;
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
    }
}
