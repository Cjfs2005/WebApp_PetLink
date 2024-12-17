<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
    Usuario administrador = (Usuario) session.getAttribute("usuario");
    Usuario usuarioPerfil = (Usuario) session.getAttribute("usuarioPerfil");
    
    String fotoPerfilBase64 = "";
    if (usuarioPerfil.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuarioPerfil.getFoto_perfil());
    }
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/administrador/assets/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/administrador/assets/css/aditional.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/administrador/assets/css/ola2.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/administrador/assets/css/popup-window.css" />
    <link rel="icon" href="${pageContext.request.contextPath}/administrador/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">
    <!-- Main -->
    <div id="main">
        <div class="inner">
            <!-- Header -->
            <header id="header">
                <img src="${pageContext.request.contextPath}/usuarioFinal/images/perfil_logo.png" class="icons">
                <h1 class="logo"><strong>DETALLES DEL USUARIO</strong></h1>
                <!-- Sección para el nombre y enlace al perfil -->

            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <div class="perfil-info">
                        <div class="left-section">
                            <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil">
                        </div>
                        <div class="central-section">
                            <h2><%= usuarioPerfil.getNombres_usuario_final() %> <%= usuarioPerfil.getApellidos_usuario_final() %></h2>
                        </div>
                    </div>
                    <hr>

                    <!-- Información -->
                    <div class="seccion">
                        <h3>Información</h3>
                        <div class="seccion-contenido">
                            <div class="row gtr-uniform">
                                <div class="col-6 col-12-xsmall">Nombres:</div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" id="nombres" value="<%= usuarioPerfil.getNombres_usuario_final() %>" placeholder="" disabled/>
                                </div>

                                <div class="col-6 col-12-xsmall">Apellidos:</div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" id="apellidos" value="<%= usuarioPerfil.getApellidos_usuario_final() %>" placeholder="" disabled/>
                                </div>

                                <div class="col-6 col-12-xsmall">DNI:</div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" id="dni" value="<%= usuarioPerfil.getDni() %>" placeholder="" disabled/>
                                </div>

                                <div class="col-6 col-12-xsmall">Correo electrónico:</div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" id="correoElectronico" value="<%= usuarioPerfil.getCorreo_electronico() %>" placeholder="" disabled/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Ubicación -->
                    <div class="seccion">
                        <h3>Ubicación</h3>
                        <div class="seccion-contenido">
                            <div class="row gtr-uniform">
                                <div class="col-6 col-12-xsmall">Dirección:</div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" id="direccion" value="<%= usuarioPerfil.getDireccion() %>" placeholder="" disabled/>
                                </div>

                                <div class="col-6 col-12-xsmall">Distrito:</div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" id="distrito" value="<%= usuarioPerfil.getDistrito().getNombre_distrito() %>" placeholder="" disabled/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Botón para Editar Perfil -->
                    <div class="row gtr-uniform">
                        <div class="col-12">
                            <ul class="actions form-buttons">
                                <li>
                                    <%-- Gianfranco: <a href="ListaAlberguesServlet?accion=donar&id=<%=perfilAlbergue.getId_usuario()%>" class="button primary big">Donar dinero</a>--%>
                                    <a href="<%=request.getContextPath()%>/ListaUsuariosAdminServlet?action=listar" class="button big">Regresar</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
    </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

</body>
</html>

