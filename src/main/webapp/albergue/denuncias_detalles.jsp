<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
<%@ page import="java.util.Base64" %>

<%
    // Obtener el usuario de la sesión
    Usuario albergue = (Usuario) session.getAttribute("usuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }

    // Obtener la denuncia desde el request
    DenunciaMaltratoAnimal denuncia = (DenunciaMaltratoAnimal) request.getAttribute("denuncia");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css">
    <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">


    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">
<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="${pageContext.request.contextPath}/albergue/images/logo_denuncia.png" class="icons">
                <h1 class="logo" style="display: inline-block;"><strong>Detalles de la Denuncia</strong></h1>
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

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="${pageContext.request.contextPath}/albergue/images/logo_denuncia_publicacion.png" class="icons">
                        <h2>Información de la denuncia #<%= denuncia != null ? denuncia.getIdDenunciaMaltratoAnimal() : "N/A" %></h2>
                    </header>

                    <div class="contenedor-imagenes">
                        <%
                            if (denuncia != null && denuncia.getFotoAnimal() != null) {
                                String fotoAnimalBase64 = Base64.getEncoder().encodeToString(denuncia.getFotoAnimal());
                        %>
                        <img src="data:image/jpeg;base64,<%= fotoAnimalBase64 %>" alt="Imagen de la denuncia #<%= denuncia.getIdDenunciaMaltratoAnimal() %>">
                        <% } else { %>
                        <img src="${pageContext.request.contextPath}/albergue/images/default_denuncia.png" alt="Imagen no disponible">
                        <% } %>
                    </div>

                    <p><strong>Descripción: </strong><%= denuncia != null ? denuncia.getDescripcionMaltrato() : "Información no disponible" %></p>
                    <p><strong>Información General:</strong></p>
                    <ul>
                        <li><strong>Nombre del maltratador: </strong><%= denuncia != null && denuncia.getNombreMaltratador() != null ? denuncia.getNombreMaltratador() : "No especificado" %></li>
                        <li><strong>Tamaño: </strong><%= denuncia != null ? denuncia.getTamanio() : "No especificado" %></li>
                        <li><strong>Raza: </strong><%= denuncia != null ? denuncia.getRaza() : "No especificado" %></li>
                        <li><strong>Dirección del animal maltratado: </strong><%= denuncia != null ? denuncia.getDireccionMaltrato() : "No especificado" %></li>
                        <li><strong>¿Se ha hecho la denuncia policial? </strong><%= denuncia != null && denuncia.isTieneDenunciaPolicial() ? "Sí" : "No" %></li>
                    </ul>
                </div>
            </section>
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

<!-- Script dentro del mismo HTML -->
<script>
    // Esperamos a que todo el DOM esté cargado
    document.addEventListener('DOMContentLoaded', function() {
        // Obtener los elementos del DOM
        const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
        const modal = document.getElementById('modal');               // El modal
        const closeModalButton = document.querySelector('.close-btn'); // Botón para cerrar el modal (X)
        const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar
        const cancelButton = document.getElementById('cancelButton'); // Botón de Cancelar

        // Función para abrir el modal
        openModalButton.addEventListener('click', function() {
            modal.classList.add('show'); // Mostrar el modal
        });

        // Función para cerrar el modal al hacer clic en la "X"
        closeModalButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });

        // Deben modificar el link para que redirija afuera del formulario
        acceptButton.addEventListener('click', function() {
            window.location.href = 'index.html';
        });

        // Función para cerrar el modal al hacer clic en el botón "Cancelar"
        cancelButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });
    });
</script>
</body>
</html>
