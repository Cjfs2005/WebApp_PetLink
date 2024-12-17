    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.example.webapp_petlink.beans.*" %>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="java.text.SimpleDateFormat" %>
    <%@ page import="java.util.Date" %>
    <%@ page import="java.time.format.DateTimeFormatter" %>
    <%@ page import="java.time.LocalDateTime" %>
    <%
        ArrayList<Usuario> coordinadores = (ArrayList<Usuario>) request.getAttribute("coordinadores");
        // Definir el formato de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

        <%
            System.out.println(coordinadores.size());
        %>
        <!-- Main -->
        <div id="main">
            <div class="inner">

                <!-- Header -->
                <header id="header">
                    <img src="<%=request.getContextPath()%>/administrador/images/GestionDeUsuarios_Transparente.png" class="icons">
                    <h1 class="logo"><strong>LISTA DE COORDINADOR ZONAL</strong></h1>
                </header>

                <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                    <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                        <li style="display: inline; padding-left: 0;">
                            <a href="<%=request.getContextPath()%>/ListasAdminServlet?action=crearCoord" class="button primary big <%= coordinadores.size() >= 4 ? "disabled-link" : "" %>" style="float: left; margin: 0;">
                                Crear nuevo coordinador
                            </a>
                        </li>
                    </ul>
                </section>

                <!-- Banner con botones que abren los modales -->
                <section class="banner">
                    <div class="content">
                        <p><strong>Descripción:</strong> Tabla que permite gestionar a los coordinadores zonales.</p>

                        <div class="table-responsive">
                            <table id="example" class="table table-striped" style="width:100%;">
                                <thead>
                                <tr>
                                    <th>Zona</th>
                                    <th>Nombre del coordinador</th>
                                    <th>Fecha de inscripción</th>
                                    <th>Correo electrónico</th>
                                    <th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                <%
                                    for (Usuario coordinador : coordinadores) {
                                %>
                                <tr>
                                    <td><%=coordinador.getZona().getNombre_zona()%></td>
                                    <td><%=coordinador.getNombres_coordinador()%> <%=coordinador.getApellidos_coordinador()%></td>
                                    <% System.out.println(coordinador.getFecha_hora_creacion());%>
                                    <%
                                        String formattedDate = "";
                                        if (coordinador.getFecha_hora_creacion() != null) {
                                            // Usar DateTimeFormatter para formatear LocalDateTime
                                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                            formattedDate = coordinador.getFecha_hora_creacion().format(formatter);
                                        } else {
                                            formattedDate = "no disponible";
                                        }
                                    %>
                                    <td><%=formattedDate%></td>

                                    <td><%=coordinador.getCorreo_electronico()%></td>
                                    <td>
                                        <ul class="icons">
                                            <li><a href="<%=request.getContextPath()%>/ListasAdminServlet?action=mostrar&id=<%=coordinador.getId_usuario()%>" class="icon fas fa-eye" title="Ver detalles"><span class="label">Ver detalles</span></a></li>
                                            <li><a href="#" class="icon fas fa-times-circle eliminar open-modal-update" title="Eliminar" data-id="<%=coordinador.getId_usuario()%>"><span class="label">Rechazar</span></a></li>
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
                                        sEmptyTable: "No se encontraron coordinadores zonales creados",
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


    <!-- Modal para rechazar publicación -->
    <div class="modal modal-update">
        <div class="modal-content">
            <span class="close-btn">&times;</span>
            <p>¿Estás seguro que quieres aceptar la eliminación del coordinador zonal?</p>
            <ul class="actions modal-buttons">
                <li><a href="#" class="button primary big accept-update" data-id="">Aceptar</a></li>
                <li><a href="#" class="button big cancel-update">Cancelar</a></li>
            </ul>
        </div>
    </div>


    <!-- JavaScript para abrir y cerrar los modales -->
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
                    const baseUrl = "<%=request.getContextPath()%>/ListasAdminServlet?action=eliminar";
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
