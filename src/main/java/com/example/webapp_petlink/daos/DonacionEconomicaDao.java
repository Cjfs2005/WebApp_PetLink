package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.SolicitudDonacionEconomica;
import com.example.webapp_petlink.beans.Estado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonacionEconomicaDao extends DaoBase {

    // Método para obtener todas las solicitudes de donación económica activas de un usuario albergue específico
    public List<SolicitudDonacionEconomica> obtenerSolicitudesActivas(int idUsuarioAlbergue) {
        List<SolicitudDonacionEconomica> solicitudes = new ArrayList<>();

        // Consulta para obtener las solicitudes activas del usuario albergue
        String sql = "SELECT s.id_solicitud_donacion_economica, s.motivo, s.monto_solicitado, " +
                "s.es_solicitud_activa, s.fecha_hora_registro, e.nombre_estado " +
                "FROM solicituddonacioneconomica s " +
                "JOIN estado e ON s.id_estado = e.id_estado " +
                "WHERE s.es_solicitud_activa = true AND s.id_usuario_albergue = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioAlbergue);  // Filtramos por id_usuario_albergue

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                solicitud.setMotivo(rs.getString("motivo"));
                solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                solicitud.setEs_solicitud_activa(rs.getBoolean("es_solicitud_activa"));

                // Convertimos el DATETIME de MySQL a LocalDateTime de Java
                Timestamp timestamp = rs.getTimestamp("fecha_hora_registro");
                if (timestamp != null) {
                    solicitud.setFecha_hora_registro(timestamp.toLocalDateTime());
                }

                // Establecemos el estado de la solicitud
                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                solicitud.setEstado(estado);

                // Añadimos la solicitud a la lista
                solicitudes.add(solicitud);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }
}
