<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.webapp_petlink.beans.SolicitudDonacionEconomica" %>
<%@ page import="com.example.webapp_petlink.beans.RegistroDonacionEconomica" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
<%@ page import="java.util.Base64" %>

<%
    Usuario albergue = (Usuario) session.getAttribute("datosUsuario");
    String nombreUsuario = albergue.getNombre_albergue();
    String fotoPerfilBase64 = "";
    if (albergue.getFoto_perfil() != null) {
        fotoPerfilBase64 = Base64.getEncoder().encodeToString(albergue.getFoto_perfil());
    }

    SolicitudDonacionEconomica solicitud = (SolicitudDonacionEconomica) request.getAttribute("solicitud");
%>
<!DOCTYPE html>
<html>
<head>
    <title>PetLink - Detalles de la Donación Económica</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/albergue/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/albergue/images/favicon.png" type="image/x-icon">
    <!-- Bootstrap y DataTables -->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <h1 class="logo"><strong>Detalles de la Donación Económica</strong></h1>
                <a href="<%=request.getContextPath()%>/PerfilAlbergueServlet" class="user-profile">
                    <% if (albergue.getFoto_perfil() != null) {%>
                    <span class="ocultar"><%=nombreUsuario%></span>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
                    <% } else {%>
                    <span class="ocultar"><%=nombreUsuario%></span>
                    <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" style="border-radius: 100%; height: 45px; width: 45px;object-fit: cover;">
                    <% } %>
                </a>
            </header>

            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <header>
                        <img src="images/imagen1Donaciones.png" class="icons">
                        <h2>Huellitas PUCP: Colecta de Dinero</h2>
                    </header>

                    <!-- Información de la Solicitud -->
                    <p><strong>Motivo:</strong> <%= solicitud.getMotivo() %></p>
                    <p><strong>Monto Solicitado:</strong> S/. <%= solicitud.getMonto_solicitado() %></p>

                    <!-- Código QR -->
                    <h3>Código QR para Donaciones</h3>
                    <div class="contenedor-imagenes">
                        <%
                            String imagenQRBase64 = "";
                            if (solicitud.getUsuario_albergue() != null && solicitud.getUsuario_albergue().getImagen_qr() != null) {
                                imagenQRBase64 = Base64.getEncoder().encodeToString(solicitud.getUsuario_albergue().getImagen_qr());
                            }
                            if (!imagenQRBase64.isEmpty()) {
                        %>
                        <img src="data:image/png;base64,<%= imagenQRBase64 %>" alt="Código QR">
                        <% } else { %>
                        <p>No hay código QR disponible.</p>
                        <% } %>
                    </div>

                    <div id="qrModal" class="modal1">
                        <div class="modal-content1">
                            <span class="close-btn">&times;</span>
                            <h2>Código QR</h2>
                            <img id="qrImage" src="" alt="Código QR" style="width: 100%; height: auto;">
                            <p id="noImageText" style="display:none;">No hay imagen disponible.</p>
                        </div>
                    </div>
                    <%
                        // Recuperar la lista de donantes desde el request
                        List<RegistroDonacionEconomica> registrosDonacion = (List<RegistroDonacionEconomica>) request.getAttribute("donantes");
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    %>
                    <!-- Tabla de Usuarios Donantes -->
                    <h3>Lista de Usuarios Donantes</h3>
                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>Nombres y Apellidos</th>
                                <th>Fecha de Donación</th>
                                <th>Monto Donado</th>
                                <th>Ver QR</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (registrosDonacion != null) {
                                    for (RegistroDonacionEconomica registro : registrosDonacion) {
                                        String imagenBase64 = registro.getImagenDonacionEconomica() != null
                                                ? "data:image/png;base64," + Base64.getEncoder().encodeToString(registro.getImagenDonacionEconomica())
                                                : null;
                            %>
                            <tr>
                                <td><%= registro.getUsuarioFinal().getNombres_usuario_final() %> <%= registro.getUsuarioFinal().getApellidos_usuario_final() %></td>
                                <td><%= registro.getFechaHoraRegistro().format(dateFormatter) %></td>
                                <td>S/. <%= registro.getMontoDonacion() %></td>
                                <td>
                                    <!-- Ícono con el atributo data-image -->
                                    <a href="#" class="icon fas fa-eye text-dark" data-image="<%= imagenBase64 != null ? imagenBase64 : "" %>"></a>
                                </td>
                            </tr>
                            <%
                                    }
                                }else{
                            %>
                            <tr>
                                <td colspan="4">No hay donantes registrados para esta solicitud.</td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </div>
                    <!-- Botones de Modificar y Eliminar -->
                    <div class="row gtr-uniform mt-4">
                        <div class="col-12">
                            <ul class="actions form-buttons">
                                <li>
                                    <a href="<%= request.getContextPath()%>/DonacionEconomicaServlet?action=modificar&id=<%= ((SolicitudDonacionEconomica) request.getAttribute("solicitud")).getId_solicitud_donacion_economica() %>"
                                       class="button primary big">Modificar</a>
                                </li>
                                <li>
                                    <a href="<%= request.getContextPath()%>/DonacionEconomicaServlet?action=eliminar&id=<%= ((SolicitudDonacionEconomica) request.getAttribute("solicitud")).getId_solicitud_donacion_economica() %>"
                                       class="button big" onclick="return confirm('¿Está seguro de eliminar esta solicitud?');">Eliminar</a>
                                </li>
                            </ul>
                        </div>
                    </div>


                    <!-- Script para controlar el modal -->
                    <script>
                        document.addEventListener('DOMContentLoaded', function () {
                            const modal = document.getElementById("qrModal");
                            const qrImage = document.getElementById("qrImage");
                            const noImageText = document.getElementById("noImageText");
                            const closeModal = document.querySelector(".close-btn");

                            // Evento para abrir el modal
                            document.querySelectorAll('.fa-eye').forEach(item => {
                                item.addEventListener('click', function (event) {
                                    event.preventDefault();
                                    const imageSrc = this.getAttribute("data-image");

                                    if (imageSrc) {
                                        qrImage.src = imageSrc;
                                        qrImage.style.display = "block";
                                        noImageText.style.display = "none";
                                    } else {
                                        qrImage.style.display = "none";
                                        noImageText.style.display = "block";
                                    }
                                    modal.style.display = "block";
                                });
                            });

                            // Evento para cerrar el modal
                            closeModal.onclick = function () {
                                modal.style.display = "none";
                                qrImage.src = "";
                                noImageText.style.display = "none";
                            };

                            // Cerrar modal al hacer clic fuera del contenido
                            window.onclick = function (event) {
                                if (event.target === modal) {
                                    modal.style.display = "none";
                                    qrImage.src = "";
                                    noImageText.style.display = "none";
                                }
                            };
                        });
                    </script>

                    <!-- Estilos del Modal -->
                    <style>
                        .modal1 {
                            display: none;
                            position: fixed;
                            z-index: 1000;
                            left: 0;
                            top: 0;
                            width: 100%;
                            height: 100%;
                            background-color: rgba(0, 0, 0, 0.7);
                        }

                        .modal-content1 {
                            background-color: #fff;
                            margin: 10% auto;
                            padding: 20px;
                            width: 80%;
                            max-width: 500px;
                            text-align: center;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                            position: relative;
                        }

                        .close-btn {
                            color: #333;
                            position: absolute;
                            top: 10px;
                            right: 15px;
                            font-size: 28px;
                            font-weight: bold;
                            cursor: pointer;
                        }

                        .close-btn:hover, .close-btn:focus {
                            color: #000;
                            text-decoration: none;
                        }

                        #noImageText {
                            color: red;
                            font-size: 16px;
                            margin-top: 10px;
                        }
                    </style>

                    <!-- DataTable Script -->
                    <script>
                        new DataTable('#example', {
                            language: {
                                sSearch: "Buscar:",
                                sLengthMenu: "Mostrar MENU registros",
                                sZeroRecords: "No se encontraron resultados",
                                sEmptyTable: "Ningún dato disponible en esta tabla",
                                sInfo: "Mostrando registros del START al END de un total de TOTAL registros",
                                sInfoEmpty: "Mostrando registros del 0 al 0 de un total de 0 registros",
                                sInfoFiltered: "(filtrado de un total de MAX registros)",
                                sLoadingRecords: "Cargando..."
                            }
                        });
                    </script>
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
<script src="<%=request.getContextPath()%>/albergue/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/albergue/assets/js/main.js"></script>
</body>
</html>
