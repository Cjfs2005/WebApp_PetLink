package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DonacionEconomicaUsuario extends DaoBase {

    public List<SolicitudDonacionEconomica> obtenerTodasSolicitudesActivas() {
        List<SolicitudDonacionEconomica> solicitudes = new ArrayList<>();
        String sql = "SELECT " +
                "sde.id_solicitud_donacion_economica, " +
                "sde.monto_solicitado, " +
                "sde.motivo, " +
                "u.nombre_albergue, " +
                "u.foto_de_portada_albergue " +
                "FROM SolicitudDonacionEconomica sde " +
                "INNER JOIN Usuario u ON sde.id_usuario_albergue = u.id_usuario " +
                "WHERE sde.es_solicitud_activa = 1 AND sde.id_estado = 2";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int contador = 0;
            while (rs.next()) {
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                solicitud.setMotivo(rs.getString("motivo"));

                Usuario albergue = new Usuario();
                albergue.setNombre_albergue(rs.getString("nombre_albergue"));

                byte[] fotoBytes = rs.getBytes("foto_de_portada_albergue");
                if (fotoBytes != null) {
                    String fotoBase64 = Base64.getEncoder().encodeToString(fotoBytes);
                    albergue.setNombre_foto_de_portada(fotoBase64);
                }

                solicitud.setUsuario_albergue(albergue);
                solicitudes.add(solicitud);

                // Mensaje de depuración
                System.out.println("[DEBUG DAO] Solicitud encontrada: ID=" + solicitud.getId_solicitud_donacion_economica() +
                        ", Monto=" + solicitud.getMonto_solicitado() + ", Motivo=" + solicitud.getMotivo());
                contador++;
            }

            // Mensaje de depuración: Total de solicitudes encontradas
            System.out.println("[DEBUG DAO] Total de solicitudes económicas activas encontradas: " + contador);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ERROR DAO] No se pudo obtener las solicitudes activas: " + e.getMessage());
        }

        return solicitudes;
    }

    public SolicitudDonacionEconomica obtenerDetalleSolicitudEconomica(int idSolicitud) {
        SolicitudDonacionEconomica solicitud = null;

        String sql = "SELECT " +
                "s.id_solicitud_donacion_economica, " +
                "s.monto_solicitado, " +
                "s.motivo, " +
                "u.nombre_albergue, " +
                "u.imagen_qr " +
                "FROM SolicitudDonacionEconomica s " +
                "INNER JOIN Usuario u ON s.id_usuario_albergue = u.id_usuario " +
                "WHERE s.id_solicitud_donacion_economica = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    solicitud = new SolicitudDonacionEconomica();
                    solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                    solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                    solicitud.setMotivo(rs.getString("motivo"));

                    Usuario albergue = new Usuario();
                    albergue.setNombre_albergue(rs.getString("nombre_albergue"));

                    // Convertir la imagen QR a Base64
                    byte[] qrBytes = rs.getBytes("imagen_qr");
                    if (qrBytes != null) {
                        String qrBase64 = Base64.getEncoder().encodeToString(qrBytes);
                        albergue.setNombre_imagen_qr(qrBase64); // Reutilizamos el atributo existente
                    }

                    solicitud.setUsuario_albergue(albergue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return solicitud;
    }
    public Map<Integer, Integer> obtenerMontosRecaudados() {
        Map<Integer, Integer> montosRecaudados = new HashMap<>();
        String sql = "SELECT " +
                "sde.id_solicitud_donacion_economica, " +
                "COALESCE(SUM(rde.monto_donacion), 0) AS monto_recaudado " +
                "FROM SolicitudDonacionEconomica sde " +
                "LEFT JOIN RegistroDonacionEconomica rde " +
                "ON sde.id_solicitud_donacion_economica = rde.id_solicitud_donacion_economica " +
                "GROUP BY sde.id_solicitud_donacion_economica";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idSolicitud = rs.getInt("id_solicitud_donacion_economica");
                int montoRecaudado = rs.getInt("monto_recaudado");
                montosRecaudados.put(idSolicitud, montoRecaudado);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ERROR DAO] Error al obtener los montos recaudados: " + e.getMessage());
        }

        return montosRecaudados;
    }

    public boolean registrarDonacionEconomica(
            int idUsuarioFinal,
            int montoDonacion,
            byte[] imagenDonacion,
            String nombreImagen,
            int idSolicitud,
            int idEstado) {

        // Consulta SQL para insertar los datos de la donación
        String sql = "INSERT INTO RegistroDonacionEconomica (" +
                "monto_donacion, fecha_hora_registro, id_solicitud_donacion_economica, " +
                "id_usuario_final, id_estado, imagen_donacion_economica, nombre_imagen_donacion_economica" +
                ") VALUES (?, NOW(), ?, ?, ?, ?, ?)";

        try (Connection con = getConnection(); // Obtener la conexión a la base de datos
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignar valores a los parámetros de la consulta
            ps.setInt(1, montoDonacion); // Monto donado
            ps.setInt(2, idSolicitud); // ID de la solicitud de donación
            ps.setInt(3, idUsuarioFinal); // ID del usuario final que realiza la donación
            ps.setInt(4, idEstado); // Estado de la donación (1: Registrado)

            // Manejo de la imagen (puede ser null si no se cargó ninguna)
            if (imagenDonacion != null) {
                ps.setBytes(5, imagenDonacion); // Insertar la imagen como bytes
            } else {
                ps.setNull(5, java.sql.Types.BLOB); // Manejar el caso de imagen no proporcionada
            }

            ps.setString(6, nombreImagen); // Nombre del archivo de la imagen

            // Ejecutar la consulta
            int filasAfectadas = ps.executeUpdate();

            // Retornar true si se insertaron registros correctamente
            return filasAfectadas > 0;

        } catch (Exception e) {
            // Manejo de excepciones
            e.printStackTrace();
            System.out.println("[ERROR DAO] Error al registrar la donación económica: " + e.getMessage());
            return false;
        }
    }


}