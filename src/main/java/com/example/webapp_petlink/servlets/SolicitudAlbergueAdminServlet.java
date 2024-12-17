package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.ListasAdminDao;
import com.example.webapp_petlink.daos.LoginDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name="SolicitudAlbergueAdminServlet", value="/SolicitudAlbergueAdminServlet")
public class SolicitudAlbergueAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listar" : request.getParameter("action");

        ListasAdminDao dao = new ListasAdminDao();

        HttpSession session = request.getSession();

        Usuario administrador = (Usuario) session.getAttribute("usuario");

        if (administrador == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        session.setAttribute("datosUsuario", administrador);

        RequestDispatcher dispatcher;

        try {
            switch(action){
                case "listar":
                    ArrayList<Usuario> albergues = dao.listasAlbergueSinPrimerRegistro();
                    request.setAttribute("albergues", albergues);
                    dispatcher = request.getRequestDispatcher("administrador/solicitud_albergue.jsp");
                    dispatcher.forward(request, response);
                    break;

                case "ver":
                    int idAlbergue;
                    try {
                        idAlbergue = Integer.parseInt(request.getParameter("id"));
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath()+"/SolicitudAdminServlet?action=lista");
                        return;
                    }
                    Usuario albergue = dao.obtenerAlberguePorId(idAlbergue);
                    System.out.println("IMPRIMIENDO DATOS");
                    System.out.println(albergue.getNombre_albergue());
                    System.out.println(albergue.getUrl_instagram());
                    request.setAttribute("albergue", albergue);
                    dispatcher = request.getRequestDispatcher("administrador/ver_detalles.jsp");
                    dispatcher.forward(request, response);
                    break;

                case "aceptar":
                    System.out.println("SE ha aceptado la solicitud del albergue");
                    try {
                        String idalber = request.getParameter("id");
                        int AlbergueID = Integer.parseInt(idalber);
                        System.out.println("------Tenemos que aceptar al ID "+AlbergueID);
                        if (dao.obtenerAlberguePorId(AlbergueID) != null) {
                            // Actualizamos la fecha de creación por la fecha de aceptación:
                            Date fecha = new Date();
                            LocalDateTime fechaLocalDateTime = fecha.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();

                            // Generamos las contraseñas
                            String contrasenia = dao.generarContrasenia(8);
                            LoginDao logindao = new LoginDao();
                            String contrasenia_hashed = logindao.hashString(contrasenia, "SHA-256");
                            Usuario albergue1 = new Usuario();
                            albergue1.setId_usuario(AlbergueID);
                            albergue1.setContrasenia(contrasenia);
                            albergue1.setContrasenia_hashed(contrasenia_hashed);
                            albergue1.setFecha_hora_creacion(fechaLocalDateTime);
                            dao.aceptarAlbergue(albergue1);
                        }

                        response.sendRedirect(request.getContextPath() + "/SolicitudAlbergueAdminServlet?action=listar&success=albergueAceptado");
                        break;
                    }
                    catch (Exception e) {
                        response.sendRedirect(request.getContextPath()+"/SolicitudAdminServlet?action=listar&error=albergueNoAceptado");
                    }

                case "rechazar":
                    System.out.println("Se ha rechazado la solicitud del albergue");
                    try {
                        String idAlb = request.getParameter("id");
                        int AlbergueID = Integer.parseInt(idAlb);
                        dao.rechazarAlbergue(AlbergueID);
                        response.sendRedirect(request.getContextPath() + "/SolicitudAlbergueAdminServlet?action=listar&success=albergueRechazado");
                        break;
                    }
                    catch (Exception e){
                        response.sendRedirect(request.getContextPath() + "/SolicitudAlbergueAdminServlet?action=listar&error=albergueNoRechazado");
                    }

                default:
                    response.sendRedirect(request.getContextPath() + "/SolicitudAlbergueAdminServlet?action=listar&error=1");

            }
        }
        catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/SolicitudAlbergueAdminServlet?action=listar&error=1");
        }
    }
}
