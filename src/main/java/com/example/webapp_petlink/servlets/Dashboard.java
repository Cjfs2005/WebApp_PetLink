package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name="dashboard", value="/dashboard")

public class Dashboard extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            HttpSession session = request.getSession();

            Usuario administrador = (Usuario) session.getAttribute("usuario");

            if (administrador == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            session.setAttribute("datosUsuario", administrador);

            RequestDispatcher dispatcher;

            // Si la sesión es válida, agregar datos al request
            request.setAttribute("title", "Dashboard");
            request.setAttribute("user", session.getAttribute("username")); // Usuario de la sesión
            request.setAttribute("role", session.getAttribute("role")); // Rol del usuario

            // Redirigir al JSP para mostrar el dashboard
            request.getRequestDispatcher("/administrador/Dashboard.jsp").forward(request, response);
        }
}



