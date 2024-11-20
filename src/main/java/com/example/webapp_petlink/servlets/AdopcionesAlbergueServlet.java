package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.AdopcionesAlbergueDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AdopcionesAlbergueServlet", value = "/AdopcionesAlbergueServlet")

public class AdopcionesAlbergueServlet extends HttpServlet{

    AdopcionesAlbergueDao adopcionesAlbergueDao = new AdopcionesAlbergueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        RequestDispatcher view;

        // Validar el parámetro id_usuario
        String idUsuarioParam = request.getParameter("id_usuario");
        if (idUsuarioParam == null || idUsuarioParam.isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idUsuarioParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp");
            return;
        }

        Usuario usuario = adopcionesAlbergueDao.obtenerUsuarioPorId(idUsuario);

        switch (action) {
            case "listar":
                if (usuario != null) {
                    request.setAttribute("usuario", usuario);

                    ArrayList<PublicacionMascotaAdopcion> publicaciones = adopcionesAlbergueDao.obtenerListaPublicacionesAdopcion(idUsuario);

                    request.setAttribute("publicacionesAdopcion", publicaciones);

                    view = request.getRequestDispatcher("albergue/adopciones_albergue_lista.jsp");
                    view.forward(request, response);
                } else {
                    response.sendRedirect("error.jsp");
                }
                break;
            case "formulario":

                if (usuario != null) {
                    request.setAttribute("usuario", usuario);

                    view = request.getRequestDispatcher("albergue/adopciones_albergue_crear.jsp");
                    view.forward(request, response);
                } else {
                    response.sendRedirect("error.jsp");
                }

                break;
            default:
                response.sendRedirect("error.jsp");
        }
    }

    @Override //Post implica que el JSP esta enviando informacion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
