<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>


<%--
  Usuario usuarioPerfil = (Usuario) request.getAttribute("usuarioPerfil");
  String fotoPerfilBase64 = "";
  if (usuarioPerfil.getFoto_perfil() != null) {
    fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuarioPerfil.getFoto_perfil());
  }
--%>


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
<!DOCTYPE HTML>
<html>
<head>
  <title>PetLink</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css">
  <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">
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

      <%--
      <!-- Header -->
      <header id="header">
        <img src="<%=request.getContextPath()%>/albergue/images/perfil_logo.png" class="icons">
        <h1 class="logo"><strong>EDITAR PERFIL</strong></h1>
        <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
          <span class="ocultar"><%= usuarioSession.getNombre_albergue() != null ? perfilAlbergue.getNombre_albergue() : "Nombre no disponible" %></span>
          <% if (!fotoPerfilBase64.isEmpty()) { %>
          <img src="data:image/png;base64,<%= fotoPerfilBase64 %>"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } else { %>
          <img src="<%=request.getContextPath()%>/albergue/images/perfil_albergue_defecto.png"
               style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
          <% } %>
        </a>
      </header>
      --%>

      <!-- Header -->
      <header id="header">
        <img src="<%=request.getContextPath()%>/albergue/images/eventos.png" class="icons">
        <h1 class="logo" style="display: inline-block;"><strong>EDITAR PERFIL</strong></h1>
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
          <header>
            <img src="${pageContext.request.contextPath}/albergue/images/form.png" class="icons">
            <h2>Datos del albergue</h2>
          </header>
          <p><strong>Complete los datos del albergue.</strong> Ingrese información detallada para que todos los usuarios puedan conocer más acerca de su albergue. Esta información será visible para todos los usuarios.</p>

          <br>

          <!-- Formulario para editar perfil -->
          <form action="${pageContext.request.contextPath}/PerfilAlbergueServlet?accion=actualizar" method="post" enctype="multipart/form-data">
            <input type="hidden" name="accion" value="actualizar" />

            <div class="row gtr-uniform">
              <div class="col-12">
                <h2>Información general del albergue</h2>
              </div>

              <!-- Nombres del encargado -->
              <div class="col-6 col-12-xsmall">
                <label for="nombres_encargado" class="input-label">Nombres del encargado</label>
                <input type="text" id="nombres_encargado" name="nombres_encargado" value="<%= perfilAlbergue.getNombres_encargado() %>" required />
              </div>

              <!-- Apellidos del encargado -->
              <div class="col-6 col-12-xsmall">
                <label for="apellidos_encargado" class="input-label">Apellidos del encargado</label>
                <input type="text" id="apellidos_encargado" name="apellidos_encargado" value="<%= perfilAlbergue.getApellidos_encargado() %>" required />
              </div>

              <!-- Nombre del albergue -->
              <div class="col-6 col-12-xsmall">
                <label for="nombre_albergue" class="input-label">Nombre del albergue</label>
                <input type="text" id="nombre_albergue" name="nombre_albergue" value="<%= perfilAlbergue.getNombre_albergue() %>" required />
              </div>

              <div class="col-6 col-12-xsmall">
                <label for="anio_creacion" class="input-label">Año de creación</label>
                <input type="text" id="anio_creacion" name="anio_creacion"
                       value="<%= perfilAlbergue.getAnio_creacion() != null ? perfilAlbergue.getAnio_creacion() : "" %>"
                       placeholder="Ingrese el año de creación (e.g., 2023)" maxlength="4" required />
              </div>

              <div class="col-12">
                <label for="descripcion_perfil" class="input-label">Sobre nosotros</label>
                <textarea name="descripcion_perfil" id="descripcion_perfil" class="text-area" maxlength="300"
                          placeholder="Ingresar una descripción sobre su albergue"><%= perfilAlbergue.getDescripcion_perfil() != null ? perfilAlbergue.getDescripcion_perfil() : "" %></textarea>
              </div>


              <!-- Cantidad de animales -->
              <div class="col-6 col-12-xsmall">
                <label for="cantidad_animales" class="input-label">Cantidad de animales albergados</label>
                <input type="text" id="cantidad_animales" name="cantidad_animales" value="<%= perfilAlbergue.getCantidad_animales() %>" />
              </div>

              <!-- Espacio disponible -->
              <div class="col-6 col-12-xsmall">
                <label for="espacio_disponible" class="input-label">Cantidad de espacios disponibles</label>
                <input type="text" id="espacio_disponible" name="espacio_disponible" value="<%= perfilAlbergue.getEspacio_disponible() %>" />
              </div>

              <!-- Foto de perfil -->
              <div class="col-12">
                <label for="foto_perfil" class="input-label">Foto de perfil</label>
                <input type="file" id="foto_perfil" name="foto_perfil" accept="image/*" />
              </div>

              <!-- Foto de portada -->
              <div class="col-12">
                <label for="foto_portada" class="input-label">Foto de portada</label>
                <input type="file" id="foto_portada" name="foto_portada" accept="image/*" />
              </div>

              <!-- Imagen QR -->
              <div class="col-12">
                <label for="imagen_qr" class="input-label">Imagen QR</label>
                <input type="file" id="imagen_qr" name="imagen_qr" accept="image/*" />
              </div>

              <!-- Dirección -->
              <div class="col-12">
                <label for="direccion_donaciones" class="input-label">Dirección del albergue</label>
                <input type="text" id="direccion_donaciones" name="direccion_donaciones" value="<%= perfilAlbergue.getDireccion_donaciones() %>" />
              </div>


            </div>

            <br>

            <!-- Puntos de acopio -->
            <div class="row gtr-uniform">
              <div class="col-12">
                <h2>Puntos de Acopio</h2>
              </div>
              <%
                int index = 0;
                for (PuntoAcopio punto : puntosAcopio) {
              %>
              <div class="col-12">
                <label for="punto_acopio_<%= index %>" class="input-label">Punto de acopio <%= index + 1 %></label>
                <input type="text" id="punto_acopio_<%= index %>" name="direccion_punto_acopio" value="<%= punto.getDireccion_punto_acopio() %>" />
              </div>
              <%
                  index++;
                }
              %>
            </div>

            <br>

            <!-- Información para donaciones -->
            <div class="row gtr-uniform">
              <div class="col-12">
                <label for="nombre_contacto_donaciones" class="input-label">Nombre del contacto para donaciones</label>
                <input type="text" id="nombre_contacto_donaciones" name="nombre_contacto_donaciones" value="<%= perfilAlbergue.getNombre_contacto_donaciones() %>" />
              </div>

              <div class="col-6 col-12-xsmall">
                <label for="numero_contacto_donaciones" class="input-label">Número del contacto</label>
                <input type="text" id="numero_contacto_donaciones" name="numero_contacto_donaciones" value="<%= perfilAlbergue.getNumero_contacto_donaciones() %>" />
              </div>

              <div class="col-6 col-12-xsmall">
                <label for="numero_yape_plin" class="input-label">Número de Yape o Plin</label>
                <input type="text" id="numero_yape_plin" name="numero_yape_plin" value="<%= perfilAlbergue.getNumero_yape_plin() %>" />
              </div>
            </div>

            <br>

            <!-- Redes sociales -->
            <div class="row gtr-uniform">
              <div class="col-12">
                <label for="url_instagram" class="input-label">Instagram</label>
                <input type="text" id="url_instagram" name="url_instagram" value="<%= perfilAlbergue.getUrl_instagram() %>" />
              </div>

              <div class="col-12">
                <ul class="actions form-buttons">
                  <li><button type="submit" class="button primary big">Guardar Cambios</button></li>
                  <li><a href="${pageContext.request.contextPath}/PerfilAlbergueServlet?accion=ver" class="button big">Cancelar</a></li>
                </ul>
              </div>
            </div>
          </form>



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

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/albergue/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/main.js"></script>

