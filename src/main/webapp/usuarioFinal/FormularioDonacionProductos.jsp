<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.HorarioRecepcionDonacion" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
  Usuario usuario = (Usuario) session.getAttribute("usuario");
  String nombreUsuario = usuario.getNombres_usuario_final();
  String apellidoUsuario = usuario.getApellidos_usuario_final();
  String fotoPerfilBase64 = "";
  if (usuario.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
  }
  else {
    fotoPerfilBase64 = "../../albergue/images/sin_perfil.png";
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Formulario de Donación</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">
<div id="wrapper">
  <div id="main">
    <div class="inner">
      <header id="header">

        <h1 class="logo"><strong>Donación de productos</strong></h1>

        <!-- Sección para el nombre y enlace al perfil -->
        <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
          <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
          <% if (!fotoPerfilBase64.isEmpty()) { %>
          <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } %>
        </a>
      </header>
      <section class="banner">
        <div class="content">
          <header>
            <img src="<%=request.getContextPath()%>/usuarioFinal/images/form.png" class="icons">
            <h2>Complete el siguiente formulario</h2>

          </header>
          <%
            SolicitudDonacionProductos solicitud = (SolicitudDonacionProductos) request.getAttribute("solicitud");
            PuntoAcopio puntoAcopio = null;
            HorarioRecepcionDonacion horarioRecepcion = null;

            if (solicitud != null && solicitud.getHorarioRecepcion() != null) {
              horarioRecepcion = solicitud.getHorarioRecepcion();
              if (horarioRecepcion.getPuntoAcopioDonacion() != null) {
                puntoAcopio = horarioRecepcion.getPuntoAcopioDonacion().getPuntoAcopio();
              }
            }
          %>

          <form action="DonacionProductosUsuarioServlet?action=crear" method="post">
            <!-- Campo oculto para enviar el idHorarioRecepcionDonacion -->
            <input type="hidden" name="idHorarioRecepcionDonacion" value="<%= horarioRecepcion != null ? horarioRecepcion.getIdHorarioRecepcionDonacion() : "" %>">

            <!-- Nombre del albergue -->
            <p>
              <strong>Albergue solicitante:</strong>
              <%= solicitud != null && solicitud.getUsuarioAlbergue() != null ? solicitud.getUsuarioAlbergue().getNombre_albergue() : "No disponible" %>
            </p>

            <!-- Descripción de productos solicitados -->
            <p>
              <strong>Productos solicitados:</strong>
              <%= solicitud != null ? solicitud.getDescripcionDonaciones() : "No disponible" %>
            </p>

            <!-- Punto de acopio -->
            <p>
              <strong>Punto de acopio:</strong>
              <%= puntoAcopio != null ? puntoAcopio.getDireccion_punto_acopio() : "No disponible" %>
            </p>

            <!-- Fecha de entrega -->
            <p>
              <strong>Fecha de entrega:</strong>
              <%= horarioRecepcion != null ? horarioRecepcion.getFechaHoraInicio().toLocalDate() : "No disponible" %>
            </p>

            <!-- Horario de entrega -->
            <p>
              <strong>Horario de entrega:</strong>
              <%= horarioRecepcion != null ? horarioRecepcion.getFechaHoraInicio().toLocalTime() + " - " + horarioRecepcion.getFechaHoraFin().toLocalTime() : "No disponible" %>
            </p>

            <!-- Productos a donar -->
            <div class="col-12">
              <label for="descripcionDonacion" class="input-label">Productos a donar (máximo 500 caracteres):</label>
              <textarea id="descripcionDonacion" name="descripcionDonacion" class="text-area" maxlength="500" placeholder="" required></textarea>
              <span id="charCount" style="margin-right: 0;">0/500</span>
            </div>

            <!-- Botones de Donar y Regresar -->
            <div class="col-12">
              <ul class="actions form-buttons">
                <li><button type="submit" class="button primary big">Donar</button></li>
                <li><a href="DonacionProductosUsuarioServlet?action=listar" class="button big">Regresar</a></li>
              </ul>
            </div>
          </form>
        </div>
      </section>
    </div>
  </div>
  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= usuario.getId_usuario() %>" />
    <jsp:param name="nombresUsuario" value="<%= usuario.getNombres_usuario_final() %>" />
    <jsp:param name="apellidosUsuario" value="<%= usuario.getApellidos_usuario_final() %>" />
    <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
  </jsp:include>
</div>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>
</body>
</html>