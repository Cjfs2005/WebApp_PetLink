<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.RegistroDonacionEconomica" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    String nombreUsuario = usuario.getNombres_usuario_final();
    String apellidoUsuario = usuario.getApellidos_usuario_final();
    String fotoPerfilBase64 = usuario.getFoto_perfil() != null
            ? Base64.getEncoder().encodeToString(usuario.getFoto_perfil())
            : "../../albergue/images/sin_perfil.png";
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

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/donaciones.png" class="icons">
                <h1 class="logo"><strong>HISTORIAL DE DONACIONES</strong></h1>
                <!-- Perfil -->
                <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Tabs -->
            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="<%=request.getContextPath()%>/HistorialDonacionesUsuarioServlet">Colectas de productos</></a>
                <a href="<%=request.getContextPath()%>/HistorialEconomicaUsuarioServlet?action=listar"><strong>Colectas de dinero</strong></a>

            </section>

            <!-- Banner -->
            <%
                List<RegistroDonacionEconomica> historial = (List<RegistroDonacionEconomica>) request.getAttribute("historial");
                if (historial != null && !historial.isEmpty()) {
                    for (RegistroDonacionEconomica registro : historial) {
                        String nombreAlbergue = registro.getSolicitudDonacionEconomica() != null &&
                                registro.getSolicitudDonacionEconomica().getUsuario_albergue() != null
                                ? registro.getSolicitudDonacionEconomica().getUsuario_albergue().getNombre_albergue()
                                : "Información no disponible";
            %>
            <section class="banner">
                <div class="content">
                    <header style="margin-bottom: 0.5em;">
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/imagen2Donaciones.png"
                             class="icons" style="margin-bottom: 0;">
                        <h2 style="margin-bottom: 0;">
                            <%= nombreAlbergue %>: Colecta de dinero
                        </h2>
                    </header>
                    <p><strong>Motivo:</strong>
                        <%= registro.getSolicitudDonacionEconomica().getMotivo() != null ? registro.getSolicitudDonacionEconomica().getMotivo() : "No disponible" %>
                    </p>
                    <p><strong>Monto solicitado:</strong> S/<%= registro.getSolicitudDonacionEconomica().getMonto_solicitado() %></p>
                    <p><strong>Monto donado:</strong> S/<%= registro.getMontoDonacion() %></p>
                    <p style="margin-top: 1em; font-weight: bolder;">
            </span>
                    </p>
                </div>
            </section>


            <%
                }
            } else {
            %>
            <p style="text-align: center;">No hay información disponible para mostrar.</p>
            <% } %>

            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>
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
</body>
</html>
