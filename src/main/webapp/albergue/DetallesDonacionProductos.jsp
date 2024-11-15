<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%@ page import="java.sql.Time" %>
<%@ page import="java.sql.Date" %>

<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="assets/css/main.css" />
  <link rel="stylesheet" href="assets/css/aditional.css">
  <link rel="stylesheet" href="assets/css/popup-window.css">
  <link rel="icon" href="images/favicon.png" type="image/x-icon">
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
</head>
<body class="is-preload">
<div id="wrapper">
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Detalles</strong></h1>
        <a href="perfil.html" class="user-profile">
          <span class="ocultar">Huellitas PUCP</span> <img src="images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="images/form.png" class="icons">
            <h2>Detalles de la donación de productos</h2>
          </header>

          <%
            SolicitudDonacionProductos solicitudDetalles = (SolicitudDonacionProductos) request.getAttribute("solicitudDetalles");
            Time horaInicio = (Time) request.getAttribute("horaInicio");
            Time horaFin = (Time) request.getAttribute("horaFin");
            if (solicitudDetalles != null) {
          %>
          <p><strong>Descripción:</strong> <%= solicitudDetalles.getDescripcionDonaciones() %></p>
          <div class="row gtr-uniform">
            <div class="col-6 col-12-xsmall">
              <label for="fecha" class="input-label">Fecha de entrega</label>
              <input type="text" id="fecha" value="<%= solicitudDetalles.getFechaHoraRegistro() %>" disabled/>
            </div>
            <div class="col-6 col-12-xsmall">
              <label for="horarioEntrega" class="input-label">Horario de entrega</label>
              <input type="text" id="horarioEntrega" value="<%= horaInicio %> - <%= horaFin %>" disabled/>
            </div>
          </div>

          <p><strong>Lista de usuarios donantes</strong></p>
          <div class="table-responsive">
            <table id="example" class="table table-striped" style="width:100%;">
              <thead>
              <tr>
                <th>DNI</th>
                <th>Nombres y apellidos</th>
                <th>Punto de acopio</th>
                <th>Fecha de donación</th>
                <th>Productos a donar</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td>22222222</td>
                <td>Christian Jair Flores Soto</td>
                <td>Av. Barcelona</td>
                <td>12/02/2023</td>
                <td><a href="#" class="icon fas fa-eye" title="Ver la donación"><span class="label">Ver</span></a></td>
              </tr>
              <!-- Reemplazar este ejemplo con datos reales -->
              </tbody>
            </table>
          </div>

          <!-- Botones de acción -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <ul class="actions form-buttons">
                <li><a href="modificarDonacionProductos.jsp?id=<%= solicitudDetalles.getIdSolicitudDonacionProductos() %>" class="button primary big">Modificar</a></li>
                <li><a href="#" class="button big" id="openModal">Eliminar</a></li>
              </ul>
            </div>
          </div>

          <!-- Modal para confirmación de eliminación -->
          <div id="modal" class="modal">
            <div class="modal-content">
              <span class="close-btn">&times;</span>
              <p>¿Está seguro de eliminar esta publicación?<br>Este post ya no será visualizado por los usuarios.</p>
              <ul class="actions modal-buttons">
                <li><a href="ListaSolicitudesDonacionProductos?action=eliminar&id=<%= solicitudDetalles.getIdSolicitudDonacionProductos() %>" class="button primary big">Aceptar</a></li>
                <li><a href="#" class="button big" id="cancelButton">Cancelar</a></li>
              </ul>
            </div>
          </div>

          <%
          } else {
          %>
          <p>No se encontraron detalles para esta solicitud.</p>
          <%
            }
          %>

        </div>
      </section>
    </div>
  </div>

  <!-- Sidebar -->
  <div id="sidebar">
    <div class="inner">
      <section class="alt" id="sidebar-header">
        <img src="images/favicon.png" alt="Logo" id="sidebar-icon">
        <p id ="sidebar-title">PetLink</p>
      </section>
      <section class="perfil">
        <div class="mini-posts">
          <article>
            <img src="images/logo_huellitas.png" alt="" id="image-perfil">
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

<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const openModalButton = document.getElementById('openModal');
    const modal = document.getElementById('modal');
    const closeModalButton = document.querySelector('.close-btn');
    const cancelButton = document.getElementById('cancelButton');

    openModalButton.addEventListener('click', function() {
      modal.classList.add('show');
    });

    closeModalButton.addEventListener('click', function() {
      modal.classList.remove('show');
    });

    cancelButton.addEventListener('click', function() {
      modal.classList.remove('show');
    });
  });
</script>
</body>
</html>
