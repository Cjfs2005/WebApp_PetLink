package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.FotoPostulacionHogarTemporal;
import com.example.webapp_petlink.beans.PostulacionHogarTemporal;
import com.example.webapp_petlink.daos.CoordinadorZonalHogarTemporalDao;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "CoordinadorZonalHogarTemporalServlet", value = "/CoordinadorZonalHogarTemporalServlet")
public class CoordinadorZonalHogarTemporalServlet extends HttpServlet {

    private final CoordinadorZonalHogarTemporalDao dao = new CoordinadorZonalHogarTemporalDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Acción que se espera (por ejemplo, "listar" o "detalles")
        String action = request.getParameter("action");

        if (action == null) {
            action = "listar";
        }

        if ("listar".equals(action)) {
            // Mostrar las postulaciones pendientes
            List<PostulacionHogarTemporal> postulacionesPendientes = dao.obtenerPostulacionesPendientes();
            request.setAttribute("postulacionesPendientes", postulacionesPendientes);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/coordinadorZonal/postulantes_hogar_temporal.jsp"); // Cambiar por el nombre real del JSP
            dispatcher.forward(request, response);

        } else if ("detalles".equals(action)) {
            // Mostrar los detalles completos de una postulación específica
            int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
            List<FotoPostulacionHogarTemporal> fotosConDetalles = dao.obtenerDetallesPostulacionConFotos(idPostulacionHogarTemporal);
            request.setAttribute("fotosConDetalles", fotosConDetalles);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/coordinadorZonal/detalles_postulantes_hogar_temporal.jsp"); // Cambiar por el nombre real del JSP
            dispatcher.forward(request, response);

        } else {
            response.sendRedirect("/coordinadorZonal/postulantes_hogar_temporal.jsp"); // Redirigir a una página de error en caso de acción inválida
        }
    }

    /*@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Acción que se espera (por ejemplo, "actualizarFecha" o "actualizarLlamada")
        String action = request.getParameter("action");

        if ("actualizarFecha".equals(action)) {
            // Actualizar la fecha de visita
            int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
            LocalDate fechaVisita = LocalDate.parse(request.getParameter("fechaVisita"));
            boolean actualizado = dao.actualizarFechaVisita(idPostulacionHogarTemporal, fechaVisita);

            if (actualizado) {
                response.sendRedirect("CoordinadorZonalHogarTemporalServlet?action=listar"); // Volver a listar las postulaciones
            } else {
                response.sendRedirect("error.jsp"); // Redirigir a una página de error en caso de fallo
            }

        } else if ("actualizarLlamada".equals(action)) {
            // Actualizar el estado de la llamada
            int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
            boolean llamoAlPostulante = Boolean.parseBoolean(request.getParameter("llamoAlPostulante"));
            boolean actualizado = dao.actualizarEstadoLlamada(idPostulacionHogarTemporal, llamoAlPostulante);

            if (actualizado) {
                response.sendRedirect("CoordinadorZonalHogarTemporalServlet?action=listar"); // Volver a listar las postulaciones
            } else {
                response.sendRedirect("error.jsp"); // Redirigir a una página de error en caso de fallo
            }

        } else {
            response.sendRedirect("error.jsp"); // Redirigir a una página de error en caso de acción inválida
        }
    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("actualizarFecha".equals(action)) {
            try {
                int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
                LocalDate fechaVisita = LocalDate.parse(request.getParameter("fechaVisita"));
                boolean actualizado = dao.actualizarFechaVisita(idPostulacionHogarTemporal, fechaVisita);

                //Gianfranco: Debug
                System.out.println("Action: " + action);
                System.out.println("ID Postulacion: " + request.getParameter("idPostulacion"));
                System.out.println("Fecha Visita: " + request.getParameter("fechaVisita"));
                System.out.println("Llamo al Postulante: " + request.getParameter("llamoAlPostulante"));

                if (actualizado) {
                    response.sendRedirect("CoordinadorZonalHogarTemporalServlet?action=listar");
                } else {
                    response.sendRedirect("error.jsp");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("/GestionCoordinadorServlet");
            }

        } else if ("actualizarLlamada".equals(action)) {
            try {
                int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
                boolean llamoAlPostulante = Boolean.parseBoolean(request.getParameter("llamoAlPostulante"));
                boolean actualizado = dao.actualizarEstadoLlamada(idPostulacionHogarTemporal, llamoAlPostulante);

                //Gianfranco: Debug
                System.out.println("Action: " + action);
                System.out.println("ID Postulacion: " + request.getParameter("idPostulacion"));
                System.out.println("Fecha Visita: " + request.getParameter("fechaVisita"));
                System.out.println("Llamo al Postulante: " + request.getParameter("llamoAlPostulante"));

                if (actualizado) {
                    response.sendRedirect("CoordinadorZonalHogarTemporalServlet?action=listar");
                } else {
                    response.sendRedirect("error.jsp");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }

        } else if ("aceptarPostulacion".equals(action)) {
            // Aceptar la postulación (cambiar estado a 2)
            int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
            boolean actualizado = dao.actualizarEstadoPostulacion(idPostulacionHogarTemporal, 2);

            if (actualizado) {
                response.sendRedirect("CoordinadorZonalHogarTemporalServlet?action=listar");
            } else {
                response.sendRedirect("error.jsp");
            }

        } else if ("rechazarPostulacion".equals(action)) {
            // Rechazar la postulación (cambiar estado a 3)
            int idPostulacionHogarTemporal = Integer.parseInt(request.getParameter("idPostulacion"));
            boolean actualizado = dao.actualizarEstadoPostulacion(idPostulacionHogarTemporal, 3);

            if (actualizado) {
                response.sendRedirect("CoordinadorZonalHogarTemporalServlet?action=listar");
            } else {
                response.sendRedirect("error.jsp");
            }

        } else {
            response.sendRedirect("error.jsp");
        }
    }


}
