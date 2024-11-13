package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.PuntoAcopio;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilAlbergue;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PerfilAlbergueServlet", value = "/PerfilAlbergueServlet")
public class PerfilAlbergueServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DaoPerfilAlbergue albergueDAO = new DaoPerfilAlbergue();
        RequestDispatcher dispatcher;

        int idUsuarioAlbergue = 7; // ID del usuario albergue, por defecto es 7

        if (accion == null || accion.isEmpty()) {
            accion = "ver"; // Acción por defecto
        }

        switch (accion) {
            case "ver":
                // Obtener perfil del albergue
                Usuario perfilAlbergue = albergueDAO.obtenerPerfilAlbergue(idUsuarioAlbergue);

                // Obtener los puntos de acopio asociados al albergue
                List<PuntoAcopio> puntosAcopio = albergueDAO.obtenerPuntosAcopio(idUsuarioAlbergue);

                // Pasar los objetos al JSP
                request.setAttribute("perfilAlbergue", perfilAlbergue);
                request.setAttribute("puntosAcopio", puntosAcopio);

                // Redirigir al JSP de perfil
                dispatcher = request.getRequestDispatcher("/albergue/PerfilAlbergue.jsp");
                dispatcher.forward(request, response);
                break;

            case "editar":
                // Obtener perfil y puntos de acopio para la edición
                perfilAlbergue = albergueDAO.obtenerPerfilAlbergue(idUsuarioAlbergue);
                puntosAcopio = albergueDAO.obtenerPuntosAcopio(idUsuarioAlbergue);

                // Pasar los datos al JSP para edición
                request.setAttribute("perfilAlbergue", perfilAlbergue);
                request.setAttribute("puntosAcopio", puntosAcopio);

                // Redirigir al JSP de edición
                dispatcher = request.getRequestDispatcher("/albergue/EditarPerfilAlbergue.jsp");
                dispatcher.forward(request, response);
                break;

            default:
                response.sendRedirect("error.jsp");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DaoPerfilAlbergue albergueDAO = new DaoPerfilAlbergue();

        if ("actualizar".equals(accion)) {
            int idUsuarioAlbergue = 7; // ID del albergue (por defecto)

            // Recibir y establecer los datos enviados del formulario
            Usuario usuario = new Usuario();
            usuario.setId_usuario(idUsuarioAlbergue);
            usuario.setNombre_albergue(request.getParameter("nombre_albergue"));
            usuario.setNombres_encargado(request.getParameter("nombres_encargado"));
            usuario.setApellidos_encargado(request.getParameter("apellidos_encargado"));
            usuario.setAnio_creacion(request.getParameter("anio_creacion"));

            // Parsear los valores numéricos
            String cantidadAnimalesStr = request.getParameter("cantidad_animales");
            String espacioDisponibleStr = request.getParameter("espacio_disponible");

            usuario.setCantidad_animales(cantidadAnimalesStr != null && !cantidadAnimalesStr.isEmpty() ? Integer.parseInt(cantidadAnimalesStr) : 0);
            usuario.setEspacio_disponible(espacioDisponibleStr != null && !espacioDisponibleStr.isEmpty() ? Integer.parseInt(espacioDisponibleStr) : 0);

            usuario.setUrl_instagram(request.getParameter("url_instagram"));
            usuario.setDireccion_donaciones(request.getParameter("direccion_donaciones"));
            usuario.setNombre_contacto_donaciones(request.getParameter("nombre_contacto_donaciones"));
            usuario.setNumero_contacto_donaciones(request.getParameter("numero_contacto_donaciones"));
            usuario.setNumero_yape_plin(request.getParameter("numero_yape_plin"));

            // Actualizar el perfil del albergue
            albergueDAO.modificarPerfilAlbergue(usuario);

            // Obtener y actualizar puntos de acopio desde el formulario
            List<PuntoAcopio> puntosAcopio = new ArrayList<>();
            String[] direcciones = request.getParameterValues("direccion_punto_acopio");
            String[] distritos = request.getParameterValues("id_distrito");

            if (direcciones != null && distritos != null && direcciones.length == distritos.length) {
                for (int i = 0; i < direcciones.length; i++) {
                    PuntoAcopio puntoAcopio = new PuntoAcopio();
                    puntoAcopio.setDireccion_punto_acopio(direcciones[i] != null ? direcciones[i] : "");

                    Distrito distrito = new Distrito();
                    if (distritos[i] != null && !distritos[i].isEmpty()) {
                        distrito.setId_distrito(Integer.parseInt(distritos[i]));
                    }
                    puntoAcopio.setDistrito(distrito);

                    puntosAcopio.add(puntoAcopio);
                }
            }

            // Actualizar puntos de acopio en la base de datos
            albergueDAO.actualizarPuntosAcopio(puntosAcopio);

            // Redirigir de vuelta al perfil
            response.sendRedirect("PerfilAlbergueServlet?accion=ver");
        }
    }
}

