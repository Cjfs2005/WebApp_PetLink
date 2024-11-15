package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.daos.DonacionProductos;
import com.example.webapp_petlink.beans.SolicitudDonacionProductos;
import com.example.webapp_petlink.beans.Estado;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/ListaSolicitudesDonacionProductos")
public class DonacionProductosServlet extends HttpServlet {
    private DonacionProductos donacionProductosDao = new DonacionProductos();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "eliminar":
                int idSolicitud = Integer.parseInt(request.getParameter("id"));
                donacionProductosDao.eliminarSolicitud(idSolicitud);
                response.sendRedirect("ListaSolicitudesDonacionProductos");
                break;

            case "verDetalles":
                int idDetalle = Integer.parseInt(request.getParameter("id"));
                SolicitudDonacionProductos solicitud = donacionProductosDao.obtenerSolicitudPorId(idDetalle);

                // Obtener solo la hora de fecha_hora_inicio y fecha_hora_fin
                Time horaInicio = donacionProductosDao.obtenerHoraInicio(idDetalle);
                Time horaFin = donacionProductosDao.obtenerHoraFin(idDetalle);

                // Enviar los atributos al JSP
                request.setAttribute("solicitudDetalles", solicitud);
                request.setAttribute("horaInicio", horaInicio);
                request.setAttribute("horaFin", horaFin);

                RequestDispatcher dispatcher = request.getRequestDispatcher("DetallesDonacionProductos.jsp");
                dispatcher.forward(request, response);
                break;


            case "listar":
            default:
                int idUsuarioAlbergue = 6;  // ID para pruebas
                List<SolicitudDonacionProductos> solicitudes = donacionProductosDao.obtenerSolicitudesActivas(idUsuarioAlbergue);
                request.setAttribute("solicitudes", solicitudes);
                RequestDispatcher listDispatcher = request.getRequestDispatcher("DonacionProductos.jsp");
                listDispatcher.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("publicar".equals(action)) {
            // Recoger los parámetros del formulario
            String descripcion = request.getParameter("descripcion");
            Date fechaRecepcion = Date.valueOf(request.getParameter("fechaRecepcion"));
            Time horaInicio = Time.valueOf(request.getParameter("horaInicioEvento") + ":00");
            Time horaFin = Time.valueOf(request.getParameter("horaFinEvento") + ":00");
            int idUsuarioAlbergue = 6; // ID para pruebas
            int idHorarioRecepcionDonacion = 1; // ID de horario para pruebas; reemplazar según el caso

            // Crear una nueva solicitud de donación
            SolicitudDonacionProductos nuevaSolicitud = new SolicitudDonacionProductos();
            nuevaSolicitud.setDescripcionDonaciones(descripcion);
            nuevaSolicitud.setFechaHoraRegistro(Date.valueOf(LocalDate.now())); // Fecha actual como fecha de registro
            nuevaSolicitud.setEsSolicitudActiva(true);

            // Asignar el estado inicial (ejemplo: "Pendiente")
            Estado estadoInicial = new Estado();
            estadoInicial.setId_estado(1); // ID para estado "Pendiente"
            nuevaSolicitud.setEstado(estadoInicial);

            // Guardar la solicitud en la base de datos
            donacionProductosDao.insertarSolicitud(nuevaSolicitud, idHorarioRecepcionDonacion, idUsuarioAlbergue);

            // Redireccionar a la lista de solicitudes
            response.sendRedirect("ListaSolicitudesDonacionProductos");
        }
    }
}
