<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
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

    <!-- Bootstrap y DataTables -->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">

    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">
    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/albergue/images/donaciones.png" class="icons">
                <h1 class="logo" style="display: inline-block;"><strong>DONACIONES DE PRODUCTOS</strong></h1>

                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <% if (albergue.getFoto_perfil() != null) {%>
                    <span class="ocultar"><%=nombreUsuario%></span>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } else {%>
                    <span class="ocultar"><%=nombreUsuario%></span>
                    <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Botón de Publicar Aviso -->
            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="ListaSolicitudesDonacionProductos?action=crear" class="button primary big" style="float: left; margin: 0;">
                            Publicar aviso
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Enlaces de navegación -->
            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="ListaSolicitudesDonacionEconomica?action=listar">Colectas de fondos</a>
                <a href="ListaSolicitudesDonacionProductos?action=listar"><strong>Colectas de productos</strong></a>
            </section>

            <!-- Lógica para mostrar la tabla o los detalles -->
            <%
                SolicitudDonacionProductos solicitudDetalles = (SolicitudDonacionProductos) request.getAttribute("solicitud");
                if (solicitudDetalles != null) {
            %>

            <!-- Sección para los detalles de la solicitud -->
            <section class="banner">
                <h2>Detalles de la Donación de Productos</h2>
                <p><strong>Descripción:</strong> <%= solicitudDetalles.getDescripcionDonaciones() %></p>
                <p><strong>Fecha de Registro:</strong> <%= solicitudDetalles.getFechaHoraRegistro() %></p>
                <p><strong>Estado:</strong> <%= solicitudDetalles.getEstado().getNombre_estado() %></p>
                <p><strong>Activa:</strong> <%= solicitudDetalles.isEsSolicitudActiva() ? "Sí" : "No" %></p>
                <a href="ListaSolicitudesDonacionProductos?action=eliminar&id=<%= solicitudDetalles.getIdSolicitudDonacionProductos() %>"
                   class="icon fas fa-trash-alt" title="Eliminar donación"
                   onclick="return confirm('¿Está seguro de que desea eliminar esta solicitud?');">
                    <span class="label">Eliminar</span>
                </a>




            </section>

            <%
            } else {
            %>

            <!-- Tabla de Solicitudes de Donación -->
            <section class="banner">
                <section class="content">
                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Fecha</th>
                                <th>Motivo</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                List<SolicitudDonacionProductos> listaSolicitudes = (List<SolicitudDonacionProductos>) request.getAttribute("solicitudes");
                                if (listaSolicitudes != null) {
                                    for (SolicitudDonacionProductos solicitud : listaSolicitudes) {
                            %>
                            <tr>
                                <td><%= solicitud.getFechaHoraRegistro() %></td>
                                <td><%= solicitud.getDescripcionDonaciones() %></td>
                                <td style="text-transform: uppercase;"><%= solicitud.getEstado().getNombre_estado() %></td>
                                <td>
                                    <ul class="icons">
                                        <!-- Enlace para Ver Detalles -->
                                        <li><a href="ListaSolicitudesDonacionProductos?action=verDetalles&id=<%= solicitud.getIdSolicitudDonacionProductos() %>" class="icon fas fa-eye" title="Ver detalles"><span class="label">Ver</span></a></li>
                                        <!-- Enlace para Eliminar -->
                                        <li><a href="ListaSolicitudesDonacionProductos?action=eliminar&id=<%= solicitud.getIdSolicitudDonacionProductos() %>"
                                               class="icon fas fa-trash-alt" title="Eliminar donación"
                                               onclick="return confirm('¿Está seguro de que desea eliminar esta solicitud?');">
                                            <span class="label">Eliminar</span></a></li>
                                    </ul>
                                </td>

                            </tr>
                            <%
                                    }
                                }
                            %>

                            </tbody>
                        </table>

                        <!-- Inicialización de DataTable -->
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                    sLengthMenu: "Mostrar _MENU_ registros",
                                    sZeroRecords: "No se encontraron resultados",
                                    sEmptyTable: "Ningún dato disponible en esta tabla",
                                    sInfo: "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                                    sInfoEmpty: "Mostrando registros del 0 al 0 de un total de 0 registros",
                                    sInfoFiltered: "(filtrado de un total de _MAX_ registros)",
                                    sLoadingRecords: "Cargando...",
                                }
                            });
                        </script>
                    </div>
                </section>
            </section>
            <% } %>
        </div>
    </div>

    <!-- Sidebar -->
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
