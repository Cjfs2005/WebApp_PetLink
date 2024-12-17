package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.BaneoHogarTemporal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.Baneos2;
import com.example.webapp_petlink.daos.GestionCoordinadorDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BaneosCoordi2Servlet", value = "/BaneosCoordi2Servlet")
public class BaneosCoordi2Servlet extends HttpServlet {

    private final Baneos2 baneosDao = new Baneos2();
    private final GestionCoordinadorDao gestionCoordinadorDao = new GestionCoordinadorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Determinar acción a ejecutar
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");

        // 1. Validar la sesión del coordinador
        HttpSession session = request.getSession();
        Usuario coordinador = (Usuario) session.getAttribute("usuario");

        if (coordinador == null) {
            System.out.println("[DEBUG] No hay sesión activa para el coordinador.");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 2. Obtener el ID del coordinador
        int idCoordinador = coordinador.getId_usuario();
        System.out.println("[DEBUG] ID del coordinador: " + idCoordinador);

        // 3. Consultar los datos del coordinador para obtener su zona
        Usuario datosCoordinador = gestionCoordinadorDao.obtenerDatosCoordinador(idCoordinador);
        if (datosCoordinador == null || datosCoordinador.getZona() == null) {
            System.out.println("[ERROR] No se encontraron los datos de la zona del coordinador.");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "El coordinador no tiene una zona asignada.");
            return;
        }

        // Guardar los datos del coordinador en sesión
        session.setAttribute("datosUsuario", datosCoordinador);

        // 4. Obtener el ID de la zona del coordinador
        int idZonaCoordinador = datosCoordinador.getZona().getId_zona();
        System.out.println("[DEBUG] Zona del coordinador: " + idZonaCoordinador);

        try {
            switch (action) {
                case "listar":
                    listarBaneosPorZona(request, response, idZonaCoordinador);
                    break;

                default:
                    System.out.println("[ERROR] Acción no reconocida: " + action);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida.");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    private void listarBaneosPorZona(HttpServletRequest request, HttpServletResponse response, int idZonaCoordinador)
            throws ServletException, IOException {
        // 5. Obtener los baneos automáticos y manuales para la zona
        System.out.println("[DEBUG] Listando baneos para la zona: " + idZonaCoordinador);

        // Listar baneos automáticos
        List<BaneoHogarTemporal> baneosAutomaticos = baneosDao.listarBaneosPorTipo(idZonaCoordinador, 0);
        System.out.println("[DEBUG] Baneos automáticos encontrados: " + baneosAutomaticos.size());
        for (BaneoHogarTemporal baneo : baneosAutomaticos) {
            System.out.println("ID Automático: " + baneo.getId_baneo_hogar_temporal() +
                    ", Motivo: " + baneo.getMotivo() +
                    ", Fecha: " + baneo.getFecha_hora_registro());
        }

        // Listar baneos manuales
        List<BaneoHogarTemporal> baneosManuales = baneosDao.listarBaneosPorTipo(idZonaCoordinador, 1);
        System.out.println("[DEBUG] Baneos manuales encontrados: " + baneosManuales.size());
        for (BaneoHogarTemporal baneo : baneosManuales) {
            System.out.println("ID Manual: " + baneo.getId_baneo_hogar_temporal() +
                    ", Motivo: " + baneo.getMotivo() +
                    ", Fecha: " + baneo.getFecha_hora_registro());
        }

        // Pasar listas al JSP
        request.setAttribute("baneosAutomaticos", baneosAutomaticos);
        request.setAttribute("baneosManuales", baneosManuales);

        // Redirigir al JSP para mostrar los baneos
        RequestDispatcher dispatcher = request.getRequestDispatcher("/coordinadorZonal/Baneos2.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
