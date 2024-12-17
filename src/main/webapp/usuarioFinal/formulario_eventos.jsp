<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionEventoBenefico" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<% PublicacionEventoBenefico evento = (PublicacionEventoBenefico) request.getAttribute("evento");
  String fotoEventoBase64 = "";
  if (evento.getFoto() != null) {
    fotoEventoBase64 = Base64.getEncoder().encodeToString(evento.getFoto());
  }

%>

<%
  Usuario usuario = (Usuario) session.getAttribute("datosUsuario");
  String nombreUsuario = usuario.getNombres_usuario_final();
  String apellidoUsuario = usuario.getApellidos_usuario_final();
  String fotoPerfilBase64 = "";
  if (usuario.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
  }
  else {
    fotoPerfilBase64 = "../../albergue/images/sin_perfil.png";
  }

  boolean hayVacantes = (evento.getAforoEvento() - evento.getCantAsistentes()) > 0;
  boolean estaInscrito = (boolean) request.getAttribute("usuarioInscrito");
  boolean hayTraslape = (boolean) request.getAttribute("hayTraslape");
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

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>Detalles del evento</strong></h1>

        <a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver" class="user-profile">
          <span class="ocultar"><%= usuario.getNombres_usuario_final() %> <%= usuario.getApellidos_usuario_final() %></span>
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
            <img src="<%=request.getContextPath()%>/usuarioFinal/images/form.png" class="icons">
            <h2><%=evento.getNombreEvento()%></h2>
          </header>

          <div class="contenedor-imagenes">
            <% if (evento.getFoto() != null) {%>
            <img src="data:image/png;base64,<%= fotoEventoBase64 %>" alt="<%=evento.getNombreFoto()%>" class="img-unica">
            <% } else {%>
            <img src="<%=request.getContextPath()%>/usuarioFinal/images/pic01.jpg" alt="<%=evento.getNombreFoto()%>" class="img-unica">
            <% } %>

          </div>

          <p><strong>Albergue organizador: </strong><%=evento.getUsuarioAlbergue().getNombre_albergue()%> </p>
          <p><strong>Razón del evento: </strong><%=evento.getRazonEvento()%></p>
          <p><strong>Descripción: </strong><%=evento.getDescripcionEvento()%> </strong></p>
          <p><strong>Información General:</strong></p>
          <ul>

            <%
              SimpleDateFormat formatter = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
              String fechaFormateada = formatter.format(evento.getFechaHoraInicioEvento());
            %>
            <li><strong>Fecha: </strong><%=fechaFormateada%></li>

            <%
              SimpleDateFormat formatterHoraInicio = new SimpleDateFormat("HH:mm"); // Para formato 12 horas con AM/PM
              String horaFormateadaInicio = formatterHoraInicio.format(evento.getFechaHoraInicioEvento());

              SimpleDateFormat formatterHoraFin = new SimpleDateFormat("HH:mm");
              String horaFormateadaFin = formatterHoraFin.format(evento.getFechaHoraFinEvento());
            %>
            <li><strong>Duración: </strong><%=horaFormateadaInicio%>-<%=horaFormateadaFin%> </li>
            <li><strong>Lugar: </strong> <%=evento.getLugarEvento().getNombre_lugar_evento()%>, <%=evento.getLugarEvento().getDireccion_lugar_evento()%> - <%=evento.getLugarEvento().getDistrito().getNombre_distrito()%></li>
            <li><strong>Invitados: </strong><%=evento.getArtistasProvedoresInvitados()%></li>
            <%
              String vacantes;
              int aforo = evento.getAforoEvento();
              if (aforo - evento.getCantAsistentes() > 0) {
                String asistentes = Integer.toString(evento.getAforoEvento() - evento.getCantAsistentes());
                String aforoStr = Integer.toString(aforo);
                vacantes = asistentes + "/" + aforoStr;
              }
              else {
                vacantes = "Se ha completado el aforo del evento";
              }
            %>

            <li><strong>Vacantes disponibles: </strong><%=vacantes%></li>
            <li><strong>Entrada: </strong><%=evento.getEntradaEvento()%></li>
          </ul>

          <!-- Break -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <ul class="actions form-buttons">

                <% if (estaInscrito) { %>
                <li>
                <li>
                  <!-- Botón para desinscribirse -->
                  <a href="<%=request.getContextPath()%>/EventoUsuarioServlet?action=desinscribir&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="button big">Desinscribirme</a>
                </li>
                </li>
                <% } else { %>
                <% if (hayVacantes) { %>
                <% if (hayTraslape) { %>
                <li>
                  <button class="button big" disabled style="background-color: #b0b0b0; color:#ffffff; cursor: not-allowed;">
                    Coincide con evento inscrito
                  </button>
                </li>
                <% } else { %>
                <li>
                  <a href="#" id="openModal" class="button primary big">
                    Inscribirme
                  </a>
                </li>
                <% } %>
                <% } else { %>
                <li>
                  <button class="button big" disabled style="background-color: grey; cursor: not-allowed;">
                    No hay vacantes disponibles
                  </button>
                </li>
                <% } %>
                <% } %>


                <li><a href="<%=request.getContextPath()%>/EventoUsuarioServlet" class="button big" >Regresar</a></li>
              </ul>
            </div>
          </div>
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

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>
<!--Modal-->
<div id="modal" class="modal">
  <div class="modal-content">
    <!-- Botón cerrar -->
    <span class="close-btn">&times;</span>
    <p>¿Estás seguro de que quieres inscribirte?</p>
    <!-- Botones de Aceptar y Cancelar -->
    <ul class="actions modal-buttons">
      <li><a href="<%=request.getContextPath()%>/EventoUsuarioServlet?action=inscribir&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="button primary big" id="acceptButton">Sí</a></li>
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
      window.location.href = 'index.html';
    });

    // Función para cerrar el modal al hacer clic en el botón "Cancelar"
    cancelButton.addEventListener('click', function() {
      modal.classList.remove('show'); // Ocultar el modal
    });
  });
</script>
</body>
</html>
