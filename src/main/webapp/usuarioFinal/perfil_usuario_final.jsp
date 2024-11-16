<%--
  Created by IntelliJ IDEA.
  User: GIANFRANCO
  Date: 6/11/2024
  Time: 02:28
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
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/ola2.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css" />
  <link rel="icon" href="${pageContext.request.contextPath}/usuarioFinal/images/favicon.png" type="image/x-icon">
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
        <h1 class="logo"><strong>MI PERFIL</strong></h1>
        <!-- Sección para el nombre y enlace al perfil -->
        <a href="perfil_usuario.html" class="user-profile">
          <span class="ocultar"><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></span>
          <img src="${pageContext.request.contextPath}/usuarioFinal/images/foto_perfil.jpg" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <div class="perfil-info">
            <div class="left-section">
              <img src="${pageContext.request.contextPath}/usuarioFinal/images/foto_perfil.jpg" alt="Foto de perfil">
            </div>
            <div class="central-section">
              <h2><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></h2>
            </div>
          </div>
          <hr>

          <!-- Información -->
          <div class="seccion">
            <h3>Información</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">Nombres:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="nombres" value="<%= usuarioPerfil.getNombres_usuario_final() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Apellidos:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="apellidos" value="<%= usuarioPerfil.getApellidos_usuario_final() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">DNI:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="dni" value="<%= usuarioPerfil.getDni() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Correo electrónico:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="correoElectronico" value="<%= usuarioPerfil.getCorreo_electronico() %>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <!-- Ubicación -->
          <div class="seccion">
            <h3>Ubicación</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">Dirección:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="direccion" value="<%= usuarioPerfil.getDireccion() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Distrito:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="distrito" value="<%= usuarioPerfil.getDistrito().getNombre_distrito() %>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <!-- Botón para Editar Perfil -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <ul class="actions form-buttons">
                <li>
                  <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=editar" class="button primary big">Editar perfil</a>
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

      <!-- Logo -->
      <section class="alt" id="sidebar-header">
        <img src="images/favicon.png" alt="Logo" id="sidebar-icon">
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

      <!-- Menú -->
      <nav id="menu">
        <header class="major"><h2>Menu</h2></header>
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
        <a href="../bienvenidos.html" id="cerrar-sesion">Cerrar Sesión</a>
      </nav>

    </div>
  </div>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

</body>
</html>
