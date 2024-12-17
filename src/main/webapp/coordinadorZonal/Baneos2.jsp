<%--
  Created by IntelliJ IDEA.
  User: aleJo
  Date: 16/12/2024
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.BaneoHogarTemporal" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>

<%
    // Recuperar lista de baneos desde el servlet
    List<BaneoHogarTemporal> baneosAutomaticos = (List<BaneoHogarTemporal>) request.getAttribute("baneosAutomaticos");
    List<BaneoHogarTemporal> baneosManuales = (List<BaneoHogarTemporal>) request.getAttribute("baneosManuales");

    System.out.println("Baneos automáticos: " + (baneosAutomaticos != null ? baneosAutomaticos.size() : "null"));
    System.out.println("Baneos manuales: " + (baneosManuales != null ? baneosManuales.size() : "null"));
    Usuario coordinador = (Usuario) session.getAttribute("datosUsuario");
    String nombreCoordinador = coordinador.getNombres_coordinador();
    String apellidoCoordinador = coordinador.getApellidos_coordinador();
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>Usuarios Baneados</title>
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
    <div id="main">
        <div class="inner">
            <header id="header">
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/user_ban.png" class="icons">
                <h1 class="logo"><strong>USUARIOS BLOQUEADOS</strong></h1>
            </header>
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
            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> En la siguiente tabla se muestran los usuarios que han sido bloqueados, los cuales ya no pueden ser solicitados por los albergues como hogar temporal.</p>
                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Nombres y apellidos</th>
                                <th>Bloqueado por</th>
                                <th>Fecha</th>
                                <th>Motivo</th>
                            </tr>
                            </thead>
                            <%@ page import="java.time.format.DateTimeFormatter" %>
                            <%
                                // Formateador de fecha
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            %>
                            <tbody>
                            <!-- Baneos Automáticos -->
                            <!-- Baneos Automáticos -->
                            <%
                                if (baneosAutomaticos != null) {
                                    for (BaneoHogarTemporal baneo : baneosAutomaticos) {
                            %>
                            <tr>
                                <td><%= baneo.getUsuario_final().getNombres_usuario_final() %> <%= baneo.getUsuario_final().getApellidos_usuario_final() %></td>
                                <td>Sistema</td>
                                <td><%= baneo.getFecha_hora_registro() != null ? baneo.getFecha_hora_registro().toLocalDate().format(formatter) : "" %></td>
                                <td><%= baneo.getMotivo() %></td>
                            </tr>
                            <%
                                    }
                                }
                            %>


                            <!-- Baneos Manuales -->
                            <!-- Baneos Manuales -->
                            <!--pipipi-->
                            <%
                                if (baneosManuales != null) {
                                    for (BaneoHogarTemporal baneo : baneosManuales) {
                            %>
                            <tr>
                                <td><%= baneo.getUsuario_final().getNombres_usuario_final() %> <%= baneo.getUsuario_final().getApellidos_usuario_final() %></td>
                                <td>Coordinador zonal</td>
                                <td><%= baneo.getFecha_hora_registro() != null ? baneo.getFecha_hora_registro().toLocalDate().format(formatter) : "" %></td>
                                <td><%= baneo.getMotivo() %></td>
                            </tr>
                            <%
                                    }
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
                                    sEmptyTable: "No hay solicitudes de eventos",
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

<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/main.js"></script>
</body>
</html>