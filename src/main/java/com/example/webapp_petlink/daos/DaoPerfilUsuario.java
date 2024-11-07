package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoPerfilUsuario extends DaoBase {

    public Usuario obtenerPerfilUsuario(int idUsuario) {
        Usuario usuario = null;
        String query = "SELECT u.*, d.nombre_distrito FROM Usuario u " +
                "JOIN Distrito d ON u.id_distrito = d.id_distrito " +
                "WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setDni(rs.getString("dni"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setCorreo_electronico(rs.getString("correo_electronico"));
                usuario.setDescripcion_perfil(rs.getString("descripcion_perfil"));

                Distrito distrito = new Distrito();
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                usuario.setDistrito(distrito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean modificarPerfilUsuario(Usuario usuario) {
        String query = "UPDATE Usuario SET dni = ?, nombres_usuario_final = ?, apellidos_usuario_final = ?, " +
                "direccion = ?, correo_electronico = ?, nombre_foto_perfil = ?, descripcion_perfil = ? " +
                "WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombres_usuario_final());
            stmt.setString(3, usuario.getApellidos_usuario_final());
            stmt.setString(4, usuario.getDireccion());
            stmt.setString(5, usuario.getCorreo_electronico());
            stmt.setString(6, usuario.getNombre_foto_perfil());
            stmt.setString(7, usuario.getDescripcion_perfil());
            stmt.setInt(8, usuario.getId_usuario());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPerfilUsuario(int idUsuario, String nombres, String apellidos, String direccion, String correo) {
        String query = "UPDATE Usuario SET nombres_usuario_final = ?, apellidos_usuario_final = ?, direccion = ?, correo_electronico = ? " +
                "WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombres);
            stmt.setString(2, apellidos);
            stmt.setString(3, direccion);
            stmt.setString(4, correo);
            stmt.setInt(5, idUsuario);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}