package com.so.backend.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.backend.models.Processo;
import com.so.backend.models.Relatorio;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/processo")
public class ProcessoController {
    static FilaController filaController = new FilaController();
    static Relatorio relatorio = new Relatorio();

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
        if (EscalonadoresController.isBlock_thread()){
            processo.setEstado("Novo");
            processo.setTempo_chegada(EscalonadoresController.getRelogio() + processo.getTempo_chegada());
        }   
        filaController.fila.addProcesso(processo);
        return ResponseEntity.ok(processo);
    }

    @PostMapping("/kill")
    public ResponseEntity<Processo> killProcesso(@RequestBody Processo processo) {
        processo.setEstado("Finalizado");
        filaController.fila.getListaDeProcessos().set(processo.getPid(), processo);
        return ResponseEntity.ok(processo);
    }

    @PostMapping("/criarRandom")
    public ResponseEntity<List<Processo>> criarProcessoRandom() {
        for (int i = 0; i < 5; i++) {
            int pid = filaController.fila.getListaDeProcessos().size() + 1;
            int tempo_execucao = new Random().nextInt(10) + 1;
            int tempo_chegada = new Random().nextInt(10);
            int prioridade = new Random().nextInt(5);
            String estado = "Pronto";
            if (EscalonadoresController.isBlock_thread()){
                estado = "Novo";
                tempo_chegada = EscalonadoresController.getRelogio() + tempo_chegada;
            }
            String nome = "P" + pid;
            int tempo_espera = 0;
            
            int tempo_restante = tempo_execucao;
            Processo processo = new Processo(pid, estado, nome, tempo_espera, tempo_execucao, tempo_chegada, prioridade,
                    tempo_restante, 0, 0, 0, 0, 0);
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

    @GetMapping("/relatorio")
    public ResponseEntity<String> relatorio() {
        JSONObject jsonResponse = new JSONObject();
        Relatorio relatorio = EscalonadoresController.getRelatorio();

        jsonResponse.put("tempo_medio_espera", relatorio.getTempo_medio_espera());
        jsonResponse.put("tempo_medio_execucao", relatorio.getTempo_medio_execucao());
        jsonResponse.put("trocas_contexto", relatorio.getTrocas_contexto());
        jsonResponse.put("tempo_total_execucao", relatorio.getTempo_total_execucao());
        jsonResponse.put("tempo_total_espera", relatorio.getTempo_total_espera());
        jsonResponse.put("tempo_total_resposta", relatorio.getTempo_total_resposta());
        jsonResponse.put("eficiencia", relatorio.getEficiencia());
        jsonResponse.put("quant_processos", relatorio.getQuant_processos());
        jsonResponse.put("timestamp_final", relatorio.getTimestamp_final());
        jsonResponse.put("timestamp_inicial", relatorio.getTimestamp_inicial());

        return ResponseEntity.ok(jsonResponse.toString());
    }

}
