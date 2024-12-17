package com.example.webapp_petlink.servlets;

import com.example.webapp_petlink.beans.*;
import com.example.webapp_petlink.daos.RegisterDao;
import jakarta.mail.MessagingException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import com.example.webapp_petlink.daos.LoginDao;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet  {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action_parameter = request.getParameter("action");
        String action = action_parameter == null ? "loginForm" : action_parameter;
        RegisterDao registerDao = new RegisterDao();
        RequestDispatcher view;

        switch (action) {

            case "registroUsuario": {

                //Validar que los datos ingresados por el usuario cumplen con los tipos de datos

                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                String dni = request.getParameter("dni");
                String direccion = request.getParameter("direccion");
                String email = request.getParameter("email");

                try {

                    int distrito = Integer.parseInt(request.getParameter("distrito"));

                    if (nombre == null || nombre.length() > 45) {

                        // Manejar error: El nombre es demasiado largo o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    } else if (apellido == null || apellido.length() > 45) {

                        // Manejar error: El apellido es demasiado largo o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    } else if (dni == null || dni.length() != 8) {

                        // Manejar error: El dni no cumple con la longitud adecuada o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    } else if (distrito < 0 || distrito > 42) {
                        // Manejar error: El distrito no es un valor valido
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    } else if (direccion == null || direccion.length() > 100) {

                        // Manejar error: La direccion no cumple con la longitud adecuada o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    } else if (email == null || email.length() > 100) {

                        // Manejar error: El email no cumple con la longitud adecuada o está vacío

                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    }

                    //Valida que el correo sea del dominio @gmail.com

                    String[] parts = email.split("@");

                    if (parts.length != 2) {
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_usuario.jsp");
                        view.forward(request, response);
                        break;
                    } else {

                        String domain = "gmail.com";

                        if (!parts[1].toLowerCase().equals(domain)) {
                            request.setAttribute("estado", "errorFormato");
                            view = request.getRequestDispatcher("registro_usuario.jsp");
                            view.forward(request, response);
                            break;
                        }
                    }
                } catch (Exception e) {
                    request.setAttribute("estado", "errorFormato");
                    view = request.getRequestDispatcher("registro_usuario.jsp");
                    view.forward(request, response);
                    break;
                }

                //Valida que no existe usuario activo que coincida con el correo electronico

                int id_distrito = Integer.parseInt(request.getParameter("distrito"));

                boolean existe = registerDao.existeUsuario(dni, email);

                if (existe) {
                    request.setAttribute("estado", "errorRegistroExistente");
                    view = request.getRequestDispatcher("registro_usuario.jsp");
                    view.forward(request, response);
                    break;
                } else {
                    //Crear el usuario en la BD con la contrasenia temporal

                    Usuario usuario = new Usuario();

                    usuario.setDni(dni);
                    usuario.setNombres_usuario_final(nombre);
                    usuario.setApellidos_usuario_final(apellido);
                    usuario.setDireccion(direccion);

                    Rol rol = new Rol();
                    rol.setId_rol(1);
                    usuario.setRol(rol);

                    usuario.setCorreo_electronico(email);

                    Distrito distrito = new Distrito();
                    distrito.setId_distrito(id_distrito);
                    usuario.setDistrito(distrito);

                    Zona zona = new Zona();
                    zona.setId_zona(registerDao.obtenerIdZona(id_distrito));
                    usuario.setZona(zona);

                    //Cargar la imagen por defecto

                    String relativePath = "/usuarioFinal/images/perfil_usuario_defecto.png";
                    String absolutePath = getServletContext().getRealPath(relativePath);

                    File file = new File(absolutePath);
                    FileInputStream fis = new FileInputStream(file);
                    byte[] datosImagen = new byte[(int) file.length()];
                    fis.read(datosImagen);
                    fis.close();

                    usuario.setFoto_perfil(datosImagen);
                    usuario.setNombre_foto_perfil(file.getName());

                    registerDao.crearUsuario(usuario);

                    //Redirige al formulario de registro de usuarios y muestra el modal de que recibira la aceptacion pronto

                    request.setAttribute("estado", "satisfactorio");
                    view = request.getRequestDispatcher("registro_usuario.jsp");
                    view.forward(request, response);
                }
                break;
            }
            case "registroAlbergue": {

                //Validar que los datos ingresados por el usuario cumplen con los tipos de datos

                String nombres_encargado = request.getParameter("nombres_encargado");

                String apellidos_encargado = request.getParameter("apellidos_encargado");
                String nombre_albergue = request.getParameter("nombre_albergue");
                String email = request.getParameter("email");
                String url = request.getParameter("url");

                try {

                    if (nombres_encargado == null || nombres_encargado.length() > 45) {

                        // Manejar error: El nombre del encargado es demasiado largo o está vacío
                        System.out.println("Falla en el nombre del encargado");
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_albergue.jsp");
                        view.forward(request, response);
                        break;
                    } else if (apellidos_encargado == null || apellidos_encargado.length() > 45) {

                        // Manejar error: El apellido del encargado es demasiado largo o está vacío
                        System.out.println("Falla en el apellido del encargado");
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_albergue.jsp");
                        view.forward(request, response);
                        break;
                    } else if (nombre_albergue == null || nombre_albergue.length() > 45) {

                        // Manejar error: El nombre del albergue no cumple con la longitud adecuada o está vacío
                        System.out.println("Falla en el nombre de albergue");
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_albergue.jsp");
                        view.forward(request, response);
                        break;
                    }
                    else if (email == null || email.length() > 100) {

                        // Manejar error: El email no cumple con la longitud adecuada o está vacío
                        System.out.println("Falla en el email");
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_albergue.jsp");
                        view.forward(request, response);
                        break;
                    }
                    else if (url == null || url.length() > 200) {

                        // Manejar error: El url de instagram no cumple con la longitud adecuada o está vacío
                        System.out.println("Falla en la url");
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_albergue.jsp");
                        view.forward(request, response);
                        break;
                    }

                    //Valida que el correo sea del dominio @gmail.com

                    String[] parts = email.split("@");

                    if (parts.length != 2) {
                        System.out.println("Falla en el correo electronico, segunda validacion");
                        request.setAttribute("estado", "errorFormato");
                        view = request.getRequestDispatcher("registro_albergue.jsp");
                        view.forward(request, response);
                        break;
                    } else {

                        String domain = "gmail.com";

                        if (!parts[1].toLowerCase().equals(domain)) {
                            System.out.println("Falla en el dominio del correo electronico");
                            request.setAttribute("estado", "errorFormato");
                            view = request.getRequestDispatcher("registro_albergue.jsp");
                            view.forward(request, response);
                            break;
                        }
                    }
                } catch (Exception e) {
                    request.setAttribute("estado", "errorFormato");
                    view = request.getRequestDispatcher("registro_albergue.jsp");
                    view.forward(request, response);
                    break;
                }

                //Valida que no existe albergue activo que coincida con el correo electronico

                boolean existe = registerDao.existeAlbergue(url, email);

                if (existe) {
                    request.setAttribute("estado", "errorRegistroExistente");
                    view = request.getRequestDispatcher("registro_albergue.jsp");
                    view.forward(request, response);
                    break;
                }
                else {

                    //Crear el albergue en la BD con la contrasenia temporal

                    Usuario usuario = new Usuario();

                    usuario.setNombres_encargado(nombres_encargado);
                    usuario.setApellidos_encargado(apellidos_encargado);
                    usuario.setNombre_albergue(nombre_albergue);
                    usuario.setCorreo_electronico(email);
                    usuario.setUrl_instagram(url);

                    Rol rol = new Rol();
                    rol.setId_rol(2);
                    usuario.setRol(rol);

                    //Cargar las imagenes por defecto

                    String relativePath = "/albergue/images/perfil_albergue_defecto.png";
                    String absolutePath = getServletContext().getRealPath(relativePath);

                    File file = new File(absolutePath);
                    FileInputStream fis = new FileInputStream(file);
                    byte[] datosImagen = new byte[(int) file.length()];
                    fis.read(datosImagen);
                    fis.close();

                    usuario.setFoto_perfil(datosImagen);
                    usuario.setNombre_foto_perfil(file.getName());

                    String relativePath2 = "/albergue/images/portada_albergue_defecto.png";
                    String absolutePath2 = getServletContext().getRealPath(relativePath2);

                    File file2 = new File(absolutePath2);
                    FileInputStream fis2 = new FileInputStream(file2);
                    byte[] datosImagen2 = new byte[(int) file2.length()];
                    fis2.read(datosImagen2);
                    fis2.close();

                    usuario.setFoto_de_portada_albergue(datosImagen2);
                    usuario.setNombre_foto_de_portada(file2.getName());

                    registerDao.crearAlbergue(usuario);

                    //Redirige al formulario de registro de usuarios y muestra el modal de que recibira la aceptacion pronto

                    request.setAttribute("estado", "satisfactorio");
                    view = request.getRequestDispatcher("registro_albergue.jsp");
                    view.forward(request, response);

                }
                break;
            }
            default:
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action_parameter = request.getParameter("action");
        String action = action_parameter == null ? "loginForm" : action_parameter;

        switch (action) {

            case "usuarioFinal":

                //Redirige al formulario de registro de usuarios

                response.sendRedirect("registro_usuario.jsp");
                break;
            case "albergue":

                //Redirige al formulario de registro de albergues

                response.sendRedirect("registro_albergue.jsp");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
        }

    }

}
