package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.Rol;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Zona;
import com.example.webapp_petlink.daos.ListasAdminDao;
import com.example.webapp_petlink.daos.LoginDao;
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
public class  ListasAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

        RequestDispatcher dispatcher;

        switch (action) {
            case "listaCoord":
                try {
                    ArrayList<Usuario> coordinadores = dao.listasCoodinadores();
                    if (coordinadores == null){
                        System.out.println("Todo est+a fallando");
                    }

                    request.setAttribute("coordinadores", coordinadores);
                    dispatcher = request.getRequestDispatcher("administrador/lista_coordinador.jsp");
                    dispatcher.forward(request, response);
                    break;
                }
                catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&error");
                }

            case "crearCoord":
                try {
                    ArrayList<Zona> zonasSinCoordinador = dao.obtenerZonasSinCoordinador();
                    request.setAttribute("zonas", zonasSinCoordinador);
                    dispatcher = request.getRequestDispatcher("administrador/crearCoordinador.jsp");
                    dispatcher.forward(request, response);
                    break;
                }
                catch (Exception e){
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&error");
                }

            case "mostrar":
                // Obtener el ID del evento desde los parámetros de la solicitud
                try {
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

                    request.setAttribute("coordinador", coordinador);
                    dispatcher = request.getRequestDispatcher("administrador/informacion_personal_coordinador.jsp");
                    dispatcher.forward(request, response);
                    break;
                }
                catch (Exception e){
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&error");
                }

            case "eliminar":
                try {
                    String idCoordinador = request.getParameter("id");
                    int CoordID = Integer.parseInt(idCoordinador);
                    if (dao.obtenerCoordinadorPorId(CoordID) != null) {
                        dao.eliminarCoordinador(CoordID);
                    }
                    response.sendRedirect(request.getContextPath()+"/ListasAdminServlet?action=listaCoord");
                    break;
                }
                catch (Exception e){
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&error");
                }

            case "solicitudesUsuarios":

                ArrayList<Usuario> usuariosRegistrados = dao.listaUsuarios();

                request.setAttribute("usuarios", usuariosRegistrados);
                dispatcher = request.getRequestDispatcher("administrador/lista_usuarios.jsp");
                dispatcher.forward(request, response);

                break;

            case "aceptarUsuario": { //Funciona tanto para usuarios como para albergues

                try {
                    String id_usuario = request.getParameter("id");

                    int idUsuario = Integer.parseInt(id_usuario);

                    Usuario usuario = dao.existeUsuario(idUsuario);

                    if (usuario != null) {
                        dao.aceptarUsuario(usuario);
                    }

                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=solicitudesUsuarios");
                    break;
                }
                catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=solicitudesUsuarios&error=1");
                }
            }

            case "rechazarUsuario": { //Funciona tanto para usuarios como para albergues

                try {
                    String id_usuario = request.getParameter("id");

                    int idUsuario = Integer.parseInt(id_usuario);

                    Usuario usuario = dao.existeUsuario(idUsuario);

                    if (usuario != null) {
                        dao.rechazarUsuario(usuario);
                    }

                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=solicitudesUsuarios");
                    break;
                }
                catch (Exception e){
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=solicitudesUsuarios&error=1");
                }
            }
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

        switch(action) {
            case "guardar":
                try {
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

                    // 2. Validar datos obligatorios
                    if (nombre == null || apellido == null || dni == null || telefono == null ||
                            correo == null || zonaAsignada == null || fechaNacimientoStr == null) {
                        response.sendRedirect(request.getContextPath()+"/ListasAdminServlet?action=crearCoord&error=1");
                        return;
                    }

                    // Validar la fecha
                    LocalDate fechaNacimiento;
                    try {
                        fechaNacimiento = LocalDate.parse(fechaNacimientoStr); // Convertir la fecha de String a LocalDate
                    } catch (DateTimeParseException e) {
                        response.sendRedirect(request.getContextPath()+"/ListasAdminServlet?action=crearCoord&error=1");
                        return;
                    }

                    int idZona;
                    try {
                        idZona = Integer.parseInt(zonaAsignada);
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath()+"/ListasAdminServlet?action=crearCoord&error=1");
                        return;
                    }

                    // Vamos a obtener su contraseña
                    String contrasenia = dao.generarContrasenia(8);
                    LoginDao logindao = new LoginDao();
                    String contrasenia_hashed = logindao.hashString(contrasenia, "SHA-256");

                    Usuario coordinador = new Usuario();
                    coordinador.setNombres_coordinador(nombre);
                    coordinador.setApellidos_coordinador(apellido);
                    coordinador.setDni(dni);
                    coordinador.setCorreo_electronico(correo);
                    coordinador.setNumero_yape_plin(telefono);
                    coordinador.setFecha_nacimiento(fechaNacimiento);
                    coordinador.setContrasenia(contrasenia);
                    coordinador.setContrasenia_hashed(contrasenia_hashed);

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
                    coordinador.setFecha_hora_creacion(fechaLocalDateTime);

                    // 4. Guardar el evento en la base de datos
                    boolean guardado = dao.guardarCoordinador(coordinador);

                    if (guardado) {
                        // 5. Redirigir a la lista de eventos con un mensaje de éxito
                        response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=listaCoord&success=true");
                    } else {
                        // Manejar el error si no se pudo guardar
                        response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=crearCoord&error=CoordNoCreado");
                    }
                    break;
                }
                catch (Exception e){
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=crearCoord&error=1");
                }


            case "actualizar":
                try {
                    //1. Extraer datos delform
                    int idCoordinador = Integer.parseInt(request.getParameter("idCoordinador"));
                    String nombre = request.getParameter("nombre");
                    String apellido = request.getParameter("apellido");
                    String dni = request.getParameter("dni");
                    String telefono = request.getParameter("telefono");
                    String correo = request.getParameter("correo");
                    String zonaAsignada = request.getParameter("zonaAsignada");
                    String fechaNacimientoStr = request.getParameter("fechaNacimiento"); // Extraer la fecha de nacimiento

                    System.out.println("DATOS: "+nombre+ " | "+apellido+ " | "+dni+ " | "+telefono+ " | "+correo);

                    // 2. Validar datos obligatorios
                    if (nombre == null || apellido == null || dni == null || telefono == null ||
                            correo == null || zonaAsignada == null || fechaNacimientoStr == null) {
                        response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=mostrar&error=1");
                        return;
                    }

                    // Validar la fecha
                    LocalDate fechaNacimiento1;
                    try {
                        fechaNacimiento1 = LocalDate.parse(fechaNacimientoStr); // Convertir la fecha de String a LocalDate
                    } catch (DateTimeParseException e) {
                        response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=mostrar&error=1");
                        return;
                    }

                    int idZona1;
                    try {
                        idZona1 = Integer.parseInt(zonaAsignada);
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=mostrar&error=1");
                        return;
                    }

                    Usuario coordinador1 = new Usuario();
                    coordinador1.setId_usuario(idCoordinador);
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
                        response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=mostrar&error=CoordNoActualizado");
                    }
                    break;
                }
                catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/ListasAdminServlet?action=mostrar&error=CoordNoActualizado");
                }
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
}