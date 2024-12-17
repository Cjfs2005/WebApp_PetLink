package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListaAlberguesDao extends DaoBase {

    public List<Usuario> listarAlbergues(){
        List<Usuario> listaAlbergues = new ArrayList<Usuario>();

        String sql = "SELECT id_usuario as ID_Albergue, nombre_albergue AS Nombre_Albergue, foto_de_portada_albergue AS Foto_Portada, " +
                "nombre_foto_de_portada AS Nombre_Foto_Portada FROM Usuario " +
                "WHERE id_rol = ?;";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 2);


            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Usuario albergue = new Usuario();
                    albergue.setId_usuario(rs.getInt("ID_Albergue"));
                    albergue.setNombre_albergue(rs.getString("Nombre_Albergue"));
                    albergue.setFoto_de_portada_albergue(rs.getBytes("Foto_Portada"));
                    albergue.setNombre_foto_de_portada(rs.getString("Nombre_Foto_Portada"));

                    listaAlbergues.add(albergue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listaAlbergues;
    }

    public boolean registrarDonacionEconomica(int idUsuarioFinal, int montoDonacion, byte[] imagenDonacion, String nombreImagenDonacion, int idSolicitudDonacionEconomica, int idEstado) {
        String sql = "INSERT INTO RegistroDonacionEconomica (monto_donacion, fecha_hora_registro, id_solicitud_donacion_economica, id_usuario_final, id_estado, imagen_donacion_economica, nombre_imagen_donacion_economica) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establecer los valores para la consulta
            pstmt.setInt(1, montoDonacion); // monto de la donación
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // fecha y hora actuales
            pstmt.setInt(3, idSolicitudDonacionEconomica); // id_solicitud_donacion_economica
            pstmt.setInt(4, idUsuarioFinal); // id del usuario final que realiza la donación
            pstmt.setInt(5, idEstado); // id del estado, por ejemplo 1 para 'activo'
            pstmt.setBytes(6, imagenDonacion); // imagen de la donación
            pstmt.setString(7, nombreImagenDonacion); // nombre del archivo de la imagen

            // Ejecutar la consulta
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Obtiene el ID de la solicitud de donación económica asociada al usuario del albergue.
     *
     * @param idUsuarioAlbergue El ID del usuario del albergue.
     * @return El ID de la solicitud de donación económica, o -1 si no se encuentra.
     */
    public int obtenerIdSolicitudDonacionEconomica(int idUsuarioAlbergue) {
        String sql = "SELECT id_solicitud_donacion_economica " +
                "FROM SolicitudDonacionEconomica " +
                "WHERE id_usuario_albergue = ? " +
                "LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establecer el parámetro para la consulta
            pstmt.setInt(1, idUsuarioAlbergue);

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retornar el primer resultado encontrado
                    return rs.getInt("id_solicitud_donacion_economica");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el ID de la solicitud de donación económica", e);
        }

        // Si no se encuentra ningún registro, retornar -1
        return -1;
    }

}
