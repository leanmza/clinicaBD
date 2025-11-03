// URL base de la API
const API_URL = "http://localhost:8080/api";

// Variables globales
let pacientes = [];
let doctores = [];

// Helpers de renderizado simples
function crearFilaPaciente(p) {
  return `
    <tr>
      <td>${p.id ?? ''}</td>
      <td>${p.nombre ?? ''}</td>
      <td>${p.apellido ?? ''}</td>
      <td>${p.email ?? ''}</td>
      <td>${p.celular ?? ''}</td>
      <td>
        <button class="btn-link" onclick="editarPaciente(${p.id})">Editar</button>
        <button class="btn-link danger" onclick="eliminarPaciente(${p.id})">Eliminar</button>
      </td>
    </tr>
  `;
}

function crearFilaDoctor(d) {
  const horario = d.horaInicio && d.horaFin ? `${d.horaInicio} - ${d.horaFin}` : '';
  return `
    <tr>
      <td>${d.id ?? ''}</td>
      <td>${d.nombre ?? ''}</td>
      <td>${d.apellido ?? ''}</td>
      <td>${d.email ?? ''}</td>
      <td>${d.celular ?? ''}</td>
      <td>${d.especialidad ?? ''}</td>
      <td>${horario}</td>
      <td>
        <button class="btn-link" onclick="editarDoctor(${d.id})">Editar</button>
        <button class="btn-link danger" onclick="eliminarDoctor(${d.id})">Eliminar</button>
      </td>
    </tr>
  `;
}

// NAVEGACIÓN
function showSection(sectionId) {
  // Ocultar todas las secciones
  document.querySelectorAll(".section").forEach((section) => {
    section.classList.remove("active");
  });

  // Mostrar la sección seleccionada
  document.getElementById(sectionId).classList.add("active");

  // Actualizar botones de navegación
  document.querySelectorAll(".nav-btn").forEach((btn) => {
    btn.classList.remove("active");
  });
  event.target.classList.add("active");

  // Si es la sección de turnos, cargar pacientes y doctores
  if (sectionId === "turnos") {
    cargarPacientesSelect();
    cargarDoctoresSelect();
  }

  // Si es dashboard, cargar todos los turnos
  if (sectionId === "dashboard") {
    filtrarTurnos("todos");
  }
}

// TABS
function showTab(tabId) {
  document.querySelectorAll(".tab-content").forEach((tab) => {
    tab.classList.remove("active");
  });
  document.getElementById(tabId).classList.add("active");

  document.querySelectorAll(".tab-btn").forEach((btn) => {
    btn.classList.remove("active");
  });
  event.target.classList.add("active");

  // Cargar datos cuando se abre una pestaña de listado
  if (tabId === "pacientes-lista") {
    cargarPacientesTabla();
  }
  if (tabId === "doctores-lista") {
    cargarDoctoresTabla();
  }
}

// ========== PACIENTES ==========
document
  .getElementById("formPaciente")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const paciente = {
      nombre: document.getElementById("pacienteNombre").value,
      apellido: document.getElementById("pacienteApellido").value,
      email: document.getElementById("pacienteEmail").value,
      celular: document.getElementById("pacienteCelular").value,
    };

    try {
      const response = await fetch(`${API_URL}/paciente`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(paciente),
      });

      if (response.ok) {
        mostrarMensaje(
          "mensajePaciente",
          "Paciente registrado exitosamente",
          "exito"
        );
        document.getElementById("formPaciente").reset();
      } else {
        mostrarMensaje(
          "mensajePaciente",
          "Error al registrar paciente",
          "error"
        );
      }
    } catch (error) {
      mostrarMensaje("mensajePaciente", "Error de conexión", "error");
    }
  });

