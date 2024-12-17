<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>

<%
    List<PostulacionHogarTemporal> postulaciones = (List<PostulacionHogarTemporal>) request.getAttribute("postulaciones");
    Usuario coordinador = (Usuario) session.getAttribute("datosUsuario");
    String nombreCoordinador = coordinador.getNombres_coordinador();
    String apellidoCoordinador = coordinador.getApellidos_coordinador();
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink - Usuarios Baneados</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/coordinadorZonal/images/favicon.png" type="image/x-icon">

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">
<div id="wrapper">
    <!-- Main -->
    <div id="main">
        <div class="inner">
            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/user_ban.png" class="icons">
                <h1 class="logo"><strong>USUARIOS PARA BLOQUEAR </strong></h1>

            </header>

            <!-- Section Links -->
            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <!-- Link para "Banear usuarios" -->
                <a href="<%=request.getContextPath()%>/BaneosCoordi1Servlet?action=listas">
                    <strong>Banear usuarios</strong>
                </a>

                <!-- Link para "Baneos realizados" -->
                <a href="<%=request.getContextPath()%>/BaneosCoordi2Servlet?action=listar">
                    Baneos realizados
                </a>
            </section>

            <!-- Table Section -->
            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> Lista de usuarios que corresponden a su zona.
                        Usted puede banear un usuario si considera que ha cometido actos discriminatorios o no.</p>

                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Nombres y apellidos</th>
                                <th>Celular</th>
                                <th>N° de rechazos</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                for (PostulacionHogarTemporal postulacion : postulaciones) {
                            %>
                            <tr>
                                <td><%= postulacion.getUsuario_final().getNombres_usuario_final() %>
                                    <%= postulacion.getUsuario_final().getApellidos_usuario_final() %></td>
                                <td><%= postulacion.getCelular_usuario() %></td>
                                <td><%= postulacion.getCantidad_rechazos_consecutivos() %></td>
                                <td>
                                    <ul class="icons">
                                        <li>
                                            <a href="<%=request.getContextPath()%>/BaneosCoordi1Servlet?action=escribir&id=<%= postulacion.getUsuario_final().getId_usuario() %>"
                                               class="icon fas fa-times-circle" title="Rechazar postulación">
                                                <span class="label">Rechazar</span>
                                            </a>
                                        </li>
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
                                    sEmptyTable: "No hay usuarios",
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
    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= coordinador.getId_usuario() %>" />
        <jsp:param name="nombresCoordinador" value="<%= coordinador.getNombres_coordinador() %>" />
        <jsp:param name="apellidosCoordinador" value="<%= coordinador.getApellidos_coordinador() %>" />
        <jsp:param name="zonaCoordinador" value="<%= coordinador.getZona().getNombre_zona()%>" />
    </jsp:include>
</div>

<!-- Scripts -->

<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/main.js"></script>
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


