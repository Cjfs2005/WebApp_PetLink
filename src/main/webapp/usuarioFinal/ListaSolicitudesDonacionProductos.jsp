  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
  <%@ page import="java.util.List" %>
  <%@ page import="java.util.Base64" %>
  <%@ page import="com.example.webapp_petlink.beans.Usuario" %>

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
  <!DOCTYPE HTML>
  <html>
  <head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
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
          <img src="<%=request.getContextPath()%>/usuarioFinal/images/donaciones.png" class="icons">
          <h1 class="logo"><strong>SOLICITUDES DE DONACIÓN</strong></h1>

          <!-- Sección para el nombre y enlace al perfil -->
          <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
            <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
            <% if (!fotoPerfilBase64.isEmpty()) { %>
            <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                 style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
            <% } %>
          </a>
        </header>

        <!-- Opciones de sección -->
        <section class="seccionPrueba" style="background-color: transparent !important; flex-wrap: wrap; gap:20px; justify-content: space-evenly;">
          <a href="#"><strong>Colectas de productos</strong></a>
          <a href="<%= request.getContextPath() %>/DonacionEconomicaUsuarioServlet?action=listar">Colectas de dinero</a>
        </section>

        <!-- Contenedor de solicitudes -->
        <div class="contenedor-banner">
          <%
            List<SolicitudDonacionProductos> solicitudes = (List<SolicitudDonacionProductos>) request.getAttribute("solicitudes");
            if (solicitudes != null && !solicitudes.isEmpty()) {
              for (SolicitudDonacionProductos solicitud : solicitudes) {
          %>

          <section class="banner">
            <div class="content">
              <%-- Imagen del albergue --%>
                <%-- Mostrar la imagen en la etiqueta <img> --%>
                <%
                  // Obtener la imagen desde el bean Usuario
                  byte[] fotoBytes = solicitud.getUsuarioAlbergue().getFoto_de_portada_albergue();
                  if (fotoBytes != null) {
                    System.out.println("[DEBUG] Imagen obtenida en bytes: " + fotoBytes.length + " bytes<br>");
                  } else {
                    System.out.println("[DEBUG] No se encontró imagen (fotoBytes es null)<br>");
                  }

                  // Convertir a Base64 si no es null
                  String fotoBase64 = (fotoBytes != null) ? Base64.getEncoder().encodeToString(fotoBytes) : null;
                  if (fotoBase64 != null) {
                    System.out.println("[DEBUG] Imagen en Base64 (parcial): " + fotoBase64.substring(0, 30) + "...<br>");
                  }
                %>

                <%-- Mostrar la imagen en la etiqueta <img> --%>
                <% if (fotoBase64 != null) { %>
                <span class="image object">
    <img src="data:image/png;base64,<%= fotoBase64 %>" alt="Imagen del albergue" />
</span>

                <% } else { %>
                <img src="images/default-placeholder.png" alt="Imagen por defecto" />
                <% } %>


              <%-- Información del albergue --%>
              <header>
                  <img src="<%=request.getContextPath()%>/usuarioFinal/images/imagen2Donaciones.png" class="icons">
                  <h2><%= solicitud.getUsuarioAlbergue().getNombre_albergue() %>: Colecta de productos</h2>
              </header>
              <p style="margin-top: 0.5em;">
                <strong>Productos solicitados:</strong>
                <%= solicitud.getDescripcionDonaciones() %>
              </p>
              <ul class="actions">
                <li>
                  <a href="DonacionProductosUsuarioServlet?action=formulario&id=<%= solicitud.getIdSolicitudDonacionProductos() %>" class="button primary big">Donar Productos</a>
                </li>
              </ul>
            </div>
          </section>
          <%
            }
          } else {
          %>
          <p style="text-align: center; font-size: 1.2em; color: #666;">No hay solicitudes de donación activas en este momento.</p>
          <%
            }
          %>
        </div>

        <!-- Paginación -->
        <div class="pagination-container" style="display: flex; justify-content: center;">
          <ul class="pagination"></ul>
        </div>
      </div>
    </div>

    <!-- Sidebar -->
    <jsp:include page="navbar.jsp">
      <jsp:param name="idUsuario" value="<%= usuario.getId_usuario() %>" />
      <jsp:param name="nombresUsuario" value="<%= usuario.getNombres_usuario_final() %>" />
      <jsp:param name="apellidosUsuario" value="<%= usuario.getApellidos_usuario_final() %>" />
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
