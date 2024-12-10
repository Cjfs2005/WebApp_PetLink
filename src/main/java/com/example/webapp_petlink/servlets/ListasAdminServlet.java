package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Rol;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Zona;
import com.example.webapp_petlink.daos.ListasAdminDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name="ListasAdminServlet", value="/ListasAdminServlet")
public class ListasAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listaCoord" : request.getParameter("action");

        ListasAdminDao dao = new ListasAdminDao();

        HttpSession session = request.getSession();

        Integer idAdmin = (Integer) session.getAttribute("id_usuario");

        if (idAdmin == null) {
            idAdmin = 10;
        }

        RequestDispatcher dispatcher;

        switch (action) {
            case "listaCoord":
                ArrayList<Usuario> coordinadores = dao.listasCoodinadores();

                request.setAttribute("coordinadores", coordinadores);
                dispatcher = request.getRequestDispatcher("administrador/lista_coordinador.jsp");
                dispatcher.forward(request, response);
                break;

            case "crearCoord":
                ArrayList<Zona> zonasSinCoordinador = dao.obtenerZonasSinCoordinador();
                request.setAttribute("zonas", zonasSinCoordinador);
                dispatcher = request.getRequestDispatcher("administrador/crearCoordinador.jsp");
                dispatcher.forward(request, response);
                break;

            case "mostrar":
                // Obtener el ID del evento desde los parámetros de la solicitud
                int idCoordi;
                try {
                    idCoordi = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath()+"/administrador/lista_coordinador.jsp");
                    return;
                }

                Usuario coordinador = dao.obtenerCoordinadorPorId(idCoordi);
                ArrayList<Zona> zonas = dao.obtenerZonasSinCoordinador();
                request.setAttribute("zonas", zonas);
                System.out.println("Se obtuvo el coordinador con ID: " + coordinador.getId_usuario());
                System.out.println("Su apellido es "+ coordinador.getApellidos_coordinador());
                System.out.println("Su fecha de nacimiento es "+coordinador.getFecha_nacimiento());

                request.setAttribute("coordinador", coordinador);
                dispatcher = request.getRequestDispatcher("administrador/informacion_personal_coordinador.jsp");
                dispatcher.forward(request, response);
                break;

            case "eliminar":
                String idCoordinador = request.getParameter("id");
                int CoordID = Integer.parseInt(idCoordinador);
                if (dao.obtenerCoordinadorPorId(CoordID) != null) {
                    dao.eliminarCoordinador(CoordID);
                }
                response.sendRedirect(request.getContextPath()+"/ListasAdminServlet?action=listaCoord");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "listaCoord" : request.getParameter("action");

        ListasAdminDao dao = new ListasAdminDao();

        HttpSession session = request.getSession();

        Integer idAdmin = (Integer) session.getAttribute("id_usuario");

        if (idAdmin == null) {
            idAdmin = 10;
        }

        RequestDispatcher dispatcher;

        switch(action) {
            case "guardar":
                System.out.println("HE LLEGADO AL SERVLET PARA GUARDAR INFO");
                //1. Extraer datos delform
                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String dni = request.getParameter("dni");
                String telefono = request.getParameter("telefono");
                String correo = request.getParameter("correo");
                String zonaAsignada = request.getParameter("zonaAsignada");
                String fechaNacimientoStr = request.getParameter("fechaNacimiento"); // Extraer la fecha de nacimiento

                System.out.println("DATOS: "+nombre+ " | "+apellido+ " | "+dni+ " | "+telefono+ " | "+correo);

                System.out.println(zonaAsignada);
                System.out.println("Fecha de nacimiento: " + fechaNacimientoStr);

                System.out.println("Se paso el paso 1");

                // 2. Validar datos obligatorios
                if (nombre == null || apellido == null || dni == null || telefono == null ||
                        correo == null || zonaAsignada == null || fechaNacimientoStr == null) {
                    request.setAttribute("error", "Por favor, complete todos los campos obligatorios.");
                    request.getRequestDispatcher("administrador/crearCoordinador.jsp").forward(request, response);
                    return;
                }

                // Validar la fecha
                LocalDate fechaNacimiento;
                try {
                    fechaNacimiento = LocalDate.parse(fechaNacimientoStr); // Convertir la fecha de String a LocalDate
                } catch (DateTimeParseException e) {
                    request.setAttribute("error", "La fecha de nacimiento no es válida.");
                    System.out.println("Error en la conversión de la fecha de nacimiento: " + e.getMessage());
                    request.getRequestDispatcher("administrador/crearCoordinador.jsp").forward(request, response);
                    return;
                }

                int idZona;
                try {
                    idZona = Integer.parseInt(zonaAsignada);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "El lugar del evento seleccionado no es válido.");
                    System.out.println("Error en la asignacion de la zona");
                    request.getRequestDispatcher("administrador/crearCoordinador.jsp").forward(request, response);
                    return;
                }

                System.out.println("Ahora vamos a crear el uaurio");

                Usuario coordinador = new Usuario();
                coordinador.setNombres_coordinador(nombre);
                coordinador.setApellidos_coordinador(apellido);
                coordinador.setDni(dni);
                coordinador.setCorreo_electronico(correo);
                coordinador.setNumero_yape_plin(telefono);
                coordinador.setFecha_nacimiento(fechaNacimiento);

                Zona zona = new Zona();
                zona.setId_zona(idZona);
                coordinador.setZona(zona);

                int idRol = 3;
                Rol rol = new Rol();
                rol.setId_rol(idRol);
                coordinador.setRol(rol);

                boolean es_usuario_activo = true;
                coordinador.setEs_usuario_activo(es_usuario_activo);

                Date fecha = new Date();
                LocalDateTime fechaLocalDateTime = fecha.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                System.out.println("la fecha de creacion es " +  fechaLocalDateTime);
                coordinador.setFecha_hora_creacion(fechaLocalDateTime);

                // 4. Guardar el evento en la base de datos
                boolean guardado = dao.guardarCoordinador(coordinador);

                if (guardado) {
                    // 5. Redirigir a la lista de eventos con un mensaje de éxito
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&success=true");
                } else {
                    // Manejar el error si no se pudo guardar
                    request.setAttribute("error", "Hubo un problema al guardar el evento. Por favor, inténtelo nuevamente.");
                    System.out.println("ha ocurrido un error en el guardado");
                    request.getRequestDispatcher("administrador/crearCoordinador.jsp").forward(request, response);
                }
                break;

            case "actualizar":
                System.out.println("HE LLEGADO AL SERVLET PARA ACTUALIZAR INFO");
                //1. Extraer datos delform
                nombre = request.getParameter("nombre");
                apellido = request.getParameter("apellido");
                dni = request.getParameter("dni");
                telefono = request.getParameter("telefono");
                correo = request.getParameter("correo");
                zonaAsignada = request.getParameter("zonaAsignada");
                fechaNacimientoStr = request.getParameter("fechaNacimiento"); // Extraer la fecha de nacimiento

                System.out.println("DATOS: "+nombre+ " | "+apellido+ " | "+dni+ " | "+telefono+ " | "+correo);

                System.out.println(zonaAsignada);
                System.out.println("Fecha de nacimiento: " + fechaNacimientoStr);

                System.out.println("Se paso el paso 1");

                // 2. Validar datos obligatorios
                if (nombre == null || apellido == null || dni == null || telefono == null ||
                        correo == null || zonaAsignada == null || fechaNacimientoStr == null) {
                    request.setAttribute("error", "Por favor, complete todos los campos obligatorios.");
                    request.getRequestDispatcher("administrador/informacion_personal_coordinador.jsp").forward(request, response);
                    return;
                }

                // Validar la fecha
                LocalDate fechaNacimiento1;
                try {
                    fechaNacimiento1 = LocalDate.parse(fechaNacimientoStr); // Convertir la fecha de String a LocalDate
                } catch (DateTimeParseException e) {
                    request.setAttribute("error", "La fecha de nacimiento no es válida.");
                    System.out.println("Error en la conversión de la fecha de nacimiento: " + e.getMessage());
                    request.getRequestDispatcher("administrador/informacion_personal_coordinador.jsp").forward(request, response);
                    return;
                }

                int idZona1;
                try {
                    idZona1 = Integer.parseInt(zonaAsignada);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "El lugar del evento seleccionado no es válido.");
                    System.out.println("Error en la asignacion de la zona");
                    request.getRequestDispatcher("administrador/informacion_personal_coordinador.jsp").forward(request, response);
                    return;
                }

                Usuario coordinador1 = new Usuario();
                coordinador1.setNombres_coordinador(nombre);
                coordinador1.setApellidos_coordinador(apellido);
                coordinador1.setDni(dni);
                coordinador1.setCorreo_electronico(correo);
                coordinador1.setNumero_yape_plin(telefono);
                coordinador1.setFecha_nacimiento(fechaNacimiento1);

                Zona zona1 = new Zona();
                zona1.setId_zona(idZona1);
                coordinador1.setZona(zona1);

                // 4. Actualizar el evento en la base de datos
                boolean actualizado = dao.actualizarCoordinador(coordinador1);

                if (actualizado) {
                    // 5. Redirigir a la lista de eventos con un mensaje de éxito
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&success=true");
                } else {
                    // Manejar el error si no se pudo guardar
                    request.setAttribute("error", "Hubo un problema al actualizar la informacion del coordinador. Por favor, inténtelo nuevamente.");
                    System.out.println("ha ocurrido un error en la actualizacion");
                    request.getRequestDispatcher("administrador/informacion_personal_coordinador.jsp").forward(request, response);
                }
                break;
        }
    }
}
