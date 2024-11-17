<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionEventoBenefico" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%
    ArrayList<PublicacionEventoBenefico> listaEventos = (ArrayList<PublicacionEventoBenefico>) request.getAttribute("listaEventos");
    Usuario usuario = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = usuario.getNombres_usuario_final();
    String apellidoUsuario = usuario.getApellidos_usuario_final();
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/eventos.png" class="icons">
                <h1 class="logo"><strong>EVENTOS BENÉFICOS</strong></h1>

                <!-- Sección para el nombre y enlace al perfil -->
                <a href="<%=request.getContextPath()%>/usuarioFinal/perfil_usuario.html" class="user-profile">
                    <span class="ocultar"><%=nombreUsuario +" " + apellidoUsuario  %></span> <img src="<%=request.getContextPath()%>/usuarioFinal/images/foto_perfil.jpg" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
                </a>
            </header>

            <!-- Search -->

            <section class="seccionPrueba">
                <div class="search-filter-container">

                    <section id="search" class="alt">
                        <label for="query" class="label-filter">Búsqueda específica:</label>
                        <form method="post" action="<%=request.getContextPath()%>/EventoUsuarioServlet?action=buscar" style="margin-bottom: 0;">
                            <input type="text" name="query" id="query" placeholder="Buscar" value="<%=request.getAttribute("busqueda") != null ? request.getAttribute("busqueda") : ""%>"/>
                        </form>

                    </section>

                    <section class="filter-menu">
                        <label for="filter" class="label-filter">Filtrar por:</label>
                        <select name="filter" id="filter">
                            <option value="solicitudes">Todos los eventos</option>
                            <option value="realizadas">Solo inscritos</option>
                        </select>
                    </section>
                </div>
            </section>

            <!-- Banner -->
            <div class="contenedor-banner">
                <%

                    if (listaEventos != null && !listaEventos.isEmpty()) {
                        for (PublicacionEventoBenefico evento : listaEventos) {
                %>
                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="<%= request.getContextPath() %>/usuarioFinal/images/eventosLogo.png" class="icons">
                            <h2><%= evento.getNombreEvento() %></h2>
                        </header>
                        <%
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaFormateada = formatter.format(evento.getFechaHoraRegistro());
                        %>
                        <p><strong>Publicado el: </strong><%=fechaFormateada%></p>
                        <%
                            SimpleDateFormat fechaEvento = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaEventForm = fechaEvento.format(evento.getFechaHoraRegistro());
                        %>
                        <p><strong>Día: </strong><%=fechaEventForm%></p>
                        <%
                            String vacantes;
                            int aforo = evento.getAforoEvento();
                            if (aforo - evento.getCantAsistentes() > 0) {
                                String asistentes = Integer.toString(evento.getAforoEvento() - evento.getCantAsistentes());
                                String aforoStr = Integer.toString(aforo);
                                vacantes = asistentes + "/" + aforoStr;
                            }
                            else {
                                vacantes = "Se ha completado el aforo del evento";
                            }
                        %>
                        <p style="margin-top: 0.5em;"><strong>Vacantes disponibles:</strong> <%=vacantes%> </p>
                        <ul class="actions">
                            <li><a href="<%=request.getContextPath()%>/EventoUsuarioServlet?action=mostrar&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="button primary big">Detalles</a></li>
                        </ul>
                    </div>
                    <span class="image object">
                            <img src="" alt="<%=evento.getNombreFoto()%>" />
                        </span>
                </section>
                <%
                    }
                } else {
                %>
                <p>No hay eventos disponibles en este momento.</p>
                <%
                    }
                %>
            </div>
            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>

        </div>
    </div>

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
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/foto_perfil.jpg" alt="" id="image-perfil">
                        <h2 id="usuario">Ander Vilchez</h2>
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

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

</body>
</html>
