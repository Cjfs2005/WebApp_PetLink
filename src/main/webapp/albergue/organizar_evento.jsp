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
%>
<%
    ArrayList<LugarEvento> lugares = (ArrayList<LugarEvento>) request.getAttribute("lugares");
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>Formulario para Organizar un Evento Benéfico</title>
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
                <h1 class="logo"><strong>Formulario para Organizar un Evento Benéfico</strong></h1>
                <a href="<%=request.getContextPath()%>/albergue/perfil.jsp" class="user-profile">
                    <span class="ocultar"><%=nombreUsuario %></span> <img src="<%=request.getContextPath()%>/albergue/images/logo_huellitas.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;"></img>
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/form.png" class="icons">
                        <h2>Detalles del Evento</h2>
                    </header>
                    <p><strong>Descripción:</strong> Completa el formulario para registrar tu evento benéfico. Incluye la fecha, lugar, aforo y otros detalles importantes.</p>

                    <form method="post" action="<%=request.getContextPath()%>/EventoAlbergueServlet?action=guardar">
                        <div class="row gtr-uniform">
                                <!--Nombre del evento-->
                                <div class="col-6 col-12-xsmall">
                                    <label for="nombre_evento" class="input-label">Nombre del evento</label>
                                    <input type="text" id="nombre_evento" name="nombre_evento" maxlength="100" required />
                                </div>

                                <!-- Día del evento -->
                                <div class="col-6 col-12-xsmall">
                                    <label for="diaEvento" class="input-label">Día del evento</label>
                                    <input type="date" id="diaEvento" name="diaEvento" min="" onkeydown="return false;" required />
                                </div>

                                <!-- Razón del evento -->
                                <div class="col-12">
                                    <label for="razonEvento" class="input-label">Razón del evento</label>
                                    <textarea id="razonEvento" name="razonEvento" class="text-area" maxlength="500" placeholder="" required></textarea>
                                    <span class="charCount" id="charCountRazon">0/500</span>
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    <label for="horaInicioEvento" class="input-label">Hora de inicio del evento</label>
                                    <input type="time" id="horaInicioEvento" name="horaInicioEvento" onkeydown="return false;" required
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
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    <label for="horaEvento" class="input-label">Hora final del evento</label>
                                    <input type="time" id="horaEvento" name="horaEvento" onkeydown="return false;" required style="appearance: none;
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
                                </div>

                                <!--Entrada para el evento-->
                                <div class="col-12">
                                    <label for="entrada" class="input-label">Entrada para el evento</label>
                                    <textarea id="entrada" name="entrada" class="text-area" maxlength="100" placeholder="" required></textarea>
                                    <span class="charCount" id="charCountEntrada">0/100</span>
                                </div>

                                <!-- Lugar del evento -->
                                <div class="col-12">
                                    <label for="distrito" class="input-label">Lugar del evento</label>
                                    <select id="distrito" name="distrito">
                                        <option value="sin-lugar" disabled selected>Seleccione el lugar</option>
                                        <%
                                            for (LugarEvento lugar : lugares){
                                        %>
                                        <option value="<%=lugar.getId_lugar_evento()%>"><%=lugar.getNombre_lugar_evento()%> - <%=lugar.getDistrito().getNombre_distrito()%>. Aforo <%=lugar.getAforo_maximo()%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>

                                <!-- Descripción del evento -->
                                <div class="col-12">
                                    <label for="descripcionEvento" class="input-label">Descripción del evento (máximo 500 caracteres)</label>
                                    <textarea id="descripcionEvento" name="descripcionEvento" class="text-area" maxlength="500" placeholder="" required></textarea>
                                    <span class="charCount" id="charCountDescripcion">0/500</span>
                                </div>

                                <!-- Artistas o proveedores invitados -->
                                <div class="col-12">
                                    <label for="artistasInvitados" class="input-label">Artistas o proveedores invitados</label>
                                    <textarea id="artistasInvitados" name="artistasInvitados" maxlength="300" class="text-area" placeholder=""></textarea>
                                    <span class="charCount" id="charCountArtistas">0/300</span>
                                </div>

                                <div class="col-12">
                                    <label for="fotosEvento" class="input-label">Adjuntar fotos del evento (solo PNG)</label>
                                    <input type="file" id="fotosEvento" name="fotosEvento" accept=".png" required />
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
                                        <li><a href="#" class="button primary big" id="openModal">Publicar</a></li>
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

</div>

<!--Modal-->
<div id="modal" class="modal">
    <div class="modal-content">
        <p>Se ha publicado el post con éxito.</p>
        <ul class="actions modal-buttons">
            <li><a href="EventoBenefico.html" class="button primary big" id="acceptButton">Aceptar</a></li>
        </ul>
    </div>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>

<!-- Validaciones -->
<script>


    // Validar que la hora de fin sea mayor que la hora de inicio
    function validarHoraFinal() {
        const horaInicio = document.getElementById('horaInicioEvento').value;
        const horaFin = document.getElementById('horaFinEvento').value;

        if (horaFin <= horaInicio) {
            alert('La hora final debe ser posterior a la hora de inicio.');
            document.getElementById('horaFinEvento').value = ''; // Limpiar campo
        }
    }



    document.getElementById('horaFinEvento').addEventListener('change', validarHoraFinal);
</script>

<!-- Contador de caracteres para la descripción -->
<script>
    const textarea = document.getElementById('descripcionEvento');
    const charCount = document.getElementById('charCount');

    textarea.addEventListener('input', function() {
        const currentLength = textarea.value.length;
        charCount.textContent = `${currentLength}/500`;
    });
</script>

<!-- Modal Scripts -->
<script>
    // Obtener los elementos del modal y el botón
    const modal = document.getElementById('modal');
    const openModalButton = document.getElementById('openModal');
    const acceptButton = document.getElementById('acceptButton');

    // Mostrar el modal cuando se hace clic en el botón "Publicar"
    openModalButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevenir el comportamiento predeterminado del enlace
        modal.style.display = 'block'; // Mostrar el modal
    });

    // Ocultar el modal cuando se hace clic en "Aceptar"
    acceptButton.addEventListener('click', function() {
        modal.style.display = 'none'; // Ocultar el modal
    });

    // Cerrar el modal si el usuario hace clic fuera del contenido del modal
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none'; // Ocultar el modal
        }
    });
