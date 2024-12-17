package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class DonacionProductos extends DaoBase {

    public List<PuntoAcopio> obtenerPuntosAcopioPorAlbergue(int idUsuarioAlbergue) {
        List<PuntoAcopio> puntos = new ArrayList<>();
        String sql = "SELECT id_punto_acopio, direccion_punto_acopio " +
                "FROM PuntoAcopio " +
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
                "    r.descripciones_donaciones AS productos_donados, " +
                "    h.fecha_hora_inicio AS horario_inicio, " +
                "    h.fecha_hora_fin AS horario_fin " +
                "FROM " +
                "    RegistroDonacionProductos r " +
                "INNER JOIN " +
                "    HorarioRecepcionDonacion h ON r.id_horario_recepcion_donacion = h.id_horario_recepcion_donacion " +
                "INNER JOIN " +
                "    PuntoAcopioDonacion pa ON h.id_punto_acopio_donacion = pa.id_punto_acopio_donacion " +
                "INNER JOIN " +
                "    PuntoAcopio pac ON pa.id_punto_acopio = pac.id_punto_acopio " +
                "INNER JOIN " +
                "    Usuario u ON r.id_usuario_final = u.id_usuario " +
                "WHERE " +
                "    pa.id_solicitud_donacion_productos = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Nombre Usuario: " + rs.getString("nombre_usuario"));
                    System.out.println("Apellido Usuario: " + rs.getString("apellido_usuario"));
                    System.out.println("Dirección Punto Acopio: " + rs.getString("direccion_acopio"));
                    System.out.println("Fecha Donación: " + rs.getTimestamp("fecha_donacion"));
                    System.out.println("Productos Donados: " + rs.getString("productos_donados"));
                    // Crear el bean RegistroDonacionProductos
                    RegistroDonacionProductos registro = new RegistroDonacionProductos();

                    // Usuario
                    Usuario usuario = new Usuario();
                    usuario.setNombres_usuario_final(rs.getString("nombre_usuario"));
                    usuario.setApellidos_usuario_final(rs.getString("apellido_usuario"));
                    registro.setUsuarioFinal(usuario);

                    // HorarioRecepcionDonacion
                    HorarioRecepcionDonacion horarioRecepcion = new HorarioRecepcionDonacion();
                    horarioRecepcion.setFechaHoraInicio(rs.getTimestamp("horario_inicio").toLocalDateTime());
                    horarioRecepcion.setFechaHoraFin(rs.getTimestamp("horario_fin").toLocalDateTime());

                    // PuntoAcopio
                    PuntoAcopio puntoAcopio = new PuntoAcopio();
                    puntoAcopio.setDireccion_punto_acopio(rs.getString("direccion_acopio"));

                    // PuntoAcopioDonacion
                    PuntoAcopioDonacion puntoAcopioDonacion = new PuntoAcopioDonacion();
                    puntoAcopioDonacion.setPuntoAcopio(puntoAcopio);

                    // Relacionar los objetos
                    horarioRecepcion.setPuntoAcopioDonacion(puntoAcopioDonacion);
                    registro.setHorarioRecepcionDonacion(horarioRecepcion);

                    // Asignar otros valores al registro
                    registro.setFechaHoraRegistro(rs.getTimestamp("fecha_donacion"));
                    registro.setDescripcionesDonaciones(rs.getString("productos_donados"));

                    // Agregar registro a la lista
                    detallesDonaciones.add(registro);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta:");
            e.printStackTrace();
        }

        return detallesDonaciones;
    }



    public List<SolicitudDonacionProductos> obtenerSolicitudesActivas(int idUsuarioAlbergue) {
        List<SolicitudDonacionProductos> solicitudes = new ArrayList<>();
        String sql = "SELECT s.id_solicitud_donacion_productos, s.descripcion_donaciones, " +
                "s.es_solicitud_activa, s.fecha_hora_registro, e.nombre_estado " +
                "FROM SolicitudDonacionProductos s " +
                "JOIN Estado e ON s.id_estado = e.id_estado " +
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
        String sqlInsertSolicitud = "INSERT INTO SolicitudDonacionProductos (descripcion_donaciones, es_solicitud_activa, fecha_hora_registro, id_estado, id_usuario_albergue) VALUES (?, ?, NOW(), ?, ?)";
        String sqlInsertPuntoAcopio = "INSERT INTO PuntoAcopioDonacion (id_solicitud_donacion_productos, id_punto_acopio) VALUES (?, ?)";
        String sqlInsertHorario = "INSERT INTO HorarioRecepcionDonacion (id_punto_acopio_donacion, fecha_hora_inicio, fecha_hora_fin) VALUES (?, ?, ?)";

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
        String sqlUpdateSolicitud = "UPDATE SolicitudDonacionProductos SET es_solicitud_activa = false WHERE id_solicitud_donacion_productos = ?";

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
                "FROM SolicitudDonacionProductos s " +
                "INNER JOIN PuntoAcopioDonacion p ON s.id_solicitud_donacion_productos = p.id_solicitud_donacion_productos " +
                "INNER JOIN HorarioRecepcionDonacion h ON p.id_punto_acopio_donacion = h.id_punto_acopio_donacion " +
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
                    solicitud.setIdSolicitudDonacionProductos(idSolicitud);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return solicitud;
    }

    public void modificarSolicitudCompleta(SolicitudDonacionProductos solicitud) throws SQLException {
        // Consulta SQL para actualizar todas las columnas
        String sql = "UPDATE SolicitudDonacionProductos s " +
                "JOIN PuntoAcopioDonacion p ON s.id_solicitud_donacion_productos = p.id_solicitud_donacion_productos " +
                "JOIN HorarioRecepcionDonacion h ON p.id_punto_acopio_donacion = h.id_punto_acopio_donacion " +
                "SET s.descripcion_donaciones = ?, " +
                "    h.fecha_hora_inicio = ?, " +
                "    h.fecha_hora_fin = ?, " +
                "    p.id_punto_acopio = ? " +
                "WHERE s.id_solicitud_donacion_productos = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Asignar parámetros a la consulta
            ps.setString(1, solicitud.getDescripcionDonaciones());
            ps.setTimestamp(2, Timestamp.valueOf(solicitud.getHorarioRecepcion().getFechaHoraInicio()));
            ps.setTimestamp(3, Timestamp.valueOf(solicitud.getHorarioRecepcion().getFechaHoraFin()));
            ps.setInt(4, solicitud.getHorarioRecepcion().getPuntoAcopioDonacion().getPuntoAcopio().getId_punto_acopio());
            ps.setInt(5, solicitud.getIdSolicitudDonacionProductos());

            // Ejecutar la actualización
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Solicitud actualizada correctamente.");
            } else {
                throw new SQLException("No se encontró la solicitud con el ID especificado.");
            }

        }
        System.out.println("Actualizando solicitud:");
        System.out.println("Descripción: " + solicitud.getDescripcionDonaciones());
        System.out.println("Fecha Hora Inicio: " + solicitud.getHorarioRecepcion().getFechaHoraInicio());
        System.out.println("Fecha Hora Fin: " + solicitud.getHorarioRecepcion().getFechaHoraFin());
        System.out.println("ID Punto Acopio: " + solicitud.getHorarioRecepcion().getPuntoAcopioDonacion().getPuntoAcopio().getId_punto_acopio());
        System.out.println("ID Solicitud: " + solicitud.getIdSolicitudDonacionProductos());

    }
    //Método para obtener los datos del albergue
    public Usuario obtenerDatosAlbergue(int idAlbergue) {
        Usuario albergue = null;
        String sql = "SELECT u.id_usuario, u.nombre_albergue, u.foto_perfil FROM Usuario u  WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAlbergue);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Se ha leido correctamente la data del usuario");
                albergue = new Usuario();
                albergue.setId_usuario(rs.getInt("id_usuario"));
                albergue.setNombre_albergue(rs.getString("nombre_albergue"));
                byte[] fotoUsuario = rs.getBytes("foto_perfil");
                if (fotoUsuario != null) {
                    albergue.setFoto_perfil(fotoUsuario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albergue;
    }


}
