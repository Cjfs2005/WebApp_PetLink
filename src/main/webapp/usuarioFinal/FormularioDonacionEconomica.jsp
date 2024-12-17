  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
  </head>
  <body class="is-preload">

  <div id="wrapper">
    <div id="main">
      <div class="inner">

        <!-- Header -->
        <header id="header">
          <img src="<%=request.getContextPath()%>/usuarioFinal/images/eventos.png" class="icons">
          <h1 class="logo"><strong>Donación de dinero</strong></h1>
          <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
            <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
            <% if (!fotoPerfilBase64.isEmpty()) { %>
            <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil"
                 style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
            <% } %>
          </a>
        </header>

        <!-- Formulario -->
        <section class="banner">
          <div class="content">
            <header>
              <img src="<%=request.getContextPath()%>/usuarioFinal/images/donation.png" class="icons">
              <h2>Complete el siguiente formulario</h2>
            </header>

            <!-- Verificar que la solicitud exista -->
            <%
              SolicitudDonacionEconomica solicitud = (SolicitudDonacionEconomica) request.getAttribute("solicitud");
              Integer montoRecaudado = (Integer) request.getAttribute("montoRecaudado");
              if (solicitud != null) {
                String qrBase64 = solicitud.getUsuario_albergue().getNombre_imagen_qr();
            %>

            <!-- Detalles de la solicitud -->
            <p><strong>Albergue solicitante:</strong> <%= solicitud.getUsuario_albergue().getNombre_albergue() %></p>
            <p><strong>Motivo:</strong> <%= solicitud.getMotivo() %></p>
            <p style="margin-bottom: 1.5em;"><strong>Monto recaudado:</strong>
              S/<%= montoRecaudado != null ? montoRecaudado : 0 %> de S/<%= solicitud.getMonto_solicitado() %>
            </p>

            <!-- Formulario de donación -->
            <form action="DonacionEconomicaUsuarioServlet?action=crearEconomica" method="post" enctype="multipart/form-data">
              <!-- ID de la solicitud -->
              <input type="hidden" name="idSolicitudDonacionEconomica" value="<%= solicitud.getId_solicitud_donacion_economica() %>">

              <!-- Monto donado -->
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">
                  <label for="monto" class="input-label">Ingresa el monto donado (S/):</label>
                  <input
                          type="text"
                          id="monto"
                          name="montoDonacion"
                          maxlength="10"
                          inputmode="numeric"
                          required
                          pattern="\d+"
                          title="Solo se permiten números.">
                </div>

                <!-- Imagen de la donación -->
                <div class="col-6 col-12-xsmall">
                  <label for="archivo" class="input-label">Subir comprobante de donación:</label>
                  <input
                          type="file"
                          id="archivo"
                          name="archivo"
                          class="contenedor-archivo"
                          accept="image/png, image/jpeg"
                          required>
                </div>
              </div>

              <!-- Mostrar QR si está disponible -->
              <div class="contenedor-imagenes">
                <% if (qrBase64 != null) { %>
                <img src="data:image/png;base64,<%= qrBase64 %>" alt="Código QR para donación" class="img-unica">
                <% } else { %>
                <p>No hay código QR disponible.</p>
                <% } %>
              </div>

              <!-- Botones -->
              <div class="row gtr-uniform">
                <div class="col-12">
                  <ul class="actions form-buttons">
                    <li><button type="submit" class="button primary big">DONAR</button></li>
                    <li><a href="DonacionEconomicaUsuarioServlet?action=listar" class="button big">REGRESAR</a></li>
                  </ul>
                </div>
              </div>
            </form>

            <% } else { %>
            <!-- Mensaje si no hay solicitud -->
            <p>No se encontraron detalles para esta solicitud.</p>
            <% } %>
          </div>
        </section>
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

  <script>
    // Validar monto en el frontend
    document.addEventListener('DOMContentLoaded', function() {
      const montoInput = document.getElementById('monto');
      montoInput.addEventListener('input', function() {
        this.value = this.value.replace(/\D/g, '');
      });
    });
  </script>
  </body>
  </html>