// ========== DOCTORES ==========
document.getElementById("formDoctor").addEventListener("submit", async (e) => {
  e.preventDefault();

  const doctor = {
    nombre: document.getElementById("doctorNombre").value,
    apellido: document.getElementById("doctorApellido").value,
    email: document.getElementById("doctorEmail").value,
    celular: document.getElementById("doctorCelular").value,
    especialidad: {
      nombre: document.getElementById("doctorEspecialidad").value,
    },
    horarios: {
      horaInicio: document.getElementById("horaInicio").value + ":00",
      horaFin: document.getElementById("horaFin").value + ":00",
    },
  };
  try {
    const response = await fetch(`${API_URL}/doctor`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctor),
    });

    if (response.ok) {
      mostrarMensaje(
        "mensajeDoctor",
        "Doctor registrado exitosamente",
        "exito"
      );
      document.getElementById("formDoctor").reset();
    } else {
      mostrarMensaje("mensajeDoctor", "Error al registrar doctor", "error");
    }
  } catch (error) {
    mostrarMensaje("mensajeDoctor", "Error de conexión", "error");
  }
});

// ========== LISTADOS Y CRUD BÁSICO ==========
async function cargarPacientesTabla() {
  const tbody = document.getElementById("pacientesTbody");
  tbody.innerHTML = `<tr><td colspan="6">Cargando...</td></tr>`;
  try {
    const res = await fetch(`${API_URL}/paciente`);
    const data = await res.json();
    if (!data || data.length === 0 || (data[0] && data[0].mensajeError)) {
      tbody.innerHTML = `<tr><td colspan="6">Sin pacientes</td></tr>`;
      return;
    }
    tbody.innerHTML = data.map(crearFilaPaciente).join("");
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="6">Error al cargar</td></tr>`;
  }
}

async function eliminarPaciente(id) {
  if (!confirm("¿Eliminar paciente?")) return;
  try {
    const res = await fetch(`${API_URL}/paciente/${id}`, { method: "DELETE" });
    if (res.ok) cargarPacientesTabla();
    else alert("Error al eliminar");
  } catch (e) {
    alert("Error de conexión");
  }
}

async function editarPaciente(id) {
  try {
    const res = await fetch(`${API_URL}/paciente/${id}`);
    const p = await res.json();
    if (!p || p.mensajeError) return alert("Paciente no encontrado");
    const nombre = prompt("Nombre", p.nombre || "");
    if (nombre === null) return;
    const apellido = prompt("Apellido", p.apellido || "");
    if (apellido === null) return;
    const email = prompt("Email", p.email || "");
    if (email === null) return;
    const celular = prompt("Celular", p.celular || "");
    if (celular === null) return;
    const body = { nombre, apellido, email, celular };
    const upd = await fetch(`${API_URL}/paciente/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    if (upd.ok) cargarPacientesTabla();
    else alert("Error al actualizar");
  } catch (e) {
    alert("Error de conexión");
  }
}

