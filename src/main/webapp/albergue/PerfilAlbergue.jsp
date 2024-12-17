<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<%
  Usuario perfilAlbergue = (Usuario) request.getAttribute("perfilAlbergue");
  List<PuntoAcopio> puntosAcopio = (List<PuntoAcopio>) request.getAttribute("puntosAcopio");

  if (perfilAlbergue == null) {
    response.sendRedirect("ListaAlberguesServlet?accion=listar");
    return;
  }

  Usuario usuarioSession = (Usuario) session.getAttribute("usuario");

  String fotoPerfilBase64 = usuarioSession.getFoto_perfil() != null
          ? Base64.getEncoder().encodeToString(usuarioSession.getFoto_perfil())
          : "";
  String fotoPortadaBase64 = usuarioSession.getFoto_de_portada_albergue() != null
          ? Base64.getEncoder().encodeToString(usuarioSession.getFoto_de_portada_albergue())
          : "";
  String fotoQrBase64 = usuarioSession.getImagen_qr() != null
          ? Base64.getEncoder().encodeToString(usuarioSession.getImagen_qr())
          : "";
%>

<%--
  Usuario albergue = (Usuario) session.getAttribute("usuario");
  String fotoPerfilBase64 = "";
  if (albergue != null && albergue.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
  }
--%>

<%--
  String fotoPerfilBase64 = "";
  if (perfilAlbergue != null && perfilAlbergue.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(perfilAlbergue.getFoto_perfil());
  }
--%>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/ola.css">
  <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <%--
      <header id="header">
        <img src="<%=request.getContextPath()%>/albergue/images/perfil_logo.png" class="icons">
        <h1 class="logo"><strong>MI PERFIL</strong></h1>
        <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
          <span class="ocultar"><%= usuarioSession.getNombre_albergue() != null ? usuarioSession.getNombre_albergue() : "Nombre no disponible" %></span>
          <% if (!fotoPerfilBase64.isEmpty()) { %>
          <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } else { %>
          <img src="<%=request.getContextPath()%>/albergue/images/perfil_albergue_defecto.png"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } %>
        </a>
      </header
        --%>

      <!-- Header -->
      <header id="header">
        <img src="<%=request.getContextPath()%>/albergue/images/eventos.png" class="icons">
        <h1 class="logo" style="display: inline-block;"><strong>MI PERFIL</strong></h1>
        <!-- Sección para el nombre y enlace al perfil -->
        <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
          <% if (usuarioSession.getFoto_perfil() != null) {%>
          <span class="ocultar"><%= usuarioSession.getNombre_albergue() != null ? perfilAlbergue.getNombre_albergue() : "Nombre no disponible" %></span> <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
          <% } else {%>
          <span class="ocultar"><%= usuarioSession.getNombre_albergue() != null ? perfilAlbergue.getNombre_albergue() : "Nombre no disponible" %></span> <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
          <% } %>
        </a>
      </header>

      <!-- Banner -->
      <section class="banner">
        <div class="content">
          <div class="perfil-info">
            <!-- Imagen de perfil dinámica -->
            <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil">
            <h2><%= perfilAlbergue.getNombre_albergue() %></h2>
            <div style="margin-bottom: 5px;">
              <a href="https://www.instagram.com/<%= perfilAlbergue.getUrl_instagram() %>" class="icon brands fa-instagram" target="_blank" style="font-size: 30px; margin-right: 10px; width: 30px; height: 30px;">
                <span class="label">Instagram</span>
              </a>
              <a href="https://mail.google.com/mail/?view=cm&fs=1&to=<%= perfilAlbergue.getNombre_contacto_donaciones() %>&su=Asunto&body=Mensaje%20aquí" class="icon solid fa-envelope" target="_blank" style="font-size: 30px; width: 30px; height: 30px;">
                <span class="label">Correo electrónico</span>
              </a>
            </div>
          </div>

          <!-- Galería -->
          <div class="galeria">
            <img src="data:image/png;base64,<%= perfilAlbergue.getFoto_de_portada_albergue() != null ? Base64.getEncoder().encodeToString(perfilAlbergue.getFoto_de_portada_albergue()) : "" %>" alt="Foto de portada">
          </div>

          <!-- Sección sobre nosotros -->
          <div class="seccion">
            <h3>Sobre nosotros</h3>
            <div class="seccion-contenido">
              <p><%= perfilAlbergue.getDescripcion_perfil() %></p>
            </div>
          </div>

          <div class="seccion">
            <h3>Información general</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">Nombre del encargado:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="nombres_encargado" value="<%= perfilAlbergue.getNombres_encargado() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Apellidos del encargado:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="apellidos_encargado" value="<%= perfilAlbergue.getApellidos_encargado() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Año de fundación:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="anio_creacion" value="<%= perfilAlbergue.getAnio_creacion() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Animales albergados:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="cantidad_animales" value="<%= perfilAlbergue.getCantidad_animales() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Espacio disponible para albergar más animales:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="espacio_disponible" value="<%= perfilAlbergue.getEspacio_disponible() %>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <div class="seccion">
            <h3>Ubicación</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">Dirección:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="direccion" value="<%= perfilAlbergue.getDireccion_donaciones() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Distrito:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="distrito" value="<%= perfilAlbergue.getDistrito().getNombre_distrito() %>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <!-- Información para donaciones -->
          <div class="seccion">
            <h3>Información para donaciones</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <%
                  if (puntosAcopio != null && !puntosAcopio.isEmpty()) {
                    for (int i = 0; i < puntosAcopio.size(); i++) {
                      PuntoAcopio punto = puntosAcopio.get(i);
                %>
                <div class="col-6 col-12-xsmall">Punto de acopio <%= i + 1 %>:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="punto_acopio<%= i + 1 %>" value="<%= punto.getDireccion_punto_acopio() %>" placeholder="" disabled/>
                </div>
                <%
                    }
                  }
                %>
                <div class="col-6 col-12-xsmall">Dirección de donaciones:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="direccion_donaciones" value="<%= perfilAlbergue.getDireccion_donaciones() %>" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Nombre de contacto:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="nombre_contacto" value="<%= perfilAlbergue.getNombre_contacto_donaciones() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Número de contacto:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="numero_contacto" value="<%= perfilAlbergue.getNumero_contacto_donaciones() %>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">Número de Yape o Plin:</div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="numero_yape_plin" value="<%= perfilAlbergue.getNumero_yape_plin() %>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <!-- Botones de acción -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <ul class="actions form-buttons">
                <li>
                  <a href="PerfilAlbergueServlet?accion=editar" class="button primary big">Editar perfil</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </section>

    </div>
  </div>

  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= usuarioSession.getId_usuario() %>" />
    <jsp:param name="nombreAlbergue" value="<%= usuarioSession.getNombre_albergue() %>" />
    <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
  </jsp:include>

