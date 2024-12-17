<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %><%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 16/12/2024
  Time: 10:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Usuario albergue = (Usuario) session.getAttribute("usuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/albergue/assets/css/popup-window.css">
    <script src="${pageContext.request.contextPath}/albergue/assets/js/pagination.js"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/albergue/images/favicon.png" type="image/x-icon">
</head>
<body>
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>BIENVENIDO A PETLINK</strong></h1>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/form.png" class="icons">
                        <h2>Estás a un paso de completar tu perfil</h2>
                    </header>
                    <p><strong>Termine de configurar su perfil.</strong> Ingrese información detallada para que todos los usuarios puedan conocer más acerca de su albergue. Esta información será visible para todos los usuarios.</p>

                    <br>
<form action="SegundoRegistroAlbergueServlet" method="POST" enctype="multipart/form-data" onsubmit="return confirm('¿Estás seguro de los datos que estás enviando?');">
    <div class="row gtr-uniform">
        <!-- Información General del Albergue -->
        <input type="hidden" name="action" value="guardar">
        <div class="col-12">
            <h2>Información general del albergue</h2>
        </div>
        <div class="col-6 col-12-xsmall">
            <label for="nombres_encargado">Nombres del encargado</label>
            <input type="text" id="nombres_encargado" name="nombres_encargado" value="<%=albergue.getNombres_encargado()%>" maxlength="45" disabled readonly/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="apellidos_encargado">Apellidos del encargado</label>
            <input type="text" id="apellidos_encargado" name="apellidos_encargado" value="<%=albergue.getApellidos_encargado()%>" maxlength="45" disabled readonly/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="correo_electronico">Correo electrónico</label>
            <input type="email" id="correo_electronico" name="correo_electronico" value="<%=albergue.getCorreo_electronico()%>" disabled readonly/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="nombre_albergue">Nombre del albergue</label>
            <input type="text" id="nombre_albergue" name="nombre_albergue" value="<%=albergue.getNombre_albergue()%>" maxlength="45" disabled readonly/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="cantidad_animales">Cantidad de animales albergados</label>
            <input type="text" id="cantidad_animales" name="cantidad_animales" maxlength="5" required/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="espacio_disponible">Espacio disponible para albergar más animales</label>
            <input type="text" id="espacio_disponible" name="espacio_disponible" maxlength="5" required/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="anio_creacion">Año de creación</label>
            <input type="text" id="anio_creacion" name="anio_creacion" required/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="distrito">Distrito</label>
            <select id="distrito" name="distrito" required>
                <option value="" selected>Seleccione su distrito</option>
                <!-- Norte -->
                <option value="1">Ancon</option>
                <option value="2">Santa Rosa</option>
                <option value="3">Carabayllo</option>
                <option value="4">Puente Piedra</option>
                <option value="5">Comas</option>
                <option value="6">Los Olivos</option>
                <option value="7">San Martín de Porres</option>
                <option value="8">Independencia</option>

                <!-- Sur -->
                <option value="9">San Juan de Miraflores</option>
                <option value="10">Villa María del Triunfo</option>
                <option value="11">Villa el Salvador</option>
                <option value="12">Pachacamac</option>
                <option value="13">Lurin</option>
                <option value="14">Punta Hermosa</option>
                <option value="15">Punta Negra</option>
                <option value="16">San Bartolo</option>
                <option value="17">Santa María del Mar</option>
                <option value="18">Pucusana</option>

                <!-- Este -->
                <option value="19">San Juan de Lurigancho</option>
                <option value="20">Lurigancho/Chosica</option>
                <option value="21">Ate</option>
                <option value="22">El Agustino</option>
                <option value="23">Santa Anita</option>
                <option value="24">La Molina</option>
                <option value="25">Cieneguilla</option>

                <!-- Oeste -->
                <option value="26">Rimac</option>
                <option value="27">Cercado de Lima</option>
                <option value="28">Breña</option>
                <option value="29">Pueblo Libre</option>
                <option value="30">Magdalena</option>
                <option value="31">Jesus María</option>
                <option value="32">La Victoria</option>
                <option value="33">Lince</option>
                <option value="34">San Isidro</option>
                <option value="35">San Miguel</option>
                <option value="36">Surquillo</option>
                <option value="37">San Borja</option>
                <option value="38">Santiago de Surco</option>
                <option value="39">Barranco</option>
                <option value="40">Chorrillos</option>
                <option value="41">San Luis</option>
                <option value="42">Miraflores</option>
            </select>
        </div>

        <div class="col-12">
            <label for="direccion">Dirección del albergue</label>
            <input type="text" id="direccion" name="direccion" maxlength="150" required/>
        </div>

        <div class="col-12">
            <label for="sobre_nosotros">Sobre nosotros</label>
            <textarea id="sobre_nosotros" name="sobre_nosotros" maxlength="300" placeholder="Ingresar una descripción sobre su albergue" required></textarea>
        </div>

        <div class="col-12">
            <label for="foto_perfil">Foto de perfil del albergue</label>
            <input type="file" id="foto_perfil" name="foto_perfil" accept=".png" required/>
        </div>

        <div class="col-12">
            <label for="foto_portada">Foto de portada del albergue</label>
            <input type="file" id="foto_portada" name="foto_portada" accept=".png" required/>
        </div>
    </div>

    <br>

    <!-- Información para Donaciones -->
    <div class="row gtr-uniform">
        <div class="col-12">
            <h2>Información para donaciones</h2>
        </div>
        <div class="col-12">
            <label for="punto_acopio1">Dirección de punto de acopio</label>
            <input type="text" id="punto_acopio1" name="punto_acopio1" maxlength="45" required/>
        </div>

        <div class="col-12">
            <label for="direccion_donaciones">Dirección de donaciones</label>
            <input type="text" id="direccion_donaciones" name="direccion_donaciones" maxlength="45" required/>
        </div>

        <div class="col-12">
            <label for="nombre_contacto_donaciones">Nombre del contacto para donaciones</label>
            <input type="text" id="nombre_contacto_donaciones" name="nombre_contacto_donaciones" required/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="numero_contacto_donaciones">Número de contacto para donaciones</label>
            <input type="text" id="numero_contacto_donaciones" name="numero_contacto_donaciones" required/>
        </div>

        <div class="col-6 col-12-xsmall">
            <label for="numero_yape_plin">Número de Yape o Plin</label>
            <input type="text" id="numero_yape_plin" name="numero_yape_plin" required/>
        </div>

        <div class="col-12">
            <label for="fotoQR">Código QR para donaciones</label>
            <input type="file" id="fotoQR" name="fotoQR" accept=".png" required/>
        </div>
    </div>

    <div class="col-12">
        <ul class="actions form-buttons">
            <li><button type="submit" class="button primary big">Aceptar</button></li>
        </ul>
    </div>
</form>
                </div>
            </section>

        </div>
    </div>
</div>
<!-- Scripts -->
<script src="${pageContext.request.contextPath}/albergue/assets/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/browser.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/breakpoints.min.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/util.js"></script>
<script src="${pageContext.request.contextPath}/albergue/assets/js/main.js"></script>
<!-- Validaciones -->
<script>
    // Función para validar que solo se ingresen números en un campo
    function validarSoloNumeros(input) {
        input.value = input.value.replace(/[^0-9]/g, '');
    }

    // Función para validar el año de creación (4 dígitos)
    function validarAnioCreacion(input) {
        if (input.value.length > 4) {
            input.value = input.value.slice(0, 4);
        }
    }

    // Función para validar que el número de contacto y Yape/Plin tenga 9 dígitos
    function validarNumero9Digitos(input) {
        input.value = input.value.replace(/[^0-9]/g, '');
        if (input.value.length > 9) {
            input.value = input.value.slice(0, 9);
        }
    }

    function validarUrl(input) {
        const urlRegex = /^https:\/\/(www\.)?instagram\.com\/[a-zA-Z0-9._]{1,30}\/?$/;
        if (!urlRegex.test(input.value)) {
            alert("Por favor ingrese una URL válida para Instagram (ejemplo: https://www.instagram.com/username).");
            input.value = ''; // Limpiar el campo si no es válido
        }
    }

    // Aplicar validaciones cuando se escriba en los campos
    document.getElementById('cantidad_animales').addEventListener('input', function() {
        validarSoloNumeros(this);
    });

    document.getElementById('espacio_disponible').addEventListener('input', function() {
        validarSoloNumeros(this);
    });


    document.getElementById('anio_creacion').addEventListener('input', function() {
        validarSoloNumeros(this);
        validarAnioCreacion(this);
    });

    document.getElementById('numero_contacto_donaciones').addEventListener('input', function() {
        validarNumero9Digitos(this);
    });

    document.getElementById('numero_yape_plin').addEventListener('input', function() {
        validarNumero9Digitos(this);
    });

    document.getElementById('url_instagram').addEventListener('blur', function() {
        validarUrl(this);
    });
</script>

<!-- Contador de caracteres para el campo "Sobre nosotros" -->
<script>
    const textarea = document.getElementById('sobre_nosotros');
    const charCount = document.getElementById('charCount');

    textarea.addEventListener('input', function() {
        const currentLength = textarea.value.length;
        charCount.textContent = `${currentLength}/300`;
    });
</script>

<script>
    // Función para validar cantidad máxima de animales albergados (10,000)
    document.getElementById('cantidad_animales').addEventListener('input', function() {
        if (parseInt(this.value) > 10000) {
            alert("La cantidad de animales albergados no puede exceder los 10,000.");
            this.value = '';
        }
    });

    // Validación de formato PNG en la selección de archivos de imagen
    function validarFormatoPNG(input) {
        if (!input.files[0].name.endsWith('.png')) {
            alert("Solo se permiten archivos en formato PNG.");
            input.value = ''; // Limpiar el campo si no es PNG
        }
    }

    document.getElementById('foto_perfil').addEventListener('change', function() {
        validarFormatoPNG(this);
    });

    document.getElementById('foto_portada').addEventListener('change', function() {
        validarFormatoPNG(this);
    });

    document.getElementById('fotoQR').addEventListener('change', function() {
        validarFormatoPNG(this);
    });
</script>

<script>
    // Validación para que el modal solo se abra si todos los campos obligatorios están completos
    document.getElementById('openModal').addEventListener('click', function(event) {
        event.preventDefault(); // Prevenir el comportamiento predeterminado

        // Obtener los campos obligatorios
        const cantidadAnimales = document.getElementById('cantidad_animales');
        const espacioDisponible = document.getElementById('espacio_disponible');
        const anioCreacion = document.getElementById('anio_creacion');
        const distrito = document.getElementById('distrito');
        const direccion = document.getElementById('direccion');
        const sobreNosotros = document.getElementById('sobre_nosotros');
        const fotoPerfil = document.getElementById('foto_perfil');
        const fotoPortada = document.getElementById('foto_portada');
        const puntoAcopio1 = document.getElementById('punto_acopio1');
        const dirDonaciones = document.getElementById('direccion_donaciones');
        const nombreContacto = document.getElementById('nombre_contacto_donaciones');
        const numeroContacto = document.getElementById('numero_contacto_donaciones');
        const numeroYape = document.getElementById('numero_yape_plin');
        const fotoQR = document.getElementById('fotoQR');
        const urlInstagram = document.getElementById('url_instagram');

        // Verificar si los campos están vacíos y crear un array con todos los campos obligatorios
        const camposObligatorios = [
            cantidadAnimales, espacioDisponible, anioCreacion, distrito, direccion, sobreNosotros,
            fotoPerfil, fotoPortada, puntoAcopio1, dirDonaciones, nombreContacto,
            numeroContacto, numeroYape, fotoQR, urlInstagram
        ];

        // Revisar si algún campo está vacío
        const camposIncompletos = camposObligatorios.some(campo => {
            return !campo.value.trim();
        });

        // Mostrar alerta o abrir el modal
        if (camposIncompletos) {
            alert("Por favor, complete todos los campos obligatorios antes de enviar.");
            return; // No abrir el modal
        }

        // Mostrar el modal si todos los campos están completos
        document.getElementById('modal').classList.add('show');
    });

    // Función para cerrar el modal cuando se hace clic en "Aceptar"
    document.getElementById('acceptButton').addEventListener('click', function() {
        window.location.href = 'perfil.html';
    });
</script>
</body>
</html>
