<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.Base64" %>
<%
  Usuario albergue = (Usuario) session.getAttribute("usuario");
  String fotoPerfilBase64 = "";
  if (albergue != null && albergue.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64 .getEncoder().encodeToString(albergue.getFoto_perfil());
  };

  PublicacionMascotaAdopcion publicacion = (PublicacionMascotaAdopcion) request.getAttribute("publicacion");
%>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>

  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css"/>
  <link rel="icon" href="images/favicon.png" type="image/x-icon">

  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
  <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
  <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
  <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">
<div id="wrapper">
  <div id="main">
    <div class="inner">
      <header id="header">
        <h1 class="logo"><strong>Detalles de la adopción</strong></h1>
        <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
          <span class="ocultar"><%= albergue.getNombre_albergue() != null ? albergue.getNombre_albergue() : "Nombre no disponible" %></span>
          <% if (!fotoPerfilBase64.isEmpty()) { %>
          <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } else { %>
          <img src="<%=request.getContextPath()%>/albergue/images/default_profile.png"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } %>
        </a>
      </header>

      <section class="banner">
        <div class="content">
          <header>
            <img src="images/form.png" class="icons">
            <h2>Adopta a <%= publicacion.getNombreMascota() %></h2>
          </header>

          <div class="contenedor-imagenes">
            <img src="data:image/png;base64,<%= Base64 .getEncoder().encodeToString(publicacion.getFotoMascota())%>" alt="Imagen de la mascota">
          </div>

          <p><strong>Descripción:</strong> <%= publicacion.getDescripcionMascota()%></p>
          <p><strong>Información General:</strong></p>
          <ul>
            <li><strong>Raza:</strong> <%= publicacion.getTipoRaza()%></li>
            <li><strong>Edad aproximada:</strong> <%=publicacion.getEdadAproximada()%></li>
            <li><strong>Género: </strong><%=publicacion.getGeneroMascota()%></li>
            <li><strong>Lugar de encuentro: </strong><%=publicacion.getLugarEncontrado()%></li>
            <li><strong>Se encuentra en un hogar temporal: </strong><%=publicacion.isEstaEnTemporal()?"Sí":"No"%></li>
          </ul>

          <p><strong>Condiciones para la adopción:</strong> <%= publicacion.getCondicionesAdopcion()%></p>
          <%if (publicacion.getAdoptante().getNombres_usuario_final() == null){%>
          <p><strong>Postulantes:</strong></p>
          <div class="table-responsive">
            <table id="example" class="table table-striped" style="width:100%;">
              <thead>
              <tr>
                <th>DNI</th>
                <th>Nombres y apellidos</th>
                <th>Distrito</th>
                <th>Acciones</th>
              </tr>
              </thead>
              <tbody>
              <%
                ArrayList<PostulacionMascotaAdopcion> postulaciones = (ArrayList<PostulacionMascotaAdopcion>) request.getAttribute("postulaciones");
                if (postulaciones != null) {
                  for (PostulacionMascotaAdopcion postulacion : postulaciones) { %>
              <tr>
                <td><%= postulacion.getUsuarioFinal().getDni()%></td>
                <td><%= postulacion.getUsuarioFinal().getNombres_usuario_final() + " " + postulacion.getUsuarioFinal().getApellidos_usuario_final() %></td>
                <td><%= postulacion.getUsuarioFinal().getDistrito().getNombre_distrito() %></td>
                <td>
                  <ul class="icons">
                    <li>
                      <form action="AdopcionesAlbergueServlet" method="POST" style="margin-bottom: 0;">
                        <input type="hidden" name="action" value="aceptarPostulante">
                        <input type="hidden" name="idPostulacion" value="<%= postulacion.getIdPostulacionMascotaAdopcion() %>">
                        <input type="hidden" name="idPublicacion" value="<%= publicacion.getIdPublicacionMascotaAdopcion() %>">
                        <input type="hidden" name="idAdoptante" value="<%= postulacion.getUsuarioFinal().getId_usuario()%>">
                        <a href="#" onclick="if (confirm('¿Estás seguro de que quieres aceptar al postulante <%=postulacion.getUsuarioFinal().getNombres_usuario_final()%> <%=postulacion.getUsuarioFinal().getApellidos_usuario_final()%>?')) { this.closest('form').submit(); } return false;" class="icon fas fa-check-circle">
                          <span class="label">Aceptar</span>
                        </a>
                      </form>
                      <!--<a href="#" class="icon fas fa-check-circle"><span class="label">Aceptar</span></a>-->
                    </li>
                  </ul>
                </td>
              </tr>
              <%   }
              } %>
              </tbody>
            </table>
            <script>
              new DataTable('#example', {
                language: {
                  sSearch: "Buscar:",
                  sLengthMenu: "Mostrar _MENU_ registros",
                  sZeroRecords: "No se encontraron resultados",
                  sEmptyTable: "Ningún dato disponible en esta tabla",
                  sInfo: "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                  sInfoEmpty: "Mostrando registros del 0 al 0 de un total de 0 registros",
                  sInfoFiltered: "(filtrado de un total de _MAX_ registros)",
                  sLoadingRecords: "Cargando...",
                }
              });
            </script>
          </div>
          <%} else{%>
          <p><strong>Adoptante:</strong> <%= publicacion.getAdoptante().getNombres_usuario_final()%> <%= publicacion.getAdoptante().getApellidos_usuario_final()%></p>
          <%}%>
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

<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    const openModalButton = document.getElementById('openModal');
    const modal = document.getElementById('modal');
    const closeModalButton = document.querySelector('.close-btn');
    const cancelButton = document.getElementById('cancelButton');

    openModalButton.addEventListener('click', function () {
      modal.classList.add('show');
    });

    closeModalButton.addEventListener('click', function () {
      modal.classList.remove('show');
    });

    cancelButton.addEventListener('click', function () {
      modal.classList.remove('show');
    });
  });
</script>
</body>
</html>

