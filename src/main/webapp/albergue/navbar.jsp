<%--
  Created by IntelliJ IDEA.
  User: CHRISTIAN
  Date: 15/11/2024
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%-- navbar.jsp --%>
<%
    String idUsuario = request.getParameter("idUsuario");
    String nombreAlbergue = request.getParameter("nombreAlbergue");
    String fotoPerfilBase64 = request.getParameter("fotoPerfilBase64");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="sidebar">
    <div class="inner">
        <section class="alt" id="sidebar-header">
            <img src="<%=request.getContextPath()%>/albergue/images/favicon.png" alt="Logo" id="sidebar-icon">
            <p id="sidebar-title">PetLink</p>
        </section>

        <section class="perfil">
            <div class="mini-posts">
                <article>
                    <% if (fotoPerfilBase64 != null && !fotoPerfilBase64.isEmpty()) { %>
                    <img src="data:image/png;base64,<%= fotoPerfilBase64 %>" alt="Foto de perfil" id="image-perfil">
                    <% } else { %>
                    <img src="<%=request.getContextPath()%>/albergue/images/default_profile.png" alt="Foto de perfil" id="image-perfil">
                    <% } %>
                    <h2 id="usuario"><%= nombreAlbergue != null ? nombreAlbergue : "Nombre no disponible" %></h2>
                </article>
            </div>
        </section>

        <nav id="menu">
            <header class="major"><h2>Menú</h2></header>
            <ul>
                <li><a href="<%=request.getContextPath()%>/PerfilAlbergueServlet">Perfil</a></li>
                <li><span class="opener">Publicaciones</span>
                    <ul>
                        <li><a href="<%=request.getContextPath()%>/EventoAlbergueServlet">Eventos benéficos</a></li>
                        <li><a href="<%=request.getContextPath()%>/AdopcionesAlbergueServlet">Adopciones</a></li>
                        <li><a href="<%=request.getContextPath()%>/ListaSolicitudesDonacionProductos">Donaciones</a></li>
                    </ul>
                </li>
                <li><a href="<%=request.getContextPath()%>/TemporalAlbergueServlet">Hogar Temporal</a></li>
                <li><a href="<%=request.getContextPath()%>/AlbergueDenunciasServlet">Denuncias por maltrato animal</a></li>
            </ul>
        </nav>

        <nav id="logout">
            <a href="${pageContext.request.contextPath}/LoginServlet?action=logout" id="cerrar-sesion">Cerrar Sesión</a>
        </nav>
    </div>
</div>

