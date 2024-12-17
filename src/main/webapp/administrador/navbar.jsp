<%
    String idUsuario = request.getParameter("idUsuario");
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
                    <img src="<%=request.getContextPath()%>/administrador/images/aa.png" alt="Foto de perfil" id="image-perfil">
                    <h2 id="usuario">Administrador</h2>
                </article>
            </div>
        </section>

        <nav id="menu">
            <header class="major"><h2>Menu</h2></header>
            <ul>
                <li><a href="<%=request.getContextPath()%>/dashboard">Dashboard</a></li>
                <li>
                    <span class="opener">Gestión de Usuarios</span>
                    <ul>
                        <li><a href="<%=request.getContextPath()%>/ListaUsuariosAdminServlet?action=listar">Lista de Usuarios</a></li>
                        <li><a href="<%=request.getContextPath()%>/ListaAlbergueAdminServlet?action=listar">Lista de Albergues</a></li>
                        <li><a href="<%=request.getContextPath()%>/ListasAdminServlet?action=listaCoord">Lista de Coordinadores zonales</a></li>
                        <li><a href="<%=request.getContextPath()%>/ListasAdminServlet?action=solicitudesUsuarios">Solicitudes de Usuario</a></li>
                        <li><a href="<%=request.getContextPath()%>/SolicitudAlbergueAdminServlet?action=listar">Solicitudes de Albergue</a></li>
                    </ul>
                </li>
                <li><a href="<%=request.getContextPath()%>/ListaLugaresAdminServlet?action=listar">Gestión de Eventos</a></li>
            </ul>
        </nav>

        <nav id="logout">
            <a href="${pageContext.request.contextPath}/LoginServlet?action=logout" id="cerrar-sesion">Cerrar Sesión</a>
        </nav>
    </div>
</div>
