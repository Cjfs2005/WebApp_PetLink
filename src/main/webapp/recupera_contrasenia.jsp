<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 14/12/2024
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <!-- Archivo CSS -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/login_assets/css/recuperar_contrasena.css">
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
  <title>Recupera tu contraseña</title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
  <link rel="icon" href="<%=request.getContextPath()%>/login_images/favicon.png" type="image/x-icon">
</head>
<body>
  <!-- Contenedor principal -->
  <div class="contenedor_recuperar">
    <!-- Imagen lado izquierdo -->
    <div class="imagen_lado_izquierdo">
      <img src="<%=request.getContextPath()%>/login_images/Recuperar_contrasena.jpg" alt="Imagen de recuperación">
    </div>
    <div class="formulario_recuperar">
      <div class="botones">
        <a href="<%=request.getContextPath()%>/index.jsp" class="iniciar_sesion">Iniciar sesión</a>
      </div>
      <h2>Recupera tu contraseña</h2>
      <p>¿Olvidaste tu contraseña?</p>
      <p>Ingresa el correo electrónico con el que te registraste para iniciar con la recuperación de cuenta.</p>
      <form id="recover-form" action="CambiarPasswordServlet?action=recuperar" method="post">
        <div class="form-group">
          <label for="email">Ingresa tu correo</label>
          <input type="email" id="email" placeholder="correo@ejemplo.com" required name="email">
        </div>
        <button type="submit" class="boton_aceptar">Aceptar</button>
      </form>
    </div>
  </div>

  <!-- Overlay para oscurecer el fondo -->
  <div id="overlay"></div>

  <!-- Modal de notificación -->
  <div id="notificationModal">

    <h3>Su cuenta ha sido validada</h3>
    <p>Pronto recibirá un correo indicándole los pasos a seguir para cambiar su contraseña.</p>
    <a id="acceptBtn" href="<%=request.getContextPath()%>/index.jsp">Aceptar</a>
  </div>

</body>
</html>
