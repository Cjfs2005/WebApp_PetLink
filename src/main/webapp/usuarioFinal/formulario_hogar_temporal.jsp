<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 11/11/2024
  Time: 12:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.time.LocalDate" %>

<%
    // Datos del usuario obtenidos de la sesión o el request
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    String nombresUsuario = usuario.getNombres_usuario_final();
    String apellidosUsuario = usuario.getApellidos_usuario_final();
    String distritoUsuario = usuario.getDistrito().getNombre_distrito();
    String direccionUsuario = usuario.getDireccion();
    String fotoPerfilBase64 = "";
    String idUsuario = Integer.toString(usuario.getId_usuario());
    if (usuario.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
    }
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink - Postulación para ser hogar temporal</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">
<!-- Wrapper -->
<div id="wrapper">
    <!-- Main -->
    <div id="main">
        <div class="inner">
            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>Postulación para ser hogar temporal</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/form.png" class="icons">
                        <h2 style="margin-bottom: 0 !important;">Complete el siguiente formulario</h2>
                    </header>

                    <!-- Formulario de Postulación -->
                    <form action="TemporalUsuarioServlet?action=postular" method="POST" style="margin-bottom: 0;" enctype="multipart/form-data">
                        <div class="row gtr-uniform">
                            <!-- Nombres y Apellidos (solo visualización) -->
                            <div class="col-6 col-12-xsmall">
                                <label for="nombres" class="input-label">Nombres</label>
                                <input type="text" id="nombres" value="<%= nombresUsuario %>" readonly disabled/>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="apellidos" class="input-label">Apellidos</label>
                                <input type="text" id="apellidos" value="<%= apellidosUsuario %>" readonly disabled/>
                            </div>

                            <!-- Edad y Género -->
                            <div class="col-6 col-12-xsmall">
                                <label for="edad" class="input-label">Edad</label>
                                <input type="text" name="edad" id="edad" value="" placeholder="" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="genero" class="input-label">Género</label>
                                <select name="genero" id="genero" required>
                                    <option value="">Seleccione su género</option>
                                    <option value="Masculino">Masculino</option>
                                    <option value="Femenino">Femenino</option>
                                    <option value="Prefiero no decirlo">Prefiero no decirlo</option>
                                </select>
                            </div>

                            <!-- Teléfono y Distrito (distrito solo visualización) -->
                            <div class="col-6 col-12-xsmall">
                                <label for="telefono" class="input-label">Número de teléfono</label>
                                <input type="text" name="telefono" id="telefono" value="" placeholder="" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="distrito" class="input-label">Distrito</label>
                                <input type="text" id="distrito" value="<%= distritoUsuario %>" readonly disabled/>
                            </div>

                            <!-- Dirección (solo visualización) -->
                            <div class="col-12">
                                <label for="direccion" class="input-label">Dirección de residencia</label>
                                <input type="text" id="direccion" value="<%= direccionUsuario %>" readonly disabled/>
                            </div>

                            <!-- Cantidad de Cuartos y Metraje -->
                            <div class="col-6 col-12-xsmall">
                                <label for="cuartos" class="input-label">Cantidad de cuartos</label>
                                <input type="text" name="cuartos" id="cuartos" placeholder="" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="metraje" class="input-label">Metraje de vivienda (m&sup2)</label>
                                <input type="text" name="metraje" id="metraje" required>
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="tieneHijo" class="input-label">¿Tiene hijos?</label>
                                <select id="tieneHijo" name="tieneHijo" required>
                                    <option value="">Seleccione su respuesta</option>
                                    <option value="1">Sí tengo hijos</option>
                                    <option value="2">No tengo hijos</option>
                                </select>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="tieneMascota" class="input-label">¿Tiene mascotas?</label>
                                <select id="tieneMascota" name="tieneMascota" required>
                                    <option value="">Seleccione su respuesta</option>
                                    <option value="1">Sí tengo mascotas</option>
                                    <option value="2">No tengo mascotas</option>
                                </select>
                            </div>

                            <!-- Dependientes de mascota -->
                            <div class="col-6 col-12-xsmall" id="aparecen-dependientes1" style="display: none;">
                                <label for="numMascotas" class="input-label">Número de mascotas</label>
                                <input type="text" id="numMascotas" value="" placeholder=""/>
                            </div>
                            <div class="col-12" id="aparecen-dependientes2" style="display: none;">
                                <label for="tipoMascotas" class="input-label">Tipo de mascotas (Indicar la cantidad de cada tipo)</label>
                                <input type="text" id="tipoMascotas" name="tipoMascotas" value="" placeholder="">
                            </div>

                            <script>
                                // Habilitar/deshabilitar campos de mascota
                                const selectMascota = document.getElementById('tieneMascota');
                                const inputTipoMascotas = document.getElementById('aparecen-dependientes2');
                                function toggleInputs() {
                                    if (selectMascota.value === '1') {
                                        inputTipoMascotas.style.display = 'block';
                                    } else {
                                        inputTipoMascotas.style.display = 'none';
                                    }
                                }
                                selectMascota.addEventListener('change', toggleInputs);
                                window.onload = toggleInputs;
                            </script>

                            <div class="col-6 col-12-xsmall">
                                <label for="viveSolo" class="input-label">¿Vive solo o con dependientes?</label>
                                <select id="viveSolo" name="viveSolo" required>
                                    <option value="">Seleccione su respuesta</option>
                                    <option value="1">Vivo solo</option>
                                    <option value="2">Vivo con dependientes</option>
                                </select>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="trabajaRemoto" class="input-label">¿Trabaja remoto o presencial?</label>
                                <select id="trabajaRemoto" name="trabajaRemoto" required>
                                    <option value="">Seleccione su respuesta</option>
                                    <option value="1">Trabajo remoto</option>
                                    <option value="2">Trabajo presencial</option>
                                </select>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="nombreRef" class="input-label">Nombre de persona de referencia</label>
                                <input type="text" id="nombreRef" name="nombreRef" value="" placeholder="" required/>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="numeroRef" class="input-label">Número de teléfono de referencia</label>
                                <input type="text" id="numeroRef" name="numeroRef" placeholder="" required/>
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="fechaInicio" class="input-label">Fecha de inicio</label>
                                <input type="date" id="fechaInicio" name="fechaInicio" value="" required/>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="fechaFin" class="input-label">Fecha de fin</label>
                                <input type="date" id="fechaFin" name="fechaFin"  value="" required disabled/>
                            </div>

                            <script>
                                const fechaInicio = document.getElementById('fechaInicio');
                                const fechaFin = document.getElementById('fechaFin');
                                function calcularFechas() {
                                    const fechaActual = new Date();
                                    fechaActual.setDate(fechaActual.getDate() + 3);
                                    const fechaMinInicio = fechaActual.toISOString().split('T')[0];
                                    fechaInicio.min = fechaMinInicio;
                                    fechaInicio.addEventListener('change', function() {
                                        if (!fechaInicio.value) {
                                            fechaFin.disabled = true;
                                            return;
                                        }
                                        fechaFin.disabled = false;
                                        const fechaInicioSeleccionada = new Date(fechaInicio.value);
                                        fechaInicioSeleccionada.setDate(fechaInicioSeleccionada.getDate() + 14);
                                        fechaFin.min = fechaInicioSeleccionada.toISOString().split('T')[0];
                                    });
                                }
                                window.onload = calcularFechas;
                            </script>

                            <div class="col-12">
                                <label for="archivo" class="input-label">Foto del lugar del temporal (Máximo 4 fotos)</label>
                                <input type="file" accept="image/png" multiple id="archivo" name="archivo" class="contenedor-archivo" required/>
                            </div>
                            <script>
                                document.getElementById('archivo').addEventListener('change', function(event) {
                                    if (this.files.length > 4) {
                                        alert('Solo puedes subir un máximo de 4 fotos.');
                                        this.value = '';
                                    }
                                });
                            </script>
                            <!-- Enviar y regresar -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><button type="button" class="button primary big" id="confirmSubmitButton">ENVIAR</button></li>
                                    <li><a href="<%=request.getContextPath()%>/TemporalUsuarioServlet?id_usuario=<%=usuario.getId_usuario()%>" class="button big">REGRESAR</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </section>
        </div>
    </div>
    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= usuario.getId_usuario() %>" />
        <jsp:param name="nombresUsuario" value="<%= usuario.getNombres_usuario_final() %>" />
        <jsp:param name="apellidosUsuario" value="<%= usuario.getApellidos_usuario_final() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Botón de confirmación
        const confirmSubmitButton = document.getElementById('confirmSubmitButton');

        // Seleccionar todos los inputs requeridos dentro del formulario
        const requiredFields = document.querySelectorAll('input[required], select[required]');

        // Validar y mostrar confirmación nativa del navegador
        confirmSubmitButton.addEventListener('click', function() {
            // Verificar si todos los campos requeridos están completos
            const allFieldsFilled = Array.from(requiredFields).every(field => field.checkValidity());

            if (allFieldsFilled) {
                // Confirmación nativa del navegador
                if (confirm("¿Estás seguro de postular para ser hogar temporal?")) {
                    document.querySelector('form').submit(); // Enviar formulario si se confirma
                }
            } else {
                // Mostrar el mensaje de error nativo del navegador para los campos vacíos
                requiredFields.forEach(field => field.reportValidity());
            }
        });
    });
