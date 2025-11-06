package com.leandro.clinica.service;

import com.leandro.clinica.model.Turno;
import com.leandro.clinica.repository.ITurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class NotificacionScheduler {

    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private NotificacionService notificacionService;

    /**
     * Tarea programada que se ejecuta todos los dias a las 9:00 AM
     * Envia notificaciones de recordatorio a todos los pacientes
     * que tienen turnos para el dia siguiente
     */
    @Scheduled(cron = "0 0 9 * * *")  // Ejecutar a las 9:00 AM todos los dias
    public void enviarNotificacionesDiarias() {
        System.out.println("\n========================================");
        System.out.println("🔔 INICIANDO ENVIO DE NOTIFICACIONES");
        System.out.println("📅 Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("========================================\n");
        
        // Calcular el rango de manana (todo el dia)
        LocalDateTime manana = LocalDateTime.now().plusDays(1);
        LocalDateTime inicioManana = manana.toLocalDate().atStartOfDay();
        LocalDateTime finManana = manana.toLocalDate().atTime(23, 59, 59);
        
        System.out.println("🔍 Buscando turnos para: " + inicioManana.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Buscar turnos de manana que esten ocupados (no cancelados)
        List<Turno> turnosManana = turnoRepository
            .findTurnosByFechaHoraBetweenAndOcupadoTrue(inicioManana, finManana);
        
        System.out.println("📋 Turnos encontrados: " + turnosManana.size());
        
        if (turnosManana.isEmpty()) {
            System.out.println("ℹ️  No hay turnos programados para manana");
        } else {
            int exitosos = 0;
            int fallidos = 0;
            
            // Enviar notificacion a cada turno
            for (Turno turno : turnosManana) {
                try {
                    System.out.println("\n📧 Enviando notificacion:");
                    System.out.println("   Paciente: " + turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido());
                    System.out.println("   Email: " + turno.getPaciente().getEmail());
                    System.out.println("   Hora turno: " + turno.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm")));
                    
                    notificacionService.enviarEmailRecordatorio(turno);
                    exitosos++;
                    
                } catch (Exception e) {
                    System.err.println("❌ Error al procesar turno #" + turno.getId());
                    fallidos++;
                }
            }
            
            System.out.println("\n========================================");
            System.out.println("✅ Notificaciones exitosas: " + exitosos);
            if (fallidos > 0) {
                System.out.println("❌ Notificaciones fallidas: " + fallidos);
            }
            System.out.println("========================================\n");
        }
    }

    /**
     * Metodo de prueba que se ejecuta cada 5 minutos
     * COMENTAR o ELIMINAR en produccion
     * Util para testing sin esperar hasta las 9 AM
     */
    // @Scheduled(fixedRate = 300000)  // Cada 5 minutos (300,000 ms)
    // public void testNotificacionesCada5Minutos() {
    //     System.out.println("\n🧪 [TEST] Ejecutando prueba de notificaciones cada 5 minutos...");
    //     enviarNotificacionesDiarias();
    // }

    /**
     * Metodo de prueba que se ejecuta cada 30 segundos
     * SOLO PARA TESTING - DESCOMENTAR TEMPORALMENTE PARA PROBAR
     */
    // @Scheduled(fixedRate = 30000)  // Cada 30 segundos
    // public void testNotificacionesRapido() {
    //     System.out.println("\n🧪 [TEST RAPIDO] Prueba cada 30 segundos...");
    //     
    //     // Buscar cualquier turno futuro para probar
    //     LocalDateTime ahora = LocalDateTime.now();
    //     LocalDateTime futuro = ahora.plusDays(30);
    //     List<Turno> turnosPrueba = turnoRepository
    //         .findTurnosByFechaHoraBetweenAndOcupadoTrue(ahora, futuro);
    //     
    //     if (!turnosPrueba.isEmpty()) {
    //         System.out.println("📧 Enviando email de prueba...");
    //         notificacionService.enviarEmailRecordatorio(turnosPrueba.get(0));
    //     } else {
    //         System.out.println("⚠️  No hay turnos para probar");
    //     }
    // }
}

