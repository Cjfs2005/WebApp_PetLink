<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.RegistroDonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.HorarioRecepcionDonacion" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- CSS de DataTables y Bootstrap -->
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

  <!-- Bootstrap y DataTables -->
  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
  <link rel="stylesheet" href="albergue/assets/css/main.css" />
  <link rel="stylesheet" href="albergue/assets/css/aditional.css">
  <link rel="icon" href="albergue/images/favicon.png" type="image/x-icon">

  <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
  <script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>

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

<!DOCTYPE html>
<html>
<head>
  <title>Detalles de la Donación</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Bootstrap y DataTables -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="albergue/assets/css/main.css">
  <link rel="stylesheet" href="albergue/assets/css/aditional.css">
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <script src="https://cdn.datatables.net/2.1.8/js/jquery.dataTables.min.js"></script>
  <script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.min.js"></script>
</head>
<body class="is-preload">
<div id="wrapper">
  <!-- Main -->
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Detalles</strong></h1>
        <a href="perfil.html" class="user-profile">
          <span class="ocultar">Huellitas PUCP</span>
          <img src="albergue/images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="albergue/images/form.png" class="icons">
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
                  <ul class="icons">
                    <li>
                      <a href="#" class="icon fas fa-eye" title="Ver la donación" style="color: black;"
                         onclick="alert('Productos: <%= donante.getDescripcionesDonaciones() %>')">
                        <span class="label">Ver</span>
                      </a>
                    </li>
                  </ul>
                </td>
              </tr>
              <% } %>
              <% } else { %>
              <tr>
                <td colspan="5" class="text-center">No hay donantes disponibles</td>
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
                  <a href="ListaSolicitudesDonacionProductos?action=modificar&id=<%= solicitud.getIdSolicitudDonacionProductos() %>"
                     class="button primary big">
                    Modificar
                  </a>
                </li>
                <li>
                  <!-- Botón Eliminar con funcionalidad para desactivar la solicitud -->
                  <a href="ListaSolicitudesDonacionProductos?action=eliminar&id=<%= solicitud.getIdSolicitudDonacionProductos() %>"
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
  <div id="sidebar">
    <div class="inner">
      <section class="alt" id="sidebar-header">
        <img src="albergue/images/favicon.png" alt="Logo" id="sidebar-icon">
        <p id="sidebar-title">PetLink</p>
      </section>
      <section class="perfil">
        <div class="mini-posts">
          <article>
            <img src="albergue/images/logo_huellitas.png" alt="" id="image-perfil">
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
        </ul>
      </nav>
      <nav id="logout">
        <a href="../bienvenidos.html" id="cerrar-sesion">Cerrar Sesión</a>
      </nav>
    </div>
  </div>
</div>
<!-- Scripts -->
<script src="<%=request.getContextPath()%>assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>assets/js/breakpoints.min.js"></script>
<script src="albergue/assets/js/util.js"></script>
<script src="albergue/assets/js/main.js"></script>
</body>
</html>
