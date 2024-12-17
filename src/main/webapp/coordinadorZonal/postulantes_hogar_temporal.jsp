<%@ page import="com.example.webapp_petlink.beans.PostulacionHogarTemporal" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.webapp_petlink.beans.Usuario" %>
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
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/main.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/aditional.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/coordinadorZonal/assets/css/popup-window.css">
    <link rel="icon" href="<%=request.getContextPath()%>/coordinadorZonal/images/favicon.png" type="image/x-icon">

    <!--Agregar-->

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/2.1.8/css/dataTables.bootstrap5.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
    <script type="text/javascript" src="https://cdn.datatables.net/2.1.8/js/dataTables.bootstrap5.js"></script>
    <!--Fin Agregar-->

</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <img src="<%=request.getContextPath()%>/coordinadorZonal/images/ht_postulantes.png" class="icons">
                <h1 class="logo"><strong>POSTULANTES A HOGAR TEMPORAL</strong></h1>
            </header>

            <!-- Banner -->
            <!-- Banner -->
            <section class="banner">
                <div class="content">
                    <p><strong>Descripción:</strong> En la siguiente tabla se muestra a los usuarios que desean ser hogares temporales.
                        Usted tendrá que revisar su información, llamar al postulante y realizar una visita inopinada en el periodo acordado.
                        Después de haber validado, aceptará o rechazará su postulación.
                        Una vez aceptado, será visible su información para los albergues. </p>

                    <div class="table-responsive">
                        <table id="example" class="table table-striped" style="width:100%;">
                            <thead>
                            <tr>
                                <th>DNI</th>
                                <th>Nombres y apellidos</th>
                                <th>Celular</th>
                                <th>¿Se llamó al postulante?</th>
                                <th>Distrito</th>
                                <th>Fecha de visita</th>
                                <th>Fecha de postulación</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                List<PostulacionHogarTemporal> postulacionesPendientes =
                                        (List<PostulacionHogarTemporal>) request.getAttribute("postulacionesPendientes");

                                if (postulacionesPendientes != null) {
                                    for (PostulacionHogarTemporal postulacion : postulacionesPendientes) {
                                        Usuario usuario = postulacion.getUsuario_final();
                                        String llamado = postulacion.getLlamo_al_postulante() ? "Sí" : "No";
                            %>
                            <tr>
                                <td><%= usuario.getDni() %> <p><%= postulacion.getId_postulacion_hogar_temporal() %></p> </td>
                                <td><%= usuario.getNombres_usuario_final() %></td>
                                <td><%= postulacion.getCelular_usuario() %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/CoordinadorZonalHogarTemporalServlet" method="post">
                                        <input type="hidden" name="action" value="actualizarLlamada" />
                                        <input type="hidden" name="idPostulacion" value="<%= postulacion.getId_postulacion_hogar_temporal() %>" />
                                        <select name="llamoAlPostulante" onchange="this.form.submit()">
                                            <option value="false" <%= !postulacion.getLlamo_al_postulante() ? "selected" : "" %>>No</option>
                                            <option value="true" <%= postulacion.getLlamo_al_postulante() ? "selected" : "" %>>Sí</option>
                                        </select>
                                    </form>
                                </td>
                                <td><%= usuario.getDireccion() %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/CoordinadorZonalHogarTemporalServlet" method="post">
                                        <input type="hidden" name="action" value="actualizarFecha" />
                                        <input type="hidden" name="idPostulacion" value="<%= postulacion.getId_postulacion_hogar_temporal() %>" />
                                        <input type="date" name="fechaVisita"
                                               value="<%= postulacion.getFecha_visita() != null ? postulacion.getFecha_visita() : "" %>"
                                               onchange="this.form.submit()" />
                                    </form>
                                </td>
                                <td><%= postulacion.getFecha_hora_registro() != null ? postulacion.getFecha_hora_registro().toLocalDate() : "" %></td>
                                <td>
                                    <ul class="icons">
                                        <!-- Aceptar Postulación -->
                                        <li>
                                            <form action="CoordinadorZonalHogarTemporalServlet" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="aceptarPostulacion" />
                                                <input type="hidden" name="idPostulacion" value="<%= postulacion.getId_postulacion_hogar_temporal() %>" />
                                                <button type="submit" class="icon fas fa-check-circle" title="Aceptar postulación" style="background: none; border: none; cursor: pointer;">
                                                    <span class="label">Aceptar</span>
                                                </button>
                                            </form>
                                        </li>
                                        <!-- Rechazar Postulación -->
                                        <li>
                                            <form action="CoordinadorZonalHogarTemporalServlet" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="rechazarPostulacion" />
                                                <input type="hidden" name="idPostulacion" value="<%= postulacion.getId_postulacion_hogar_temporal() %>" />
                                                <button type="submit" class="icon fas fa-times-circle" title="Rechazar postulación" style="background: none; border: none; cursor: pointer;">
                                                    <span class="label">Rechazar</span>
                                                </button>
                                            </form>
                                        </li>
                                        <!-- Ver más información -->
                                        <li>
                                            <a href="CoordinadorZonalHogarTemporalServlet?action=detalles&idPostulacion=<%= postulacion.getId_postulacion_hogar_temporal() %>"
                                               class="open-modal-info-trufa icon fas fa-eye" title="Ver más información">
                                                <span class="label">Ver detalles</span>
                                            </a>
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                            </tbody>
                        </table>
                        <script>
                            new DataTable('#example', {
                                language: {
                                    sSearch: "Buscar:",  // Cambia el texto del campo de búsqueda
                                    sLengthMenu: "Mostrar _MENU_ registros",
                                    sZeroRecords: "No se encontraron resultados",
                                    sEmptyTable: "No se encontraron postulantes como hogar temporal",
                                    sInfo: "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                                    sInfoEmpty: "Mostrando registros del 0 al 0 de un total de 0 registros",
                                    sInfoFiltered: "(filtrado de un total de _MAX_ registros)",
                                    sLoadingRecords: "Cargando...",
                                }
                            });
                        </script>

                    </div>
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
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/coordinadorZonal/assets/js/main.js"></script>

<!-- Modal Aceptar -->
<div class="modal modal-accept">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>El postulante ha sido aceptado con éxito.</p>
        <ul class="actions modal-buttons">
            <li><a href="postulantes.html" class="button primary big accept-action">Aceptar</a></li>
        </ul>
    </div>
</div>

<!-- Modal Rechazar -->
<div class="modal modal-reject">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <p>Se ha rechazado la solicitud del postulante.</p>
        <ul class="actions modal-buttons">
            <li><a href="#" class="button primary big reject-action">OK</a></li>
        </ul>
    </div>
</div>


<!-- Script dentro del mismo HTML -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Función para abrir un modal
        function openModal(modal) {
            modal.classList.add('show');
        }

        // Función para cerrar un modal
        function closeModal(modal) {
            modal.classList.remove('show');
        }

        // Configuración del Modal Aceptar
        const modalAccept = document.querySelector('.modal-accept');
        const openModalAcceptButtons = document.querySelectorAll('.open-modal-found'); // Botones para abrir el modal de aceptar
        const closeModalAcceptButton = modalAccept.querySelector('.close-btn');
        const acceptActionButton = modalAccept.querySelector('.accept-action');

        openModalAcceptButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                openModal(modalAccept);
            });
        });

        closeModalAcceptButton.addEventListener('click', function() {
            closeModal(modalAccept);
        });

        acceptActionButton.addEventListener('click', function(event) {
            event.preventDefault();
            closeModal(modalAccept); // O redirigir según tu necesidad
        });

        // Configuración del Modal Rechazar
        const modalReject = document.querySelector('.modal-reject');
        const openModalRejectButtons = document.querySelectorAll('.open-modal-update'); // Botones para abrir el modal de rechazar
        const closeModalRejectButton = modalReject.querySelector('.close-btn');
        const rejectActionButton = modalReject.querySelector('.reject-action');

        openModalRejectButtons.forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                openModal(modalReject);
            });
        });

        closeModalRejectButton.addEventListener('click', function() {
            closeModal(modalReject);
        });

        rejectActionButton.addEventListener('click', function(event) {
            event.preventDefault();
            closeModal(modalReject);
        });
    });

</script>



</body>
</html>


