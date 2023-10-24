package com.so.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WebSocketService {
    private static SimpMessagingTemplate template;

    @Autowired
    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public static void broker(Object message) {
        try {
            // Serializar o objeto Processo em JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String processoJSON = objectMapper.writeValueAsString(message);

            // Enviar a mensagem JSON para o tópico
            template.convertAndSend("/topic/processo", processoJSON);
        } catch (

        JsonProcessingException e) {
            e.printStackTrace();
            // Tratar o erro apropriadamente
        }
    }

}