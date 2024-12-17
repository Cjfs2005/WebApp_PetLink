<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 22/11/2024
  Time: 21:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Base64" %>
<%@ page import="jakarta.servlet.http.*" %>


<% //Java code

  //Se obtiene los datos enviados por el Servlet

  Usuario datosUsuario = (Usuario) session.getAttribute("usuario");

  int tiene_postulacion = (int) request.getAttribute("tiene_postulacion");

  PublicacionMascotaAdopcion publicacion = (PublicacionMascotaAdopcion) request.getAttribute("publicacion");

  // Convertir la foto de perfil (byte[]) a una cadena base64 si no es null
  String fotoPerfilBase64 = "";
  if (datosUsuario != null && datosUsuario.getFoto_perfil() != null) {
    byte[] fotoPerfilBytes = datosUsuario.getFoto_perfil();
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(fotoPerfilBytes);
  }

  String Nombres_usuario_final = datosUsuario.getNombres_usuario_final();
  String Apellidos_usuario_final = datosUsuario.getApellidos_usuario_final();

%>

<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/ola2.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css"/>
  <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
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
        <h1 class="logo"><strong>Detalles de la mascota en adopción</strong></h1>

        <!-- Sección para el nombre y enlace al perfil -->
        <!-- Sección para el nombre y enlace al perfil -->
        <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
          <span class="ocultar"><%= datosUsuario.getNombres_usuario_final() %> <%= datosUsuario.getApellidos_usuario_final() %></span>
          <% if (!fotoPerfilBase64.isEmpty()) { %>
          <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } %>
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="images/form.png" class="icons">
            <h2>Ficha de datos de <%=publicacion.getNombreMascota()%></h2>
          </header>

          <div class="contenedor-imagenes">

            <%
              // Convertir la foto de perfil (byte[]) a una cadena base64 si no es null
              String fotoMascotaBase64 = "";
              if (publicacion != null && publicacion.getFotoMascota() != null) {
                byte[] fotoMascotaBytes = publicacion.getFotoMascota();
                fotoMascotaBase64 = Base64.getEncoder().encodeToString(fotoMascotaBytes);
              }
            %>

            <img src="data:image/png;base64,<%= fotoMascotaBase64 %>" alt=""/>

          </div>

          <p><strong>Raza:</strong> <%=publicacion.getTipoRaza()%></p>
          <p><strong>Lugar en el que fue encontrado:</strong> <%=publicacion.getLugarEncontrado()%></p>
          <p><strong>Descripción:</strong> <%=publicacion.getDescripcionMascota()%></p>
          <p><strong>Edad aproximada:</strong> <%=publicacion.getEdadAproximada()%></p>
          <p><strong>Género:</strong> <%=publicacion.getGeneroMascota()%></p>
          <p><strong>¿Está en un hogar temporal?:</strong> <%=publicacion.isEstaEnTemporal() ? "Sí" : "No"%></p>
          <p><strong>Condiciones para la adopción:</strong> <%=publicacion.getCondicionesAdopcion()%> </p>

          <!-- Break -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <ul class="actions form-buttons">
                <%if(tiene_postulacion==1){%>
                  <li><a href="${pageContext.request.contextPath}/AdopcionesUsuarioFinalServlet?accion=historial" class="button big" >Regresar</a></li>
                <%} else{%>
                  <li>
                    <form action="AdopcionesUsuarioFinalServlet" method="POST" onsubmit="return confirm('¿Estás seguro de que quieres postular para la adopción de <%=publicacion.getNombreMascota()%>?');" style="margin-bottom: 0;">
                    <input type="hidden" name="accion" value="adoptar">
                    <input type="hidden" name="id" value="<%= publicacion.getIdPublicacionMascotaAdopcion() %>">
                    <button type="submit" class="button primary big">Adoptar</button>
                    </form>
                  </li>
                  <li><a href="${pageContext.request.contextPath}/AdopcionesUsuarioFinalServlet?accion=ver" class="button big" >Regresar</a></li>
                <%}%>
              </ul>
            </div>
          </div>

        </div>
      </section>
    </div>
  </div>

  <!-- Sidebar -->
  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= datosUsuario.getId_usuario() %>" />
    <jsp:param name="nombresUsuario" value="<%= datosUsuario.getNombres_usuario_final() %>" />
    <jsp:param name="apellidosUsuario" value="<%= datosUsuario.getApellidos_usuario_final() %>" />
    <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
  </jsp:include>

</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
<!--Modal-->
<div id="modal" class="modal">
  <div class="modal-content">
    <!-- Botón cerrar -->
    <span class="close-btn">&times;</span>
    <p>¿Estás seguro de que quieres postular para la adopción de <%=publicacion.getNombreMascota()%>?</p>
    <!-- Botones de Aceptar y Cancelar -->
    <ul class="actions modal-buttons">
      <li><a href="#" class="button primary big" id="acceptButton">Sí</a></li>
      <li><a href="#" class="button big" id="cancelButton">No</a></li>
    </ul>
  </div>
</div>
<!-- Script dentro del mismo HTML -->
<script>
  // Esperamos a que todo el DOM esté cargado
  document.addEventListener('DOMContentLoaded', function() {
    // Obtener los elementos del DOM
    const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
    const modal = document.getElementById('modal');               // El modal
    const closeModalButton = document.querySelector('.close-btn'); // Botón para cerrar el modal (X)
    const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar
    const cancelButton = document.getElementById('cancelButton'); // Botón de Cancelar

    // Función para abrir el modal
    openModalButton.addEventListener('click', function() {
      modal.classList.add('show'); // Mostrar el modal
    });

    // Función para cerrar el modal al hacer clic en la "X"
    closeModalButton.addEventListener('click', function() {
      modal.classList.remove('show'); // Ocultar el modal
    });

    // Deben modificar el link para que redirija afuera del formulario
    acceptButton.addEventListener('click', function() {
      window.location.href = 'adopciones_usuario.html';
    });

    // Función para cerrar el modal al hacer clic en el botón "Cancelar"
    cancelButton.addEventListener('click', function() {
      modal.classList.remove('show'); // Ocultar el modal
    });
  });
</script>
</body>
</html>
