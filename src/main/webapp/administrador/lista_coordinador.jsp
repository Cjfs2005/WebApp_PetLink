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
                <img src="<%=request.getContextPath()%>/administrador/images/GestionDeUsuarios_Transparente.png" class="icons">
                <h1 class="logo"><strong>LISTA DE COORDINADOR ZONAL</strong></h1>
            </header>

            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="<%=request.getContextPath()%>/ListasAdminServlet?action=crearCoord" class="button primary big" style="float: left; margin: 0;">
                            Crear nuevo coordinador
                        </a>
                    </li>
                </ul>
            </section>

            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="<%=request.getContextPath()%>/ListasAdminServlet?action=crearCoord" class="button primary big" style="float: left; margin: 0;">
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
                                <%
                                    String formattedDate = "";
                                    if (coordinador.getFecha_hora_creacion() != null) {
                                        formattedDate = dateFormat.format(coordinador.getFecha_hora_creacion());
                                    }
                                    else {
                                        formattedDate = "no disponible";
                                    }
                                %>
                                <td><%=formattedDate%></td>
                                <td><%=coordinador.getCorreo_electronico()%></td>
                                <td>
                                    <ul class="icons">
                                        <li><a href="informacion_personal_coordinador.html" class="icon fas fa-eye" title="Ver detalles"><span class="label">Ver detalles</span></a></li>
                                        <li><a href="#" class="icon fas fa-times-circle eliminar open-modal-update" title="Eliminar" data-usuario="Juan Pérez"><span class="label">Rechazar</span></a></li>
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
                                    sEmptyTable: "Ningún coordinador zonal inscrito",
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

    <!-- Sidebar -->
    <div id="sidebar">
        <div class="inner">
            <!-- Logo -->
            <section class="alt" id="sidebar-header">
                <img src="images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id="sidebar-title">PetLink</p>
            </section>

            <!-- Perfil -->
            <section class="perfil">
                <div class="mini-posts">
                    <article>
                        <img src="images/aa.png" alt="" id="image-perfil">
                        <h2 id="usuario">Administrador</h2>
                    </article>
                </div>
            </section>

            <!-- Menu -->
            <nav id="menu">
                <header class="major">
                    <h2>Menu</h2>
                </header>
                <ul>
                    <li><a href="dashboard.html">Dashboard</a></li>
                    <li>
                        <span class="opener">Gestión de Usuarios</span>
                        <ul>
                            <li><a href="gestion_usuarios.html">Formulario Coordinador Zonal</a></li>
                            <li><a href="lista_usuarios.html">Lista de Usuarios</a></li>
                            <li><a href="lista_albergue.html">Lista de Albergues</a></li>
                            <li><a href="lista_coordinador.html">Lista de Coordinadores zonales</a></li>
                            <li><a href="solicitud_albergue.html">Solicitudes de Albergue</a></li>
                        </ul>
                    </li>
                    <li><a href="historial.html">Gestión de Eventos</a></li>
                </ul>
            </nav>

            <!-- Logout -->
            <nav id="logout">
                <a href="../bienvenidos.html" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>

        </div>
    </div>

</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>


<!-- Modal para aceptar publicación -->
<div class="modal modal-found">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>¿Estás seguro que quieres aceptar la eliminación del albergue?</p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big accept-found">Aceptar</a></li>
            <li><a href="#" class="button big cancel-found">Cancelar</a></li>
        </ul>
    </div>
</div>

<!-- Modal para rechazar publicación -->
<div class="modal modal-update">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>¿Estás seguro que quieres aceptar la eliminación del coordinador zonal?</p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big accept-update">Aceptar</a></li>
            <li><a href="#" class="button big cancel-update">Cancelar</a></li>
        </ul>
    </div>
</div>


<!-- JavaScript para abrir y cerrar los modales -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Función para abrir un modal y verificar en consola
        function openModal(modal) {
            modal.classList.add('show');
            console.log(`Modal ${modal.className} abierto`);
        }

        // Función para cerrar un modal y verificar en consola
        function closeModal(modal) {
            modal.classList.remove('show');
            console.log(`Modal ${modal.className} cerrado`);
        }

        // Configuración del Modal Aceptar
        const modalFound = document.querySelector('.modal-found');
        const openModalFoundButtons = document.querySelectorAll('.open-modal-found');
        const closeModalFoundButton = modalFound.querySelector('.close-btn');
        const acceptFoundButtons = modalFound.querySelectorAll('.accept-found');
        const cancelFoundButtons = modalFound.querySelectorAll('.cancel-found');

        openModalFoundButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                openModal(modalFound);
            });
        });

        closeModalFoundButton.addEventListener('click', function() {
            closeModal(modalFound);
        });

        cancelFoundButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                closeModal(modalFound);
            });
        });

        acceptFoundButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                closeModal(modalFound);
            });
        });

        // Configuración del Modal Rechazar
        const modalUpdate = document.querySelector('.modal-update');
        const openModalUpdateButtons = document.querySelectorAll('.open-modal-update');
        const closeModalUpdateButton = modalUpdate.querySelector('.close-btn');
        const acceptUpdateButtons = modalUpdate.querySelectorAll('.accept-update');
        const cancelUpdateButtons = modalUpdate.querySelectorAll('.cancel-update');

        openModalUpdateButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                openModal(modalUpdate);
            });
        });

        closeModalUpdateButton.addEventListener('click', function() {
            closeModal(modalUpdate);
        });

        cancelUpdateButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                closeModal(modalUpdate);
            });
        });

        acceptUpdateButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                closeModal(modalUpdate);
            });
        });
    });
</script>

</body>
</html>

