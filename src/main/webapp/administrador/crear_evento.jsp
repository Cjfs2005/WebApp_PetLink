<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.*" %>
<%@ page import="java.util.ArrayList" %>
<%
    ArrayList<Distrito> distritos = (ArrayList<Distrito>) request.getAttribute("distritos");
    Usuario administrador = (Usuario) session.getAttribute("datosUsuario");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/administrador/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>Gestión de eventos</strong></h1>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="<%=request.getContextPath()%>/administrador/images/form.png" class="icons">
                        <h2>Formulario para crear los espacios para los eventos </h2>
                    </header>

                    <form id="formulario-lugar" method="POST" action="<%=request.getContextPath()%>/ListaLugaresAdminServlet?action=guardar">
                        <div class="row gtr-uniform">
                            <!-- Input para Nombre -->
                            <div class="col-6 col-12-xsmall">
                                <label for="nombre-lugar" class="input-label">Nombre</label>
                                <input type="text" id="nombre-lugar" name="nombre-lugar" value="" placeholder="Ingrese el nombre del lugar" />
                            </div>

                            <!-- Input para Dirección -->
                            <div class="col-6 col-12-xsmall">
                                <label for="direccion" class="input-label">Dirección</label>
                                <input type="text" id="direccion" name="direccion" value="" placeholder="Ingrese la dirección" />
                            </div>

                            <!-- Input para Aforo máximo -->
                            <div class="col-6 col-12-xsmall">
                                <label for="aforo" class="input-label">Aforo máximo</label>
                                <input type="text" id="aforo" name="aforo" value="" placeholder="Ingrese el aforo máximo" />
                            </div>

                            <!-- Input para Distrito -->
                            <div class="col-6 col-12-xsmall">
                                <label for="distrito" class="input-label">Distrito</label>
                                <select id="distrito" name="distrito">
                                    <option value="">Seleccione su distrito</option>
                                    <%
                                        for (Distrito distrito : distritos) {
                                    %>
                                    <option value="<%=distrito.getId_distrito()%>"><%=distrito.getNombre_distrito()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>

                            <!-- Botones -->
                            <div class="col-12">
                                <ul class="actions form-buttons">
                                    <li><a href="#" class="button primary big" id="creationButton">Creación de lugar</a></li>
                                    <li><a href="<%=request.getContextPath()%>/ListaLugaresAdminServlet?action=listar" class="button big">Regresar</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </section>
        </div>
    </div>

<jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
</jsp:include>

<!-- Modal para creación de lugar -->
<div id="creationModal" class="modal">
    <div class="modal-content">
        <p>¡Se creó con éxito!</p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big" id="closeCreationModal">Aceptar</a></li>
        </ul>
    </div>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const nombreInput = document.getElementById('nombre-lugar');
        const direccionInput = document.getElementById('direccion');
        const aforoInput = document.getElementById('aforo');
        const distritoSelect = document.getElementById('distrito');
        const creationButton = document.getElementById('creationButton');
        const creationModal = document.getElementById('creationModal');
        const closeCreationModalButton = document.getElementById('closeCreationModal');
        const form = document.getElementById('formulario-lugar');

        // Función para verificar si todos los campos son válidos
        function verificarCampos() {
            const camposValidos = (
                nombreInput.value.trim() !== "" && /^[a-zA-Z\s]+$/.test(nombreInput.value) &&
                direccionInput.value.trim() !== "" &&
                /^[0-9]{1,4}$/.test(aforoInput.value) &&
                distritoSelect.value !== ""
            );

            if (camposValidos) {
                creationButton.classList.remove('disabled');
                creationButton.style.pointerEvents = 'auto';
            } else {
                creationButton.classList.add('disabled');
                creationButton.style.pointerEvents = 'none';
            }
        }

        // Permitir solo letras en el campo "Nombre"
        nombreInput.addEventListener('input', () => {
            nombreInput.value = nombreInput.value.replace(/[^a-zA-Z\s]/g, ''); // Elimina números y caracteres especiales
            verificarCampos();
        });

        // Permitir solo números en el campo "Aforo máximo" y limitar a 4 dígitos
        aforoInput.addEventListener('input', () => {
            aforoInput.value = aforoInput.value.replace(/\D/g, '').slice(0, 4); // Elimina letras y limita la longitud
            verificarCampos();
        });

        // Verificar los demás campos al cambiar
        direccionInput.addEventListener('input', verificarCampos);
        distritoSelect.addEventListener('change', verificarCampos);

        // Llamado inicial para verificar el estado del botón al cargar la página
        verificarCampos();

        // Manejo del modal de "Creación de lugar"
        creationButton.addEventListener('click', function(e) {
            e.preventDefault(); // Evitar la recarga
            if (!creationButton.classList.contains('disabled')) {
                creationModal.classList.add('show');
            }
        });

        // Cerrar modal y redirigir al hacer clic en "Aceptar" en el modal de creación
        closeCreationModalButton.addEventListener('click', function(e) {
            e.preventDefault(); // Evitar comportamiento por defecto
            form.submit(); // Enviar el formulario
        });
    });
</script>
</body>
</html>