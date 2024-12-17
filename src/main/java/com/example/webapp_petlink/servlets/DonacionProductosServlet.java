package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.DonacionProductos;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/ListaSolicitudesDonacionProductos")
public class DonacionProductosServlet extends HttpServlet {
    private final DonacionProductos donacionProductosDao = new DonacionProductos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        Integer idAlbergue = (Integer) session.getAttribute("id_usuario");

        if (idAlbergue == null) {
            idAlbergue = 6;
        }

        Usuario datosAlbergue = donacionProductosDao.obtenerDatosAlbergue(idAlbergue);
        session.setAttribute("datosUsuario", datosAlbergue);

        if (action == null) action = "listar"; // Valor por defecto

        try {
            switch (action) {
                case "listar":
                    listarSolicitudes(request, response);
                    break;

                case "crear":
                    mostrarFormularioCreacion(request, response);
                    break;

                case "eliminar":
                    eliminarSolicitud(request, response);
                    break;

                case "verDetalles":

                    verDetalles(request, response);
                    break;
                case "modificar": // Aquí llamamos a mostrarFormularioModificacion
                    mostrarFormularioModificacion(request, response);
                    break;

                default:
                    listarSolicitudes(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    private void mostrarFormularioModificacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID de la solicitud no fue proporcionado.");
                return;
            }

            int idSolicitud = Integer.parseInt(idParam);
            SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerDetallesPorId(idSolicitud);

            if (solicitud != null) {
                // Asegurarse de que horarioRecepcion no sea nulo
                if (solicitud.getHorarioRecepcion() == null) {
                    solicitud.setHorarioRecepcion(new HorarioRecepcionDonacion());
                }

                // Cargar puntos de acopio
                int idUsuarioAlbergue = 6; // ID de usuario del albergue
                List<PuntoAcopio> puntosAcopio = donacionProductosDao.obtenerPuntosAcopioPorAlbergue(idUsuarioAlbergue);

                request.setAttribute("solicitud", solicitud);
                request.setAttribute("puntosAcopio", puntosAcopio);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/modificarDonacionProductos.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró la solicitud con el ID especificado.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        }
    }




    private void listarSolicitudes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuarioAlbergue = 6; // ID de albergue (prueba)
        List<SolicitudDonacionProductos> solicitudes = donacionProductosDao.obtenerSolicitudesActivas(idUsuarioAlbergue);
        if (solicitudes != null) {
            request.setAttribute("solicitudes", solicitudes);
        } else {
            request.setAttribute("error", "No se pudieron cargar las solicitudes.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/DonacionProductos.jsp");
        dispatcher.forward(request, response);
    }

    private void verDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud no proporcionado.");
                return;
            }

            int idSolicitud = Integer.parseInt(idParam);
            SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerDetallesPorId(idSolicitud);

            if (solicitud == null) {
                response.sendRedirect(request.getContextPath() + "/ListaSolicitudesDonacionProductos?action=listar");
                return;
            }

