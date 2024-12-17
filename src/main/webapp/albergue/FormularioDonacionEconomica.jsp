<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Formulario de Solicitud de Donaciones Económicas</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">

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
                <h1 class="logo"><strong>Formulario de Solicitud de Donaciones Económicas</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <% if (albergue.getFoto_perfil() != null) {%>
                    <span class="ocultar"><%=nombreUsuario%></span> <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
                    <% } else {%>
                    <span class="ocultar"><%=nombreUsuario%></span> <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/albergue/images/form.png" class="icons">
                        <h2>Detalles de la Donación</h2>
                    </header>

                    <!-- Formulario -->
                    <form action="<%= request.getContextPath()%>/DonacionEconomicaServlet?action=crear" method="post">
                        <!-- Campo oculto para el ID del estado -->
                        <input type="hidden" name="idEstado" value="1" />

                        <!-- Campo oculto para el ID del usuario albergue -->
                        <input type="hidden" name="idUsuarioAlbergue" value="6" />

                        <div class="row gtr-uniform">
                            <!-- Monto Requerido -->
                            <div class="col-12">
                                <label for="monto" class="input-label">Monto requerido</label>
                                <input type="text" id="monto" name="monto"
                                       value=""
                                       placeholder="Ingrese el monto"
                                       required />
                            </div>

                            <!-- Motivo -->
                            <div class="col-12">
                                <label for="motivo" class="input-label">Motivo</label>
                                <textarea id="motivo" name="motivo" maxlength="300" class="text-area"
                                          placeholder="Explique el motivo de la solicitud"
                                          required></textarea>
                            </div>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><button type="submit" class="button primary big">Publicar</button></li>
                                    <li><a href="<%= request.getContextPath()%>/DonacionEconomicaServlet?action=listar" class="button big">Cancelar</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>




                </div>
            </section>

        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>

</body>
</html>
