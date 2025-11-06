# 📧 CONFIGURACIÓN DEL SISTEMA DE NOTIFICACIONES

## ✅ Sistema Implementado

El sistema de notificaciones automáticas ha sido implementado con éxito. Incluye:

- **Notificaciones por Email** usando Gmail
- **Envío automático diario** a las 9:00 AM
- **Recordatorios 24 horas antes** del turno
- **Emails de confirmación** al crear un turno

---

## 🔧 PASOS PARA CONFIGURAR

### 1. Crear cuenta de Gmail (si no existe)

Crea la cuenta: **clinica.turnos.2025@gmail.com**

O usa cualquier cuenta Gmail que prefieras.

---

### 2. Habilitar Verificación en 2 Pasos

1. Ve a: https://myaccount.google.com/security
2. Busca **"Verificación en 2 pasos"**
3. Actívala (es requisito obligatorio)

---

### 3. Generar Contraseña de Aplicación

1. En la misma página de seguridad, busca **"Contraseñas de aplicaciones"**
2. Selecciona:
   - **Aplicación:** Correo
   - **Dispositivo:** Otro (personalizado)
   - **Nombre:** Sistema Clinica 2025
3. Haz clic en **"Generar"**
4. **Copia la contraseña generada** (16 caracteres, ejemplo: `abcd efgh ijkl mnop`)

---

### 4. Configurar en application.properties

Abre el archivo: `src/main/resources/application.properties`

Busca esta sección:

```properties
# Configuracion de Email para notificaciones
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=clinica.turnos.2025@gmail.com
spring.mail.password=TU_CONTRASENA_DE_APLICACION_AQUI  ← CAMBIAR AQUÍ
```

**Reemplaza** `TU_CONTRASENA_DE_APLICACION_AQUI` con la contraseña de 16 caracteres que generaste.

**Ejemplo:**
```properties
spring.mail.password=abcd efgh ijkl mnop
```

---

## 🧪 CÓMO PROBAR EL SISTEMA

### Opción 1: Prueba Rápida (Recomendado para testing)

1. Abre: `src/main/java/com/leandro/clinica/service/NotificacionScheduler.java`

2. **Descomenta** este método al final del archivo:

```java
@Scheduled(fixedRate = 30000)  // Cada 30 segundos
public void testNotificacionesRapido() {
    System.out.println("\n🧪 [TEST RAPIDO] Prueba cada 30 segundos...");
    
    // Buscar cualquier turno futuro para probar
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime futuro = ahora.plusDays(30);
    List<Turno> turnosPrueba = turnoRepository
        .findTurnosByFechaHoraBetweenAndOcupadoTrue(ahora, futuro);
    
    if (!turnosPrueba.isEmpty()) {
        System.out.println("📧 Enviando email de prueba...");
        notificacionService.enviarEmailRecordatorio(turnosPrueba.get(0));
    } else {
        System.out.println("⚠️  No hay turnos para probar");
    }
}
```

3. **Asegúrate de tener turnos en la base de datos**

4. **Ejecuta la aplicación**

5. **Verás en la consola** cada 30 segundos el intento de envío

6. **Revisa el email** del paciente para ver la notificación

7. **⚠️ IMPORTANTE:** Vuelve a **comentar** este método después de probar

---

### Opción 2: Prueba Cada 5 Minutos

Descomenta el método:

```java
@Scheduled(fixedRate = 300000)  // Cada 5 minutos
public void testNotificacionesCada5Minutos() {
    System.out.println("\n🧪 [TEST] Ejecutando prueba cada 5 minutos...");
    enviarNotificacionesDiarias();
}
```

---

### Opción 3: Esperar al Horario Real (9:00 AM)

El sistema se ejecutará automáticamente todos los días a las 9:00 AM sin hacer nada adicional.

---

## 📋 FUNCIONALIDADES IMPLEMENTADAS

### 1. NotificacionService.java

**Métodos:**
- `enviarEmailRecordatorio(Turno turno)` - Envía recordatorio 24h antes
- `enviarEmailConfirmacion(Turno turno)` - Confirma turno al crearlo

### 2. NotificacionScheduler.java

**Tarea programada:**
- Se ejecuta a las **9:00 AM todos los días**
- Busca turnos del día siguiente
- Envía emails automáticamente
- Muestra log detallado en consola

### 3. Formato del Email

```
Hola Juan Pérez,

Le recordamos que tiene un turno programado:

📅 Fecha: 07/11/2025
🕐 Hora: 15:30
👨‍⚕️ Doctor: Dr. Carlos Sánchez
📋 Especialidad: Ortodoncia

Por favor, llegue 10 minutos antes de su turno.
Si necesita cancelar o reprogramar, contáctenos con anticipación.

Saludos cordiales,
Clínica Odontológica
Tel: (261) 123-4567
Email: clinica.turnos.2025@gmail.com
```

---

## 🎤 QUÉ DECIR EN LA PRESENTACIÓN

*"Implementé un **sistema de notificaciones automáticas** que:*

✅ *Envía recordatorios por email 24 horas antes del turno*  
✅ *Se ejecuta automáticamente todos los días a las 9:00 AM usando **Spring Scheduler***  
✅ *Incluye toda la información relevante: fecha, hora, doctor y especialidad*  
✅ *También envía email de confirmación al crear un turno*  
✅ *Mejora la experiencia del usuario y reduce el ausentismo*  

*El sistema está configurado con Gmail SMTP y puede escalarse fácilmente para incluir SMS usando Twilio."*

---

## ⚠️ PROBLEMAS COMUNES

### Error: "Authentication failed"
- **Causa:** Contraseña incorrecta o no es una contraseña de aplicación
- **Solución:** Genera una nueva contraseña de aplicación

### Error: "Connection timeout"
- **Causa:** Firewall o antivirus bloqueando puerto 587
- **Solución:** Verifica configuración de firewall

### No llegan los emails
- **Causa:** Pueden estar en spam
- **Solución:** Revisa la carpeta de spam/correo no deseado

### No hay turnos para notificar
- **Causa:** No hay turnos programados para mañana
- **Solución:** Crea turnos con fecha de mañana para probar

---

## 🚀 MEJORAS FUTURAS (Opcional)

- [ ] Agregar notificaciones por SMS usando Twilio
- [ ] Enviar recordatorio también 1 hora antes
- [ ] Notificación al doctor con lista de turnos del día
- [ ] Email con resumen semanal de turnos
- [ ] Confirmación de asistencia desde el email

---

## 📞 SOPORTE

Si tienes problemas con la configuración, revisa:
1. Que la verificación en 2 pasos esté activa
2. Que uses la contraseña de aplicación (no la contraseña normal)
3. Que el formato del email en `application.properties` sea correcto
4. Los logs en la consola para ver errores específicos

---

**¡Sistema listo para usar!** 🎉

