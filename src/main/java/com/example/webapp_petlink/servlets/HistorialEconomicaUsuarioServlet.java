package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.RegistroDonacionEconomica;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.HistorialEconomicaUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistorialEconomicaUsuarioServlet", value = "/HistorialEconomicaUsuarioServlet")
public class HistorialEconomicaUsuarioServlet extends HttpServlet {

    private final HistorialEconomicaUsuario dao = new HistorialEconomicaUsuario();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int idUsuarioFinal = usuario.getId_usuario();
        System.out.println("[DEBUG] ID del usuario final: " + idUsuarioFinal);

        List<RegistroDonacionEconomica> historial = dao.obtenerHistorialDonacionesEconomicas(idUsuarioFinal);

        if (historial == null || historial.isEmpty()) {
            System.out.println("[DEBUG] No hay historial de donaciones para el usuario con ID: " + idUsuarioFinal);
            request.setAttribute("mensaje", "No hay información disponible para mostrar.");
        } else {
            System.out.println("[DEBUG] Historial encontrado para el usuario con ID: " + idUsuarioFinal);
            for (RegistroDonacionEconomica registro : historial) {
                System.out.println("===== Registro de Donación =====");
                System.out.println("ID de Donación: " + registro.getIdRegistroDonacionEconomica());
                System.out.println("Monto Donado: " + registro.getMontoDonacion());
                System.out.println("Fecha de Registro: " + registro.getFechaHoraRegistro());
                System.out.println("Motivo: " + (registro.getSolicitudDonacionEconomica() != null ? registro.getSolicitudDonacionEconomica().getMotivo() : "No disponible"));
                System.out.println("Monto Solicitado: " + (registro.getSolicitudDonacionEconomica() != null ? registro.getSolicitudDonacionEconomica().getMonto_solicitado() : "No disponible"));
                System.out.println("Estado: " + (registro.getSolicitudDonacionEconomica() != null && registro.getSolicitudDonacionEconomica().getEstado() != null ? registro.getSolicitudDonacionEconomica().getEstado().getNombre_estado() : "No disponible"));
                System.out.println("Nombre del Albergue: " + (registro.getSolicitudDonacionEconomica() != null && registro.getSolicitudDonacionEconomica().getUsuario_albergue() != null ? registro.getSolicitudDonacionEconomica().getUsuario_albergue().getNombre_albergue() : "No disponible"));
                System.out.println("===============================");
            }
            request.setAttribute("historial", historial);
        }

        request.getRequestDispatcher("/usuarioFinal/Historial2.jsp").forward(request, response);
    }
}
