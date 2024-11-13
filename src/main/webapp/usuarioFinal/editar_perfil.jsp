<%--
  Created by IntelliJ IDEA.
  User: GIANFRANCO
  Date: 6/11/2024
  Time: 03:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%
  Usuario usuarioPerfil = (Usuario) request.getAttribute("usuarioPerfil");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/ola2.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css" />
  <link rel="icon" href="images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <!-- Header -->
      <header id="header">
        <img src="${pageContext.request.contextPath}/usuarioFinal/images/perfil_logo.png" class="icons">
        <h1 class="logo"><strong>Editar perfil</strong></h1>

        <!-- Sección para el nombre y enlace al perfil -->
        <a href="perfil_usuario.html" class="user-profile">
          <span class="ocultar"><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></span>
          <img src="${pageContext.request.contextPath}/usuarioFinal/images/foto_perfil.jpg" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="${pageContext.request.contextPath}/usuarioFinal/images/form.png" class="icons">
            <h2>Datos del usuario</h2>
          </header>
          <form action="PerfilUsuarioServlet?accion=actualizar" method="post" style="margin-bottom: 0;">
            <div class="row gtr-uniform">
              <div class="col-6 col-12-xsmall">
                <label for="nombres" class="input-label">Nombres</label>
                <input type="text" id="nombres" name="nombres" value="<%= usuarioPerfil.getNombres_usuario_final() %>" placeholder="" required/>
              </div>
              <div class="col-6 col-12-xsmall">
                <label for="apellidos" class="input-label">Apellidos</label>
                <input type="text" id="apellidos" name="apellidos" value="<%= usuarioPerfil.getApellidos_usuario_final() %>" placeholder="" required/>
              </div>
              <!-- Break -->
              <div class="col-6 col-12-xsmall">
                <label for="dni" class="input-label">DNI</label>
                <input type="text" id="dni" value="<%= usuarioPerfil.getDni() %>" placeholder="" disabled/>
              </div>
              <div class="col-6 col-12-xsmall">
                <label for="distrito" class="input-label">Distrito</label>
                <select id="distrito" disabled>
                  <option value="">Seleccione su distrito</option>
                  <option value="35" <%= "San Miguel".equals(usuarioPerfil.getDistrito().getNombre_distrito()) ? "selected" : "" %>>San Miguel</option>
                  <!-- Aquí deberías colocar todas las demás opciones de distrito, con lógica similar para el valor seleccionado -->
                </select>
              </div>
              <!-- Break -->
              <div class="col-6 col-12-xsmall">
                <label for="direccion" class="input-label">Dirección</label>
                <input type="text" id="direccion" name="direccion" value="<%= usuarioPerfil.getDireccion() %>" placeholder="" required/>
              </div>
              <div class="col-6 col-12-xsmall">
                <label for="correo" class="input-label">Correo electrónico</label>
                <input type="email" id="correo" name="correo" value="<%= usuarioPerfil.getCorreo_electronico() %>" placeholder="" required/>
              </div>

              <!-- Break -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Guardar Cambios</button></li>
                  <li><a href="PerfilUsuarioServlet?accion=ver" class="button big">Cancelar</a></li>
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
        <img src="${pageContext.request.contextPath}/usuarioFinal/images/favicon.png" alt="Logo" id="sidebar-icon">
        <p id="sidebar-title">PetLink</p>
      </section>

      <!-- Perfil -->
      <section class="perfil">
        <div class="mini-posts">
          <article>
            <img src="${pageContext.request.contextPath}/usuarioFinal/images/foto_perfil.jpg" alt="" id="image-perfil">
            <h2 id="usuario"><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></h2>
          </article>
        </div>
      </section>

      <!-- Menu -->
      <nav id="menu">
        <header class="major">
          <h2>Menu</h2>
        </header>
        <ul>
          <li><a href="perfil_usuario.html">PERFIL</a></li>
          <li>
            <span class="opener">ALBERGUES</span>
            <ul>
              <li><a href="albergue_usuario.html">LISTA DE ALBERGUES</a></li>
              <li><a href="eventos.html">EVENTOS BENÉFICOS</a></li>
              <li><a href="Donaciones1.html">SOLICITUDES DE DONACIÓN</a></li>
              <li><a href="Donaciones_historial.html">HISTORIAL DE DONACIONES</a></li>
              <li><a href="adopciones_usuario.html">MASCOTAS EN ADOPCIÓN</a></li>
              <li><a href="adopciones_historial_usuario.html">HISTORIAL DE ADOPCIONES</a></li>
            </ul>
          </li>
          <li><a href="Hogar_temporal.html">HOGAR TEMPORAL</a></li>
          <li><a href="denuncias_usuario.html">DENUNCIAS POR MALTRATO ANIMAL</a></li>
          <li><a href="mascotas_perdidas_usuario.html">MASCOTAS PERDIDAS</a></li>
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
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/main.js"></script>

<!-- Modal -->
<div id="modal" class="modal">
  <div class="modal-content">
    <!-- Botón cerrar -->
    <span class="close-btn">&times;</span>
    <p>¿Estás seguro de editar tu perfil?</p>
    <!-- Botones de Aceptar y Cancelar -->
    <ul class="actions modal-buttons">
      <li><a href="#" class="button primary big" id="acceptButton">Sí</a></li>
      <li><a href="#" class="button big" id="cancelButton">No</a></li>
    </ul>
  </div>
</div>

<!-- Script dentro del mismo HTML -->
<!-- Script -->
<script>
  document.addEventListener('DOMContentLoaded', function () {
    const openModalButton = document.getElementById('openModal');
    const modal = document.getElementById('modal');
    const closeModalButton = document.querySelector('.close-btn');
    const acceptButton = document.getElementById('acceptButton');
    const cancelButton = document.getElementById('cancelButton');

    const nombresInput = document.getElementById('nombres');
    const apellidosInput = document.getElementById('apellidos');

    function validarYMostrarModal(event) {
      if (nombresInput.checkValidity() && apellidosInput.checkValidity()) {
        modal.classList.add('show');
      } else {
        document.querySelector('form').reportValidity();
      }
    }

    closeModalButton.addEventListener('click', function () {
      modal.classList.remove('show');
    });

    acceptButton.addEventListener('click', function () {
      window.location.href = 'perfil_usuario.html';
    });

    cancelButton.addEventListener('click', function () {
      modal.classList.remove('show');
    });

    openModalButton.addEventListener('click', validarYMostrarModal);
  });
</script>

<!--Scrips de validación-->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const nombresInput = document.getElementById('nombres');
    const apellidosInput = document.getElementById('apellidos');

    function limitarCaracteres(event) {
      const maxLength = 45;
      const inputField = event.target;

      if (inputField.value.length > maxLength) {
        inputField.value = inputField.value.substring(0, maxLength);
      }
    }

    nombresInput.addEventListener('input', limitarCaracteres);
    apellidosInput.addEventListener('input', limitarCaracteres);
  });
</script>

</body>
</html>
