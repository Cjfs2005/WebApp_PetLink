package com.example.webapp_petlink.servlets;
import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.DonacionProductos;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/ListaSolicitudesDonacionProductos")
public class DonacionProductosServlet extends HttpServlet {
    private DonacionProductos donacionProductosDao = new DonacionProductos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar"; // Valor por defecto

        try {
            switch (action) {
                case "listar":
                    listarSolicitudes(request, response); // Listar todas las solicitudes
                    break;

                case "crear":
                    mostrarFormularioCreacion(request, response); // Mostrar formulario de creación
                    break;

                case "eliminar":
                    eliminarSolicitud(request, response); // Eliminar una solicitud (lógica)
                    break;

                case "verDetalles":
                    verDetalles(request, response); // Ver detalles de una solicitud (incluye donantes)
                    break;
                case "modificar":
                    mostrarFormularioModificacion(request, response);

                default:
                    listarSolicitudes(request, response); // Por defecto, listar solicitudes
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    private void mostrarFormularioModificacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerDetallesPorId(idSolicitud);

            if (solicitud != null) {
                // Enviar la solicitud al JSP de creación
                request.setAttribute("solicitud", solicitud);
                RequestDispatcher dispatcher = request.getRequestDispatcher("crearDonacionProductos.jsp");
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

        RequestDispatcher dispatcher = request.getRequestDispatcher("DonacionProductos.jsp");
        dispatcher.forward(request, response);
    }
    private void verDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener el ID de la solicitud desde la URL
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            System.out.println("Recibido ID de solicitud en servlet: " + idSolicitud);

            // 1. Llamar al DAO para obtener los detalles completos de la solicitud
            SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerDetallesPorId(idSolicitud);
            if (solicitud != null) {
                System.out.println("Solicitud encontrada: " + solicitud.getDescripcionDonaciones());
                if (solicitud.getHorarioRecepcion() != null) {
                    System.out.println("Fecha de entrega (inicio): " + solicitud.getHorarioRecepcion().getFechaHoraInicio());
                    System.out.println("Fecha de entrega (fin): " + solicitud.getHorarioRecepcion().getFechaHoraFin());
                } else {
                    System.out.println("Horario de recepción es NULL");
                }
            } else {
                System.out.println("No se encontró solicitud para el ID: " + idSolicitud);
            }

            // 2. Llamar al DAO para obtener la lista de donantes asociados
            List<RegistroDonacionProductos> donantes = donacionProductosDao.obtenerDetallesDonacionesPorSolicitud(idSolicitud);
            System.out.println("Total de donantes encontrados: " + (donantes != null ? donantes.size() : 0));

            // Mostrar detalles de cada donante y validar relaciones intermedias
            if (donantes != null && !donantes.isEmpty()) {
                for (RegistroDonacionProductos donante : donantes) {
                    System.out.println("Donante encontrado:");
                    System.out.println("Nombre: " + donante.getUsuarioFinal().getNombres_usuario_final());
                    System.out.println("Apellido: " + donante.getUsuarioFinal().getApellidos_usuario_final());
                    System.out.println("Productos Donados: " + donante.getDescripcionesDonaciones());

                    // Validar relaciones intermedias
                    if (donante.getHorarioRecepcionDonacion() != null) {
                        System.out.println("Horario Recepción Donación está presente.");
                        if (donante.getHorarioRecepcionDonacion().getPuntoAcopioDonacion() != null) {
                            System.out.println("Punto Acopio Donación está presente.");
                            if (donante.getHorarioRecepcionDonacion().getPuntoAcopioDonacion().getPuntoAcopio() != null) {
                                System.out.println("Dirección Punto Acopio: " +
                                        donante.getHorarioRecepcionDonacion().getPuntoAcopioDonacion().getPuntoAcopio().getDireccion_punto_acopio());
                            } else {
                                System.out.println("Punto Acopio es NULL.");
                            }
                        } else {
                            System.out.println("Punto Acopio Donación es NULL.");
                        }
                    } else {
                        System.out.println("Horario Recepción Donación es NULL.");
                    }
                }
            } else {
                System.out.println("No se encontraron donantes para la solicitud con ID: " + idSolicitud);
            }

            // 3. Enviar los datos al JSP
            request.setAttribute("solicitud", solicitud);
            request.setAttribute("donantes", donantes);

            // 4. Redirigir a la vista de detalles
            RequestDispatcher dispatcher = request.getRequestDispatcher("DetallesDonacionProductos.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            System.out.println("Error: ID de solicitud inválido.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        } catch (Exception e) {
            System.out.println("Error inesperado en el servlet:");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al mostrar los detalles de la solicitud.");
        }
    }




    private void eliminarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            donacionProductosDao.eliminarSolicitudLogica(idSolicitud); // Llama al nuevo método
            response.sendRedirect("ListaSolicitudesDonacionProductos?action=listar");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        } catch (Exception e) {
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

        RequestDispatcher dispatcher = request.getRequestDispatcher("crearDonacionProductos.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("publicar".equals(action)) {
                publicarSolicitud(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarSolicitud(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud: " + e.getMessage());
        }
    }

    private void publicarSolicitud(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Capturar datos del formulario
        String descripcion = request.getParameter("descripcion");
        String fechaRecepcionParam = request.getParameter("fechaRecepcion"); // Ejemplo: "2024-11-20"
        String horaInicioParam = request.getParameter("horaInicioEvento"); // Ejemplo: "23:29"
        String horaFinParam = request.getParameter("horaFinEvento"); // Ejemplo: "23:59"

        // Combinar fecha y hora en un formato compatible
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime horaInicio = LocalDateTime.parse(fechaRecepcionParam + "T" + horaInicioParam, formatter);
        LocalDateTime horaFin = LocalDateTime.parse(fechaRecepcionParam + "T" + horaFinParam, formatter);

        // Obtener el punto de acopio seleccionado
        String puntoSeleccionado = request.getParameter("puntoAcopio");
        if (puntoSeleccionado == null || puntoSeleccionado.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un punto de acopio.");
        }
        int idPuntoAcopio = Integer.parseInt(puntoSeleccionado);

        // Crear el objeto solicitud
        SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
        solicitud.setDescripcionDonaciones(descripcion);
        solicitud.setFechaHoraRegistro(Date.valueOf(fechaRecepcionParam));
        solicitud.setEsSolicitudActiva(true);

        Usuario usuarioAlbergue = new Usuario();
        usuarioAlbergue.setId_usuario(6); // ID fijo para pruebas
        solicitud.setUsuarioAlbergue(usuarioAlbergue);

        // Asignar el estado inicial
        Estado estado = new Estado();
        estado.setId_estado(1); // Estado "Pendiente"
        solicitud.setEstado(estado);

        // Llamar al DAO para insertar
        donacionProductosDao.crearSolicitudConRelacion(solicitud, idPuntoAcopio, horaInicio, horaFin);

        // Redirigir a la lista de solicitudes
        response.sendRedirect("ListaSolicitudesDonacionProductos?action=listar");
    }


}
