<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 13/11/2024
  Time: 12:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink - Hogares Temporales Disponibles</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">
<div id="wrapper">
    <div id="main">
        <div class="inner">
            <header id="header">
                <img src="<%=request.getContextPath()%>/albergue/images/logo_hogarTemporal.png" class="icons">
                <h1 class="logo"><strong>HOGARES TEMPORALES DISPONIBLES</strong></h1>
                <a href="perfil.html" class="user-profile">
                    <span class="ocultar">Huellitas PUCP</span>
                    <img src="<%=request.getContextPath()%>/albergue/images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> En la siguiente tabla se muestran los hogares temporales disponibles. Puede ver más información o enviar una solicitud al albergue.</p>

                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>DNI</th>
                                <th>Nombres y apellidos</th>
                                <th>Distrito</th>
                                <th>Fecha de inicio</th>
                                <th>Fecha fin</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                ArrayList<Usuario> hogaresDisponibles = (ArrayList<Usuario>) request.getAttribute("hogaresDisponibles");
                                for (Usuario hogar : hogaresDisponibles) {
                            %>
                            <tr>
                                <td><%= hogar.getDni() %></td>
                                <td><%= hogar.getNombres_usuario_final() %> <%= hogar.getApellidos_usuario_final() %></td>
                                <td><%= hogar.getDistrito().getNombre_distrito() %></td>
                                <td><%= hogar.getUltima_postulacion_hogar_temporal().getFecha_inicio_temporal() %></td>
                                <td><%= hogar.getUltima_postulacion_hogar_temporal().getFecha_fin_temporal() %></td>
                                <td>
                                    <ul class="icons">
                                        <li><a href="#" class="icon fas fa-eye" title="Ver más información"><span class="label">Ver</span></a></li>
                                        <li><a href="#" class="icon fas fa-envelope" title="Enviar solicitud"><span class="label">Enviar</span></a></li>
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
                    </div>
                </div>
            </section>
        </div>
    </div>

    <div id="sidebar">
        <div class="inner">
            <section class="alt" id="sidebar-header">
                <img src="<%=request.getContextPath()%>/albergue/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id ="sidebar-title">PetLink</p>
            </section>

            <section class="perfil">
                <div class="mini-posts">
                    <article>
                        <img src="<%=request.getContextPath()%>/albergue/images/logo_huellitas.png" alt="" id="image-perfil">
                        <h2 id="usuario">HUELLITAS PUCP</h2>
                    </article>
                </div>
            </section>

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
                    <li><a href="denuncias.html">Denuncias por maltrato animal</a></li>
                </ul>
            </nav>

            <nav id="logout">
                <a href="#" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>
        </div>
    </div>
</div>

<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
</body>
</html>

