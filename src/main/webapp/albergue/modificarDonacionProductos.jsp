<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
    // Obtener la solicitud desde el servlet
    SolicitudDonacionProductos solicitud = (SolicitudDonacionProductos) request.getAttribute("solicitud");
    List<PuntoAcopio> puntosAcopio = (List<PuntoAcopio>) request.getAttribute("puntosAcopio");
%>
<%
    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
    Usuario usuario = albergue;
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Modificar Solicitud de Donaciones de Productos</title>
    <meta charset="utf-8" />

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/assets/images/favicon.png" type="image/x-icon">
    <style>
        #charCount {
            font-size: 12px;
            color: #888;
            float: right;
        }

        /* Estilo personalizado para inputs de hora */
        .time-input {
            appearance: none;
            border-radius: 0.375em;
            border: none;
            border: solid 1px rgba(210, 215, 217, 0.75);
            color: inherit;
            display: block;
            outline: 0;
            padding: 0 1em;
            text-decoration: none;
            width: 100%;
            background: #ffffff;
            padding-top: 7.5px;
            padding-bottom: 7.5px;
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
                <h1 class="logo"><strong>Modificar Donación de Productos</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombre_albergue() %></span>
                    <img src="<%= !fotoPerfilBase64.isEmpty() ? "data:image/png;base64," + fotoPerfilBase64 : request.getContextPath() + "/albergue/images/default_profile.png" %>"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <!-- Formulario -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/form.png" class="icons">
                        <h2>Formulario para Modificar Solicitud</h2>
                    </header>
                    <p><strong>Descripción:</strong> Actualice los datos de la solicitud de donaciones de productos.</p>

                    <form action="DonacionProductosServlet?action=actualizar" method="POST">
                        <!-- Campo oculto con el ID de la solicitud -->
                        <input type="hidden" name="id" value="<%= solicitud != null ? solicitud.getIdSolicitudDonacionProductos() : "" %>" />

                        <div class="row gtr-uniform">
                            <!-- Fecha programada para recepción -->
                            <div class="col-6 col-12-small">
                                <label for="fechaRecepcion" class="input-label">Fecha programada para recepción</label>
                                <input type="date" id="fechaRecepcion" name="fechaRecepcion"
                                       value="<%= solicitud != null && solicitud.getHorarioRecepcion() != null
                                              && solicitud.getHorarioRecepcion().getFechaHoraInicio() != null
                                              ? solicitud.getHorarioRecepcion().getFechaHoraInicio().toLocalDate()
                                              : "" %>" required />
                            </div>

                            <!-- Puntos de acopio -->
                            <div class="col-6 col-12-small">
                                <label for="puntosAcopio" class="input-label">Puntos de Acopio</label>
                                <select id="puntosAcopio" name="puntoAcopio" required>
                                    <option value="" disabled>Seleccione un punto de acopio</option>
                                    <% if (puntosAcopio != null) {
                                        for (PuntoAcopio punto : puntosAcopio) {
                                            boolean isSelected = solicitud != null && solicitud.getHorarioRecepcion() != null
                                                    && solicitud.getHorarioRecepcion().getPuntoAcopioDonacion() != null
                                                    && solicitud.getHorarioRecepcion().getPuntoAcopioDonacion().getPuntoAcopio() != null
                                                    && solicitud.getHorarioRecepcion().getPuntoAcopioDonacion().getPuntoAcopio().getId_punto_acopio() == punto.getId_punto_acopio();
                                    %>
                                    <option value="<%= punto.getId_punto_acopio() %>" <%= isSelected ? "selected" : "" %>>
                                        <%= punto.getDireccion_punto_acopio() %>
                                    </option>
                                    <%    }
                                    } %>
                                </select>
                            </div>

                            <!-- Hora de inicio de recepción -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaInicioEvento" class="input-label">Hora de inicio </label>
                                <input type="time" id="horaInicioEvento" name="horaInicioEvento" class="time-input"
                                       value="<%= solicitud != null && solicitud.getHorarioRecepcion() != null
                                              && solicitud.getHorarioRecepcion().getFechaHoraInicio() != null
                                              ? solicitud.getHorarioRecepcion().getFechaHoraInicio().toLocalTime()
                                              : "" %>" required />
                            </div>

                            <!-- Hora final de recepción -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaFinEvento" class="input-label">Hora final </label>
                                <input type="time" id="horaFinEvento" name="horaFinEvento" class="time-input"
                                       value="<%= solicitud != null && solicitud.getHorarioRecepcion() != null
                                              && solicitud.getHorarioRecepcion().getFechaHoraFin() != null
                                              ? solicitud.getHorarioRecepcion().getFechaHoraFin().toLocalTime()
                                              : "" %>" required />
                            </div>

                            <!-- Productos solicitados -->
                            <div class="col-12">
                                <label for="descripcion" class="input-label">Productos solicitados</label>
                                <textarea id="descripcion" name="descripcion" maxlength="500" required><%= solicitud != null ? solicitud.getDescripcionDonaciones() : "" %></textarea>
                            </div>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><button type="submit" class="button primary big">Actualizar</button></li>
                                    <li><a href="DonacionProductosServlet?action=listar" class="button big">Cancelar</a></li>
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
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const horaInicioInput = document.getElementById("horaInicioEvento");
        const horaFinInput = document.getElementById("horaFinEvento");
        const formulario = document.querySelector("form");

        formulario.addEventListener("submit", function (event) {
            const horaInicio = horaInicioInput.value;
            const horaFin = horaFinInput.value;

            // Validar que la hora de fin sea mayor que la hora de inicio
            if (horaFin && horaInicio && horaFin <= horaInicio) {
                event.preventDefault(); // Evita el envío del formulario
                alert("La hora de fin debe ser mayor que la hora de inicio.");
            }
        });
    });
</script>

</body>
</html>
