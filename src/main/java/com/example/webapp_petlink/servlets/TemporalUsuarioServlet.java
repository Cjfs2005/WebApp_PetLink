package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.SolicitudHogarTemporal;
import com.example.webapp_petlink.beans.PostulacionHogarTemporal;
import com.example.webapp_petlink.daos.HogarTemporalDao;
import com.example.webapp_petlink.daos.LoginDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;

@WebServlet(name = "TemporalUsuarioServlet", value = "/TemporalUsuarioServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class TemporalUsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        HogarTemporalDao hogarTemporalDao = new HogarTemporalDao();
        RequestDispatcher view;
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        int idUsuario = usuario.getId_usuario();
        try{
            switch (action) {
                case "listar":
                        boolean esBaneado = hogarTemporalDao.isUsuarioBaneado(idUsuario);
                        String correo = usuario.getCorreo_electronico();
                        String contrasenia = usuario.getContrasenia();
                        LoginDao loginDao = new LoginDao();
                        usuario = loginDao.obtenerUsuario(correo, contrasenia);
                        request.getSession().setAttribute("usuario", usuario);
                        if (usuario != null) {

                            // Verificar si el usuario está habilitado y tiene una última postulación
                            if (usuario.getUltima_postulacion_hogar_temporal() != null &&
                                    usuario.getUltima_postulacion_hogar_temporal().getEstado() != null &&
                                    "Aprobada".equals(usuario.getUltima_postulacion_hogar_temporal().getEstado().getNombre_estado())) {

                                ArrayList<SolicitudHogarTemporal> solicitudes = hogarTemporalDao.obtenerSolicitudesPorPostulacion(usuario.getUltima_postulacion_hogar_temporal());
                                request.setAttribute("solicitudes", solicitudes);
                                esBaneado = hogarTemporalDao.isUsuarioBaneado(idUsuario);
                                request.setAttribute("esBaneado", esBaneado);
                                usuario = hogarTemporalDao.obtenerUsuarioPorId(idUsuario);
                                request.setAttribute("usuario", usuario);
                            } else{
                                request.setAttribute("esBaneado", esBaneado);
                                request.setAttribute("usuario", usuario);
                            }

                            view = request.getRequestDispatcher("usuarioFinal/Hogar_temporal.jsp");
                            view.forward(request, response);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/error.jsp"); // Redirigir si el usuario no existe
                        }
                    break;
                case "postularForm":
                    // Redirigir a formulario de nueva postulación
                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);
                        request.setAttribute("distritos", hogarTemporalDao.obtenerDistritos());
                        view = request.getRequestDispatcher("usuarioFinal/formulario_hogar_temporal.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/error.jsp");
                    }

                    break;
                case "modificarForm":
                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);
                        request.setAttribute("distritos", hogarTemporalDao.obtenerDistritos());
                        view = request.getRequestDispatcher("usuarioFinal/formulario_hogar_temporal_mod.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/error.jsp");
                    }

                    break;
                case "aceptarSolicitud":
                    String idSolicitudParam = request.getParameter("id_solicitud");
                    int idSolicitud = Integer.parseInt(idSolicitudParam);
                    hogarTemporalDao.aceptarSolicitud(idSolicitud);
                    response.sendRedirect("TemporalUsuarioServlet");
                    break;
                case "rechazarSolicitud":
                    idSolicitudParam = request.getParameter("id_solicitud");
                    idSolicitud = Integer.parseInt(idSolicitudParam);
                    hogarTemporalDao.rechazarSolicitud(idSolicitud);
                    response.sendRedirect("TemporalUsuarioServlet");
                    break;
            }
        } catch (Exception e){
            response.sendRedirect("TemporalUsuarioServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
        HogarTemporalDao hogarTemporalDao = new HogarTemporalDao();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        int idUsuario = usuario.getId_usuario();
            if ("postular".equals(action)) {
                try{
                    PostulacionHogarTemporal postulacion = new PostulacionHogarTemporal();
                    postulacion.setUsuario_final(usuario);
                    postulacion.setEdad_usuario(request.getParameter("edad"));
                    postulacion.setGenero_usuario(request.getParameter("genero"));
                    postulacion.setCelular_usuario(request.getParameter("telefono"));
                    postulacion.setCantidad_cuartos(request.getParameter("cuartos"));
                    postulacion.setMetraje_vivienda(request.getParameter("metraje"));
                    postulacion.setTiene_mascotas("1".equals(request.getParameter("tieneMascota")));
                    postulacion.setTipo_mascotas(request.getParameter("tipoMascotas"));
                    postulacion.setTiene_hijos("1".equals(request.getParameter("tieneHijo")));
                    postulacion.setTiene_dependientes("2".equals(request.getParameter("viveSolo")));
                    postulacion.setForma_trabajo("1".equals(request.getParameter("trabajaRemoto")) ? "Remoto" : "Presencial");
                    postulacion.setNombre_persona_referencia(request.getParameter("nombreRef"));
                    postulacion.setCelular_persona_referencia(request.getParameter("numeroRef"));
                    postulacion.setFecha_inicio_temporal(LocalDate.parse(request.getParameter("fechaInicio")));
                    postulacion.setFecha_fin_temporal(LocalDate.parse(request.getParameter("fechaFin")));
                    postulacion.setFecha_hora_registro(LocalDateTime.now());
                    postulacion.setCantidad_rechazos_consecutivos(0);

                    Estado estado = new Estado();
                    estado.setId_estado(1); // Estado inicial: Pendiente
                    postulacion.setEstado(estado);
                    postulacion.setLlamo_al_postulante(false);
                    postulacion.setFecha_visita(null);

                    hogarTemporalDao.guardarNuevaPostulacion(postulacion);
                    hogarTemporalDao.actualizarUltimaPostulacionUsuario(postulacion.getUsuario_final());

                    int idUltimaPostulacion = hogarTemporalDao.obtenerIdUltimaPostulacionUsuario(idUsuario);

                    for (Part filePart : request.getParts()) {
                        if (filePart.getName().equals("archivo") && filePart.getSize() > 0) {
                            String fileName = filePart.getSubmittedFileName();
                            byte[] fileContent = filePart.getInputStream().readAllBytes();
                            hogarTemporalDao.guardarFotoPostulacion(idUltimaPostulacion, fileContent, fileName);
                        }
                    }
                    String correo = usuario.getCorreo_electronico();
                    String contrasenia = usuario.getContrasenia();
                    LoginDao loginDao = new LoginDao();
                    usuario = loginDao.obtenerUsuario(correo, contrasenia);
                    request.getSession().setAttribute("usuario", usuario);

                    response.sendRedirect("TemporalUsuarioServlet");
                } catch (RuntimeException e) {
                    response.sendRedirect("TemporalUsuarioServlet?action=postularForm");
                }

            } else if ("modificar".equals(action)) {
                try{
                    PostulacionHogarTemporal postulacion = new PostulacionHogarTemporal();
                    postulacion.setId_postulacion_hogar_temporal(Integer.parseInt(request.getParameter("id_postulacion")));
                    postulacion.setUsuario_final(usuario);
                    postulacion.setEdad_usuario(request.getParameter("edad"));
                    postulacion.setGenero_usuario(request.getParameter("genero"));
                    postulacion.setCelular_usuario(request.getParameter("telefono"));
                    postulacion.setCantidad_cuartos(request.getParameter("cuartos"));
                    postulacion.setMetraje_vivienda(request.getParameter("metraje"));
                    postulacion.setTiene_mascotas("1".equals(request.getParameter("tieneMascota")));
                    postulacion.setTipo_mascotas(request.getParameter("tipoMascotas"));
                    postulacion.setTiene_hijos("1".equals(request.getParameter("tieneHijo")));
                    postulacion.setTiene_dependientes("2".equals(request.getParameter("viveSolo")));
                    postulacion.setForma_trabajo("1".equals(request.getParameter("trabajaRemoto")) ? "Remoto" : "Presencial");
                    postulacion.setNombre_persona_referencia(request.getParameter("nombreRef"));
                    postulacion.setCelular_persona_referencia(request.getParameter("numeroRef"));
                    postulacion.setFecha_inicio_temporal(LocalDate.parse(request.getParameter("fechaInicio")));
                    postulacion.setFecha_fin_temporal(LocalDate.parse(request.getParameter("fechaFin")));
                    postulacion.setFecha_hora_registro(LocalDateTime.now());
                    postulacion.setCantidad_rechazos_consecutivos(0);

                    Estado estado = new Estado();
                    estado.setId_estado(1); // Estado inicial: Pendiente
                    postulacion.setEstado(estado);
                    postulacion.setLlamo_al_postulante(false);
                    postulacion.setFecha_visita(null);

                    hogarTemporalDao.modificarPostulacion(postulacion);

                    String correo = usuario.getCorreo_electronico();
                    String contrasenia = usuario.getContrasenia();
                    LoginDao loginDao = new LoginDao();
                    usuario = loginDao.obtenerUsuario(correo, contrasenia);
                    request.getSession().setAttribute("usuario", usuario);

                    response.sendRedirect("TemporalUsuarioServlet");
                } catch (RuntimeException e) {
                    response.sendRedirect("TemporalUsuarioServlet?action=modificarForm");
                }
            }
    }
}