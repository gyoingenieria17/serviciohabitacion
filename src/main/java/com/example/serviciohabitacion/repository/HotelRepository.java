package com.example.serviciohabitacion.repository;

import com.example.serviciohabitacion.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}
