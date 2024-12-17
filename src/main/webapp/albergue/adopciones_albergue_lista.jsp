<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 19/11/2024
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.Base64" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">

    <script src="<%=request.getContextPath()%>/albergue/assets/js/pagination.js"></script>

</head>
<body class="is-preload">

    <!-- Wrapper -->
    <div id="wrapper">

        <!-- Main -->
        <div id="main">
            <div class="inner">

                <!-- Parte generica, para los siguientes jsp guiarse -->

                <!-- Header -->
                <header id="header">
                    <img src="<%=request.getContextPath()%>/albergue/images/logo_adopcion.png" class="icons">
                    <h1 class="logo" style="display: inline-block;"><strong>ADOPCIONES</strong></h1>

                    <!-- Sección para el nombre y enlace al perfil -->
                    <%
                        Usuario albergue = (Usuario) session.getAttribute("usuario");
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

                <!-- Parte especifica del jsp -->

                <!-- Buscador
                <section class="seccionPrueba">
                    <div class="search-filter-container">
                        <section id="search" class="alt">
                            <label for="query" class="label-filter">Búsqueda específica:</label>
                            <form method="post" action="#" style="margin-bottom: 0;">
                                <input type="text" name="query" id="query" placeholder="Buscar" />
                            </form>
                        </section>
                    </div>
                </section>-->

                <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                    <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                        <li style="display: inline; padding-left: 0;">
                            <a href="<%=request.getContextPath()%>/AdopcionesAlbergueServlet?action=formulario" class="button primary big" style="float: left; margin: 0;">
                                Publicar aviso
                            </a>
                        </li>
                    </ul>
                </section>

                <div class="contenedor-banner">

                    <%
                        ArrayList<PublicacionMascotaAdopcion> publicacionesAdopcion = (ArrayList<PublicacionMascotaAdopcion>) request.getAttribute("publicacionesAdopcion");
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
                                    <a href="<%=request.getContextPath()%>/AdopcionesAlbergueServlet?action=detalles&id_publicacion=<%=publicacion.getIdPublicacionMascotaAdopcion()%>" class="button primary big">Ver detalles</a>
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

                </div>

                <div class="pagination-container" style="display: flex; justify-content: center;">
                    <ul class="pagination"></ul>
                </div>


            </div>
        </div>

        <!-- Sidebar -->

        <jsp:include page="navbar.jsp">
            <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
            <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
            <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
        </jsp:include>

    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>

</body>
</html>
