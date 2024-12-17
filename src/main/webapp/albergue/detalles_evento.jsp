<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Base64" %>
<%
    PublicacionEventoBenefico evento = (PublicacionEventoBenefico) request.getAttribute("evento");
    String fotoEventoBase64 = "";
    if (evento.getFoto() != null) {
        fotoEventoBase64 = Base64.getEncoder().encodeToString(evento.getFoto());
    }

    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
    ArrayList<InscripcionEventoBenefico> inscritos = (ArrayList<InscripcionEventoBenefico>) request.getAttribute("inscritos");
    String estadoEvento = evento.getEstado().getNombre_estado();
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />

    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">


    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>

    <style>
        .estado-aprobada {
            color: green;
            font-weight: bold;
        }

        .estado-pendiente {
            color: orange;
            font-weight: bold;
        }

        .estado-rechazada {
            color: red;
            font-weight: bold;
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
                <h1 class="logo"><strong>Detalles del evento</strong></h1>
                <!-- Sección para el nombre y enlace al perfil -->
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
                        <h2><%=evento.getNombreEvento()%></h2>
                    </header>

                    <div class="contenedor-imagenes">
                        <% if (evento.getFoto() != null) {%>
                        <img src="data:image/png;base64,<%= fotoEventoBase64 %>" alt="<%=evento.getNombreFoto()%>" class="img-unica">
                        <% } else {%>
                        <img src="<%=request.getContextPath()%>/usuarioFinal/images/pic01.jpg" alt="<%=evento.getNombreFoto()%>" class="img-unica">
                        <% } %>
                    </div>

                    <p><strong>Razón del evento: </strong><%=evento.getRazonEvento()%></p>
                    <p><strong>Descripción: </strong><%=evento.getDescripcionEvento()%></p>
                    <p><strong>Información General:</strong></p>
                    <ul>
                        <%
                            SimpleDateFormat fechaEvent = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                            String fechaEventFormatter = fechaEvent.format(evento.getFechaHoraInicioEvento());
                        %>
                        <li><strong>Fecha: </strong><%=fechaEventFormatter%></li>
                        <%
                            SimpleDateFormat formatterHoraInicio = new SimpleDateFormat("HH:mm"); // Para formato 12 horas con AM/PM
                            String horaFormateadaInicio = formatterHoraInicio.format(evento.getFechaHoraInicioEvento());

                            SimpleDateFormat formatterHoraFin = new SimpleDateFormat("HH:mm");
                            String horaFormateadaFin = formatterHoraFin.format(evento.getFechaHoraFinEvento());
                        %>
                        <li><strong>Duración: </strong>
                            <% if (evento.getFechaHoraInicioEvento() != null && evento.getFechaHoraFinEvento() != null) { %>
                            <%=horaFormateadaInicio%> - <%=horaFormateadaFin%>
                            <% } else { %>
                            No especificado
                            <% } %>
                        </li>
                        <li><strong>Lugar: </strong>
                            <% if (evento.getLugarEvento() != null && evento.getLugarEvento().getDistrito() != null) { %>
                            <%=evento.getLugarEvento().getNombre_lugar_evento()%>, <%=evento.getLugarEvento().getDireccion_lugar_evento()%> - <%=evento.getLugarEvento().getDistrito().getNombre_distrito()%>
                            <% } else { %>
                            Información no disponible
                            <% } %>
                        </li>
                        <li><strong>Invitados: </strong><%=evento.getArtistasProvedoresInvitados()%></li>
                        <li><strong>Aforo: </strong><%=evento.getAforoEvento()%></li>
                        <li><strong>Entrada: </strong><%=evento.getEntradaEvento()%>.</li>
                        <li>
                            <strong>Estado: </strong>
                            <span class="
                                <%= evento.getEstado().getNombre_estado().equalsIgnoreCase("Aprobada") ? "estado-aprobada" :
                                    evento.getEstado().getNombre_estado().equalsIgnoreCase("Pendiente") ? "estado-pendiente" :
                                    "estado-rechazada"
                                %>">
                                <%=evento.getEstado().getNombre_estado().toUpperCase()%>
                            </span>
                        </li>
                    </ul>
                    <p><strong>Lista de usuarios inscritos: </strong><%=inscritos.size()%>/<%=evento.getAforoEvento()%></p>
                    <div class="table-responsive">
                        <table table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>N°</th>
                                <th>Nombres y apellidos</th>
                                <th>Correo electrónico</th>
                                <th>Fecha de inscripción</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                int contador = 1;
                                for (InscripcionEventoBenefico inscrito : inscritos) {
                            %>
                            <tr>
                                <td><%=contador++%></td>
                                <td><%=inscrito.getUsuario_final().getNombres_usuario_final() + " " + inscrito.getUsuario_final().getApellidos_usuario_final()%></td>
                                <td><%=inscrito.getUsuario_final().getCorreo_electronico()%></td>
                                <%
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String fechaFormateada = formatter.format(inscrito.getFecha_hora_registro());
                                %>
                                <td><%=fechaFormateada%></td>

                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                    sLengthMenu: "Mostrar _MENU_ registros",
                                    sZeroRecords: "No se encontraron resultados",
                                    sEmptyTable: "Ningún usuario se ha inscrito",
                                    sInfo: "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                                    sInfoEmpty: "Mostrando registros del 0 al 0 de un total de 0 registros",
                                    sInfoFiltered: "(filtrado de un total de _MAX_ registros)",
                                    sLoadingRecords: "Cargando...",
                                }
                            });
                        </script>
                    </div>

                    <div class="row gtr-uniform">

                        <!-- Break -->
                        <div class="col-12">
                            <ul class="actions form-buttons">
                                <%-- Botón Modificar --%>
                                <% if ("Pendiente".equalsIgnoreCase(estadoEvento)) { %>
                                <a href="<%=request.getContextPath()%>/EventoAlbergueServlet?action=editar&id=<%= evento.getIdPublicacionEventoBenefico() %>"
                                   class="button primary">Modificar</a>
                                <% } else { %>
                                <a class="button primary disabled" style="pointer-events: none; opacity: 0.6;">Modificar</a>
                                <% } %>
                                <li><a href="#" class="button big" id="openModal">Eliminar</a></li>
                            </ul>
                        </div>
                        <div id="modal" class="modal">
                            <div class="modal-content">
                                <!-- Botón cerrar -->
                                <span class="close-btn">&times;</span>
                                <p>¿Está seguro de eliminar este evento?</p>
                                <!-- Botones de Aceptar y Cancelar -->
                                <ul class="actions modal-buttons">
                                    <li><a href="<%=request.getContextPath()%>/EventoAlbergueServlet?action=eliminar&id=<%=evento.getIdPublicacionEventoBenefico()%>" class="button primary big" id="acceptButton">Aceptar</a></li>
                                    <li><a href="#" class="button big" id="cancelButton">Cancelar</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
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

