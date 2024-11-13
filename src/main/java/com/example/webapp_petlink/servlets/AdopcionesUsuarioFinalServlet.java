package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.AdopcionesUsuarioFinalDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
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

        switch (accion) {
            case "ver":

                // Obtenemos la ID del usuario desde la sesion
                HttpSession session = request.getSession();

                Integer integer_ID_usuario = (Integer) session.getAttribute("id_usuario");

                // Si el usuario es nulo entonces por ahora se asume que el id es 1
                if (integer_ID_usuario == null) {
                    integer_ID_usuario = 1; // Al final se modificara para colocar que redirija a error.jsp o algo asi
                }

                int ID_usuario = (int) integer_ID_usuario;

                //Se obtiene los atributos a enviar usando el Dao y se agregan
                //al request usando setAttribute

                Usuario datosUsuario = adopcionUsuarioFinalDao.obtenerDatosUsuario(ID_usuario);

                request.setAttribute("datosUsuario", datosUsuario);

                ArrayList<PublicacionMascotaAdopcion> publicacionesAdopcion= adopcionUsuarioFinalDao.obtenerListaPublicacionesAdopcion();

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
                    request.setAttribute("datosUsuario", datosUsuario);
                } else {
                    System.out.println("No se encontró el usuario con ID: " + ID_usuario);
                }

                //

                break;
            default:
                response.sendRedirect("error.jsp");
                break;
        }
    }

    @Override //Post implica que el JSP esta enviando informacion
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