</script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const dateInputs = document.querySelectorAll('input[type="date"]');
        dateInputs.forEach(function(input) {
            input.addEventListener('keydown', function(event) {
                event.preventDefault(); // Evita cualquier entrada manual
            });
            input.addEventListener('paste', function(event) {
                event.preventDefault(); // Bloquea la acción de pegar texto
            });
        });
    });
</script>
</body>
</html>
<!--<script>
document.addEventListener('DOMContentLoaded', function () {
// Obtener los elementos del DOM
const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
const modal = document.getElementById('modal');               // El modal
const closeModalButton = document.querySelector('.close-btn'); // Botón para cerrar el modal (X)
const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar
const cancelButton = document.getElementById('cancelButton'); // Botón de Cancelar

// Seleccionar todos los inputs requeridos dentro del formulario
const requiredFields = document.querySelectorAll('input[required], select[required]');

// Función para validar campos y mostrar el modal si todos están completados
function validarYMostrarModal(event) {
// Verificar si todos los campos requeridos están completos
const allFieldsFilled = Array.from(requiredFields).every(field => field.checkValidity());

if (allFieldsFilled) {
modal.classList.add('show'); // Mostrar el modal
} else {
// Mostrar el mensaje de error nativo del navegador para los campos vacíos
requiredFields.forEach(field => field.reportValidity());
}
}

// Función para cerrar el modal al hacer clic en la "X"
closeModalButton.addEventListener('click', function () {
modal.classList.remove('show'); // Ocultar el modal
});

// Redirigir cuando el usuario haga clic en el botón de Aceptar dentro del modal
acceptButton.addEventListener('click', function () {
window.location.href = 'Hogar_temporal.html'; // Redirigir a la página destino
});

// Función para cerrar el modal al hacer clic en el botón "Cancelar"
cancelButton.addEventListener('click', function () {
modal.classList.remove('show'); // Ocultar el modal
});

// Validar y abrir el modal solo si todos los campos requeridos están llenos
openModalButton.addEventListener('click', validarYMostrarModal);
});
</script>-->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Campos del formulario
        const edadInput = document.getElementById('edad');
        const telefonoInput = document.getElementById('telefono');
        const cuartosInput = document.getElementById('cuartos');
        const metrajeInput = document.getElementById('metraje');
        const nombreRefInput = document.getElementById('nombreRef');
        const numeroRefInput = document.getElementById('numeroRef');
        const numeroMascotas = document.getElementById('numMascotas');

        // Función para restringir a números con un límite de dígitos
        function limitarNumeros(input, maxLength) {
            input.addEventListener('input', function () {
                this.value = this.value.replace(/\D/g, ''); // Elimina cualquier cosa que no sea número
                if (this.value.length > maxLength) {
                    this.value = this.value.slice(0, maxLength); // Limitar a maxLength dígitos
                }
            });
        }

        // Función para limitar caracteres en texto
        function limitarCaracteres(input, maxLength) {
            input.addEventListener('input', function () {
                if (this.value.length > maxLength) {
                    this.value = this.value.slice(0, maxLength); // Limitar a maxLength caracteres
                }
            });
        }

        // Aplicar restricciones
        limitarNumeros(edadInput, 3);         // Edad: máximo 3 dígitos
        limitarNumeros(telefonoInput, 9);     // Teléfono: máximo 9 dígitos
        limitarNumeros(cuartosInput, 2);      // Cuartos: máximo 2 dígitos
        limitarNumeros(metrajeInput, 10);     // Metraje: máximo 10 dígitos
        limitarCaracteres(nombreRefInput, 45); // Nombre de referencia: máximo 45 caracteres
        limitarNumeros(numeroRefInput, 9);    // Número de referencia: máximo 9 dígitos
        limitarNumeros(numeroMascotas,2);
    });
</script>