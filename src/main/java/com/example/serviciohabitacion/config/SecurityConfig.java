package com.example.serviciohabitacion.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando la seguridad para la aplicación...");

        http
            // Deshabilitar CSRF para APIs REST, pero considera habilitarlo si manejas formularios en producción.
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // Configuración CORS
            // Configurar las autorizaciones para diferentes endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/habitacion/rabbitmq/send").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/v1/habitacion/rabbitmq/**").permitAll()
                .requestMatchers("/api/v1/habitaciones/*").permitAll()
                // Permitir acceso a todos los métodos GET para los endpoints de hotel
                .requestMatchers(HttpMethod.GET, "/api/v1/hotel/**").permitAll() //.hasRole("ADMIN") // Solo para ADMIN
                // Permitir acceso a todos los métodos POST para los endpoints de hotel
                .requestMatchers(HttpMethod.POST, "/api/v1/hotel/**").permitAll()
                
                // Permitir acceso a todos los métodos PUT para los endpoints de hotel
                .requestMatchers(HttpMethod.PUT, "/api/v1/hotel/**").permitAll()
                
                // Permitir acceso a todos los métodos DELETE para los endpoints de hotel
                .requestMatchers(HttpMethod.DELETE, "/api/v1/hotel/**").permitAll()
                
                // Requerir autenticación para cualquier otro endpoint no configurado explícitamente
                .anyRequest().authenticated()
            )
            
            // Configurar autenticación básica para pruebas rápidas
            .httpBasic(httpBasic -> {})
            
            // Habilitar sesiones para la autenticación (opcional, depende de tu arquitectura)
            .sessionManagement(session -> session.maximumSessions(1));

        logger.info("Seguridad configurada correctamente.");

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorna un codificador BCrypt para las contraseñas
        return new BCryptPasswordEncoder();
    }
}
