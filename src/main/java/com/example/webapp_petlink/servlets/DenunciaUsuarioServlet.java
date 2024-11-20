package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DAODenunciaUsuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DenunciaUsuarioServlet", value = "/DenunciaUsuarioServlet")
public class DenunciaUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DAODenunciaUsuario denunciaDao = new DAODenunciaUsuario();
        RequestDispatcher dispatcher;

        if (accion == null || accion.isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarDenuncias(request, response, denunciaDao);
                break;

            case "ver":
                int idDenuncia = Integer.parseInt(request.getParameter("id"));
                DenunciaMaltratoAnimal denuncia = denunciaDao.obtenerDenunciaPorId(idDenuncia);
                request.setAttribute("denuncia", denuncia);
                dispatcher = request.getRequestDispatcher("usuarioFinal/ver_denuncia.jsp");
                dispatcher.forward(request, response);
                break;

            case "editar":
                idDenuncia = Integer.parseInt(request.getParameter("id"));
                denuncia = denunciaDao.obtenerDenunciaPorId(idDenuncia);
                request.setAttribute("denuncia", denuncia);
                dispatcher = request.getRequestDispatcher("usuarioFinal/editar_denuncia.jsp");
                dispatcher.forward(request, response);
                break;

            default:
                response.sendRedirect("error.jsp");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        DAODenunciaUsuario denunciaDao = new DAODenunciaUsuario();

        switch (accion) {
            case "registrar":
                registrarDenuncia(request, response, denunciaDao);
                break;

            case "actualizar":
                actualizarDenuncia(request, response, denunciaDao);
                break;

            case "eliminar":
                eliminarDenuncia(request, response, denunciaDao);
                break;

            default:
                response.sendRedirect("error.jsp");
                break;
        }
    }

    private void listarDenuncias(HttpServletRequest request, HttpServletResponse response, DAODenunciaUsuario denunciaDao) throws ServletException, IOException {
        int idUsuario = 1; // Este ID debería ser dinámico según la sesión del usuario.
        List<DenunciaMaltratoAnimal> denuncias = denunciaDao.obtenerDenunciasPorUsuario(idUsuario);
        request.setAttribute("denuncias", denuncias);
        RequestDispatcher dispatcher = request.getRequestDispatcher("usuarioFinal/denuncia_usuario.jsp");
        dispatcher.forward(request, response);
    }

    private void registrarDenuncia(HttpServletRequest request, HttpServletResponse response, DAODenunciaUsuario denunciaDao) throws IOException, ServletException {
        DenunciaMaltratoAnimal nuevaDenuncia = new DenunciaMaltratoAnimal();

        // Obtener los campos del formulario
        nuevaDenuncia.setTamanio(request.getParameter("tamanio"));
        nuevaDenuncia.setRaza(request.getParameter("raza"));
        nuevaDenuncia.setDescripcionMaltrato(request.getParameter("descripcionMaltrato"));
        nuevaDenuncia.setNombreMaltratador(request.getParameter("nombreMaltratador"));
        nuevaDenuncia.setDireccionMaltrato(request.getParameter("direccionMaltrato"));
        nuevaDenuncia.setTieneDenunciaPolicial(Boolean.parseBoolean(request.getParameter("tieneDenunciaPolicial")));

        // Manejar la imagen (fotoAnimal)
        Part filePart = request.getPart("fotoAnimal"); // Archivo enviado desde el formulario
        if (filePart != null && filePart.getSize() > 0) {
            byte[] foto = filePart.getInputStream().readAllBytes();
            nuevaDenuncia.setFotoAnimal(foto);
            nuevaDenuncia.setNombreFotoAnimal(filePart.getSubmittedFileName());
        }

        // Asignar usuario desde la sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario"); // Asegúrate de que el usuario esté guardado en la sesión
        if (usuario != null) {
            nuevaDenuncia.setUsuarioFinal(usuario);
        } else {
            response.sendRedirect("login.jsp"); // Redirigir a inicio de sesión si no hay usuario
            return;
        }

        // Asignar un estado inicial
        Estado estadoInicial = new Estado();
        estadoInicial.setId_estado(1); // Ejemplo: ID del estado "Pendiente"
        nuevaDenuncia.setEstado(estadoInicial);

        // Registrar la denuncia
        if (denunciaDao.registrarDenuncia(nuevaDenuncia)) {
            response.sendRedirect("DenunciaUsuarioServlet?accion=listar");
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    private void actualizarDenuncia(HttpServletRequest request, HttpServletResponse response, DAODenunciaUsuario denunciaDao) throws IOException {
        int idDenuncia = Integer.parseInt(request.getParameter("id"));
        DenunciaMaltratoAnimal denunciaActualizada = new DenunciaMaltratoAnimal();
        denunciaActualizada.setIdDenunciaMaltratoAnimal(idDenuncia);
        denunciaActualizada.setTamanio(request.getParameter("tamanio"));
        denunciaActualizada.setRaza(request.getParameter("raza"));
        denunciaActualizada.setDescripcionMaltrato(request.getParameter("descripcionMaltrato"));
        denunciaActualizada.setNombreMaltratador(request.getParameter("nombreMaltratador"));
        denunciaActualizada.setDireccionMaltrato(request.getParameter("direccionMaltrato"));
        denunciaActualizada.setTieneDenunciaPolicial(Boolean.parseBoolean(request.getParameter("tieneDenunciaPolicial")));

        // Asignar estado actualizado
        Estado estadoActualizado = new Estado();
        estadoActualizado.setId_estado(Integer.parseInt(request.getParameter("idEstado")));
        denunciaActualizada.setEstado(estadoActualizado);

        if (denunciaDao.actualizarDenuncia(denunciaActualizada)) {
            response.sendRedirect("DenunciaUsuarioServlet?accion=listar");
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    private void eliminarDenuncia(HttpServletRequest request, HttpServletResponse response, DAODenunciaUsuario denunciaDao) throws IOException {
        int idDenuncia = Integer.parseInt(request.getParameter("id"));
        if (denunciaDao.eliminarDenuncia(idDenuncia)) {
            response.sendRedirect("DenunciaUsuarioServlet?accion=listar");
        } else {
            response.sendRedirect("error.jsp");
        }
    }
}