async function cargarDoctoresTabla() {
  const tbody = document.getElementById("doctoresTbody");
  tbody.innerHTML = `<tr><td colspan="8">Cargando...</td></tr>`;
  try {
    const res = await fetch(`${API_URL}/doctor`);
    const data = await res.json();
    if (!data || data.length === 0 || (data[0] && data[0].mensajeError)) {
      tbody.innerHTML = `<tr><td colspan="8">Sin doctores</td></tr>`;
      return;
    }
    tbody.innerHTML = data.map(crearFilaDoctor).join("");
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="8">Error al cargar</td></tr>`;
  }
}

async function eliminarDoctor(id) {
  if (!confirm("¿Eliminar doctor?")) return;
  try {
    const res = await fetch(`${API_URL}/doctor/${id}`, { method: "DELETE" });
    if (res.ok) cargarDoctoresTabla();
    else alert("Error al eliminar");
  } catch (e) {
    alert("Error de conexión");
  }
}

async function editarDoctor(id) {
  try {
    const res = await fetch(`${API_URL}/doctor/${id}`);
    const d = await res.json();
    if (!d || d.mensajeError) return alert("Doctor no encontrado");
    const nombre = prompt("Nombre", d.nombre || "");
    if (nombre === null) return;
    const apellido = prompt("Apellido", d.apellido || "");
    if (apellido === null) return;
    const email = prompt("Email", d.email || "");
    if (email === null) return;
    const celular = prompt("Celular", d.celular || "");
    if (celular === null) return;
    const especialidad = prompt("Especialidad", d.especialidad || "");
    if (especialidad === null) return;
    const horaInicio = prompt("Hora inicio (HH:MM:SS)", d.horaInicio || "");
    if (horaInicio === null) return;
    const horaFin = prompt("Hora fin (HH:MM:SS)", d.horaFin || "");
    if (horaFin === null) return;
    const body = {
      nombre,
      apellido,
      email,
      celular,
      especialidad: { nombre: especialidad },
      horarios: { horaInicio, horaFin },
    };
    const upd = await fetch(`${API_URL}/doctor/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    if (upd.ok) cargarDoctoresTabla();
    else alert("Error al actualizar");
  } catch (e) {
    alert("Error de conexión");
  }
}

// ========== CARGAR SELECTS ==========
async function cargarPacientesSelect() {
  try {
    const response = await fetch(`${API_URL}/paciente`);
    pacientes = await response.json();

    const selects = ["turnoAsignarPaciente", "turnoReservarPaciente"];
    selects.forEach((selectId) => {
      const select = document.getElementById(selectId);
      select.innerHTML = '<option value="">Seleccione un paciente</option>';

      pacientes.forEach((paciente) => {
        if (paciente.id) {
          const option = document.createElement("option");
          option.value = paciente.id;
          option.textContent = `${paciente.nombre} ${paciente.apellido}`;
          select.appendChild(option);
        }
      });
    });
  } catch (error) {
    console.error("Error al cargar pacientes:", error);
  }
}

async function cargarDoctoresSelect() {
  try {
    const response = await fetch(`${API_URL}/doctor`);
    doctores = await response.json();

    const selects = ["turnoAsignarDoctor", "turnoReservarDoctor"];
    selects.forEach((selectId) => {
      const select = document.getElementById(selectId);
      select.innerHTML = '<option value="">Seleccione un doctor</option>';

      doctores.forEach((doctor) => {
        if (doctor.id) {
          const option = document.createElement("option");
          option.value = doctor.id;
          option.textContent = `Dr. ${doctor.nombre} ${doctor.apellido} - ${doctor.especialidad}`;
          select.appendChild(option);
        }
      });
    });
  } catch (error) {
    console.error("Error al cargar doctores:", error);
  }
}

// ========== ASIGNAR TURNO ==========
document
  .getElementById("formAsignarTurno")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const pacienteId = document.getElementById("turnoAsignarPaciente").value;
    const doctorId = document.getElementById("turnoAsignarDoctor").value;

    const turno = {
      paciente: { id: parseInt(pacienteId) },
      doctor: { id: parseInt(doctorId) },
    };

    try {
      const response = await fetch(`${API_URL}/turno/asignar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(turno),
      });

      const data = await response.json();

      if (response.ok && data.id) {
        mostrarMensaje(
          "mensajeAsignar",
          `Turno asignado exitosamente para el ${data.fecha} a las ${data.hora}`,
          "exito"
        );
        document.getElementById("formAsignarTurno").reset();
      } else {
        mostrarMensaje(
          "mensajeAsignar",
          data.mensajeError || "Error al asignar turno",
          "error"
        );
      }
    } catch (error) {
      mostrarMensaje("mensajeAsignar", "Error de conexión", "error");
    }
  });

// ========== RESERVAR TURNO ==========
document
  .getElementById("formReservarTurno")
  .addEventListener("submit", async (e) => {
    e.preventDefault();

    const pacienteId = document.getElementById("turnoReservarPaciente").value;
    const doctorId = document.getElementById("turnoReservarDoctor").value;
    const fechaHora = document.getElementById("turnoFechaHora").value;

    const turno = {
      paciente: { id: parseInt(pacienteId) },
      doctor: { id: parseInt(doctorId) },
      fechaHora: fechaHora,
    };

    try {
      const response = await fetch(`${API_URL}/turno/reservar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(turno),
      });

      const data = await response.json();

      if (response.ok && data.id) {
        mostrarMensaje(
          "mensajeReservar",
          `Turno reservado exitosamente para el ${data.fecha} a las ${data.hora}`,
          "exito"
        );
        document.getElementById("formReservarTurno").reset();
      } else {
        mostrarMensaje(
          "mensajeReservar",
          data.mensajeError || "Error al reservar turno",
          "error"
        );
      }
    } catch (error) {
      mostrarMensaje("mensajeReservar", "Error de conexión", "error");
    }
  });

