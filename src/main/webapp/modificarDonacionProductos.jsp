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
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Modificar Solicitud de Donaciones de Productos</title>
    <meta charset="utf-8" />

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="assets/css/main.css" />
    <link rel="stylesheet" href="assets/css/aditional.css">
    <link rel="icon" href="assets/images/favicon.png" type="image/x-icon">
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
                <a href="perfil.html" class="user-profile">
                    <span class="ocultar">Huellitas PUCP</span>
                    <img src="images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;" />
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

                    <form action="ListaSolicitudesDonacionProductos?action=actualizar" method="POST">
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
                                <label for="horaInicioEvento" class="input-label">Hora de inicio de recepción</label>
                                <input type="time" id="horaInicioEvento" name="horaInicioEvento" class="time-input"
                                       value="<%= solicitud != null && solicitud.getHorarioRecepcion() != null
                                              && solicitud.getHorarioRecepcion().getFechaHoraInicio() != null
                                              ? solicitud.getHorarioRecepcion().getFechaHoraInicio().toLocalTime()
                                              : "" %>" required />
                            </div>

                            <!-- Hora final de recepción -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaFinEvento" class="input-label">Hora final de recepción</label>
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
                                    <li><a href="ListaSolicitudesDonacionProductos?action=listar" class="button big">Cancelar</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>

                </div>
            </section>
        </div>
    </div>
       <jsp:include page="albergue/navbar.jsp">
        <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>
</div>

</body>
</html>
