package com.example.serviciohabitacion.repository;

import com.example.serviciohabitacion.entity.Habitacion;
import com.example.serviciohabitacion.entity.Hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {
    List<Habitacion> findByHotel(Hotel hotel);
}
