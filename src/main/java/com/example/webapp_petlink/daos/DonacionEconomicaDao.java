package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonacionEconomicaDao extends DaoBase {

    public List<PuntoAcopio> obtenerPuntosAcopioPorAlbergue(int idUsuarioAlbergue) {
        List<PuntoAcopio> puntos = new ArrayList<>();
        String sql = "SELECT id_punto_acopio, direccion_punto_acopio " +
                "FROM puntoacopio " +
                "WHERE id_usuario_albergue = ?";

        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioAlbergue);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PuntoAcopio punto = new PuntoAcopio();
                punto.setId_punto_acopio(rs.getInt("id_punto_acopio"));
                punto.setDireccion_punto_acopio(rs.getString("direccion_punto_acopio"));
                puntos.add(punto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puntos;
    }

    public List<RegistroDonacionProductos> obtenerDetallesDonacionesPorSolicitud(int idSolicitud) {
        List<RegistroDonacionProductos> detallesDonaciones = new ArrayList<>();

        String sql = "SELECT " +
                "    u.nombres_usuario_final AS nombre_usuario, " +
                "    u.apellidos_usuario_final AS apellido_usuario, " +
                "    pac.direccion_punto_acopio AS direccion_acopio, " +
                "    r.fecha_hora_registro AS fecha_donacion, " +
                "    r.descripciones_donaciones AS productos_donados " +
                "FROM " +
                "    registrodonacionproductos r " +
                "INNER JOIN " +
                "    horariorecepciondonacion h ON r.id_horario_recepcion_donacion = h.id_horario_recepcion_donacion " +
                "INNER JOIN " +
                "    puntoacopiodonacion pa ON h.id_punto_acopio_donacion = pa.id_punto_acopio_donacion " +
                "INNER JOIN " +
                "    puntoacopio pac ON pa.id_punto_acopio = pac.id_punto_acopio " +
                "INNER JOIN " +
                "    usuario u ON r.id_usuario_final = u.id_usuario " +
                "WHERE " +
                "    pa.id_solicitud_donacion_productos = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            System.out.println("Parámetro preparado con ID de solicitud: " + idSolicitud);


            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear el bean RegistroDonacionProductos
                    RegistroDonacionProductos registro = new RegistroDonacionProductos();

                    // Crear el bean Usuario y setear sus valores
                    Usuario usuario = new Usuario();
                    usuario.setNombres_usuario_final(rs.getString("nombre_usuario"));
                    usuario.setApellidos_usuario_final(rs.getString("apellido_usuario"));

                    // Crear el bean PuntoAcopio y setear su dirección
                    PuntoAcopio puntoAcopio = new PuntoAcopio();
                    puntoAcopio.setDireccion_punto_acopio(rs.getString("direccion_acopio"));
                    // Setear valores en el RegistroDonacionProductos
                    registro.setUsuarioFinal(usuario);
                    registro.setFechaHoraRegistro(rs.getTimestamp("fecha_donacion"));
                    registro.setDescripcionesDonaciones(rs.getString("productos_donados"));
                    System.out.println("Registro encontrado:");
                    System.out.println("Nombre: " + usuario.getNombres_usuario_final());
                    System.out.println("Apellido: " + usuario.getApellidos_usuario_final());
                    System.out.println("Punto de Acopio: " + puntoAcopio.getDireccion_punto_acopio());
                    System.out.println("Fecha de Donación: " + registro.getFechaHoraRegistro());
                    System.out.println("Productos Donados: " + registro.getDescripcionesDonaciones());
                    // Añadir el registro a la lista
                    detallesDonaciones.add(registro);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta:");
            e.printStackTrace();
        }
        System.out.println("Total de registros encontrados: " + detallesDonaciones.size());
        return detallesDonaciones;
    }

    public List<SolicitudDonacionProductos> obtenerSolicitudesActivas(int idUsuarioAlbergue) {
        List<SolicitudDonacionProductos> solicitudes = new ArrayList<>();
        String sql = "SELECT s.id_solicitud_donacion_productos, s.descripcion_donaciones, " +
                "s.es_solicitud_activa, s.fecha_hora_registro, e.nombre_estado " +
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

    public void crearSolicitudConRelacion(SolicitudDonacionProductos solicitud, int idPuntoAcopio, LocalDateTime horaInicio, LocalDateTime horaFin) {
        String sqlInsertSolicitud = "INSERT INTO solicituddonacionproductos (descripcion_donaciones, es_solicitud_activa, fecha_hora_registro, id_estado, id_usuario_albergue) VALUES (?, ?, NOW(), ?, ?)";
        String sqlInsertPuntoAcopio = "INSERT INTO puntoacopiodonacion (id_solicitud_donacion_productos, id_punto_acopio) VALUES (?, ?)";
        String sqlInsertHorario = "INSERT INTO horariorecepciondonacion (id_punto_acopio_donacion, fecha_hora_inicio, fecha_hora_fin) VALUES (?, ?, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false);

            int idSolicitudGenerada;
            try (PreparedStatement psSolicitud = con.prepareStatement(sqlInsertSolicitud, Statement.RETURN_GENERATED_KEYS)) {
                psSolicitud.setString(1, solicitud.getDescripcionDonaciones());
                psSolicitud.setBoolean(2, solicitud.isEsSolicitudActiva());
                psSolicitud.setInt(3, solicitud.getEstado().getId_estado());
                psSolicitud.setInt(4, solicitud.getUsuarioAlbergue().getId_usuario());

                psSolicitud.executeUpdate();

                ResultSet rs = psSolicitud.getGeneratedKeys();
                if (rs.next()) {
                    idSolicitudGenerada = rs.getInt(1);
                } else {
                    throw new SQLException("Error al generar el ID para solicituddonacionproductos.");
                }
            }

            int idPuntoAcopioDonacion;
            try (PreparedStatement psPuntoAcopio = con.prepareStatement(sqlInsertPuntoAcopio, Statement.RETURN_GENERATED_KEYS)) {
                psPuntoAcopio.setInt(1, idSolicitudGenerada);
                psPuntoAcopio.setInt(2, idPuntoAcopio);
                psPuntoAcopio.executeUpdate();

                ResultSet rsPunto = psPuntoAcopio.getGeneratedKeys();
                if (rsPunto.next()) {
                    idPuntoAcopioDonacion = rsPunto.getInt(1);
                } else {
                    throw new SQLException("Error al generar ID para puntoacopiodonacion.");
                }
            }

            try (PreparedStatement psHorario = con.prepareStatement(sqlInsertHorario)) {
                psHorario.setInt(1, idPuntoAcopioDonacion);
                psHorario.setTimestamp(2, Timestamp.valueOf(horaInicio));
                psHorario.setTimestamp(3, Timestamp.valueOf(horaFin));
                psHorario.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection con = getConnection()) {
                con.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    public void eliminarSolicitudLogica(int idSolicitud) throws SQLException {
        String sqlUpdateSolicitud = "UPDATE solicituddonacionproductos SET es_solicitud_activa = false WHERE id_solicitud_donacion_productos = ?";

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

    public SolicitudDonacionProductos obtenerDetallesPorId(int idSolicitud) {
        SolicitudDonacionProductos solicitud = null;

        String sql = "SELECT s.descripcion_donaciones, h.fecha_hora_inicio, h.fecha_hora_fin " +
                "FROM solicituddonacionproductos s " +
                "INNER JOIN puntoacopiodonacion p ON s.id_solicitud_donacion_productos = p.id_solicitud_donacion_productos " +
                "INNER JOIN horariorecepciondonacion h ON p.id_punto_acopio_donacion = h.id_punto_acopio_donacion " +
                "WHERE s.id_solicitud_donacion_productos = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    solicitud = new SolicitudDonacionProductos();
                    solicitud.setDescripcionDonaciones(rs.getString("descripcion_donaciones"));

                    HorarioRecepcionDonacion horario = new HorarioRecepcionDonacion();
                    if (rs.getTimestamp("fecha_hora_inicio") != null) {
                        horario.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                    }
                    if (rs.getTimestamp("fecha_hora_fin") != null) {
                        horario.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());
                    }
                    solicitud.setHorarioRecepcion(horario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return solicitud;
    }
    public void modificarSolicitud(SolicitudDonacionProductos solicitud) throws SQLException {
        String sql = "UPDATE solicituddonacionproductos " +
                "SET descripcion_donaciones = ?, id_estado = ?, es_solicitud_activa = ? " +
                "WHERE id_solicitud_donacion_productos = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Establecer los parámetros
            ps.setString(1, solicitud.getDescripcionDonaciones());
            ps.setInt(2, solicitud.getEstado().getId_estado());
            ps.setBoolean(3, solicitud.isEsSolicitudActiva());
            ps.setInt(4, solicitud.getIdSolicitudDonacionProductos());

            // Ejecutar la actualización
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Solicitud actualizada con éxito.");
            } else {
                throw new SQLException("No se encontró la solicitud con el ID especificado para actualizarla.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
