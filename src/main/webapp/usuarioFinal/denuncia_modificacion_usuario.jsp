<%--
  Created by IntelliJ IDEA.
  User: p4t1c0rn1o
  Date: 11/17/24
  Time: 9:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
<%
  // Obtener la denuncia desde el request
  DenunciaMaltratoAnimal denuncia = (DenunciaMaltratoAnimal) request.getAttribute("denuncia");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink - Modificar Denuncia</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/main.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">
<div id="wrapper">
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Modificación de denuncia por maltrato animal</strong></h1>
        <a href="<%=request.getContextPath()%>/usuarioFinal/perfil_usuario_final.jsp" class="user-profile">
          <span class="ocultar">Ander Vilchez</span>
          <img src="<%=request.getContextPath()%>/assets/images/foto_perfil.jpg" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
        </a>
      </header>

      <!-- Formulario -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="<%=request.getContextPath()%>/assets/images/form.png" class="icons">
            <h2>Completa el siguiente formulario</h2>
          </header>

          <form action="<%=request.getContextPath()%>/DenunciaUsuarioServlet" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="id" value="<%= denuncia.getIdDenunciaMaltratoAnimal() %>">

            <div class="row gtr-uniform">
              <!-- Tamaño -->
              <div class="col-6 col-12-xsmall">
                <label for="tamanio" class="input-label">Tamaño</label>
                <select id="tamanio" name="tamanio" required>
                  <option value="">- Seleccione el tamaño de la mascota -</option>
                  <option value="Pequeño" <%= "Pequeño".equals(denuncia.getTamanio()) ? "selected" : "" %>>Pequeño</option>
                  <option value="Mediano" <%= "Mediano".equals(denuncia.getTamanio()) ? "selected" : "" %>>Mediano</option>
                  <option value="Grande" <%= "Grande".equals(denuncia.getTamanio()) ? "selected" : "" %>>Grande</option>
                </select>
              </div>

              <!-- Raza -->
              <div class="col-6 col-12-xsmall">
                <label for="raza" class="input-label">Raza</label>
                <input type="text" id="raza" name="raza" value="<%= denuncia.getRaza() %>" placeholder="Ejemplo: Criollo" maxlength="45" required />
              </div>

              <!-- Descripción del maltrato -->
              <div class="col-12">
                <label for="descripcionMaltrato" class="input-label">Descripción del maltrato (máximo 300 caracteres):</label>
                <textarea id="descripcionMaltrato" name="descripcionMaltrato" class="text-area" maxlength="300" required><%= denuncia.getDescripcionMaltrato() %></textarea>
              </div>

              <!-- Nombre del maltratador -->
              <div class="col-12">
                <label for="nombreMaltratador" class="input-label">Nombre del maltratador</label>
                <input type="text" id="nombreMaltratador" name="nombreMaltratador" value="<%= denuncia.getNombreMaltratador() %>" placeholder="Ejemplo: Juan Pérez" maxlength="45" required />
              </div>

              <!-- Dirección del maltrato -->
              <div class="col-12">
                <label for="direccionMaltrato" class="input-label">Dirección del animal maltratado</label>
                <input type="text" id="direccionMaltrato" name="direccionMaltrato" value="<%= denuncia.getDireccionMaltrato() %>" placeholder="Ejemplo: Avenida Siempre Viva 742" maxlength="100" required />
              </div>

              <!-- Denuncia Policial -->
              <div class="col-12">
                <label for="tieneDenunciaPolicial" class="input-label">¿Se ha hecho una denuncia policial?</label>
                <select id="tieneDenunciaPolicial" name="tieneDenunciaPolicial" required>
                  <option value="true" <%= denuncia.isTieneDenunciaPolicial() ? "selected" : "" %>>Sí</option>
                  <option value="false" <%= !denuncia.isTieneDenunciaPolicial() ? "selected" : "" %>>No</option>
                </select>
              </div>

              <!-- Imagen -->
              <div class="col-12">
                <label for="fotoAnimal" class="input-label">Foto del animal maltratado</label>
                <input type="file" id="fotoAnimal" name="fotoAnimal" accept="image/png, image/jpeg" />
              </div>

              <!-- Botones -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Modificar</button></li>
                  <li><a href="<%=request.getContextPath()%>/DenunciaUsuarioServlet?accion=listar" class="button big">Cancelar</a></li>
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
        <img src="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" alt="Logo" id="sidebar-icon">
        <p id="sidebar-title">PetLink</p>
      </section>

      <!-- Perfil -->
      <section class="perfil">
        <div class="mini-posts">
          <article>
            <img src="<%=request.getContextPath()%>/usuarioFinal/images/foto_perfil.jpg" alt="" id="image-perfil">
            <h2 id="usuario">Ander Vilchez</h2>
          </article>
        </div>
      </section>

      <!-- Menú -->
      <nav id="menu">
        <header class="major"><h2>Menu</h2></header>
        <ul>
          <li><a href="<%=request.getContextPath()%>/usuarioFinal/perfil_usuario_final.jsp">PERFIL</a></li>
          <li>
            <span class="opener">ALBERGUES</span>
            <ul>
              <li><a href="<%=request.getContextPath()%>/usuarioFinal/albergue_usuario.jsp">LISTA DE ALBERGUES</a></li>
              <li><a href="<%=request.getContextPath()%>/usuarioFinal/eventos.jsp">EVENTOS BENÉFICOS</a></li>
              <li><a href="<%=request.getContextPath()%>/usuarioFinal/Donaciones1.jsp">SOLICITUDES DE DONACIÓN</a></li>
              <li><a href="<%=request.getContextPath()%>/usuarioFinal/Donaciones_historial.jsp">HISTORIAL DE DONACIONES</a></li>
              <li><a href="<%=request.getContextPath()%>/usuarioFinal/adopciones_usuario.jsp">MASCOTAS EN ADOPCIÓN</a></li>
              <li><a href="<%=request.getContextPath()%>/usuarioFinal/adopciones_historial_usuario.jsp">HISTORIAL DE ADOPCIONES</a></li>
            </ul>
          </li>
          <li><a href="<%=request.getContextPath()%>/usuarioFinal/Hogar_temporal.jsp">HOGAR TEMPORAL</a></li>
          <li><a href="<%=request.getContextPath()%>/usuarioFinal/denuncia_usuario.jsp">DENUNCIAS POR MALTRATO ANIMAL</a></li>
          <li><a href="<%=request.getContextPath()%>/usuarioFinal/mascotas_perdidas_usuario.jsp">MASCOTAS PERDIDAS</a></li>
        </ul>
      </nav>

      <!-- Logout -->
      <nav id="logout">
        <a href="<%=request.getContextPath()%>/usuarioFinal/bienvenidos.jsp" id="cerrar-sesion">Cerrar Sesión</a>
      </nav>
    </div>
  </div>
</div>
</body>
</html>

