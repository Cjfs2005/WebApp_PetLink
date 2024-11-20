<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html lang="es">
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/usuarioFinal/assets/css/popup-window.css">
    <script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/pagination.js"></script>
    <link rel="icon" href="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/logo_denuncia.png" class="icons">
                <h1 class="logo"><strong>DENUNCIAS POR MALTRATO ANIMAL</strong></h1>

                <!-- Sección para el nombre y enlace al perfil -->
                <a href="<%=request.getContextPath()%>/usuarioFinal/perfil_usuario_final.jsp" class="user-profile">
                    <span class="ocultar">Ander Vilchez</span>
                    <img src="<%=request.getContextPath()%>/usuarioFinal/images/foto_perfil.jpg"
                         style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;" />
                </a>
            </header>

            <!-- Botón para registrar nueva denuncia -->
            <section style="margin: 0; margin-top: 20px; padding: 0; height: auto; display: flex; align-items: center;">
                <ul class="actions" style="list-style: none; margin: 0; padding: 0;">
                    <li style="display: inline; padding-left: 0;">
                        <a href="<%=request.getContextPath()%>/usuarioFinal/denuncia_creacion_usuario.jsp" class="button primary big">
                            Registrar denuncia
                        </a>
                    </li>
                </ul>
            </section>

            <!-- Banner dinámico de denuncias -->
            <div class="contenedor-banner">
                <%
                    // Obtener la lista de denuncias desde la solicitud
                    List<DenunciaMaltratoAnimal> denuncias = (List<DenunciaMaltratoAnimal>) request.getAttribute("denuncias");
                    if (denuncias != null && !denuncias.isEmpty()) {
                        for (DenunciaMaltratoAnimal denuncia : denuncias) {
                %>
                <section class="banner">
                    <div class="content">
                        <header>
                            <img src="<%=request.getContextPath()%>/usuarioFinal/images/logo_denuncia_publicacion.png" class="icons">
                            <h2>Denuncia #<%= denuncia.getIdDenunciaMaltratoAnimal() %></h2>
                            <p><strong>Descripción:</strong> <%= denuncia.getDescripcionMaltrato() %></p>
                        </header>
                        <ul class="actions">
                            <!-- Modificar Denuncia -->
                            <li>
                                <a href="<%=request.getContextPath()%>/DenunciaUsuarioServlet?accion=editar&id=<%= denuncia.getIdDenunciaMaltratoAnimal() %>"
                                   class="button primary big">
                                    Modificar
                                </a>
                            </li>

                            <!-- Eliminar Denuncia -->
                            <li>
                                <form action="<%=request.getContextPath()%>/DenunciaUsuarioServlet" method="POST" onsubmit="return confirm('¿Estás seguro de que deseas eliminar esta denuncia?');">
                                    <input type="hidden" name="accion" value="eliminar">
                                    <input type="hidden" name="id" value="<%= denuncia.getIdDenunciaMaltratoAnimal() %>">
                                    <button type="submit" class="button big">Eliminar</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                    <span class="image object">
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/maltrato1.jpg" alt="Foto Denuncia" />
                    </span>
                </section>
                <% } } else { %>
                <p>No hay denuncias registradas.</p>
                <% } %>
            </div>

            <!-- Paginación -->
            <div class="pagination-container" style="display: flex; justify-content: center;">
                <ul class="pagination"></ul>
            </div>
        </div>
    </div>

    <!-- Sidebar -->
    <div id="sidebar">
        <div class="inner">

            <!-- Logo -->
            <section class="alt" id="sidebar-header">
                <img src="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id="sidebar-title">PetLink</p>
            </section>

            <!-- Perfil -->
            <section class="perfil">
                <div class="mini-posts">
                    <article>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/foto_perfil.jpg" alt="" id="image-perfil">
                        <h2 id="usuario">Ander Vilchez</h2>
                    </article>
                </div>
            </section>

            <!-- Menú -->
            <nav id="menu">
                <header class="major"><h2>Menu</h2></header>
                <ul>
                    <li><a href="<%=request.getContextPath()%>/usuarioFinal/perfil_usuario_final.jsp">PERFIL</a></li>
                    <li>
                        <span class="opener">ALBERGUES</span>
                        <ul>
                            <li><a href="<%=request.getContextPath()%>/usuarioFinal/albergue_usuario.jsp">LISTA DE ALBERGUES</a></li>
                            <li><a href="<%=request.getContextPath()%>/usuarioFinal/eventos.jsp">EVENTOS BENÉFICOS</a></li>
                            <li><a href="<%=request.getContextPath()%>/usuarioFinal/Donaciones1.jsp">SOLICITUDES DE DONACIÓN</a></li>
                            <li><a href="<%=request.getContextPath()%>/usuarioFinal/Donaciones_historial.jsp">HISTORIAL DE DONACIONES</a></li>
                            <li><a href="<%=request.getContextPath()%>/usuarioFinal/adopciones_usuario.jsp">MASCOTAS EN ADOPCIÓN</a></li>
                            <li><a href="<%=request.getContextPath()%>/usuarioFinal/adopciones_historial_usuario.jsp">HISTORIAL DE ADOPCIONES</a></li>
                        </ul>
                    </li>
                    <li><a href="<%=request.getContextPath()%>/usuarioFinal/Hogar_temporal.jsp">HOGAR TEMPORAL</a></li>
                    <li><a href="<%=request.getContextPath()%>/usuarioFinal/denuncia_usuario.jsp">DENUNCIAS POR MALTRATO ANIMAL</a></li>
                    <li><a href="<%=request.getContextPath()%>/usuarioFinal/mascotas_perdidas_usuario.jsp">MASCOTAS PERDIDAS</a></li>
                </ul>
            </nav>

            <!-- Logout -->
            <nav id="logout">
                <a href="<%=request.getContextPath()%>/usuarioFinal/bienvenidos.jsp" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/usuarioFinal/assets/js/main.js"></script>

</body>
</html>
