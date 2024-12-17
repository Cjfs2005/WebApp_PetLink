package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.daos.DenunciaMaltratoAnimalDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@WebServlet(name = "DenunciaServlet", urlPatterns = {"/DenunciaServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
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
                case "list":
                    listarDenuncias(request, response);
                    break;
                case "denunciaForm":
                    request.getRequestDispatcher("usuarioFinal/denuncia_creacion_usuario.jsp").forward(request, response);
                    break;
                case "editarForm":
                    DenunciaMaltratoAnimal denuncia = denunciaDAO.obtenerDenunciaPorId(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("denuncia", denuncia);
                    request.getRequestDispatcher("usuarioFinal/denuncia_modificacion_usuario.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            response.sendRedirect("DenunciaServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "insert";
        switch (action) {
                case "insert":
                    try {
                        insertarDenuncia(request, response);
                    } catch (Exception e) {
                        response.sendRedirect("DenunciaServlet?action=denunciaForm");
                    }
                    break;
                case "delete":
                    try {
                        eliminarDenuncia(request, response);
                    } catch (Exception e) {
                        //response.sendRedirect("DenunciaServlet");
                        // Imprimir el error en los logs del servidor
                        e.printStackTrace();

                        // Enviar un mensaje de error al cliente
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error en el procesamiento: " + e.getMessage());
                    }
                    break;
                case "update":
                    try {
                        actualizarDenuncia(request, response);
                    } catch (Exception e) {
                        response.sendRedirect("DenunciaServlet?action=editarForm");
                    }
                    break;
        }
    }

    // Listar todas las denuncias
    private void listarDenuncias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        int idUsuario = usuario.getId_usuario();
        List<DenunciaMaltratoAnimal> denuncias = denunciaDAO.obtenerTodasLasDenuncias(idUsuario);
        request.setAttribute("denuncias", denuncias);
        request.getRequestDispatcher("usuarioFinal/denuncia_usuario.jsp").forward(request, response);
    }

    // Mostrar una denuncia específica
    private void mostrarDenuncia(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        DenunciaMaltratoAnimal denuncia = denunciaDAO.obtenerDenunciaPorId(id);
        if (denuncia != null) {
            request.setAttribute("denuncia", denuncia);
            request.getRequestDispatcher("usuarioFinal/denuncia_usuario.jsp").forward(request, response);
        } else {
            response.sendRedirect("DenunciaServlet?action=list");
        }
    }

    // Insertar una nueva denuncia
    private void insertarDenuncia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        DenunciaMaltratoAnimal denuncia = new DenunciaMaltratoAnimal();
        denuncia.setTamanio(request.getParameter("tamanio"));
        denuncia.setRaza(request.getParameter("raza"));
        denuncia.setDescripcionMaltrato(request.getParameter("descripcionMaltrato"));
        denuncia.setNombreMaltratador(request.getParameter("nombreMaltratador"));
        denuncia.setDireccionMaltrato(request.getParameter("direccionMaltrato"));
        denuncia.setEsDenunciaActiva(true);
        denuncia.setFechaHoraRegistro(LocalDateTime.now());

        Usuario usuarioFinal = new Usuario();
        usuarioFinal.setId_usuario(usuario.getId_usuario());
        denuncia.setUsuarioFinal(usuarioFinal);

        Estado estado = new Estado();
        estado.setId_estado(2);
        denuncia.setEstado(estado);

        String fileName = null;
        byte[] fileContent = null;
        try {
            for (Part filePart : request.getParts()) {
                if (filePart.getName().equals("fotoAnimal") && filePart.getSize() > 0) {
                    fileName = filePart.getSubmittedFileName();
                    fileContent = filePart.getInputStream().readAllBytes();
                }
            }
        } catch (Exception e) {
            response.sendRedirect("DenunciaServlet?action=denunciaForm");
        }

        denuncia.setTieneDenunciaPolicial("true".equals(request.getParameter("tieneDenunciaPolicial")));

        denuncia.setNombreFotoAnimal(fileName);
        denuncia.setFotoAnimal(fileContent);
        denunciaDAO.insertarDenuncia(denuncia);
        response.sendRedirect("DenunciaServlet?action=list");
    }

    // Actualizar una denuncia existente
    private void actualizarDenuncia(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        DenunciaMaltratoAnimal denuncia = new DenunciaMaltratoAnimal();
        denuncia.setIdDenunciaMaltratoAnimal(id);
        denuncia.setTamanio(request.getParameter("tamanio"));
        denuncia.setRaza(request.getParameter("raza"));
        denuncia.setDescripcionMaltrato(request.getParameter("descripcionMaltrato"));
        String fileName = null;
        byte[] fileContent = null;
        try {
            for (Part filePart : request.getParts()) {
                if (filePart.getName().equals("fotoAnimal") && filePart.getSize() > 0) {
                    fileName = filePart.getSubmittedFileName();
                    fileContent = filePart.getInputStream().readAllBytes();
                }
            }
        } catch (Exception e) {
            response.sendRedirect("DenunciaServlet?action=denunciaForm");
        }

        denuncia.setTieneDenunciaPolicial("true".equals(request.getParameter("tieneDenunciaPolicial")));

        denuncia.setNombreFotoAnimal(fileName);
        denuncia.setFotoAnimal(fileContent);

        denunciaDAO.actualizarDenuncia(denuncia);

        response.sendRedirect("DenunciaServlet");
    }

    // Eliminar una denuncia
    private void eliminarDenuncia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        denunciaDAO.eliminarDenuncia(id);
        response.sendRedirect("DenunciaServlet");
    }
}
