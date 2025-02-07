package com.example.serviciohabitacion.service;

import com.example.serviciohabitacion.entity.Habitacion;
import com.example.serviciohabitacion.entity.Hotel;
import com.example.serviciohabitacion.entity.TipoHabitacion;
import com.example.serviciohabitacion.repository.HabitacionRepository;
import com.example.serviciohabitacion.repository.HotelRepository;
import com.example.serviciohabitacion.repository.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private static final String ERROR_TIPO_HABITACION_NO_EXISTE = "El tipo de habitación con ID %d no existe.";
    private static final String ERROR_HABITACION_NO_EXISTE = "La habitación con ID %d no existe.";
    private static final String ERROR_HOTEL_NO_EXISTE = "El hotel con ID %d no existe.";

    public Habitacion insertarHabitacion(Habitacion habitacion) {
        // Validar que el hotel exista
        Hotel hotel = hotelRepository.findById(habitacion.getHotel().getIdHotel())
                .orElseThrow(() -> new IllegalArgumentException(String.format(ERROR_HOTEL_NO_EXISTE, habitacion.getHotel().getIdHotel())));
        habitacion.setHotel(hotel);

        // Validar que el tipo de habitación exista
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findById(habitacion.getTipoHabitacion().getIdTipoHabitacion())
                .orElseThrow(() -> new IllegalArgumentException(String.format(ERROR_TIPO_HABITACION_NO_EXISTE, habitacion.getTipoHabitacion().getIdTipoHabitacion())));
        habitacion.setTipoHabitacion(tipoHabitacion);

        // Guardar la habitación
        return habitacionRepository.save(habitacion);
    }

    public Habitacion actualizarHabitacion(Integer idHabitacion, Habitacion habitacionActualizada) {
        // Buscar la habitación existente
        Habitacion habitacionExistente = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ERROR_HABITACION_NO_EXISTE, idHabitacion)));

        // Validar y actualizar hotel si se proporciona
        if (habitacionActualizada.getHotel() != null && habitacionActualizada.getHotel().getIdHotel() != null) {
            Hotel hotel = hotelRepository.findById(habitacionActualizada.getHotel().getIdHotel())
                    .orElseThrow(() -> new IllegalArgumentException(String.format(ERROR_HOTEL_NO_EXISTE, habitacionActualizada.getHotel().getIdHotel())));
            habitacionExistente.setHotel(hotel);
        }

        // Validar y actualizar tipo de habitación si se proporciona
        if (habitacionActualizada.getTipoHabitacion() != null && habitacionActualizada.getTipoHabitacion().getIdTipoHabitacion() != null) {
            TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findById(habitacionActualizada.getTipoHabitacion().getIdTipoHabitacion())
                    .orElseThrow(() -> new IllegalArgumentException(String.format(ERROR_TIPO_HABITACION_NO_EXISTE, habitacionActualizada.getTipoHabitacion().getIdTipoHabitacion())));
            habitacionExistente.setTipoHabitacion(tipoHabitacion);
        }

        // Actualizar otros campos
        if (habitacionActualizada.getNumeroHabitacion() != null) {
            habitacionExistente.setNumeroHabitacion(habitacionActualizada.getNumeroHabitacion());
        }
        if (habitacionActualizada.getDescripcion() != null) {
            habitacionExistente.setDescripcion(habitacionActualizada.getDescripcion());
        }
        if (habitacionActualizada.getCapacidad() != null) {
            habitacionExistente.setCapacidad(habitacionActualizada.getCapacidad());
        }
        if (habitacionActualizada.getPrecioBase() != null) {
            habitacionExistente.setPrecioBase(habitacionActualizada.getPrecioBase());
        }
        if (habitacionActualizada.getEstado() != null) {
            habitacionExistente.setEstado(habitacionActualizada.getEstado());
        }

        // Actualizar siempre la fecha de actualización
        habitacionExistente.setFechaActualizacion(LocalDateTime.now());

        // Guardar los cambios
        return habitacionRepository.save(habitacionExistente);
    }

    public List<Habitacion> obtenerHabitacionesPorHotel(Integer idHotel) {
        Hotel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ERROR_HOTEL_NO_EXISTE, idHotel)));
        return habitacionRepository.findByHotel(hotel);
    }

    public Map<String, Map<String, Long>> obtenerConteoHabitacionesPorTipoYEstado() {
        List<Hotel> hoteles = hotelRepository.findAll();
        Map<String, Map<String, Long>> resultado = new HashMap<>();

        for (Hotel hotel : hoteles) {
            Map<String, Long> conteoPorTipo = new HashMap<>();
            List<TipoHabitacion> tiposHabitacion = tipoHabitacionRepository.findAll();

            for (TipoHabitacion tipoHabitacion : tiposHabitacion) {
                Long totalHabitaciones = habitacionRepository.countByHotelAndTipoHabitacion(hotel, tipoHabitacion);
                Long habitacionesDisponibles = habitacionRepository.countByHotelAndTipoHabitacionAndEstado(hotel, tipoHabitacion, true);
                Long habitacionesNoDisponibles = habitacionRepository.countByHotelAndTipoHabitacionAndEstado(hotel, tipoHabitacion, false);

                conteoPorTipo.put(tipoHabitacion.getNombre() + "_total", totalHabitaciones);
                conteoPorTipo.put(tipoHabitacion.getNombre() + "_disponibles", habitacionesDisponibles);
                conteoPorTipo.put(tipoHabitacion.getNombre() + "_no_disponibles", habitacionesNoDisponibles);
            }

            resultado.put(hotel.getNombre(), conteoPorTipo);
        }

        return resultado;
    }
}
