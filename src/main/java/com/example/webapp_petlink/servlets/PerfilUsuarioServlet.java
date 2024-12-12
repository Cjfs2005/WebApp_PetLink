package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilUsuario;
import com.example.webapp_petlink.daos.LoginDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "PerfilUsuarioServlet", value = "/PerfilUsuarioServlet")
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DaoPerfilUsuario perfilDao = new DaoPerfilUsuario();
        RequestDispatcher dispatcher;

        // Si no se proporciona ninguna acción, usa "ver" como acción predeterminada
        if (accion == null || accion.isEmpty()) {
            accion = "ver";
        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        int ID_usuario = usuario.getId_usuario();

        try{
            switch (accion) {
                case "ver":
                    Usuario usuarioPerfil = usuario;
                    request.setAttribute("usuarioPerfil", usuarioPerfil);
                    dispatcher = request.getRequestDispatcher("usuarioFinal/perfil_usuario_final.jsp");
                    dispatcher.forward(request, response);
                    break;
                case "editar":
                    usuarioPerfil = usuario;
                    request.setAttribute("usuarioPerfil", usuarioPerfil);
                    dispatcher = request.getRequestDispatcher("usuarioFinal/editar_perfil.jsp");
                    dispatcher.forward(request, response);
                    break;
                default:
                    response.sendRedirect("error.jsp");
                    break;
            }
        } catch (Exception e) {
            response.sendRedirect("PerfilUsuarioServlet");
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        int idUsuario = usuario.getId_usuario();
        try{
            if ("actualizar".equals(accion)) {
                String nombres = request.getParameter("nombres");
                String apellidos = request.getParameter("apellidos");
                String direccion = request.getParameter("direccion");
                String correo = request.getParameter("correo");

                DaoPerfilUsuario perfilDao = new DaoPerfilUsuario();
                perfilDao.actualizarPerfilUsuario(idUsuario, nombres, apellidos, direccion, correo);

                String correoactualizar = usuario.getCorreo_electronico();
                String contrasenia = usuario.getContrasenia();
                LoginDao loginDao = new LoginDao();
                usuario = loginDao.obtenerUsuario(correoactualizar, contrasenia);
                request.getSession().setAttribute("usuario", usuario);

                response.sendRedirect("PerfilUsuarioServlet?accion=ver");
            }
        } catch (Exception e) {
            response.sendRedirect("PerfilUsuarioServlet?accion=editar");
        }

    }
}
