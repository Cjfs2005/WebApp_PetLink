<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 11/12/2024
  Time: 13:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% String estado = request.getAttribute("estado") != null ? (String) request.getAttribute("estado"): "espera"; %>

<!DOCTYPE HTML>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de usuarios</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/login_assets/css/bienvenido.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/login_assets/css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="icon" href="<%=request.getContextPath()%>/login_images/favicon.png" type="image/x-icon">
</head>

<body>
    <!-- Encabezado con logo y botón de inicio de sesión -->

    <!-- Contenedor principal -->
    <div class="contenedor_principal">
        <!-- Lado izquierdo con imagen y texto -->
        <div class="lado_izquierdo" style="align-self:flex-start;">
            <div class="recuadro-imagen-texto">
                <div class="recuadro-imagen">
                    <img src="login_images/imagen_registro_mejorado.jpg" alt="Perros jugando" class="imagen_main">
                </div>
                <div class="mensaje_izquierda">
                    <h1>Empieza a ayudar</h1>
                    <p style="margin-bottom: 49px;">Una red social para ayudar animales</p>
                </div>
            </div>
        </div>

        <div class="lado_derecho">
            <h2>Regístrate como usuario</h2>

            <!-- Formulario de registro -->

            <form id="login-form" action="RegisterServlet?action=registroUsuario" method="post">
                <div class="form-group">
                    <label for="nombre">Nombres</label>
                    <input type="text" id="nombre" name="nombre" maxlength="45" required>
                </div>
                <div class="form-group">
                    <label for="apellido">Apellidos</label>
                    <input type="text" id="apellido" name="apellido" maxlength="45" required>
                </div>
                <div class="form-group">
                    <label for="dni">Número de DNI</label>
                    <input type="text" id="dni" name="dni" maxlength="8" required>
                </div>
                <div class="form-group">
                    <label for="distrito">Distrito</label>
                    <select id="distrito" name="distrito" required>
                        <option value="" disabled selected>Distrito</option>
                        <!-- Norte -->
                        <option value="1">Ancon</option>
                        <option value="2">Santa Rosa</option>
                        <option value="3">Carabayllo</option>
                        <option value="4">Puente Piedra</option>
                        <option value="5">Comas</option>
                        <option value="6">Los Olivos</option>
                        <option value="7">San Martín de Porres</option>
                        <option value="8">Independencia</option>

                        <!-- Sur -->
                        <option value="9">San Juan de Miraflores</option>
                        <option value="10">Villa María del Triunfo</option>
                        <option value="11">Villa el Salvador</option>
                        <option value="12">Pachacamac</option>
                        <option value="13">Lurin</option>
                        <option value="14">Punta Hermosa</option>
                        <option value="15">Punta Negra</option>
                        <option value="16">San Bartolo</option>
                        <option value="17">Santa María del Mar</option>
                        <option value="18">Pucusana</option>

                        <!-- Este -->
                        <option value="19">San Juan de Lurigancho</option>
                        <option value="20">Lurigancho/Chosica</option>
                        <option value="21">Ate</option>
                        <option value="22">El Agustino</option>
                        <option value="23">Santa Anita</option>
                        <option value="24">La Molina</option>
                        <option value="25">Cieneguilla</option>

                        <!-- Oeste -->
                        <option value="26">Rimac</option>
                        <option value="27">Cercado de Lima</option>
                        <option value="28">Breña</option>
                        <option value="29">Pueblo Libre</option>
                        <option value="30">Magdalena</option>
                        <option value="31">Jesus María</option>
                        <option value="32">La Victoria</option>
                        <option value="33">Lince</option>
                        <option value="34">San Isidro</option>
                        <option value="35">San Miguel</option>
                        <option value="36">Surquillo</option>
                        <option value="37">San Borja</option>
                        <option value="38">Santiago de Surco</option>
                        <option value="39">Barranco</option>
                        <option value="40">Chorrillos</option>
                        <option value="41">San Luis</option>
                        <option value="42">Miraflores</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="direccion">Dirección</label>
                    <input type="text" id="direccion" name="direccion" maxlength="100" required>
                </div>
                <div class="form-group">
                    <label for="email">Correo electrónico (Gmail)</label>
                    <input type="email" id="email" name="email" maxlength="100" required>
                </div>
                <div class="contenedor_olvide"><a href="<%=request.getContextPath()%>/index.jsp" id="olvideContrasenaBtn" class="olvide_contrasena">¿Ya tienes una cuenta?</a></div>
                <button type="submit" class="login-button">Registrarme</button>
            </form>
        </div>
    </div>

    <!-- Overlay para oscurecer el fondo -->
    <div id="overlay" style="display: none;"></div>

    <!-- Modal de notificación -->
    <div id="notificationModal" style="display: none;">
        <!-- Icono de campana -->
        <i class="fas fa-bell"></i>

        <!-- Botón para cerrar en la parte superior derecha -->
        <span id="closeModal">&times;</span>

        <%
            String titulo;
            String mensaje;

            switch (estado){

                case "errorFormato":
                    mensaje = "Alguno de los datos ingresados en su solicitud no cumple con el formato, vuelva a intentarlo.";
                    titulo = "Registro Fallido";
                    break;
                case "errorRegistroExistente":
                    mensaje = "Ya existe una cuenta con ese DNI o correo electronico, intente con otros datos o inicie sesión.";
                    titulo = "Registro Fallido";
                    break;
                default:
                    mensaje = "Su solicitud de registro ha sido enviada satisfactoriamente.\n" +
                            " Pronto recibirá un correo de aceptación indicándole los pasos a seguir para completar su registro.";
                    titulo = "Registro Completado";
            }
        %>

        <h3>
            <%=titulo%>
        </h3>

        <p>
            <%= mensaje %>
        </p>
        <button id="acceptBtn">Aceptar</button>
    </div>

    <script>
        // Obtener los elementos del DOM
        const overlay = document.getElementById('overlay');
        const modal = document.getElementById('notificationModal');
        const closeButton = document.getElementById('closeModal');
        const acceptButton = document.getElementById('acceptBtn');

        // Función para mostrar el modal
        function showModal() {
            overlay.style.display = 'block';
            modal.style.display = 'block';
        }

        // Función para ocultar el modal
        function hideModal() {
            overlay.style.display = 'none';
            modal.style.display = 'none';
        }

        // Mostrar el modal según el valor de "estado"
        <% if (estado != "espera") { %>
        showModal();
        <% } %>

        // Cerrar el modal al hacer clic en el botón de cerrar
        closeButton.addEventListener('click', hideModal);

        // Cerrar el modal al hacer clic en el botón de aceptar
        acceptButton.addEventListener('click', hideModal);
    </script>

</body>

</html>
