<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <!-- Bootstrap y DataTables -->
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
                    <img src="images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <!-- Barra de navegación -->
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="container-fluid">
                    <a class="navbar-brand" href="#">PetLink</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav">
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="ListaSolicitudesDonacionEconomica.jsp">Donaciones Económicas</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="ListaSolicitudesDonacionProductos.jsp">Donaciones de Productos</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="EventoBenefico.html">Eventos Benéficos</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="adopciones.html">Adopciones</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="perfil.html">Perfil</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="logout.html">Cerrar Sesión</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Sección de botones para acciones -->
            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="form_don_economica.html" class="button primary big" style="float: left; margin: 0;">
                            Publicar aviso
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Sección para las opciones de colectas -->
            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="#"><strong>Colectas de fondos</strong></a>
                <a href="donaciones_productos.html">Colectas de productos</a>
            </section>

            <!-- Tabla de Solicitudes de Donación Económica -->
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
                                // Recuperamos la lista de solicitudes de donación económica del servlet
                                List<SolicitudDonacionEconomica> listaSolicitudes = (List<SolicitudDonacionEconomica>) request.getAttribute("solicitudes");
                                // Creamos un formateador para la fecha
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                // Iteramos sobre las solicitudes y mostramos los datos en la tabla
                                for (SolicitudDonacionEconomica solicitud : listaSolicitudes) {
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
                                        <li><a href="detalles_don_economica.html" class="icon fas fa-eye" title="Ver detalles"><span class="label">Ver</span></a></li>
                                        <li><a href="#" class="icon fas fa-trash-alt" title="Eliminar donación"><span class="label">Eliminar</span></a></li>
                                    </ul>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example'); // Inicializamos DataTable
                        </script>
                    </div>
                </div>
            </section>
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
