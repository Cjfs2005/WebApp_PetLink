package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.PublicacionMascotaPerdida;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Estado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublicacionMascotaPerdidaDuenioDAO extends DaoBase {

    /**
     * Método para obtener todas las publicaciones de mascotas perdidas creadas por dueños.
     *
     * @return Una lista de objetos PublicacionMascotaPerdida.
     */
    public List<PublicacionMascotaPerdida> listarPublicacionesDueños() {
        List<PublicacionMascotaPerdida> publicaciones = new ArrayList<>();

        // Consulta SQL para obtener publicaciones de mascotas perdidas creadas por dueños
        String query = "SELECT p.id_publicacion_mascota_perdida, p.descripcion_mascota, p.lugar_perdida, " +
                "p.fecha_perdida, p.celular_contacto, p.nombre_contacto, e.nombre_estado, u.dni, " +
                "CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombre_completo " +
                "FROM PublicacionMascotaPerdida p " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "JOIN Estado e ON p.id_estado = e.id_estado " +
                "WHERE p.id_tipo_publicacion_mascota_perdida = 1"; // Tipo 1: publicaciones del dueño

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Procesar el resultado de la consulta
            while (rs.next()) {
                PublicacionMascotaPerdida publicacion = new PublicacionMascotaPerdida();

                // Asignar valores a los atributos de PublicacionMascotaPerdida
                publicacion.setIdPublicacionMascotaPerdida(rs.getInt("id_publicacion_mascota_perdida"));
                publicacion.setDescripcionMascota(rs.getString("descripcion_mascota"));
                publicacion.setLugarPerdida(rs.getString("lugar_perdida"));
                publicacion.setFechaPerdida(rs.getString("fecha_perdida"));
                publicacion.setCelularContacto(rs.getString("celular_contacto"));
                publicacion.setNombreContacto(rs.getString("nombre_contacto"));

                // Asignar estado
                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                publicacion.setEstado(estado);

                // Asignar usuario
                Usuario usuario = new Usuario();
                usuario.setDni(rs.getString("dni"));
                usuario.setNombres_usuario_final(rs.getString("nombre_completo"));
                publicacion.setUsuario(usuario);

                publicaciones.add(publicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicaciones;
    }

    /**
     * Método para actualizar el estado de una publicación.
     *
     * @param idPublicacion El ID de la publicación a actualizar.
     * @param idEstado El nuevo estado de la publicación.
     * @return True si la operación fue exitosa, de lo contrario False.
     */
    public boolean actualizarEstadoPublicacion(int idPublicacion, int idEstado) {
        String query = "UPDATE PublicacionMascotaPerdida SET id_estado = ? WHERE id_publicacion_mascota_perdida = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idEstado); // Nuevo estado
            stmt.setInt(2, idPublicacion); // ID de la publicación

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

        // Consulta SQL para obtener detalles de la publicación
        String query = "SELECT p.descripcion_mascota, p.lugar_perdida, p.fecha_perdida, " +
                "p.nombre_contacto, p.celular_contacto, p.recompensa, u.dni, " +
                "CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombre_completo " +
                "FROM PublicacionMascotaPerdida p " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "WHERE p.id_publicacion_mascota_perdida = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPublicacion); // Establecer el ID de la publicación
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