// ========== DASHBOARD DE TURNOS ==========
async function filtrarTurnos(tipo, event) {
  const turnosList = document.getElementById("turnosList");
  turnosList.innerHTML = '<div class="loading">Cargando turnos...</div>';

  // Actualizar botones activos sólo si viene desde un click
  if (event && event.target && event.target.classList) {
    document.querySelectorAll(".filter-btn").forEach((btn) => {
      btn.classList.remove("active");
    });
    event.target.classList.add("active");
  }

  let url = "";
  switch (tipo) {
    case "todos":
      url = `${API_URL}/turno`;
      break;
    case "pendientes":
      url = `${API_URL}/turno/pendientes`;
      break;
    case "cancelados":
      url = `${API_URL}/turno/cancelados`;
      break;
  }

  try {
    const response = await fetch(url);
    const turnos = await response.json();

    if (
      !turnos ||
      turnos.length === 0 ||
      (turnos[0] && turnos[0].mensajeError)
    ) {
      turnosList.innerHTML =
        '<div class="empty-state">No hay turnos para mostrar</div>';
      return;
    }

    turnosList.innerHTML = "";
    turnos.forEach((turno) => {
      if (turno.id) {
        turnosList.innerHTML += crearTurnoCard(turno);
      }
    });
  } catch (error) {
    turnosList.innerHTML =
      '<div class="empty-state">Error al cargar turnos</div>';
  }
}

function crearTurnoCard(turno) {
  const estado = turno.ocupado ? "ocupado" : "cancelado";
  const estadoTexto = turno.ocupado ? "Ocupado" : "Cancelado";

  return `
        <div class="turno-card ${estado}">
            <div class="turno-header">
                <span class="turno-id">Turno #${turno.id}</span>
                <span class="turno-estado ${estado}">${estadoTexto}</span>
            </div>
            <div class="turno-info">
                <p><strong>📅 Fecha:</strong> ${turno.fecha}</p>
                <p><strong>🕐 Hora:</strong> ${turno.hora}</p>
                <p><strong>👨‍⚕️ Doctor:</strong> Dr. ${turno.doctor.nombre} ${
    turno.doctor.apellido
  }</p>
                <p><strong>📋 Especialidad:</strong> ${
                  turno.doctor.especialidad
                }</p>
                <p><strong>👤 Paciente:</strong> ${turno.paciente.nombre} ${
    turno.paciente.apellido
  }</p>
            </div>
            <button class="btn-cancelar" onclick="cancelarTurno(${turno.id})" ${
    !turno.ocupado ? "disabled" : ""
  }>
                ${turno.ocupado ? "Cancelar Turno" : "Ya Cancelado"}
            </button>
        </div>
    `;
}

