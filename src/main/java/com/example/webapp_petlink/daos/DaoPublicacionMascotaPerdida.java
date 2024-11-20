package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.PublicacionMascotaPerdida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoPublicacionMascotaPerdida extends DaoBase {

    // Método para obtener todas las publicaciones con soporte de paginación y búsqueda
    public List<PublicacionMascotaPerdida> obtenerPublicaciones(int pagina, int registrosPorPagina, String criterioBusqueda) {
        List<PublicacionMascotaPerdida> publicaciones = new ArrayList<>();
        String query = "SELECT * FROM PublicacionMascotaPerdida WHERE CONCAT(nombre, celularContacto) LIKE ? " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + criterioBusqueda + "%");
            stmt.setInt(2, registrosPorPagina);
            stmt.setInt(3, (pagina - 1) * registrosPorPagina);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PublicacionMascotaPerdida publicacion = new PublicacionMascotaPerdida();
                publicacion.setIdPublicacionMascotaPerdida(rs.getInt("idPublicacionMascotaPerdida"));
                publicacion.setNombre(rs.getString("nombre"));
                publicacion.setCelularContacto(rs.getString("celularContacto"));
                publicacion.setFechaPerdida(rs.getString("fechaPerdida"));
                publicacion.setFechaHoraRegistro(rs.getString("fechaHoraRegistro"));
                publicaciones.add(publicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicaciones;
    }

    // Método para actualizar el estado de una publicación
    public boolean actualizarEstadoPublicacion(int idPublicacion, String estado) {
        String query = "UPDATE PublicacionMascotaPerdida SET estado = ? WHERE idPublicacionMascotaPerdida = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, estado);
            stmt.setInt(2, idPublicacion);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener detalles de una publicación específica
    public PublicacionMascotaPerdida obtenerDetallesPublicacion(int idPublicacion) {
        PublicacionMascotaPerdida publicacion = null;
        String query = "SELECT * FROM PublicacionMascotaPerdida WHERE idPublicacionMascotaPerdida = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPublicacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                publicacion = new PublicacionMascotaPerdida();
                publicacion.setIdPublicacionMascotaPerdida(rs.getInt("idPublicacionMascotaPerdida"));
                publicacion.setNombre(rs.getString("nombre"));
                publicacion.setCelularContacto(rs.getString("celularContacto"));
                publicacion.setFechaPerdida(rs.getString("fechaPerdida"));
                publicacion.setFechaHoraRegistro(rs.getString("fechaHoraRegistro"));
                publicacion.setDescripcionMascota(rs.getString("descripcionMascota"));
                publicacion.setFueEncontrada(rs.getBoolean("fueEncontrada"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicacion;
    }
}