<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 10/11/2024
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudHogarTemporal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    // Formateador para las fechas
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    boolean esBaneado = (boolean) request.getAttribute("esBaneado");
    LocalDate currentDate = LocalDate.now();
    PostulacionHogarTemporal ultimaPostulacion = usuario.getUltima_postulacion_hogar_temporal();
    String estadoUsuario = "Indefinido";
    String periodoInicio = "";
    String periodoFin = "";
    String botonTexto = "";
    String botonLink = "";
    boolean mostrarPeriodo = false;

    if (esBaneado) {
        estadoUsuario = "BANEADO";
        botonTexto = ""; // No mostrar botón
    } else if (ultimaPostulacion == null) {
        estadoUsuario = "INHABILITADO";
        botonTexto = "POSTULAR PARA SER HOGAR TEMPORAL";
        botonLink = request.getContextPath() + "/TemporalUsuarioServlet?action=postularForm&id_usuario=" + usuario.getId_usuario();
    } else if (ultimaPostulacion.getEstado() != null && "Pendiente".equals(ultimaPostulacion.getEstado().getNombre_estado())
            && ultimaPostulacion.getFecha_inicio_temporal().isAfter(currentDate)) {
        estadoUsuario = "PENDIENTE";
        botonTexto = "MODIFICAR POSTULACIÓN";
        botonLink = request.getContextPath() + "/TemporalUsuarioServlet?action=modificarForm&id_usuario=" + usuario.getId_usuario();
    } else if (ultimaPostulacion.getEstado() != null && "Aprobada".equals(ultimaPostulacion.getEstado().getNombre_estado())) {
        estadoUsuario = "HABILITADO";
        mostrarPeriodo = true;
        if (ultimaPostulacion != null && ultimaPostulacion.getFecha_inicio_temporal() != null) {
            periodoInicio = ultimaPostulacion.getFecha_inicio_temporal().format(formatter);
        }
        if (ultimaPostulacion != null && ultimaPostulacion.getFecha_fin_temporal() != null) {
            periodoFin = ultimaPostulacion.getFecha_fin_temporal().format(formatter);
        }
    } else if (ultimaPostulacion.getEstado() == null ||
            "Rechazada".equals(ultimaPostulacion.getEstado().getNombre_estado()) ||
            (ultimaPostulacion.getFecha_fin_temporal() != null && ultimaPostulacion.getFecha_fin_temporal().isBefore(currentDate))) {
        estadoUsuario = "INHABILITADO";
        botonTexto = "POSTULAR PARA SER HOGAR TEMPORAL";
        botonLink = request.getContextPath() + "/TemporalUsuarioServlet?action=postularForm&id_usuario=" + usuario.getId_usuario();
    }

    String fotoPerfilBase64 = "";
    if (usuario.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
    }
%>
<%
    ArrayList<SolicitudHogarTemporal> solicitudes = (ArrayList<SolicitudHogarTemporal>) request.getAttribute("solicitudes");
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination_bigbanner.js"></script>
    <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">
