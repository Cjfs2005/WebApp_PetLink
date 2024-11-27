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
}