</script>

<!-- Contador de caracteres para los textareas -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Definimos los textareas y sus contadores asociados
        const areas = [
            { textarea: 'razonEvento', counter: 'charCountRazon' },
            { textarea: 'entrada', counter: 'charCountEntrada' },
            { textarea: 'descripcionEvento', counter: 'charCountDescripcion' },
            { textarea: 'artistasInvitados', counter: 'charCountArtistas' }
        ];

        // Asignamos eventos a cada textarea
        areas.forEach(area => {
            const textarea = document.getElementById(area.textarea);
            const counter = document.getElementById(area.counter);

            if (textarea && counter) {
                // Actualizamos el contador al escribir
                textarea.addEventListener('input', function () {
                    const currentLength = textarea.value.length;
                    counter.textContent = `${currentLength}/${textarea.maxLength}`;
                });
            }
        });
    });

</script>

<!-- Validación de hora de fin después de hora de inicio -->
<script>
    function validarHoraFinal() {
        const horaInicio = document.getElementById('horaInicioEvento').value;
        const horaFin = document.getElementById('horaFinEvento').value;

        if (horaFin && horaInicio && horaFin <= horaInicio) {
            alert('La hora final debe ser posterior a la hora de inicio.');
            document.getElementById('horaFinEvento').value = ''; // Limpiar campo
        }
    }

    document.getElementById('horaFinEvento').addEventListener('change', validarHoraFinal);
</script>

<script>

    // Validación de fechas
    const fechaInicio = document.getElementById('diaEvento');

    document.addEventListener('DOMContentLoaded', function() {
        // Configurar la fecha mínima para fechaInicio como la fecha actual
        const hoy = new Date().toISOString().split("T")[0];
        fechaInicio.setAttribute("min", hoy);

    });
</script>
</body>
</html>







