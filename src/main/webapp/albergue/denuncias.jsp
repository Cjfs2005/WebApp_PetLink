<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css">
    <script src="${pageContext.request.contextPath}/albergue/assets/js/pagination.js"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/albergue/images/logo_denuncia.png" class="icons">
                <h1 class="logo" style="display: inline-block;"><strong>DENUNCIAS POR MALTRATO ANIMAL</strong></h1>

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

            <!-- Banner -->
            <div class="contenedor-banner">
                <%
                    // Obtener la lista de denuncias pasada desde el servlet
                    List<DenunciaMaltratoAnimal> denuncias = (List<DenunciaMaltratoAnimal>) request.getAttribute("denuncias");
                    if (denuncias != null && !denuncias.isEmpty()) {
                        for (int i = 0; i < denuncias.size(); i++) {
                            DenunciaMaltratoAnimal denuncia = denuncias.get(i);
                %>

                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="" class="icons">
                            <h2>Denuncia #<%= denuncia.getIdDenunciaMaltratoAnimal() %></h2>
                        </header>
                        <ul class="actions">
                            <li>
                                <a href="${pageContext.request.contextPath}/AlbergueDenunciasServlet?accion=detalles&idDenuncia=<%= denuncia.getIdDenunciaMaltratoAnimal() %>" class="button primary big">
                                    Detalles
                                </a>
                            </li>
                        </ul>
                    </div>
                    <span class="image object">
                        <img src="data:image/jpeg;base64,<%= denuncia.getNombreFotoAnimal() %>" alt="Imagen de la denuncia #<%= denuncia.getIdDenunciaMaltratoAnimal() %>" />
                    </span>
                </section>





                <%
                    }
                } else {
                %>
                <p>No hay denuncias disponibles en este momento.</p>
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
<script src="${pageContext.request.contextPath}/albergue/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/main.js"></script>

<!-- Modal -->
<div id="modal" class="modal">
    <div class="modal-content">
        <!-- Botón cerrar -->
        <span class="close-btn">&times;</span>
        <p>¿Estás seguro de que deseas eliminar la denuncia?</p>
        <!-- Botones de Aceptar y Cancelar -->
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big" id="acceptButton">Eliminar</a></li>
        </ul>
    </div>
</div>
<!-- Script dentro del mismo HTML -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const openModalButton = document.getElementById('openModal');
        const modal = document.getElementById('modal');
        const closeModalButton = document.querySelector('.close-btn');
        const acceptButton = document.getElementById('acceptButton');
        const cancelButton = document.getElementById('cancelButton');

        openModalButton.addEventListener('click', function() {
            modal.classList.add('show');
        });

        closeModalButton.addEventListener('click', function() {
            modal.classList.remove('show');
        });

        acceptButton.addEventListener('click', function() {
            window.location.href = 'denuncias_usuario.html';
        });

        cancelButton.addEventListener('click', function() {
            modal.classList.remove('show');
        });
    });
</script>

</body>
</html>
