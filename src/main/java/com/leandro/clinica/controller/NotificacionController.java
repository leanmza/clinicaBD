package com.leandro.clinica.controller;

import com.leandro.clinica.model.Turno;
import com.leandro.clinica.repository.ITurnoRepository;
import com.leandro.clinica.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ITurnoRepository turnoRepository;

    /**
     * Endpoint para obtener estadisticas del sistema de notificaciones
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Calcular turnos de mañana
            LocalDateTime manana = LocalDateTime.now().plusDays(1);
            LocalDateTime inicioManana = manana.toLocalDate().atStartOfDay();
            LocalDateTime finManana = manana.toLocalDate().atTime(23, 59, 59);
            
            List<Turno> turnosManana = turnoRepository
                .findTurnosByFechaHoraBetweenAndOcupadoTrue(inicioManana, finManana);
            
            stats.put("turnosManana", turnosManana.size());
            stats.put("fechaManana", inicioManana.toLocalDate().toString());
            stats.put("estado", "activo");
            stats.put("horarioEnvio", "09:00 AM");
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            stats.put("error", "Error al obtener estadisticas: " + e.getMessage());
            return ResponseEntity.ok(stats);
        }
    }

    /**
     * Endpoint para enviar notificaciones manualmente
     */
    @PostMapping("/enviar-ahora")
    public ResponseEntity<Map<String, Object>> enviarNotificacionesAhora() {
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // Calcular turnos de mañana
            LocalDateTime manana = LocalDateTime.now().plusDays(1);
            LocalDateTime inicioManana = manana.toLocalDate().atStartOfDay();
            LocalDateTime finManana = manana.toLocalDate().atTime(23, 59, 59);
            
            List<Turno> turnosManana = turnoRepository
                .findTurnosByFechaHoraBetweenAndOcupadoTrue(inicioManana, finManana);
            
            if (turnosManana.isEmpty()) {
                resultado.put("mensaje", "No hay turnos programados para mañana");
                resultado.put("exitosos", 0);
                resultado.put("total", 0);
                return ResponseEntity.ok(resultado);
            }
            
            int exitosos = 0;
            int fallidos = 0;
            
            // Enviar notificaciones
            for (Turno turno : turnosManana) {
                try {
                    notificacionService.enviarEmailRecordatorio(turno);
                    exitosos++;
                } catch (Exception e) {
                    fallidos++;
                    System.err.println("Error al enviar a " + turno.getPaciente().getEmail());
                }
            }
            
            resultado.put("mensaje", "Notificaciones enviadas exitosamente");
            resultado.put("exitosos", exitosos);
            resultado.put("fallidos", fallidos);
            resultado.put("total", turnosManana.size());
            resultado.put("fecha", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            resultado.put("mensajeError", "Error al enviar notificaciones: " + e.getMessage());
            return ResponseEntity.ok(resultado);
        }
    }

    /**
     * Endpoint para enviar email de prueba
     */
    @PostMapping("/prueba/{turnoId}")
    public ResponseEntity<Map<String, Object>> enviarEmailPrueba(@PathVariable Long turnoId) {
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new Exception("Turno no encontrado"));
            
            notificacionService.enviarEmailRecordatorio(turno);
            
            resultado.put("mensaje", "Email de prueba enviado exitosamente");
            resultado.put("destinatario", turno.getPaciente().getEmail());
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            resultado.put("mensajeError", "Error al enviar email de prueba: " + e.getMessage());
            return ResponseEntity.ok(resultado);
        }
    }
}