<!-- Modal -->
<div id="modal" class="modal">
  <div class="modal-content">
    <p>Su perfil se ha cambiado con éxito</p>
    <ul class="actions modal-buttons">
      <li><a href="perfil.html" class="button primary big" id="acceptButton">Aceptar</a></li>
    </ul>
  </div>
</div>

<!-- Validaciones -->
<script>
  // Función para validar que solo se ingresen números en un campo
  function validarSoloNumeros(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
  }

  // Función para validar el año de creación (4 dígitos)
  function validarAnioCreacion(input) {
    if (input.value.length > 4) {
      input.value = input.value.slice(0, 4);
    }
  }

  // Función para validar que el número de contacto y Yape/Plin tenga 9 dígitos
  function validarNumero9Digitos(input) {
    input.value = input.value.replace(/[^0-9]/g, '');
    if (input.value.length > 9) {
      input.value = input.value.slice(0, 9);
    }
  }

  // Función para validar la URL del campo Instagram
  function validarUrl(input) {
    const urlRegex = /^(https?:\/\/)?(www\.)?instagram\.com\/[a-zA-Z0-9(@_.?=)]{1,}$/;
    if (!urlRegex.test(input.value)) {
      alert("Por favor ingrese una URL válida para Instagram.");
      input.value = '';
    }
  }

  // Aplicar validaciones cuando se escriba en los campos
  document.getElementById('cantidad_animales').addEventListener('input', function() {
    validarSoloNumeros(this);
  });

  document.getElementById('espacio_disponible').addEventListener('input', function() {
    validarSoloNumeros(this);
  });

  document.getElementById('anio_creacion').addEventListener('input', function() {
    validarSoloNumeros(this);
    validarAnioCreacion(this);
  });

  document.getElementById('numero_contacto_donaciones').addEventListener('input', function() {
    validarNumero9Digitos(this);
  });

  document.getElementById('numero_yape_plin').addEventListener('input', function() {
    validarNumero9Digitos(this);
  });

  document.getElementById('url_instagram').addEventListener('blur', function() {
    validarUrl(this);
  });
</script>

<!-- Contador de caracteres para el campo "Sobre nosotros" -->
<script>
  const textarea = document.getElementById('sobre_nosotros');
  const charCount = document.getElementById('charCount');

  textarea.addEventListener('input', function() {
    const currentLength = textarea.value.length;
    charCount.textContent = `${currentLength}/300`;
  });
</script>
</body>
</html>

