package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.PuntoAcopio;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.DaoPerfilAlbergue;
import com.example.webapp_petlink.daos.ListaAlberguesDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet(name = "ListaAlberguesServlet", value = "/ListaAlberguesServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class ListaAlberguesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "listar"; // Acción por defecto
        }

        try {
            switch (accion) {
                case "listar":
                    listarAlbergues(request, response);
                    break;
                case "detalles":
                    detallesAlbergue(request, response);
                    break;
                case "donar":
                    donarAlbergue(request, response);
                    break;
                default:
                    listarAlbergues(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("EventoUsuarioServlet");
        }
    }

    private void listarAlbergues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListaAlberguesDao alberguesDao = new ListaAlberguesDao();
        // Obtener la lista de albergues desde el DAO
        List<Usuario> listaAlbergues = alberguesDao.listarAlbergues();

        // Guardar la lista en la sesión
        HttpSession session = request.getSession();
        session.setAttribute("listaAlbergues", listaAlbergues);


        //Se instancia el RequestDispatcher
        RequestDispatcher dispatcher;
        // Redirigir al JSP
        dispatcher = request.getRequestDispatcher("usuarioFinal/lista_albergues.jsp");
        dispatcher.forward(request, response);
    }

    private void detallesAlbergue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idAlbergue = request.getParameter("id");
        System.out.println(idAlbergue);

        if (idAlbergue != null && !idAlbergue.isEmpty()) {
            try {

                // Convertir el id a un número si es necesario
                int idUsuarioAlbergue = Integer.parseInt(idAlbergue);

                // Obtener los detalles del albergue y los puntos de acopio asociados
                DaoPerfilAlbergue albergueDAO = new DaoPerfilAlbergue();
                Usuario perfilAlbergue = albergueDAO.obtenerPerfilAlbergue(idUsuarioAlbergue);

                List<PuntoAcopio> puntosAcopio = albergueDAO.obtenerPuntosAcopio(idUsuarioAlbergue);
                if (puntosAcopio == null || puntosAcopio.isEmpty()) {
                    System.out.println("La lista de puntos de acopio está vacía.");
                }

                //Gianfranco: Agregado para tener session como cierto perfil de albergue
                HttpSession session = request.getSession();
                session.setAttribute("perfilAlbergue", perfilAlbergue);
                session.setAttribute("puntosAcopio", puntosAcopio);

                //Se instancia el RequestDispatcher
                RequestDispatcher dispatcher;
                // Redirigir al JSP
                dispatcher = request.getRequestDispatcher("usuarioFinal/detalle_albergue.jsp");
                dispatcher.forward(request, response);

            } catch (NumberFormatException e) {
                // En caso de que el id no sea válido o no se pueda convertir
                response.sendRedirect("ListaAlberguesServlet?accion=listar");
            }
        } else {
            // Si no se pasa un ID válido en la solicitud
            response.sendRedirect("ListaAlberguesServlet?accion=listar");
        }
    }


    private void donarAlbergue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la sesión actual
        HttpSession session = request.getSession();

        // Obtener el objeto Usuario (quien hará la donación) desde la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");


        //Gianfranco: Sobre Albergue escogido para los detalles
        Usuario perfilAlbergue = (Usuario) session.getAttribute("perfilAlbergue");
        String idAlbergue = String.valueOf(perfilAlbergue.getId_usuario());

        // Validar que el usuario esté autenticado
        if (usuario == null) {
            response.sendRedirect("LoginServlet");
            return;
        }


        if (idAlbergue != null && !idAlbergue.isEmpty()) {
            try {
                // Convertir el ID del albergue a un número entero
                int idUsuarioAlbergue = Integer.parseInt(idAlbergue);


                if (perfilAlbergue != null) {
                    // Redirigir al JSP del formulario de donaciones
                    //Se instancia el RequestDispatcher
                    RequestDispatcher dispatcher;
                    // Redirigir al JSP
                    dispatcher = request.getRequestDispatcher("usuarioFinal/formulario_donaciones.jsp");
                    dispatcher.forward(request, response);
                } else {
                    // Si no se encuentra el perfil del albergue

                    response.sendRedirect("ListaAlberguesServlet?accion=listar");
                }
            } catch (NumberFormatException e) {
                // Manejar el caso donde el ID no sea un número válido
                e.printStackTrace();
                response.sendRedirect("ListaAlberguesServlet?accion=listar");
            }
        } else {
            // Si no se pasa un ID válido
            response.sendRedirect("ListaAlberguesServlet?accion=listar");
        }
    }





    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        System.out.println("Acción recibida: " + accion); // Verificar qué valor está llegando

        if (accion == null || accion.isEmpty()) {
            response.sendRedirect("ListaAlberguesServlet?accion=listar");
            return;
        }

        if ("donacion".equalsIgnoreCase(accion)) {
            registrarDonacion(request, response);
        } else {
            response.sendRedirect("ListaAlberguesServlet?accion=listar");
        }
    }

    private void registrarDonacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la sesión actual
        HttpSession session = request.getSession();

        // Obtener el usuario que realiza la donación desde la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Obtener el perfil del albergue desde la sesión
        Usuario perfilAlbergue = (Usuario) session.getAttribute("perfilAlbergue");

        // Validar que el usuario y el perfil del albergue estén disponibles
        if (usuario == null || perfilAlbergue == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El usuario o el perfil del albergue no están disponibles en la sesión.");
            return;
        }

        try {
            // Obtener el ID del usuario que realiza la donación
            int idUsuarioFinal = usuario.getId_usuario();
            System.out.println("ID Usuario Final: " + idUsuarioFinal);

            // Obtener el ID del albergue
            int idAlbergue = perfilAlbergue.getId_usuario();
            System.out.println("ID Albergue: " + idAlbergue);

            // Llamar al DAO para obtener el id_solicitud_donacion_economica
            ListaAlberguesDao donacionDAO = new ListaAlberguesDao();
            int idSolicitudDonacionEconomica = donacionDAO.obtenerIdSolicitudDonacionEconomica(idAlbergue);

            if (idSolicitudDonacionEconomica == -1) {
                // Si no se encontró ningún id_solicitud_donacion_economica, redirigir con error
                response.sendRedirect("ListaAlberguesServlet?accion=listar");
                return;
            }

            // Obtener los datos enviados desde el formulario
            String montoString = request.getParameter("monto");
            Part archivoPart = request.getPart("archivo");

            // Validar el monto de la donación
            if (montoString == null || montoString.trim().isEmpty()) {
                response.sendRedirect("ListaAlberguesServlet");
                return;
            }

            int montoDonacion;
            try {
                montoDonacion = Integer.parseInt(montoString.trim());
                if (montoDonacion <= 0) {
                    response.sendRedirect("ListaAlberguesServlet");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("ListaAlberguesServlet");
                return;
            }

            // Validar el archivo subido
            if (archivoPart == null || archivoPart.getSize() <= 0) {
                response.sendRedirect("ListaAlberguesServlet");
                return;
            }

            // Validar el tipo de archivo (solo imágenes)
            String contentType = archivoPart.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.sendRedirect("ListaAlberguesServlet");
                return;
            }

            // Leer el contenido del archivo como un arreglo de bytes
            byte[] imagenDonacion;
            String nombreImagenDonacion = archivoPart.getSubmittedFileName();
            try (InputStream inputStream = archivoPart.getInputStream()) {
                imagenDonacion = inputStream.readAllBytes();
            }

            // Verificar los datos obtenidos para depuración
            System.out.println("ID Usuario Final: " + idUsuarioFinal);
            System.out.println("ID Albergue: " + idAlbergue);
            System.out.println("ID Solicitud Donación Económica: " + idSolicitudDonacionEconomica);
            System.out.println("Monto: " + montoDonacion);
            System.out.println("Nombre imagen: " + nombreImagenDonacion);
            System.out.println("Tamaño de la imagen: " + imagenDonacion.length);

            // Registrar la donación
            int idEstado = 1; // Estado predeterminado (activo)
            boolean registroExitoso = donacionDAO.registrarDonacionEconomica(
                    idUsuarioFinal,
                    montoDonacion,
                    imagenDonacion,
                    nombreImagenDonacion,
                    idSolicitudDonacionEconomica,
                    idEstado
            );

            if (registroExitoso) {
                // Redirigir al historial de donaciones o mostrar un mensaje de éxito
                //Se instancia el RequestDispatcher
                RequestDispatcher dispatcher;
                // Redirigir al JSP
                dispatcher = request.getRequestDispatcher("usuarioFinal/lista_albergues.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect("ListaAlberguesServlet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ListaAlberguesServlet?accion=listar");
        }
    }


}
