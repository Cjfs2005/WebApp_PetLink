package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Correo;
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

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action_parameter = request.getParameter("action");
        String action = action_parameter == null ? "loginForm" : action_parameter;

        switch (action) {

            case "usuarioFinal":

                //Codigo de prueba del envio de correo

                Correo correo = new Correo();

                try {
                    correo.sendEmail("a20220270@pucp.edu.pe","Hola causa", "Lokzo");
                    System.out.println("Correo enviado");
                }
                catch (MessagingException e) {
                    System.out.println(e);
                }

                break;
            case "albergue":
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
        }

    }

}
