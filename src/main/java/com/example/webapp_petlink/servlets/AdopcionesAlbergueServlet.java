package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.AdopcionesAlbergueDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "AdopcionesAlbergueServlet", value = "/AdopcionesAlbergueServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)

public class AdopcionesAlbergueServlet extends HttpServlet{

    AdopcionesAlbergueDao adopcionesAlbergueDao = new AdopcionesAlbergueDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
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

        try {
            switch (action) {
                case "listar":
                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);

                        ArrayList<PublicacionMascotaAdopcion> publicaciones = adopcionesAlbergueDao.obtenerListaPublicacionesAdopcion(idUsuario);

                        request.setAttribute("publicacionesAdopcion", publicaciones);

                        view = request.getRequestDispatcher("albergue/adopciones_albergue_lista.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect("error.jsp");
                    }
                    break;
                case "formulario":

                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);

                        view = request.getRequestDispatcher("albergue/adopciones_albergue_crear.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect("error.jsp");
                    }

                    break;
                case "detalles":
                    int idPublicacion = Integer.parseInt(request.getParameter("id_publicacion"));
                    ArrayList<PostulacionMascotaAdopcion> postulaciones = adopcionesAlbergueDao.obtenerPostulacionesPorId(idPublicacion);
                    PublicacionMascotaAdopcion publicacion = adopcionesAlbergueDao.obtenerPublicacionPorId(idPublicacion);

                    request.setAttribute("publicacion", publicacion);
                    request.setAttribute("postulaciones", postulaciones);

                    view = request.getRequestDispatcher("albergue/detalles_adopciones.jsp");
                    view.forward(request, response);
                    break;
                default:
                    response.sendRedirect("error.jsp");
            }
        } catch (Exception e) {
            response.sendRedirect("AdopcionesAlbergueServlet");
        }
    }

    @Override //Post implica que el JSP esta enviando informacion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        RequestDispatcher view;

        // Validar  el parámetro id_usuario

        HttpSession sesion = request.getSession();

        Usuario sessionUsuario = (Usuario) sesion.getAttribute("usuario");

        int idUsuario = sessionUsuario.getId_usuario();

        Usuario usuario = sessionUsuario;
        switch (action) {
            case "crear":
                try{
                    PublicacionMascotaAdopcion publicacion = new PublicacionMascotaAdopcion();
                    publicacion.setUsuarioAlbergue(usuario);
                    publicacion.setTipoRaza(request.getParameter("tipoRaza"));
                    publicacion.setLugarEncontrado(request.getParameter("direccionEncontrado"));
                    publicacion.setDescripcionMascota(request.getParameter("descripcionGeneral"));
                    publicacion.setEdadAproximada(request.getParameter("edadAproximada"));
                    publicacion.setGeneroMascota(request.getParameter("generoMascota"));
                    publicacion.setEstaEnTemporal("Si".equals(request.getParameter("hogarTemporal")));
                    publicacion.setCondicionesAdopcion(request.getParameter("condicionesAdopcion"));
                    publicacion.setNombreMascota(request.getParameter("nombreMascota"));
                    String fileName = null;
                    byte[] fileContent = null;
                    try {
                        for (Part filePart : request.getParts()) {
                            if (filePart.getName().equals("fotosMascota") && filePart.getSize() > 0) {
                                fileName = filePart.getSubmittedFileName();
                                fileContent = filePart.getInputStream().readAllBytes();
                            }
                        }
                    } catch (Exception e) {
                        response.sendRedirect("AdopcionesAlbergueServlet?action=formulario");
                    }

                    publicacion.setFotoMascota(fileContent);
                    publicacion.setNombreFotoMascota(fileName);
                    publicacion.setEsPublicacionActiva(true);
                    publicacion.setFechaHoraRegistro(Timestamp.valueOf(LocalDateTime.now()));

                    Usuario albergue = new Usuario();
                    albergue.setId_usuario(idUsuario);
                    publicacion.setUsuarioAlbergue(albergue);

                    Estado estado = new Estado();
                    estado.setId_estado(2);
                    publicacion.setEstado(estado);

                    adopcionesAlbergueDao.guardarPublicacionAdopcion(publicacion);

                    response.sendRedirect("AdopcionesAlbergueServlet");

                } catch (Exception e) {
                    response.sendRedirect("AdopcionesAlbergueServlet?action=formulario");
                }
                break;
            case "aceptarPostulante":
                try {
                    int idPostulacion = Integer.parseInt(request.getParameter("idPostulacion"));
                    int idAdoptante = Integer.parseInt(request.getParameter("idAdoptante"));
                    int idPublicacion = Integer.parseInt(request.getParameter("idPublicacion"));
                    adopcionesAlbergueDao.aceptarPostulacion(idPostulacion,idPublicacion);
                    adopcionesAlbergueDao.actualizarAdoptantePublicacion(idAdoptante, idPublicacion);
                    response.sendRedirect("AdopcionesAlbergueServlet?action=detalles&id_publicacion=" + request.getParameter("idPublicacion"));
                } catch (Exception e) {
                    response.sendRedirect("AdopcionesAlbergueServlet?action=detalles&id_publicacion=" + request.getParameter("idPublicacion"));
                }
                break;
        }
    }

}
