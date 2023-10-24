package com.so.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.backend.models.Processo;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/processo")
public class ProcessoController {
    static FilaController filaController = new FilaController();

    public FilaController getFila() {
        return filaController;
    }

    public void setFila(FilaController filaController) {
        ProcessoController.filaController = filaController;
    }

    @PostMapping("/quantum")
    public ResponseEntity<Integer> setQuantum(@RequestBody int quantum) {
        EscalonadoresController.setQuantum(quantum);
        return ResponseEntity.ok(quantum);
    }

    @PostMapping("/criar")
    public ResponseEntity<Processo> criarProcesso(@RequestBody Processo processo) {
        processo.setPid(filaController.fila.getListaDeProcessos().size());
        processo.setNome("P" + processo.getPid());
        if (EscalonadoresController.getQuant_threads() != 0) {
            processo.setEstado("Novo");
        }
        filaController.fila.addProcesso(processo);
        return ResponseEntity.ok(processo);
    }

    @PostMapping("/kill")
    public ResponseEntity<Processo> killProcesso(@RequestBody Processo processo) {
        // colocar processo como finalizado
        processo.setEstado("Finalizado");
        filaController.fila.getListaDeProcessos().set(processo.getPid(), processo);
        return ResponseEntity.ok(processo);
    }

    @PostMapping("/criarRandom")
    public ResponseEntity<List<Processo>> criarProcessoRandom() {
        for (int i = 0; i < 5; i++) {
            int pid = filaController.fila.getListaDeProcessos().size() + 1;
            String estado = "Pronto";
            if (EscalonadoresController.getQuant_threads() != 0) {
                estado = "Novo";
            }
            String nome = "P" + pid;
            int tempo_espera = 0;
            int tempo_execucao = new Random().nextInt(10) + 1;
            int tempo_chegada = new Random().nextInt(10);
            int prioridade = new Random().nextInt(10);
            int tempo_restante = tempo_execucao;
            Processo processo = new Processo(pid, estado, nome, tempo_espera, tempo_execucao, tempo_chegada, prioridade,
                    tempo_restante);
            filaController.fila.addProcesso(processo);
        }
        return ResponseEntity.ok(filaController.fila.getListaDeProcessos());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Processo>> listarProcessos() {
        return ResponseEntity.ok(filaController.fila.getListaDeProcessos());
    }


    @GetMapping("/limpar")
    public ResponseEntity<List<Processo>> limparProcessos() {
        filaController.fila.getListaDeProcessos().clear();
        return ResponseEntity.ok(filaController.fila.getListaDeProcessos());
    }

}
