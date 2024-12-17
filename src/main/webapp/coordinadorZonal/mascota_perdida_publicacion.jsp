<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionMascotaPerdida" %>
<%
    List<PublicacionMascotaPerdida> publicaciones = (List<PublicacionMascotaPerdida>) request.getAttribute("publicaciones");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Mascotas Perdidas</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/aditional.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.min.css"/>
</head>
<body class="is-preload">

<div id="wrapper">

    <div id="main">
        <div class="inner">

            <header id="header">
                <img src="${pageContext.request.contextPath}/coordinadorZonal/assets/images/logo.png" class="icons">
                <h1 class="logo"><strong>Mascotas Perdidas</strong></h1>
            </header>

            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> Tabla con las publicaciones de mascotas perdidas.</p>

                    <div class="table-responsive">
                        <table id="publicacionesTabla" class="table table-striped">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Lugar de Pérdida</th>
                                <th>Fecha de Pérdida</th>
                                <th>Contacto</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (publicaciones != null) {
                                for (PublicacionMascotaPerdida publicacion : publicaciones) { %>
                            <tr>
                                <td><%= publicacion.getIdPublicacionMascotaPerdida() %></td>
                                <td><%= publicacion.getNombre() %></td>
                                <td><%= publicacion.getLugarPerdida() %></td>
                                <td><%= publicacion.getFechaPerdida() %></td>
                                <td><%= publicacion.getNombreContacto() + " (" + publicacion.getCelularContacto() + ")" %></td>
                                <td>
                                    <ul class="icons">
                                        <li><a href="#" class="icon fas fa-eye open-modal-info" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>"><span class="label">Ver</span></a></li>
                                        <li><a href="#" class="icon fas fa-edit open-modal-update" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>"><span class="label">Actualizar</span></a></li>
                                    </ul>
                                </td>
                            </tr>
                            <%      }
                            } else { %>
                            <tr>
                                <td colspan="6" class="text-center">No hay publicaciones disponibles.</td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <div id="sidebar">
        <div class="inner">
            <section class="alt" id="sidebar-header">
                <img src="${pageContext.request.contextPath}/coordinadorZonal/assets/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id="sidebar-title">PetLink</p>
            </section>
            <nav id="menu">
                <header class="major">
                    <h2>Menú</h2>
                </header>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/PerfilUsuarioServlet">Mi Perfil</a></li>
                    <li><a href="${pageContext.request.contextPath}/mascotas_perdidas.jsp">Mascotas Perdidas</a></li>
                </ul>
            </nav>
        </div>
    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/2.1.8/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.min.js"></script>
<script>
    $(document).ready(function () {
        $('#publicacionesTabla').DataTable();

        // Manejo de modales para "Ver información"
        $('.open-modal-info').on('click', function () {
            const id = $(this).data('id');
            $.get("${pageContext.request.contextPath}/MascotaPerdidaPublicacionServlet", { accion: "detalles", idPublicacion: id }, function (data) {
                alert("Detalles: " + JSON.stringify(data));
            });
        });

        // Manejo de modales para "Actualizar publicación"
        $('.open-modal-update').on('click', function () {
            const id = $(this).data('id');
            alert("Abrir modal para actualizar publicación con ID: " + id);
        });
    });
</script>

</body>
</html>
