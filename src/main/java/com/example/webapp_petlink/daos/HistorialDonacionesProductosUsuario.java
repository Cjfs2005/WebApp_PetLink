package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistorialDonacionesProductosUsuario extends DaoBase {

    public List<RegistroDonacionProductos> obtenerHistorialDonaciones(int idUsuarioFinal) {
        List<RegistroDonacionProductos> historial = new ArrayList<>();
        String sql = "SELECT " +
                "r.id_registro_donacion_productos, " +
                "r.descripciones_donaciones AS productos_donados, " +
                "r.fecha_hora_registro AS fecha_donacion, " +
                "u.id_usuario AS id_donante, " +
                "u.nombres_usuario_final AS nombre_donante, " +
                "a.id_usuario AS id_albergue, " +
                "a.nombre_albergue AS nombre_albergue, " +
                "s.descripcion_donaciones AS productos_solicitados, " +
                "p.direccion_punto_acopio AS punto_acopio, " +
                "h.fecha_hora_inicio AS horario_inicio, " +
                "h.fecha_hora_fin AS horario_fin, " +
                "e.nombre_estado AS estado_donacion " +
                "FROM RegistroDonacionProductos r " +
                "INNER JOIN Usuario u ON r.id_usuario_final = u.id_usuario " +
                "INNER JOIN HorarioRecepcionDonacion h ON r.id_horario_recepcion_donacion = h.id_horario_recepcion_donacion " +
                "INNER JOIN PuntoAcopioDonacion pd ON h.id_punto_acopio_donacion = pd.id_punto_acopio_donacion " +
                "INNER JOIN PuntoAcopio p ON pd.id_punto_acopio = p.id_punto_acopio " +
                "INNER JOIN SolicitudDonacionProductos s ON pd.id_solicitud_donacion_productos = s.id_solicitud_donacion_productos " +
                "INNER JOIN Usuario a ON s.id_usuario_albergue = a.id_usuario " +
                "INNER JOIN Estado e ON r.id_estado = e.id_estado " +
                "WHERE u.id_usuario = ? " +
                "AND r.id_estado = 2";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioFinal); // Parámetro para el ID del usuario

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear instancia de RegistroDonacionProductos
                    RegistroDonacionProductos registro = new RegistroDonacionProductos();
                    registro.setIdRegistroDonacionProductos(rs.getInt("id_registro_donacion_productos"));
                    registro.setDescripcionesDonaciones(rs.getString("productos_donados"));
                    registro.setFechaHoraRegistro(rs.getTimestamp("fecha_donacion"));

                    // Estado
                    Estado estado = new Estado();
                    estado.setNombre_estado(rs.getString("estado_donacion"));
                    registro.setEstado(estado);

                    // Horario y punto de acopio
                    HorarioRecepcionDonacion horario = new HorarioRecepcionDonacion();
                    horario.setFechaHoraInicio(rs.getTimestamp("horario_inicio").toLocalDateTime());
                    horario.setFechaHoraFin(rs.getTimestamp("horario_fin").toLocalDateTime());

                    PuntoAcopio puntoAcopio = new PuntoAcopio();
                    puntoAcopio.setDireccion_punto_acopio(rs.getString("punto_acopio"));

                    PuntoAcopioDonacion puntoAcopioDonacion = new PuntoAcopioDonacion();
                    puntoAcopioDonacion.setPuntoAcopio(puntoAcopio);

                    SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
                    solicitud.setDescripcionDonaciones(rs.getString("productos_solicitados"));

                    puntoAcopioDonacion.setSolicitudDonacionProductos(solicitud);
                    horario.setPuntoAcopioDonacion(puntoAcopioDonacion);
                    registro.setHorarioRecepcionDonacion(horario);

                    // Albergue
                    Usuario albergue = new Usuario();
                    albergue.setId_usuario(rs.getInt("id_albergue"));
                    albergue.setNombre_albergue(rs.getString("nombre_albergue"));
                    solicitud.setUsuarioAlbergue(albergue);

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
