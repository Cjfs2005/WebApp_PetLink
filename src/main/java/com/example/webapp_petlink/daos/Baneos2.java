package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.BaneoHogarTemporal;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Baneos2 extends DaoBase {

    public List<BaneoHogarTemporal> listarBaneosPorTipo(int idZona, int tipoDeBaneo) {
        List<BaneoHogarTemporal> baneos = new ArrayList<>();
        String sql = "SELECT b.id_baneo_hogar_temporal, b.motivo, b.fecha_hora_registro, b.tipo_de_baneo, " +
                "u.nombres_usuario_final, u.apellidos_usuario_final, u.correo_electronico " +
                "FROM BaneoHogarTemporal b " +
                "INNER JOIN Usuario u ON b.id_usuario_final = u.id_usuario " +
                "WHERE b.tipo_de_baneo = ? AND u.id_zona = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tipoDeBaneo); // Tipo de baneo (0 o 1)
            pstmt.setInt(2, idZona);      // ID de la zona

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BaneoHogarTemporal baneo = new BaneoHogarTemporal();

                    // Asignar valores desde la base de datos
                    baneo.setId_baneo_hogar_temporal(rs.getInt("id_baneo_hogar_temporal"));
                    baneo.setMotivo(rs.getString("motivo"));
                    baneo.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());
                    baneo.setTipo_de_baneo(rs.getInt("tipo_de_baneo"));

                    // Usuario Final
                    Usuario usuario = new Usuario();
                    usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                    usuario.setCorreo_electronico(rs.getString("correo_electronico"));
                    baneo.setUsuario_final(usuario);

                    baneos.add(baneo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los baneos", e);
        }

        return baneos;
    }
}
