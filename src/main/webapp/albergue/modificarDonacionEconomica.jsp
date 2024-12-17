<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
  // Obtener la solicitud desde el request
  Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
  String nombreUsuario = albergue.getNombre_albergue();
  String fotoPerfilBase64 = "";
  if (albergue.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
  }
  SolicitudDonacionEconomica solicitud = (SolicitudDonacionEconomica) request.getAttribute("solicitud");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>Formulario para Donación Económica</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
  <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
  <style>
    #charCount {
      font-size: 12px;
      color: #888;
      float: right;
    }
  </style>
</head>
<body class="is-preload">
<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Modificar Donación Económica</strong></h1>
        <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
          <span class="ocultar"><%= albergue.getNombre_albergue() %></span>
          <img src="<%= !fotoPerfilBase64.isEmpty() ? "data:image/png;base64," + fotoPerfilBase64 : request.getContextPath() + "/albergue/images/default_profile.png" %>"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <header>
            <img src="<%=request.getContextPath()%>/albergue/images/form.png" class="icons">
            <h2>Formulario para Donación Económica</h2>
          </header>
          <p><strong>Descripción:</strong> Complete los detalles sobre la solicitud de donación económica.</p>

          <form action="DonacionEconomicaServlet?action=actualizar" method="post">
            <!-- ID de la solicitud (campo oculto) -->
            <input type="hidden" name="idSolicitud" value="<%= solicitud != null ? solicitud.getId_solicitud_donacion_economica() : "" %>" />

            <!-- Monto solicitado -->
            <div class="row gtr-uniform">
              <div class="col-12">
                <label for="monto" class="input-label">Monto Requerido:</label>
                <input type="text" id="monto" name="monto"
                       value="<%= solicitud != null ? solicitud.getMonto_solicitado() : "" %>" required />
              </div>

              <!-- Motivo -->
              <div class="col-12">
                <label for="motivo" class="input-label">Motivo:</label>
                <textarea id="motivo" name="motivo" required><%= solicitud != null ? solicitud.getMotivo() : "" %></textarea>
              </div>

              <!-- Botones -->
              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Guardar Cambios</button></li>
                  <li><a href="<%= request.getContextPath() %>/DonacionEconomicaServlet?action=listar" class="button big">Cancelar</a></li>
                </ul>
              </div>
            </div>
          </form>

        </div>
      </section>
    </div>
  </div>

  <!-- Sidebar -->
  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
    <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
    <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
  </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>

<!-- Modal -->
<div id="modal" class="modal">
  <div class="modal-content">
    <p>Se ha enviado su publicación con éxito.</p>
    <ul class="actions modal-buttons">
      <li><a href="donaciones_productos.html" class="button primary big" id="acceptButton">Aceptar</a></li>
    </ul>
  </div>
</div>

<!-- Lógica para el Modal -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
    const modal = document.getElementById('modal');               // El modal
    const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar

    // Función para abrir el modal
    openModalButton.addEventListener('click', function() {
      modal.classList.add('show'); // Mostrar el modal
    });

    // Redirigir al hacer clic en el botón "Aceptar"
    acceptButton.addEventListener('click', function() {
      window.location.href = 'donaciones.html';
    });
  });
</script>

<!-- Contador de caracteres para la descripción -->
<script>
  const textarea = document.getElementById('descripcion');
  const charCount = document.getElementById('charCount');

  textarea.addEventListener('input', function() {
    const currentLength = textarea.value.length;
    charCount.textContent = `${currentLength}/500`;
  });
</script>

</body>
</html>
