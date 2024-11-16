package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.SolicitudDonacionProductos;
import com.example.webapp_petlink.beans.HorarioRecepcionDonacion;
import com.example.webapp_petlink.beans.Estado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DonacionProductos extends DaoBase {

    // Método para obtener todas las solicitudes de donación activas de un usuario albergue específico
    public List<SolicitudDonacionProductos> obtenerSolicitudesActivas(int idUsuarioAlbergue) {
        List<SolicitudDonacionProductos> solicitudes = new ArrayList<>();

        String sql = "SELECT s.id_solicitud_donacion_productos, s.descripcion_donaciones, " +
                "s.es_solicitud_activa, s.fecha_hora_registro, " +
                "e.nombre_estado " +
                "FROM solicituddonacionproductos s " +
                "JOIN estado e ON s.id_estado = e.id_estado " +
                "WHERE s.es_solicitud_activa = true " +
                "AND s.id_usuario_albergue = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioAlbergue);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
                solicitud.setIdSolicitudDonacionProductos(rs.getInt("id_solicitud_donacion_productos"));
                solicitud.setDescripcionDonaciones(rs.getString("descripcion_donaciones"));
                solicitud.setEsSolicitudActiva(rs.getBoolean("es_solicitud_activa"));
                solicitud.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));

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

    // Método para eliminar una solicitud de donación por su ID
    public void eliminarSolicitud(int idSolicitud) {
        String sql = "DELETE FROM solicituddonacionproductos WHERE id_solicitud_donacion_productos = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener los detalles de una solicitud por su ID
    public SolicitudDonacionProductos obtenerSolicitudPorId(int idSolicitud) {
        SolicitudDonacionProductos solicitud = null;
        String sql = "SELECT s.id_solicitud_donacion_productos, s.descripcion_donaciones, " +
                "s.es_solicitud_activa, s.fecha_hora_registro, e.nombre_estado " +
                "FROM solicituddonacionproductos s " +
                "JOIN estado e ON s.id_estado = e.id_estado " +
                "WHERE s.id_solicitud_donacion_productos = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                solicitud = new SolicitudDonacionProductos();
                solicitud.setIdSolicitudDonacionProductos(rs.getInt("id_solicitud_donacion_productos"));
                solicitud.setDescripcionDonaciones(rs.getString("descripcion_donaciones"));
                solicitud.setEsSolicitudActiva(rs.getBoolean("es_solicitud_activa"));
                solicitud.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));

                Estado estado = new Estado();
                estado.setNombre_estado(rs.getString("nombre_estado"));
                solicitud.setEstado(estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitud;
    }

    // Método para insertar una nueva solicitud de donación
    // Método para insertar una nueva solicitud de donación
    public void insertarSolicitud(SolicitudDonacionProductos solicitud, int idHorarioRecepcionDonacion, int idUsuarioAlbergue) {
        String sql = "INSERT INTO solicituddonacionproductos (descripcion_donaciones, es_solicitud_activa, fecha_hora_registro, id_estado, id_usuario_albergue) " +
                "SELECT ?, ?, h.fecha_hora_inicio, ?, ? " +
                "FROM horariorecepciondonacion h " +
                "WHERE h.id_horario_recepcion_donacion = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // Establecemos los valores de los parámetros
            ps.setString(1, solicitud.getDescripcionDonaciones());  // Descripción de donaciones
            ps.setBoolean(2, solicitud.isEsSolicitudActiva());      // Estado de la solicitud (activa o no)

            // Obtenemos el id del estado desde el objeto Estado en SolicitudDonacionProductos
            if (solicitud.getEstado() != null) {
                ps.setInt(3, solicitud.getEstado().getId_estado());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER); // En caso de que el estado sea null
            }

            ps.setInt(4, idUsuarioAlbergue);                       // ID del usuario albergue
            ps.setInt(5, idHorarioRecepcionDonacion);              // ID del horario de recepción para obtener la fecha

            ps.executeUpdate();
            System.out.println("Solicitud de donación insertada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Time obtenerHoraInicio(int idSolicitud) {
        String sql = "SELECT h.fecha_hora_inicio FROM horariorecepciondonacion h " +
                "JOIN solicituddonacionproductos s ON s.id_horario_recepcion_donacion = h.id_horario_recepcion_donacion " +
                "WHERE s.id_solicitud_donacion_productos = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTime("fecha_hora_inicio"); // Solo la hora
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra la hora
    }

    // Método para obtener la hora de fin de una solicitud de donación
    public Time obtenerHoraFin(int idSolicitud) {
        String sql = "SELECT h.fecha_hora_fin FROM horariorecepciondonacion h " +
                "JOIN solicituddonacionproductos s ON s.id_horario_recepcion_donacion = h.id_horario_recepcion_donacion " +
                "WHERE s.id_solicitud_donacion_productos = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTime("fecha_hora_fin"); // Solo la hora
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra la hora
    }

}