</div>

<!-- Modal para mostrar el QR -->
<div id="qrModal" class="modal">
  <div class="modal-content">
    <span class="close">&times;</span>
    <h2>Código QR</h2>
    <img src="${pageContext.request.contextPath}/albergue/images/<%=perfilAlbergue.getNombre_logo_albergue()%>" alt="Código QR" style="width: 100%; height: auto;">
  </div>
</div>

<!-- Scripts para controlar el modal -->
<script>
  // Obtener el modal
  var modal = document.getElementById("qrModal");

  // Obtener el botón que abre el modal
  var btn = document.getElementById("verQRBtn");

  // Obtener el <span> que cierra el modal
  var span = document.getElementsByClassName("close")[0];

  // Cuando el usuario haga clic en el botón, abrir el modal
  btn.onclick = function() {
    modal.style.display = "block";
  }

  // Cuando el usuario haga clic en <span> (x), cerrar el modal
  span.onclick = function() {
    modal.style.display = "none";
  }

  // Cuando el usuario haga clic fuera del modal, cerrarlo
  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }
</script>

<!-- Estilos adicionales para el modal -->
<style>
  /* Estilo para el modal */
  .modal {
    display: none;
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0,0,0,0.4);
  }

  .modal-content {
    background-color: white;
    margin: 15% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
    max-width: 500px;
    text-align: center;
  }

  .close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
  }

  .close:hover,
  .close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
  }
</style>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/albergue/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/main.js"></script>

</body>
</html>
