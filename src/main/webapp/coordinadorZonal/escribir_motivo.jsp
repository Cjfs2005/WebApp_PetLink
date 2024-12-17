<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Base64" %>
<%
    Usuario coordinador = (Usuario) session.getAttribute("datosUsuario");
    String nombreCoordinador = coordinador.getNombres_coordinador();
    String apellidoCoordinador = coordinador.getApellidos_coordinador();
    String zonaCoordinador = coordinador.getZona().getNombre_zona();

    Usuario miEstimado = (Usuario) request.getAttribute("miEstimado");
    System.out.println("vamos a banear a "+ miEstimado.getNombres_usuario_final());
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/coordinadorZonal/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>Banear usuario</strong></h1>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/coordinadorZonal/images/form.png" class="icons">
                        <h2>MOTIVO DE BANEO</h2>
                    </header>


                    <form id="banearForm" action="<%=request.getContextPath()%>/BaneosCoordi1Servlet?action=banear" method="post">
                        <div class="row gtr-uniform">

                            <div class="col-12">
                                <label for="motivo" class="input-label">Escriba la razón por la cual se baneará al usuario</label>
                                <textarea name="motivo" id="motivo" class="text-area" placeholder=""></textarea>
                            </div>

                            <input type="hidden" name="id" value="<%= miEstimado.getId_usuario() %>">

                            <!-- Break -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><a href="#" class="button primary big" id="openModal">Banear</a></li>
                                    <li><a href="<%=request.getContextPath()%>/BaneosCoordi1Servlet?action=listas" class="button big">Cancelar</a></li>
                                </ul>
                            </div>

                        </div>
                    </form>

                </div>
            </section>

        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= coordinador.getId_usuario() %>" />
        <jsp:param name="nombresUsuario" value="<%= coordinador.getNombres_coordinador() %>" />
        <jsp:param name="apellidosUsuario" value="<%= coordinador.getApellidos_coordinador() %>" />
        <jsp:param name="zonaCoordinador" value="<%= coordinador.getZona().getNombre_zona()%>" />
    </jsp:include>



</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/main.js"></script>

<!-- Modal -->
<div id="modal" class="modal">
    <div class="modal-content">
        <!-- Botón cerrar -->
        <span class="close-btn">&times;</span>
        <p>¿Está seguro de banear al usuario?<br>El usuario baneado ya no será considerado por los albergues como hogar temporal </p>
        <!-- Botones de Aceptar y Cancelar -->
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big" id="acceptButton">Banear</a></li>
            <li><a href="#" class="button big" id="cancelButton">Cancelar</a></li>
        </ul>
    </div>
</div>

<!-- Script dentro del mismo HTML -->
<script>
    // Esperamos a que todo el DOM esté cargado
    document.addEventListener('DOMContentLoaded', function() {
        // Obtener los elementos del DOM
        const openModalButton = document.getElementById('openModal'); // Botón que abre el modal
        const modal = document.getElementById('modal');               // El modal
        const closeModalButton = document.querySelector('.close-btn'); // Botón para cerrar el modal (X)
        const acceptButton = document.getElementById('acceptButton'); // Botón de Aceptar
        const cancelButton = document.getElementById('cancelButton'); // Botón de Cancelar

        // Función para abrir el modal
        openModalButton.addEventListener('click', function() {
            event.preventDefault();
            modal.classList.add('show'); // Mostrar el modal
        });

        // Función para cerrar el modal al hacer clic en la "X"
        closeModalButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });

        // Deben modificar el link para que redirija afuera del formulario
        acceptButton.addEventListener('click', function (event) {
            event.preventDefault(); // Evita la navegación predeterminada
            document.getElementById('banearForm').submit(); // Envía el formulario
        });

        // Función para cerrar el modal al hacer clic en el botón "Cancelar"
        cancelButton.addEventListener('click', function (event) {
            event.preventDefault();
            modal.classList.remove('show'); // Ocultar el modal
        });
    });
</script>
</body>
</html>
