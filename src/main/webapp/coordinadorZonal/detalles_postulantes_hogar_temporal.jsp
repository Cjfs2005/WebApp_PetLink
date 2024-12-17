<%@ page import="com.example.webapp_petlink.beans.FotoPostulacionHogarTemporal" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %><%--
  Created by IntelliJ IDEA.
  User: GIANFRANCO
  Date: 15/12/2024
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Usuario coordinador = (Usuario) session.getAttribute("datosUsuario");
    String nombreCoordinador = coordinador.getNombres_coordinador();
    String apellidoCoordinador = coordinador.getApellidos_coordinador();
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>PetLink</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/perfil.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/ola2.css"/>

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
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/cz_solicitud_logo.png" class="icons">
                <h1 class="logo"><strong>DATOS DEL POSTULANTE</strong></h1>
            </header>



            <section class="banner">
                <div class="content">
                    <%
                        // Verificar que el atributo tiene datos
                        List<FotoPostulacionHogarTemporal> fotos = (List<FotoPostulacionHogarTemporal>) request.getAttribute("fotosConDetalles");
                        PostulacionHogarTemporal postulacion = null;

                        if (fotos != null && !fotos.isEmpty()) {
                            postulacion = fotos.get(0).getPostulacionHogarTemporal(); // Obtener el objeto de la postulación desde la primera foto
                        }
                    %>

                    <% if (postulacion != null) { %>
                    <!-- Información del perfil -->
                    <div class="perfil-info">
                        <div class="central-section">
                            <h2><%= postulacion.getUsuario_final().getNombres_usuario_final() + " " + postulacion.getUsuario_final().getApellidos_usuario_final() %></h2>
                        </div>
                    </div>
                    <hr>

                    <div class="row gtr-uniform">
                        <div class="col-6 col-12-xsmall">Edad:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getEdad_usuario() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Género:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getGenero_usuario() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Celular:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getCelular_usuario() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Dirección:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getUsuario_final().getDireccion() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Distrito:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getUsuario_final().getDistrito().getNombre_distrito() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Cantidad de cuartos:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getCantidad_cuartos() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Metraje de vivienda:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getMetraje_vivienda() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Tiene mascotas:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getTiene_mascotas() ? "Sí" : "No" %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Tipo de mascotas:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getTipo_mascotas() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">¿Tiene hijos?</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getTiene_hijos() ? "Sí" : "No" %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">¿Vive solo o con dependientes?</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getTiene_dependientes() ? "Con dependientes" : "Solo" %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">¿Trabaja remoto o presencial?</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getForma_trabajo() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Persona de referencia:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getNombre_persona_referencia() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Celular de persona de referencia:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getCelular_persona_referencia() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Fecha de inicio de temporal:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getFecha_inicio_temporal() %>" placeholder="" disabled/>
                        </div>

                        <div class="col-6 col-12-xsmall">Fecha de fin de temporal:</div>
                        <div class="col-6 col-12-xsmall">
                            <input type="text" value="<%= postulacion.getFecha_fin_temporal() %>" placeholder="" disabled/>
                        </div>
                    </div>
                    <br>
                    <p>Fotos del hogar temporal:</p>
                    <div class="contenedor-imagenes">
                        <% for (FotoPostulacionHogarTemporal foto : fotos) { %>
                        <img src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(foto.getFotoLugarTemporal()) %>"
                             alt="<%= foto.getNombreFotoLugarTemporal() %>">
                        <% } %>
                    </div>
                    <% } else { %>
                    <p>No se encontraron detalles para esta postulación.</p>
                    <% } %>
                </div>
            </section>

        </div>
    </div>

    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= coordinador.getId_usuario() %>" />
        <jsp:param name="nombresCoordinador" value="<%= coordinador.getNombres_coordinador() %>" />
        <jsp:param name="apellidosCoordinador" value="<%= coordinador.getApellidos_coordinador() %>" />
        <jsp:param name="zonaCoordinador" value="<%= coordinador.getZona().getNombre_zona()%>" />
    </jsp:include>

</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/browser.min.js"></script>
<script src="assets/js/breakpoints.min.js"></script>
<script src="assets/js/util.js"></script>
<script src="assets/js/main.js"></script>

</body>
</html>