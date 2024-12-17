package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.PublicacionMascotaPerdida;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicacionMascotaPerdidaUsuarioDAO extends DaoBase {

    /**
     * Método para listar las publicaciones de mascotas perdidas realizadas por usuarios.
     *
     * @return Una lista de objetos PublicacionMascotaPerdida.
     */
    public List<PublicacionMascotaPerdida> listarPublicacionesDeUsuarios() {
        List<PublicacionMascotaPerdida> publicaciones = new ArrayList<>();
        String query = "SELECT p.id_publicacion, p.fecha_hora_registro, p.fecha_perdida, u.dni, " +
                "CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombre_completo, " +
                "u.numero_contacto_donaciones " +
                "FROM PublicacionMascotaPerdida p " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PublicacionMascotaPerdida publicacion = new PublicacionMascotaPerdida();
                publicacion.setIdPublicacionMascotaPerdida(rs.getInt("id_publicacion"));
                publicacion.setFechaHoraRegistro(rs.getString("fecha_hora_registro"));
                publicacion.setFechaPerdida(rs.getString("fecha_perdida"));

                Usuario usuario = new Usuario();
                usuario.setDni(rs.getString("dni"));
                usuario.setNombres_usuario_final(rs.getString("nombre_completo"));
                usuario.setNumero_contacto_donaciones(rs.getString("numero_contacto_donaciones"));
                publicacion.setUsuario(usuario);

                publicaciones.add(publicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicaciones;
    }

    /**
     * Método para actualizar el estado de una publicación específica.
     *
     * @param idPublicacion El ID de la publicación.
     * @param estado        El nuevo estado de la publicación.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarEstadoPublicacion(int idPublicacion, String estado) {
        String query = "UPDATE PublicacionMascotaPerdida SET estado = ? WHERE id_publicacion = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, estado);
            stmt.setInt(2, idPublicacion);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para obtener los detalles de una publicación específica.
     *
     * @param idPublicacion El ID de la publicación a consultar.
     * @return Un objeto PublicacionMascotaPerdida con los detalles.
     */
    public PublicacionMascotaPerdida obtenerDetallesPublicacion(int idPublicacion) {
        PublicacionMascotaPerdida publicacion = null;

        String query = "SELECT p.descripcion_mascota, p.lugar_perdida, p.fecha_perdida, " +
                "p.nombre_contacto, p.celular_contacto, p.recompensa, u.dni, " +
                "CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombre_completo " +
                "FROM PublicacionMascotaPerdida p " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.id_publicacion = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPublicacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                publicacion = new PublicacionMascotaPerdida();
                publicacion.setDescripcionMascota(rs.getString("descripcion_mascota"));
                publicacion.setLugarPerdida(rs.getString("lugar_perdida"));
                publicacion.setFechaPerdida(rs.getString("fecha_perdida"));
                publicacion.setNombreContacto(rs.getString("nombre_contacto"));
                publicacion.setCelularContacto(rs.getString("celular_contacto"));
                publicacion.setRecompensa(rs.getInt("recompensa"));

                Usuario usuario = new Usuario();
                usuario.setDni(rs.getString("dni"));
                usuario.setNombres_usuario_final(rs.getString("nombre_completo"));
                publicacion.setUsuario(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicacion;
    }
}
