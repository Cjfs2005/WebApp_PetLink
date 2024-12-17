<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 11/11/2024
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Base64" %>

<% //Java code

    //Se obtiene los datos enviados por el Servlet

    Usuario datosUsuario = (Usuario) session.getAttribute("usuario");
    ArrayList<PublicacionMascotaAdopcion> publicacionesAdopcion = (ArrayList<PublicacionMascotaAdopcion>) request.getAttribute("publicacionesAdopcion");

    // Convertir la foto de perfil (byte[]) a una cadena base64 si no es null
    String fotoPerfilBase64 = "";
    if (datosUsuario != null && datosUsuario.getFoto_perfil() != null) {
        byte[] fotoPerfilBytes = datosUsuario.getFoto_perfil();
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(fotoPerfilBytes);
    }

%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/ola2.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css"/>
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">
<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/logo_adopcion.png" class="icons">
                <h1 class="logo"><strong>MASCOTAS EN ADOPCIÓN</strong></h1>

                <!-- Sección para el nombre y enlace al perfil -->
                <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
                    <span class="ocultar"><%= datosUsuario.getNombres_usuario_final() %> <%= datosUsuario.getApellidos_usuario_final() %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Banner -->
            <div class="contenedor-banner">
                <%
                    if (publicacionesAdopcion != null && !publicacionesAdopcion.isEmpty()) {
                %>
                <% for (PublicacionMascotaAdopcion publicacion : publicacionesAdopcion) { %>

                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="<%=request.getContextPath()%>/usuarioFinal/images/logo_adopcion_publicacion.png"
                                 class="icons">
                            <h2>Adopta a <%= publicacion.getNombreMascota() %>
                            </h2>
                        </header>
                        <ul class="actions">
                            <li>
                                <a href="<%=request.getContextPath()%>/AdopcionesUsuarioFinalServlet?accion=detalles&idPublicacion=<%=publicacion.getIdPublicacionMascotaAdopcion()%>&tiene_postulacion=0" class="button primary big">Ver detalles</a>
                            </li>
                        </ul>
                    </div>
                    <%
                        // Convertir la foto de perfil (byte[]) a una cadena base64 si no es null
                        String fotoMascotaBase64 = "";
                        if (publicacion != null && publicacion.getFotoMascota() != null) {
                            byte[] fotoMascotaBytes = publicacion.getFotoMascota();
                            fotoMascotaBase64 = Base64.getEncoder().encodeToString(fotoMascotaBytes);
                        }
                    %>
                    <span class="image object">
                            <img src="data:image/png;base64,<%= fotoMascotaBase64 %>" alt=""/>
                        </span>
                </section>
                <%}%>
                <% } else { %>
                    <p style="margin-bottom: 0">No hay solicitudes de adopción disponibles en este momento.</p>
                <% } %>

            </div>

            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>

        </div>
    </div>

    <!-- Sidebar -->

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= datosUsuario.getId_usuario() %>" />
        <jsp:param name="nombresUsuario" value="<%= datosUsuario.getNombres_usuario_final() %>" />
        <jsp:param name="apellidosUsuario" value="<%= datosUsuario.getApellidos_usuario_final() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

<!-- Añadido por Gianfranco -->
<!-- Modal -->
<div id="modal" class="modal">
    <div class="modal-content">
        <!-- Botón cerrar -->
        <span class="close-btn">&times;</span>
        <p>Presiona "Confirmar" para que se comuniquen contigo dentro de las próximas
            48 horas y evalúen tu perfil como posible adoptante.</p>
        <!-- Botones de Aceptar y Cancelar -->
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big" id="acceptButton">Confirmar</a></li>
        </ul>
    </div>
</div>
<!-- Script dentro del mismo HTML -->
<script>
    // Esperamos a que todo el DOM esté cargado
    document.addEventListener('DOMContentLoaded', function () {
        // Obtener los elementos del DOM
        const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
        const modal = document.getElementById('modal');               // El modal
        const closeModalButton = document.querySelector('.close-btn'); // Botón para cerrar el modal (X)
        const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar
        const cancelButton = document.getElementById('cancelButton'); // Botón de Cancelar

        // Función para abrir el modal
        openModalButton.addEventListener('click', function () {
            modal.classList.add('show'); // Mostrar el modal
        });

        // Función para cerrar el modal al hacer clic en la "X"
        closeModalButton.addEventListener('click', function () {
            modal.classList.remove('show'); // Ocultar el modal
        });

        // Deben modificar el link para que redirija afuera del formulario
        acceptButton.addEventListener('click', function () {
            window.location.href = 'https://www.ejemplo.com';
        });

        // Función para cerrar el modal al hacer clic en el botón "Cancelar"
        cancelButton.addEventListener('click', function () {
            modal.classList.remove('show'); // Ocultar el modal
        });
    });
</script>
</body>
</html>
