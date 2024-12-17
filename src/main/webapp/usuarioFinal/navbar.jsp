<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 15/11/2024
  Time: 16:51
  To change this template use File | Settings | File Templates.
--%>
<%
    String idUsuario = request.getParameter("idUsuario");
    String nombresUsuario = request.getParameter("nombresUsuario");
    String apellidosUsuario = request.getParameter("apellidosUsuario");
    String fotoPerfilBase64 = request.getParameter("fotoPerfilBase64");
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
                    <% if (fotoPerfilBase64 != null && !fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil" id="image-perfil">
                    <% } %>
                    <h2 id="usuario"><%= nombresUsuario %> <%= apellidosUsuario %></h2>
                </article>
            </div>
        </section>

        <nav id="menu">
            <header class="major"><h2>Menu</h2></header>
            <ul>
                <li><a href="<%=request.getContextPath()%>/PerfilUsuarioServlet?accion=ver">PERFIL</a></li>
                <li><span class="opener">ALBERGUES</span>
                    <ul>
                        <li><a href="<%=request.getContextPath()%>/ListaAlberguesServlet">LISTA DE ALBERGUES</a></li>
                        <li><a href="<%=request.getContextPath()%>/EventoUsuarioServlet">EVENTOS BENÉFICOS</a></li>
                        <li><a href="<%=request.getContextPath()%>/DonacionProductosUsuarioServlet">SOLICITUDES DE DONACIÓN</a></li>
                        <li><a href="<%=request.getContextPath()%>/HistorialDonacionesUsuarioServlet">HISTORIAL DE DONACIONES</a></li>
                        <li><a href="<%=request.getContextPath()%>/AdopcionesUsuarioFinalServlet?accion=ver">MASCOTAS EN ADOPCIÓN</a></li>
                        <li><a href="<%=request.getContextPath()%>/AdopcionesUsuarioFinalServlet?accion=historial">HISTORIAL DE ADOPCIONES</a></li>
                    </ul>
                </li>
                <li><a href="<%=request.getContextPath()%>/TemporalUsuarioServlet">HOGAR TEMPORAL</a></li>
                <li><a href="<%=request.getContextPath()%>/DenunciaServlet">DENUNCIAS POR MALTRATO ANIMAL</a></li>
            </ul>
        </nav>

        <nav id="logout">
            <a href="${pageContext.request.contextPath}/LoginServlet?action=logout" id="cerrar-sesion" id="cerrar-sesion">Cerrar Sesión</a>
        </nav>
    </div>
</div>
