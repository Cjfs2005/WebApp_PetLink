package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilUsuario;
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

        switch (accion) {
            case "ver":
                int ID_usuario = 1; // Ejemplo. Podrías obtener este ID dinámicamente.
                Usuario usuarioPerfil = perfilDao.obtenerPerfilUsuario(ID_usuario);
                request.setAttribute("usuarioPerfil", usuarioPerfil);
                dispatcher = request.getRequestDispatcher("usuarioFinal/perfil_usuario_final.jsp");
                dispatcher.forward(request, response);
                break;
            case "editar":
                ID_usuario = 1; // Ejemplo.
                usuarioPerfil = perfilDao.obtenerPerfilUsuario(ID_usuario);
                request.setAttribute("usuarioPerfil", usuarioPerfil);
                dispatcher = request.getRequestDispatcher("usuarioFinal/editar_perfil.jsp");
                dispatcher.forward(request, response);
                break;
            default:
                response.sendRedirect("error.jsp");
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("actualizar".equals(accion)) {
            int ID_usuario = 1; // ID del usuario a actualizar (ejemplo).
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String direccion = request.getParameter("direccion");
            String correo = request.getParameter("correo");

            DaoPerfilUsuario perfilDao = new DaoPerfilUsuario();
            perfilDao.actualizarPerfilUsuario(ID_usuario, nombres, apellidos, direccion, correo);

            response.sendRedirect("PerfilUsuarioServlet?accion=ver");
        }
    }
}
