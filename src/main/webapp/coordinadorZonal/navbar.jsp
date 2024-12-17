<%
    String idUsuario = request.getParameter("idUsuario");
    String nombresCoordinador = request.getParameter("nombresCoordinador");
    String apellidosCoordinador = request.getParameter("apellidosCoordinador");
    String zonaCoordinador = request.getParameter("zonaCoordinador");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="sidebar">
    <div class="inner">
        <section class="alt" id="sidebar-header">
            <img src="<%=request.getContextPath()%>/usuarioFinal/images/favicon.png" alt="Logo" id="sidebar-icon">
            <p id="sidebar-title">PetLink</p>
        </section>

        <section class="perfil">
            <div class="mini-posts">
                <article>
                    <img src="<%=request.getContextPath()%>/albergue/images/sin_perfil.png" alt="Foto de perfil" id="image-perfil">
                    <h2 id="usuario"><%= nombresCoordinador %> <%= apellidosCoordinador %><br>Zona <%= zonaCoordinador%></h2>
                </article>
            </div>
        </section>

        <nav id="menu">
            <header class="major"><h2>Menu</h2></header>
            <ul>
                <li><a href="<%=request.getContextPath()%>/GestionCoordinadorServlet">Gestión de eventos</a></li>
                <li><a href="<%=request.getContextPath()%>/CoordinadorZonalHogarTemporalServlet">Postulantes a hogar temporal</a></li>
                <li>
                    <span class="opener">Baneo de usuarios</span>
                    <ul>
                        <li><a href="<%=request.getContextPath()%>/BaneosCoordi1Servlet">Banear usuarios</a></li>
                        <li><a href="<%=request.getContextPath()%>/BaneosCoordi2Servlet">Baneos realizados</a></li>
                    </ul>
                </li>
            </ul>
        </nav>

        <nav id="logout">
            <a href="${pageContext.request.contextPath}/LoginServlet?action=logout" id="cerrar-sesion">Cerrar Sesión</a>
        </nav>
    </div>
</div>
