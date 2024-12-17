package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.PublicacionMascotaPerdida;
import com.example.webapp_petlink.daos.PublicacionMascotaPerdidaDuenioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PublicacionMascotaPerdidaDuenioServlet", value = "/PublicacionMascotaPerdidaDuenioServlet")
public class PublicacionMascotaPerdidaDuenioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion==null) accion = "listar";
        PublicacionMascotaPerdidaDuenioDAO dao = new PublicacionMascotaPerdidaDuenioDAO();

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if ("listar".equals(accion)) {
            // Obtener todas las publicaciones
            List<PublicacionMascotaPerdida> publicaciones = dao.listarPublicacionesDueños();
            StringBuilder json = new StringBuilder("[");

            for (int i = 0; i < publicaciones.size(); i++) {
                PublicacionMascotaPerdida publicacion = publicaciones.get(i);

                json.append("{")
                        .append("\"id\":").append(publicacion.getIdPublicacionMascotaPerdida()).append(",")
                        .append("\"descripcion\":\"").append(publicacion.getDescripcionMascota()).append("\",")
                        .append("\"lugarPerdida\":\"").append(publicacion.getLugarPerdida()).append("\",")
                        .append("\"fechaPerdida\":\"").append(publicacion.getFechaPerdida()).append("\",")
                        .append("\"nombreContacto\":\"").append(publicacion.getNombreContacto()).append("\",")
                        .append("\"celularContacto\":\"").append(publicacion.getCelularContacto()).append("\"")
                        .append("}");

                if (i < publicaciones.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");
            out.print(json.toString());
        } else if ("detalles".equals(accion)) {
            // Obtener detalles de una publicación específica
            try {
                int idPublicacion = Integer.parseInt(request.getParameter("idPublicacion"));
                PublicacionMascotaPerdida publicacion = dao.obtenerDetallesPublicacion(idPublicacion);

                String json = "{"
                        + "\"id\":" + publicacion.getIdPublicacionMascotaPerdida() + ","
                        + "\"descripcion\":\"" + publicacion.getDescripcionMascota() + "\","
                        + "\"lugarPerdida\":\"" + publicacion.getLugarPerdida() + "\","
                        + "\"fechaPerdida\":\"" + publicacion.getFechaPerdida() + "\","
                        + "\"nombreContacto\":\"" + publicacion.getNombreContacto() + "\","
                        + "\"celularContacto\":\"" + publicacion.getCelularContacto() + "\""
                        + "}";

                out.print(json);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        PublicacionMascotaPerdidaDuenioDAO dao = new PublicacionMascotaPerdidaDuenioDAO();

        if ("actualizarEstado".equals(accion)) {
            try {
                int idPublicacion = Integer.parseInt(request.getParameter("idPublicacion"));
                int nuevoEstado = Integer.parseInt(request.getParameter("nuevoEstado"));

                boolean exito = dao.actualizarEstadoPublicacion(idPublicacion, nuevoEstado);

                String json = "{"
                        + "\"exito\":" + exito + ","
                        + "\"mensaje\":\"" + (exito ? "Estado actualizado con éxito" : "Error al actualizar el estado") + "\""
                        + "}";
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros inválidos");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
}
