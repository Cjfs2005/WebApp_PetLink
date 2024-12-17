<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%
    ArrayList<LugarEvento> lugares = (ArrayList<LugarEvento>) request.getAttribute("lugares");
    Usuario administrador = (Usuario) session.getAttribute("datosUsuario");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/administrador/images/favicon.png" type="image/x-icon">

    <!--Agregar-->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
    <!--Fin Agregar-->

</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/administrador/images/historial_12.png" class="icons">
                <h1 class="logo"><strong>Historial de lugares para eventos</strong></h1>

            </header>

            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="<%=request.getContextPath()%>/ListaLugaresAdminServlet?action=crear" class="button primary big" style="float: left; margin: 0;">
                            Creación de lugar
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> Tabla que recopila la información de los eventos creados.</p>

                    <div class="table-responsive">
                        <table table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Nombre del lugar</th>
                                <th>Dirección</th>
                                <th>Aforo máximo</th>
                                <th>Distrito</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (LugarEvento lugar: lugares){%>
                            <tr>
                                <td><%=lugar.getNombre_lugar_evento()%></td>
                                <td><%= lugar.getDireccion_lugar_evento()%></td>
                                <td><%=lugar.getAforo_maximo()%> personas</td>
                                <td><%=lugar.getDistrito().getNombre_distrito()%></td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                    sLengthMenu: "Mostrar _MENU_ registros",
                                    sZeroRecords: "No se encontraron resultados",
                                    sEmptyTable: "No se encontraron lugares creados para los eventos",
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
        <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
    </jsp:include>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>
</body>
</html>