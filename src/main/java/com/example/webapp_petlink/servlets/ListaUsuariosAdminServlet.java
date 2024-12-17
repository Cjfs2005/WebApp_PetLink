package com.example.webapp_petlink.servlets;


import com.example.webapp_petlink.beans.PuntoAcopio;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilAlbergue;
import com.example.webapp_petlink.daos.DaoPerfilUsuario;
import com.example.webapp_petlink.daos.ListasAdminDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="ListaUsuariosAdminServlet", value="/ListaUsuariosAdminServlet")
public class ListaUsuariosAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");

        ListasAdminDao dao = new ListasAdminDao();

        HttpSession session = request.getSession();

        Usuario administrador = (Usuario) session.getAttribute("usuario");

        if (administrador == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        session.setAttribute("datosUsuario", administrador);

        RequestDispatcher dispatcher;

        switch (action) {
            case "listar":
                ArrayList<Usuario> usuarios = dao.listarUsuariosConRegistroCompleto();
                if (usuarios == null){
                    System.out.println("Todo esta fallando");
                }
                request.setAttribute("usuarios", usuarios);
                dispatcher = request.getRequestDispatcher("administrador/listarUsuarios.jsp");
                dispatcher.forward(request, response);
                break;

            case "mostrar":
                detallesUsuario(request, response);
                break;

            case "eliminar":
                String idAlbergue = request.getParameter("id");
                int AlbergueID = Integer.parseInt(idAlbergue);
                dao.eliminarAlbergue(AlbergueID);
                response.sendRedirect(request.getContextPath()+"/ListaUsuariosAdminServlet?action=listar");
                break;
        }
    }

    private void detallesUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idUsuario = request.getParameter("id");
        System.out.println("ID de usuario mostrando es " + idUsuario);

        if (idUsuario != null && !idUsuario.isEmpty()) {
            try {

                // Convertir el id a un número si es necesario
                int idUsuarioAlbergue = Integer.parseInt(idUsuario);

                // Obtener los detalles del albergue y los puntos de acopio asociados
                DaoPerfilUsuario albergueDAO = new DaoPerfilUsuario();
                Usuario perfilAlbergue = albergueDAO.obtenerPerfilUsuario(idUsuarioAlbergue);
                System.out.println("El nombre del usuario es "+ perfilAlbergue.getNombres_usuario_final());

                //Gianfranco: Agregado para tener session como cierto perfil de albergue
                HttpSession session = request.getSession();
                session.setAttribute("usuarioPerfil", perfilAlbergue);

                //Se instancia el RequestDispatcher
                RequestDispatcher dispatcher;
                // Redirigir al JSP
                dispatcher = request.getRequestDispatcher("administrador/detalles_usuario.jsp");
                dispatcher.forward(request, response);

            } catch (NumberFormatException e) {
                // En caso de que el id no sea válido o no se pueda convertir
                response.sendRedirect(request.getContextPath() + "/ListaUsuariosAdminServlet?accion=listar");
            }
        } else {
            // Si no se pasa un ID válido en la solicitud
            response.sendRedirect(request.getContextPath() + "/ListaAlbergueAdminServlet?accion=listar");
        }
    }
}

