package com.example.serviciohabitacion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhabitacion")
    private Integer idHabitacion;

    @Column(name = "numero_habitacion", nullable = false)
    private String numeroHabitacion;

    private String descripcion;

    private Integer capacidad;

    @Column(name = "precio_base", nullable = false)
    private Double precioBase;

    private Boolean estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.EAGER) // Relación con TipoHabitacion
    @JoinColumn(name = "idtipo_habitacion", nullable = false)
    private TipoHabitacion tipoHabitacion;

    @ManyToOne(fetch = FetchType.EAGER) // Relación con Hotel
    @JoinColumn(name = "idhotel", nullable = false) // FK que apunta al hotel asociado
    private Hotel hotel;

    // Inicializar fechas antes de insertar
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Actualizar la fecha de actualización antes de actualizar
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
