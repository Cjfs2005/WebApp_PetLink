<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 14/12/2024
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cambia tu Contraseña</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/login_assets/css/recuperar_contrasena.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link rel="icon" href="<%=request.getContextPath()%>/login_images/favicon.png" type="image/x-icon">
</head>
<body>
    <div class="contenedor_recuperar">
        <div class="imagen_lado_izquierdo">
            <img src="<%=request.getContextPath()%>/login_images/Recuperar_contrasena.jpg" alt="Imagen de recuperación" class="imagen_recuperar">
        </div>
        <div class="formulario_recuperar" style="flex-grow: 1; width: 100%;">
            <div class="botones">
                <a href="<%=request.getContextPath()%>/index.jsp" class="iniciar_sesion">Iniciar sesión</a>
            </div>
            <h2>Cambia tu contraseña</h2>
            <p>Cambia de contraseña</p>
            <form id="change-password-form" action="CambiarPasswordServlet?action=confirmar" method="post">
                <div class="form-group">
                    <input type="password" id="new-password" name="new-password" placeholder="Nueva contraseña" required>
                </div>
                <div class="form-group">
                    <input type="password" id="repeat-password" name="repeat-password" placeholder="Repetir nueva contraseña" required>
                </div>
                <button type="submit" class="boton_aceptar">Aceptar</button>
            </form>
        </div>
    </div>

</body>
</html>
