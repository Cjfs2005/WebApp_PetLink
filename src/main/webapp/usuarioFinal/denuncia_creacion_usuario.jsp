<%--
  Created by IntelliJ IDEA.
  User: p4t1c0rn1o
  Date: 11/17/24
  Time: 12:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
  Usuario usuarioPerfil = (Usuario) session.getAttribute("usuario");
  String fotoPerfilBase64 = "";
  if (usuarioPerfil.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuarioPerfil.getFoto_perfil());
  }
%>
<!DOCTYPE HTML>
<html lang="es">
<head>
  <title>PetLink - Registro de Denuncia</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Registro de denuncia por maltrato animal</strong></h1>
        <!-- Sección para el nombre y enlace al perfil -->
        <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
          <span class="ocultar"><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></span>
          <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
        </a>
      </header>

      <!-- Formulario -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="<%=request.getContextPath()%>/usuarioFinal/images/form.png" class="icons">
            <h2>Completa el siguiente formulario</h2>
          </header>

          <form action="DenunciaServlet" method="POST" onsubmit="return confirm('¿Estás seguro de que deseas registrar la denuncia?');" enctype="multipart/form-data">
            <input type="hidden" name="action" value="insert">
            <div class="row gtr-uniform">

              <!-- Tamaño -->
              <div class="col-6 col-12-xsmall">
                <label for="tamanio" class="input-label">Tamaño</label>
                <select name="tamanio" id="tamanio" required>
                  <option value="">Seleccione el tamaño</option>
                  <option value="Pequeño">Pequeño</option>
                  <option value="Mediano">Mediano</option>
                  <option value="Grande">Grande</option>
                </select>
              </div>

              <!-- Raza -->
              <div class="col-6 col-12-xsmall">
                <label for="raza" class="input-label">Raza</label>
                <input type="text" id="raza" name="raza" maxlength="45" placeholder="Ingrese la raza" required>
              </div>

              <!-- Descripción -->
              <div class="col-12">
                <label for="descripcionMaltrato" class="input-label">Descripción del maltrato (máximo 300 caracteres):</label>
                <textarea id="descripcionMaltrato" class="text-area" name="descripcionMaltrato" maxlength="300" placeholder="Describa el maltrato..." required></textarea>
              </div>

              <!-- Nombre del Maltratador -->
              <div class="col-12">
                <label for="nombreMaltratador" class="input-label">Nombre del maltratador</label>
                <input type="text" id="nombreMaltratador" name="nombreMaltratador" maxlength="45" placeholder="Ingrese el nombre del maltratador" required>
              </div>

              <!-- Dirección -->
              <div class="col-12">
                <label for="direccionMaltrato" class="input-label">Dirección del animal maltratado</label>
                <input type="text" id="direccionMaltrato" name="direccionMaltrato" maxlength="100" placeholder="Ingrese la dirección" required>
              </div>

              <!-- Denuncia Policial -->
              <div class="col-12">
                <label for="tieneDenunciaPolicial" class="input-label">¿Se ha realizado una denuncia policial?</label>
                <select id="tieneDenunciaPolicial" name="tieneDenunciaPolicial" required>
                  <option value="">Seleccione una opción</option>
                  <option value="true">Sí</option>
                  <option value="false">No</option>
                </select>
              </div>

              <!-- Foto -->
              <div class="col-12">
                <label for="fotoAnimal" class="input-label">Foto del animal maltratado</label>
                <input type="file" id="fotoAnimal" name="fotoAnimal" accept="image/png" required>
              </div>

              <!-- Botones -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Registrar</button></li>
                  <li><a href="<%=request.getContextPath()%>/DenunciaServlet" class="button big">Cancelar</a></li>
                </ul>
              </div>

            </div>
          </form>
        </div>
      </section>

    </div>
  </div>

  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= usuarioPerfil.getId_usuario() %>" />
    <jsp:param name="nombresUsuario" value="<%= usuarioPerfil.getNombres_usuario_final() %>" />
    <jsp:param name="apellidosUsuario" value="<%= usuarioPerfil.getApellidos_usuario_final() %>" />
    <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
  </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

</body>
</html>
