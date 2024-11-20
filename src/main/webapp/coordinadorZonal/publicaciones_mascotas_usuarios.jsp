<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionMascotaPerdida" %>
<%
  // Obtener la lista de publicaciones desde el servlet
  List<PublicacionMascotaPerdida> publicaciones = (List<PublicacionMascotaPerdida>) request.getAttribute("publicaciones");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink - Publicaciones de Usuario</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/aditional.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/popup-window.css">
  <link rel="icon" href="${pageContext.request.contextPath}/coordinadorZonal/images/favicon.png" type="image/x-icon">
  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
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
        <img src="${pageContext.request.contextPath}/coordinadorZonal/images/cz_solicitud_logo.png" class="icons">
        <h1 class="logo"><strong>Mascotas Perdidas: Publicaciones de Usuario</strong></h1>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <p><strong>Descripción:</strong> Tabla que brinda información sobre actualizaciones hechas por usuarios respecto a mascotas perdidas.</p>
          <div class="table-responsive">
            <table id="example" class="table table-striped" style="width:100%;">
              <thead>
              <tr>
                <th>DNI</th>
                <th>Nombres y Apellidos</th>
                <th>Teléfono</th>
                <th>Fecha de Pérdida</th>
                <th>Fecha de Publicación</th>
                <th>Acciones</th>
              </tr>
              </thead>
              <tbody>
              <% if (publicaciones != null) {
                for (PublicacionMascotaPerdida publicacion : publicaciones) { %>
              <tr>
                <td><%= publicacion.getUsuario().getDni() %></td>
                <td><%= publicacion.getUsuario().getNombres_usuario_final() + " " + publicacion.getUsuario().getApellidos_usuario_final() %></td>
                <td><%= publicacion.getUsuario().getNumero_contacto_donaciones() %></td>
                <td><%= publicacion.getFechaPerdida() %></td>
                <td><%= publicacion.getFechaHoraRegistro() %></td>
                <td>
                  <ul class="icons">
                    <li>
                      <a href="#" class="icon fas fa-check-circle open-modal-found" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>">
                        <span class="label">Aceptar</span>
                      </a>
                    </li>
                    <li>
                      <a href="#" class="icon fas fa-times-circle open-modal-update" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>">
                        <span class="label">Rechazar</span>
                      </a>
                    </li>
                    <li>
                      <a href="#" class="icon fas fa-eye open-modal-info" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>">
                        <span class="label">Ver</span>
                      </a>
                    </li>
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

  <!-- Sidebar -->
  <div id="sidebar">
    <div class="inner">
      <!-- Logo -->
      <section class="alt" id="sidebar-header">
        <img src="${pageContext.request.contextPath}/coordinadorZonal/images/favicon.png" alt="Logo" id="sidebar-icon">
        <p id="sidebar-title">PetLink</p>
      </section>
      <!-- Menú -->
      <nav id="menu">
        <header class="major">
          <h2>Menú</h2>
        </header>
        <ul>
          <li><a href="${pageContext.request.contextPath}/PerfilUsuarioServlet">Mi Perfil</a></li>
          <li><a href="${pageContext.request.contextPath}/mascotas_perdidas_usuario.jsp">Mascotas Perdidas</a></li>
        </ul>
      </nav>
    </div>
  </div>

</div>

<!-- Scripts -->
<script>
  $(document).ready(function () {
    $('#example').DataTable();
  });
</script>

</body>
</html>
