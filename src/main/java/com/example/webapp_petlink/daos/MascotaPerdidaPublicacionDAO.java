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

public class MascotaPerdidaPublicacionDAO extends DaoBase {

    /**
     * Método para listar todas las publicaciones de mascotas perdidas.
     *
     * @return Una lista de objetos PublicacionMascotaPerdida.
     */
    public List<PublicacionMascotaPerdida> listarPublicaciones() {
        List<PublicacionMascotaPerdida> publicaciones = new ArrayList<>();

        String query = "SELECT p.id_publicacion_mascota_perdida, p.nombre, p.descripcion_mascota, " +
                "p.lugar_perdida, p.fecha_perdida, p.celular_contacto, p.nombre_contacto, " +
                "e.nombre_estado, u.dni, " +
                "CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombre_completo " +
                "FROM PublicacionMascotaPerdida p " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "JOIN Estado e ON p.id_estado = e.id_estado";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PublicacionMascotaPerdida publicacion = new PublicacionMascotaPerdida();

                publicacion.setIdPublicacionMascotaPerdida(rs.getInt("id_publicacion_mascota_perdida"));
                publicacion.setNombre(rs.getString("nombre"));
                publicacion.setDescripcionMascota(rs.getString("descripcion_mascota"));
                publicacion.setLugarPerdida(rs.getString("lugar_perdida"));
                publicacion.setFechaPerdida(rs.getString("fecha_perdida"));
                publicacion.setCelularContacto(rs.getString("celular_contacto"));
                publicacion.setNombreContacto(rs.getString("nombre_contacto"));

                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                publicacion.setEstado(estado);

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
     * Método para obtener los detalles de una publicación específica.
     *
     * @param idPublicacion El ID de la publicación.
     * @return Un objeto PublicacionMascotaPerdida con los detalles.
     */
    public PublicacionMascotaPerdida obtenerDetallesPublicacion(int idPublicacion) {
        PublicacionMascotaPerdida publicacion = null;

        String query = "SELECT p.nombre, p.descripcion_mascota, p.lugar_perdida, p.fecha_perdida, " +
                "p.celular_contacto, p.nombre_contacto, p.recompensa, e.nombre_estado, " +
                "CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombre_completo " +
                "FROM PublicacionMascotaPerdida p " +
                "JOIN Usuario u ON p.id_usuario = u.id_usuario " +
                "JOIN Estado e ON p.id_estado = e.id_estado " +
                "WHERE p.id_publicacion_mascota_perdida = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPublicacion);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    publicacion = new PublicacionMascotaPerdida();
                    publicacion.setNombre(rs.getString("nombre"));
                    publicacion.setDescripcionMascota(rs.getString("descripcion_mascota"));
                    publicacion.setLugarPerdida(rs.getString("lugar_perdida"));
                    publicacion.setFechaPerdida(rs.getString("fecha_perdida"));
                    publicacion.setCelularContacto(rs.getString("celular_contacto"));
                    publicacion.setNombreContacto(rs.getString("nombre_contacto"));
                    publicacion.setRecompensa(rs.getInt("recompensa"));

                    Estado estado = new Estado();
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                    publicacion.setEstado(estado);

                    Usuario usuario = new Usuario();
                    usuario.setNombres_usuario_final(rs.getString("nombre_completo"));
                    publicacion.setUsuario(usuario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicacion;
    }

    /**
     * Método para actualizar el estado de una publicación.
     *
     * @param idPublicacion El ID de la publicación.
     * @param idEstado El nuevo estado de la publicación.
     * @return True si la operación fue exitosa, de lo contrario False.
     */
    public boolean actualizarEstadoPublicacion(int idPublicacion, int idEstado) {
        String query = "UPDATE PublicacionMascotaPerdida SET id_estado = ? WHERE id_publicacion_mascota_perdida = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idEstado);
            stmt.setInt(2, idPublicacion);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
