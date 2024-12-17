package com.example.webapp_petlink.servlets;
import com.example.webapp_petlink.beans.Correo;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.daos.CambiarPasswordDao;
import com.example.webapp_petlink.daos.LoginDao;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "CambiarPasswordServlet", urlPatterns = {"/CambiarPasswordServlet"})
public class CambiarPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action_parameter = request.getParameter("action");

        String action = action_parameter == null ? "loginForm" : action_parameter;

        switch (action) {

            case "recuperar":{

                String email = request.getParameter("email");

                if(email != null || !email.isEmpty()){

                    //se genera un token de identificacion universal
                    String token = UUID.randomUUID().toString();

                    //Se verifica que exista cuenta activa sin contrasenia temporal en la base de datos

                    CambiarPasswordDao cambiarPasswordDao = new CambiarPasswordDao();

                    Usuario usuario = new Usuario();

                    usuario = cambiarPasswordDao.obtenerUsuarioEmail(email);

                    int idUsuario = usuario.getId_usuario();
                    int idRol = usuario.getRol().getId_rol();

                    if(idUsuario > 0){

                        cambiarPasswordDao.actualizarTemporalToken(idUsuario, idRol, token);

                        //Se envia el correo de recuperacion con la contrasenia temporal si el correo es valido

                        Correo correo = new Correo();

                        String link_cambiar_contrasenia = "http://localhost:8080/WebApp_PetLink_war_exploded/cambiar_contrasenia.jsp"; //Luego se modificara por el link de la aplicacion desplegada en la nube

                        try {
                            correo.sendEmail(email,"Solicitud de recuperacion de contraseña - Petlink", "Estimado usuario, para recuperar su contraseña por favor utilice la contraseña temporal en el enlace adjunto. \n\n " + "Se adjunta su contraseña temporal: " + token + " \n\n Podrá modificarla en el siguiente enlace: " + link_cambiar_contrasenia);
                        }
                        catch (MessagingException e) {
                            System.out.println(e);
                        }

                        response.sendRedirect(request.getContextPath() + "/index.jsp"); //Retorna al inicio
                        break;
                    }
                    else{
                        response.sendRedirect(request.getContextPath() + "/recupera_contrasenia.jsp");
                        break;
                    }

                }

                response.sendRedirect(request.getContextPath() + "/recupera_contrasenia.jsp");

                break;
            }
            case "cambiar": {

                String email = request.getParameter("email");

                String password = request.getParameter("temp-password");

                CambiarPasswordDao cambiarPasswordDao = new CambiarPasswordDao();

                Usuario usuario = cambiarPasswordDao.obtenerUsuarioTemporal(email, password);

                if(usuario != null){ //Existe un usuario activo (aprobado) con contrasenia temporal o un usuario activo (aprobado) que esta intentando recuperar su contrasenia

                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuario);
                    session.setMaxInactiveInterval(60*10); //10 minutos de inactividad maxima durante la recuperacion de contrasenia

                    request.getRequestDispatcher("/confirmar_contrasenia.jsp").forward(request, response);

                }
                else{
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }

                break;
            }
            case "confirmar": {

                String new_pass = request.getParameter("new-password");
                String repeat_pass = request.getParameter("repeat-password");

                if(request.getSession().getAttribute("usuario") != null){ //Existe usuario en la sesion

                    if(new_pass.equals(repeat_pass)){ //Las contrasenias ingresadas hacen match

                        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

                        CambiarPasswordDao cambiarPasswordDao = new CambiarPasswordDao();
                        cambiarPasswordDao.actualizarContrasenia(usuario.getId_usuario(),new_pass);

                        //Se cierra la sesion y se retorna al login

                        HttpSession session = request.getSession();
                        session.invalidate();
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                    }
                    else{
                        request.getRequestDispatcher("/confirmar_contrasenia.jsp").forward(request, response);
                    }
                }
                else{
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }

                break;
            }
        }

    }

}
