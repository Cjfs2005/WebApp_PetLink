package com.example.webapp_petlink.servlets;


import com.example.webapp_petlink.beans.PuntoAcopio;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilAlbergue;
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

@WebServlet(name="ListaAlbergueAdminServlet", value="/ListaAlbergueAdminServlet")
public class ListaAlbergueAdminServlet extends HttpServlet {

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
                ArrayList<Usuario> albergues = dao.listarAlbergueConRegistroCompleto();
                if (albergues == null){
                    System.out.println("Todo esta fallando");
                }
                request.setAttribute("albergues", albergues);
                dispatcher = request.getRequestDispatcher("administrador/lista_albergue.jsp");
                dispatcher.forward(request, response);
                break;

            case "mostrar":
                detallesAlbergue(request, response);
                break;

            case "eliminar":
                String idAlbergue = request.getParameter("id");
                int AlbergueID = Integer.parseInt(idAlbergue);
                dao.eliminarAlbergue(AlbergueID);
                response.sendRedirect(request.getContextPath()+"/ListaAlbergueAdminServlet?action=listar");
                break;
        }
    }

    private void detallesAlbergue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idUsuario = request.getParameter("id");
        System.out.println(idUsuario);

        if (idUsuario != null && !idUsuario.isEmpty()) {
            try {

                // Convertir el id a un número si es necesario
                int idUsuarioAlbergue = Integer.parseInt(idUsuario);

                // Obtener los detalles del albergue y los puntos de acopio asociados
                DaoPerfilAlbergue albergueDAO = new DaoPerfilAlbergue();
                Usuario perfilAlbergue = albergueDAO.obtenerPerfilAlbergue(idUsuarioAlbergue);

                List<PuntoAcopio> puntosAcopio = albergueDAO.obtenerPuntosAcopio(idUsuarioAlbergue);
                if (puntosAcopio == null || puntosAcopio.isEmpty()) {
                    System.out.println("La lista de puntos de acopio está vacía.");
                }

                //Gianfranco: Agregado para tener session como cierto perfil de albergue
                HttpSession session = request.getSession();
                session.setAttribute("perfilAlbergue", perfilAlbergue);
                session.setAttribute("puntosAcopio", puntosAcopio);

                //Se instancia el RequestDispatcher
                RequestDispatcher dispatcher;
                // Redirigir al JSP
                dispatcher = request.getRequestDispatcher("administrador/detalles_albergue.jsp");
                dispatcher.forward(request, response);

            } catch (NumberFormatException e) {
                // En caso de que el id no sea válido o no se pueda convertir
                response.sendRedirect(request.getContextPath() + "/ListaAlbergueAdminServlet?accion=listar");
            }
        } else {
            // Si no se pasa un ID válido en la solicitud
            response.sendRedirect(request.getContextPath() + "/ListaAlbergueAdminServlet?accion=listar");
        }
    }
}
