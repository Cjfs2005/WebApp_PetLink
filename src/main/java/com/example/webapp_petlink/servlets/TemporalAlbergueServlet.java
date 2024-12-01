package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.HogarTemporalDao;

import com.example.webapp_petlink.daos.LoginDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
@WebServlet(name = "TemporalAlbergueServlet", value = "/TemporalAlbergueServlet")
public class TemporalAlbergueServlet extends HttpServlet {
   private final HogarTemporalDao hogarTemporalDao = new HogarTemporalDao();

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");
      RequestDispatcher view;
      Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
      /* Validar el parámetro id_usuario
      String idUsuarioParam = request.getParameter("id_usuario");
      if (idUsuarioParam == null || idUsuarioParam.isEmpty()) {
         response.sendRedirect("error.jsp");
         return;
      }

      int idUsuario;
      try {
         idUsuario = Integer.parseInt(idUsuarioParam);
      } catch (NumberFormatException e) {
         response.sendRedirect("error.jsp");
         return;
      }

      Usuario usuario = hogarTemporalDao.obtenerUsuarioPorId(idUsuario);*/

      switch (action) {
         case "listar":
            if (usuario != null) {
               ArrayList<PostulacionHogarTemporal> postulacionesDisponibles = hogarTemporalDao.obtenerHogaresTemporalesDisponibles();
               request.setAttribute("usuario", usuario);
               request.setAttribute("postulacionesDisponibles", postulacionesDisponibles);

               view = request.getRequestDispatcher("albergue/hogar_temporal.jsp");
               view.forward(request, response);
            } else {
               response.sendRedirect("error.jsp");
            }
            break;

         case "ver":
            String idPostulacionParam = request.getParameter("id_postulacion");
            if (idPostulacionParam == null || idPostulacionParam.isEmpty()) {
               response.sendRedirect("error.jsp");
               return;
            }

            int idPostulacion;
            try {
               idPostulacion = Integer.parseInt(idPostulacionParam);
            } catch (NumberFormatException e) {
               response.sendRedirect("error.jsp");
               return;
            }

            PostulacionHogarTemporal postulacion = hogarTemporalDao.obtenerPostulacionPorId(idPostulacion);

            if (postulacion != null) {
               ArrayList<byte[]> fotos = hogarTemporalDao.obtenerFotosPorPostulacion(idPostulacion);
               request.setAttribute("usuario", usuario);
               request.setAttribute("postulacion", postulacion);
               request.setAttribute("fotos", fotos);

               view = request.getRequestDispatcher("albergue/hogar_temporal_info1.jsp");
               view.forward(request, response);
            } else {
               response.sendRedirect("error.jsp");
            }
            break;

         case "solicitar":
            idPostulacionParam = request.getParameter("id_postulacion");
            if (idPostulacionParam == null || idPostulacionParam.isEmpty()) {
               response.sendRedirect("error.jsp");
               return;
            }
            try {
               idPostulacion = Integer.parseInt(idPostulacionParam);
            } catch (NumberFormatException e) {
               response.sendRedirect("error.jsp");
               return;
            }

            postulacion = hogarTemporalDao.obtenerPostulacionPorId(idPostulacion);

            if (postulacion != null) {
               request.setAttribute("usuario", usuario);
               request.setAttribute("postulacion", postulacion);

               view = request.getRequestDispatcher("albergue/solic_hogar_temporal.jsp");
               view.forward(request, response);
            } else {
               response.sendRedirect("error.jsp");
            }
            break;
         case "historial":
            if (usuario != null) {
               ArrayList<SolicitudHogarTemporal> solicitudes = hogarTemporalDao.obtenerSolicitudesRealizadas(usuario.getId_usuario());
               request.setAttribute("usuario", usuario);
               request.setAttribute("solicitudes", solicitudes);

               view = request.getRequestDispatcher("albergue/historialHogares.jsp");
               view.forward(request, response);
            } else {
               response.sendRedirect("error.jsp");
            }
            break;
      }
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String action = request.getParameter("action");

      Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

      switch (action) {
         case "registrarSolicitud":
            int idPostulacion = Integer.parseInt(request.getParameter("id_postulacion"));
            String nombreMascota = request.getParameter("nombreMascota");
            String descripcionMascota = request.getParameter("descripcionMascota");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            Part fotoMascotaPart = request.getPart("fotoMascota");

            byte[] fotoMascota = null;
            if (fotoMascotaPart != null && fotoMascotaPart.getSize() > 0) {
               fotoMascota = fotoMascotaPart.getInputStream().readAllBytes();
            }

            SolicitudHogarTemporal solicitud = new SolicitudHogarTemporal();
            solicitud.setNombreMascota(nombreMascota);
            solicitud.setDescripcionMascota(descripcionMascota);
            solicitud.setFechaInicio(java.sql.Date.valueOf(fechaInicio));
            solicitud.setFechaFin(java.sql.Date.valueOf(fechaFin));
            solicitud.setFotoMascota(fotoMascota);
            solicitud.setPostulacionHogarTemporal(hogarTemporalDao.obtenerPostulacionPorId(idPostulacion));
            solicitud.setUsuarioAlbergue(usuario);
            solicitud.setEstado(hogarTemporalDao.obtenerEstadoPorNombre("Pendiente"));

            hogarTemporalDao.registrarSolicitud(solicitud);

            response.sendRedirect(request.getContextPath() + "/TemporalAlbergueServlet?action=listar");
            break;

         default:
            response.sendRedirect("error.jsp");
            break;
      }
   }
}