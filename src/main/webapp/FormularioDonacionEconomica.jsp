<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Formulario de Solicitud de Donaciones Económicas</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="assets/css/main.css" />
    <link rel="stylesheet" href="assets/css/aditional.css">
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
                <h1 class="logo"><strong>Formulario de Solicitud de Donaciones Económicas</strong></h1>
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
                        <h2>Detalles de la Donación</h2>
                    </header>

                    <!-- Formulario -->
                    <form action="<%= request.getContextPath()%>/ListaSolicitudesDonacionEconomica?action=crear" method="post">
                        <!-- Campo oculto para el ID del estado -->
                        <input type="hidden" name="idEstado" value="1" />

                        <!-- Campo oculto para el ID del usuario albergue -->
                        <input type="hidden" name="idUsuarioAlbergue" value="6" />

                        <div class="row gtr-uniform">
                            <!-- Monto Requerido -->
                            <div class="col-12">
                                <label for="monto" class="input-label">Monto requerido</label>
                                <input type="text" id="monto" name="monto"
                                       value=""
                                       placeholder="Ingrese el monto"
                                       required />
                            </div>

                            <!-- Motivo -->
                            <div class="col-12">
                                <label for="motivo" class="input-label">Motivo</label>
                                <textarea id="motivo" name="motivo" maxlength="300" class="text-area"
                                          placeholder="Explique el motivo de la solicitud"
                                          required></textarea>
                            </div>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><button type="submit" class="button primary big">Publicar</button></li>
                                    <li><a href="<%= request.getContextPath()%>/ListaSolicitudesDonacionEconomica?action=listar" class="button big">Cancelar</a></li>
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
</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>

</body>
</html>
