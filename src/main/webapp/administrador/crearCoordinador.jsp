<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%
  ArrayList<Zona> zonas = (ArrayList<Zona>) request.getAttribute("zonas");
  // Definir el formato de fecha
  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  Usuario administrador = (Usuario) session.getAttribute("datosUsuario");
%>

<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/main.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/administrador/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <!-- Header -->
      <header id="header">
        <img src="<%=request.getContextPath()%>/administrador/images/cjfs.png" class="icons">
        <h1 class="logo" style="display: inline-block;"><strong>GESTIÓN DE USUARIOS</strong></h1>
      </header>

      <!-- Formulario de Coordinador Zonal -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="<%=request.getContextPath()%>/administrador/images/form.png" class="icons">
            <h2>Formulario de Coordinador Zonal</h2>
          </header>
          <p>Complete el siguiente formulario para registrar un nuevo coordinador zonal. Asegúrese de proporcionar todos los detalles solicitados de manera correcta.</p>

          <form method="POST" action="<%=request.getContextPath()%>/ListasAdminServlet?action=guardar">
            <div class="row gtr-uniform">
              <!-- Nombre -->
              <div class="col-6 col-12-xsmall">
                <label for="nombre" class="input-label">Nombre <span class="required">*</span></label>
                <input type="text" id="nombre" name="nombre" placeholder="Nombre" required />
              </div>

              <!-- Apellido -->
              <div class="col-6 col-12-xsmall">
                <label for="apellido" class="input-label">Apellido <span class="required">*</span></label>
                <input type="text" id="apellido" name="apellido" placeholder="Apellido" required />
              </div>

              <!-- DNI -->
              <div class="col-6 col-12-xsmall">
                <label for="dni" class="input-label">DNI <span class="required">*</span></label>
                <input type="text" id="dni" name="dni" placeholder="DNI" required />
              </div>

              <!-- Teléfono -->
              <div class="col-6 col-12-xsmall">
                <label for="telefono" class="input-label">Teléfono <span class="required">*</span></label>
                <input type="text" id="telefono" name="telefono" placeholder="Teléfono" required />
              </div>

              <!-- Correo Electrónico -->
              <div class="col-12">
                <label for="correo" class="input-label">Correo Electrónico <span class="required">*</span></label>
                <input type="email" id="correo" name="correo" placeholder="Correo Electrónico" required />
              </div>

              <!-- Zona Asignada -->
              <div class="col-6 col-12-xsmall">
                <label for="zonaAsignada" class="input-label">Zona Asignada <span class="required">*</span></label>
                <select id="zonaAsignada" name="zonaAsignada" required>
                  <option value="sin-zona">Seleccione una zona</option>
                  <%
                    if (zonas != null && !zonas.isEmpty()) {
                  %>
                  <% for (Zona zona : zonas) { %>
                  <option value="<%=zona.getId_zona()%>"><%=zona.getNombre_zona()%></option>
                  <% } %>
                  <% } %>
                </select>
              </div>

              <!-- Fecha de Nacimiento -->
              <div class="col-6 col-12-xsmall">
                <label for="fechaNacimiento" class="input-label">Fecha de Nacimiento <span class="required">*</span></label>
                <input type="date" id="fechaNacimiento" name="fechaNacimiento" required />
              </div>

              <!-- Botones -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><a href="#" class="button primary big" id="creationButton">Aceptar</a></li>
                  <li><a href="<%=request.getContextPath()%>/ListasAdminServlet?action=listaCoord" class="button big" id="cancelar">Cancelar</a></li>
                </ul>
              </div>
            </div>
          </form>

        </div>
      </section>

    </div>
  </div>

  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
  </jsp:include>

</div>

<!-- Modal para creación de lugar -->
<div id="creationModal" class="modal">
  <div class="modal-content">
    <p>¡Se creó con éxito!</p>
    <ul class="actions modal-buttons">
      <li><a href="#" class="button primary big" id="modalAcceptLink">Aceptar</a></li> <!--closeCreationModal-->
    </ul>
  </div>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

