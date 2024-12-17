<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>

<%
    // Obtener el usuario desde la sesión
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("LoginServlet");
        return;
    }

    // Obtener el perfil del albergue desde la sesión
    Usuario perfilAlbergue = (Usuario) session.getAttribute("perfilAlbergue");
    if (perfilAlbergue == null) {
        response.sendRedirect("ListaAlberguesServlet?accion=listar");
        return;
    }

    String fotoPerfilBase64 = "";
    if (usuario != null && usuario.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
    }
%>



<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/usuarioFinal/assets/css/aditional.css">
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
                    <header>
                        <img src="${pageContext.request.contextPath}/usuarioFinal/images/form.png" class="icons">
                        <h2>Complete el siguiente formulario</h2>
                    </header>
                    <p style="margin-bottom: 1.5em;"><strong>Albergue beneficiario:</strong> <%= perfilAlbergue.getNombre_albergue() %> </p>


                    <form action="${pageContext.request.contextPath}/ListaAlberguesServlet?accion=donacion" method="post" enctype="multipart/form-data" style="margin: 0;">
                    <!--<form action="${pageContext.request.contextPath}/ListaAlberguesServlet?accion=donacion" method="post">-->

                        <div class="row gtr-uniform">

                            <div class="col-6 col-12-xsmall">
                                <label for="monto" class="input-label">Ingresa el monto donado (S/.)</label>
                                <input type="text" id="monto" name="monto" placeholder="Monto en soles" maxlength="10" inputmode="numeric" required />
                            </div>
                            <div class="col-6 col-12-xsmall">
                                <label for="archivo" class="input-label">Foto de donación</label>
                                <input type="file" id="archivo" name="archivo" class="contenedor-archivo" accept="image/png, image/jpeg" required />
                            </div>
                        </div>

                        <div class="contenedor-imagenes">
                            <img src="${pageContext.request.contextPath}/usuarioFinal/images/yape.jpg" alt="Método de donación" class="img-unica">
                        </div>

                        <div class="row gtr-uniform">
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li>
                                        <button type="submit" class="button primary big" id="donarButton">DONAR</button>
                                    </li>
                                    <li>
                                        <a href="${pageContext.request.contextPath}/ListaAlberguesServlet?accion=listar" class="button big">REGRESAR</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>

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

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const montoInput = document.getElementById('monto');

        montoInput.addEventListener('input', function() {
            // Solo permitir números, eliminando cualquier otro carácter
            this.value = this.value.replace(/\D/g, ''); // \D es todo lo que no es un dígito
        });
    });
</script>

</body>
</html>