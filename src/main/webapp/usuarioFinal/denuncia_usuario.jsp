<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Usuario usuarioPerfil = (Usuario) session.getAttribute("usuario");
    String fotoPerfilBase64 = "";
    if (usuarioPerfil.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuarioPerfil.getFoto_perfil());
    }
%>
<!DOCTYPE HTML>
<html lang="es">
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
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
                <img src="${pageContext.request.contextPath}/usuarioFinal/images/logo_denuncia.png" class="icons">
                <h1 class="logo"><strong>DENUNCIAS POR MALTRATO ANIMAL</strong></h1>

                <!-- Sección para el nombre y enlace al perfil -->
                <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
                    <span class="ocultar"><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></span>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
                </a>
            </header>

            <!-- Botón para registrar nueva denuncia -->
            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="<%=request.getContextPath()%>/DenunciaServlet?action=denunciaForm" class="button primary big">
                            Registrar denuncia
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Banner dinámico de denuncias -->
            <div class="contenedor-banner">
                <%
                    // Obtener la lista de denuncias desde la solicitud
                    List<DenunciaMaltratoAnimal> denuncias = (List<DenunciaMaltratoAnimal>) request.getAttribute("denuncias");
                    if (denuncias != null && !denuncias.isEmpty()) {
                        for (DenunciaMaltratoAnimal denuncia : denuncias) {
                %>
                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="<%=request.getContextPath()%>/usuarioFinal/images/logo_denuncia_publicacion.png" class="icons">
                            <h2>Denuncia #<%= denuncia.getIdDenunciaMaltratoAnimal() %></h2>
                        </header>
                        <ul class="actions">
                            <!-- Modificar Denuncia -->
                            <li>
                                <a href="<%=request.getContextPath()%>/DenunciaServlet?action=editarForm&id=<%= denuncia.getIdDenunciaMaltratoAnimal() %>"
                                   class="button primary big">
                                    Modificar
                                </a>
                            </li>

                            <!-- Eliminar Denuncia -->
                            <li>
                                <form action="DenunciaServlet" method="POST" onsubmit="return confirm('¿Estás seguro de que deseas eliminar esta denuncia?');" style="margin-bottom: 0;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= denuncia.getIdDenunciaMaltratoAnimal() %>">
                                    <button type="submit" class="button big">Eliminar</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                    <span class="image object">
                        <% if (denuncia.getFotoAnimal() != null && denuncia.getFotoAnimal().length > 0) { %>
                        <img src="data:image/png;base64,<%= Base64.getEncoder().encodeToString(denuncia.getFotoAnimal()) %>" alt="Foto Denuncia" />
                    <% } else { %>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/maltrato1.jpg" alt="Foto Denuncia" />
                    <% } %>
                    </span>
                </section>
                <% } } else { %>
                <p>No hay denuncias registradas.</p>
                <% } %>
            </div>

            <!-- Paginación -->
            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>
        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= usuarioPerfil.getId_usuario() %>" />
        <jsp:param name="nombresUsuario" value="<%= usuarioPerfil.getNombres_usuario_final() %>" />
        <jsp:param name="apellidosUsuario" value="<%= usuarioPerfil.getApellidos_usuario_final() %>" />
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
