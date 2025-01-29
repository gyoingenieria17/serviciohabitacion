package com.example.serviciohabitacion.listener;

import com.example.serviciohabitacion.entity.Habitacion;
import com.example.serviciohabitacion.service.HabitacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final HabitacionService habitacionService;
    private final ObjectMapper objectMapper;

    public RabbitMQListener(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = "habitacionesQueue")
    public void handleHabitacionMessage(String message) {
        try {
            Habitacion habitacion = objectMapper.readValue(message, Habitacion.class);
            if (habitacion.getIdHabitacion() == null) {
                habitacionService.insertarHabitacion(habitacion);
            } else {
                habitacionService.actualizarHabitacion(habitacion.getIdHabitacion(), habitacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
