package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.AdopcionesUsuarioFinalDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;


@WebServlet(name = "AdopcionesUsuarioFinalServlet", value = "/AdopcionesUsuarioFinalServlet")
public class AdopcionesUsuarioFinalServlet extends HttpServlet {

    @Override //Get implica que el JSP esta solicitando informacion
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Se obtiene el parametro accion del request usando
        //getParameter

        String accion = request.getParameter("accion");

        //Se instancia el Dao respectivo
        AdopcionesUsuarioFinalDao adopcionUsuarioFinalDao = new AdopcionesUsuarioFinalDao();

        //Se instancia el RequestDispatcher
        RequestDispatcher dispatcher;

        //Se verifica la accion, en caso sea nula se usa la predeterminada
        if (accion == null || accion.isEmpty()) {
            accion = "ver";
        }
        try {
            if(accion.equals("ver")) {

                    // Obtenemos la ID del usuario desde la sesion
                    HttpSession session = request.getSession();

                    Usuario usuario = (Usuario) session.getAttribute("usuario");
                    int ID_usuario = usuario.getId_usuario();

                    //Se obtiene los atributos a enviar usando el Dao y se agregan
                    //al request usando setAttribute

                    Usuario datosUsuario = usuario;

                    ArrayList<PublicacionMascotaAdopcion> publicacionesAdopcion = adopcionUsuarioFinalDao.obtenerListaPublicacionesAdopcion(usuario.getId_usuario());

                    request.setAttribute("publicacionesAdopcion", publicacionesAdopcion);

                    //Por ultimo se define el dispatcher mediante el metodo getRequestDispatcher
                    //especificando el jsp destino (direccion absoluta o la relativa al directorio webapp

                    dispatcher = request.getRequestDispatcher("usuarioFinal/adopciones_usuario_final.jsp");

                    //Se utiliza el metodo forward sobre el dispatcher para ejectuar las acciones
                    //Notar que el objeto request es el contenedor de los atributos enviados pues estamos
                    //usando RequestDispatcher, no obstante, si quisieramos redirigir o enviar
                    //algo directamente al cliente se tendria que emplear el objeto response

                    dispatcher.forward(request, response);

                    //Temporal

                    if (datosUsuario != null) {
                        System.out.println("ID Usuario: " + datosUsuario.getId_usuario());
                        System.out.println("Nombre Usuario: " + datosUsuario.getNombres_usuario_final());
                        System.out.println("Apellido Usuario: " + datosUsuario.getApellidos_usuario_final());
                        System.out.println("Correo Usuario: " + datosUsuario.getCorreo_electronico());
                    } else {
                        System.out.println("No se encontró el usuario con ID: " + ID_usuario);
                    }
                }
                else if (accion.equals("detalles")) {

                    HttpSession session = request.getSession();
                    Usuario usuario = (Usuario) session.getAttribute("usuario");

                    if (usuario != null) {

                        //Validaciones

                        String idPublicacionParameter = request.getParameter("idPublicacion");

                        int tiene_postulacion = request.getParameter("tiene_postulacion").equals("1") ? 1 : 0;

                        request.setAttribute("tiene_postulacion", tiene_postulacion);

                        System.out.println("idPublicacionParameter: " + idPublicacionParameter);

                        if(idPublicacionParameter != null){

                            int idPublicacion = Integer.parseInt(idPublicacionParameter);

                            //Obtencion de la data y envio hacia el jsp

                            PublicacionMascotaAdopcion publicacion = adopcionUsuarioFinalDao.obtenerPublicacionAdopcion(idPublicacion);

                            request.setAttribute("publicacion", publicacion);

                            dispatcher = request.getRequestDispatcher("usuarioFinal/detalles_adopciones_usuario_final.jsp");

                            dispatcher.forward(request, response);
                        }
                        else{
                            System.out.println("Id Publicacion nula");
                            response.sendRedirect("AdopcionesUsuarioFinalServlet");
                        }
                    }
                    else{
                        System.out.println("Usuario nulo");
                        response.sendRedirect("AdopcionesUsuarioFinalServlet");
                    }
                }
                else if (accion.equals("historial")) {
                    // Obtenemos la ID del usuario desde la sesion
                    HttpSession session = request.getSession();

                    Usuario usuario = (Usuario) session.getAttribute("usuario");
                    int ID_usuario = usuario.getId_usuario();

                    int tiene_postulacion = 1;

                    request.setAttribute("tiene_postulacion", tiene_postulacion);

                    //Se obtiene los atributos a enviar usando el Dao y se agregan
                    //al request usando setAttribute

                    Usuario datosUsuario = usuario;

                    ArrayList<PublicacionMascotaAdopcion> publicacionesHistorial = adopcionUsuarioFinalDao.verHistorialPostulacionMascotaAdopcion(usuario.getId_usuario());

                    request.setAttribute("publicacionesHistorial", publicacionesHistorial);

                    //Por ultimo se define el dispatcher mediante el metodo getRequestDispatcher
                    //especificando el jsp destino (direccion absoluta o la relativa al directorio webapp

                    dispatcher = request.getRequestDispatcher("usuarioFinal/adopciones_historial_usuario.jsp");

                    //Se utiliza el metodo forward sobre el dispatcher para ejectuar las acciones
                    //Notar que el objeto request es el contenedor de los atributos enviados pues estamos
                    //usando RequestDispatcher, no obstante, si quisieramos redirigir o enviar
                    //algo directamente al cliente se tendria que emplear el objeto response

                    dispatcher.forward(request, response);

                    //Temporal

                    if (datosUsuario != null) {
                        System.out.println("ID Usuario: " + datosUsuario.getId_usuario());
                        System.out.println("Nombre Usuario: " + datosUsuario.getNombres_usuario_final());
                        System.out.println("Apellido Usuario: " + datosUsuario.getApellidos_usuario_final());
                        System.out.println("Correo Usuario: " + datosUsuario.getCorreo_electronico());
                    } else {
                        System.out.println("No se encontró el usuario con ID: " + ID_usuario);
                    }
                }
        } catch (Exception e) {
            response.sendRedirect("AdopcionesUsuarioFinalServlet");
        }
    }

    @Override //Post implica que el JSP esta enviando informacion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Se obtiene el parametro accion del request usando
        //getParameter
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = request.getParameter("accion");

        //Se instancia el Dao respectivo
        AdopcionesUsuarioFinalDao adopcionUsuarioFinalDao = new AdopcionesUsuarioFinalDao();

        //Se instancia el RequestDispatcher
        RequestDispatcher dispatcher;

        //Se verifica la accion, en caso sea nula se usa la predeterminada
        if (accion == null || accion.isEmpty()) {
            accion = "ver";
        }

        switch (accion) {
            case "adoptar":
                try {
                    adopcionUsuarioFinalDao.registrarPostulacionMascotaAdopcion(Timestamp.valueOf(LocalDateTime.now()),Integer.parseInt(request.getParameter("id")), usuario.getId_usuario(), 1);
                    response.sendRedirect("AdopcionesUsuarioFinalServlet");
                } catch (Exception e) {
                    response.sendRedirect("AdopcionesUsuarioFinalServlet?accion=detalles&idPublicacion=" + request.getParameter("id"));
                }
                break;
        }
    }
}
