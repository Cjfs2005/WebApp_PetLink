<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%
    Usuario administrador = (Usuario) session.getAttribute("datosUsuario");
    Usuario albergue = (Usuario) request.getAttribute("albergue");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/administrador/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/administrador/images/cjfs.png" class="icons">
                <h1 class="logo" style="display: inline-block;"><strong>DATOS DEL ALBERGUE</strong></h1>
            </header>

            <!-- Información sobre el Albergue -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/administrador/images/form.png" class="icons">
                        <h2>Información sobre el Albergue</h2>
                    </header>
                    <p>Estos son los detalles personales registrados en el sistema para el albergue Huellitas.</p>

                    <div class="row gtr-uniform">
                        <!-- Nombre -->
                        <div class="col-6 col-12-xsmall">
                            <label for="nombre" class="input-label">Nombres del encargado</label>
                            <input type="text" id="nombre" name="nombre" value="<%=albergue.getNombres_encargado()%>" disabled />
                        </div>

                        <!-- Apellido -->
                        <div class="col-6 col-12-xsmall">
                            <label for="apellido" class="input-label">Apellidos del encargado</label>
                            <input type="text" id="apellido" name="apellido" value="<%=albergue.getApellidos_encargado()%>" disabled />
                        </div>

                        <!-- Nombre del albergue -->
                        <div class="col-12">
                            <label for="nombre_albergue" class="input-label">Nombre del albergue</label>
                            <input type="text" id="nombre_albergue" name="nombre_albergue" value="<%= albergue.getNombre_albergue()%>" disabled />
                        </div>

                        <!-- Correo Electrónico -->
                        <div class="col-12">
                            <label for="correo" class="input-label">Correo Electrónico</label>
                            <input type="email" id="correo" name="correo" value="<%=albergue.getCorreo_electronico()%>" disabled />
                        </div>

                        <div class="col-12">
                            <label for="instagram" class="input-label">Instagram</label>
                            <input type="text" id="instagram" name="instagram" value="<%=albergue.getUrl_instagram()%>" disabled />
                        </div>

                        <!-- Botones -->
                        <div class="col-12">
                            <ul class="actions form-buttons">
                                <li><a href="#" class="button primary big open-modal-found openModal" data-id="<%=albergue.getId_usuario()%>"><span class="label">Aceptar</span></a></li> <!-- fa-check para aceptar -->
                                <li><a href="#" class="button big open-modal-update openModal1" data-id="<%=albergue.getId_usuario()%>"><span class="label">Rechazar</span></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>


    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
    </jsp:include>
    </div>

    <!-- Scripts -->
    <script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

<div class="modal modal-found">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>Estás seguro que quieres aceptar la solicitud de acceso del albergue?</p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big accept-found" data-id="">Aceptar</a></li>
            <li><a href="#" class="button big cancel-found">Cancelar</a></li>
        </ul>
    </div>
</div>

<!-- Modal para rechazar publicación -->
<div class="modal modal-update">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>¿Estás seguro que quieres eliminar la solicitud de acceso de este albergue?</p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big accept-update" data-id="">Aceptar</a></li>
            <li><a href="#" class="button big cancel-update">Cancelar</a></li>
        </ul>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const modalUpdate = document.querySelector('.modal-update');
        const openModalUpdateButtons = document.querySelectorAll('.open-modal-update');
        const closeModalUpdateButton = modalUpdate.querySelector('.close-btn');
        const acceptUpdateButton = modalUpdate.querySelector('.accept-update');
        const cancelUpdateButton = modalUpdate.querySelector('.cancel-update');

        openModalUpdateButtons.forEach(function(button) {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                const coordinatorId = button.getAttribute('data-id'); // Obtener el ID del coordinador
                const baseUrl = "<%=request.getContextPath()%>/SolicitudAlbergueAdminServlet?action=rechazar";
                acceptUpdateButton.setAttribute('href', baseUrl + '&id=' + coordinatorId); // Concatenar la URL
                modalUpdate.classList.add('show'); // Mostrar el modal
            });
        });

        closeModalUpdateButton.addEventListener('click', function() {
            modalUpdate.classList.remove('show'); // Ocultar el modal
        });

        cancelUpdateButton.addEventListener('click', function(event) {
            event.preventDefault();
            modalUpdate.classList.remove('show'); // Ocultar el modal
        });

        acceptUpdateButton.addEventListener('click', function() {
            modalUpdate.classList.remove('show'); // Opcional: Ocultar el modal
        });
    });

</script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const modalUpdate = document.querySelector('.modal-found');
        const openModalUpdateButtons = document.querySelectorAll('.open-modal-found');
        const closeModalUpdateButton = modalUpdate.querySelector('.close-btn');
        const acceptUpdateButton = modalUpdate.querySelector('.accept-found');
        const cancelUpdateButton = modalUpdate.querySelector('.cancel-found');

        openModalUpdateButtons.forEach(function(button) {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                const coordinatorId = button.getAttribute('data-id'); // Obtener el ID del coordinador
                const baseUrl = "<%=request.getContextPath()%>/SolicitudAlbergueAdminServlet?action=aceptar";
                acceptUpdateButton.setAttribute('href', baseUrl + '&id=' + coordinatorId); // Concatenar la URL
                modalUpdate.classList.add('show'); // Mostrar el modal
            });
        });

        closeModalUpdateButton.addEventListener('click', function() {
            modalUpdate.classList.remove('show'); // Ocultar el modal
        });

        cancelUpdateButton.addEventListener('click', function(event) {
            event.preventDefault();
            modalUpdate.classList.remove('show'); // Ocultar el modal
        });

        acceptUpdateButton.addEventListener('click', function() {
            modalUpdate.classList.remove('show'); // Opcional: Ocultar el modal
        });
    });
</script>
</body>
</html>