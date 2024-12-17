<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionMascotaPerdida" %>
<%
  List<PublicacionMascotaPerdida> publicaciones = (List<PublicacionMascotaPerdida>) request.getAttribute("publicaciones");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink - Mascotas Perdidas | Dueños</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/aditional.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/coordinadorZonal/assets/css/popup-window.css">
  <link rel="icon" href="${pageContext.request.contextPath}/coordinadorZonal/images/favicon.png" type="image/x-icon">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css" />
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <!-- Header -->
      <header id="header">
        <img src="${pageContext.request.contextPath}/coordinadorZonal/images/user_ban.png" class="icons">
        <h1 class="logo"><strong>MASCOTAS PERDIDAS | DUEÑOS</strong></h1>
        <a href="#" class="user-profile">
          <span class="ocultar">Marco Lang</span>
          <img src="${pageContext.request.contextPath}/coordinadorZonal/images/foto_coordinador.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;" />
        </a>
      </header>

      <!-- Banner -->
      <div class="contenedor-banner">
        <% if (publicaciones != null) {
          for (PublicacionMascotaPerdida publicacion : publicaciones) { %>
        <section class="banner">
          <div class="content">
            <header>
              <h1><%= publicacion.getNombre() %>. ID: <%= publicacion.getIdPublicacionMascotaPerdida() %></h1>
            </header>
            <ul class="actions" style="display: flex; flex-direction: column; padding: 0; gap: 10px;">
              <li style="width: 100%;">
                <a href="#" class="button open-modal-update" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>" style="display: flex; justify-content: center; align-items: center; padding: 10px 0; width: 100%;">REALIZAR ACTUALIZACIÓN</a>
              </li>
              <li style="width: 100%;">
                <a href="#" class="button open-modal-info" data-id="<%= publicacion.getIdPublicacionMascotaPerdida() %>" style="display: flex; justify-content: center; align-items: center; padding: 10px 0; width: 100%;">VER INFORMACIÓN</a>
              </li>
            </ul>
          </div>
          <span class="image object">
                        <img src="${pageContext.request.contextPath}/images/<%= publicacion.getNombre().toLowerCase() %>.jpg" alt="Imagen de <%= publicacion.getNombre() %>" />
                    </span>
        </section>
        <% }
        } else { %>
        <p>No hay publicaciones disponibles.</p>
        <% } %>
      </div>

      <!-- Pagination -->
      <div style="display: flex; justify-content: center;">
        <ul class="pagination">
          <li><span class="button disabled">&laquo;</span></li>
          <li><a href="#" class="page active">1</a></li>
          <li><a href="#" class="page">2</a></li>
          <li><a href="#" class="page">3</a></li>
          <li><span>…</span></li>
          <li><a href="#" class="page">10</a></li>
          <li><a href="#" class="button">&raquo;</a></li>
        </ul>
      </div>
    </div>
  </div>

  <!-- Sidebar -->
  <div id="sidebar">
    <div class="inner">

      <!-- Logo -->
      <section class="alt" id="sidebar-header">
        <img src="${pageContext.request.contextPath}/coordinadorZonal/images/favicon.png" alt="Logo" id="sidebar-icon">
        <p id="sidebar-title">PetLink</p>
      </section>

      <!-- Perfil -->
      <section class="perfil">
        <div class="mini-posts">
          <article>
            <img src="${pageContext.request.contextPath}/coordinadorZonal/images/foto_coordinador.png" alt="" id="image-perfil">
            <h2 id="usuario">Marco Lang<br>Zona Este</h2>
          </article>
        </div>
      </section>

      <!-- Menu -->
      <nav id="menu">
        <header class="major">
          <h2>Menú</h2>
        </header>
        <ul>
          <li><a href="${pageContext.request.contextPath}/solicitudes_usuario_mascotas_perdidas.html">SOLICITUD DEL USUARIO</a></li>
          <li><a href="${pageContext.request.contextPath}/solicitudes_duenio_mascotas_perdidas.html">SOLICITUD DEL DUEÑO</a></li>
          <li><a href="${pageContext.request.contextPath}/postulantes.html">Postulantes a hogar temporal</a></li>
        </ul>
      </nav>

      <!-- Logout -->
      <nav id="logout">
        <a href="${pageContext.request.contextPath}/logout.jsp" id="cerrar-sesion">Cerrar Sesión</a>
      </nav>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
<script>
  $(document).ready(function () {
    // Cargar detalles en el modal de información
    $('.open-modal-info').click(function () {
      const id = $(this).data('id');
      $.get('${pageContext.request.contextPath}/MascotaPerdidaDuenioServlet', {accion: 'detalles', idPublicacion: id}, function (data) {
        alert(JSON.stringify(data)); // Debug: Muestra detalles en consola
        // Aquí debes mostrar los datos en el modal de "Ver Información".
      });
    });

    // Enviar actualización desde el modal
    $('.open-modal-update').click(function () {
      const id = $(this).data('id');
      // Aquí puedes implementar lógica para mostrar el modal y procesar la actualización.
    });
  });
</script>
</body>
</html>
