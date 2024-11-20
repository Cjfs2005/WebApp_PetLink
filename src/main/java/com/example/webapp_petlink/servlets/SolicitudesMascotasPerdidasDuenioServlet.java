package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.PublicacionMascotaPerdida;
import com.example.webapp_petlink.daos.DaoPublicacionMascotaPerdidaDuenio;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SolicitudesMascotasPerdidasDuenioServlet", value = "/SolicitudesMascotasPerdidasDuenioServlet")
public class SolicitudesMascotasPerdidasDuenioServlet extends HttpServlet {

    // Método GET para manejar las solicitudes
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DaoPublicacionMascotaPerdidaDuenio dao = new DaoPublicacionMascotaPerdidaDuenio();

        // Acción predeterminada para listar publicaciones
        if (accion == null || accion.isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                // Recuperar parámetros de paginación y búsqueda
                int pagina = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 1;
                int registrosPorPagina = request.getParameter("registros") != null ? Integer.parseInt(request.getParameter("registros")) : 10;
                String criterioBusqueda = request.getParameter("criterio") != null ? request.getParameter("criterio") : "";

                // Obtener la lista de publicaciones con base en los filtros
                List<PublicacionMascotaPerdida> publicaciones = dao.obtenerPublicaciones(pagina, registrosPorPagina, criterioBusqueda);
                request.setAttribute("publicaciones", publicaciones);

                // Redirigir a la vista JSP con la lista de publicaciones
                RequestDispatcher dispatcher = request.getRequestDispatcher("coordinadorZonal/solicitudes_duenio_mascotas_perdidas.jsp");
                dispatcher.forward(request, response);
                break;

            case "detalles":
                // Obtener los detalles de una publicación específica
                int id = Integer.parseInt(request.getParameter("id"));
                PublicacionMascotaPerdida publicacion = dao.obtenerDetallesPublicacion(id);
                request.setAttribute("publicacion", publicacion);

                // No redirigir a otra página, solo pasar la información para mostrar en el modal
                RequestDispatcher dispatcherDetalles = request.getRequestDispatcher("coordinadorZonal/solicitudes_duenio_mascotas_perdidas.jsp");
                dispatcherDetalles.forward(request, response);
                break;

            default:
                // Si no se encuentra la acción, redirigir a la página de error
                response.sendRedirect("error.jsp");
                break;
        }
    }

    // Método POST para manejar las actualizaciones de estado
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DaoPublicacionMascotaPerdidaDuenio dao = new DaoPublicacionMascotaPerdidaDuenio();

        // Acción de actualizar el estado de una publicación
        if ("actualizarEstado".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String estado = request.getParameter("estado");

            // Actualizar el estado de la publicación
            boolean actualizado = dao.actualizarEstadoPublicacion(id, estado);

            // Establecer un mensaje para mostrar en el frontend (por ejemplo, en un modal)
            if (actualizado) {
                request.setAttribute("mensaje", "Estado actualizado correctamente.");
                request.getRequestDispatcher("coordinadorZonal/solicitudes_duenio_mascotas_perdidas.jsp").forward(request, response);
            } else {
                request.setAttribute("mensaje", "Hubo un error al actualizar el estado.");
                request.getRequestDispatcher("coordinadorZonal/solicitudes_duenio_mascotas_perdidas.jsp").forward(request, response);
            }
        }
    }
}
