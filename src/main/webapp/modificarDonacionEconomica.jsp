<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%
  // Obtener la solicitud desde el request
  SolicitudDonacionEconomica solicitud = (SolicitudDonacionEconomica) request.getAttribute("solicitud");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>Formulario para Donación Económica</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="assets/css/main.css" />
  <link rel="stylesheet" href="assets/css/aditional.css">
  <link rel="stylesheet" href="assets/css/popup-window.css">
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
        <h1 class="logo"><strong>Modificar Donación Económica</strong></h1>
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
            <h2>Formulario para Donación Económica</h2>
          </header>
          <p><strong>Descripción:</strong> Complete los detalles sobre la solicitud de donación económica.</p>

          <form action="ListaSolicitudesDonacionEconomica?action=actualizar" method="post">
            <!-- ID de la solicitud (campo oculto) -->
            <input type="hidden" name="idSolicitud" value="<%= solicitud != null ? solicitud.getId_solicitud_donacion_economica() : "" %>" />

            <!-- Monto solicitado -->
            <div class="row gtr-uniform">
              <div class="col-12">
                <label for="monto" class="input-label">Monto Requerido:</label>
                <input type="text" id="monto" name="monto"
                       value="<%= solicitud != null ? solicitud.getMonto_solicitado() : "" %>" required />
              </div>

              <!-- Motivo -->
              <div class="col-12">
                <label for="motivo" class="input-label">Motivo:</label>
                <textarea id="motivo" name="motivo" required><%= solicitud != null ? solicitud.getMotivo() : "" %></textarea>
              </div>

              <!-- Botones -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Guardar Cambios</button></li>
                  <li><a href="<%= request.getContextPath() %>/ListaSolicitudesDonacionEconomica?action=listar" class="button big">Cancelar</a></li>
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
        <a href="#" id="cerrar-sesion">Cerrar Sesión</a>
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

<!-- Modal -->
<div id="modal" class="modal">
  <div class="modal-content">
    <p>Se ha enviado su publicación con éxito.</p>
    <ul class="actions modal-buttons">
      <li><a href="donaciones_productos.html" class="button primary big" id="acceptButton">Aceptar</a></li>
    </ul>
  </div>
</div>

<!-- Lógica para el Modal -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
    const modal = document.getElementById('modal');               // El modal
    const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar

    // Función para abrir el modal
    openModalButton.addEventListener('click', function() {
      modal.classList.add('show'); // Mostrar el modal
    });

    // Redirigir al hacer clic en el botón "Aceptar"
    acceptButton.addEventListener('click', function() {
      window.location.href = 'donaciones.html';
    });
  });
</script>

<!-- Contador de caracteres para la descripción -->
<script>
  const textarea = document.getElementById('descripcion');
  const charCount = document.getElementById('charCount');

  textarea.addEventListener('input', function() {
    const currentLength = textarea.value.length;
    charCount.textContent = `${currentLength}/500`;
  });
</script>

</body>
</html>
