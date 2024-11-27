package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.PublicacionEventoBenefico;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Zona;
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

@WebServlet(name="ListasAdminServlet", value="/ListasAdminServlet")
public class ListasAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listaCoord" : request.getParameter("action");

        ListasAdminDao dao = new ListasAdminDao();

        HttpSession session = request.getSession();

        Integer idAdmin = (Integer) session.getAttribute("id_usuario");

        if (idAdmin == null) {
            idAdmin = 10;
        }

        RequestDispatcher dispatcher;

        switch (action) {
            case "listaCoord":
                ArrayList<Usuario> coordinadores = dao.listasCoodinadores();

                request.setAttribute("coordinadores", coordinadores);
                dispatcher = request.getRequestDispatcher("administrador/lista_coordinador.jsp");
                dispatcher.forward(request, response);
                break;

            case "crearCoord":
                ArrayList<Zona> zonas = dao.obtenerZonas();
                request.setAttribute("zonas", zonas);
                dispatcher = request.getRequestDispatcher("administrador/lista_zonas.jsp");
                dispatcher.forward(request, response);
                break;
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
