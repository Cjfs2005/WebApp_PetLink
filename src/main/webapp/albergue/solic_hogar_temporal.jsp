<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 13/11/2024
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<%
    PostulacionHogarTemporal postulacion = (PostulacionHogarTemporal) request.getAttribute("postulacion");%>
<!DOCTYPE HTML>
<html lang="es">
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
    <style>
        #charCount {
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

            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>Solicitar hogar temporal</strong></h1>
                <%
                    Usuario usuario = (Usuario) request.getAttribute("usuario");
                    String fotoPerfilBase64 = "";
                    if (usuario != null && usuario.getFoto_perfil() != null) {
                        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
                    }
                %>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombre_albergue() %></span>
                    <img src="<%= !fotoPerfilBase64.isEmpty() ? "data:image/png;base64," + fotoPerfilBase64 : request.getContextPath() + "/albergue/images/default_profile.png" %>"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <!-- Formulario -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/albergue/images/form.png" class="icons">
                        <h2>Complete el siguiente formulario</h2>
                    </header>
                    <p><strong>Términos y condiciones:</strong> Toda la información ingresada será enviada al hogar temporal.</p>
                    <br>
                    <form action="<%=request.getContextPath()%>/TemporalAlbergueServlet?action=registrarSolicitud" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="id_usuario" value="<%= usuario.getId_usuario() %>">
                        <input type="hidden" name="id_postulacion" value="<%=postulacion.getId_postulacion_hogar_temporal()%>">

                        <div class="row gtr-uniform">
                            <div class="col-12">
                                <label for="nombreMascota" class="input-label">Nombre de la Mascota:</label>
                                <input type="text" id="nombreMascota" name="nombreMascota" maxlength="45" required />
                            </div>

                            <div class="col-12">
                                <label for="descripcion_mascota" class="input-label">Descripción de la Mascota:</label>
                                <textarea id="descripcion_mascota" class="text-area" name="descripcionMascota" maxlength="300" required></textarea>
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="fechaInicio" class="input-label">Fecha de Inicio:</label>
                                <input type="date" id="fechaInicio" name="fechaInicio"  min="" onkeydown="return false;" required />
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="fechaFin" class="input-label">Fecha de Fin:</label>
                                <input type="date" id="fechaFin" name="fechaFin"  min="" onkeydown="return false;" required />
                            </div>

                            <div class="col-12">
                                <label for="fotoMascota" class="input-label">Foto de la Mascota:</label>
                                <input type="file" id="fotoMascota" name="fotoMascota" accept="image/png, image/jpeg" class="contenedor-archivo" required />
                            </div>
                            <script>
                                const fechaInicio = document.getElementById('fechaInicio');
                                const fechaFin = document.getElementById('fechaFin');

                                // Fechas obtenidas del backend
                                const fechaInicioPostulacion = new Date('<%= postulacion.getFecha_inicio_temporal() %>');
                                const fechaFinPostulacion = new Date('<%= postulacion.getFecha_fin_temporal() %>');

                                function calcularFechas() {
                                    // Obtener la fecha actual
                                    const fechaActual = new Date();

                                    // Calcular la fecha mínima para fechaInicio
                                    let fechaMinInicio;
                                    if (fechaInicioPostulacion > fechaActual) {
                                        fechaMinInicio = new Date(fechaInicioPostulacion);
                                    } else {
                                        fechaMinInicio = new Date(fechaActual);
                                    }
                                    fechaMinInicio.setDate(fechaMinInicio.getDate() + 1); // Un día después
                                    fechaInicio.min = fechaMinInicio.toISOString().split('T')[0];
                                    fechaInicio.max = fechaFinPostulacion.toISOString().split('T')[0]; // No puede exceder la fecha fin de postulación

                                    // Configuración dinámica para fechaFin
                                    fechaInicio.addEventListener('change', function () {
                                        if (!fechaInicio.value) {
                                            fechaFin.disabled = true;
                                            return;
                                        }

                                        fechaFin.disabled = false;

                                        const fechaInicioSeleccionada = new Date(fechaInicio.value);

                                        // La fecha fin debe ser al menos un día después de fechaInicio
                                        const fechaMinFin = new Date(fechaInicioSeleccionada);
                                        fechaMinFin.setDate(fechaMinFin.getDate() + 1);
                                        fechaFin.min = fechaMinFin.toISOString().split('T')[0];

                                        // La fecha fin no puede exceder la fecha fin de la postulación
                                        fechaFin.max = fechaFinPostulacion.toISOString().split('T')[0];
                                    });
                                }

                                window.onload = calcularFechas;
                            </script>
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><input type="submit" value="Enviar Solicitud" class="button primary big" /></li>
                                    <li><a href="<%=request.getContextPath()%>/TemporalAlbergueServlet?action=listar&id_usuario=<%= usuario.getId_usuario() %>" class="button big">Cancelar</a></li>
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
        <jsp:param name="idUsuario" value="<%= usuario.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= usuario.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>


<script>
    document.addEventListener('DOMContentLoaded', function () {
        const textarea = document.getElementById('descripcion_mascota');
        const charCount = document.getElementById('charCount');

        textarea.addEventListener('input', function () {
            const currentLength = textarea.value.length;
            charCount.textContent = `${currentLength}/300`;
        });
    });
</script>
<!-- Validaciones personalizadas -->
<script>
    // Validación de formato PNG
    document.getElementById('archivo').addEventListener('change', function() {
        if (!this.files[0].name.endsWith('.png')) {
            alert("Solo se permiten archivos en formato PNG.");
            this.value = ''; // Limpiar el campo si no es PNG
        }
    });
</script>
<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js" defer></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js" defer></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js" defer></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js" defer></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js" defer></script>
</body>
</html>
