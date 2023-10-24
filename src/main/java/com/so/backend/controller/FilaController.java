package com.so.backend.controller;

import com.so.backend.models.Fila;

public class FilaController {
    Fila fila = new Fila();

    public boolean isEmpty() {
        return fila.getListaDeProcessos().isEmpty();
    }

    // verifica se todos os processos estão finalizados
    public boolean isAllFinalizado() {
        for (int i = 0; i < fila.getListaDeProcessos().size(); i++) {
            if (!fila.getListaDeProcessos().get(i).getEstado().equals("Finalizado")) {
                return false;
            }
        }
        return true;
    }

}
