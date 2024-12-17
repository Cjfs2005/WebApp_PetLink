<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 13/11/2024
  Time: 12:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE HTML>
<html lang="es">
<head>
    <title>PetLink - Detalles del Hogar Temporal</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/ola2.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>Detalles del hogar temporal</strong></h1>
                <%
                    Usuario usuario = (Usuario) request.getAttribute("usuario");
                    String fotoPerfilBase64 = "";
                    if (usuario != null && usuario.getFoto_perfil() != null) {
                        fotoPerfilBase64 = Base64.getEncoder().encodeToString(usuario.getFoto_perfil());
                    }
                %>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <span class="ocultar"><%= usuario.getNombre_albergue() %></span>
                    <img src="<%= !fotoPerfilBase64.isEmpty() ? "data:image/png;base64," + fotoPerfilBase64 : request.getContextPath() + "/albergue/images/default_profile.png" %>"
                         style="border-radius: 100%; height: 45px; width: 45px; object-fit: cover;">
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">

                    <%
                        PostulacionHogarTemporal postulacion = (PostulacionHogarTemporal) request.getAttribute("postulacion");
                        ArrayList<byte[]> fotos = (ArrayList<byte[]>) request.getAttribute("fotos");
                    %>

                    <!-- Información del perfil -->
                    <div class="perfil-info">
                        <div class="left-section">
                            <%if(postulacion.getUsuario_final().getFoto_perfil()!= null){%>
                            <img src="<%="data:image/png;base64," +Base64.getEncoder().encodeToString(postulacion.getUsuario_final().getFoto_perfil())%>" alt="Foto de perfil">
                            <%}%>
                        </div>
                        <div class="central-section">
                            <h2><%=postulacion.getUsuario_final().getNombres_usuario_final()%> <%=postulacion.getUsuario_final().getApellidos_usuario_final()%></h2>
                        </div>
                    </div>
                    <hr>

                    <div class="seccion">
                        <h3>Información</h3>
                        <div class="seccion-contenido">
                            <div class="row gtr-uniform">
                                <div class="col-6 col-12-xsmall">
                                    Edad:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getEdad_usuario() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Género:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getGenero_usuario() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Celular:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getCelular_usuario() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Dirección:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getUsuario_final().getDireccion() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Distrito:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getUsuario_final().getDistrito().getNombre_distrito() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Cantidad de cuartos:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getCantidad_cuartos() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Metraje de la vivienda:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getMetraje_vivienda() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    ¿Tiene mascotas?:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getTiene_mascotas()?"Sí":"No" %>" disabled />
                                </div>

                                <%
                                    if(postulacion.getTiene_mascotas()){
                                %>
                                <div class="col-6 col-12-xsmall">
                                    Tipo y cantidad de mascotas:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getTipo_mascotas()%>" disabled />
                                </div>
                                <%
                                    }
                                %>

                                <div class="col-6 col-12-xsmall">
                                    ¿Tiene hijos?:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getTiene_hijos()?"Sí":"No" %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    ¿Vive solo o con dependientes?:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getTiene_dependientes()?"Con dependiente":"Solo" %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    ¿Trabaja remoto o presencial?:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getForma_trabajo()%>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Nombre de persona de referencia:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getNombre_persona_referencia() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Celular de persona de referencia:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getCelular_persona_referencia() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Fecha de inicio de temporal:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getFecha_inicio_temporal() %>" disabled />
                                </div>

                                <div class="col-6 col-12-xsmall">
                                    Género:
                                </div>
                                <div class="col-6 col-12-xsmall">
                                    <input type="text" value="<%= postulacion.getFecha_fin_temporal() %>" disabled />
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="seccion">
                        <h3>Fotos del Hogar Temporal</h3>
                        <div class="seccion-contenido">
                            <div class="contenedor-imagenes">
                                    <%
                                        if (fotos != null && !fotos.isEmpty()) {
                                            for (byte[] foto : fotos) {
                                    %>
                                    <img src="data:image/png;base64,<%= Base64.getEncoder().encodeToString(foto) %>" alt="Foto del Hogar">
                                    <%
                                            }
                                        }
                                    %>
                            </div>
                        </div>
                    </div>

                    <div class="row gtr-uniform">
                        <div class="col-12">
                            <ul class="actions form-buttons">
                                <li><a href="<%=request.getContextPath()%>/TemporalAlbergueServlet?action=solicitar&id_postulacion=<%= postulacion.getId_postulacion_hogar_temporal() %>" class="button primary big">Contactar</a></li>
                                <li><a href="<%=request.getContextPath()%>/TemporalAlbergueServlet?action=listar" class="button big">Regresar</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <!-- Sidebar -->
    <jsp:include page="navbar.jsp">
        <jsp:param name="idUsuario" value="<%= usuario.getId_usuario() %>" />
        <jsp:param name="nombreAlbergue" value="<%= usuario.getNombre_albergue() %>" />
        <jsp:param name="fotoPerfilBase64" value="<%= fotoPerfilBase64 %>" />
    </jsp:include>

</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
</body>
</html>
