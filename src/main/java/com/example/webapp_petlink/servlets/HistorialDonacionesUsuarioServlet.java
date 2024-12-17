package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.RegistroDonacionProductos;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.HistorialDonacionesProductosUsuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistorialDonacionesUsuarioServlet", value = "/HistorialDonacionesUsuarioServlet")
public class HistorialDonacionesUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");

        if (action == null || action.isEmpty() || action.equals("listarProductos")) {
            listarHistorialProductos(request, response, usuario.getId_usuario());
        } else if (action.equals("listarEconomica")) {
            response.sendRedirect(request.getContextPath() + "/HistorialEconomicaUsuarioServlet");
        } else {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    private void listarHistorialProductos(HttpServletRequest request, HttpServletResponse response, int idUsuarioFinal) throws ServletException, IOException {
        HistorialDonacionesProductosUsuario historialDao = new HistorialDonacionesProductosUsuario();
        List<RegistroDonacionProductos> historial = null;

        try {
            // Intentar obtener el historial de donaciones
            historial = historialDao.obtenerHistorialDonaciones(idUsuarioFinal);

            if (historial == null || historial.isEmpty()) {
                System.out.println("Historial vacío para el usuario con ID: " + idUsuarioFinal);
                request.setAttribute("mensaje", "No hay información disponible para mostrar.");
            } else {
                System.out.println("Historial encontrado para el usuario con ID: " + idUsuarioFinal);
                for (RegistroDonacionProductos registro : historial) {
                    System.out.println("===== Registro de Donación =====");
                    System.out.println("ID de Donación: " + registro.getIdRegistroDonacionProductos());
                    System.out.println("Productos Donados: " + registro.getDescripcionesDonaciones());
                    System.out.println("Fecha de Registro: " + registro.getFechaHoraRegistro());

                    if (registro.getEstado() != null) {
                        System.out.println("Estado de la Donación: " + registro.getEstado().getNombre_estado());
                    } else {
                        System.out.println("Estado de la Donación: Información no disponible");
                    }

                    // Validar y mostrar información adicional
                    if (registro.getHorarioRecepcionDonacion() != null) {
                        System.out.println("Horario de Recepción: "
                                + registro.getHorarioRecepcionDonacion().getFechaHoraInicio() + " - "
                                + registro.getHorarioRecepcionDonacion().getFechaHoraFin());
                        if (registro.getHorarioRecepcionDonacion().getPuntoAcopioDonacion() != null) {
                            System.out.println("Punto de Acopio: "
                                    + registro.getHorarioRecepcionDonacion().getPuntoAcopioDonacion().getPuntoAcopio().getDireccion_punto_acopio());
                        }
                    }
                }

                // Pasar historial al JSP
                request.setAttribute("historial", historial);
            }

        } catch (Exception e) {
            // Manejar cualquier excepción que ocurra
            System.err.println("Error al obtener el historial de donaciones: " + e.getMessage());
            e.printStackTrace();

            // Agregar un mensaje de error para mostrar en el JSP
            request.setAttribute("mensaje", "Ocurrió un error al obtener el historial. Intente nuevamente más tarde.");
        } finally {
            // Redirigir siempre al JSP, con o sin datos válidos
            RequestDispatcher dispatcher = request.getRequestDispatcher("/usuarioFinal/Historial1.jsp");
            dispatcher.forward(request, response);
        }
    }


}