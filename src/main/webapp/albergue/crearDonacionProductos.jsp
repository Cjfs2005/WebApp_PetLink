<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.PuntoAcopio" %>
<%@ page import="com.example.webapp_petlink.daos.DonacionProductos" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>
<%
    DonacionProductos dao = new DonacionProductos();
    List<PuntoAcopio> puntosAcopio = dao.obtenerPuntosAcopioPorAlbergue(6); // ID de prueba

    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    if (albergue == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = albergue.getFoto_perfil() != null
            ? Base64.getEncoder().encodeToString(albergue.getFoto_perfil())
            : null;
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>Crear Solicitud de Donaciones de Productos</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
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
                <h1 class="logo"><strong>Crear Donación de Productos</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <span class="ocultar"><%=nombreUsuario%></span>
                    <% if (fotoPerfilBase64 != null) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } else { %>
                    <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" alt="Sin perfil"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Formulario -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/form.png" class="icons">
                        <h2>Formulario para Crear Solicitud</h2>
                    </header>
                    <p><strong>Descripción:</strong> Complete los detalles para registrar una nueva solicitud de donaciones de productos.</p>

                    <form action="ListaSolicitudesDonacionProductos?action=publicar" method="POST">
                        <div class="row gtr-uniform">

                            <!-- Fecha de recepción -->
                            <div class="col-6 col-12-small">
                                <label for="fechaRecepcion" class="input-label">Fecha de recepción</label>
                                <input type="date" id="fechaRecepcion" name="fechaRecepcion"
                                       min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" required />
                            </div>

                            <!-- Puntos de acopio -->
                            <div class="col-6 col-12-small">
                                <label for="puntoAcopio" class="input-label">Punto de Acopio</label>
                                <select id="puntoAcopio" name="puntoAcopio" required>
                                    <option value="" disabled selected>Seleccione un punto de acopio</option>
                                    <% if (puntosAcopio != null && !puntosAcopio.isEmpty()) {
                                        for (PuntoAcopio punto : puntosAcopio) { %>
                                    <option value="<%= punto.getId_punto_acopio() %>"><%= punto.getDireccion_punto_acopio() %></option>
                                    <% }
                                    } else { %>
                                    <option value="" disabled>No hay puntos disponibles</option>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Hora inicio y fin -->
                            <!-- Hora inicio -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaInicioEvento" class="input-label">Hora de inicio</label>
                                <input type="time" id="horaInicioEvento" name="horaInicioEvento" class="time-input" required />
                            </div>

                            <!-- Hora fin -->
                            <div class="col-6 col-12-xsmall">
                                <label for="horaFinEvento" class="input-label">Hora de fin</label>
                                <input type="time" id="horaFinEvento" name="horaFinEvento" class="time-input" required />
                            </div>

                            <style>
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


                            <!-- Descripción -->
                            <div class="col-12">
                                <label for="descripcion" class="input-label">Productos solicitados</label>
                                <textarea id="descripcion" name="descripcion" maxlength="500" placeholder="Describa los productos requeridos" required></textarea>
                                <span id="charCount">0/500</span>
                            </div>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><button type="submit" class="button primary big">Publicar</button></li>
                                    <li><a href="ListaSolicitudesDonacionProductos?action=listar" class="button big">Cancelar</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </section>

        </div>
    </div>

    <!-- Sidebar -->
    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= albergue.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= albergue.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>
</div>

<!-- Scripts -->
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const textarea = document.getElementById('descripcion');
        const charCount = document.getElementById('charCount');
        textarea.addEventListener('input', () => {
            charCount.textContent = `${textarea.value.length}/500`;
        });
    });
</script>
<!-- Scripts -->
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
