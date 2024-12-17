<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 14/12/2024
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cambia tu contraseña</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/login_assets/css/recuperar_contrasena.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="icon" href="<%=request.getContextPath()%>/login_images/favicon.png" type="image/x-icon">
</head>
<body>
    <div class="contenedor_recuperar">
        <div class="imagen_lado_izquierdo">
            <img src="<%=request.getContextPath()%>/login_images/Recuperar_contrasena.jpg" alt="Imagen de recuperación">
        </div>
        <div class="formulario_recuperar">
            <div class="botones">
                <a href="<%=request.getContextPath()%>/index.jsp" class="iniciar_sesion">Iniciar sesión</a>
            </div>
            <h2>Cambia tu contraseña</h2>
            <p>Ingresa con tu correo electrónico y con la contraseña temporal enviada a este para proceder con el cambio de contraseña.</p>
            <form id="change-password-form" action="CambiarPasswordServlet?action=cambiar" method="post">
                <div class="form-group">
                    <input type="email" id="email" name="email" placeholder="Ingresa tu correo" required>
                </div>
                <div class="form-group">
                    <input type="password" id="temp-password" name="temp-password" placeholder="Ingresa tu contraseña temporal" required>
                </div>
                <button type="submit" class="boton_aceptar">Ingresar</button>
            </form>
        </div>
    </div>

    <script src="<%=request.getContextPath()%>/login_assets/js/recuperar_contrasena.js"></script>

</body>
</html>
