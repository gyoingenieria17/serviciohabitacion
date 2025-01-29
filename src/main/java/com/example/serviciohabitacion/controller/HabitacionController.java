package com.example.serviciohabitacion.controller;

import com.example.serviciohabitacion.entity.Habitacion;
import com.example.serviciohabitacion.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    @PostMapping
    public ResponseEntity<Habitacion> insertarHabitacion(@RequestBody Habitacion habitacion) {
        return ResponseEntity.ok(habitacionService.insertarHabitacion(habitacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habitacion> actualizarHabitacion(@PathVariable Integer id, @RequestBody Habitacion datosActualizados) {
        return ResponseEntity.ok(habitacionService.actualizarHabitacion(id, datosActualizados));
    }

    @GetMapping("/{idHotel}")
    public ResponseEntity<List<Habitacion>> obtenerHabitacionesPorHotel(@PathVariable Integer idHotel) {
        return ResponseEntity.ok(habitacionService.obtenerHabitacionesPorHotel(idHotel));
    }
}
