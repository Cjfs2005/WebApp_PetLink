<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 13/12/2024
  Time: 18:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%
    ArrayList<Usuario> usuarios = (ArrayList<Usuario>) request.getAttribute("usuarios");

    Usuario administrador = (Usuario) session.getAttribute("datosUsuario");

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

    <!-- Estilos adicionales para el botón deshabilitado -->
    <style>
        .disabled-link {
            pointer-events: none;
            opacity: 0.5;
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
                    <img src="<%=request.getContextPath()%>/administrador/images/GestionDeUsuarios_Transparente.png" class="icons">
                    <h1 class="logo"><strong>LISTA DE SOLICITUDES DE USUARIOS</strong></h1>
                </header>

                <!-- Banner con botones que abren los modales -->
                <section class="banner">
                    <div class="content">
                        <p><strong>Descripción:</strong> Tabla que permite aceptar o rechazar a los usuarios finales registrados</p>

                        <div class="table-responsive">
                            <table id="example" class="table table-striped" style="width:100%;">
                                <thead>
                                <tr>
                                    <th>Distrito</th>
                                    <th>Nombre del usuario</th>
                                    <th>Dirección</th>
                                    <th>Fecha de inscripción</th>
                                    <th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (Usuario usuario : usuarios) {
                                    %>
                                <tr>
                                    <td><%=usuario.getDistrito().getNombre_distrito()%></td>
                                    <td><%=usuario.getNombres_usuario_final()%> <%=usuario.getApellidos_usuario_final()%></td>
                                    <td><%=usuario.getDireccion()%></td>
                                    <%
                                        String formattedDate = "";
                                        if (usuario.getFecha_hora_creacion() != null) {
                                            // Usar DateTimeFormatter para formatear LocalDateTime
                                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                            formattedDate = usuario.getFecha_hora_creacion().format(formatter);
                                        } else {
                                            formattedDate = "no disponible";
                                        }
                                    %>
                                    <td><%=formattedDate%></td>

                                    <td>
                                        <ul class="icons">
                                            <li><a href="#" class="icon fas fa-check-circle open-modal-update" title="Aceptar" data-id="<%=usuario.getId_usuario()%>" data-action="aceptar"> <span class="label">Aceptar</span></a></li>
                                            <li><a href="#" class="icon fas fa-times-circle open-modal-update" title="Eliminar" data-id="<%=usuario.getId_usuario()%>" data-action="rechazar"><span class="label">Rechazar</span></a></li>
                                        </ul>
                                    </td>
                                </tr>
                                    <%
                                        }
                                    %>
                            </table>
                            <script>
                                new DataTable('#example', {
                                    language: {
                                        sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                        sLengthMenu: "Mostrar _MENU_ registros",
                                        sZeroRecords: "No se encontraron resultados",
                                        sEmptyTable: "No se encontraron usuarios",
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

    <!-- Scripts -->
    <script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
    <script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

    <!-- Modal para aceptar usuario -->
    <div class="modal modal-aceptar">
        <div class="modal-content">
            <span class="close-btn">&times;</span>
            <p>¿Estás seguro que quieres aceptar al usuario?</p>
            <ul class="actions modal-buttons">
                <li><a href="#" class="button primary big accept-update" data-id="">Aceptar</a></li>
                <li><a href="#" class="button big cancel-update">Cancelar</a></li>
            </ul>
        </div>
    </div>

    <!-- Modal para rechazar usuario -->
    <div class="modal modal-rechazar">
        <div class="modal-content">
            <span class="close-btn">&times;</span>
            <p>¿Estás seguro que quieres rechazar al usuario?</p>
            <ul class="actions modal-buttons">
                <li><a href="#" class="button primary big accept-update" data-id="">Aceptar</a></li>
                <li><a href="#" class="button big cancel-update">Cancelar</a></li>
            </ul>
        </div>
    </div>

    <script>

        document.addEventListener('DOMContentLoaded', function() {
            // Obtener los modales de Aceptar y Rechazar
            const modalAceptar = document.querySelector('.modal-aceptar');
            const modalRechazar = document.querySelector('.modal-rechazar');

            // Obtener los botones de abrir modal (Aceptar y Rechazar)
            const openModalButtons = document.querySelectorAll('.open-modal-update');

            // Botones para cerrar los modales
            const closeModalButtons = document.querySelectorAll('.close-btn');

            // Botón para cancelar
            const cancelUpdateButtons = document.querySelectorAll('.cancel-update');

            // Botón para aceptar (en los dos modales)
            const acceptUpdateButtons = document.querySelectorAll('.accept-update');

            openModalButtons.forEach(function(button) {
                button.addEventListener('click', function(event) {
                    event.preventDefault();
                    const userId = button.getAttribute('data-id');
                    const action = button.getAttribute('data-action'); // Obtener la acción (aceptar o rechazar)

                    let modalToShow = null;
                    let baseUrl = "";

                    // Mostrar el modal correspondiente
                    if (action === "aceptar") {
                        modalToShow = modalAceptar;
                        baseUrl = "<%=request.getContextPath()%>/ListasAdminServlet?action=aceptarUsuario";
                    } else if (action === "rechazar") {
                        modalToShow = modalRechazar;
                        baseUrl = "<%=request.getContextPath()%>/ListasAdminServlet?action=rechazarUsuario";
                    }

                    // Establecer el href del botón "Aceptar" en el modal con la URL correcta
                    const acceptButton = modalToShow.querySelector('.accept-update');
                    acceptButton.setAttribute('href', baseUrl + '&id=' + userId);

                    // Mostrar el modal correspondiente
                    modalToShow.classList.add('show');
                });
            });

            // Cerrar modales
            closeModalButtons.forEach(function(closeBtn) {
                closeBtn.addEventListener('click', function() {
                    const modal = closeBtn.closest('.modal');
                    modal.classList.remove('show'); // Ocultar el modal
                });
            });

            // Cancelar acción en el modal
            cancelUpdateButtons.forEach(function(cancelBtn) {
                cancelBtn.addEventListener('click', function(event) {
                    event.preventDefault();
                    const modal = cancelBtn.closest('.modal');
                    modal.classList.remove('show'); // Ocultar el modal
                });
            });

            // Aceptar la acción (en ambos modales)
            acceptUpdateButtons.forEach(function(acceptBtn) {
                acceptBtn.addEventListener('click', function() {
                    // El enlace href será utilizado para redirigir al usuario
                    const modal = acceptBtn.closest('.modal');
                    modal.classList.remove('show'); // Opcional: Cerrar el modal
                });
            });
        });

    </script>

</body>
</html>
