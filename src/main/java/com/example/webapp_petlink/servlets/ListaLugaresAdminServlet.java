package com.example.webapp_petlink.servlets;


import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.LugarEvento;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.ListasAdminDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name="ListaLugaresAdminServlet", value="/ListaLugaresAdminServlet")
public class ListaLugaresAdminServlet extends HttpServlet {

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
            switch (action) {
                case "listar":
                    ArrayList<LugarEvento> lugares = dao.listarLugares();
                    if (lugares == null){
                        System.out.println("Todo esta fallando");
                    }
                    request.setAttribute("lugares", lugares);
                    dispatcher = request.getRequestDispatcher("administrador/lugares_eventos.jsp");
                    dispatcher.forward(request, response);
                    break;

                case "crear":
                    //Insertar logica
                    ArrayList<Distrito> distritos = dao.obtenerDistritos();
                    request.setAttribute("distritos", distritos);
                    dispatcher = request.getRequestDispatcher("administrador/crear_evento.jsp");
                    dispatcher.forward(request, response);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&error=1");
            }
        }
        catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&error=1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listaCoord" : request.getParameter("action");

        ListasAdminDao dao = new ListasAdminDao();

        HttpSession session = request.getSession();

        Usuario administrador = (Usuario) session.getAttribute("usuario");

        if (administrador == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        session.setAttribute("datosUsuario", administrador);


        if (action.equals("guardar")) {
            try {
                String nombre = request.getParameter("nombre-lugar");
                String direccion = request.getParameter("direccion");
                String aforo = request.getParameter("aforo");
                String distrito = request.getParameter("distrito");

                if (nombre == null || direccion == null || aforo == null || distrito == null) {
                    response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&error=faltanCampos");
                    return;
                }

                int cantAforo = 0;
                try {
                    cantAforo = Integer.parseInt(aforo);
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&error=idFaltante");
                }

                int idDistrito;
                try {
                    idDistrito = Integer.parseInt(distrito);
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&error=idFaltante");
                    return;
                }

                // Creamos el objeto lugar
                LugarEvento lugar = new LugarEvento();
                lugar.setNombre_lugar_evento(nombre);
                lugar.setDireccion_lugar_evento(direccion);
                lugar.setAforo_maximo(cantAforo);

                Distrito distrito1 = new Distrito();
                distrito1.setId_distrito(idDistrito);

                lugar.setDistrito(distrito1);
                boolean guardado = dao.guardarLugar(lugar);

                if (guardado) {
                    // 5. Redirigir a la lista de eventos con un mensaje de éxito
                    response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&success=lugarCreado");
                } else {
                    // Manejar el error si no se pudo guardar
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=crearCoord&error=lugarNoCreado");
                }
                return;
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/ListaLugaresAdminServlet?action=listar&error=1");
            }
        }
    }
}

