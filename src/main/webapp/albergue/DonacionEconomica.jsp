<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Validar si el usuario está logueado
    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    if (albergue == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink - Donaciones Económicas</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="icon" href="images/favicon.png" type="image/x-icon">

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
                <img src="<%=request.getContextPath()%>/albergue/images/donaciones.png" class="icons">

                <h1 class="logo" style="display: inline-block;"><strong>DONACIONES ECONÓMICAS</strong></h1>
                <!-- Sección para el nombre y enlace al perfil -->
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

            <!-- Botón Publicar -->
            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="ListaSolicitudesDonacionEconomica?action=mostrar" class="button primary big" style="float: left; margin: 0;">
                            Publicar aviso
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Enlaces -->
            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="ListaSolicitudesDonacionEconomica?action=listar"><strong>Colectas de fondos</strong></a>
                <a href="ListaSolicitudesDonacionProductos?action=listar">Colectas de productos</a>
            </section>

            <!-- Tabla -->
            <section class="banner">
                <div class="content">
                    <div class="table-responsive">
                        <%
                            List<SolicitudDonacionEconomica> solicitudes = (List<SolicitudDonacionEconomica>) request.getAttribute("solicitudes");
                            if (solicitudes == null || solicitudes.isEmpty()) {
                        %>
                        <p style="text-align: center; font-size: 18px; color: #555;">No hay solicitudes de donación económica en este momento.</p>
                        <% } else { %>
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Fecha</th>
                                <th>Motivo</th>
                                <th>Estado</th>
                                <th>Monto solicitado</th>
                                <th>Detalles</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                for (SolicitudDonacionEconomica solicitud : solicitudes) {
                                    String fechaRegistro = "";
                                    if (solicitud.getFecha_hora_registro() != null) {
                                        fechaRegistro = solicitud.getFecha_hora_registro().format(formatter);
                                    }
                            %>
                            <tr>
                                <td><%= fechaRegistro %></td>
                                <td><%= solicitud.getMotivo() %></td>
                                <td style="text-transform: uppercase;"><%= solicitud.getEstado().getNombre_estado() %></td>
                                <td>S/. <%= solicitud.getMonto_solicitado() %></td>
                                <td>
                                    <ul class="icons">
                                        <li>
                                            <a href="ListaSolicitudesDonacionEconomica?action=verDetalles&id=<%= solicitud.getId_solicitud_donacion_economica() %>"
                                               class="icon fas fa-eye"
                                               title="Ver detalles">
                                                <span class="label">Ver</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="ListaSolicitudesDonacionEconomica?action=eliminar&id=<%= solicitud.getId_solicitud_donacion_economica() %>"
                                               class="icon fas fa-trash-alt"
                                               title="Eliminar solicitud"
                                               onclick="return confirm('¿Está seguro de que desea eliminar esta solicitud?');">
                                                <span class="label">Eliminar</span>
                                            </a>
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",
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
                        <% } %>
                    </div>
                </div>
            </section>
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
