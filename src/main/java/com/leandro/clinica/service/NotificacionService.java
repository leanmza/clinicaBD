package com.leandro.clinica.service;

import com.leandro.clinica.model.Turno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class NotificacionService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia un email de recordatorio al paciente 24 horas antes de su turno
     * @param turno El turno para el cual se enviara el recordatorio
     */
    public void enviarEmailRecordatorio(Turno turno) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("clinica.turnos.2025@gmail.com");
            mensaje.setTo(turno.getPaciente().getEmail());
            mensaje.setSubject("Recordatorio de Turno - Clinica Odontologica");
            
            String textoMensaje = String.format(
                "Hola %s %s,\n\n" +
                "Le recordamos que tiene un turno programado:\n\n" +
                "📅 Fecha: %s\n" +
                "🕐 Hora: %s\n" +
                "👨‍⚕️ Doctor: Dr. %s %s\n" +
                "📋 Especialidad: %s\n\n" +
                "Por favor, llegue 10 minutos antes de su turno.\n" +
                "Si necesita cancelar o reprogramar, contactenos con anticipacion.\n\n" +
                "Saludos cordiales,\n" +
                "Clinica Odontologica\n" +
                "Tel: (261) 123-4567\n" +
                "Email: clinica.turnos.2025@gmail.com",
                turno.getPaciente().getNombre(),
                turno.getPaciente().getApellido(),
                turno.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                turno.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                turno.getDoctor().getNombre(),
                turno.getDoctor().getApellido(),
                turno.getDoctor().getEspecialidad().getNombre()
            );
            
            mensaje.setText(textoMensaje);
            mailSender.send(mensaje);
            
            System.out.println("✅ Email enviado exitosamente a: " + turno.getPaciente().getEmail());
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email a " + turno.getPaciente().getEmail() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envia un email de confirmacion cuando se crea un nuevo turno
     * @param turno El turno recien creado
     */
    public void enviarEmailConfirmacion(Turno turno) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("clinica.turnos.2025@gmail.com");
            mensaje.setTo(turno.getPaciente().getEmail());
            mensaje.setSubject("Confirmacion de Turno - Clinica Odontologica");
            
            String textoMensaje = String.format(
                "Hola %s %s,\n\n" +
                "Su turno ha sido confirmado exitosamente:\n\n" +
                "📅 Fecha: %s\n" +
                "🕐 Hora: %s\n" +
                "👨‍⚕️ Doctor: Dr. %s %s\n" +
                "📋 Especialidad: %s\n" +
                "🆔 Numero de turno: #%d\n\n" +
                "Recibira un recordatorio 24 horas antes de su cita.\n\n" +
                "Saludos cordiales,\n" +
                "Clinica Odontologica",
                turno.getPaciente().getNombre(),
                turno.getPaciente().getApellido(),
                turno.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                turno.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                turno.getDoctor().getNombre(),
                turno.getDoctor().getApellido(),
                turno.getDoctor().getEspecialidad().getNombre(),
                turno.getId()
            );
            
            mensaje.setText(textoMensaje);
            mailSender.send(mensaje);
            
            System.out.println("✅ Email de confirmacion enviado a: " + turno.getPaciente().getEmail());
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email de confirmacion: " + e.getMessage());
        }
    }
}

