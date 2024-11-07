package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.daos.DenunciaMaltratoAnimalDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "DenunciaServlet", urlPatterns = {"/denuncias"})
public class DenunciaServlet extends HttpServlet {

    private DenunciaMaltratoAnimalDAO denunciaDAO;

    @Override
    public void init() throws ServletException {
        denunciaDAO = new DenunciaMaltratoAnimalDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "view":
                    mostrarDenuncia(request, response);
                    break;
                case "delete":
                    eliminarDenuncia(request, response);
                    break;
                default:
                    listarDenuncias(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error handling GET request", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "insert";

        try {
            switch (action) {
                case "insert":
                    insertarDenuncia(request, response);
                    break;
                case "update":
                    actualizarDenuncia(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error handling POST request", e);
        }
    }

    // Listar todas las denuncias
    private void listarDenuncias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DenunciaMaltratoAnimal> denuncias = denunciaDAO.obtenerTodasLasDenuncias();
        request.setAttribute("denuncias", denuncias);
        request.getRequestDispatcher("denuncias.jsp").forward(request, response);
    }

    // Mostrar una denuncia específica
    private void mostrarDenuncia(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        DenunciaMaltratoAnimal denuncia = denunciaDAO.obtenerDenunciaPorId(id);
        if (denuncia != null) {
            request.setAttribute("denuncia", denuncia);
            request.getRequestDispatcher("ver_denuncia.jsp").forward(request, response);
        } else {
            response.sendRedirect("denuncias?action=list");
        }
    }

    // Insertar una nueva denuncia
    private void insertarDenuncia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DenunciaMaltratoAnimal denuncia = new DenunciaMaltratoAnimal();
        denuncia.setNombreFotoAnimal(request.getParameter("nombreFotoAnimal"));
        denuncia.setTamanio(request.getParameter("tamanio"));
        denuncia.setRaza(request.getParameter("raza"));
        denuncia.setDescripcionMaltrato(request.getParameter("descripcionMaltrato"));
        denuncia.setNombreMaltratador(request.getParameter("nombreMaltratador"));
        denuncia.setDireccionMaltrato(request.getParameter("direccionMaltrato"));
        denuncia.setEsDenunciaActiva(true);
        denuncia.setFechaHoraRegistro(new Date());

        Usuario usuarioFinal = new Usuario();
        usuarioFinal.setId_usuario(Integer.parseInt(request.getParameter("usuarioFinalId")));
        denuncia.setUsuarioFinal(usuarioFinal);

        Estado estado = new Estado();
        estado.setId_estado(Integer.parseInt(request.getParameter("estadoId")));
        denuncia.setEstado(estado);

        denunciaDAO.insertarDenuncia(denuncia);
        response.sendRedirect("denuncias?action=list");
    }

    // Actualizar una denuncia existente
    private void actualizarDenuncia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        DenunciaMaltratoAnimal denuncia = denunciaDAO.obtenerDenunciaPorId(id);

        if (denuncia != null) {
            denuncia.setNombreFotoAnimal(request.getParameter("nombreFotoAnimal"));
            denuncia.setTamanio(request.getParameter("tamanio"));
            denuncia.setRaza(request.getParameter("raza"));
            denuncia.setDescripcionMaltrato(request.getParameter("descripcionMaltrato"));
            denuncia.setNombreMaltratador(request.getParameter("nombreMaltratador"));
            denuncia.setDireccionMaltrato(request.getParameter("direccionMaltrato"));
            denuncia.setEsDenunciaActiva(Boolean.parseBoolean(request.getParameter("esDenunciaActiva")));

            denunciaDAO.actualizarDenuncia(denuncia);
        }

        response.sendRedirect("denuncias?action=list");
    }

    // Eliminar una denuncia
    private void eliminarDenuncia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        denunciaDAO.eliminarDenuncia(id);
        response.sendRedirect("denuncias?action=list");
    }
}