<div id="wrapper">
    <div id="main">
        <div class="inner">
            <header id="header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/hogarTemporal.png" class="icons">
                <h1 class="logo"><strong>HOGAR TEMPORAL</strong></h1>
                <a href="perfil_usuario.html" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <section class="seccionPrueba">
                <div class="search-filter-container" style="display: flex; align-items: center;">
                    <section id="search" class="alt">
                        <h3 style="margin-bottom: 0.1em;">ESTADO DEL USUARIO: <span style="color:<%= "HABILITADO".equals(estadoUsuario) ? "green" : "BANEADO".equals(estadoUsuario) ? "red" : "orange" %>;">
                                    <%= estadoUsuario %></span></h3>
                        <% if (mostrarPeriodo) { %>
                        <p style="margin-bottom: 0.1em;"><strong>PERIODO DE HOGAR TEMPORAL:</strong></p>
                        <p style="margin-bottom: 0.1em;"><%= periodoInicio %> - <%= periodoFin %></p>
                        <% } %>
                    </section>

                    <section class="filter-menu">
                        <% if (!botonTexto.isEmpty()) { %>
                        <a href="<%= botonLink %>" class="button primary big" style="max-width: fit-content;align-self: self-end;line-height: 3.4;"><%= botonTexto %></a>
                        <% } %>
                    </section>
                </div>
            </section>
            <style>
                @media screen and (max-width: 768px) {

                    .filter-menu a{
                        width: 100%;
                        max-width: 100% !important;
                    }
                }
            </style>
            <% if (solicitudes != null && !solicitudes.isEmpty() && estadoUsuario.equals("HABILITADO")) { %>
            <!-- Iterar sobre cada solicitud y mostrarla como un banner -->
            <% for (SolicitudHogarTemporal solicitud : solicitudes) { %>
            <section class="banner" style="<%= "Rechazada".equals(solicitud.getEstado().getNombre_estado()) ? "background-color: #dab61398;" : "" %>">
            <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/hogarTemporalLogo.png" class="icons">
                        <h1>Solicitud del albergue "<%= solicitud.getUsuarioAlbergue().getNombre_albergue() %>"</h1>
                    </header>
                    <p><h3>Estado de la solicitud: <span style="color:
                    <%= "Pendiente".equals(solicitud.getEstado().getNombre_estado()) ? "orange" :
                        "Rechazada".equals(solicitud.getEstado().getNombre_estado()) ? "red" : "green" %>;">
                    <%= solicitud.getEstado().getNombre_estado() %></span></h3></p>
                    <p><strong>Nombre de la mascota:</strong> <%= solicitud.getNombreMascota() %> </p>
                    <p><strong>Descripción de la mascota:</strong> <%= solicitud.getDescripcionMascota() %></p>
                    <p><strong>Periodo solicitado:</strong> <%= sdf.format(solicitud.getFechaInicio()) %> - <%= sdf.format(solicitud.getFechaFin()) %></p>
                    <% if ("Pendiente".equals(solicitud.getEstado().getNombre_estado())){%>
                    <ul class="actions">
                        <li><a href="<%=request.getContextPath()%>/TemporalUsuarioServlet?action=aceptarSolicitud&id_usuario=<%=usuario.getId_usuario()%>&id_solicitud=<%=solicitud.getIdSolicitudHogarTemporal()%>" class="button primary big">ACEPTAR</a></li>
                        <li><a href="<%=request.getContextPath()%>/TemporalUsuarioServlet?action=rechazarSolicitud&id_usuario=<%=usuario.getId_usuario()%>&id_solicitud=<%=solicitud.getIdSolicitudHogarTemporal()%>" class="button big">RECHAZAR</a></li>
                    </ul>
                    <%}%>
                </div>
                <span class="image object">
                <% if (solicitud.getFotoMascota() != null && !solicitud.getFotoMascota().isEmpty()) { %>
                    <img src="data:image/png;base64,<%= solicitud.getFotoMascota() %>" alt="Imagen de mascota" />
                <% } else { %>
                    <img src="<%=request.getContextPath()%>/usuarioFinal/images/default_pet.png" alt="Imagen de mascota" />
                <% } %>
            </span>
            </section>
            <% } %>
            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>
            <% } %>
        </div>
    </div>

    <div id="sidebar">
        <div class="inner">
            <section class="alt" id="sidebar-header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id="sidebar-title">PetLink</p>
            </section>

            <section class="perfil">
                <div class="mini-posts">
                    <article>
                        <% if (!fotoPerfilBase64.isEmpty()) { %>
                        <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil" id="image-perfil">
                        <% } %>
                        <h2 id="usuario"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></h2>
                    </article>
                </div>
            </section>

            <nav id="menu">
                <header class="major"><h2>Menu</h2></header>
                <ul>
                    <li><a href="perfil_usuario.html">PERFIL</a></li>
                    <li><span class="opener">ALBERGUES</span>
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

            <nav id="logout">
                <a href="../bienvenidos.html" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>
        </div>
    </div>
</div>

<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>
</body>
</html>

