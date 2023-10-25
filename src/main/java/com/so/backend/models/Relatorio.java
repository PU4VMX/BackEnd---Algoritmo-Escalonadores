package com.so.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relatorio {
    String timestamp_inicial;
    String timestamp_final;
    int quant_processos;
    int tempo_medio_espera;
    int tempo_medio_execucao;
    int trocas_contexto;
    int tempo_total_execucao;
    int tempo_total_espera;
    int tempo_total_resposta;
    int eficiencia;

    //construtor 
    public Relatorio() {
        this.timestamp_inicial = "";
        this.timestamp_final = "";
        this.quant_processos = 0;
        this.tempo_medio_espera = 0;
        this.tempo_medio_execucao = 0;
        this.trocas_contexto = 0;
        this.tempo_total_execucao = 0;
        this.tempo_total_espera = 0;
        this.tempo_total_resposta = 0;
        this.eficiencia = 0;
    }

    public void setTimestamp_inicial(String string) {
        timestamp_inicial = string;
    }

    public void setTimestamp_final(String string) {
        timestamp_final = string;
    }

    public void setQuant_processos(int i) {
        quant_processos = i;
    }

    @Override
    public String toString() {
        return "{eficiencia:" + eficiencia + ", quant_processos:" + quant_processos + ", tempo_medio_espera:"
                + tempo_medio_espera + ", tempo_medio_execucao:" + tempo_medio_execucao + ", tempo_total_espera:"
                + tempo_total_espera + ", tempo_total_execucao:" + tempo_total_execucao + ", tempo_total_resposta:"
                + tempo_total_resposta + ", timestamp_final:" + timestamp_final + ", timestamp_inicial:"
                + timestamp_inicial + ", trocas_contexto:" + trocas_contexto + "}";
    }
}
