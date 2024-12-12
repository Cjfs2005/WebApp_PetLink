<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    ArrayList<PublicacionEventoBenefico> listaEventos = (ArrayList<PublicacionEventoBenefico>) request.getAttribute("listaEventos");
    Usuario coordinador = (Usuario) session.getAttribute("datosUsuario");
    String nombreCoordinador = coordinador.getNombres_coordinador();
    String apellidoCoordinador = coordinador.getApellidos_coordinador();
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/coordinadorZonal/images/favicon.png" type="image/x-icon">

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
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/gestion_solicitudes.png" class="icons">
                <h1 class="logo"><strong>GESTIÓN DE EVENTOS</strong></h1>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> En la siguiente tabla se muestran todas las solicitudes de publicación de eventos de los albergues que pertenecen a su zona.
                        Vea la publicación y acepte o rechaze según lo vea conveniente.</p>

                    <div class="table-responsive">
                        <table table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Nombre del Albergue</th>
                                <th>Nombre del evento</th>
                                <th>Lugar del evento</th>
                                <th>Fecha</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                for (PublicacionEventoBenefico evento : listaEventos){
                            %>
                            <tr>
                                <td><%=evento.getUsuarioAlbergue().getNombre_albergue()%></td>
                                <td><%=evento.getNombreEvento()%></td>
                                <td><%=evento.getLugarEvento().getNombre_lugar_evento()%> - <%=evento.getLugarEvento().getDistrito().getNombre_distrito()%></td>
                                <%
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String fechaFormateada = formatter.format(evento.getFechaHoraInicioEvento());
                                %>
                                <td><%=fechaFormateada%></td>
                                <td>
                                    <ul class="icons">
                                        <li><a href="#" class="icon fas fa-check-circle open-modal-found openModal" title="Aceptar evento" data-id="<%=evento.getIdPublicacionEventoBenefico()%>"><span class="label">Aceptar</span></a></li> <!-- fa-check para aceptar -->
                                        <li><a href="#" class="icon fas fa-times-circle open-modal-update openModal1" title="Rechazar evento" data-id="<%=evento.getIdPublicacionEventoBenefico()%>"><span class="label">Rechazar</span></a></li> <!-- fa-times para rechazar -->
                                        <li><a href="<%=request.getContextPath()%>/GestionCoordinadorServlet?action=mostrar&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="open-modal-info-trufa icon fas fa-eye" title="Ver más información"><span class="label">Ver detalles</span></a></li> <!-- fa-eye para ver -->
                                    </ul>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                    sLengthMenu: "Mostrar _MENU_ registros",
                                    sZeroRecords: "No se encontraron resultados",
                                    sEmptyTable: "No hay solicitudes de eventos",
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
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id ="sidebar-title">PetLink</p>
            </section>

            <!-- Perfil -->

            <section class="perfil">

                <div class="mini-posts">
                    <article>
                        <img src="images/foto_coordinador.png" alt="" id="image-perfil">
                        <h2 id="usuario">Marco Lang<br>Zona Este</h2>
                    </article>
                </div>

            </section>

            <!-- Menu -->
            <nav id="menu">
                <header class="major">
                    <h2>Menu</h2>
                </header>
                <ul>
                    <li>
                        <span class="opener">SOLICITUD DE PUBLICACION DE MASCOTAS PERDIDAS</span>
                        <ul>
                            <li><a href="solicitudes_usuario_mascotas_perdidas.html">SOLICITUD DEL USUARIO</a></li>
                            <li><a href="solicitudes_duenio_mascotas_perdidas.html">SOLICITUD DEL DUEÑO</a></li>
                        </ul>
                    </li>
                    <li>
                        <span class="opener">PUBLICACIONES DE MASCOTAS PERDIDAS</span>
                        <ul>
                            <li><a href="mascotas_perdidas_publicaciones.html">Mascotas Perdidas publicadas por Usuarios</a></li>
                            <li><a href="mascotas_perdidas_publicaciones_dueños.html">Mascotas Perdidas publicadas por Dueños</a></li>
                        </ul>
                    </li>
                    <li><a href="postulantes.html">Postulantes a hogar temporal</a></li>
                    <li>
                        <span class="opener">Baneo de usuarios</span>
                        <ul>
                            <li><a href="banear_usuarios.html">Banear usuarios</a></li>
                            <li><a href="baneos_realizados.html">Baneos realizados</a></li>
                        </ul>
                    </li>
                    <li>
                        <span class="opener">GESTIÓN DE PUBLICACIONES</span>
                        <ul>
                            <li><a href="<%=request.getContextPath()%>/GestionCoordinadorServlet">Gestión de eventos</a></li>
                            <li><a href="mascotas_perdidas_publicaciones_dueños.html">Gestión de adopciones</a></li>
                        </ul>
                    </li>

                </ul>
            </nav>

            <!-- Logout -->

            <nav id="logout">
                <a href="#" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>

        </div>
    </div>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/main.js"></script>

<div class="modal modal-found">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>¿Estás seguro que quieres aceptar la publicación del albergue?</p>
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
        <p>¿Estás seguro que quieres rechazar la publicación?
            No se le mostrará a los usuarios y se le notificará al albergue.</p>
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
                const baseUrl = "<%=request.getContextPath()%>/GestionCoordinadorServlet?action=rechazar";
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
                const baseUrl = "<%=request.getContextPath()%>/GestionCoordinadorServlet?action=aceptar";
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
