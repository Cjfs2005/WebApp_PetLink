package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonacionEconomicaDao extends DaoBase {

    public List<SolicitudDonacionEconomica> obtenerSolicitudesActivas(int idUsuarioAlbergue) {
        List<SolicitudDonacionEconomica> solicitudes = new ArrayList<>();
        String sql = "SELECT s.id_solicitud_donacion_economica, s.monto_solicitado, s.motivo, " +
                "s.es_solicitud_activa, s.fecha_hora_registro, e.nombre_estado " +
                "FROM SolicitudDonacionEconomica s " +
                "JOIN Estado e ON s.id_estado = e.id_estado " +
                "WHERE s.es_solicitud_activa = true " +
                "AND s.id_usuario_albergue = ?";


        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioAlbergue);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                solicitud.setMotivo(rs.getString("motivo"));
                solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                solicitud.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                solicitud.setEstado(estado);

                solicitudes.add(solicitud);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    public void crearSolicitudEconomica(SolicitudDonacionEconomica solicitud) {
        String sqlInsertSolicitud = "INSERT INTO SolicitudDonacionEconomica " +
                "(monto_solicitado, motivo, es_solicitud_activa, fecha_hora_registro, id_estado, id_usuario_albergue) " +
                "VALUES (?, ?, ?, NOW(), ?, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false); // Iniciar una transacción

            // Insertar solicitud económica
            try (PreparedStatement psSolicitud = con.prepareStatement(sqlInsertSolicitud, Statement.RETURN_GENERATED_KEYS)) {
                psSolicitud.setInt(1, solicitud.getMonto_solicitado());
                psSolicitud.setString(2, solicitud.getMotivo());
                psSolicitud.setBoolean(3, solicitud.getEs_solicitud_activa());
                psSolicitud.setInt(4, solicitud.getEstado().getId_estado());
                psSolicitud.setInt(5, solicitud.getUsuario_albergue().getId_usuario());

                psSolicitud.executeUpdate();

                // Obtener el ID generado automáticamente
                ResultSet rs = psSolicitud.getGeneratedKeys();
                if (rs.next()) {
                    int idSolicitudGenerada = rs.getInt(1);
                    solicitud.setId_solicitud_donacion_economica(idSolicitudGenerada); // Guardar el ID en el objeto
                } else {
                    throw new SQLException("Error al generar el ID para solicituddonacioneconomica.");
                }
            }

            con.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection con = getConnection()) {
                con.rollback(); // Revertir la transacción si hay un error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
    public void eliminarSolicitudLogica(int idSolicitud) throws SQLException {
        String sqlUpdateSolicitud = "UPDATE SolicitudDonacionEconomica SET es_solicitud_activa = false WHERE id_solicitud_donacion_economica = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUpdateSolicitud)) {

            ps.setInt(1, idSolicitud);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Solicitud desactivada lógicamente con éxito.");
            } else {
                throw new SQLException("No se encontró la solicitud con el ID especificado para desactivarla.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public SolicitudDonacionEconomica obtenerDetallesPorId(int idSolicitud) throws SQLException {
        String sql = "SELECT s.id_solicitud_donacion_economica, s.monto_solicitado, s.motivo, " +
                "s.fecha_hora_registro, e.nombre_estado, u.id_usuario, u.nombres_usuario_final, " +
                "u.apellidos_usuario_final, u.imagen_qr " +
                "FROM SolicitudDonacionEconomica s " +
                "JOIN Estado e ON s.id_estado = e.id_estado " +
                "JOIN Usuario u ON s.id_usuario_albergue = u.id_usuario " +
                "WHERE s.id_solicitud_donacion_economica = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                // Verificar si el resultado está vacío
                if (!rs.next()) {
                    return null; // Devuelve null si no se encuentra la solicitud
                }

                // Crear la solicitud y asignar valores
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                solicitud.setMotivo(rs.getString("motivo"));
                solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                solicitud.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

                // Asignar estado
                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                solicitud.setEstado(estado);

                // Asignar usuario asociado (albergue)
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                usuario.setImagen_qr(rs.getBytes("imagen_qr"));
                solicitud.setUsuario_albergue(usuario);

                return solicitud;
            }
        }
    }




    public List<RegistroDonacionEconomica> obtenerRegistrosDonacionPorSolicitud(int idSolicitud) throws SQLException {
        List<RegistroDonacionEconomica> registros = new ArrayList<>();

        String sql = "SELECT rde.id_registro_donacion_economica, rde.monto_donacion, rde.fecha_hora_registro, " +
                "u.id_usuario, u.nombres_usuario_final, u.apellidos_usuario_final " +
                "FROM RegistroDonacionEconomica rde " +
                "JOIN Usuario u ON u.id_usuario = rde.id_usuario_final " +
                "WHERE rde.id_solicitud_donacion_economica = ? " +
                "ORDER BY rde.fecha_hora_registro DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            System.out.println("Ejecutando consulta para obtener registros de donación con ID de solicitud: " + idSolicitud);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear un nuevo objeto RegistroDonacionEconomica
                    RegistroDonacionEconomica registro = new RegistroDonacionEconomica();
                    registro.setIdRegistroDonacionEconomica(rs.getInt("id_registro_donacion_economica"));
                    System.out.println("ID Registro Donación: " + registro.getIdRegistroDonacionEconomica());

                    registro.setMontoDonacion(rs.getInt("monto_donacion"));
                    System.out.println("Monto Donación: " + registro.getMontoDonacion());

                    registro.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());
                    System.out.println("Fecha y Hora de Registro: " + registro.getFechaHoraRegistro());

                    // Asociar usuario final
                    Usuario usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    System.out.println("ID Usuario Final: " + usuario.getId_usuario());

                    usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    System.out.println("Nombres Usuario Final: " + usuario.getNombres_usuario_final());

                    usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                    System.out.println("Apellidos Usuario Final: " + usuario.getApellidos_usuario_final());

                    registro.setUsuarioFinal(usuario);

                    registros.add(registro);
                }

                if (registros.isEmpty()) {
                    System.out.println("No se encontraron registros de donaciones para la solicitud con ID: " + idSolicitud);
                } else {
                    System.out.println("Registros de donación obtenidos con éxito: " + registros.size());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener registros de donación: " + e.getMessage());
            throw e;
        }

        return registros;
    }

    public void actualizarSolicitud(SolicitudDonacionEconomica solicitud) throws SQLException {
        String sql = "UPDATE SolicitudDonacionEconomica " +
                "SET monto_solicitado = ?, motivo = ?, fecha_hora_registro = NOW() " +
                "WHERE id_solicitud_donacion_economica = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Configurar los parámetros
            ps.setInt(1, solicitud.getMonto_solicitado());
            ps.setString(2, solicitud.getMotivo());
            ps.setInt(3, solicitud.getId_solicitud_donacion_economica());

            // Ejecutar la actualización
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Solicitud actualizada con éxito: ID " + solicitud.getId_solicitud_donacion_economica());
            } else {
                throw new SQLException("No se encontró la solicitud con el ID especificado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la solicitud: " + e.getMessage());
            throw e; // Propagar la excepción para manejarla en el Servlet
        }
    }




}
