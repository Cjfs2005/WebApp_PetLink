package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.PostulacionHogarTemporal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.BaneosCoordi1;
import com.example.webapp_petlink.daos.EventoUsuarioDao;
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

@WebServlet(name = "BaneosCoordi1Servlet", value = "/BaneosCoordi1Servlet")
public class BaneosCoordi1Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Colocamos la accion que se ejecutará
        String action = request.getParameter("action") == null ? "listas" : request.getParameter("action");

        // 1. Validar sesión del coordinador
        HttpSession session = request.getSession();
        Usuario coordinador = (Usuario) session.getAttribute("usuario");

        if (coordinador == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 2. Obtener el ID del coordinador
        int idCoordinador = coordinador.getId_usuario();

        // 3. Consultar los datos completos del coordinador para obtener su zona
        GestionCoordinadorDao gestionDao = new GestionCoordinadorDao();
        Usuario datosCoordinador = gestionDao.obtenerDatosCoordinador(idCoordinador);
        session.setAttribute("datosUsuario", datosCoordinador);

        // 4. Obtener el ID de la zona
        int idZonaCoordinador = datosCoordinador.getZona().getId_zona();

        // 5. Filtrar las postulaciones aprobadas por la zona del coordinador
        BaneosCoordi1 postulacionDAO = new BaneosCoordi1();

        switch (action) {
            // Caso para listar a todos los usuarios que sean hogar temporal
            case "listas":
                List<PostulacionHogarTemporal> postulacionesAprobadas = postulacionDAO.obtenerPostulacionesAprobadasPorZona(idZonaCoordinador);

                // 6. Pasar la lista filtrada al JSP
                request.setAttribute("postulaciones", postulacionesAprobadas);

                // 7. Redirigir al JSP
                RequestDispatcher dispatcher = request.getRequestDispatcher("/coordinadorZonal/Baneos1.jsp");
                dispatcher.forward(request, response);
                break;

            case "escribir":
                //caso para reenviar al usuairo a escribir un motivo
                int idUser;
                try {
                    idUser = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath()+"/BaneosCoordi1Servlet");
                    return;
                }

                EventoUsuarioDao eventoUsuarioDao = new EventoUsuarioDao();
                Usuario miEstimado = eventoUsuarioDao.obtenerDatoUsuario(idUser);

                request.setAttribute("miEstimado", miEstimado);

                // 7. Redirigir al JSP
                dispatcher = request.getRequestDispatcher("/coordinadorZonal/escribir_motivo.jsp");
                dispatcher.forward(request, response);

                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listas" : request.getParameter("action");

        // Validar sesión
        HttpSession session = request.getSession();
        Usuario coordinador = (Usuario) session.getAttribute("usuario");

        if (coordinador == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        switch (action) {
            case "banear":
                // 1. Obtener parámetros del formulario
                String motivo = request.getParameter("motivo");
                int idUsuarioBaneado;

                try {
                    idUsuarioBaneado = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/BaneosCoordi1Servlet?action=listas");
                    return;
                }

                // 2. Llamar al DAO para registrar el baneo
                BaneosCoordi1 baneosDao = new BaneosCoordi1();
                boolean exito = baneosDao.baneoPorCoordinador(motivo, idUsuarioBaneado);

                // 3. Redirigir según el resultado
                if (exito) {
                    System.out.println("Usuario baneado exitosamente con ID: " + idUsuarioBaneado);
                    response.sendRedirect(request.getContextPath() + "/BaneosCoordi1Servlet?action=listas");
                } else {
                    System.out.println("Error al banear al usuario con ID: " + idUsuarioBaneado);
                    response.sendRedirect(request.getContextPath() + "/BaneosCoordi1Servlet?action=listas&error=1");
                }
                break;

            default:
                doGet(request, response);
                break;
        }
    }
}
