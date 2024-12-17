<%--
  Created by IntelliJ IDEA.
  User: GIANFRANCO
  Date: 12/11/2024
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.List" %>

<%@ page import="java.util.Base64" %>


<%
  // Obtener el usuario desde la sesión
  Usuario usuario = (Usuario) session.getAttribute("usuario");

  // Obtener el perfil del albergue desde la sesión
  Usuario perfilAlbergue = (Usuario) session.getAttribute("perfilAlbergue");
  if (perfilAlbergue == null) {
    response.sendRedirect("ListaAlberguesServlet?accion=listar");
    return;
  }


  List<PuntoAcopio> puntosAcopio = (List<PuntoAcopio>) session.getAttribute("puntosAcopio");
  if (puntosAcopio == null) {
    response.sendRedirect("ListaAlberguesServlet?accion=listar");
    return;
  }

  String fotoPerfilBase64 = "";
  if (usuario != null && usuario.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
  }
%>


<%-- Importar las clases necesarias --%>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="java.util.List" %>
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/ola.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/popup-window.css">
  <link rel="icon" href="${pageContext.request.contextPath}/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <header id="header">
        <img src="<%=request.getContextPath()%>/usuarioFinal/images/hogarTemporal.png" class="icons">
        <h1 class="logo"><strong>HOGAR TEMPORAL</strong></h1>
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
          <div class="perfil-info">
            <img src="${pageContext.request.contextPath}/albergue/images/logo_huellitas.png" alt="Foto de perfil">
            <h2><%=perfilAlbergue.getNombre_albergue()%></h2>
            <div style="margin-bottom: 5px;">
              <a href="https://www.instagram.com/<%=perfilAlbergue.getUrl_instagram()%>" class="icon brands fa-instagram" target="_blank" style="font-size: 30px; margin-right: 10px; width: 30px; height: 30px;">
                <span class="label">Instagram</span>
              </a>
              <a href="https://mail.google.com/mail/?view=cm&fs=1&to=<%=perfilAlbergue.getNombre_contacto_donaciones()%>&su=Asunto&body=Mensaje%20aquí" class="icon solid fa-envelope" target="_blank" style="font-size: 30px; width: 30px; height: 30px;">
                <span class="label">Correo electrónico</span>
              </a>
            </div>
          </div>

          <!-- Galería -->
          <div class="galeria">
            <img src="${pageContext.request.contextPath}/albergue/images/portada_huellitas.png" alt="Foto de portada"> <!-- Session -->
          </div>

          <!-- Sección sobre nosotros -->
          <div class="seccion">
            <h3>Sobre nosotros</h3>
            <div class="seccion-contenido">
              <p><%=perfilAlbergue.getDescripcion_perfil()%></p>
            </div>
          </div>

          <div class="seccion">
            <h3>Información general</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">
                  Nombre del encargado:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="nombres_encargado" value="<%=perfilAlbergue.getNombres_encargado()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Apellidos del encargado:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="apellidos_encargado" value="<%=perfilAlbergue.getApellidos_encargado()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Año de fundación:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="anio_creacion" value="<%=perfilAlbergue.getAnio_creacion()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Animales albergados:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="cantidad_animales" value="<%=perfilAlbergue.getCantidad_animales()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Espacio disponible para albergar más animales:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="espacio_disponible" value="<%=perfilAlbergue.getEspacio_disponible()%>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <div class="seccion">
            <h3>Ubicación</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <div class="col-6 col-12-xsmall">
                  Dirección:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="direccion" value="<%=perfilAlbergue.getDireccion_donaciones()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Distrito:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="distrito" value="<%=perfilAlbergue.getDistrito().getNombre_distrito()%>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <div class="seccion">
            <h3>Información para donaciones</h3>
            <div class="seccion-contenido">
              <div class="row gtr-uniform">
                <%
                  // Verifica si la lista de puntos de acopio no está vacía
                  if (puntosAcopio != null && !puntosAcopio.isEmpty()) {
                    int i = 0; // Contador para iterar sobre los puntos de acopio
                    for (PuntoAcopio punto : puntosAcopio) {
                %>
                <div class="col-6 col-12-xsmall">
                  <label for="punto_acopio<%= i + 1 %>">Punto de acopio <%= i + 1 %>:</label>
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="punto_acopio<%= i + 1 %>" value="<%= punto.getDireccion_punto_acopio() %>" placeholder="" disabled/>
                </div>
                <%
                    i++; // Incrementa el contador para el siguiente punto de acopio
                  }
                } else {
                %>
                <div class="col-12-xlarge">
                  <label>No hay puntos de acopio disponibles</label>
                </div>
                <%
                  }
                %>


                <div class="col-6 col-12-xsmall">
                  Dirección de donaciones:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="direccion_donaciones" value="<%=perfilAlbergue.getDireccion_donaciones()%>" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Nombre de contacto:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="nombre_contacto" value="<%=perfilAlbergue.getNombre_contacto_donaciones()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Número de contacto:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="numero_contacto" value="<%=perfilAlbergue.getNumero_contacto_donaciones()%>" placeholder="" disabled/>
                </div>

                <div class="col-6 col-12-xsmall">
                  Número de Yape o Plin:
                </div>
                <div class="col-6 col-12-xsmall">
                  <input type="text" id="numero_yape_plin" value="<%=perfilAlbergue.getNumero_yape_plin()%>" placeholder="" disabled/>
                </div>
              </div>
            </div>
          </div>

          <div class="row gtr-uniform">
            <div class="col-12">
              <ul class="actions form-buttons">
                <li>
                  <%-- Gianfranco: <a href="ListaAlberguesServlet?accion=donar&id=<%=perfilAlbergue.getId_usuario()%>" class="button primary big">Donar dinero</a>--%>
                    <a href="<%=request.getContextPath()%>/ListaAlberguesServlet?accion=donar" class="button primary big">Donar dinero</a>
                </li>
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
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/usuarioFinal/assets/js/main.js"></script>

</body>
</html>