<!-- Script de manejo de modales -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const creationButton = document.getElementById('creationButton');
    const creationModal = document.getElementById('creationModal');
    const modalAcceptLink = document.getElementById('modalAcceptLink'); // Enlace "Aceptar" del modal
    const form = document.querySelector('form'); // Formulario principal

    creationButton.addEventListener('click', function (e) {
      e.preventDefault(); // Evitar el envío inmediato del formulario
      if (form.checkValidity()) { // Verifica si el formulario es válido
        creationModal.classList.add('show'); // Muestra el modal
      }
    });

    // Enviar el formulario al presionar el enlace del modal
    modalAcceptLink.addEventListener('click', function (e) {
      e.preventDefault(); // Evitar el comportamiento predeterminado del enlace
      creationModal.classList.remove('show'); // Opcional: ocultar el modal
      form.submit(); // Envía el formulario al servidor
    });
  });
</script>

<script>
  document.addEventListener('DOMContentLoaded', function () {
    // Obtener elementos del formulario
    const nombreInput = document.getElementById('nombre');
    const apellidoInput = document.getElementById('apellido');
    const dniInput = document.getElementById('dni');
    const telefonoInput = document.getElementById('telefono');
    const correoInput = document.getElementById('correo');
    const fechaNacimientoInput = document.getElementById('fechaNacimiento');
    const zonaAsignadaSelect = document.getElementById('zonaAsignada');
    const creationButton = document.getElementById('creationButton');
    const creationModal = document.getElementById('creationModal');
    const closeCreationModalButton = document.getElementById('closeCreationModal');

    // Inicialmente deshabilitar el botón de creación
    creationButton.classList.add('disabled');
    creationButton.style.pointerEvents = 'none'; // Deshabilitar clics

    // Función para verificar todos los campos
    function verificarCampos() {
      const camposValidos = (
              nombreInput.value.trim() !== "" &&
              apellidoInput.value.trim() !== "" &&
              dniInput.value.trim() !== "" &&
              telefonoInput.value.trim().length === 9 && // Teléfono exactamente 9 dígitos
              correoInput.value.trim() !== "" &&
              fechaNacimientoInput.value.trim() !== "" &&
              zonaAsignadaSelect.value !== "sin-zona" && // Validación de zona asignada
              correoInput.checkValidity() && // Correo válido
              fechaNacimientoInput.value <= new Date().toISOString().split("T")[0] // Fecha válida
      );

      // Habilitar o deshabilitar el botón en función de la validez de los campos
      if (camposValidos) {
        creationButton.classList.remove('disabled');
        creationButton.style.pointerEvents = 'auto'; // Permitir clics
      } else {
        creationButton.classList.add('disabled');
        creationButton.style.pointerEvents = 'none'; // Deshabilitar clics
      }
    }

    // Aplicar restricciones en tiempo real
    function soloLetras(input) {
      input.addEventListener('input', function () {
        this.value = this.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
        verificarCampos(); // Verificar después de cada cambio
      });
    }

    function soloNumeros(input, maxLength) {
      input.addEventListener('input', function () {
        this.value = this.value.replace(/\D/g, '');
        if (this.value.length > maxLength) {
          this.value = this.value.slice(0, maxLength);
        }
        verificarCampos(); // Verificar después de cada cambio
      });
    }

    function validarCorreo(input) {
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      input.addEventListener('input', function () {
        input.setCustomValidity(regex.test(this.value) ? "" : "Correo electrónico no válido");
        verificarCampos(); // Verificar después de cada cambio
      });
    }

    function bloquearFechaFutura(input) {
      const hoy = new Date().toISOString().split("T")[0];
      input.setAttribute("max", hoy);
      input.addEventListener('keydown', function(event) { event.preventDefault(); });
      input.addEventListener('paste', function(event) { event.preventDefault(); });
      input.addEventListener('input', verificarCampos); // Verificar después de cada cambio
    }

    // Aplicar restricciones a cada campo
    soloLetras(nombreInput);
    soloLetras(apellidoInput);
    soloNumeros(dniInput, 8);
    soloNumeros(telefonoInput, 9); // Teléfono: exactamente 9 dígitos
    validarCorreo(correoInput);
    bloquearFechaFutura(fechaNacimientoInput);
    zonaAsignadaSelect.addEventListener('change', verificarCampos); // Validar zona asignada al cambiar

    // Evitar recarga y mostrar el modal cuando los campos sean válidos
    creationButton.addEventListener('click', function(e) {
      e.preventDefault();
      creationModal.classList.add('show');
    });

    // Cerrar el modal al hacer clic en "Aceptar"
    closeCreationModalButton.addEventListener('click', function() {
      creationModal.classList.remove('show');
      window.location.href = '<%=request.getContextPath()%>/ListasAdminServlet';
    });
  });
</script>
</body>
</html>
