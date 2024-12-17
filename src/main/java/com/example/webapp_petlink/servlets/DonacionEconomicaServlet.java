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

@WebServlet(name = "DonacionProductosServlet", value = "/DonacionProductosServlet")
public class DonacionProductosServlet extends HttpServlet {
    private final DonacionProductos donacionProductosDao = new DonacionProductos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Obtener ID del usuario/albergue desde la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Integer idAlbergue = usuario.getId_usuario();


        // Obtener los datos del usuario/albergue y almacenarlos en sesión
        Usuario datosAlbergue = donacionProductosDao.obtenerDatosAlbergue(idAlbergue);
        session.setAttribute("datosUsuario", datosAlbergue);

        if (action == null) action = "listar"; // Valor por defecto

        try {
            switch (action) {
                case "listar":
                    listarSolicitudes(idAlbergue, request, response);
                    break;
                case "crear":
                    mostrarFormularioCreacion(idAlbergue, request, response);
                    break;
                case "eliminar":
                    eliminarSolicitud(request, response);
                    break;
                case "verDetalles":
                    verDetalles(request, response);
                    break;
                case "modificar":
                    mostrarFormularioModificacion(idAlbergue, request, response);
                    break;
                default:
                    listarSolicitudes(idAlbergue, request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    private void listarSolicitudes(int idAlbergue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SolicitudDonacionProductos> solicitudes = donacionProductosDao.obtenerSolicitudesActivas(idAlbergue);
        request.setAttribute("solicitudes", solicitudes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/DonacionProductos.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioCreacion(int idAlbergue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<PuntoAcopio> puntosAcopio = donacionProductosDao.obtenerPuntosAcopioPorAlbergue(idAlbergue);
        request.setAttribute("puntosAcopio", puntosAcopio);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/crearDonacionProductos.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioModificacion(int idAlbergue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerDetallesPorId(idSolicitud);

            if (solicitud != null) {
                // Asegurarse de que horarioRecepcion no sea nulo
                if (solicitud.getHorarioRecepcion() == null) {
                    solicitud.setHorarioRecepcion(new HorarioRecepcionDonacion());
                }

                List<PuntoAcopio> puntosAcopio = donacionProductosDao.obtenerPuntosAcopioPorAlbergue(idAlbergue);

                request.setAttribute("solicitud", solicitud);
                request.setAttribute("puntosAcopio", puntosAcopio);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/modificarDonacionProductos.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró la solicitud.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        }
    }

    private void verDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerDetallesPorId(idSolicitud);
            List<RegistroDonacionProductos> donantes = donacionProductosDao.obtenerDetallesDonacionesPorSolicitud(idSolicitud);

            request.setAttribute("solicitud", solicitud);
            request.setAttribute("donantes", donantes);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/DetallesDonacionProductos.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de solicitud inválido.");
        }
    }

    private void eliminarSolicitud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            donacionProductosDao.eliminarSolicitudLogica(idSolicitud);
            response.sendRedirect("DonacionProductosServlet?action=listar");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar la solicitud.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("publicar".equals(action)) {
                publicarSolicitud(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarSolicitud(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    private void publicarSolicitud(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Integer idAlbergue = (Integer) session.getAttribute("usuario");

        // Crear solicitud y asociar albergue
        SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
        solicitud.setDescripcionDonaciones(request.getParameter("descripcion"));
        solicitud.setEsSolicitudActiva(true);

        Usuario usuarioAlbergue = new Usuario();
        usuarioAlbergue.setId_usuario(idAlbergue);
        solicitud.setUsuarioAlbergue(usuarioAlbergue);

        response.sendRedirect("DonacionProductosServlet?action=listar");
    }

    private void actualizarSolicitud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            String descripcion = request.getParameter("descripcion");

            SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
            solicitud.setIdSolicitudDonacionProductos(idSolicitud);
            solicitud.setDescripcionDonaciones(descripcion);

            donacionProductosDao.modificarSolicitudCompleta(solicitud);
            response.sendRedirect("DonacionProductosServlet?action=listar");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar la solicitud.");
        }
    }
}
