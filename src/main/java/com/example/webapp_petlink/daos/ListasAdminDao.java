package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import jakarta.mail.MessagingException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class ListasAdminDao extends DaoBase{

    // SECCIÓN DE ALBERGUES
    public ArrayList<Usuario> listasAlbergueSinPrimerRegistro(){
        ArrayList<Usuario> albergues = new ArrayList<>();

        String sql = "SELECT * FROM Usuario WHERE es_usuario_activo IS NULL AND id_rol = 2 ORDER BY fecha_hora_creacion ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Usuario albergue = new Usuario();
                albergue.setId_usuario(rs.getInt("id_usuario"));
                albergue.setNombre_albergue(rs.getString("nombre_albergue"));
                albergue.setFecha_hora_creacion(rs.getObject("fecha_hora_creacion", java.time.LocalDateTime.class));
                albergues.add(albergue);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return albergues;
    }

    public Usuario obtenerAlberguePorId(int id){
        Usuario albergue = null;
        String sql = "SELECT id_usuario, nombre_albergue, nombres_encargado, apellidos_encargado, correo_electronico, url_instagram " +
                "FROM Usuario u WHERE u.id_usuario = ? AND u.es_usuario_activo IS NULL";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    albergue = new Usuario();
                    albergue.setId_usuario(rs.getInt("id_usuario"));
                    albergue.setNombre_albergue(rs.getString("nombre_albergue"));
                    albergue.setNombres_encargado(rs.getString("nombres_encargado"));
                    albergue.setApellidos_encargado(rs.getString("apellidos_encargado"));
                    albergue.setCorreo_electronico(rs.getString("correo_electronico"));
                    albergue.setUrl_instagram(rs.getString("url_instagram"));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return albergue;
    }

    public void aceptarAlbergue(Usuario albergue) {
        String sql = "UPDATE Usuario SET es_usuario_activo = 1, es_primera_contrasenia_temporal = 1, fecha_hora_creacion = ?, contrasenia = ?, contrasenia_hashed = ? WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (albergue.getFecha_hora_creacion() != null) {
                ps.setTimestamp(1, java.sql.Timestamp.valueOf(albergue.getFecha_hora_creacion()));
            } else {
                // Si es null, asignamos la fecha y hora actual
                ps.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            }
            ps.setString(2, albergue.getContrasenia());

            ps.setString(3, albergue.getContrasenia_hashed());
            ps.setInt(4, albergue.getId_usuario());

            // Vamos a obtener su correo
            String correoAlbergue = obtenerCorreo(albergue.getId_usuario());

            ps.executeUpdate();

            String link_cambiar_contrasenia = "http://35.243.146.148:8080/WebApp_PetLink-1.0-SNAPSHOT/cambiar_contrasenia.jsp"; //Luego se modificara por el link de la aplicacion desplegada en la nube

            Correo correo = new Correo();

            try {
                correo.sendEmail(correoAlbergue,"Registro aceptado en Petlink", "Tras validar los datos de su registro, su albergue ha sido aceptado en Petlink. \n\n " + "Se adjunta su contraseña temporal: " + albergue.getContrasenia() + ". \n\n Podrá modificarla en el siguiente enlace: " + link_cambiar_contrasenia);
            }
            catch (MessagingException e) {
                System.out.println(e);
            }
            System.out.println("Se ha aceptado satisfactoriamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rechazarAlbergue(int idAlbergue) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAlbergue);
            String correoAlbergue = obtenerCorreo(idAlbergue);
            ps.executeUpdate();
            Correo correo = new Correo();
            try {
                correo.sendEmail(correoAlbergue,"Registro rechazado en Petlink", "Tras validar los datos de su registro, lamentamos informarle que no ha sido aceptado en Petlink.\n\n" + "Le pedimos encarecidamente que revise sus datos y realice el registro otra vez. \n\n" + "Agradecemos su interés en formar parte de Petlink.");
            }
            catch (MessagingException e) {
                System.out.println(e);
            }
            System.out.println("Se ha rechazado satisfactoriamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String obtenerCorreo(int id) {
        String sql = "SELECT correo_electronico FROM Usuario WHERE id_usuario = ?";
        String correo = null; // Variable para almacenar el correo

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Establecer el parámetro del PreparedStatement
            ps.setInt(1, id);

            // Ejecutar la consulta y obtener el ResultSet
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si se encuentra el correo, lo obtenemos del ResultSet
                    correo = rs.getString("correo_electronico");
                }
            }

        } catch (SQLException e) {
            // Manejo de excepciones
            e.printStackTrace();
        }

        // Devolver el correo (o null si no se encontró)
        return correo;
    }

