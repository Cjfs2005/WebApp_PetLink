<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="com.example.webapp_petlink.daos.DonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%
    DonacionProductos dao = new DonacionProductos();
    List<PuntoAcopio> puntosAcopio = dao.obtenerPuntosAcopioPorAlbergue(6); // ID del albergue
    if (puntosAcopio == null || puntosAcopio.isEmpty()) {
%>
    <p style="color: red;">No hay puntos de acopio disponibles. Verifica la configuración.</p>
<%
    }
%>
<%
    SolicitudDonacionProductos solicitud = (SolicitudDonacionProductos) request.getAttribute("solicitud");
    boolean esEdicion = (solicitud != null);
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Formulario para donación de productos</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="assets/css/main.css" />
    <link rel="stylesheet" href="assets/css/aditional.css">
    <link rel="stylesheet" href="assets/css/popup-window.css">
    <link rel="icon" href="images/favicon.png" type="image/x-icon">
    <style>
        #charCount {
            font-size: 12px;
            color: #888;
            float: right;
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
                <h1 class="logo"><strong>Donación de productos</strong></h1>
                <a href="perfil.html" class="user-profile">
                    <span class="ocultar">Huellitas PUCP</span>
                    <img src="images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/form.png" class="icons">
                        <h2>Formulario para las donaciones de productos</h2>
                    </header>
                    <p><strong>Descripción:</strong> Complete los detalles sobre la donación.</p>

                    <form action="ListaSolicitudesDonacionProductos?action=publicar" method="POST">
                        <div class="row gtr-uniform">

                            <!-- Fecha programada para recepción -->
                            <div class="col-6 col-12-small">
                                <label for="fechaRecepcion" class="input-label">Fecha programada para recepción</label>
                                <input type="date" id="fechaRecepcion" name="fechaRecepcion" min="" required />
                            </div>

                            <!-- Puntos de acopio -->
                            <div class="col-6 col-12-small">
                                <label for="puntosAcopio" class="input-label">Puntos de Acopio</label>
                                <select id="puntosAcopio" name="puntoAcopio" style="appearance: none;" required>
                                    <option value="" disabled selected>Seleccione un punto de acopio</option>
                                    <%
                                        for (PuntoAcopio punto : puntosAcopio) {
                                    %>
                                    <option value="<%= punto.getId_punto_acopio() %>"><%= punto.getDireccion_punto_acopio() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>

                            <!-- Hora de inicio de recepción -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaInicioEvento" class="input-label">Hora de inicio de recepción</label>
                                <input type="time" id="horaInicioEvento" name="horaInicioEvento" required style="appearance: none;
                                border-radius: 0.375em;
                                border: none;
                                border: solid 1px rgba(210, 215, 217, 0.75);
                                color: inherit;
                                display: block;
                                outline: 0;
                                padding: 0 1em;
                                text-decoration: none;
                                width: 100%;
                                background: #ffffff;
                                padding-top: 7.5px;
                                padding-bottom: 7.5px;" />
                            </div>

                            <!-- Hora final de recepción -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaFinEvento" class="input-label">Hora final de recepción</label>
                                <input type="time" id="horaFinEvento" name="horaFinEvento" required style="appearance: none;
                                border-radius: 0.375em;
                                border: none;
                                border: solid 1px rgba(210, 215, 217, 0.75);
                                color: inherit;
                                display: block;
                                outline: 0;
                                padding: 0 1em;
                                text-decoration: none;
                                width: 100%;
                                background: #ffffff;
                                padding-top: 7.5px;
                                padding-bottom: 7.5px;" />
                            </div>

                            <!-- Productos solicitados -->
                            <div class="col-12">
                                <label for="descripcion" class="input-label">Productos solicitados</label>
                                <textarea id="descripcion" name="descripcion" class="text-area" maxlength="500" required></textarea>
                                <span id="charCount">0/500</span>
                            </div>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><button type="submit" class="button primary big">Publicar</button></li>
                                    <li><a href="javascript:history.back()" class="button big">Cancelar</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>
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
                <a href="bienvenidos.html" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>

        </div>
    </div>
    <script>
        const fechaInicio = document.getElementById('fechaRecepcion');
        document.addEventListener('DOMContentLoaded', function () {
            const hoy = new Date().toISOString().split("T")[0];
            fechaInicio.setAttribute("min", hoy);
        });
    </script>

</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
</body>
</html>
