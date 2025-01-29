package com.example.serviciohabitacion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/habitacion/rabbitmq")
public class RabbitMQController {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam(required = false, defaultValue = "habitacionesExchange") String exchange, 
            @RequestParam(required = false, defaultValue = "habitacionesRoutingKey") String routingKey, 
            @RequestBody String message) {

        log.info("Recibiendo solicitud para enviar mensaje a RabbitMQ.");
        log.info("Exchange: {}, RoutingKey: {}, Mensaje: {}", exchange, routingKey, message);

        if (message == null || message.isEmpty()) {
            log.error("El mensaje recibido está vacío.");
            return ResponseEntity.badRequest().body("El mensaje no puede estar vacío.");
        }

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("Mensaje enviado exitosamente a RabbitMQ en el exchange: {} con routingKey: {}", exchange, routingKey);
            return ResponseEntity.ok("Mensaje enviado exitosamente a RabbitMQ en el exchange: " + exchange + " con routingKey: " + routingKey);
        } catch (Exception e) {
            log.error("Error al enviar el mensaje a RabbitMQ: ", e);
            return ResponseEntity.status(500).body("Error al enviar el mensaje a RabbitMQ: " + e.getMessage());
        }
    }
}
