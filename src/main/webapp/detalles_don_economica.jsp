<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page import="com.example.webapp_petlink.beans.RegistroDonacionEconomica" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html>
<head>
    <title>PetLink - Detalles de la Donación Económica</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <!-- Estilos -->
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="assets/css/main.css" />
    <link rel="stylesheet" href="assets/css/aditional.css">
    <link rel="stylesheet" href="assets/css/popup-window.css">
    <link rel="icon" href="images/favicon.png" type="image/x-icon">

    <!-- Scripts -->
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
                <h1 class="logo"><strong>Detalles de la Donación Económica</strong></h1>
                <a href="perfil.html" class="user-profile">
                    <span class="ocultar">Huellitas PUCP</span>
                    <img src="images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/imagen1Donaciones.png" class="icons">
                        <h2>Huellitas PUCP: Colecta de Dinero</h2>
                    </header>

                    <!-- Información de la Solicitud -->
                    <p><strong>Motivo:</strong> <%= ((SolicitudDonacionEconomica) request.getAttribute("solicitud")).getMotivo() %></p>
                    <p><strong>Monto Solicitado:</strong> S/. <%= ((SolicitudDonacionEconomica) request.getAttribute("solicitud")).getMonto_solicitado() %></p>

                    <!-- Código QR -->
                    <h3>Código QR para Donaciones</h3>
                    <div class="contenedor-imagenes">
                        <%
                            String imagenQR = (String) request.getAttribute("imagenQR");
                            if (imagenQR != null) {
                        %>
                        <img src="data:image/jpeg;base64,<%= imagenQR %>" alt="Código QR para Donaciones" />
                        <%
                        } else {
                        %>
                        <p>No hay código QR disponible.</p>
                        <%
                            }
                        %>
                    </div>

                    <!-- Tabla de Usuarios Donantes -->
                    <h3>Lista de Usuarios Donantes</h3>
                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Nombres y Apellidos</th>
                                <th>Fecha de Donación</th>
                                <th>Monto Donado</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                List<RegistroDonacionEconomica> registrosDonacion = (List<RegistroDonacionEconomica>) request.getAttribute("registrosDonacion");
                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                if (registrosDonacion != null && !registrosDonacion.isEmpty()) {
                                    for (RegistroDonacionEconomica registro : registrosDonacion) {
                            %>
                            <tr>
                                <td><%= registro.getUsuarioFinal().getNombres_usuario_final() %> <%= registro.getUsuarioFinal().getApellidos_usuario_final() %></td>
                                <td><%= registro.getFechaHoraRegistro().format(dateFormatter) %></td>
                                <td>S/. <%= registro.getMontoDonacion() %></td>
                            </tr>
                            <%
                                }
                            } else {
                            %>

                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example');
                        </script>
                    </div>

                    <!-- Botones de Acción -->
                    <div class="row gtr-uniform">
                        <div class="col-12">
                            <ul class="actions form-buttons">
                                <li><a href="<%= request.getContextPath()%>/ListaSolicitudesDonacionEconomica?action=modificar&id=<%= ((SolicitudDonacionEconomica) request.getAttribute("solicitud")).getId_solicitud_donacion_economica() %> " class="button primary big">Modificar</a></li>
                                <li><a href="<%= request.getContextPath() %>/ListaSolicitudesDonacionEconomica?action=eliminar&id=<%= ((SolicitudDonacionEconomica) request.getAttribute("solicitud")).getId_solicitud_donacion_economica() %>" class="button big" onclick="return confirm('¿Está seguro de eliminar esta solicitud?');">Eliminar</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <!-- Sidebar -->
    <div id="sidebar">
        <div class="inner">
            <section class="alt" id="sidebar-header">
                <img src="images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id="sidebar-title">PetLink</p>
            </section>

            <!-- Perfil -->
            <section class="perfil">
                <div class="mini-posts">
                    <article>
                        <img src="images/logo_huellitas.png" alt="" id="image-perfil">
                        <h2 id="usuario">HUELLITAS PUCP</h2>
                    </article>
                </div>
            </section>

            <!-- Menú -->
            <nav id="menu">
                <header class="major">
                    <h2>Menú</h2>
                </header>
                <ul>
                    <li><a href="perfil.html">Perfil</a></li>
                    <li>
                        <span class="opener">Publicaciones</span>
                        <ul>
                            <li><a href="EventoBenefico.html">Eventos Benéficos</a></li>
                            <li><a href="adopciones.html">Adopciones</a></li>
                            <li><a href="donaciones.html">Donaciones</a></li>
                        </ul>
                    </li>
                    <li><a href="hogar_temporal.html">Hogar Temporal</a></li>
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
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>

</body>
</html>
