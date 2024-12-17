package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.SolicitudDonacionEconomica;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DonacionEconomicaUsuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DonacionEconomicaUsuarioServlet", value = "/DonacionEconomicaUsuarioServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class DonacionEconomicaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        System.out.println("[INFO] Usuario autenticado con ID: " + usuario.getId_usuario());

        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        DonacionEconomicaUsuario donacionDao = new DonacionEconomicaUsuario();

        System.out.println("[INFO][DonacionEconomicaUsuarioServlet] Acción recibida: " + action);

        try {
            switch (action) {
                case "listar":
                    System.out.println("[DEBUG][DonacionEconomicaUsuarioServlet] Listando solicitudes activas...");
                    List<SolicitudDonacionEconomica> solicitudes = donacionDao.obtenerTodasSolicitudesActivas();
                    Map<Integer, Integer> montosRecaudados = donacionDao.obtenerMontosRecaudados();

                    request.setAttribute("solicitudes", solicitudes);
                    request.setAttribute("montosRecaudados", montosRecaudados);
                    request.getRequestDispatcher("usuarioFinal/ListaSolicitudesDonacionEconomica.jsp").forward(request, response);
                    break;

                case "detalle":
                    int idSolicitud = Integer.parseInt(request.getParameter("id"));
                    System.out.println("[DEBUG][DonacionEconomicaUsuarioServlet] ID de solicitud económica recibida: " + idSolicitud);

                    SolicitudDonacionEconomica solicitud = donacionDao.obtenerDetalleSolicitudEconomica(idSolicitud);
                    int montoRecaudado = donacionDao.obtenerMontosRecaudados().getOrDefault(idSolicitud, 0);

                    if (solicitud != null) {
                        System.out.println("[DEBUG][DonacionEconomicaUsuarioServlet] Detalles encontrados: ID=" + idSolicitud +
                                ", Monto solicitado=" + solicitud.getMonto_solicitado() +
                                ", Monto recaudado=" + montoRecaudado);

                        request.setAttribute("solicitud", solicitud);
                        request.setAttribute("montoRecaudado", montoRecaudado);
                        request.getRequestDispatcher("usuarioFinal/FormularioDonacionEconomica.jsp").forward(request, response);
                    } else {
                        System.out.println("[ERROR][DonacionEconomicaUsuarioServlet] No se encontraron detalles para la solicitud con ID: " + idSolicitud);
                        response.sendRedirect(request.getContextPath() + "/error.jsp");
                    }
                    break;

                default:
                    System.out.println("[ERROR][DonacionEconomicaUsuarioServlet] Acción no reconocida: " + action);
                    response.sendRedirect(request.getContextPath() + "/error.jsp");
                    break;
            }
        } catch (Exception e) {
            System.out.println("[ERROR][DonacionEconomicaUsuarioServlet] Excepción capturada: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        System.out.println("[INFO] Usuario autenticado con ID: " + usuario.getId_usuario());

        String action = request.getParameter("action");
        DonacionEconomicaUsuario donacionDao = new DonacionEconomicaUsuario();

        if ("crearEconomica".equals(action)) {
            try {
                // Capturar parámetros del formulario
                String idSolicitudParam = request.getParameter("idSolicitudDonacionEconomica");
                if (idSolicitudParam == null || idSolicitudParam.isEmpty()) {
                    throw new IllegalArgumentException("El parámetro idSolicitudDonacionEconomica no puede ser null o vacío.");
                }
                int idSolicitud = Integer.parseInt(idSolicitudParam);

                String montoDonacionParam = request.getParameter("montoDonacion");
                if (montoDonacionParam == null || montoDonacionParam.isEmpty()) {
                    throw new IllegalArgumentException("El parámetro montoDonacion no puede ser null o vacío.");
                }
                int montoDonacion = Integer.parseInt(montoDonacionParam);

                // Procesar el archivo de imagen
                Part archivo = request.getPart("archivo");
                if (archivo == null || archivo.getSize() == 0) {
                    throw new IllegalArgumentException("El archivo de imagen es requerido.");
                }
                if (!archivo.getContentType().startsWith("image/")) {
                    throw new IllegalArgumentException("El archivo debe ser una imagen válida (PNG o JPEG).");
                }
                byte[] imagenDonacion = archivo.getInputStream().readAllBytes();
                String nombreImagen = archivo.getSubmittedFileName();

                // Registrar la donación económica
                int idEstado = 2; // Estado por defecto
                boolean registrado = donacionDao.registrarDonacionEconomica(usuario.getId_usuario(), montoDonacion, imagenDonacion, nombreImagen, idSolicitud, idEstado);

                if (registrado) {
                    System.out.println("[INFO][DonacionEconomicaUsuarioServlet] Donación registrada con éxito.");
                    response.sendRedirect(request.getContextPath() + "/DonacionEconomicaUsuarioServlet?action=listar");
                } else {
                    throw new Exception("No se pudo registrar la donación.");
                }

            } catch (Exception e) {
                System.out.println("[ERROR][DonacionEconomicaUsuarioServlet] Error al procesar la donación económica: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("usuarioFinal/FormularioDonacionEconomica.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Acción no permitida.");
        }
    }
}
