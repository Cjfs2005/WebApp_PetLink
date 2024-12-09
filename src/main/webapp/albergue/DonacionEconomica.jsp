<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="assets/css/main.css" />
    <link rel="stylesheet" href="assets/css/aditional.css">
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
                <img src="images/donaciones.png" class="icons">
                <h1 class="logo" style="display: inline-block;"><strong>DONACIONES ECONÓMICAS</strong></h1>

                <!-- Sección para el nombre y enlace al perfil -->
                <a href="perfil.html" class="user-profile">
                    <span class="ocultar">Huellitas PUCP</span>
                    <img src="images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
                </a>
            </header>

            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="ListaSolicitudesDonacionEconomica?action=mostrar" class="button primary big" style="float: left; margin: 0;">
                            Publicar aviso
                        </a>

                    </li>
                </ul>
            </section>

            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="ListaSolicitudesDonacionEconomica?action=listar"><strong>Colectas de fondos</strong></a>
                <a href="ListaSolicitudesDonacionProductos?action=listar">Colectas de productos</a>
            </section>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <div class="table-responsive">
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
                                // Recuperamos la lista de solicitudes de donación económica desde el servlet
                                List<SolicitudDonacionEconomica> solicitudes = (List<SolicitudDonacionEconomica>) request.getAttribute("solicitudes");
                                // Creamos un formateador para la fecha
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                                // Iteramos sobre las solicitudes y mostramos los datos en la tabla
                                for (SolicitudDonacionEconomica solicitud : solicitudes) {
                                    String fechaRegistro = "";
                                    String estado = solicitud.getEstado().getNombre_estado();

                                    // Si la fecha es de tipo LocalDateTime, la formateamos
                                    if (solicitud.getFecha_hora_registro() != null) {
                                        fechaRegistro = solicitud.getFecha_hora_registro().format(formatter);
                                    }
                            %>
                            <tr>
                                <td><%= fechaRegistro %></td>
                                <td><%= solicitud.getMotivo() %></td>
                                <td style="text-transform: uppercase;"><%= estado %></td>
                                <td>S/. <%= solicitud.getMonto_solicitado() %></td>
                                <td>
                                    <ul class="icons">
                                        <li>
                                            <a href="ListaSolicitudesDonacionEconomica?action=VerDetalles&id=<%= solicitud.getId_solicitud_donacion_economica() %>"
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
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example');
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
                        <img src="images/logo_huellitas.png" alt="" id="image-perfil">
                        <h2 id="usuario">HUELLITAS PUCP</h2>
                    </article>
                </div>
            </section>

            <!-- Menu -->
            <nav id="menu">
                <header class="major">
                    <h2>Menu</h2>
                </header>
                <ul>
                    <li><a href="perfil.html">Perfil</a></li>
                    <li>
                        <span class="opener">Publicaciones</span>
                        <ul>
                            <li><a href="EventoBenefico.html">Eventos benéficos</a></li>
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