package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.EventoUsuarioDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="EventoAlbergueServlet", value="/EventoAlbergueServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1 MB
        maxFileSize = 1024 * 1024 * 5,       // 5 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class EventoAlbergueServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        String filter = request.getParameter("filter") == null ? "activos" : request.getParameter("filter");

        EventoUsuarioDao daoEvento = new EventoUsuarioDao();

        HttpSession session = request.getSession();

        Usuario albergue = (Usuario) session.getAttribute("usuario");

        if (albergue == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int idAlbergue = albergue.getId_usuario();

        Usuario datosAlbergue = daoEvento.obtenerDatosAlbergue(idAlbergue);
        session.setAttribute("datosUsuario", datosAlbergue);

        RequestDispatcher dispatcher;

        try {
            switch (action) {
                case "lista":

                    List<String> filtrosPermitidos = Arrays.asList("activos", "realizados", "pendientes", "rechazados");
                    if (!filtrosPermitidos.contains(filter)) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Filtro no válido");
                        return;
                    }

                    ArrayList<PublicacionEventoBenefico> listaeventos;

                    switch (filter) {
                        case "realizados":
                            listaeventos = daoEvento.listarEventosRealizados(idAlbergue);
                            break;
                        case "pendientes":
                            listaeventos = daoEvento.listarEventosPendientes(idAlbergue);
                            break;
                        case "rechazados":
                            listaeventos = daoEvento.listarEventosRechazados(idAlbergue);
                            break;
                        default:
                            listaeventos = daoEvento.listarEventosAlbergue(idAlbergue);
                    }

                    request.setAttribute("eventos", listaeventos);
                    request.setAttribute("filter", filter);
                    dispatcher = request.getRequestDispatcher("albergue/EventoBenefico.jsp");
                    dispatcher.forward(request, response);
                    break;

                case "mostrar":
                    String idEventoStr = request.getParameter("id");
                    if (idEventoStr == null || idEventoStr.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=idFaltante");
                        return;
                    }

                    // Obtener el ID del evento desde los parámetros de la solicitud
                    int idPublicacion;
                    try {
                        idPublicacion = Integer.parseInt(idEventoStr);
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=formatoInvalido");
                        return;
                    }

                    // Crear instancia del DAO y buscar el evento por ID

                    PublicacionEventoBenefico evento = daoEvento.buscarPorIdDeAlbergue(idPublicacion, idAlbergue);
                    if (evento == null) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=eventoNoEncontrado");
                        return;
                    }
                    ArrayList<InscripcionEventoBenefico> inscritos = daoEvento.listarInscritos(idPublicacion);

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

                case "editar":

                    String idEventoStrEditar = request.getParameter("id");
                    if (idEventoStrEditar == null || idEventoStrEditar.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=idFaltante");
                        return;
                    }

                    int iDEvento;
                    try {
                        iDEvento = Integer.parseInt(idEventoStrEditar);
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=formatoInvalido");
                        return;
                    }
                    PublicacionEventoBenefico eventoEditar = daoEvento.buscarPorId(iDEvento);
                    if (eventoEditar == null) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=eventoNoEncontrado");
                        return;
                    }

                    ArrayList<LugarEvento> lugaresEditar = daoEvento.listarLugaresEventos();
                    request.setAttribute("lugares", lugaresEditar);
                    request.setAttribute("evento", eventoEditar);

                    dispatcher = request.getRequestDispatcher("albergue/evento_modificar.jsp");
                    dispatcher.forward(request, response);
                    break;

                case "eliminar":
                    String idEventoEliminar = request.getParameter("id");
                    if (idEventoEliminar == null || idEventoEliminar.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=idFaltante");
                        return;
                    }
                    int idEventoInt;
                    try {
                        idEventoInt = Integer.parseInt(idEventoEliminar);
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=formatoInvalido");
                        return;
                    }

                    if (daoEvento.buscarPorId(idEventoInt) != null) {
                        daoEvento.eliminarInscripcionEventoAlbergue(idEventoInt);
                        daoEvento.eliminarEvento(idEventoInt);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&error=eventoNoEncontrado");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&success=eventoEliminado");
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&errorEncontrado");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        EventoUsuarioDao daoEvento = new EventoUsuarioDao();
        HttpSession session = request.getSession();

        // Verificamos nuevamente si el usuario está en sesión
        Usuario albergue = (Usuario) session.getAttribute("usuario");
        if (albergue == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Ahora obtenemos el id_usuario para usarlo en el flujo
        int idAlbergue = albergue.getId_usuario();

        try {
            switch (action) {
                case "buscar":
                    try {
                        String query = request.getParameter("query");
                        ArrayList<PublicacionEventoBenefico> eventos = daoEvento.buscarEventoNombre(query, idAlbergue);

                        request.setAttribute("eventos", eventos);
                        request.setAttribute("busqueda", query);

                        RequestDispatcher dispatcher = request.getRequestDispatcher("albergue/EventoBenefico.jsp");
                        dispatcher.forward(request, response);
                        break;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&errorEnBusqueda");
                    }

                case "guardar":
                    try {
                        // 1. Extraer datos del formulario
                        String nombreEvento = request.getParameter("nombre_evento");
                        String diaEvento = request.getParameter("diaEvento");
                        String horaInicio = request.getParameter("horaInicioEvento");
                        String horaFin = request.getParameter("horaEvento");
                        String entrada = request.getParameter("entrada");
                        String razon = request.getParameter("razonEvento");
                        String descripcion = request.getParameter("descripcionEvento");
                        String artistas = request.getParameter("artistasInvitados");
                        String idLugarEventoStr = request.getParameter("distrito");
                        String aforoStr = request.getParameter("aforo");

                        if (!validarCamposEvento(request, nombreEvento, diaEvento, horaInicio, horaFin, entrada, razon, descripcion, idLugarEventoStr, aforoStr, artistas)) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                            return;
                        }

                        int idLugarEvento;
                        try {
                            idLugarEvento = Integer.parseInt(idLugarEventoStr);
                        } catch (NumberFormatException e) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                            return;
                        }

                        int aforoEvento;
                        try {
                            aforoEvento = Integer.parseInt(aforoStr);
                        } catch (NumberFormatException e) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                            return;
                        }

                        // Obtener el archivo de la imagen
                        Part filePart = request.getPart("fotosEvento"); // Campo "name" en el formulario JSP
                        String fileName = null;
                        byte[] fotoBytes = null;

                        if (filePart != null && filePart.getSize() > 0) {
                            // Validar tamaño máximo del archivo
                            if (filePart.getSize() > (1024 * 1024 * 5)) { // Más de 5 MB
                                request.setAttribute("error", "El archivo no debe superar los 5 MB.");
                                request.getRequestDispatcher("albergue/organizar_evento.jsp").forward(request, response);
                                return;
                            }

                            // Validar formato del archivo
                            fileName = filePart.getSubmittedFileName();
                            if (!fileName.endsWith(".png")) {
                                response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                                return;
                            }

                            // Leer los bytes de la imagen
                            fotoBytes = filePart.getInputStream().readAllBytes();
                        } else {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                            return;
                        }

                        // 3. Convertir fecha y hora a java.util.Date
                        Date fechaHoraInicioEvento = null;
                        Date fechaHoraFinEvento = null;
                        Date fechaHoraRegistroEvento = new Date(); // Fecha y hora actuales
                        try {
                            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            fechaHoraInicioEvento = dateTimeFormat.parse(diaEvento + " " + horaInicio);
                            fechaHoraFinEvento = dateTimeFormat.parse(diaEvento + " " + horaFin);
                        } catch (Exception e) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                            return;
                        }

                        if (fechaHoraInicioEvento.after(fechaHoraFinEvento)) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=1");
                            return;
                        }

                        // 3. Construir objeto PublicacionEventoBenefico
                        PublicacionEventoBenefico evento = new PublicacionEventoBenefico();
                        evento.setNombreEvento(nombreEvento);
                        evento.setEntradaEvento(entrada);
                        evento.setRazonEvento(razon);
                        evento.setAforoEvento(aforoEvento);

                        evento.setFechaHoraInicioEvento(fechaHoraInicioEvento);
                        evento.setFechaHoraFinEvento(fechaHoraFinEvento);
                        evento.setFechaHoraRegistro(fechaHoraRegistroEvento);

                        evento.setNombreFoto(fileName); // Nombre del archivo
                        evento.setFoto(fotoBytes);     // Bytes de la imagen

                        evento.setDescripcionEvento(descripcion);
                        evento.setArtistasProvedoresInvitados(artistas);

                        LugarEvento lugar = new LugarEvento();
                        lugar.setId_lugar_evento(idLugarEvento);
                        evento.setLugarEvento(lugar);

                        Usuario usuario = new Usuario();
                        usuario.setId_usuario(idAlbergue);
                        evento.setUsuarioAlbergue(usuario);

                        // Colocamos algunos ID que luego serán modificados en la opeación
                        int IdEstado = 1;
                        boolean eventoActivo = true;

                        Estado estado = new Estado();
                        estado.setId_estado(IdEstado);
                        evento.setEstado(estado);

                        evento.setEsEventoActivo(eventoActivo);

                        // 4. Guardar el evento en la base de datos
                        boolean guardado = daoEvento.guardarEvento(evento);

                        if (guardado) {
                            // 5. Redirigir a la lista de eventos con un mensaje de éxito
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&success=true");
                        } else {
                            // Manejar el error si no se pudo guardar
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=NoCreado");
                        }
                        break;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=crear&error=NoCreado");
                    }

                case "actualizar": {
                    try {
                        // Obtener parámetros del formulario
                        int idEvento = Integer.parseInt(request.getParameter("id_evento"));
                        String nombreEvento1 = request.getParameter("nombre_evento");
                        String diaEvento1 = request.getParameter("diaEvento");
                        String horaInicio1 = request.getParameter("horaInicioEvento");
                        String horaFin1 = request.getParameter("horaEvento");
                        String entrada1 = request.getParameter("entrada");
                        String razon1 = request.getParameter("razonEvento");
                        String descripcion1 = request.getParameter("descripcionEvento");
                        String artistas1 = request.getParameter("artistasInvitados");
                        String idLugarEventoStr1 = request.getParameter("distrito");
                        String aforoStr1 = request.getParameter("aforo");

                        // Validar campos obligatorios
                        if (!validarCamposEvento(request, nombreEvento1, diaEvento1, horaInicio1, horaFin1, entrada1, razon1, descripcion1, idLugarEventoStr1, aforoStr1, artistas1)) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=1");
                            return;
                        }

                        // Validar y convertir datos
                        int idLugarEvento;
                        try {
                            idLugarEvento = Integer.parseInt(idLugarEventoStr1);
                        } catch (NumberFormatException e) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=1");
                            return;
                        }

                        int aforoEvento;
                        try {
                            aforoEvento = Integer.parseInt(aforoStr1);
                        } catch (NumberFormatException e) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=1");
                            return;
                        }

                        // Obtener la imagen desde el formulario
                        Part filePart1 = request.getPart("fotosEvento");
                        byte[] nuevaFoto = null;
                        String nuevoNombreFoto = null;

                        if (filePart1 != null && filePart1.getSize() > 0) {
                            String fileName1 = filePart1.getSubmittedFileName();
                            if (!fileName1.endsWith(".png")) {
                                response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=1");
                                return;
                            }
                            nuevaFoto = filePart1.getInputStream().readAllBytes();
                            nuevoNombreFoto = filePart1.getSubmittedFileName();
                        }

                        // Obtener evento actual para conservar la imagen si no se sube una nueva
                        PublicacionEventoBenefico eventoActual = daoEvento.buscarPorId(idEvento);

                        // Crear el objeto evento actualizado
                        PublicacionEventoBenefico evento1 = new PublicacionEventoBenefico();
                        evento1.setIdPublicacionEventoBenefico(idEvento);
                        evento1.setNombreEvento(nombreEvento1);
                        evento1.setEntradaEvento(entrada1);
                        evento1.setRazonEvento(razon1);
                        evento1.setAforoEvento(aforoEvento);
                        evento1.setDescripcionEvento(descripcion1);
                        evento1.setArtistasProvedoresInvitados(artistas1);

                        LugarEvento lugar1 = new LugarEvento();
                        lugar1.setId_lugar_evento(idLugarEvento);
                        evento1.setLugarEvento(lugar1);

                        Usuario usuario1 = new Usuario();
                        usuario1.setId_usuario(idAlbergue);
                        evento1.setUsuarioAlbergue(usuario1);

                        try {
                            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            evento1.setFechaHoraInicioEvento(dateTimeFormat.parse(diaEvento1 + " " + horaInicio1));
                            evento1.setFechaHoraFinEvento(dateTimeFormat.parse(diaEvento1 + " " + horaFin1));
                        } catch (Exception e) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=1");
                            return;
                        }

                        // Si no se sube una nueva imagen, conservar la existente
                        if (nuevaFoto != null) {
                            evento1.setFoto(nuevaFoto);
                            evento1.setNombreFoto(nuevoNombreFoto);
                        } else {
                            evento1.setFoto(eventoActual.getFoto());
                            evento1.setNombreFoto(eventoActual.getNombreFoto());
                        }

                        // Actualizar el evento en la base de datos
                        boolean actualizado = daoEvento.actualizarEvento(evento1);
                        if (actualizado) {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&success=true");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=EventoNoModificado");
                        }
                        break;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=editar&error=EventoNoModificado");
                    }
                }
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet?action=lista&errorEncontrado");
        }
    }

    private boolean validarCamposEvento(HttpServletRequest request, String nombreEvento, String diaEvento, String horaInicio, String horaFin,
                                        String entrada, String razon, String descripcion, String idLugarEventoStr, String aforoStr, String artistas) {
        if (nombreEvento == null || diaEvento == null || horaInicio == null || horaFin == null ||
                entrada == null || razon == null || descripcion == null || idLugarEventoStr == null || aforoStr == null) {
            request.setAttribute("error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        if (nombreEvento.length() > 100) {
            request.setAttribute("error", "El nombre del evento no puede exceder los 100 caracteres.");
            return false;
        }
        if (descripcion.length() > 500) {
            request.setAttribute("error", "La descripción del evento no puede exceder los 500 caracteres.");
            return false;
        }
        if (razon.length() > 500) {
            request.setAttribute("error", "El campo de razón del evento no puede exceder los 500 caracteres.");
            return false;
        }
        if (artistas.length() > 300) {
            request.setAttribute("error", "El campo de artistas del evento no puede exceder los 300 caracteres.");
            return false;
        }
        if (entrada.length() > 100) {
            request.setAttribute("error", "El campo de entrada del evento no puede exceder los 100 caracteres.");
            return false;
        }
        return true;
    }
}
