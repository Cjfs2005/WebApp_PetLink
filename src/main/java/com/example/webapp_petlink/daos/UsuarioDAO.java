package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;

public class UsuarioDAO extends DaoBase{
    public void updateUsuario(Usuario usuario) {
        String sql = "UPDATE Usuario SET cantidad_animales = ?, espacio_disponible = ?, " +
                "anio_creacion = ?, id_distrito= ?, direccion = ?, descripcion_perfil=?, foto_perfil = ?,nombre_foto_perfil=?, " +
                "foto_de_portada_albergue=?, nombre_foto_de_portada=?, direccion_donaciones=?, nombre_contacto_donaciones=?," +
                "numero_contacto_donaciones=?, numero_yape_plin=?, imagen_qr=?, nombre_imagen_qr=?, id_zona=?, tiene_registro_completo=1 WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,usuario.getCantidad_animales());
            pstmt.setInt(2,usuario.getEspacio_disponible());
            pstmt.setString(3,usuario.getAnio_creacion());
            pstmt.setInt(4,usuario.getDistrito().getId_distrito());
            pstmt.setString(5, usuario.getDireccion());
            pstmt.setString(6,usuario.getDescripcion_perfil());
            pstmt.setBytes(7,usuario.getFoto_perfil());
            pstmt.setString(8,usuario.getNombre_foto_perfil());
            pstmt.setBytes(9, usuario.getFoto_de_portada_albergue());
            pstmt.setString(10,usuario.getNombre_foto_de_portada());
            pstmt.setString(11,usuario.getDireccion_donaciones());
            pstmt.setString(12,usuario.getNombre_contacto_donaciones());
            pstmt.setString(13,usuario.getNumero_contacto_donaciones());
            pstmt.setString(14,usuario.getNumero_yape_plin());
            pstmt.setBytes(15,usuario.getImagen_qr());
            pstmt.setString(16,usuario.getNombre_imagen_qr());
            pstmt.setInt(17,usuario.getZona().getId_zona());
            pstmt.setInt(18,usuario.getId_usuario());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void generarEconomica(Usuario usuario) {
        String sql = "INSERT INTO SolicitudDonacionEconomica (monto_solicitado,motivo,es_solicitud_activa,fecha_hora_registro,id_usuario_albergue,id_estado) values(1000,'Apoyo general al albergue', 1,now(),?,2)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,usuario.getId_usuario());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
