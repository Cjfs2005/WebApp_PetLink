package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.GestionCoordinadorDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name= "GestionCoordinadorServlet", value="/GestionCoordinadorServlet")
public class GestionCoordinadorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        GestionCoordinadorDao dao = new GestionCoordinadorDao();

        HttpSession session = request.getSession();

        Usuario coordinador = (Usuario) session.getAttribute("usuario");

        if (coordinador == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int idCoordinador = coordinador.getId_usuario();

        Usuario datosCoordinador = dao.obtenerDatosCoordinador(idCoordinador);
        session.setAttribute("datosUsuario", datosCoordinador);

        int idZonaCoordinador = datosCoordinador.getZona().getId_zona();

        try {
            switch (action) {
                case "lista":
                    ArrayList<PublicacionEventoBenefico> eventos = dao.obtenerEventosPendientesPorZona(idZonaCoordinador);
                    System.out.println("Hay "+ eventos.size()+" eventos pendienes en su zona "+idZonaCoordinador);
                    request.setAttribute("listaEventos", eventos);
                    RequestDispatcher disp = request.getRequestDispatcher("coordinadorZonal/gestion_eventos.jsp");
                    disp.forward(request, response);
                    break;

                case "mostrar":
                    int idPublicacion;
                    try {
                        idPublicacion = Integer.parseInt(request.getParameter("id"));
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath()+"/coordinadorZonal/gestion_eventos.jsp");
                        return;
                    }
                    PublicacionEventoBenefico evento = dao.obtenerEventoPendientePorId(idPublicacion);
                    // Pasar el evento como atributo a la solicitud para mostrar en el JSP
                    request.setAttribute("evento", evento);
                    // Redirigir a la página de detalles
                    RequestDispatcher dispatcher = request.getRequestDispatcher("coordinadorZonal/ver_evento.jsp");
                    dispatcher.forward(request, response);
                    break;

                case "aceptar":
                    String idEvento = request.getParameter("id");
                    int EventID = Integer.parseInt(idEvento);
                    System.out.println("------Tenemos que aceptar al ID "+EventID);
                    if (dao.obtenerEventoPendientePorId(EventID) != null) {
                        dao.aceptarEvento(EventID);
                    }

                    response.sendRedirect(request.getContextPath() + "/GestionCoordinadorServlet?action=lista&success=eventoAceptado");
                    break;

                case "rechazar":
                    String idEvento1 = request.getParameter("id");
                    int EventID1 = Integer.parseInt(idEvento1);
                    dao.rechazarEvento(EventID1);
                    response.sendRedirect(request.getContextPath() + "/GestionCoordinadorServlet?action=lista&success=eventoRechazado");
                    break;

                default:
                    // Acción no encontrada, redirigir a la lista de eventos
                    response.sendRedirect(request.getContextPath() + "/GestionCoordinadorServlet?action=lista");
                    break;
            }
        }
        catch (Exception e){
            response.sendRedirect(request.getContextPath() + "/GestionCoordinadorServlet?action=lista&error=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