            List<RegistroDonacionProductos> donantes = donacionProductosDao.obtenerDetallesDonacionesPorSolicitud(idSolicitud);
            request.setAttribute("solicitud", solicitud);
            request.setAttribute("donantes", donantes);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/DetallesDonacionProductos.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        }
    }

    private void eliminarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            System.out.println("ID recibido en eliminarSolicitud: " + idParam); // <-- Línea para depuración

            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID de la solicitud no fue proporcionado.");
                return;
            }

            int idSolicitud = Integer.parseInt(idParam);
            System.out.println("ID convertido a entero: " + idSolicitud); // <-- Línea para depuración

            donacionProductosDao.eliminarSolicitudLogica(idSolicitud);
            System.out.println("Solicitud eliminada con éxito para ID: " + idSolicitud); // <-- Línea para depuración

            response.sendRedirect("ListaSolicitudesDonacionProductos?action=listar");
        } catch (NumberFormatException e) {
            System.err.println("Error: ID de solicitud inválido."); // <-- Línea para depuración
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        } catch (Exception e) {
            System.err.println("Error al desactivar la solicitud."); // <-- Línea para depuración
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al desactivar la solicitud.");
        }
    }


    private void mostrarFormularioCreacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuarioAlbergue = 6; // ID de albergue para pruebas
        List<PuntoAcopio> puntosAcopio = donacionProductosDao.obtenerPuntosAcopioPorAlbergue(idUsuarioAlbergue);

        if (puntosAcopio != null) {
            request.setAttribute("puntosAcopio", puntosAcopio);
        } else {
            request.setAttribute("error", "No se pudieron cargar los puntos de acopio.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/crearDonacionProductos.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("publicar".equals(action)) {
                publicarSolicitud(request, response);
            } else if("actualizar".equals(action)) {
                actualizarSolicitud(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }



    private void publicarSolicitud(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // Recuperar parámetros del formulario
            String descripcion = request.getParameter("descripcion");
            String fechaRecepcionParam = request.getParameter("fechaRecepcion");
            String horaInicioParam = request.getParameter("horaInicioEvento");
            String horaFinParam = request.getParameter("horaFinEvento");
            String idPuntoAcopioParam = request.getParameter("puntoAcopio");

            // Validar parámetros obligatorios
            if (descripcion == null || fechaRecepcionParam == null || horaInicioParam == null || horaFinParam == null || idPuntoAcopioParam == null ||
                    descripcion.trim().isEmpty() || fechaRecepcionParam.trim().isEmpty() || horaInicioParam.trim().isEmpty() || horaFinParam.trim().isEmpty() || idPuntoAcopioParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Faltan parámetros obligatorios: descripción, fecha, hora o punto de acopio.");
            }

            // Convertir las fechas y horas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm");
            LocalDateTime horaInicio = LocalDateTime.parse(fechaRecepcionParam + "T" + horaInicioParam, formatter);
            LocalDateTime horaFin = LocalDateTime.parse(fechaRecepcionParam + "T" + horaFinParam, formatter);

            int idPuntoAcopio = Integer.parseInt(idPuntoAcopioParam);

            // Crear la solicitud
            SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
            solicitud.setDescripcionDonaciones(descripcion);
            solicitud.setFechaHoraRegistro(Date.valueOf(fechaRecepcionParam));
            solicitud.setEsSolicitudActiva(true);

            // Asociar usuario albergue
            Usuario usuarioAlbergue = new Usuario();
            usuarioAlbergue.setId_usuario(6); // ID de prueba
            solicitud.setUsuarioAlbergue(usuarioAlbergue);

            // Asociar estado inicial
            Estado estado = new Estado();
            estado.setId_estado(2); // Estado inicial
            solicitud.setEstado(estado);

            // Asociar horario y punto de acopio
            HorarioRecepcionDonacion horario = new HorarioRecepcionDonacion();
            horario.setFechaHoraInicio(horaInicio);
            horario.setFechaHoraFin(horaFin);

            PuntoAcopio puntoAcopio = new PuntoAcopio();
            puntoAcopio.setId_punto_acopio(idPuntoAcopio);

            PuntoAcopioDonacion puntoAcopioDonacion = new PuntoAcopioDonacion();
            puntoAcopioDonacion.setPuntoAcopio(puntoAcopio);

            horario.setPuntoAcopioDonacion(puntoAcopioDonacion);
            solicitud.setHorarioRecepcion(horario);

            // Guardar en la base de datos
            donacionProductosDao.crearSolicitudConRelacion(solicitud, idPuntoAcopio, horaInicio, horaFin);

            // Redirigir a la lista con mensaje de éxito
            response.sendRedirect("ListaSolicitudesDonacionProductos?action=listar&mensaje=creado");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }
    private void actualizarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener parámetros del formulario
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            String descripcion = request.getParameter("descripcion");
            String fechaRecepcion = request.getParameter("fechaRecepcion");
            String horaInicioEvento = request.getParameter("horaInicioEvento");
            String horaFinEvento = request.getParameter("horaFinEvento");
            int idPuntoAcopio = Integer.parseInt(request.getParameter("puntoAcopio"));

            // Configurar los datos de la solicitud
            SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
            solicitud.setIdSolicitudDonacionProductos(idSolicitud);
            solicitud.setDescripcionDonaciones(descripcion);

            HorarioRecepcionDonacion horario = new HorarioRecepcionDonacion();
            horario.setFechaHoraInicio(LocalDateTime.parse(fechaRecepcion + "T" + horaInicioEvento));
            horario.setFechaHoraFin(LocalDateTime.parse(fechaRecepcion + "T" + horaFinEvento));
            solicitud.setHorarioRecepcion(horario);

            PuntoAcopio puntoAcopio = new PuntoAcopio();
            puntoAcopio.setId_punto_acopio(idPuntoAcopio);

            PuntoAcopioDonacion puntoAcopioDonacion = new PuntoAcopioDonacion();
            puntoAcopioDonacion.setPuntoAcopio(puntoAcopio);
            horario.setPuntoAcopioDonacion(puntoAcopioDonacion);

            // Llamar al DAO para actualizar todos los datos
            donacionProductosDao.modificarSolicitudCompleta(solicitud);

            // Redirigir con mensaje de éxito
            System.out.println(idSolicitud);
            System.out.println(descripcion);
            System.out.println(fechaRecepcion);
            System.out.println(horaInicioEvento);
            System.out.println(horaFinEvento);
            System.out.println(idPuntoAcopio);


            response.sendRedirect("ListaSolicitudesDonacionProductos?action=listar&mensaje=actualizado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar la solicitud.");
        }
    }

}
