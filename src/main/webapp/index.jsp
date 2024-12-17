<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 6/11/2024
  Time: 08:19
  To change this template use File | Settings | File Templates.
--%>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Inicia sesión en PetLink</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/login_assets/css/bienvenido.css">
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
  <title>Recupera tu cuenta</title>
  <link rel="icon" href="login_images/favicon.png" type="image/x-icon">
</head>
<body>
<div class="contenedor_principal">
  <div class="lado_izquierdo">
    <div class="recuadro-imagen-texto">
      <img src="login_images/imagen_registro_mejorado.jpg" alt="Perros jugando" class="imagen_main">
      <div class="mensaje_izquierda">
        <h1>Bienvenido a PetLink</h1>
        <p>Una red social para ayudar animales</p>
      </div>
    </div>
  </div>

  <div class="lado_derecho">
    <h2>Inicia sesión en PetLink</h2>
    <form id="login-form" action="LoginServlet?action=login" method="post">
      <div class="form-group">
        <label for="email">Ingresa tu correo electrónico</label>
        <div class="input-container">
          <i class="fas fa-envelope"></i>
          <input type="email" id="email" name="email" placeholder="Correo electrónico" required>
        </div>
      </div>
      <div class="form-group">
        <label for="password">Ingresa tu contraseña</label>
        <div class="input-container">
          <i class="fas fa-lock"></i>
          <input type="password" id="password" name="password" placeholder="Contraseña" required>
        </div>
      </div>
      <%-- Mensaje de error --%>
      <% String loginError = (String) request.getAttribute("loginError"); %>
      <% if (loginError != null) { %>
      <div class="error-message" style="color: crimson; font-size: smaller"><%= loginError %></div>
      <% } %>
      <div class="contenedor_olvide"><a href="<%=request.getContextPath()%>/recupera_contrasenia.jsp" id="olvideContrasenaBtn" class="olvide_contrasena">¿Olvidaste tu contraseña?</a></div>
      <button type="submit" class="login-button" id="loginButton">Iniciar sesión</button>
      <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <p class="registro-texto">¿No tienes una cuenta? ¡Regístrate!</p>
    </form>

    <form action="RegisterServlet" method="get">
      <input type="hidden" name="action" value="usuarioFinal">
      <button id="registrarseBtn" class="registro-usuario" type="submit">
        <i class="fas fa-users"></i> Registrarse como usuario
      </button>
    </form>

    <form action="RegisterServlet" method="get">
      <input type="hidden" name="action" value="albergue">
      <button id="registrarseAlbergueBtn" class="registro-albergue" type="submit">
        <i class="fas fa-home"></i> Registrarse como albergue
      </button>
    </form>

  </div>
</div>
</body>
</html>
