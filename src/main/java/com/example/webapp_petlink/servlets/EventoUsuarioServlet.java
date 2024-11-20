package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.PublicacionEventoBenefico;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name= "EventoUsuarioServlet", value="/EventoUsuarioServlet")
public class EventoUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si no tiene una acción, será entonces lista
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        String filter = request.getParameter("filter") == null ? "solicitudes" : request.getParameter("filter");

        EventoUsuarioDao dao = new EventoUsuarioDao();


        HttpSession session = request.getSession();

        Integer idUsuario = (Integer) session.getAttribute("id_usuario");

        if (idUsuario == null) {
            idUsuario = 5;
        }

        Usuario datosUsuario = dao.obtenerDatoUsuario(idUsuario);
        session.setAttribute("datosUsuario", datosUsuario);

        switch (action) {
            case "lista":
                ArrayList<PublicacionEventoBenefico> listaEventos;
                if (filter.equals("realizadas")) {
                    // Filtrar eventos en los que el usuario está inscrito
                    listaEventos = dao.listarEventosInscritos(idUsuario);
                } else {
                    // Mostrar todos los eventos
                    listaEventos = dao.listarEventos();
                }
                request.setAttribute("listaEventos", listaEventos);
                request.setAttribute("filter", filter);
                RequestDispatcher disp = request.getRequestDispatcher("usuarioFinal/eventos.jsp");
                disp.forward(request, response);

            case "mostrar":
                // Obtener el ID del evento desde los parámetros de la solicitud
                int idPublicacion;
                try {
                    idPublicacion = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath()+"/usuarioFinal/eventos.jsp");
                    return;
                }

                // Crear instancia del DAO y buscar el evento por ID

                PublicacionEventoBenefico evento = dao.buscarPorId(idPublicacion);
                boolean usuarioInscrito = dao.verificarInscripcion(idPublicacion, idUsuario);

                if (evento != null) {
                    System.out.println("Evento encontrado: " + evento.getNombreEvento());
                    System.out.println("Razón del Evento: " + evento.getRazonEvento());
                    System.out.println("Lugar del Evento: " + (evento.getLugarEvento() != null ? evento.getLugarEvento().getDireccion_lugar_evento() : "Lugar no encontrado"));
                    System.out.println("Nombre del albergue: " + (evento.getUsuarioAlbergue() != null ? evento.getUsuarioAlbergue().getNombre_albergue() : "Albergue no encontrado"));
                    System.out.println("Direccion del evento: " + (evento.getLugarEvento() != null ? evento.getLugarEvento().getDistrito().getNombre_distrito() : "Distrito no encontrado"));
                } else {
                    System.out.println("Evento no encontrado para el id: " + idPublicacion);
                }

                // Pasar el evento como atributo a la solicitud para mostrar en el JSP
                request.setAttribute("evento", evento);
                request.setAttribute("usuarioInscrito", usuarioInscrito);

                // Redirigir a la página de detalles
                RequestDispatcher dispatcher = request.getRequestDispatcher("usuarioFinal/formulario_eventos.jsp");
                dispatcher.forward(request, response);
                break;

            case "inscribir":
                int idEvento;
                try {
                    idEvento = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=lista");
                    return;
                }

                // Llama al método del DAO para insertar la inscripción
                boolean exito = dao.inscribibirseEvento(idUsuario, idEvento);
                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEvento + "&success=1");
                } else {
                    response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEvento + "&error=1");
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        EventoUsuarioDao daoEvento = new EventoUsuarioDao();

        switch (action) {
            case "crea":
                String hola = request.getParameter("hola");
                break;

            case "buscar":
                String query = request.getParameter("query");
                System.out.println(query);
                ArrayList<PublicacionEventoBenefico> eventos = daoEvento.buscarEventoNombre(query);

                System.out.println("Número de eventos encontrados: " + eventos.size());
                for (PublicacionEventoBenefico evento : eventos) {
                    System.out.println("Evento encontrado: " + evento.getNombreEvento());
                }
                request.setAttribute("listaEventos", eventos);
                request.setAttribute("busqueda", query);

                RequestDispatcher dispatcher = request.getRequestDispatcher("usuarioFinal/eventos.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }
}
