package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.AdopcionesUsuarioFinalDao;
import com.example.webapp_petlink.daos.LoginDao;
import com.example.webapp_petlink.daos.PuntoAcopioDAO;
import com.example.webapp_petlink.daos.UsuarioDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "SegundoRegistroAlbergueServlet", value = "/SegundoRegistroAlbergueServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class SegundoRegistroAlbergueServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "mostrar" : request.getParameter("action");
        RequestDispatcher view;

        // Validar  el parámetro id_usuario

        HttpSession sesion = request.getSession();

        Usuario sessionUsuario = (Usuario) sesion.getAttribute("usuario");

        int idUsuario = sessionUsuario.getId_usuario();

        Integer idUsuarioParam = idUsuario;

        if (idUsuarioParam == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        Usuario usuario = sessionUsuario;
        //try{
            view = request.getRequestDispatcher("albergue/edicion_inicial.jsp");
            view.forward(request, response);
        /*} catch (Exception e) {
            response.sendRedirect("AdopcionesAlbergueServlet");
        }*/

    }


        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Obtener la sesión actual
            HttpSession session = request.getSession();

            // Obtener el objeto 'usuario' desde la sesión
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Validar si el usuario está en sesión
            if (usuario == null) {
                // Redirigir a login si no hay usuario en sesión
                response.sendRedirect("login.jsp");
                return;
            }

            try {
                // Obtener parámetros del formulario
                String cantidadAnimales = request.getParameter("cantidad_animales");
                String espacioDisponible = request.getParameter("espacio_disponible");
                String anioCreacion = request.getParameter("anio_creacion");
                String idDistrito = request.getParameter("distrito");
                String direccion = request.getParameter("direccion");
                String descripcionPerfil = request.getParameter("sobre_nosotros");
                String puntoAcopio = request.getParameter("punto_acopio1");
                String direccionDonaciones = request.getParameter("direccion_donaciones");
                String nombreContactoDonaciones = request.getParameter("nombre_contacto_donaciones");
                String numeroContactoDonaciones = request.getParameter("numero_contacto_donaciones");
                String numeroYapePlin = request.getParameter("numero_yape_plin");
                String fileNamePerfil = null;
                byte[] fileContentPefil = null;
                try {
                    for (Part filePart : request.getParts()) {
                        if (filePart.getName().equals("foto_perfil") && filePart.getSize() > 0) {
                            fileNamePerfil = filePart.getSubmittedFileName();
                            fileContentPefil = filePart.getInputStream().readAllBytes();
                        }
                    }
                } catch (Exception e) {
                    response.sendRedirect("SegundoRgistroAlbergueServlet");
                }
                String fileNamePortada = null;
                byte[] fileContentPortada = null;
                try {
                    for (Part filePart : request.getParts()) {
                        if (filePart.getName().equals("foto_portada") && filePart.getSize() > 0) {
                            fileNamePortada = filePart.getSubmittedFileName();
                            fileContentPortada = filePart.getInputStream().readAllBytes();
                        }
                    }
                } catch (Exception e) {
                    response.sendRedirect("SegundoRgistroAlbergueServlet");
                }
                String fileNameQr = null;
                byte[] fileContentQr = null;
                try {
                    for (Part filePart : request.getParts()) {
                        if (filePart.getName().equals("fotoQR") && filePart.getSize() > 0) {
                            fileNameQr = filePart.getSubmittedFileName();
                            fileContentQr = filePart.getInputStream().readAllBytes();
                        }
                    }
                } catch (Exception e) {
                    response.sendRedirect("SegundoRgistroAlbergueServlet");
                }

                // Asignar los valores a los atributos del objeto usuario
                usuario.setCantidad_animales(Integer.parseInt(cantidadAnimales));
                usuario.setEspacio_disponible(Integer.parseInt(espacioDisponible));
                usuario.setAnio_creacion(anioCreacion);
                Distrito distrito = new Distrito();
                distrito.setId_distrito(Integer.parseInt(idDistrito));
                usuario.setDistrito(distrito); // Asumimos que tienes una clase Distrito
                usuario.setDireccion(direccion);
                usuario.setDescripcion_perfil(descripcionPerfil);
                usuario.setFoto_perfil(fileContentPefil);
                usuario.setNombre_foto_perfil(fileNamePerfil);
                usuario.setFoto_de_portada_albergue(fileContentPortada);
                usuario.setNombre_foto_de_portada(fileNamePortada);
                usuario.setImagen_qr(fileContentQr);
                usuario.setNombre_imagen_qr(fileNameQr);
                usuario.setDireccion_donaciones(direccionDonaciones);
                usuario.setNombre_contacto_donaciones(nombreContactoDonaciones);
                usuario.setNumero_contacto_donaciones(numeroContactoDonaciones);
                usuario.setNumero_yape_plin(numeroYapePlin);

                // Crear instancia de UsuarioDAO para actualizar la información del usuario
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuarioDAO.updateUsuario(usuario);  // Actualiza el usuario en la base de datos

                // Si se ha proporcionado un punto de acopio, insertarlo en la base de datos
                if (puntoAcopio != null && !puntoAcopio.isEmpty()) {
                    PuntoAcopioDAO puntoAcopioDAO = new PuntoAcopioDAO();
                    puntoAcopioDAO.insertPuntoAcopio(usuario.getId_usuario(), puntoAcopio);  // Inserta el punto de acopio en la base de datos
                }

                usuarioDAO.generarEconomica(usuario);

                String correo = usuario.getCorreo_electronico();
                String contrasenia = usuario.getContrasenia();
                LoginDao loginDao = new LoginDao();
                usuario = loginDao.obtenerUsuario(correo, contrasenia);
                request.getSession().setAttribute("usuario", usuario);

                response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet");

            }   catch (Exception e) {
                // Captura cualquier otro tipo de excepción
                e.printStackTrace();
                response.sendRedirect("AdopcionesAlbergueServlet");  // Redirige a otro servlet para manejar el error
            }

    }
}
