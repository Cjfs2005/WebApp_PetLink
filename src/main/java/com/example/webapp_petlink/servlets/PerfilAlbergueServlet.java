package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.PuntoAcopio;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilAlbergue;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PerfilAlbergueServlet", value = "/PerfilAlbergueServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class PerfilAlbergueServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DaoPerfilAlbergue albergueDAO = new DaoPerfilAlbergue();
        RequestDispatcher dispatcher;

        if (accion == null || accion.isEmpty()) {
            accion = "ver";
        }

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        int idUsuarioAlbergue = usuario.getId_usuario();

        switch (accion) {
            case "ver":
                Usuario perfilAlbergue = albergueDAO.obtenerPerfilAlbergue(idUsuarioAlbergue);
                List<PuntoAcopio> puntosAcopio = albergueDAO.obtenerPuntosAcopio(idUsuarioAlbergue);

                request.setAttribute("perfilAlbergue", perfilAlbergue);
                request.setAttribute("puntosAcopio", puntosAcopio);

                dispatcher = request.getRequestDispatcher("/albergue/PerfilAlbergue.jsp");
                dispatcher.forward(request, response);
                break;

            case "editar":
                perfilAlbergue = albergueDAO.obtenerPerfilAlbergue(idUsuarioAlbergue);
                puntosAcopio = albergueDAO.obtenerPuntosAcopio(idUsuarioAlbergue);

                request.setAttribute("perfilAlbergue", perfilAlbergue);
                request.setAttribute("puntosAcopio", puntosAcopio);

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
            Usuario usuario = new Usuario();
            HttpSession session = request.getSession();
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
            int idUsuarioAlbergue = usuarioSesion.getId_usuario();

            usuario.setId_usuario(idUsuarioAlbergue);
            usuario.setNombre_albergue(request.getParameter("nombre_albergue"));
            usuario.setNombres_encargado(request.getParameter("nombres_encargado"));
            usuario.setApellidos_encargado(request.getParameter("apellidos_encargado"));
            usuario.setAnio_creacion(request.getParameter("anio_creacion"));

            String cantidadAnimalesStr = request.getParameter("cantidad_animales");
            String espacioDisponibleStr = request.getParameter("espacio_disponible");

            usuario.setCantidad_animales(cantidadAnimalesStr != null && !cantidadAnimalesStr.isEmpty() ? Integer.parseInt(cantidadAnimalesStr) : 0);
            usuario.setEspacio_disponible(espacioDisponibleStr != null && !espacioDisponibleStr.isEmpty() ? Integer.parseInt(espacioDisponibleStr) : 0);

            usuario.setUrl_instagram(request.getParameter("url_instagram"));
            usuario.setDireccion_donaciones(request.getParameter("direccion_donaciones"));
            usuario.setNombre_contacto_donaciones(request.getParameter("nombre_contacto_donaciones"));
            usuario.setNumero_contacto_donaciones(request.getParameter("numero_contacto_donaciones"));
            usuario.setNumero_yape_plin(request.getParameter("numero_yape_plin"));
            usuario.setDescripcion_perfil(request.getParameter("descripcion_perfil"));

            // Manejo de imágenes (foto de perfil, portada, QR)
            Part fotoPerfil = request.getPart("foto_perfil");
            Part fotoPortada = request.getPart("foto_portada");
            Part qrYape = request.getPart("imagen_qr");

            if (fotoPerfil != null && fotoPerfil.getSize() > 0) {
                InputStream is = fotoPerfil.getInputStream();
                usuario.setFoto_perfil(is.readAllBytes());
            }

            if (fotoPortada != null && fotoPortada.getSize() > 0) {
                InputStream is = fotoPortada.getInputStream();
                usuario.setFoto_de_portada_albergue(is.readAllBytes());
            }

            if (qrYape != null && qrYape.getSize() > 0) {
                InputStream is = qrYape.getInputStream();
                usuario.setImagen_qr(is.readAllBytes());
            }

            // Actualizar los datos en la base de datos
            albergueDAO.modificarPerfilAlbergue(usuario);

            // Manejo de puntos de acopio
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

            // Actualizar puntos de acopio
            albergueDAO.actualizarPuntosAcopio(puntosAcopio);

            // Redirigir al perfil actualizado
            response.sendRedirect("PerfilAlbergueServlet?accion=ver");
        }
    }

}
