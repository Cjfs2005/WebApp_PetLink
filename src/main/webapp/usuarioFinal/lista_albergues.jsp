<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: GIANFRANCO
  Date: 10/12/2024
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Base64" %>
<%

    Usuario usuario = (Usuario) session.getAttribute("usuario");

    String fotoPerfilBase64 = "";
    if (usuario != null && usuario.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
    }

%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css">
    <script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/pagination.js"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <header id="header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/albergues.png" class="icons">
                <h1 class="logo"><strong>ALBERGUES</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Search

            <section class="seccionPrueba">
                <div class="search-filter-container">
                    <section id="search" class="alt">
                        <label for="query" class="label-filter">Búsqueda específica:</label>
                        <form method="post" action="#" style="margin-bottom: 0;">
                            <input type="text" name="query" id="query" placeholder="Buscar" />
                        </form>
                    </section>

                    <section class="filter-menu">
                    </section>
                </div>
            </section>
            -->

            <!-- Banner -->
            <div class="contenedor-banner">
                <%
                    List<Usuario> listaAlbergues = (List<Usuario>) session.getAttribute("listaAlbergues");
                    if (listaAlbergues != null && !listaAlbergues.isEmpty()) {
                        for (Usuario albergue : listaAlbergues) {
                            String fotoPortadaBase64 = null;
                            if (albergue.getFoto_de_portada_albergue() != null) {
                                fotoPortadaBase64 = java.util.Base64.getEncoder().encodeToString(albergue.getFoto_de_portada_albergue());
                            }
                %>
                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="${pageContext.request.contextPath}/usuarioFinal/images/petHouse.png" class="icons">
                            <h2>"<%=albergue.getNombre_albergue()%>"</h2>
                        </header>
                        <ul class="actions">
                            <li><a href="<%= request.getContextPath() %>/ListaAlberguesServlet?accion=detalles&id=<%=albergue.getId_usuario()%>" class="button primary big">Ver detalles</a></li>
                        </ul>

                    </div>
                    <span class="image object">
                        <% if (fotoPortadaBase64 != null) { %>
                            <img src="data:image/png;base64,<%=fotoPortadaBase64%>" alt="<%=albergue.getNombre_foto_de_portada()%>" />
                        <% } else { %>
                            <img src="images/default-image.png" alt="Imagen de portada no disponible" />
                        <% } %>
                    </span>
                </section>
                <%
                    }
                } else {
                %>
                <p>No hay albergues disponibles.</p>
                <%
                    }
                %>
            </div>





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
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/main.js"></script>

</body>
</html>