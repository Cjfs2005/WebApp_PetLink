<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.PublicacionEventoBenefico" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<% PublicacionEventoBenefico evento = (PublicacionEventoBenefico) request.getAttribute("evento");
    String fotoEventoBase64 = "";
    if (evento.getFoto() != null) {
        fotoEventoBase64 = Base64.getEncoder().encodeToString(evento.getFoto());
    }
%>
<%
    Usuario coordinador = (Usuario) session.getAttribute("datosUsuario");
    String nombreCoordinador = coordinador.getNombres_coordinador();
    String apellidoCoordinador = coordinador.getApellidos_coordinador();
    String zonaCoordinador = coordinador.getZona().getNombre_zona();
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
                <h1 class="logo"><strong>Detalles del evento</strong></h1>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/form.png" class="icons">
                        <h2><%=evento.getNombreEvento()%></h2>
                    </header>

                    <div class="contenedor-imagenes">
                        <% if (evento.getFoto() != null) {%>
                        <img src="data:image/png;base64,<%= fotoEventoBase64 %>" alt="<%=evento.getNombreFoto()%>" class="img-unica">
                        <% } else {%>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/pic01.jpg" alt="<%=evento.getNombreFoto()%>" class="img-unica">
                        <% } %>

                    </div>

                    <p><strong>Albergue organizador: </strong><%=evento.getUsuarioAlbergue().getNombre_albergue()%> </p>
                    <p><strong>Razón del evento: </strong><%=evento.getRazonEvento()%></p>
                    <p><strong>Descripción: </strong><%=evento.getDescripcionEvento()%> </strong></p>
                    <p><strong>Información General:</strong></p>
                    <ul>

                        <%
                            SimpleDateFormat formatter = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                            String fechaFormateada = formatter.format(evento.getFechaHoraInicioEvento());
                        %>
                        <li><strong>Fecha: </strong><%=fechaFormateada%></li>

                        <%
                            SimpleDateFormat formatterHoraInicio = new SimpleDateFormat("HH:mm"); // Para formato 12 horas con AM/PM
                            String horaFormateadaInicio = formatterHoraInicio.format(evento.getFechaHoraInicioEvento());

                            SimpleDateFormat formatterHoraFin = new SimpleDateFormat("HH:mm");
                            String horaFormateadaFin = formatterHoraFin.format(evento.getFechaHoraFinEvento());
                        %>
                        <li><strong>Duración: </strong><%=horaFormateadaInicio%>-<%=horaFormateadaFin%> </li>
                        <li><strong>Lugar: </strong> <%=evento.getLugarEvento().getDireccion_lugar_evento()%> - <%=evento.getLugarEvento().getDistrito().getNombre_distrito()%></li>
                        <li><strong>Invitados: </strong><%=evento.getArtistasProvedoresInvitados()%></li>
                        <li><strong>Entrada: </strong><%=evento.getEntradaEvento()%></li>
                    </ul>
                    <p><strong>Datos del albergue: </strong></p>
                    <ul>
                        <li><strong>Encargado del albergue: </strong><%=evento.getUsuarioAlbergue().getNombres_encargado()%> <%=evento.getUsuarioAlbergue().getApellidos_encargado()%></li>
                        <li><strong>Correo: </strong><%=evento.getUsuarioAlbergue().getCorreo_electronico()%></li>
                    </ul>

                </div>
            </section>
        </div>
    </div>

    <!-- Sidebar -->
    <div id="sidebar">
        <div class="inner">

            <!-- Logo -->

            <section class="alt" id="sidebar-header">
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/favicon.png" alt="Logo" id="sidebar-icon">
                <p id ="sidebar-title">PetLink</p>
            </section>

            <!-- Perfil -->

            <section class="perfil">

                <div class="mini-posts">
                    <article>
                        <img src="images/foto_coordinador.png" alt="" id="image-perfil">
                        <h2 id="usuario">Marco Lang<br>Zona Este</h2>
                    </article>
                </div>

            </section>

            <!-- Menu -->
            <nav id="menu">
                <header class="major">
                    <h2>Menu</h2>
                </header>
                <ul>
                    <li>
                        <span class="opener">SOLICITUD DE PUBLICACION DE MASCOTAS PERDIDAS</span>
                        <ul>
                            <li><a href="solicitudes_usuario_mascotas_perdidas.html">SOLICITUD DEL USUARIO</a></li>
                            <li><a href="solicitudes_duenio_mascotas_perdidas.html">SOLICITUD DEL DUEÑO</a></li>
                        </ul>
                    </li>
                    <li>
                        <span class="opener">PUBLICACIONES DE MASCOTAS PERDIDAS</span>
                        <ul>
                            <li><a href="mascotas_perdidas_publicaciones.html">Mascotas Perdidas publicadas por Usuarios</a></li>
                            <li><a href="mascotas_perdidas_publicaciones_dueños.html">Mascotas Perdidas publicadas por Dueños</a></li>
                        </ul>
                    </li>
                    <li><a href="postulantes.html">Postulantes a hogar temporal</a></li>
                    <li>
                        <span class="opener">Baneo de usuarios</span>
                        <ul>
                            <li><a href="banear_usuarios.html">Banear usuarios</a></li>
                            <li><a href="baneos_realizados.html">Baneos realizados</a></li>
                        </ul>
                    </li>
                    <li>
                        <span class="opener">GESTIÓN DE PUBLICACIONES</span>
                        <ul>
                            <li><a href="<%=request.getContextPath()%>/GestionCoordinadorServlet">Gestión de eventos</a></li>
                            <li><a href="mascotas_perdidas_publicaciones_dueños.html">Gestión de adopciones</a></li>
                        </ul>
                    </li>

                </ul>
            </nav>

            <!-- Logout -->

            <nav id="logout">
                <a href="#" id="cerrar-sesion">Cerrar Sesión</a>
            </nav>

        </div>
    </div>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/main.js"></script>
<!--Modal-->
<div id="modal" class="modal">
    <div class="modal-content">
        <!-- Botón cerrar -->
        <span class="close-btn">&times;</span>
        <p>¿Estás seguro de que quieres inscribirte?</p>
        <!-- Botones de Aceptar y Cancelar -->
        <ul class="actions modal-buttons">
            <li><a href="<%=request.getContextPath()%>/EventoUsuarioServlet?action=inscribir&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="button primary big" id="acceptButton">Sí</a></li>
            <li><a href="#" class="button big" id="cancelButton">No</a></li>
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
            modal.classList.add('show'); // Mostrar el modal
        });

        // Función para cerrar el modal al hacer clic en la "X"
        closeModalButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });

        // Deben modificar el link para que redirija afuera del formulario
        acceptButton.addEventListener('click', function() {
            window.location.href = 'index.html';
        });

        // Función para cerrar el modal al hacer clic en el botón "Cancelar"
        cancelButton.addEventListener('click', function() {
            modal.classList.remove('show'); // Ocultar el modal
        });
    });
</script>
</body>
</html>

