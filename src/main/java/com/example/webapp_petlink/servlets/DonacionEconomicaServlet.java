package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.DonacionEconomicaDao;
import com.example.webapp_petlink.daos.EventoUsuarioDao;

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

@WebServlet(name = "DonacionEconomicaServlet", value = "/DonacionEconomicaServlet")
public class DonacionEconomicaServlet extends HttpServlet {
    private final DonacionEconomicaDao donacionEconomicaDao = new DonacionEconomicaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        System.out.println("estamos en el servlet con un user");

        EventoUsuarioDao daoEvento = new EventoUsuarioDao();
        Usuario datosAlbergue = daoEvento.obtenerDatosAlbergue(usuario.getId_usuario());
        session.setAttribute("datosUsuario", datosAlbergue);

        String action = request.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "listar":
                    listarSolicitudes(usuario.getId_usuario(), request, response);
                    break;
                case "modificar":
                    modificarSolicitud(request, response);
                    break;
                case "verDetalles":
                    verDetalles(request, response);
                    break;
                case "mostrar":
                    mostrarFormularioCreacion(request, response);
                    break;
                case "eliminar":
                    eliminarSolicitud(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción GET no válida");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "crear";

        try {
            switch (action) {
                case "crear":
                    crearSolicitud(usuario, request, response);
                    break;
                case "actualizar":
                    actualizarSolicitud(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción POST no válida");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    private void listarSolicitudes(int idUsuarioAlbergue, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<SolicitudDonacionEconomica> solicitudes = donacionEconomicaDao.obtenerSolicitudesActivas(idUsuarioAlbergue);
        request.setAttribute("solicitudes", solicitudes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/DonacionEconomica.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioCreacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/FormularioDonacionEconomica.jsp");
        dispatcher.forward(request, response);
    }

    private void crearSolicitud(Usuario usuario, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int monto = Integer.parseInt(request.getParameter("monto"));
            System.out.println("EL ID ES " + monto);
            String motivo = request.getParameter("motivo");

            SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
            solicitud.setMonto_solicitado(monto);
            solicitud.setMotivo(motivo);
            solicitud.setEs_solicitud_activa(true);

            Estado estado = new Estado();
            estado.setId_estado(2);
            solicitud.setEstado(estado);
            solicitud.setUsuario_albergue(usuario);

            donacionEconomicaDao.crearSolicitudEconomica(solicitud);

            response.sendRedirect("DonacionEconomicaServlet?action=listar");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al crear la solicitud.");
        }
    }

    private void eliminarSolicitud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            donacionEconomicaDao.eliminarSolicitudLogica(idSolicitud);
            response.sendRedirect("DonacionEconomicaServlet?action=listar");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar la solicitud.");
        }
    }

    private void verDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            SolicitudDonacionEconomica solicitud = donacionEconomicaDao.obtenerDetallesPorId(idSolicitud);
            //Para registrar en la tabla los donantes
            List<RegistroDonacionEconomica> donantes = donacionEconomicaDao.obtenerRegistrosDonacionPorSolicitud(idSolicitud);

            if (solicitud != null) {
                request.setAttribute("solicitud", solicitud);
                request.setAttribute("donantes", donantes); // Lista de donantes

                RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/detalles_don_economica.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("DonacionEconomicaServlet?action=listar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los detalles.");
        }
    }

    private void modificarSolicitud(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("id"));
            System.out.println("EL ID ES " + idSolicitud);
            SolicitudDonacionEconomica solicitud = donacionEconomicaDao.obtenerDetallesPorId(idSolicitud);
            if (solicitud != null) {
                System.out.println("HOLAAAAAAAAAA ESTMOS MODIFICANDO");
                request.setAttribute("solicitud", solicitud);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/albergue/modificarDonacionEconomica.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("DonacionEconomicaServlet?action=listar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar la solicitud para modificar.");
        }
    }

    private void actualizarSolicitud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("idSolicitud"));
            int monto = Integer.parseInt(request.getParameter("monto"));
            String motivo = request.getParameter("motivo");

            SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
            solicitud.setId_solicitud_donacion_economica(idSolicitud);
            solicitud.setMonto_solicitado(monto);
            solicitud.setMotivo(motivo);

            donacionEconomicaDao.actualizarSolicitud(solicitud);

            response.sendRedirect("DonacionEconomicaServlet?action=listar&mensaje=actualizado");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al actualizar la solicitud.");
        }
    }
}
