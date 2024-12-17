<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    Usuario coordinador = (Usuario) request.getAttribute("coordinador");
    ArrayList<Zona> zonas = (ArrayList<Zona>) request.getAttribute("zonas");
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
                <h1 class="logo" style="display: inline-block;"><strong>INFORMACIÓN DEL COORDINADOR ZONAL</strong></h1>
            </header>

            <!-- Información Personal -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/administrador/images/form.png" class="icons">
                        <h2>Información Personal del Coordinador</h2>
                    </header>
                    <p>En la siguiente sección se le muestra los datos del coordinador de la zona <%=coordinador.getZona().getNombre_zona()%>.
                         Modifique los datos si lo ve conveniente.</p>

                    <form id="infoForm" method="post" action="<%=request.getContextPath()%>/ListasAdminServlet?action=actualizar">
                        <div class="row gtr-uniform">
                            <div class="col-12">
                                <input type="hidden" class="input-label" name="idCoordinador" value="<%= coordinador.getId_usuario()%>">
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="nombre" class="input-label">Nombres</label>
                                <input type="text" id="nombre" name="nombre" value="<%=coordinador.getNombres_coordinador()%>" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="apellido" class="input-label">Apellidos</label>
                                <input type="text" id="apellido" name="apellido" value="<%=coordinador.getApellidos_coordinador()%>" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="dni" class="input-label">DNI</label>
                                <input type="text" id="dni" name="dni" value="<%=coordinador.getDni()%>" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="telefono" class="input-label">Número de teléfono</label>
                                <input type="text" id="telefono" name="telefono" value="<%=coordinador.getNumero_yape_plin()%>" required />
                            </div>
                            <div class="col-12">
                                <label for="email" class="input-label">Correo Electrónico</label>
                                <input type="email" id="email" name="correo" value="<%=coordinador.getCorreo_electronico()%>" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="zona" class="input-label">Zona asignada</label>
                                <select id="zona" name="zonaAsignada" required>
                                    <option value="<%=coordinador.getZona().getId_zona()%>" selected><%=coordinador.getZona().getNombre_zona()%></option>
                                    <%
                                        for (Zona zona : zonas) {
                                    %>
                                    <option value="<%=zona.getId_zona()%>"><%=zona.getNombre_zona()%></option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="fechaNacimiento" class="input-label">Fecha de nacimiento</label>
                                <input type="date" id="fechaNacimiento" name="fechaNacimiento" value="<%= coordinador.getFecha_nacimiento() != null ? coordinador.getFecha_nacimiento().toString() : "" %>" required />
                            </div>
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><a href="#" class="button primary big disabled" id="guardarBtn">Guardar</a></li>
                                    <li><a href="<%=request.getContextPath()%>/ListasAdminServlet?action=listaCoord" class="button big">Cancelar</a></li>
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

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const guardarBtn = document.getElementById('guardarBtn');
        const form = document.getElementById('infoForm');

        // Al hacer clic en el enlace "Guardar"
        guardarBtn.addEventListener('click', function (e) {
            e.preventDefault(); // Evita el comportamiento predeterminado del enlace
            if (form.checkValidity()) {
                // Si el formulario es válido, envíalo
                form.submit();
            } else {
                // Si no es válido, muestra un mensaje o activa las validaciones HTML5
                alert('Por favor, completa todos los campos correctamente antes de guardar.');
            }
        });
    });

</script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const nombreInput = document.getElementById('nombre');
        const apellidoInput = document.getElementById('apellido');
        const dniInput = document.getElementById('dni');
        const telefonoInput = document.getElementById('telefono');
        const emailInput = document.getElementById('email');
        const zonaSelect = document.getElementById('zona');
        const fechaNacimientoInput = document.getElementById('fechaNacimiento');
        const guardarBtn = document.getElementById('guardarBtn');

        function verificarCampos() {
            const camposValidos = (
                nombreInput.value.trim() !== "" &&
                apellidoInput.value.trim() !== "" &&
                dniInput.value.length === 8 &&
                telefonoInput.value.length === 9 &&
                emailInput.checkValidity() &&
                zonaSelect.value !== "" &&
                fechaNacimientoInput.value !== ""
            );

            if (camposValidos) {
                guardarBtn.classList.remove('disabled');
                guardarBtn.style.pointerEvents = 'auto';
            } else {
                guardarBtn.classList.add('disabled');
                guardarBtn.style.pointerEvents = 'none';
            }
        }

        function soloLetras(input) {
            input.addEventListener('input', () => {
                input.value = input.value.replace(/[^a-zA-Z\s]/g, ''); // Elimina números y caracteres especiales
                verificarCampos();
            });
        }

        function soloNumeros(input, maxLength) {
            input.addEventListener('input', () => {
                input.value = input.value.replace(/\D/g, '').slice(0, maxLength); // Elimina letras y limita la longitud
                verificarCampos();
            });
        }

        // Aplicar las funciones de validación a cada campo correspondiente
        soloLetras(nombreInput);
        soloLetras(apellidoInput);
        soloNumeros(dniInput, 8);
        soloNumeros(telefonoInput, 9);
        emailInput.addEventListener('input', verificarCampos);
        zonaSelect.addEventListener('change', verificarCampos);
        fechaNacimientoInput.addEventListener('change', verificarCampos);

        // Al hacer clic en "Editar", se habilita la edición de campos
        document.getElementById('editarBtn').addEventListener('click', function (e) {
            e.preventDefault();
            nombreInput.removeAttribute('readonly');
            apellidoInput.removeAttribute('readonly');
            dniInput.removeAttribute('readonly');
            telefonoInput.removeAttribute('readonly');
            emailInput.removeAttribute('readonly');
            zonaSelect.removeAttribute('readonly');
            fechaNacimientoInput.removeAttribute('readonly');
            guardarBtn.style.display = 'inline-block';
            verificarCampos(); // Verifica la validez inicial al habilitar los campos
        });
    });
</script>
</body>
</html>
