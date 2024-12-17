<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>
<%
    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
    PublicacionEventoBenefico evento = (PublicacionEventoBenefico) request.getAttribute("evento");
%>
<%
    ArrayList<LugarEvento> lugares = (ArrayList<LugarEvento>) request.getAttribute("lugares");
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>Editar evento benéfico</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
    <style>
        .charCount {
            font-size: 12px;
            color: #888;
            float: right;
        }

        /* Estilos para centrar el modal */
        .modal {
            display: none; /* Mantener oculto inicialmente */
            position: fixed; /* Fijar en la pantalla */
            z-index: 1; /* Asegurar que esté encima de otros elementos */
            left: 0;
            top: 0;
            width: 100%; /* Ancho completo de la pantalla */
            height: 100%; /* Alto completo de la pantalla */
            background-color: rgba(0, 0, 0, 0.4); /* Fondo semitransparente */
        }

        .modal-content {
            background-color: #fff;
            margin: auto; /* Centrar el modal horizontalmente */
            padding: 20px;
            border-radius: 10px;
            width: 80%; /* Ancho del modal */
            max-width: 500px; /* Máximo ancho para pantallas grandes */
            position: relative;
            top: 50%; /* Posicionarlo a la mitad verticalmente */
            transform: translateY(-50%); /* Ajuste para centrar verticalmente */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .error-message{
            color: red;
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
                <h1 class="logo"><strong>Editar evento: <%=evento.getNombreEvento()%></strong></h1>
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
                        <img src="images/form.png" class="icons">
                        <h2>Detalles del Evento</h2>
                    </header>
                    <p><strong>Descripción:</strong> Modifica los datos que creas conveniente para el evento <%=evento.getNombreEvento()%></p>

                    <form id="eventoForm" method="post" action="<%=request.getContextPath()%>/EventoAlbergueServlet?action=actualizar" onsubmit="return validarFormulario();" enctype="multipart/form-data">
                        <div class="row gtr-uniform">

                            <div class="col-12">
                                <input type="hidden" class="input-label" name="id_evento" value="<%=evento.getIdPublicacionEventoBenefico()%>">
                            </div>
                            <!--Nombre del albergue-->
                            <div class="col-12">
                                <label for="nombre-albergue" class="input-label">Nombre del albergue</label>
                                <input type="text" id="nombre-albergue" name="nombre-albergue" value="<%=albergue.getNombre_albergue()%>" disabled>
                            </div>

                            <!-- Nombre del evento -->
                            <div class="col-6 col-12-xsmall">
                                <label for="nombre_evento" class="input-label">Nombre del evento</label>
                                <input type="text" id="nombre_evento" name="nombre_evento" maxlength="100" value="<%=evento.getNombreEvento()%>" required />
                                <span class="error-message" id="error-nombre_evento"></span>
                            </div>

                            <!-- Día del evento -->
                            <div class="col-6 col-12-xsmall">
                                <label for="diaEvento" class="input-label">Día del evento</label>
                                <input type="date" id="diaEvento" name="diaEvento" value="<%= new SimpleDateFormat("yyyy-MM-dd").format(evento.getFechaHoraInicioEvento()) %>" min="" onkeydown="return false;" required />
                                <span class="error-message" id="error-diaEvento"></span>
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="horaInicioEvento" class="input-label">Hora de inicio del evento</label>
                                <input type="time" id="horaInicioEvento" name="horaInicioEvento" value="<%= new SimpleDateFormat("HH:mm").format(evento.getFechaHoraInicioEvento()) %>" onkeydown="return false;" required
                                       style="appearance: none;
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
                                                    padding-bottom: 7.5px;"/>
                                <span class="error-message" id="error-horaInicioEvento"></span>
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="horaEvento" class="input-label">Hora final del evento</label>
                                <input type="time" id="horaEvento" name="horaEvento" value="<%= new SimpleDateFormat("HH:mm").format(evento.getFechaHoraFinEvento()) %>" required onkeydown="return false;"
                                       style="appearance: none;
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
                                                    padding-bottom: 7.5px;"/>
                                <span class="error-message" id="error-horaEvento"></span>
                            </div>

                            <!-- Lugar del evento -->
                            <div class="col-6 col-12-xsmall">
                                <label for="distrito" class="input-label">Lugar del evento</label>
                                <select id="distrito" name="distrito">
                                    <%
                                        for (LugarEvento lugar : lugares) {
                                    %>
                                    <option value="<%= lugar.getId_lugar_evento() %>" <%= lugar.getId_lugar_evento() == evento.getLugarEvento().getId_lugar_evento() ? "selected" : "" %>>
                                        <%= lugar.getNombre_lugar_evento() %> - <%= lugar.getDistrito().getNombre_distrito() %>. Aforo <%= lugar.getAforo_maximo() %>
                                    </option>
                                    <%
                                        }
                                    %>
                                </select>
                                <span class="error-message" id="error-distrito"></span>
                            </div>

                            <div class="col-6 col-12-xsmall">
                                <label for="aforo" class="input-label">Aforo para el evento</label>
                                <input type="text" id="aforo" name="aforo" maxlength="100000" value="<%=evento.getAforoEvento()%>" required />
                                <span class="error-message" id="error-aforo"></span>
                            </div>

                            <!--Entrada para el evento-->
                            <div class="col-12">
                                <label for="entrada" class="input-label">Entrada para el evento (indicar que donación el usuario puede llevar para entrar al evento)</label>
                                <textarea id="entrada" name="entrada" class="text-area" maxlength="100" required><%=evento.getEntradaEvento()%></textarea>
                                <span class="charCount" id="charCountEntrada">0/100</span>
                                <span class="error-message" id="error-entrada"></span>
                            </div>

                            <!-- Razón del evento -->
                            <div class="col-12">
                                <label for="razonEvento" class="input-label">Razón del evento</label>
                                <textarea id="razonEvento" name="razonEvento" class="text-area" maxlength="500" required><%=evento.getRazonEvento()%></textarea>
                                <span class="charCount" id="charCountRazon">0/500</span>
                                <span class="error-message" id="error-razonEvento"></span>
                            </div>

                            <!-- Descripción del evento -->
                            <div class="col-12">
                                <label for="descripcionEvento" class="input-label">Descripción del evento (máximo 500 caracteres)</label>
                                <textarea id="descripcionEvento" name="descripcionEvento" class="text-area" maxlength="500"  required><%=evento.getDescripcionEvento()%></textarea>
                                <span class="charCount" id="charCountDescripcion">0/500</span>
                                <span class="error-message" id="error-descripcionEvento"></span>
                            </div>

                            <!-- Artistas o proveedores invitados -->
                            <div class="col-12">
                                <label for="artistasInvitados" class="input-label">Artistas o proveedores invitados</label>
                                <textarea id="artistasInvitados" name="artistasInvitados" maxlength="300" class="text-area" value=""><%=evento.getArtistasProvedoresInvitados()%></textarea>
                                <span class="charCount" id="charCountArtistas">0/300</span>
                            </div>

                            <div class="col-12">
                                <label for="fotosEvento" class="input-label">Modificar foto del evento (solo PNG, máximo 5 MB)</label>

                                <!-- Mostrar imagen actual si existe -->
                                <% if (evento.getFoto() != null && evento.getFoto().length > 0) { %>
                                <div>
                                    <p>Imagen actual del evento:</p>
                                    <img src="data:image/png;base64,<%= Base64.getEncoder().encodeToString(evento.getFoto()) %>"
                                         alt="Imagen del Evento"
                                         style="max-width: 100%; max-height: 200px; border: 1px solid #ccc; padding: 5px;" />
                                </div>
                                <% } else { %>
                                <p>No hay imagen registrada para este evento.</p>
                                <% } %>

                                <!-- Input para subir nueva imagen -->
                                <input type="file" id="fotosEvento" name="fotosEvento" accept=".png" />
                                <span class="error-message" id="error-fotosEvento"></span>
                            </div>

                            <script>
                                document.getElementById('fotosEvento').addEventListener('change', function(event) {
                                    if (this.files.length > 1) {
                                        alert('Solo puedes subir una foto.');
                                        this.value = '';
                                    }
                                });
                            </script>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><a href="#" class="button primary big" id="validateFormButton">Publicar</a></li>
                                    <!--<li><button class="button primary big" type="button" id="validateFormButton">Publicar</button></li>-->
                                    <li><a href="javascript:history.back()" class="button big">Cancelar</a></li>
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

<!--Modal-->
<div id="modal" class="modal">
    <div class="modal-content">
        <p>Se ha publicado el post con éxito.</p>
        <ul class="actions modal-buttons">
            <!--<li><button class="button primary big" type="button" onclick="enviarFormulario()">Confirmar</button></li>-->
            <li><a href="javascript:void(0);" class="button primary big" onclick="enviarFormulario()">Confirmar</a></li>

            <!--<li><a href="<%=request.getContextPath()%>/EventoAlbergueServlet" class="button primary big" onclick="enviarFormulario()">Confirmar</a></li>-->
        </ul>
    </div>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>

<script>
    document.getElementById('aforo').addEventListener('keypress', function (event) {
        const charCode = event.charCode; // Obtener el código del carácter ingresado
        // Verificar si el carácter no es un número (charCode < 48 || charCode > 57)
        if (charCode < 48 || charCode > 57) {
            event.preventDefault(); // Bloquear la entrada
        }
    });

    document.getElementById('validateFormButton').addEventListener('click', function (event) {
        console.log('JavaScript cargado correctamente.');
        event.preventDefault();

        // Ocultar todos los mensajes de error previos
        const errorMessages = document.querySelectorAll('.error-message');
        errorMessages.forEach(function (message) {
            message.textContent = '';
        });

        // Validar campos obligatorios
        let isValid = true;

        const requiredFields = [
            { id: 'nombre_evento', errorId: 'error-nombre_evento', message: 'El nombre del evento es obligatorio.' },
            { id: 'diaEvento', errorId: 'error-diaEvento', message: 'Debe seleccionar un día para el evento.' },
            { id: 'horaInicioEvento', errorId: 'error-horaInicioEvento', message: 'Debe ingresar la hora de inicio.' },
            { id: 'horaEvento', errorId: 'error-horaEvento', message: 'Debe ingresar la hora final.' },
            { id: 'entrada', errorId: 'error-entrada', message: 'Debe especificar la entrada al evento.' },
            { id: 'razonEvento', errorId: 'error-razonEvento', message: 'Debe especificar la razón del evento.' },
            { id: 'descripcionEvento', errorId: 'error-descripcionEvento', message: 'Debe proporcionar una descripción del evento.' },
            { id: 'fotosEvento', errorId: 'error-fotosEvento', message: 'Debe adjuntar una foto válida en formato PNG.' }
        ];

        // Validar selección del campo "distrito"
        const distrito = document.getElementById('distrito');
        if (distrito.value === 'sin-lugar') {
            isValid = false;
            document.getElementById('error-distrito').textContent = 'Debe seleccionar un lugar para el evento.';
        }

        // Validar "aforo"
        const aforoInput = document.getElementById('aforo');
        const aforoValue = aforoInput.value.trim();

        if (!aforoValue) {
            isValid = false;
            document.getElementById('error-aforo').textContent = 'Debe ingresar el aforo para el evento.';
        } else if (!/^\d+$/.test(aforoValue)) {
            isValid = false;
            document.getElementById('error-aforo').textContent = 'El aforo debe ser un número.';
        } else if (distrito.value !== 'sin-lugar') {
            const selectedOption = distrito.options[distrito.selectedIndex];
            const maxAforo = parseInt(selectedOption.textContent.match(/Aforo (\d+)/)?.[1]);

            if (parseInt(aforoValue) > maxAforo) {
                isValid = false;
                document.getElementById('error-aforo').textContent = `El aforo no puede superar el máximo permitido (${maxAforo}).`;
            }
        }

        // Validar que la hora final sea mayor que la de inicio
        const horaInicio = document.getElementById('horaInicioEvento').value;
        const horaFin = document.getElementById('horaEvento').value;
        if (horaInicio && horaFin && horaFin <= horaInicio) {
            isValid = false;
            document.getElementById('error-horaEvento').textContent = 'La hora final debe ser posterior a la hora de inicio.';
        }

        // Validar tamaño y formato del archivo de imagen
        const fotosEventoInput = document.getElementById('fotosEvento');
        if (fotosEventoInput.files.length > 0) {
            const file = fotosEventoInput.files[0];
            if (file.type !== 'image/png') {
                isValid = false;
                document.getElementById('error-fotosEvento').textContent = 'El archivo debe ser una imagen PNG.';
            } else if (file.size > 5 * 1024 * 1024) { // 2 MB
                isValid = false;
                document.getElementById('error-fotosEvento').textContent = 'El archivo no puede superar los 5 MB.';
            }
        }

        // Si todo es válido, mostrar el modal
        if (isValid) {
            const modal = document.getElementById('modal');
            modal.style.display = 'block';

        }

    });

    function enviarFormulario() {
        console.log('Enviando formulario...');
        document.getElementById('eventoForm').submit();
    }

    // Inicializar contadores de caracteres para textareas
    function updateCharCount(textareaId, counterId) {
        const textarea = document.getElementById(textareaId);
        const counter = document.getElementById(counterId);

        if (textarea && counter) {
            counter.textContent = textarea.value.length + '/' + textarea.maxLength;
            textarea.addEventListener('input', function () {
                counter.textContent = textarea.value.length + '/' + textarea.maxLength;
            });
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        updateCharCount('entrada', 'charCountEntrada');
        updateCharCount('razonEvento', 'charCountRazon');
        updateCharCount('descripcionEvento', 'charCountDescripcion');
        updateCharCount('artistasInvitados', 'charCountArtistas');

        // Validar que la fecha mínima sea hoy
        const hoy = new Date().toISOString().split('T')[0];
        document.getElementById('diaEvento').setAttribute('min', hoy);
    });
</script>
</body>
</html>
