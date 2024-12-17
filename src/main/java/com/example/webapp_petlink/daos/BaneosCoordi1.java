package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.PostulacionHogarTemporal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Estado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaneosCoordi1 extends DaoBase {

    public List<PostulacionHogarTemporal> obtenerPostulacionesAprobadasPorZona(int idZona) {
        List<PostulacionHogarTemporal> listaPostulaciones = new ArrayList<>();

        String query = "SELECT " +
                "pht.id_postulacion_hogar_temporal, " +
                "pht.celular_usuario, " +
                "pht.fecha_inicio_temporal, " +
                "pht.fecha_fin_temporal, " +
                "pht.cantidad_rechazos_consecutivos, " +
                "u.id_usuario, "+
                "u.nombres_usuario_final, " +
                "u.apellidos_usuario_final " +
                "FROM PostulacionHogarTemporal pht " +
                "INNER JOIN Usuario u ON pht.id_usuario_final = u.id_usuario " +
                "WHERE pht.id_estado = 2 " +
                "AND u.id_zona = ?"; // Filtrar por zona

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idZona); // Parámetro para el id de zona

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostulacionHogarTemporal postulacion = new PostulacionHogarTemporal();
                    postulacion.setId_postulacion_hogar_temporal(rs.getInt("id_postulacion_hogar_temporal"));
                    postulacion.setCelular_usuario(rs.getString("celular_usuario"));
                    postulacion.setFecha_inicio_temporal(rs.getDate("fecha_inicio_temporal").toLocalDate());
                    postulacion.setFecha_fin_temporal(rs.getDate("fecha_fin_temporal").toLocalDate());
                    postulacion.setCantidad_rechazos_consecutivos(rs.getInt("cantidad_rechazos_consecutivos"));

                    Usuario usuario = new Usuario();
                    usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                    usuario.setId_usuario(rs.getInt("id_usuario"));

                    postulacion.setUsuario_final(usuario);
                    listaPostulaciones.add(postulacion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPostulaciones;
    }

    public boolean banearUsuario(int idUsuarioFinal, String motivo, int tipoDeBaneo) {
        String verificarQuery = "SELECT COUNT(*) AS total FROM BaneoHogarTemporal WHERE id_usuario_final = ?";
        String insertarBaneoQuery = "INSERT INTO BaneoHogarTemporal (motivo, fecha_hora_registro, id_usuario_final, tipo_de_baneo) VALUES (?, NOW(), ?, ?)";
        String eliminarPostulacionQuery = "DELETE FROM PostulacionHogarTemporal WHERE id_usuario_final = ?";

        try (Connection conn = getConnection();
             PreparedStatement psVerificar = conn.prepareStatement(verificarQuery);
             PreparedStatement psInsertar = conn.prepareStatement(insertarBaneoQuery);
             PreparedStatement psEliminar = conn.prepareStatement(eliminarPostulacionQuery)) {

            // 1. Verificar si el usuario ya está baneado
            psVerificar.setInt(1, idUsuarioFinal);
            ResultSet rs = psVerificar.executeQuery();

            if (rs.next() && rs.getInt("total") > 0) {
                System.out.println("El usuario ya está baneado.");
                return false; // No se inserta porque ya está baneado
            }

            // 2. Insertar en la tabla BaneoHogarTemporal
            psInsertar.setString(1, motivo);
            psInsertar.setInt(2, idUsuarioFinal);
            psInsertar.setInt(3, tipoDeBaneo);
            psInsertar.executeUpdate();

            // 3. Eliminar de la tabla PostulacionHogarTemporal
            psEliminar.setInt(1, idUsuarioFinal);
            psEliminar.executeUpdate();

            System.out.println("Usuario baneado con éxito y postulación eliminada.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Dao para bloquear al usuario por medio del coordinador, falta asociar del jsp al servlet y que se envie al dao
    public boolean baneoPorCoordinador(String motivo, int id){
        String sql = "INSERT INTO BaneoHogarTemporal (motivo, fecha_hora_registro, id_usuario_final, tipo_de_baneo) VALUES (?, NOW(), ?, 1)";
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, motivo);
            ps.setInt(2, id);

            // Ejecutar la inserción
            int filasInsertadas = ps.executeUpdate();

            return filasInsertadas > 0; // Devuelve true si se inserta correctamente

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre un error, devuelve false
        }
    }

}
