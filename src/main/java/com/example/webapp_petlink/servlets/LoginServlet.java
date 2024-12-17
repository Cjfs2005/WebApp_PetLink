package com.example.webapp_petlink.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.LoginDao;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    private LoginDao loginDao = new LoginDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action_parameter = request.getParameter("action");

        String action = action_parameter == null ? "loginForm" : action_parameter;

        switch (action){

            case "login":

                String correo = request.getParameter("email");
                String contrasenia = request.getParameter("password");

                Usuario usuario = loginDao.obtenerUsuario(correo, contrasenia);

                if (usuario != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario);
                    session.setMaxInactiveInterval(60*60); //60 minutos de inactividad maxima

                    String rol = usuario.getRol().getNombre_rol();

                    System.out.println("Ingreso satisfactorio");

                    switch (rol) {
                        case "Administrador":
                            response.sendRedirect(request.getContextPath() + "/ListasAdminServlet");
                            break;
                        case "Albergue":
                            response.sendRedirect(request.getContextPath() + "/EventoAlbergueServlet");
                            break;
                        case "Coordinador de Zona":
                            response.sendRedirect(request.getContextPath() + "/GestionCoordinadorServlet");
                            break;
                        case "Usuario Final":
                            response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/index.jsp");
                            break;
                    }
                } else {
                    // En caso de que el inicio de sesión falle
                    request.setAttribute("loginError", "Correo o contraseña incorrectos.");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
                break;
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action_parameter = request.getParameter("action");

        String action = action_parameter == null ? "loginForm" : action_parameter;
        switch(action){
            case "logout":
                HttpSession session = request.getSession();
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
            case "loginForm":
                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    response.sendRedirect(request.getContextPath());
                    String rol = usuario.getRol().getNombre_rol();
                    switch (rol) {
                        case "Administrador":
                            response.sendRedirect(request.getContextPath() + "/administrador/eventos.jsp");
                            break;
                        case "Albergue":
                            response.sendRedirect(request.getContextPath() + "/AdopcionesAlbergueServlet");
                            break;
                        case "Coordinador de Zona":
                            response.sendRedirect(request.getContextPath() + "/coordinadorZonal/eventos.jsp");
                            break;
                        case "Usuario Final":
                            response.sendRedirect(request.getContextPath() + "/EventoUsuarioServlet");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/index.jsp");
                            break;
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
                break;
        }
    }
}
