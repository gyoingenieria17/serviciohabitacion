package com.example.serviciohabitacion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "municipio")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmunicipio")
    private Integer idMunicipio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo", nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "iddepartamento", nullable = false)
    @JsonBackReference
    private Departamento departamento;

    @OneToMany(mappedBy = "municipio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Hotel> hoteles;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}

