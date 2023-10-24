package com.so.backend.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/executar")
public class ExecutarController {
    EscalonadoresController escalonadoresController = new EscalonadoresController();

    @PostMapping("/run")
    public ResponseEntity<String> run(@RequestBody String run) {
        JSONObject jsonResponse = new JSONObject();
        run = run.toUpperCase();
        escalonadoresController.setAlgoritmo(run);
        if (!EscalonadoresController.isBlock_thread()) {
            Thread escalonadorThread = new Thread(escalonadoresController);
            escalonadorThread.start();
            jsonResponse.put("message", "Algoritmo " + run + " iniciado");
        } else {
            jsonResponse.put("message", "Já existe uma thread em execução");
        }
        
        return ResponseEntity.ok(jsonResponse.toString());
    }
    @GetMapping("/stop")
    public ResponseEntity<String> stop() {
        return ResponseEntity.ok("STOP");
    }
}