async function cancelarTurno(id) {
  if (!confirm("¿Está seguro de cancelar este turno?")) {
    return;
  }

  try {
    const response = await fetch(`${API_URL}/turno/${id}`, {
      method: "DELETE",
    });

    if (response.ok) {
      alert("Turno cancelado exitosamente");
      // Recargar la lista actual
      const filtroActivo = document.querySelector(".filter-btn.active");
      if (filtroActivo) {
        filtroActivo.click();
      }
    } else {
      alert("Error al cancelar turno");
    }
  } catch (error) {
    alert("Error de conexión");
  }
}

// ========== BÚSQUEDAS DE TURNOS ==========
async function buscarTurnoPorId() {
  const id = document.getElementById("buscarTurnoId").value;
  const cont = document.getElementById("turnosBusquedaResultados");
  if (!id) return (cont.innerHTML = '<div class="empty-state">Ingrese un ID</div>');
  cont.innerHTML = '<div class="loading">Buscando...</div>';
  try {
    const res = await fetch(`${API_URL}/turno/${id}`);
    const t = await res.json();
    if (!t || t.mensajeError || !t.id) {
      cont.innerHTML = '<div class="empty-state">No encontrado</div>';
      return;
    }
    cont.innerHTML = crearTurnoCard(t);
  } catch (e) {
    cont.innerHTML = '<div class="empty-state">Error de conexión</div>';
  }
}

async function buscarTurnosPorDoctor() {
  const nombre = document.getElementById("buscarDoctorNombre").value || '';
  const apellido = document.getElementById("buscarDoctorApellido").value || '';
  const cont = document.getElementById("turnosBusquedaResultados");
  if (!nombre && !apellido) return (cont.innerHTML = '<div class="empty-state">Complete nombre y/o apellido</div>');
  cont.innerHTML = '<div class="loading">Buscando...</div>';
  try {
    const url = `${API_URL}/turno/doctor?nombre=${encodeURIComponent(nombre)}&apellido=${encodeURIComponent(apellido)}`;
    const res = await fetch(url);
    const arr = await res.json();
    if (!arr || arr.length === 0 || (arr[0] && arr[0].mensajeError)) {
      cont.innerHTML = '<div class="empty-state">Sin resultados</div>';
      return;
    }
    cont.innerHTML = arr.filter(t => t && t.id).map(crearTurnoCard).join("");
  } catch (e) {
    cont.innerHTML = '<div class="empty-state">Error de conexión</div>';
  }
}

async function buscarTurnosPorPaciente() {
  const nombre = document.getElementById("buscarPacienteNombre").value || '';
  const apellido = document.getElementById("buscarPacienteApellido").value || '';
  const cont = document.getElementById("turnosBusquedaResultados");
  if (!nombre && !apellido) return (cont.innerHTML = '<div class="empty-state">Complete nombre y/o apellido</div>');
  cont.innerHTML = '<div class="loading">Buscando...</div>';
  try {
    const url = `${API_URL}/turno/paciente?nombre=${encodeURIComponent(nombre)}&apellido=${encodeURIComponent(apellido)}`;
    const res = await fetch(url);
    const arr = await res.json();
    if (!arr || arr.length === 0 || (arr[0] && arr[0].mensajeError)) {
      cont.innerHTML = '<div class="empty-state">Sin resultados</div>';
      return;
    }
    cont.innerHTML = arr.filter(t => t && t.id).map(crearTurnoCard).join("");
  } catch (e) {
    cont.innerHTML = '<div class="empty-state">Error de conexión</div>';
  }
}

// ========== UTILIDADES ==========
function mostrarMensaje(elementoId, texto, tipo) {
  const mensaje = document.getElementById(elementoId);
  mensaje.textContent = texto;
  mensaje.className = `mensaje ${tipo}`;

  setTimeout(() => {
    mensaje.className = "mensaje";
  }, 5000);
}

// ========== INICIALIZACIÓN ==========
window.addEventListener("DOMContentLoaded", () => {
  // Cargar turnos al iniciar
  filtrarTurnos("todos");
});
