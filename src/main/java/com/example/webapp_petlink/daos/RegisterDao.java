package com.example.webapp_petlink.daos;
import com.example.webapp_petlink.beans.Usuario;
import java.sql.*;
import java.util.Random;
import java.time.LocalDateTime;


public class RegisterDao extends DaoBase{

    //Obtener el id de zona a partir de un distrito

    public int obtenerIdZona(int idDistrito){

        int id_zona = 1;

        String sql = "SELECT id_zona from mydb.Distrito where id_distrito = ?";

        try (Connection conn = getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDistrito);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { // Verificar si el ResultSet tiene datos
                    id_zona = rs.getInt("id_zona");
                } else {
                    System.out.println("No se encontró la zona para el distrito con ID: " + idDistrito);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_zona;
    }


    //Valida que no exista cuenta con el mismo correo o el mismo DNI

    public boolean existeUsuario(String dni, String correo){

        String sql = "SELECT u.dni, u.correo_electronico from mydb.Usuario u where es_usuario_activo = 1 and (dni = ? or correo_electronico = ?)";

        boolean existe = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setString(1, dni);
             stmt.setString(2, correo);

             ResultSet rs = stmt.executeQuery();

             if(rs.next()){
                 existe = true;
             }
             else{
                 existe = false;
             }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    //Crea nuevo usuario y contrasenia temporal

    public void crearUsuario(Usuario usuario){

        //Crear contrasenia

        String contrasenia = generarContrasenia(8);

        LoginDao loginDao = new LoginDao();

        String contrasenia_hashed = loginDao.hashString(contrasenia, "SHA-256");

        //Obtener el tiempo actual de creacion

        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Timestamp hora_creacion = Timestamp.valueOf(fechaHoraActual);

        //Consulta SQL

        String sql = "INSERT INTO mydb.Usuario(dni,nombres_usuario_final, apellidos_usuario_final, direccion, id_rol, correo_electronico, id_distrito, id_zona, contrasenia, contrasenia_hashed, es_contrasenia_temporal, es_primera_contrasenia_temporal, es_usuario_activo, fecha_hora_creacion, foto_perfil, nombre_foto_perfil) " +
                "VALUES(?,?,?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombres_usuario_final());
            stmt.setString(3, usuario.getApellidos_usuario_final());
            stmt.setString(4, usuario.getDireccion());
            stmt.setInt(5, usuario.getRol().getId_rol());
            stmt.setString(6, usuario.getCorreo_electronico());
            stmt.setInt(7,usuario.getDistrito().getId_distrito());
            stmt.setInt(8,usuario.getZona().getId_zona());
            stmt.setString(9,contrasenia);
            stmt.setString(10,contrasenia_hashed);
            stmt.setBoolean(11, true);
            stmt.setBoolean(12, true);
            stmt.setBoolean(13, false);
            stmt.setTimestamp(14, hora_creacion);
            stmt.setBytes(15, usuario.getFoto_perfil());
            stmt.setString(16,usuario.getNombre_foto_perfil());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Codigo para crear contrasenias temporales

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

    //Valida que no exista cuenta con el mismo correo o el mismo DNI

    public boolean existeAlbergue(String url, String correo){

        String sql = "SELECT u.url_instagram, u.correo_electronico from mydb.Usuario u where es_usuario_activo = 1 and (url_instagram = ? or correo_electronico = ?)";

        boolean existe = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, url);
            stmt.setString(2, correo);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                existe = true;
            }
            else{
                existe = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    //Crea nuevo albergue y contrasenia temporal

    public void crearAlbergue(Usuario usuario){

        //Crear contrasenia

        String contrasenia = generarContrasenia(8);

        LoginDao loginDao = new LoginDao();

        String contrasenia_hashed = loginDao.hashString(contrasenia, "SHA-256");

        //Obtener el tiempo actual de creacion

        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Timestamp hora_creacion = Timestamp.valueOf(fechaHoraActual);

        //Consulta SQL

        String sql = "INSERT INTO mydb.Usuario(url_instagram, nombres_encargado, apellidos_encargado, nombre_albergue, id_rol, correo_electronico, contrasenia, contrasenia_hashed, es_contrasenia_temporal, es_primera_contrasenia_temporal, fecha_hora_creacion, foto_perfil, nombre_foto_perfil, foto_de_portada_albergue, nombre_foto_de_portada) " +
                "VALUES(?,?,?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getUrl_instagram());
            stmt.setString(2, usuario.getNombres_encargado());
            stmt.setString(3, usuario.getApellidos_encargado());
            stmt.setString(4, usuario.getNombre_albergue());
            stmt.setInt(5, usuario.getRol().getId_rol());
            stmt.setString(6, usuario.getCorreo_electronico());
            stmt.setString(7,contrasenia);
            stmt.setString(8,contrasenia_hashed);
            stmt.setBoolean(9, true);
            stmt.setBoolean(10, true);
            stmt.setTimestamp(11, hora_creacion);
            stmt.setBytes(12, usuario.getFoto_perfil());
            stmt.setString(13,usuario.getNombre_foto_perfil());
            stmt.setBytes(14, usuario.getFoto_de_portada_albergue());
            stmt.setString(15,usuario.getNombre_foto_de_portada());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
