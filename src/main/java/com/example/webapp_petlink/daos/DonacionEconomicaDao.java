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

            try (PreparedStatement psSolicitud = con.prepareStatement(sqlInsertSolicitud, Statement.RETURN_GENERATED_KEYS)) {
                psSolicitud.setInt(1, solicitud.getMonto_solicitado());
                psSolicitud.setString(2, solicitud.getMotivo());
                psSolicitud.setBoolean(3, solicitud.getEs_solicitud_activa());
                psSolicitud.setInt(4, solicitud.getEstado().getId_estado()); // Estado debería ser un valor válido, e.g., 2
                psSolicitud.setInt(5, solicitud.getUsuario_albergue().getId_usuario());

                psSolicitud.executeUpdate();

                // Obtener el ID generado automáticamente
                ResultSet rs = psSolicitud.getGeneratedKeys();
                if (rs.next()) {
                    int idSolicitudGenerada = rs.getInt(1);
                    solicitud.setId_solicitud_donacion_economica(idSolicitudGenerada); // Guardar el ID generado en el objeto
                } else {
                    throw new SQLException("Error al generar el ID para SolicitudDonacionEconomica.");
                }
            }

            con.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            e.printStackTrace();
            // Revertir la transacción si hay un error
            try (Connection con = getConnection()) {
                if (con != null) {
                    con.rollback();
                }
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

        System.out.println("[DEBUG DAO] Ejecutando obtenerDetallesPorId con ID solicitud: " + idSolicitud);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            System.out.println("[DEBUG DAO] Consulta preparada correctamente: " + ps.toString());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("[DEBUG DAO] No se encontraron detalles para ID: " + idSolicitud);
                    return null;
                }

                // Crear la solicitud
                SolicitudDonacionEconomica solicitud = new SolicitudDonacionEconomica();
                solicitud.setId_solicitud_donacion_economica(rs.getInt("id_solicitud_donacion_economica"));
                solicitud.setMotivo(rs.getString("motivo"));
                solicitud.setMonto_solicitado(rs.getInt("monto_solicitado"));
                solicitud.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

                // Estado
                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                solicitud.setEstado(estado);

                // Usuario (Albergue)
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));

                byte[] imagenQR = rs.getBytes("imagen_qr");
                usuario.setImagen_qr(imagenQR);
                System.out.println("[DEBUG DAO] Imagen QR: " + (imagenQR != null ? "Presente (tamaño: " + imagenQR.length + ")" : "No disponible"));

                solicitud.setUsuario_albergue(usuario);

                System.out.println("[DEBUG DAO] Solicitud obtenida con éxito. ID solicitud: " + solicitud.getId_solicitud_donacion_economica());
                return solicitud;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR DAO] Error al obtener detalles de la solicitud: " + e.getMessage());
            throw e;
        }
    }



    public List<RegistroDonacionEconomica> obtenerRegistrosDonacionPorSolicitud(int idSolicitud) throws SQLException {
        List<RegistroDonacionEconomica> registros = new ArrayList<>();

        String sql = "SELECT rde.id_registro_donacion_economica, rde.monto_donacion, rde.fecha_hora_registro, " +
                "rde.imagen_donacion_economica, rde.nombre_imagen_donacion_economica, " +
                "u.id_usuario AS id_donante, u.nombres_usuario_final, u.apellidos_usuario_final " +
                "FROM RegistroDonacionEconomica rde " +
                "JOIN Usuario u ON u.id_usuario = rde.id_usuario_final " +
                "WHERE rde.id_solicitud_donacion_economica = ? " +
                "ORDER BY rde.fecha_hora_registro DESC";

        System.out.println("[DEBUG DAO] Iniciando método obtenerRegistrosDonacionPorSolicitud");
        System.out.println("[DEBUG DAO] ID de solicitud recibida: " + idSolicitud);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            System.out.println("[DEBUG DAO] Consulta preparada correctamente: " + ps.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear un objeto RegistroDonacionEconomica
                    RegistroDonacionEconomica registro = new RegistroDonacionEconomica();

                    // Asignar datos del registro
                    registro.setIdRegistroDonacionEconomica(rs.getInt("id_registro_donacion_economica"));
                    registro.setMontoDonacion(rs.getInt("monto_donacion"));
                    registro.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

                    byte[] imagenDonacion = rs.getBytes("imagen_donacion_economica");
                    registro.setImagenDonacionEconomica(imagenDonacion);
                    registro.setNombreImagenDonacionEconomica(rs.getString("nombre_imagen_donacion_economica"));

                    // Crear un objeto Usuario para el donante
                    Usuario donante = new Usuario();
                    donante.setId_usuario(rs.getInt("id_donante"));
                    donante.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    donante.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));

                    // Debug para asegurarnos de que los datos sean correctos
                    System.out.println("[DEBUG DAO] Donante - ID: " + donante.getId_usuario() +
                            ", Nombre: " + donante.getNombres_usuario_final() +
                            ", Apellido: " + donante.getApellidos_usuario_final());

                    // Asignar el donante al registro
                    registro.setUsuarioFinal(donante);

                    // Agregar el registro completo a la lista
                    registros.add(registro);
                }
                System.out.println("[DEBUG DAO] Total de registros encontrados: " + registros.size());
            }
        } catch (SQLException e) {
            System.err.println("[ERROR DAO] Error al obtener registros de donación: " + e.getMessage());
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
