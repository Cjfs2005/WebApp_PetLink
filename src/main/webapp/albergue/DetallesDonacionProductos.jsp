<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.RegistroDonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.HorarioRecepcionDonacion" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- CSS de DataTables y Bootstrap -->
<%
  Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
  String nombreUsuario = albergue.getNombre_albergue();
  String fotoPerfilBase64 = "";
  if (albergue.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
  }
%>
<%
  SolicitudDonacionProductos solicitud = (SolicitudDonacionProductos) request.getAttribute("solicitud");
  HorarioRecepcionDonacion horario = solicitud != null ? solicitud.getHorarioRecepcion() : null;
  String descripcion = solicitud != null && solicitud.getDescripcionDonaciones() != null ?
          solicitud.getDescripcionDonaciones() : "No disponible";
  String fechaEntrega = horario != null && horario.getFechaHoraInicio() != null ?
          horario.getFechaHoraInicio().toLocalDate().toString() : "No disponible";
  String horarioEntrega = horario != null && horario.getFechaHoraInicio() != null && horario.getFechaHoraFin() != null ?
          horario.getFechaHoraInicio().toLocalTime().toString() + " - " + horario.getFechaHoraFin().toLocalTime().toString() : "No disponible";
  List<RegistroDonacionProductos> donantes = (List<RegistroDonacionProductos>) request.getAttribute("donantes");
%>

<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
  <!-- Bootstrap y DataTables -->
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>

  <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
  <script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>


<!DOCTYPE html>
<html>
<head>
  <title>Detalles de la Donación</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body class="is-preload">
<div id="wrapper">
  <!-- Main -->
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Detalles</strong></h1>
        <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
          <% if (albergue.getFoto_perfil() != null) {%>
          <span class="ocultar"><%=nombreUsuario%></span> <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
          <% } else {%>
          <span class="ocultar"><%=nombreUsuario%></span> <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
          <% } %>
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="images/form.png" class="icons">
            <h2>Detalles de la donación de productos</h2>
          </header>
          <p><strong>Descripción:</strong> <%= descripcion %></p>
          <div class="row gtr-uniform">
            <div class="col-6 col-12-xsmall">
              <label for="fecha" class="input-label">Fecha de entrega</label>
              <input type="text" id="fecha" value="<%= fechaEntrega %>" disabled />
            </div>
            <div class="col-6 col-12-xsmall">
              <label for="horario" class="input-label">Horario de entrega</label>
              <input type="text" id="horario" value="<%= horarioEntrega %>" disabled />
            </div>
          </div>

          <!-- Tabla de donantes -->
          <p><strong>Lista de usuarios donantes</strong></p>
          <div class="table-responsive">
            <table id="example" class="table table-striped" style="width:100%;">
              <thead>
              <tr>
                <th>Nombres y apellidos</th>
                <th>Punto de acopio</th>
                <th>Fecha de donación</th>
                <th>Productos a donar</th>
              </tr>
              </thead>
              <tbody>
              <% if (donantes != null && !donantes.isEmpty()) { %>
              <% for (RegistroDonacionProductos donante : donantes) { %>
              <tr>
                <td><%= donante.getUsuarioFinal().getNombres_usuario_final() + " " + donante.getUsuarioFinal().getApellidos_usuario_final() %></td>
                <td>
                  <%= (donante.getHorarioRecepcionDonacion() != null
                          && donante.getHorarioRecepcionDonacion().getPuntoAcopioDonacion() != null
                          && donante.getHorarioRecepcionDonacion().getPuntoAcopioDonacion().getPuntoAcopio() != null) ?
                          donante.getHorarioRecepcionDonacion().getPuntoAcopioDonacion().getPuntoAcopio().getDireccion_punto_acopio() : "No disponible" %>
                </td>
                <td><%= donante.getFechaHoraRegistro() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(donante.getFechaHoraRegistro()) : "No disponible" %></td>
                <td>
                  <%= donante.getDescripcionesDonaciones() %>
                </td>
              </tr>
              <% } %>

              <% } %>
              </tbody>
            </table>
            <script>
              new DataTable('#example', {
                language: {
                  sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
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
            <div class="col-12">
              <ul class="actions form-buttons">
                <li>
                  <!-- Botón Modificar -->
                  <a href="DonacionProductosServlet?action=modificar&id=<%= solicitud.getIdSolicitudDonacionProductos() %>"
                     class="button primary big">
                    Modificar
                  </a>
                </li>
                <li>
                  <!-- Botón Eliminar con funcionalidad para desactivar la solicitud -->
                  <a href="DonacionProductosServlet?action=eliminar&id=<%= solicitud.getIdSolicitudDonacionProductos() %>"
                     class="button big"
                     onclick="return confirm('¿Está seguro de que desea eliminar esta solicitud?');">
                    Eliminar
                  </a>
                </li>
              </ul>
            </div>

          </div>
        </div>
      </section>
    </div>
  </div>

  <!-- Sidebar -->
  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
    <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
    <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
  </jsp:include>
</div>
<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
</body>
</html>
