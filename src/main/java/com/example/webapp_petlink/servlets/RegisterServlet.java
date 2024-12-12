package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Correo;
import com.example.webapp_petlink.daos.RegisterDao;
import jakarta.mail.MessagingException;
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

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet  {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action_parameter = request.getParameter("action");
        String action = action_parameter == null ? "loginForm" : action_parameter;
        RegisterDao registerDao = new RegisterDao();
        RequestDispatcher view;

        switch (action) {

            case "registroUsuario":

                //Validar que los datos ingresados por el usuario cumplen con los tipos de datos

                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String dni = request.getParameter("dni");
                String direccion = request.getParameter("direccion");
                String email = request.getParameter("email");

                try {

                    int distrito = Integer.parseInt(request.getParameter("distrito"));

                    if (nombre == null || nombre.length() > 45) {

                        // Manejar error: El nombre es demasiado largo o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                    }
                    else if (apellido == null || nombre.length() > 45) {

                        // Manejar error: El apellido es demasiado largo o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                    }
                    else if (dni == null || dni.length() != 8) {

                        // Manejar error: El dni no cumple con la longitud adecuada o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                    }
                    else if (distrito < 0 || distrito >42) {
                        // Manejar error: El distrito no es un valor valido
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                    }
                    else if (direccion == null || direccion.length() > 100) {

                        // Manejar error: La direccion no cumple con la longitud adecuada o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                    }
                    else if (email == null || email.length() > 100) {

                        // Manejar error: El email no cumple con la longitud adecuada o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                    }
                }
                catch (Exception e) {
                    request.setAttribute("estado", "errorFormato");
                    view = request.getRequestDispatcher("registro_usuario.jsp");
                    view.forward(request, response);
                }

                int distrito = Integer.parseInt(request.getParameter("distrito"));

                //Valida que no existe usuario activo que coincida con el correo electronico

                boolean existe = registerDao.existeUsuario(dni,email);

                if(existe) {
                    request.setAttribute("estado", "errorRegistroExistente");
                    view = request.getRequestDispatcher("registro_usuario.jsp");
                    view.forward(request, response);
                }
                else{
                    //Crear el usuario en la BD con la contrasenia temporal

                }

                //Redirige al formulario de registro de usuarios y muestra el modal de que recibira la aceptacion pronto

                request.setAttribute("estado", "satisfactorio");
                view = request.getRequestDispatcher("registro_usuario.jsp");
                view.forward(request, response);

                break;
            case "registroAlbergue":

                //Redirige al formulario de registro de albergues y muestra el modal de que recibira la aceptacion pronto


                break;
            default:
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action_parameter = request.getParameter("action");
        String action = action_parameter == null ? "loginForm" : action_parameter;

        switch (action) {

            case "usuarioFinal":

                //Redirige al formulario de registro de usuarios

                response.sendRedirect("registro_usuario.jsp");
                break;
            case "albergue":

                //Redirige al formulario de registro de albergues

                response.sendRedirect("registro_albergue.jsp");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
        }

    }

}

/* Codigo de prueba del envio de correo

                Correo correo = new Correo();

                try {
                    correo.sendEmail("a20223209@pucp.edu.pe","Hola causa", "Lokzo");
                    System.out.println("Correo enviado");
                }
                catch (MessagingException e) {
                    System.out.println(e);
                }

*/