// ALBERGUES QUE YA HAN COMPLETADO TODO SU REGISTRO
    public ArrayList<Usuario> listarAlbergueConRegistroCompleto(){
        ArrayList<Usuario> albergues = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre_albergue, u.id_distrito, d.nombre_distrito, u.fecha_hora_creacion " +
                "FROM Usuario u " +
                "JOIN Distrito d ON u.id_distrito = d.id_distrito " +
                "WHERE u.id_rol = 2 AND u.tiene_registro_completo = 1 AND es_usuario_activo = 1 ORDER BY fecha_hora_creacion ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Usuario albergue = new Usuario();
                albergue.setId_usuario(rs.getInt("id_usuario"));
                albergue.setNombre_albergue(rs.getString("nombre_albergue"));

                Distrito distrito = new Distrito();
                distrito.setId_distrito(rs.getInt("id_distrito"));
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));

                albergue.setDistrito(distrito);

                albergue.setFecha_hora_creacion(rs.getObject("fecha_hora_creacion", java.time.LocalDateTime.class));
                albergues.add(albergue);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return albergues;
    }

    public void eliminarAlbergue(int id){
        String sql ="UPDATE Usuario SET es_usuario_activo = 0 WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

            String correoAlbergue = obtenerCorreo(id);
            Correo correo = new Correo();

            System.out.println("--SE VA A ENVIAR AL CORREO "+ correoAlbergue);

            try {
                correo.sendEmail(correoAlbergue,"Eliminación de cuenta en Petlink", "Hemos visto que incumple con nuestras normas políticas, por dicha razón su cuenta de PetLink ha sido eliminada.\n\n" + "Si desea apelar, póngase en contacto con nosotros");
            }
            catch (MessagingException e) {
                System.out.println(e);
            }
            System.out.println("Se ha rechazado satisfactoriamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // SECCION DE COORDINADORES ZONALES

    public ArrayList<Usuario> listasCoodinadores(){

        ArrayList<Usuario> coordinadores = new ArrayList<>();

        String sql = "SELECT * FROM Usuario u JOIN Zona z on u.id_zona = z.id_zona  WHERE id_rol = 3";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Usuario coordinador = new Usuario();
                coordinador.setId_usuario(rs.getInt("id_usuario"));
                coordinador.setNombres_coordinador(rs.getString("nombres_coordinador"));
                coordinador.setApellidos_coordinador(rs.getString("apellidos_coordinador"));
                coordinador.setCorreo_electronico(rs.getString("correo_electronico"));
                coordinador.setDni(rs.getString("dni"));
                coordinador.setFecha_hora_creacion(rs.getObject("fecha_hora_creacion", java.time.LocalDateTime.class)); // Asignando fecha de creación

                Zona zona = new Zona();
                zona.setId_zona(rs.getInt("id_zona"));
                zona.setNombre_zona(rs.getString("nombre_zona"));

                coordinador.setZona(zona);

                coordinadores.add(coordinador);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return coordinadores;
    }

    public ArrayList<Zona> obtenerZonasSinCoordinador() {
        ArrayList<Zona> zonasSinCoordinador = new ArrayList<>();

        // SQL para obtener zonas que no tienen coordinador asignado
        String sql = "SELECT * FROM Zona z " +
                "LEFT JOIN Usuario u ON z.id_zona = u.id_zona AND u.id_rol = 3 " +  // Asumiendo que id_rol = 3 es para coordinadores
                "WHERE u.id_usuario IS NULL";  // Filtrar las zonas que no tienen coordinador

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Zona zona = new Zona();
                zona.setId_zona(rs.getInt("id_zona"));
                zona.setNombre_zona(rs.getString("nombre_zona"));
                zonasSinCoordinador.add(zona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zonasSinCoordinador;
    }

    public Usuario obtenerCoordinadorPorId(int id){
        Usuario coordinador = null;

        // query para obtener la informacion para el coordinador
        String sql = "SELECT * FROM Usuario u JOIN Zona z ON u.id_zona = z.id_zona WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    coordinador = new Usuario();
                    coordinador.setId_usuario(rs.getInt("id_usuario"));
                    coordinador.setNombres_coordinador(rs.getString("nombres_coordinador"));
                    coordinador.setApellidos_coordinador(rs.getString("apellidos_coordinador"));
                    coordinador.setCorreo_electronico(rs.getString("correo_electronico"));
                    coordinador.setDni(rs.getString("dni"));
                    coordinador.setNumero_yape_plin(rs.getString("numero_yape_plin"));

                    java.sql.Date sqlDate = rs.getDate("fecha_nacimiento");
                    if (sqlDate != null) {
                        coordinador.setFecha_nacimiento(sqlDate.toLocalDate()); // Conversión a LocalDate
                    }

                    Zona zona = new Zona();
                    zona.setId_zona(rs.getInt("id_zona"));
                    zona.setNombre_zona(rs.getString("nombre_zona"));
                    coordinador.setZona(zona);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return coordinador;
    }

    public void eliminarCoordinador(int id){
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            String correo_electronico = obtenerCorreo(id);
            Correo correo = new Correo();

            try {
                correo.sendEmail(correo_electronico,"Cuenta eliminada en Petlink", "Le comentamos que usted ha sido relegado del cargo de coordinador zonal.\n\n" + "Le agradecemos por el tiempo servido para el albergue PetLink");
            }
            catch (MessagingException e) {
                System.out.println(e);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean guardarCoordinador(Usuario coordinador) {
        String sql = "INSERT INTO Usuario (dni, id_rol, correo_electronico, es_usuario_activo, numero_yape_plin, nombres_coordinador, apellidos_coordinador, id_zona, fecha_nacimiento, fecha_hora_creacion, contrasenia, contrasenia_hashed, es_contrasenia_temporal, es_primera_contrasenia_temporal) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 ,0)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Asignar los parámetros del PreparedStatement
            ps.setString(1, coordinador.getDni());
            ps.setInt(2, coordinador.getRol().getId_rol());
            ps.setString(3, coordinador.getCorreo_electronico());
            ps.setBoolean(4, coordinador.getEs_usuario_activo());
            ps.setString(5, coordinador.getNumero_yape_plin());
            ps.setString(6, coordinador.getNombres_coordinador());
            ps.setString(7, coordinador.getApellidos_coordinador());
            ps.setInt(8, coordinador.getZona().getId_zona());
            ps.setDate(9, java.sql.Date.valueOf(coordinador.getFecha_nacimiento()));
            ps.setTimestamp(10, java.sql.Timestamp.valueOf(coordinador.getFecha_hora_creacion()));
            ps.setString(11, coordinador.getContrasenia());
            ps.setString(12, coordinador.getContrasenia_hashed());

            // Ejecutar el INSERT para guardar al coordinador
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Recuperar la clave generada (ID del coordinador insertado)
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idCoordinador = generatedKeys.getInt(1);  // Obtener el ID del nuevo coordinador

                        // Ahora que el coordinador está insertado, podemos obtener el nombre de la zona
                        String nombreZona = obtenerNombreZona(coordinador.getZona().getId_zona());

                        // Enviar el correo con el nombre de la zona
                        Correo correo = new Correo();
                        String link_cambiar_contrasenia = "http://35.243.146.148:8080/WebApp_PetLink-1.0-SNAPSHOT/cambiar_contrasenia.jsp"; // Link de cambio de contraseña

                        try {
                            correo.sendEmail(coordinador.getCorreo_electronico(),
                                    "Cuenta creada como coordinador zonal en Petlink",
                                    "Se ha creado su cuenta como coordinador de la zona " + nombreZona + "\n\n" +
                                            "Se adjunta su contraseña temporal: " + coordinador.getContrasenia() +
                                            " \n\n Podrá modificarla en el siguiente enlace: " + link_cambiar_contrasenia);
                        } catch (MessagingException e) {
                            System.out.println("Error al enviar correo: " + e.getMessage());
                        }

                        return true; // El proceso fue exitoso
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Si ocurrió un error en el proceso
    }

    // Método para obtener el nombre de la zona
    public String obtenerNombreZona(int idZona) {
        String sql = "SELECT nombre_zona FROM Zona WHERE id_zona = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idZona);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre_zona");  // Obtener el nombre de la zona
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Zona no asignada";  // En caso de error o si la zona no existe
    }

    public boolean actualizarCoordinador(Usuario coordinador){
        String sql = "UPDATE Usuario SET dni = ?, correo_electronico = ?, numero_yape_plin = ?, " +
                "nombres_coordinador = ?, apellidos_coordinador = ?, id_zona = ?, fecha_nacimiento = ? WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, coordinador.getDni());

            ps.setString(2,coordinador.getCorreo_electronico());

            ps.setString(3, coordinador.getNumero_yape_plin());
            ps.setString(4, coordinador.getNombres_coordinador());
            ps.setString(5, coordinador.getApellidos_coordinador());
            ps.setInt(6, coordinador.getZona().getId_zona());
            ps.setDate(7, java.sql.Date.valueOf(coordinador.getFecha_nacimiento())); // Fecha de nacimiento
            ps.setInt(8, coordinador.getId_usuario());

            return ps.executeUpdate()>0;

        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public String generarContrasenia(int length){

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";

        Random random = new Random();

        char[] password = new char[length];

        for (int i = 0; i<length ; i++){
            password[i] = characters.charAt(random.nextInt(characters.length()));
        }

        String password_string = new String(password);


        return password_string;

    }

    public ArrayList<Usuario> listaUsuarios(){

        ArrayList<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT u.id_usuario, d.nombre_distrito, u.nombres_usuario_final, u.apellidos_usuario_final, u.direccion, u.fecha_hora_creacion " +
                "FROM mydb.Usuario u JOIN mydb.Distrito d ON d.id_distrito = u.id_distrito WHERE u.es_primera_contrasenia_temporal = ? " +
                "AND u.es_usuario_activo = ? AND u.id_rol = ? ORDER BY u.fecha_hora_creacion DESC";

        int es_primera_contrasenia_temporal = 1;
        int es_usuario_activo = 0;
        int id_rol = 1;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setInt(1, es_primera_contrasenia_temporal);
            ps.setInt(2, es_usuario_activo);
            ps.setInt(3, id_rol);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));

                Distrito distrito = new Distrito();
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));

                usuario.setDistrito(distrito);

                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setFecha_hora_creacion(rs.getObject("fecha_hora_creacion", java.time.LocalDateTime.class));

                usuarios.add(usuario);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return usuarios;
    }

    public Usuario existeUsuario(int id_usuario){

        Usuario usr = new Usuario();

        String sql = "SELECT * from mydb.Usuario where id_usuario = ? and es_usuario_activo = 0 and es_primera_contrasenia_temporal = 1;";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setInt(1, id_usuario);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                usr.setId_usuario(rs.getInt("id_usuario"));
                usr.setCorreo_electronico(rs.getString("correo_electronico"));
                usr.setContrasenia(rs.getString("contrasenia"));
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return usr;
    }

    public void aceptarUsuario(Usuario usuario){

        Correo correo = new Correo();

        String sql = "UPDATE mydb.Usuario SET es_usuario_activo = 1 WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {

            ps.setInt(1, usuario.getId_usuario());

            ps.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        String link_cambiar_contrasenia = "http://35.243.146.148:8080/WebApp_PetLink-1.0-SNAPSHOT/cambiar_contrasenia.jsp"; //Luego se modificara por el link de la aplicacion desplegada en la nube

        try {
            correo.sendEmail(usuario.getCorreo_electronico(),"Registro aceptado en Petlink", "Tras validar los datos de su registro, usted ha sido aceptado en Petlink. \n\n " + "Se adjunta su contraseña temporal: " + usuario.getContrasenia() + " \n\n Podrá modificarla en el siguiente enlace: " + link_cambiar_contrasenia);
        }
        catch (MessagingException e) {
            System.out.println(e);
        }
    }

    public void rechazarUsuario(Usuario usuario){

        eliminarCoordinador(usuario.getId_usuario());

        Correo correo = new Correo();

        try {
            correo.sendEmail(usuario.getCorreo_electronico(),"Registro rechazado en Petlink", "Tras validar los datos de su registro, lamentamos informarle que no ha sido aceptado en Petlink.\n\n" + "Le pedimos encarecidamente que revise sus datos y realice el registro otra vez. \n\n" + "Agradecemos su interés en formar parte de Petlink.");
        }
        catch (MessagingException e) {
            System.out.println(e);
        }
    }

    // Para los lugares de los eventos
    public ArrayList<LugarEvento> listarLugares(){
        ArrayList<LugarEvento> lugares = new ArrayList<>();

        String sql = "SELECT * FROM LugarEvento l JOIN Distrito d WHERE l.id_distrito = d.id_distrito";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                LugarEvento lugar = new LugarEvento();
                lugar.setId_lugar_evento(rs.getInt("id_lugar_evento"));
                lugar.setNombre_lugar_evento(rs.getString("nombre_lugar_evento"));
                lugar.setAforo_maximo(rs.getInt("aforo_maximo"));
                lugar.setDireccion_lugar_evento(rs.getString("direccion_lugar_evento"));

                Distrito distrito = new Distrito();
                distrito.setId_distrito(rs.getInt("id_distrito"));
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                lugar.setDistrito(distrito);

                lugares.add(lugar);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return lugares;
    }

    public ArrayList<Distrito> obtenerDistritos(){
        ArrayList<Distrito> distritos = new ArrayList<>();

        String sql = "SELECT * FROM Distrito";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Distrito distrito = new Distrito();
                distrito.setId_distrito(rs.getInt("id_distrito"));
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                distritos.add(distrito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distritos;

    }

    public boolean guardarLugar(LugarEvento lugar) {
        String sql = "INSERT INTO LugarEvento (nombre_lugar_evento, direccion_lugar_evento, aforo_maximo, id_distrito) \n" +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, lugar.getNombre_lugar_evento());
            ps.setString(2, lugar.getDireccion_lugar_evento());
            ps.setInt(3, lugar.getAforo_maximo());
            ps.setInt(4, lugar.getDistrito().getId_distrito());
            return ps.executeUpdate()>0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //LIstar a los usuarios que ya están registrados
    public ArrayList<Usuario> listarUsuariosConRegistroCompleto(){
        ArrayList<Usuario> users = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombres_usuario_final, u.apellidos_usuario_final, u.id_distrito, d.nombre_distrito, u.fecha_hora_creacion " +
                "FROM Usuario u " +
                "JOIN Distrito d ON u.id_distrito = d.id_distrito " +
                "WHERE u.id_rol = 1 AND u.es_usuario_activo = 1 AND u.es_primera_contrasenia_temporal = 0 ORDER BY fecha_hora_creacion ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Usuario user = new Usuario();
                user.setId_usuario(rs.getInt("id_usuario"));
                user.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                user.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));

                Distrito distrito = new Distrito();
                distrito.setId_distrito(rs.getInt("id_distrito"));
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));

                user.setDistrito(distrito);

                user.setFecha_hora_creacion(rs.getObject("fecha_hora_creacion", java.time.LocalDateTime.class));
                users.add(user);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
}
