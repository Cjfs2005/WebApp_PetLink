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

    Usuario datosUsuario = (Usuario) request.getAttribute("datosUsuario");
    ArrayList<PublicacionMascotaAdopcion> publicacionesAdopcion = (ArrayList<PublicacionMascotaAdopcion>) request.getAttribute("publicacionesAdopcion");

    // Convertir la foto de perfil (byte[]) a una cadena base64 si no es null
    String fotoPerfilBase64 = "";
    if (datosUsuario != null && datosUsuario.getFoto_perfil() != null) {
        byte[] fotoPerfilBytes = datosUsuario.getFoto_perfil();
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(fotoPerfilBytes);
    }

    String Nombres_usuario_final = datosUsuario.getNombres_usuario_final();
    String Apellidos_usuario_final = datosUsuario.getApellidos_usuario_final();

%>

<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/ola2.css" />
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
    <link rel="icon" href="images/favicon.png" type="image/x-icon">
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
                <a href="perfil_usuario.html" class="user-profile">
                    <span class="ocultar">
                        <%= datosUsuario != null ? datosUsuario.getNombres_usuario_final() + " " + datosUsuario.getApellidos_usuario_final() : "" %>
                    </span>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"  style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
                </a>
            </header>

            <!-- Banner -->
            <div class="contenedor-banner">

                <% for (PublicacionMascotaAdopcion publicacion : publicacionesAdopcion) { %>

                    <section class="banner">
                        <div class="content">
                            <header>
                                <img src="<%=request.getContextPath()%>/usuarioFinal/images/logo_adopcion_publicacion.png" class="icons">
                                <h2>Adopta a <%= publicacion.getNombreMascota() %></h2>
                            </header>
                            <ul class="actions">
                                <li>
                                    <a href="formulario_adopcion.html" class="button primary big">Ver detalles</a>
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
                                                <img src="data:image/png;base64,<%= fotoMascotaBase64 %>" alt="" />
                        </span>
                    </section>

                <%}%>

            </div>

            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>

        </div>
    </div>

    <!-- Sidebar Start -->

    <!-- Sidebar -->
    <div id="sidebar">
        <div class="inner">

            <!-- Logo -->

            <section class="alt" id="sidebar-header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id ="sidebar-title">PetLink</p>
            </section>

            <!-- Perfil -->

            <section class="perfil">

                <div class="mini-posts">
                    <article>
                        <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="" id="image-perfil">
                        <h2 id="usuario"><%= datosUsuario != null ? datosUsuario.getNombres_usuario_final() + " " + datosUsuario.getApellidos_usuario_final() : "" %></h2>
                    </article>
                </div>

            </section>

            <!-- Menu -->
            <nav id="menu">
                <header class="major">
                    <h2>Menu</h2>
                </header>
                <ul>
                    <li><a href="perfil_usuario.html">PERFIL</a></li>
                    <li>
                        <span class="opener">ALBERGUES</span>
                        <ul>
                            <li><a href="albergue_usuario.html">LISTA DE ALBERGUES</a></li>
                            <li><a href="eventos.html">EVENTOS BENÉFICOS</a></li>
                            <li><a href="Donaciones1.html">SOLICITUDES DE DONACIÓN</a></li>
                            <li><a href="Donaciones_historial.html">HISTORIAL DE DONACIONES</a></li>
                            <li><a href="adopciones_usuario.html">MASCOTAS EN ADOPCIÓN</a></li>
                            <li><a href="adopciones_historial_usuario.html">HISTORIAL DE ADOPCIONES</a></li>
                        </ul>
                    </li>
                    <li><a href="Hogar_temporal.html">HOGAR TEMPORAL</a></li>
                    <li><a href="denuncias_usuario.html">DENUNCIAS POR MALTRATO ANIMAL</a></li>
                    <li><a href="mascotas_perdidas_usuario.html">MASCOTAS PERDIDAS</a></li>
                </ul>
            </nav>
            <!-- Logout -->

            <nav id="logout">
                <a href="../bienvenidos.html" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>

        </div>
    </div>

    <!-- Sidebar End -->

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
    document.addEventListener('DOMContentLoaded', function() {
        // Obtener los elementos del DOM
        const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
        const modal = document.getElementById('modal');               // El modal
        const closeModalButton = document.querySelector('.close-btn'); // Botón para cerrar el modal (X)
        const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar
        const cancelButton = document.getElementById('cancelButton'); // Botón de Cancelar

        // Función para abrir el modal
        openModalButton.addEventListener('click', function() {
            modal.classList.add('show'); // Mostrar el modal
        });

        // Función para cerrar el modal al hacer clic en la "X"
        closeModalButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });

        // Deben modificar el link para que redirija afuera del formulario
        acceptButton.addEventListener('click', function() {
            window.location.href = 'https://www.ejemplo.com';
        });

        // Función para cerrar el modal al hacer clic en el botón "Cancelar"
        cancelButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });
    });
</script>
</body>
</html>
