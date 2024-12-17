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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name= "EventoUsuarioServlet", value="/EventoUsuarioServlet")
public class EventoUsuarioServlet extends HttpServlet {

    private static final List<String> ACCIONES_VALIDAS = Arrays.asList("lista", "mostrar", "inscribir", "buscar", "desinscribir");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si no tiene una acción, será entonces lista
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        if (!ACCIONES_VALIDAS.contains(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            return;
        }

        String filter = request.getParameter("filter") == null ? "solicitudes" : request.getParameter("filter");

        EventoUsuarioDao dao = new EventoUsuarioDao();

        HttpSession session = request.getSession();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        Usuario datosUsuario = usuario;

        int idUsuario = usuario.getId_usuario();

        session.setAttribute("datosUsuario", datosUsuario);

        try {
            switch (action) {
                case "lista":
                    ArrayList<PublicacionEventoBenefico> listaEventos;
                    if (!filter.matches("solicitudes|realizadas")) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Filtro no válido");
                        return;
                    }
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
                    break;
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
                    boolean es_activo = dao.verSiEsActivo(idPublicacion);
                    boolean usuarioInscrito = dao.verificarInscripcion(idPublicacion, idUsuario);

                    if (evento != null && es_activo) {
                        System.out.println("Evento encontrado: " + evento.getNombreEvento());
                        System.out.println("Razón del Evento: " + evento.getRazonEvento());
                        System.out.println("Lugar del Evento: " + (evento.getLugarEvento() != null ? evento.getLugarEvento().getDireccion_lugar_evento() : "Lugar no encontrado"));
                        System.out.println("Nombre del albergue: " + (evento.getUsuarioAlbergue() != null ? evento.getUsuarioAlbergue().getNombre_albergue() : "Albergue no encontrado"));
                        System.out.println("Direccion del evento: " + (evento.getLugarEvento() != null ? evento.getLugarEvento().getDistrito().getNombre_distrito() : "Distrito no encontrado"));
                    } else {
                        System.out.println("Evento no encontrado para el id: " + idPublicacion);
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=lista&error=eventoNoEncontrado");
                        return;
                    }

                    LocalDateTime fechaInicio = evento.getFechaHoraInicioEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime fechaFin = evento.getFechaHoraFinEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    System.out.println("-----FECHAS:");
                    System.out.println(fechaInicio);
                    System.out.println(fechaFin);
                    System.out.println("---------");

                    boolean hayTraslape = dao.verificarTraslape(idUsuario, fechaInicio, fechaFin);

                    System.out.println("------TRASLAPE? "+hayTraslape);

                    // Pasar el evento como atributo a la solicitud para mostrar en el JSP
                    request.setAttribute("evento", evento);
                    request.setAttribute("usuarioInscrito", usuarioInscrito);
                    request.setAttribute("hayTraslape", hayTraslape);

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

                    evento = dao.buscarPorId(idEvento);
                    if (evento == null) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=lista&error=eventoNoEncontrado");
                        return;
                    }

                    if (evento.getAforoEvento() <= evento.getCantAsistentes()) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEvento + "&error=noVacantes");
                        return;
                    }

                    if (dao.verificarInscripcion(idEvento, idUsuario)) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEvento + "&error=yaInscrito");
                        return;
                    }

                    // Convertir fechas de `Date` a `LocalDateTime`
                    LocalDateTime inicio = evento.getFechaHoraInicioEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime fin = evento.getFechaHoraFinEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    if (dao.verificarTraslape(idUsuario, inicio, fin)) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEvento + "&error=traslapeHorario");
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

                case "desinscribir":
                    // Lógica para desinscripción
                    int idEventoDesinscribir;
                    try {
                        idEventoDesinscribir = Integer.parseInt(request.getParameter("id"));
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=lista");
                        return;
                    }

                    PublicacionEventoBenefico eventoDesinscribir = dao.buscarPorId(idEventoDesinscribir);
                    if (eventoDesinscribir == null) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=lista&error=eventoNoEncontrado");
                        return;
                    }

                    // Verificar si el usuario está inscrito en el evento
                    if (!dao.verificarInscripcion(idEventoDesinscribir, idUsuario)) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEventoDesinscribir + "&error=noInscrito");
                        return;
                    }

                    // Llamar al DAO para desinscribir al usuario
                    boolean exitoDesinscripcion = dao.desinscribirUsuarioDeEvento(idUsuario, idEventoDesinscribir);
                    if (exitoDesinscripcion) {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEventoDesinscribir + "&success=desinscripcion");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet?action=mostrar&id=" + idEventoDesinscribir + "&error=desinscripcion");
                    }
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        }
        catch (Exception e) {
            response.sendRedirect("EventoUsuarioServlet?action=lista&error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        if (!ACCIONES_VALIDAS.contains(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            return;
        }

        EventoUsuarioDao daoEvento = new EventoUsuarioDao();

        try {
            switch (action) {
                case "buscar":
                    String query = request.getParameter("query");
                    if (query != null) {
                        query = query.replaceAll("[<>\"']", ""); // Sanitizar entrada
                    }
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

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        }
        catch (Exception e){
            response.sendRedirect("EventoUsuarioServlet?action=lista&error");
        }
    }
}
