<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 19/11/2024
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.Base64" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">

    <style>
        .charCount {
            font-size: 12px;
            color: #888;
            float: right;
        }
    </style>

</head>

<body class="is-preload">

    <!-- Wrapper -->
    <div id="wrapper">

        <!-- Main -->
        <div id="main">
            <div class="inner">

                <!-- Parte generica, para los siguientes jsp guiarse -->

                <!-- Header -->
                <header id="header">
                    <img src="<%=request.getContextPath()%>/albergue/images/logo_adopcion.png" class="icons">
                    <h1 class="logo" style="display: inline-block;"><strong>ADOPCIONES</strong></h1>

                    <!-- Sección para el nombre y enlace al perfil -->
                    <%
                        Usuario albergue = (Usuario) request.getAttribute("usuario");
                        String fotoPerfilBase64 = "";
                        if (albergue != null && albergue.getFoto_perfil() != null) {
                            fotoPerfilBase64 = Base64 .getEncoder().encodeToString(albergue.getFoto_perfil());
                        }
                    %>
                    <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                        <span class="ocultar"><%= albergue.getNombre_albergue() != null ? albergue.getNombre_albergue() : "Nombre no disponible" %></span>
                        <% if (!fotoPerfilBase64.isEmpty()) { %>
                        <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"
                             style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                        <% } else { %>
                        <img src="<%=request.getContextPath()%>/albergue/images/default_profile.png"
                             style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                        <% } %>
                    </a>

                </header>

                <!-- Parte especifica del jsp -->

                <!-- Banner -->
                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="images/form.png" class="icons">
                            <h2>Detalles de la Mascota</h2>
                        </header>
                        <p><strong>Términos y Condiciones:</strong> Completa el formulario con los detalles de la mascota que deseas dar en adopción. Asegúrate de incluir toda la información relevante. La adopción debe cumplir con los requisitos legales y éticos para el bienestar de la mascota.</p>

                        <form action="AdopcionesAlbergueServlet" method="POST" onsubmit="return confirm('¿Estás seguro de que deseas publicar esta mascota ne adopción?');" style="margin-bottom: 0;" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="crear">
                            <div class="row gtr-uniform">

                                <div class="col-12 col-12-xsmall">
                                    <label for="tipoRaza" class="input-label">Tipo de raza</label>
                                    <input type="text" id="tipoRaza" name="tipoRaza" maxlength="45" required />
                                </div>

                                <!-- Dirección donde fue encontrado -->
                                <div class="col-12">
                                    <label for="direccionEncontrado" class="input-label">Dirección donde fue encontrado (máximo 100 caracteres)</label>
                                    <input type="text" id="direccionEncontrado" name="direccionEncontrado" maxlength="100" required />

                                </div>

                                <!-- Descripción general -->
                                <div class="col-12">
                                    <label for="descripcionGeneral" class="input-label">Descripción general (máximo 300 caracteres)</label>
                                    <textarea id="descripcionGeneral" name="descripcionGeneral" class="text-area" maxlength="300" placeholder="" required></textarea>

                                </div>

                                <div class="col-6 col-12-xsmall">
                                    <label for="generoMascota" class="input-label">Género</label>
                                    <select id="generoMascota" name="generoMascota" required>
                                        <option value="">- Selecciona un género -</option>
                                        <option value="Macho">Macho</option>
                                        <option value="Hembra">Hembra</option>
                                    </select>
                                </div>

                                <!-- Edad aproximada -->
                                <div class="col-6 col-12-xsmall">
                                    <label for="edadAproximada" class="input-label">Edad aproximada</label>
                                    <input type="text" id="edadAproximada" name="edadAproximada" placeholder="" required />
                                </div>

                                <!-- Se encuentra en un hogar temporal -->
                                <div class="col-6 col-12-xsmall">
                                    <label class="input-label">¿Se encuentra en un hogar temporal?</label>
                                    <ul class="actions">
                                        <li><input type="radio" id="hogarTemporalSi" name="hogarTemporal" value="Si" required>
                                            <label for="hogarTemporalSi">Sí</label>
                                        </li>
                                        <li><input type="radio" id="hogarTemporalNo" name="hogarTemporal" value="No">
                                            <label for="hogarTemporalNo">No</label>
                                        </li>
                                    </ul>
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    <label for="nombreMascota" class="input-label">Nombre de la mascota</label>
                                    <input type="text" id="nombreMascota" name="nombreMascota"  maxlength="45" required />
                                </div>

                                <!-- Condiciones para la adopción -->
                                <div class="col-12">
                                    <label for="condicionesAdopcion" class="input-label">Condiciones para la adopción</label>
                                    <textarea id="condicionesAdopcion" class="text-area" name="condicionesAdopcion" maxlength="300" required></textarea>

                                </div>

                                <!-- Adjuntar fotos de la mascota -->
                                <div class="col-12">
                                    <label for="fotosMascota" class="input-label">Adjuntar fotos de la mascota (solo PNG)</label>
                                    <input type="file" id="fotosMascota" name="fotosMascota" accept=".png" required />
                                </div>

                                <!-- Botones -->
                                <div class="col-12">
                                    <ul class="actions form-buttons">
                                        <li><button type="submit" class="button primary big">Publicar</button></li>
                                        <li><a href="AdopcionesAlbergueServlet" class="button big">Cancelar</a></li> <!-- Botón Cancelar -->
                                    </ul>
                                </div>

                            </div>
                        </form>
                    </div>
                </section>


            </div>
        </div>

        <!-- Sidebar -->

        <jsp:include page="navbar.jsp">
            <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
            <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
            <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
        </jsp:include>

    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>

    <!--Modal-->
    <div id="modal" class="modal">
        <div class="modal-content">
            <p>Se ha publicado el post con éxito.</p>
            <!-- Botones de Aceptar y Cancelar -->
            <ul class="actions modal-buttons">
                <li><a href="adopciones.html" class="button primary big" id="acceptButton">Aceptar</a></li>
            </ul>
        </div>
    </div>

    <!-- Java Script -->

    <!-- Verificar el codigo Java Script, especialmente la sentencia this -->

    <script>
        // Contadores de caracteres para los campos
        document.getElementById('direccionEncontrado').addEventListener('input', function () {
            const direccionInput = document.getElementById('direccionEncontrado');
            document.getElementById('charCountDireccion').textContent = `${direccionInput.value.length}/100`;
        });

        document.getElementById('descripcionGeneral').addEventListener('input', function () {
            const descripcionInput = document.getElementById('descripcionGeneral');
            document.getElementById('charCountDescripcion').textContent = `${descripcionInput.value.length}/300`;
        });

        document.getElementById('condicionesAdopcion').addEventListener('input', function () {
            const condicionesInput = document.getElementById('condicionesAdopcion');
            document.getElementById('charCountCondiciones').textContent = `${condicionesInput.value.length}/300`;
        });

        // Validación para la edad aproximada (solo números y menor que 100)
        document.getElementById('edadAproximada').addEventListener('input', function (event) {
            const edadInput = event.target;
            edadInput.value = edadInput.value.replace(/[^0-9]/g, ''); // Solo números
            if (parseInt(edadInput.value, 10) > 99) { // Máximo 99
                alert("La edad debe ser un número menor a 100.");
                edadInput.value = '';
            }
        });

        // Validación para el formato de imagen PNG
        document.getElementById('fotosMascota').addEventListener('change', function (event) {
            const fotosInput = event.target;
            if (fotosInput.files[0] && !fotosInput.files[0].name.endsWith('.png')) {
                alert("Solo se permiten archivos en formato PNG.");
                fotosInput.value = '';
            }
        });

        // Validación al presionar el botón Publicar
        document.getElementById('openModal').addEventListener('click', function (event) {
            event.preventDefault();

            // Campos obligatorios
            const tipoRaza = document.getElementById('tipoRaza');
            const direccionEncontrado = document.getElementById('direccionEncontrado');
            const descripcionGeneral = document.getElementById('descripcionGeneral');
            const generoMascota = document.getElementById('generoMascota');
            const edadAproximada = document.getElementById('edadAproximada');
            const condicionesAdopcion = document.getElementById('condicionesAdopcion');
            const hogarTemporal = document.querySelector('input[name="hogarTemporal"]:checked');
            const fotosMascota = document.getElementById('fotosMascota');

            const camposObligatorios = [
                tipoRaza, direccionEncontrado, descripcionGeneral, generoMascota, edadAproximada, condicionesAdopcion, fotosMascota
            ];

            const camposIncompletos = camposObligatorios.some(campo => !campo.value.trim());

            // Verificar si se seleccionó una opción de hogar temporal
            const hogarTemporalCompleto = hogarTemporal !== null;

            // Verificar si la edad es válida
            const edadValida = parseInt(edadAproximada.value, 10) < 100;

            if (camposIncompletos || !hogarTemporalCompleto || !edadValida) {
                alert("Por favor, complete todos los campos obligatorios y asegúrese de que la edad sea menor a 100.");
                return;
            }

            // Mostrar el modal si todos los campos están completos y válidos
            document.getElementById('modal').classList.add('show');
        });

        // Función para cerrar el modal cuando se hace clic en "Aceptar"
        document.getElementById('acceptButton').addEventListener('click', function () {
            window.location.href = 'adopciones.html';
        });
    </script>

</body>
</html>
