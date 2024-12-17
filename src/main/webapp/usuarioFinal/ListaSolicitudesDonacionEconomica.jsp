<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>

<%
    List<SolicitudDonacionEconomica> solicitudes = (List<SolicitudDonacionEconomica>) request.getAttribute("solicitudes");
    Map<Integer, Integer> montosRecaudados = (Map<Integer, Integer>) request.getAttribute("montosRecaudados");
%>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    String nombreUsuario = usuario.getNombres_usuario_final();
    String apellidoUsuario = usuario.getApellidos_usuario_final();
    String fotoPerfilBase64 = "";
    if (usuario.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
    }
    else {
        fotoPerfilBase64 = "../../albergue/images/sin_perfil.png";
    }
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
</head>
<body class="is-preload">

<div id="wrapper">
    <div id="main">
        <div class="inner">
            <header id="header">

                <img src="<%=request.getContextPath()%>/usuarioFinal/images/donaciones.png" class="icons">
                <h1 class="logo"><strong>SOLICITUDES DE DONACIÓN</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>
            <!-- Opciones de sección -->
            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="<%=request.getContextPath()%>/DonacionProductosUsuarioServlet?action=listar">Colectas de productos</a>
                <a href="<%=request.getContextPath()%>/DonacionEconomicaUsuarioServlet?action=listar"><strong>Colectas de dinero</strong></a>
            </section>

            <div class="contenedor-banner">
                <% if (solicitudes != null && !solicitudes.isEmpty()) { %>
                <% for (SolicitudDonacionEconomica solicitud : solicitudes) { %>
                <section class="banner">
                    <div class="content">
                                <span class="image object">
                                    <% if (solicitud.getUsuario_albergue().getNombre_foto_de_portada() != null) { %>
                                        <img src="data:image/jpeg;base64,<%= solicitud.getUsuario_albergue().getNombre_foto_de_portada() %>" alt="Imagen del albergue" />
                                    <% } else { %>
                                        <img src="images/default-placeholder.png" alt="Imagen por defecto" />
                                    <% } %>
                                </span>
                        <header>
                            <img src="<%=request.getContextPath()%>/usuarioFinal/images/imagen1Donaciones.png" class="icons">
                            <h2><%= solicitud.getUsuario_albergue().getNombre_albergue() %>: Colecta de dinero</h2>
                        </header>
                        <p><strong>Motivo:</strong> <%= solicitud.getMotivo() %></p>
                        <p><strong>Monto recaudado:</strong>
                            S/<%= montosRecaudados.getOrDefault(solicitud.getId_solicitud_donacion_economica(), 0) %>
                            de S/<%= solicitud.getMonto_solicitado() %>
                        </p>
                        <ul class="actions">
                            <li>
                                <a href="DonacionEconomicaUsuarioServlet?action=detalle&id=<%= solicitud.getId_solicitud_donacion_economica() %>" class="button primary big">Donar Dinero</a>
                            </li>

                        </ul>
                    </div>
                </section>
                <% } %>
                <% } else { %>
                <p style="text-align: center; font-size: 1.2em; color: #666;">No hay solicitudes de donación activas en este momento.</p>
                <% } %>
            </div>
        </div>
    </div>
    <!-- Sidebar -->
    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= usuario.getId_usuario() %>" />
        <jsp:param name="nombresUsuario" value="<%= usuario.getNombres_usuario_final() %>" />
        <jsp:param name="apellidosUsuario" value="<%= usuario.getApellidos_usuario_final() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>
</div>

<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

</body>
</html>
