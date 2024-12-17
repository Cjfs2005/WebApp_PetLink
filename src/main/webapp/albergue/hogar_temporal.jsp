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
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<!DOCTYPE HTML>
<html lang="es">
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
                <h1 class="logo"><strong>HOGAR TEMPORAL</strong></h1>
                <%
                    Usuario albergue = (Usuario) request.getAttribute("usuario");
                    String fotoPerfilBase64 = "";
                    if (albergue != null && albergue.getFoto_perfil() != null) {
                        fotoPerfilBase64 = Base64 .getEncoder().encodeToString(albergue.getFoto_perfil());
                    }
                %>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <span class="ocultar"><%= albergue.getNombre_albergue() != null ? albergue.getNombre_albergue() : "Nombre no disponible" %></span>
                    <% if (!fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } else { %>
                    <img src="<%=request.getContextPath()%>/albergue/images/default_profile.png"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
                <a href="TemporalAlbergueServlet"><strong>Hogares disponibles</strong></a>
                <a href="TemporalAlbergueServlet?action=historial">Hogares solicitados</a>
            </section>

            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> En la siguiente tabla se muestran los hogares temporales disponibles. Puede ver más información o enviar una solicitud al hogar temporal.</p>

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
                                ArrayList<PostulacionHogarTemporal> postulacionesDisponibles = (ArrayList<PostulacionHogarTemporal>) request.getAttribute("postulacionesDisponibles");
                                for (PostulacionHogarTemporal postulacion : postulacionesDisponibles) {
                                    Usuario usuario = postulacion.getUsuario_final();
                            %>
                            <tr>
                                <td><%= usuario.getDni() %></td>
                                <td><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></td>
                                <td><%= usuario.getDistrito() != null ? usuario.getDistrito().getNombre_distrito() : "N/A" %></td>
                                <td><%= postulacion.getFecha_inicio_temporal() %></td>
                                <td><%= postulacion.getFecha_fin_temporal() %></td>
                                <td>
                                    <ul class="icons">
                                        <!-- Botón Ver más información -->
                                        <li>
                                            <a href="<%=request.getContextPath()%>/TemporalAlbergueServlet?action=ver&id_postulacion=<%= postulacion.getId_postulacion_hogar_temporal() %>"
                                               class="icon fas fa-eye"
                                               title="Ver más información">
                                                <span class="label">Ver</span>
                                            </a>
                                        </li>
                                        <!-- Botón Enviar solicitud -->
                                        <li>
                                            <a href="<%=request.getContextPath()%>/TemporalAlbergueServlet?action=solicitar&id_postulacion=<%= postulacion.getId_postulacion_hogar_temporal() %>"
                                               class="icon fas fa-envelope"
                                               title="Enviar solicitud">
                                                <span class="label">Enviar</span>
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
                    </div>
                </div>
            </section>
        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>

<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
</body>
</html>

