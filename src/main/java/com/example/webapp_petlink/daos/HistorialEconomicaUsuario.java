package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistorialEconomicaUsuario extends DaoBase {

    public List<RegistroDonacionEconomica> obtenerHistorialDonacionesEconomicas(int idUsuarioFinal) {
        List<RegistroDonacionEconomica> historial = new ArrayList<>();
        String sql = "SELECT r.id_registro_donacion_economica, r.monto_donacion, r.fecha_hora_registro, " +
                "s.id_solicitud_donacion_economica, s.monto_solicitado, s.motivo, s.fecha_hora_registro AS fecha_solicitud, " +
                "e.nombre_estado AS estado_donacion, a.id_usuario AS id_albergue, a.nombre_albergue AS nombre_albergue " +
                "FROM RegistroDonacionEconomica r " +
                "INNER JOIN SolicitudDonacionEconomica s ON r.id_solicitud_donacion_economica = s.id_solicitud_donacion_economica " +
                "INNER JOIN Usuario a ON s.id_usuario_albergue = a.id_usuario " +
                "INNER JOIN Estado e ON s.id_estado = e.id_estado " +
                "WHERE r.id_usuario_final = ? AND s.id_estado = 2;";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioFinal); // Parámetro para el ID del usuario final

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear instancia de RegistroDonacionEconomica
                    RegistroDonacionEconomica registro = new RegistroDonacionEconomica();
                    registro.setIdRegistroDonacionEconomica(rs.getInt("id_registro_donacion_economica"));
                    registro.setMontoDonacion(rs.getInt("monto_donacion"));
                    registro.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

                    // Crear instancia de SolicitudDonacionEconomica
                    SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                    solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                    solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                    solicitud.setMotivo(rs.getString("motivo"));
                    solicitud.setFecha_hora_registro(rs.getTimestamp("fecha_solicitud").toLocalDateTime());

                    // Asociar albergue a la solicitud
                    Usuario albergue = new Usuario();
                    albergue.setId_usuario(rs.getInt("id_albergue"));
                    albergue.setNombre_albergue(rs.getString("nombre_albergue"));
                    solicitud.setUsuario_albergue(albergue);

                    // Asociar estado a la solicitud
                    Estado estado = new Estado();
                    estado.setNombre_estado(rs.getString("estado_donacion"));
                    solicitud.setEstado(estado);

                    // Asociar solicitud al registro
                    registro.setSolicitudDonacionEconomica(solicitud);

                    // Agregar registro a la lista
                    historial.add(registro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return historial;
    }
}