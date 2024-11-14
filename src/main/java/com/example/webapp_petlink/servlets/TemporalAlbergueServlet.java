package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.HogarTemporalDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet(name = "TemporalAlbergueServlet", value = "/TemporalAlbergueServlet")
public class TemporalAlbergueServlet extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HogarTemporalDao hogarTemporalDao = new HogarTemporalDao();
      ArrayList<Usuario> hogaresDisponibles = hogarTemporalDao.obtenerHogaresTemporalesDisponibles();

      request.setAttribute("hogaresDisponibles", hogaresDisponibles);
      RequestDispatcher view = request.getRequestDispatcher("albergue/hogar_temporal.jsp");
      view.forward(request, response);
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
}