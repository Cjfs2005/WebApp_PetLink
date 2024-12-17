package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.AlbergueDenunciaMaltratoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AlbergueDenunciasServlet", urlPatterns = {"/AlbergueDenunciasServlet"})
public class AlbergueDenunciasServlet extends HttpServlet {

    private final AlbergueDenunciaMaltratoDAO denunciaDAO = new AlbergueDenunciaMaltratoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion == null || accion.equalsIgnoreCase("listar")) {
            listarDenuncias(request, response);
        } else if (accion.equalsIgnoreCase("detalles")) {
            mostrarDetalles(request, response);
        } else {
            listarDenuncias(request, response);
        }
    }

    /**
     * Método para listar todas las denuncias aceptadas y activas.
     */
    private void listarDenuncias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuarioSesion = (Usuario) sesion.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?action=login");
            return;
        }

        try {
            // Obtener todas las denuncias aceptadas
            List<DenunciaMaltratoAnimal> denuncias = denunciaDAO.obtenerDenunciasAceptadas();

            // Pasar la lista de denuncias al JSP
            request.setAttribute("denuncias", denuncias);
            request.getRequestDispatcher("/albergue/denuncias.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar las denuncias");
        }
    }

    /**
     * Método para mostrar los detalles de una denuncia específica.
     */
    private void mostrarDetalles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuarioSesion = (Usuario) sesion.getAttribute("usuario");

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?action=login");
            return;
        }

        try {
            // Obtener el ID de la denuncia desde los parámetros
            String idDenunciaStr = request.getParameter("idDenuncia");

            if (idDenunciaStr == null || idDenunciaStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de denuncia no especificado");
                return;
            }

            int idDenuncia;
            try {
                idDenuncia = Integer.parseInt(idDenunciaStr);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de denuncia inválido");
                return;
            }

            // Obtener la denuncia por ID
            DenunciaMaltratoAnimal denuncia = denunciaDAO.obtenerDenunciaPorId(idDenuncia);

            if (denuncia == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Denuncia no encontrada");
                return;
            }

            // Pasar los detalles de la denuncia al JSP
            request.setAttribute("denuncia", denuncia);
            request.getRequestDispatcher("/albergue/denuncias_detalles.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al mostrar los detalles de la denuncia");
        }
    }
}
