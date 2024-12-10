package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Zona;

import java.sql.*;
import java.util.ArrayList;

public class ListasAdminDao extends DaoBase{
    public ArrayList<Usuario> listasAlbergue(){
        ArrayList<Usuario> albergues = new ArrayList<>();

        String sql = "SELECT * FROM Usuario";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Usuario albergue = new Usuario();
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return albergues;
    }

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

    public ArrayList<Zona> obtenerZonas(){
        ArrayList<Zona> zonas = new ArrayList<>();

        String sql = "SELECT * FROM Zona";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Zona zona = new Zona();
                zona.setId_zona(rs.getInt("id_zona"));
                zona.setNombre_zona(rs.getString("nombre_zona"));
                zonas.add(zona);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return zonas;
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
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean guardarCoordinador(Usuario coordinador){
        String sql = "INSERT INTO Usuario (dni, id_rol, correo_electronico, es_usuario_activo, numero_yape_plin, nombres_coordinador, apellidos_coordinador, id_zona, fecha_nacimiento, fecha_hora_creacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, coordinador.getDni());
            ps.setInt(2,coordinador.getRol().getId_rol());
            ps.setString(3,coordinador.getCorreo_electronico());
            ps.setBoolean(4, coordinador.getEs_usuario_activo());
            ps.setString(5, coordinador.getNumero_yape_plin());
            ps.setString(6, coordinador.getNombres_coordinador());
            ps.setString(7, coordinador.getApellidos_coordinador());
            ps.setInt(8, coordinador.getZona().getId_zona());
            ps.setDate(9, java.sql.Date.valueOf(coordinador.getFecha_nacimiento())); // Fecha de nacimiento
            ps.setTimestamp(10, java.sql.Timestamp.valueOf(coordinador.getFecha_hora_creacion()));

            return ps.executeUpdate() > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCoordinador(Usuario coordinador){
        String sql = "UPDATE Usuario SET dni = ?, correo_electronico = ?, numero_yape_plin = ?, " +
                "nombres_coordinador = ?, apellidos_coordinador = ?, id_zona = ?, fecha_nacimiento = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, coordinador.getDni());

            ps.setString(2,coordinador.getCorreo_electronico());

            ps.setString(3, coordinador.getNumero_yape_plin());
            ps.setString(4, coordinador.getNombres_coordinador());
            ps.setString(5, coordinador.getApellidos_coordinador());
            ps.setInt(6, coordinador.getZona().getId_zona());
            ps.setDate(7, java.sql.Date.valueOf(coordinador.getFecha_nacimiento())); // Fecha de nacimiento

            return ps.executeUpdate()>0;

        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
