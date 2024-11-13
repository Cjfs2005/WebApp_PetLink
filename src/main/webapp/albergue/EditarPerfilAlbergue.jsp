<%--
  Created by IntelliJ IDEA.
  User: GIANFRANCO
  Date: 12/11/2024
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="perfilAlbergue" class="com.example.webapp_petlink.beans.Usuario" scope="request" />
<jsp:useBean id="puntosAcopio" class="java.util.ArrayList" scope="request" />



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

      <!-- Header -->
      <header id="header">
        <h1 class="logo"><strong>EDITAR PERFIL</strong></h1>
        <!-- Sección para el nombre y enlace al perfil -->
        <a href="perfil.html" class="user-profile">
          <span class="ocultar">Huellitas PUCP</span> <img src="${pageContext.request.contextPath}/albergue/images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;"></img>
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

          <form action="PerfilAlbergueServlet" method="post">
            <input type="hidden" name="accion" value="actualizar" />

          <div class="row gtr-uniform">
            <div class="col-12">
              <h2>Información general del albergue</h2>
            </div>

            <!-- Nombres del encargado -->
            <div class="col-6 col-12-xsmall">
              <label for="nombres_encargado" class="input-label">Nombres del encargado</label>
              <input type="text" id="nombres_encargado" name="nombres_encargado" value="${perfilAlbergue.nombres_encargado}" required/>
            </div>

            <!-- Apellidos del encargado -->
            <div class="col-6 col-12-xsmall">
              <label for="apellidos_encargado" class="input-label">Apellidos del encargado</label>
              <input type="text" id="apellidos_encargado" name="apellidos_encargado" value="${perfilAlbergue.apellidos_encargado}" required/>
            </div>

            <!-- Nombre del albergue -->
            <div class="col-6 col-12-xsmall">
              <label for="nombre_albergue" class="input-label">Nombre del albergue</label>
              <input type="text" id="nombre_albergue" name="nombre_albergue" value="${perfilAlbergue.nombre_albergue}" required/>
            </div>

            <!-- Otros campos de perfil -->
            <div class="col-6 col-12-xsmall">
              <label for="cantidad_animales" class="input-label">Cantidad de animales albergados</label>
              <input type="text" id="cantidad_animales" name="cantidad_animales" value="${perfilAlbergue.cantidad_animales}" placeholder="" />
            </div>

            <!-- Espacio disponible -->
            <div class="col-6 col-12-xsmall">
              <label for="espacio_disponible" class="input-label">Cantidad de espacios disponibles para nuevos animales</label>
              <input type="text" id="espacio_disponible" name="espacio_disponible" value="${perfilAlbergue.espacio_disponible}" placeholder="" />
            </div>

            <!-- Distrito -->
            <div class="col-6 col-12-xsmall">
              <label for="distrito" class="input-label">Distrito</label>
              <select id="distrito" name="id_distrito">
                <option value="">Seleccione su distrito</option>
                <option value="1" ${perfilAlbergue.distrito.id_distrito == 1 ? 'selected' : ''}>Ancon</option>
                <option value="2" ${perfilAlbergue.distrito.id_distrito == 2 ? 'selected' : ''}>Santa Rosa</option>
                <option value="3" ${perfilAlbergue.distrito.id_distrito == 3 ? 'selected' : ''}>Carabayllo</option>
              </select>
            </div>

            <!-- Otros campos -->
            <div class="col-12">
              <label for="direccion" class="input-label">Dirección del albergue</label>
              <input type="text" id="direccion" name="direccion_donaciones" value="${perfilAlbergue.direccion_donaciones}" />
            </div>

            <div class="col-12">
              <label for="sobre_nosotros" class="input-label">Sobre nosotros</label>
              <textarea id="sobre_nosotros" name="descripcion_perfil" class="text-area" maxlength="300" placeholder="Ingresar una descripción sobre su albergue">${perfilAlbergue.descripcion_perfil}</textarea>
            </div>
          </div>

          <br>

          <!-- Información para donaciones -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <h2>Información para donaciones</h2>
            </div>

            <!-- Puntos de acopio -->
            <div class="col-12">
              <label for="punto_acopio1" class="input-label">Punto de acopio 1</label>
              <input type="text" id="punto_acopio1" name="direccion_punto_acopio" value="${puntosAcopio[0].direccion_punto_acopio}" placeholder="" />
            </div>

            <div class="col-12">
              <label for="punto_acopio2" class="input-label">Punto de acopio 2</label>
              <input type="text" id="punto_acopio2" name="direccion_punto_acopio" value="${puntosAcopio[1].direccion_punto_acopio}" placeholder="" />
            </div>

            <div class="col-12">
              <label for="punto_acopio3" class="input-label">Punto de acopio 3</label>
              <input type="text" id="punto_acopio3" name="direccion_punto_acopio" value="${puntosAcopio[2].direccion_punto_acopio}" placeholder="" />
            </div>

            <div class="col-12">
              <label for="punto_acopio4" class="input-label">Punto de acopio 4</label>
              <input type="text" id="punto_acopio4" name="direccion_punto_acopio" value="${puntosAcopio[3].direccion_punto_acopio}" placeholder="" />
            </div>

            <!-- Dirección de donaciones -->
            <div class="col-12">
              <label for="direccion_donaciones" class="input-label">Dirección de donaciones</label>
              <input type="text" id="direccion_donaciones" name="direccion_donaciones" value="${perfilAlbergue.direccion_donaciones}" />
            </div>

            <!-- Nombre del contacto -->
            <div class="col-12">
              <label for="nombre_contacto_donaciones" class="input-label">Nombre del contacto para donaciones</label>
              <input type="text" id="nombre_contacto_donaciones" name="nombre_contacto_donaciones" value="${perfilAlbergue.nombre_contacto_donaciones}" placeholder="" />
            </div>

            <div class="col-6 col-12-xsmall">
              <label for="numero_contacto_donaciones" class="input-label">Número del contacto para donaciones</label>
              <input type="text" id="numero_contacto_donaciones" name="numero_contacto_donaciones" value="${perfilAlbergue.numero_contacto_donaciones}" placeholder="" />
            </div>

            <div class="col-6 col-12-xsmall">
              <label for="numero_yape_plin" class="input-label">Número de Yape o Plin</label>
              <input type="text" id="numero_yape_plin" name="numero_yape_plin" value="${perfilAlbergue.numero_yape_plin}" placeholder="" />
            </div>
          </div>

          <br>

          <!-- Redes sociales -->
          <div class="row gtr-uniform">
            <div class="col-12">
              <label for="url_instagram" class="input-label">Instagram</label>
              <input type="text" id="url_instagram" name="url_instagram" value="${perfilAlbergue.url_instagram}" placeholder="" />
            </div>

            <div class="col-12">
              <ul class="actions form-buttons">
                <!-- Botón "Aceptar" - Actualiza los datos en la base de datos -->
                <li>
                  <form action="PerfilAlbergueServlet" method="post">
                    <input type="hidden" name="accion" value="actualizar" />
                    <button type="submit" class="button primary big">Aceptar</button>
                  </form>
                </li>

                <!-- Botón "Cancelar" - Redirige a la vista del perfil sin realizar cambios -->
                <li>
                  <a href="PerfilAlbergueServlet?accion=ver" class="button big">Cancelar</a>
                </li>
              </ul>
            </div>
          </div>

          </form>

        </div>
      </section>


    </div>
  </div>

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

