package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.SolicitudDonacionProductos;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DonacionProductosUsuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DonacionProductosUsuarioServlet", value = "/DonacionProductosUsuarioServlet")
public class DonacionProductosUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        DonacionProductosUsuario donacionDao = new DonacionProductosUsuario();
        RequestDispatcher view;

        // Obtener el usuario de la sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Validar si el usuario está autenticado
        if (usuario == null) {
            System.out.println("[ERROR] Usuario no autenticado. Redirigiendo a login.jsp.");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Imprimir el ID del usuario autenticado para depuración
        System.out.println("[DEBUG] Usuario autenticado con ID: " + usuario.getId_usuario());

        try {
            switch (action) {
                case "listar":
                    // Listar todas las solicitudes activas
                    System.out.println("[DEBUG] Listando solicitudes activas...");
                    request.setAttribute("solicitudes", donacionDao.obtenerTodasSolicitudesActivas());
                    view = request.getRequestDispatcher("usuarioFinal/ListaSolicitudesDonacionProductos.jsp");
                    view.forward(request, response);
                    break;

                case "formulario":
                    // Mostrar formulario para una solicitud específica
                    int idSolicitud = Integer.parseInt(request.getParameter("id"));
                    System.out.println("[DEBUG] ID de solicitud recibido: " + idSolicitud);

                    SolicitudDonacionProductos solicitud = donacionDao.obtenerDetallesParaFormulario(idSolicitud);

                    if (solicitud != null) {
                        request.setAttribute("solicitud", solicitud);
                        view = request.getRequestDispatcher("usuarioFinal/FormularioDonacionProductos.jsp");
                        view.forward(request, response);
                    } else {
                        System.out.println("[ERROR] No se encontraron detalles para la solicitud con ID: " + idSolicitud);
                        response.sendRedirect(request.getContextPath() + "/error.jsp");
                    }
                    break;

                default:
                    System.out.println("[ERROR] Acción no reconocida: " + action);
                    response.sendRedirect(request.getContextPath() + "/error.jsp");
                    break;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Ocurrió un error en el servlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        DonacionProductosUsuario donacionDao = new DonacionProductosUsuario();

        // Obtener el usuario de la sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Validar si el usuario está autenticado
        if (usuario == null) {
            System.out.println("[ERROR] Usuario no autenticado. Redirigiendo a login.jsp.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Imprimir el ID del usuario autenticado para depuración
        System.out.println("[DEBUG] Usuario autenticado con ID: " + usuario.getId_usuario());

        if ("crear".equals(action)) {
            try {
                // Obtener datos del formulario
                int idHorarioRecepcionDonacion = Integer.parseInt(request.getParameter("idHorarioRecepcionDonacion"));
                String descripcionDonacion = request.getParameter("descripcionDonacion");

                System.out.println("[DEBUG] Registrando donación...");
                System.out.println("[DEBUG] ID Horario Recepción: " + idHorarioRecepcionDonacion);
                System.out.println("[DEBUG] Descripción: " + descripcionDonacion);

                // Registrar la donación
                boolean exito = donacionDao.registrarDonacion(usuario.getId_usuario(), idHorarioRecepcionDonacion, descripcionDonacion);

                if (exito) {
                    System.out.println("[DEBUG] Donación registrada exitosamente.");
                    response.sendRedirect(request.getContextPath() + "/DonacionProductosUsuarioServlet?action=listar");
                } else {
                    System.out.println("[ERROR] Falló el registro de la donación.");
                    response.sendRedirect(request.getContextPath() + "/error.jsp");
                }

            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Error de formato en los datos del formulario: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            } catch (Exception e) {
                System.out.println("[ERROR] Error desconocido al registrar la donación: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } else {
            System.out.println("[ERROR] Acción no reconocida en POST: " + action);
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
