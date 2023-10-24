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
        run = run.toUpperCase();
        escalonadoresController.setAlgoritmo(run);
        Thread thread = new Thread(escalonadoresController);
        thread.start();
        if(EscalonadoresController.getQuant_threads() == 0){
            JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("mensagem", "Running");
            return ResponseEntity.ok(jsonResponse.toString());
        }
        else{
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("mensagem", "Já existe uma thread em execução");
            return ResponseEntity.ok(jsonResponse.toString());
        }
    }

    @GetMapping("/stop")
    public ResponseEntity<String> stop() {
        escalonadoresController.stop();
        return ResponseEntity.ok("STOP");
    }
}
