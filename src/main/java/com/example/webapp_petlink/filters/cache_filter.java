package com.example.webapp_petlink.filters;

import com.example.webapp_petlink.beans.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "cache_filter",
        servletNames = {
                "AdopcionesAlbergueServlet",
                "AdopcionesUsuarioFinalServlet",
                "AlbergueDenunciasServlet",
                "DenunciaServlet",
                "DonacionEconomicaServlet",
                "DonacionProductosServlet",
                "EventoAlbergueServlet",
                "EventoUsuarioServlet",
                "GestionCoordinadorServlet",
                "HelloServlet",
                "ListasAdminServlet",
                "ListaAlbergueAdminServlet",
                "ListaAlberguesServlet",
                "ListaLugaresAdminServlet",
                "ListaUsuariosAdminServlet",
                "MascotaPerdidaDuenoServlet",
                "MascotaPerdidaPublicacionServlet",
                "PerfilAlbergueServlet",
                "PerfilUsuarioServlet",
                "PublicacionMascotaPerdidaDuenoServlet",
                "PublicacionMascotaPerdidaUsuarioServlet",
                "SolicitudAlbergueAdminServlet",
                "SolicitudesMascotasPerdidasDuenoServlet",
                "SolicitudesMascotasPerdidasServlet",
                "TemporalAlbergueServlet",
                "TemporalUsuarioServlet",
                "CoordinadorZonalHogarTemporalServlet",
                "BaneosCoordi1Servlet",
                "BaneosCoordi2Servlet",
                "Dashboard",
                "DonacionEconomicaUsuarioServlet",
                "DonacionProductosUsuarioServlet",
                "HistorialDonacionesUsuarioServlet",
                "HistorialEconomicaUsuarioServlet"
        }
)
public class cache_filter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath());
        } else {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0);
            if (usuario.getEs_usuario_activo() &&
                    !usuario.getEs_primera_contrasenia_temporal() &&
                    usuario.getRol().getId_rol()==2 &&
                    !usuario.getTiene_registro_completo()) {
                response.sendRedirect(request.getContextPath() + "/SegundoRegistroAlbergueServlet");
            } else {
                chain.doFilter(request, response);
            }
        }
    }
}
