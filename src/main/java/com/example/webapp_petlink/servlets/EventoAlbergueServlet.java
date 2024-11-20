package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.InscripcionEventoBenefico;
import com.example.webapp_petlink.beans.LugarEvento;
import com.example.webapp_petlink.beans.PublicacionEventoBenefico;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.EventoUsuarioDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name="EventoAlbergueServlet", value="/EventoAlbergueServlet")
public class EventoAlbergueServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        EventoUsuarioDao daoEvento = new EventoUsuarioDao();

        HttpSession session = request.getSession();

        Integer idAlbergue = (Integer) session.getAttribute("id_usuario");

        if (idAlbergue == null) {
            idAlbergue = 6;
        }

        Usuario datosAlbergue = daoEvento.obtenerDatosAlbergue(idAlbergue);
        session.setAttribute("datosUsuario", datosAlbergue);

        RequestDispatcher dispatcher;

        switch (action) {
            case "lista":

                ArrayList<PublicacionEventoBenefico> listaeventos = daoEvento.listarEventosAlbergue(idAlbergue);

                request.setAttribute("eventos", listaeventos);
                dispatcher = request.getRequestDispatcher("albergue/EventoBenefico.jsp");
                dispatcher.forward(request, response);
                break;

            case "mostrar":
                // Obtener el ID del evento desde los parámetros de la solicitud
                int idPublicacion;
                try {
                    idPublicacion = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath()+"/albergue/EventoBenefico.jsp");
                    return;
                }

                // Crear instancia del DAO y buscar el evento por ID

                ArrayList<InscripcionEventoBenefico> inscritos = daoEvento.listarInscritos(idPublicacion);
                PublicacionEventoBenefico evento = daoEvento.buscarPorId(idPublicacion);

                // Pasar el evento como atributo a la solicitud para mostrar en el JSP
                request.setAttribute("evento", evento);
                request.setAttribute("inscritos", inscritos);

                // Redirigir a la página de detalles
                dispatcher = request.getRequestDispatcher("albergue/detalles_evento.jsp");
                dispatcher.forward(request, response);
                break;

            case "crear":
                ArrayList<LugarEvento> lugares = daoEvento.listarLugaresEventos();
                request.setAttribute("lugares", lugares);
                dispatcher = request.getRequestDispatcher("albergue/organizar_evento.jsp");
                dispatcher.forward(request, response);
                break;

            case "eliminar":
                String idEvento = request.getParameter("id");
                int EventID = Integer.parseInt(idEvento);
                if (daoEvento.buscarPorId(EventID) != null) {
                    daoEvento.eliminarInscripcionEventoAlbergue(EventID);
                    daoEvento.eliminarEvento(EventID);
                }
                response.sendRedirect(request.getContextPath()+"/EventoAlbergueServlet");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        EventoUsuarioDao daoEvento = new EventoUsuarioDao();

        HttpSession session = request.getSession();

        Integer idAlbergue = (Integer) session.getAttribute("id_usuario");

        if (idAlbergue == null) {
            idAlbergue = 6;
        }

        switch (action) {
            case "buscar":
                String query = request.getParameter("query");
                ArrayList<PublicacionEventoBenefico> eventos = daoEvento.buscarEventoNombre(query, idAlbergue);

                request.setAttribute("eventos", eventos);
                request.setAttribute("busqueda", query);

                RequestDispatcher dispatcher = request.getRequestDispatcher("albergue/EventoBenefico.jsp");
                dispatcher.forward(request, response);
                break;

            case "guardar":
                break;

        }
    }
}
