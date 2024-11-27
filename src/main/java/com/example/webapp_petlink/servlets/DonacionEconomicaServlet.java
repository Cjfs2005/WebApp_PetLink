    package com.example.webapp_petlink.servlets;

    import com.example.webapp_petlink.beans.*;
    import com.example.webapp_petlink.daos.DonacionEconomicaDao;
    import jakarta.servlet.RequestDispatcher;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;

    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.Base64;
    import java.util.List;

    @WebServlet("/ListaSolicitudesDonacionEconomica")
    public class DonacionEconomicaServlet extends HttpServlet {
        private final DonacionEconomicaDao donacionEconomicaDao = new DonacionEconomicaDao();

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Determinar la acción solicitada
            String method = request.getMethod();
            String action = request.getParameter("action");
            if (action == null) action = method.equalsIgnoreCase("POST") ? "crear" : "listar";

            try {
                switch (action) {
                    case "listar":
                        listarSolicitudes(request, response);
                        break;
                    case "crear":
                        crearSolicitud(request, response);
                        break;
                    case "eliminar":
                        eliminarSolicitud(request, response);
                        break;
                    case "modificar":
                        modificarSolicitud(request, response);
                        break;
                    case "actualizar":
                        actualizarSolicitud(request, response);
                        break;
                    case "VerDetalles":
                        verDetalles(request, response);
                        break;
                    default:
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
            }
        }

        private void listarSolicitudes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            int idUsuarioAlbergue = 6; // ID de albergue (prueba)
            List<SolicitudDonacionEconomica> solicitudes = donacionEconomicaDao.obtenerSolicitudesActivas(idUsuarioAlbergue);
            request.setAttribute("solicitudes", solicitudes);
            RequestDispatcher dispatcher = request.getRequestDispatcher("DonacionEconomica.jsp");
            dispatcher.forward(request, response);
        }

        private void crearSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                // Recuperar datos del formulario
                int monto = Integer.parseInt(request.getParameter("monto"));
                String motivo = request.getParameter("motivo");
                int idEstado = Integer.parseInt(request.getParameter("idEstado"));
                int idUsuarioAlbergue = Integer.parseInt(request.getParameter("idUsuarioAlbergue"));

                // Crear el objeto solicitud
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setMonto_solicitado(monto);
                solicitud.setMotivo(motivo);
                solicitud.setEs_solicitud_activa(true);

                Estado estado = new Estado();
                estado.setId_estado(idEstado);
                solicitud.setEstado(estado);

                Usuario usuario = new Usuario();
                usuario.setId_usuario(idUsuarioAlbergue);
                solicitud.setUsuario_albergue(usuario);

                // Guardar en la base de datos
                donacionEconomicaDao.crearSolicitudEconomica(solicitud);

                // Redirigir a la lista
                response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al crear la solicitud.");
            }
        }

        private void eliminarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                int idSolicitud = Integer.parseInt(request.getParameter("id"));
                donacionEconomicaDao.eliminarSolicitudLogica(idSolicitud);
                response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar la solicitud.");
            }
        }



        private void verDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            System.out.println("Iniciando método verDetalles en el Servlet...");

            try {
                // Validar y obtener el parámetro ID
                String idParam = request.getParameter("id");
                if (idParam == null || idParam.trim().isEmpty()) {
                    System.out.println("ID de solicitud no proporcionado.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud no proporcionado.");
                    return;
                }

                int idSolicitud = Integer.parseInt(idParam);
                System.out.println("ID de solicitud recibido: " + idSolicitud);

                // Obtener los detalles de la solicitud económica
                SolicitudDonacionEconomica solicitud = donacionEconomicaDao.obtenerDetallesPorId(idSolicitud);
                System.out.println("Solicitud obtenida: " + solicitud);

                if (solicitud == null) {
                    System.out.println("No se encontró la solicitud con el ID especificado.");
                    response.sendRedirect(request.getContextPath() + "/ListaSolicitudesDonacionEconomica?action=listar");
                    return;
                }

                // Obtener registros de donación asociados a la solicitud
                List<RegistroDonacionEconomica> registros = donacionEconomicaDao.obtenerRegistrosDonacionPorSolicitud(idSolicitud);
                System.out.println("Cantidad de registros obtenidos: " + registros.size());

                // Mostrar datos de cada registro en la consola (opcional para depuración)
                for (RegistroDonacionEconomica registro : registros) {
                    System.out.println("Registro ID: " + registro.getIdRegistroDonacionEconomica());
                    System.out.println("Monto Donado: " + registro.getMontoDonacion());
                    System.out.println("Fecha y Hora: " + registro.getFechaHoraRegistro());
                    System.out.println("Usuario: " + registro.getUsuarioFinal().getNombres_usuario_final() +
                            " " + registro.getUsuarioFinal().getApellidos_usuario_final());
                }

                // Obtener el código QR del usuario asociado (en este caso, el albergue)
                Usuario usuarioAlbergue = solicitud.getUsuario_albergue();
                if (usuarioAlbergue != null && usuarioAlbergue.getImagen_qr() != null) {
                    // Convertir la imagen QR a Base64 para mostrarla en el JSP
                    String base64QR = Base64.getEncoder().encodeToString(usuarioAlbergue.getImagen_qr());
                    request.setAttribute("imagenQR", base64QR);
                    System.out.println("Imagen QR cargada y convertida a Base64.");
                } else {
                    request.setAttribute("imagenQR", null); // Si no hay QR, se deja como null
                    System.out.println("No se encontró imagen QR para este albergue.");
                }

                // Pasar los datos al JSP
                request.setAttribute("solicitud", solicitud);
                request.setAttribute("registrosDonacion", registros);

                // Redirigir al JSP
                RequestDispatcher dispatcher = request.getRequestDispatcher("detalles_don_economica.jsp");
                dispatcher.forward(request, response);
                System.out.println("Redirigiendo al JSP de detalles...");
            } catch (NumberFormatException e) {
                System.out.println("ID de solicitud inválido.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
            } catch (SQLException e) {
                System.out.println("Error al obtener los detalles de la solicitud: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los detalles de la solicitud.");
            }
        }
        private void modificarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                // Obtener el ID de la solicitud
                String idParam = request.getParameter("id");
                if (idParam == null || idParam.trim().isEmpty()) {
                    response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar");
                    return;
                }

                int idSolicitud = Integer.parseInt(idParam);

                // Obtener los detalles de la solicitud desde la base de datos
                SolicitudDonacionEconomica solicitud = donacionEconomicaDao.obtenerDetallesPorId(idSolicitud);

                if (solicitud == null) {
                    response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar&mensaje=Solicitud no encontrada");
                    return;
                }

                // Pasar los datos al JSP
                request.setAttribute("solicitud", solicitud);
                RequestDispatcher dispatcher = request.getRequestDispatcher("modificarDonacionEconomica.jsp");
                dispatcher.forward(request, response);
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al cargar la solicitud para modificar.");
            }
        }



        private void actualizarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                // Recuperar datos del formulario
                int idSolicitud = Integer.parseInt(request.getParameter("idSolicitud"));
                int monto = Integer.parseInt(request.getParameter("monto"));
                String motivo = request.getParameter("motivo");

                // Crear la solicitud actualizada
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setId_solicitud_donacion_economica(idSolicitud);
                solicitud.setMonto_solicitado(monto);
                solicitud.setMotivo(motivo);

                // Actualizar en la base de datos
                donacionEconomicaDao.actualizarSolicitud(solicitud);

                // Redirigir a la lista
                response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar&mensaje=actualizado");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al actualizar la solicitud.");
            }
        }



    }
