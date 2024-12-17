<%--
  Created by IntelliJ IDEA.
  User: p4t1c0rn1o
  Date: 11/17/24
  Time: 9:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
  // Obtener la denuncia desde el request
  DenunciaMaltratoAnimal denuncia = (DenunciaMaltratoAnimal) request.getAttribute("denuncia");
%>
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
  <title>PetLink - Modificar Denuncia</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">
<div id="wrapper">
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Modificación de denuncia por maltrato animal</strong></h1>
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

          <form action="DenunciaServlet" method="POST" onsubmit="return confirm('¿Estás seguro de que deseas modificar la denuncia?');" enctype="multipart/form-data">
            <input type="hidden" name="action" value="update">
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
                <input type="text" id="nombreMaltratador" name="nombreMaltratador" value="<%= denuncia.getNombreMaltratador() %>" placeholder="Ejemplo: Juan Pérez" maxlength="45" required readonly disabled/>
              </div>

              <!-- Dirección del maltrato -->
              <div class="col-12">
                <label for="direccionMaltrato" class="input-label">Dirección del animal maltratado</label>
                <input type="text" id="direccionMaltrato" name="direccionMaltrato" value="<%= denuncia.getDireccionMaltrato() %>" placeholder="Ejemplo: Avenida Siempre Viva 742" maxlength="100" required readonly disabled/>
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
                <input type="file" id="fotoAnimal" name="fotoAnimal" accept="image/png" required/>
              </div>

              <!-- Botones -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Modificar</button></li>
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

