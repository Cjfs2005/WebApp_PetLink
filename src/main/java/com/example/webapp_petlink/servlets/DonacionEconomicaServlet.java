package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.SolicitudDonacionEconomica;
import com.example.webapp_petlink.daos.DonacionEconomicaDao;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ListaSolicitudesDonacionEconomica")
public class DonacionEconomicaServlet extends HttpServlet {
    private DonacionEconomicaDao donacionEconomicaDao = new DonacionEconomicaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Usamos un ID de albergue de prueba (idUsuarioAlbergue = 6) para las solicitudes activas
        int idUsuarioAlbergue = 6;

        // Obtenemos las solicitudes activas desde el DAO
        List<SolicitudDonacionEconomica> solicitudes = donacionEconomicaDao.obtenerSolicitudesActivas(idUsuarioAlbergue);

        // Pasamos las solicitudes a la JSP para mostrarlas
        request.setAttribute("solicitudes", solicitudes);

        // Redirigimos a la página que mostrará las solicitudes
        RequestDispatcher dispatcher = request.getRequestDispatcher("/DonacionEconomica.jsp");
        dispatcher.forward(request, response);
    }
}
