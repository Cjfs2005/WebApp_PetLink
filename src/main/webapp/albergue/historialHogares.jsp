<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 16/11/2024
  Time: 13:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudHogarTemporal" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    // Formateador para las fechas
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<%
    ArrayList<SolicitudHogarTemporal> solicitudes = (ArrayList<SolicitudHogarTemporal>) request.getAttribute("solicitudes");
%>
<!DOCTYPE HTML>
<html lang="es">
<head>
    <title>PetLink - Hogares Temporales Disponibles</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
    <script src="<%=request.getContextPath()%>/albergue/assets/js/pagination_bigbanner.js"></script>
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">
<div id="wrapper">
    <div id="main">
        <div class="inner">
            <header id="header">
                <img src="<%=request.getContextPath()%>/albergue/images/logo_hogarTemporal.png" class="icons">
                <h1 class="logo"><strong>HOGAR TEMPORAL</strong></h1>
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

            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="TemporalAlbergueServlet">Hogares disponibles</a>
                <a href="TemporalAlbergueServlet?action=historial"><strong>Hogares solicitados</strong></a>
            </section>

            <% if (solicitudes != null && !solicitudes.isEmpty()) { %>
            <!-- Iterar sobre cada solicitud y mostrarla como un banner -->
            <% for (SolicitudHogarTemporal solicitud : solicitudes) { %>
            <section class="banner" style="<%= "Rechazada".equals(solicitud.getEstado().getNombre_estado()) ? "background-color: #dab61398;" : "" %>">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/hogarTemporalLogo.png" class="icons">
                        <h1>Solicitud para <%= solicitud.getPostulacionHogarTemporal().getUsuario_final().getNombres_usuario_final() %> <%= solicitud.getPostulacionHogarTemporal().getUsuario_final().getApellidos_usuario_final() %></h1>
                    </header>
                    <p><h3>Estado de la solicitud: <span style="color:
                    <%= "Pendiente".equals(solicitud.getEstado().getNombre_estado()) ? "orange" :
                        "Rechazada".equals(solicitud.getEstado().getNombre_estado()) ? "red" : "green" %>;">
                    <%= solicitud.getEstado().getNombre_estado() %></span></h3></p>
                    <p><strong>Nombre de la mascota:</strong> <%= solicitud.getNombreMascota() %> </p>
                    <p><strong>Descripción de la mascota:</strong> <%= solicitud.getDescripcionMascota() %></p>
                    <p><strong>Periodo solicitado:</strong> <%= sdf.format(solicitud.getFechaInicio()) %> - <%= sdf.format(solicitud.getFechaFin()) %></p>
                </div>
                <span class="image object">
                <% if (solicitud.getFotoMascota() != null && solicitud.getFotoMascota().length > 0) { %>
                    <img src="data:image/png;base64,<%= Base64.getEncoder().encodeToString(solicitud.getFotoMascota()) %>" alt="Imagen de mascota" />
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

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>

<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
</body>
</html>


