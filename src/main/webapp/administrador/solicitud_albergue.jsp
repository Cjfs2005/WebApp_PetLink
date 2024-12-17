<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%
    Usuario administrador = (Usuario) session.getAttribute("datosUsuario");
    ArrayList<Usuario> albergues = (ArrayList<Usuario>) request.getAttribute("albergues");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/administrador/images/favicon.png" type="image/x-icon">

    <!--Agregar-->

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
    <!--Fin Agregar-->

</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/administrador/images/solicitudes_albergue.png" class="icons">
                <h1 class="logo"><strong>Solicitudes pendientes de Albergue</strong></h1>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> Tabla que permite gestionar las postulaciones de los albergues.</p>

                    <div class="table-responsive">
                        <table table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>N°</th>
                                <th>Nombre del Albergue</th>
                                <th>Fecha de solicitud</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                int contador = 1;
                                for (Usuario albergue : albergues) {
                            %>
                            <tr>
                                <td><%= contador++%></td>
                                <td><%= albergue.getNombre_albergue()%></td>
                                <%
                                    String formattedDate = "";
                                    if (albergue.getFecha_hora_creacion() != null) {
                                        // Usar DateTimeFormatter para formatear LocalDateTime
                                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                        formattedDate = albergue.getFecha_hora_creacion().format(formatter);
                                    } else {
                                        formattedDate = "no disponible";
                                    }
                                %>
                                <td><%=formattedDate%></td>
                                <td>
                                    <ul class="icons">
                                        <li><a href="#" class="icon fas fa-check-circle open-modal-found openModal" title="Aceptar evento" data-id="<%=albergue.getId_usuario()%>"><span class="label">Aceptar</span></a></li> <!-- fa-check para aceptar -->
                                        <li><a href="#" class="icon fas fa-times-circle open-modal-update openModal1" title="Rechazar evento" data-id="<%=albergue.getId_usuario()%>"><span class="label">Rechazar</span></a></li> <!-- fa-times para rechazar -->
                                        <li><a href="<%=request.getContextPath()%>/SolicitudAlbergueAdminServlet?action=ver&id=<%=albergue.getId_usuario()%>" class="icon fas fa-eye" title="Ver"><span class="label">Ver información</span></a></li>
                                    </ul>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                    sLengthMenu: "Mostrar _MENU_ registros",
                                    sZeroRecords: "No se encontraron resultados",
                                    sEmptyTable: "No se encontraron albergues",
                                    sInfo: "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                                    sInfoEmpty: "Mostrando registros del 0 al 0 de un total de 0 registros",
                                    sInfoFiltered: "(filtrado de un total de _MAX_ registros)",
                                    sLoadingRecords: "Cargando...",
                                }
                            });
                        </script>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
    </jsp:include>


</div>

<!-- Ventana emergente -->
<div id="modal" class="modal">
    <div class="modal-content">
        <p id="modal-message"></p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big" id="acceptModal">Aceptar</a></li>
        </ul>
    </div>
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
