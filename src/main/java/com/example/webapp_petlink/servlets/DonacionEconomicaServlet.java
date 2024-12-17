package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.DonacionEconomicaDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@WebServlet("/ListaSolicitudesDonacionEconomica")
public class DonacionEconomicaServlet extends HttpServlet {
    private final DonacionEconomicaDao donacionEconomicaDao = new DonacionEconomicaDao();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("datosUsuario");

        if (usuario == null) {
            // Si no hay un usuario logueado, redirigir al login
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Determinar la acción solicitada
        String method = request.getMethod();
        String action = request.getParameter("action");
        if (action == null) action = method.equalsIgnoreCase("POST") ? "crear" : "listar";

        try {
            switch (action) {
                case "listar":
                    listarSolicitudes(usuario.getId_usuario(), request, response);
                    break;
                case "crear":
                    crearSolicitud(usuario, request, response);
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
                case "verDetalles":
                    verDetalles(request, response);
                    break;
                case "mostrar":
                    mostrarFormularioCreacion(request, response);
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

    private void listarSolicitudes(int idUsuarioAlbergue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SolicitudDonacionEconomica> solicitudes = donacionEconomicaDao.obtenerSolicitudesActivas(idUsuarioAlbergue);
        request.setAttribute("solicitudes", solicitudes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/DonacionEconomica.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioCreacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/FormularioDonacionEconomica.jsp");
        dispatcher.forward(request, response);
    }

    private void crearSolicitud(Usuario usuario, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Recuperar datos del formulario
            int monto = Integer.parseInt(request.getParameter("monto"));
            String motivo = request.getParameter("motivo");

            // Crear el objeto solicitud
            SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
            solicitud.setMonto_solicitado(monto);
            solicitud.setMotivo(motivo);
            solicitud.setEs_solicitud_activa(true);

            Estado estado = new Estado();
            estado.setId_estado(2); // Estado inicial "Aprobado"
            solicitud.setEstado(estado);

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
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                System.out.println("[DEBUG Servlet] ID de solicitud no proporcionado.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud no proporcionado.");
                return;
            }

            int idSolicitud = Integer.parseInt(idParam);
            System.out.println("[DEBUG Servlet] Procesando detalles de solicitud ID: " + idSolicitud);

            // Obtener detalles de la solicitud
            SolicitudDonacionEconomica solicitud = donacionEconomicaDao.obtenerDetallesPorId(idSolicitud);
            if (solicitud == null) {
                System.out.println("[DEBUG Servlet] Solicitud no encontrada con ID: " + idSolicitud);
                response.sendRedirect(request.getContextPath() + "/ListaSolicitudesDonacionEconomica?action=listar");
                return;
            }

            // Obtener registros de donación asociados
            List<RegistroDonacionEconomica> registros = donacionEconomicaDao.obtenerRegistrosDonacionPorSolicitud(idSolicitud);
            System.out.println("[DEBUG Servlet] Total de registros de donación obtenidos: " + registros.size());

            // Procesar la imagen QR del albergue
            Usuario usuarioAlbergue = solicitud.getUsuario_albergue();
            if (usuarioAlbergue != null && usuarioAlbergue.getImagen_qr() != null) {
                String base64QR = Base64.getEncoder().encodeToString(usuarioAlbergue.getImagen_qr());
                request.setAttribute("imagenQR", base64QR);
                System.out.println("[DEBUG Servlet] Imagen QR convertida a Base64.");
            } else {
                request.setAttribute("imagenQR", null);
                System.out.println("[DEBUG Servlet] Imagen QR no disponible.");
            }

            // Procesar imágenes de los registros de donación económica
            for (RegistroDonacionEconomica registro : registros) {
                if (registro.getImagenDonacionEconomica() != null) {
                    String base64Donacion = Base64.getEncoder().encodeToString(registro.getImagenDonacionEconomica());
                    registro.setNombreImagenDonacionEconomica(base64Donacion); // Usar el atributo existente para enviar la imagen
                    System.out.println("[DEBUG Servlet] Imagen de donación convertida a Base64 para registro ID: " + registro.getIdRegistroDonacionEconomica());
                } else {
                    System.out.println("[DEBUG Servlet] No hay imagen disponible para registro ID: " + registro.getIdRegistroDonacionEconomica());
                }
            }

            // Configurar atributos para el JSP
            request.setAttribute("solicitud", solicitud);
            request.setAttribute("registrosDonacion", registros);

            // Redireccionar al JSP de detalles
            RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/detalles_don_economica.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR Servlet] ID de solicitud no es válido.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        } catch (SQLException e) {
            System.out.println("[ERROR Servlet] Error al obtener detalles: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los detalles de la solicitud.");
        }
    }


    private void modificarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar");
                return;
            }

            int idSolicitud = Integer.parseInt(idParam);
            SolicitudDonacionEconomica solicitud = donacionEconomicaDao.obtenerDetallesPorId(idSolicitud);

            if (solicitud == null) {
                response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar&mensaje=Solicitud no encontrada");
                return;
            }

            request.setAttribute("solicitud", solicitud);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/modificarDonacionEconomica.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al cargar la solicitud para modificar.");
        }
    }

    private void actualizarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("idSolicitud"));
            int monto = Integer.parseInt(request.getParameter("monto"));
            String motivo = request.getParameter("motivo");

            SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
            solicitud.setId_solicitud_donacion_economica(idSolicitud);
            solicitud.setMonto_solicitado(monto);
            solicitud.setMotivo(motivo);

            donacionEconomicaDao.actualizarSolicitud(solicitud);

            response.sendRedirect("ListaSolicitudesDonacionEconomica?action=listar&mensaje=actualizado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al actualizar la solicitud.");
        }
    }
}
