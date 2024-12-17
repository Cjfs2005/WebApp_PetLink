<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionEventoBenefico" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <script src="<%=request.getContextPath()%>/albergue/assets/js/pagination.js"></script>
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
    <style>
        .estado-aprobada {
            color: green;
            font-weight: bold;
        }

        .estado-pendiente {
            color: orange;
            font-weight: bold;
        }

        .estado-rechazada {
            color: red;
            font-weight: bold;
        }

    </style>
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/albergue/images/eventos.png" class="icons">
                <h1 class="logo" style="display: inline-block;"><strong>EVENTOS BENÉFICOS</strong></h1>
                <!-- Sección para el nombre y enlace al perfil -->
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <span class="ocultar"><%= nombreUsuario != null ? nombreUsuario : "Nombre no disponible" %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } else { %>
                    <img src="${pageContext.request.contextPath}/albergue/images/default_profile.png"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Search -->
            <section class="seccionPrueba">
                <div class="search-filter-container">
                    <section id="search" class="alt">
                        <label for="query" class="label-filter">Búsqueda específica:</label>
                        <form method="post" action="<%=request.getContextPath()%>/EventoAlbergueServlet?action=buscar" style="margin-bottom: 0;">
                            <input type="text" name="query" id="query" placeholder="Buscar" value="<%=request.getAttribute("busqueda") != null ? request.getAttribute("busqueda") : ""%>" />
                        </form>
                    </section>

                    <section class="filter-menu">
                        <label for="filter" class="label-filter">Filtrar por:</label>
                        <form method="GET" action="<%=request.getContextPath()%>/EventoAlbergueServlet">

                            <select name="filter" id="filter" onchange="this.form.submit()">
                                <option value="activos" <%= "activos".equals(request.getParameter("filter")) || request.getParameter("filter") == null ? "selected" : "" %>>Eventos próximos</option>
                                <option value="realizados" <%= "realizados".equals(request.getParameter("filter")) ? "selected" : "" %>>Eventos realizados</option>
                                <option value="pendientes" <%= "pendientes".equals(request.getParameter("filter")) ? "selected" : "" %>>Eventos pendientes</option>
                                <option value="rechazados" <%= "rechazados".equals(request.getParameter("filter")) ? "selected" : "" %> >Eventos rechazados</option>
                                <input type="hidden" name="action" value="lista">
                            </select>
                        </form>
                    </section>
                </div>
            </section>

            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="<%=request.getContextPath()%>/EventoAlbergueServlet?action=crear" class="button primary big" style="float: left; margin: 0;">
                            Crear nuevo evento
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Banner -->
            <div class="contenedor-banner">

                <%
                    ArrayList<PublicacionEventoBenefico> eventos = (ArrayList<PublicacionEventoBenefico>) request.getAttribute("eventos");
                    if (eventos != null && !eventos.isEmpty()) {
                        for (PublicacionEventoBenefico evento : eventos) {
                %>
                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="<%=request.getContextPath()%>/albergue/images/eventosLogo.png" class="icons">
                            <h2><%=evento.getNombreEvento()%></h2>
                        </header>
                        <%
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaFormateada = formatter.format(evento.getFechaHoraRegistro());
                        %>
                        <p><strong>Publicado el: </strong><%=fechaFormateada%></p>
                        <%
                            SimpleDateFormat fechaEvento = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaEventForm = fechaEvento.format(evento.getFechaHoraInicioEvento());
                        %>
                        <p><strong>Día: </strong><%=fechaEventForm%></p>
                        <ul class="actions">
                            <li><a href="<%=request.getContextPath()%>/EventoAlbergueServlet?action=mostrar&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="button primary big">Detalles</a></li>
                        </ul>
                    </div>
                    <span class="image object">
                            <% if (evento.getFoto() != null && evento.getFoto().length > 0) { %>
                        <img src="data:image/png;base64,<%= Base64.getEncoder().encodeToString(evento.getFoto()) %>" alt="Imagen de evento" />
                    <% } else { %>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/pic01.jpg" alt="Imagen de evento" />
                    <% } %>
                        </span>
                </section>
                <%
                    }
                } else {
                %>
                <p>No hay eventos benéficos creados.</p>
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
        <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>

</body>
</html>
