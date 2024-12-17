package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CoordinadorZonalHogarTemporalDao extends DaoBase {

    public List<PostulacionHogarTemporal> obtenerPostulacionesPendientes() {
        List<PostulacionHogarTemporal> postulaciones = new ArrayList<>();
        String sql = """
        SELECT 
            p.id_postulacion_hogar_temporal, -- Agregado el campo ID
            u.dni AS dni_usuario,
            CONCAT(u.nombres_usuario_final, ' ', u.apellidos_usuario_final) AS nombres_apellidos,
            u.direccion AS distrito_usuario,
            p.celular_usuario,
            p.llamo_al_postulante,
            p.fecha_visita,
            p.fecha_hora_registro
        FROM 
            PostulacionHogarTemporal p
        INNER JOIN Usuario u ON p.id_usuario_final = u.id_usuario
        WHERE 
            p.id_estado = 1;
        """;

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                PostulacionHogarTemporal postulacion = new PostulacionHogarTemporal();

                // Asignar el ID de la postulación
                postulacion.setId_postulacion_hogar_temporal(resultSet.getInt("id_postulacion_hogar_temporal"));

                // Mapear datos del usuario
                Usuario usuario = new Usuario();
                usuario.setDni(resultSet.getString("dni_usuario"));
                usuario.setNombres_usuario_final(resultSet.getString("nombres_apellidos")); // Combina nombres y apellidos
                usuario.setDireccion(resultSet.getString("distrito_usuario")); // Considerado como distrito aquí
                postulacion.setUsuario_final(usuario);

                // Asignar otros campos de la postulación
                postulacion.setCelular_usuario(resultSet.getString("celular_usuario"));
                postulacion.setLlamo_al_postulante(resultSet.getBoolean("llamo_al_postulante"));
                postulacion.setFecha_visita(resultSet.getDate("fecha_visita") != null ? resultSet.getDate("fecha_visita").toLocalDate() : null);
                postulacion.setFecha_hora_registro(resultSet.getTimestamp("fecha_hora_registro").toLocalDateTime());

                // Agregar postulación a la lista
                postulaciones.add(postulacion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return postulaciones;
    }


    public boolean actualizarFechaVisita(int idPostulacionHogarTemporal, java.time.LocalDate fechaVisita) {
        String sql = """
                UPDATE PostulacionHogarTemporal
                SET fecha_visita = ?
                WHERE id_postulacion_hogar_temporal = ?;
                """;

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, java.sql.Date.valueOf(fechaVisita));
            preparedStatement.setInt(2, idPostulacionHogarTemporal);

            return preparedStatement.executeUpdate() > 0; // Devuelve true si se actualizó al menos una fila.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para actualizar el estado de la llamada al postulante.
     *
     * @param idPostulacionHogarTemporal ID de la postulación.
     * @param llamoAlPostulante Nuevo estado de la llamada (true = sí, false = no).
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarEstadoLlamada(int idPostulacionHogarTemporal, boolean llamoAlPostulante) {
        String sql = """
                UPDATE PostulacionHogarTemporal
                SET llamo_al_postulante = ?
                WHERE id_postulacion_hogar_temporal = ?;
                """;

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBoolean(1, llamoAlPostulante);
            preparedStatement.setInt(2, idPostulacionHogarTemporal);

            return preparedStatement.executeUpdate() > 0; // Devuelve true si se actualizó al menos una fila.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<FotoPostulacionHogarTemporal> obtenerDetallesPostulacionConFotos(int idPostulacionHogarTemporal) {
        List<FotoPostulacionHogarTemporal> fotos = new ArrayList<>();
        PostulacionHogarTemporal postulacion = null;

        String sqlPostulacion = """
    SELECT 
        p.id_postulacion_hogar_temporal,
        p.edad_usuario,
        p.genero_usuario,
        p.celular_usuario,
        p.cantidad_cuartos,
        p.metraje_vivienda,
        p.tiene_mascotas,
        p.tipo_mascotas,
        p.tiene_hijos,
        p.tiene_dependientes,
        p.forma_trabajo,
        p.nombre_persona_referencia,
        p.celular_persona_referencia,
        p.fecha_inicio_temporal,
        p.fecha_fin_temporal,
        p.fecha_hora_registro,
        p.cantidad_rechazos_consecutivos,
        p.llamo_al_postulante,
        p.fecha_visita,
        u.dni,
        u.nombres_usuario_final,
        u.apellidos_usuario_final,
        u.direccion,
        d.id_distrito,
        d.nombre_distrito,
        e.id_estado,
        e.nombre_estado
    FROM 
        PostulacionHogarTemporal p
    INNER JOIN Usuario u ON p.id_usuario_final = u.id_usuario
    INNER JOIN Estado e ON p.id_estado = e.id_estado
    LEFT JOIN Distrito d ON u.id_distrito = d.id_distrito
    WHERE 
        p.id_postulacion_hogar_temporal = ?;
    """;

        String sqlFotos = """
            SELECT 
                f.id_foto_postulacion_hogar_temporal,
                f.foto_lugar_temporal,
                f.nombre_foto_lugar_temporal
            FROM 
                FotoPostulacionHogarTemporal f
            WHERE 
                f.id_postulacion_hogar_temporal = ?;
            """;

        try (Connection connection = this.getConnection()) {
            // Obtener datos de la postulación
            try (PreparedStatement pstPostulacion = connection.prepareStatement(sqlPostulacion)) {
                pstPostulacion.setInt(1, idPostulacionHogarTemporal);
                try (ResultSet rsPostulacion = pstPostulacion.executeQuery()) {
                    if (rsPostulacion.next()) {
                        postulacion = new PostulacionHogarTemporal();
                        postulacion.setId_postulacion_hogar_temporal(rsPostulacion.getInt("id_postulacion_hogar_temporal"));
                        postulacion.setEdad_usuario(rsPostulacion.getString("edad_usuario"));
                        postulacion.setGenero_usuario(rsPostulacion.getString("genero_usuario"));
                        postulacion.setCelular_usuario(rsPostulacion.getString("celular_usuario"));
                        postulacion.setCantidad_cuartos(rsPostulacion.getString("cantidad_cuartos"));
                        postulacion.setMetraje_vivienda(rsPostulacion.getString("metraje_vivienda"));
                        postulacion.setTiene_mascotas(rsPostulacion.getBoolean("tiene_mascotas"));
                        postulacion.setTipo_mascotas(rsPostulacion.getString("tipo_mascotas"));
                        postulacion.setTiene_hijos(rsPostulacion.getBoolean("tiene_hijos"));
                        postulacion.setTiene_dependientes(rsPostulacion.getBoolean("tiene_dependientes"));
                        postulacion.setForma_trabajo(rsPostulacion.getString("forma_trabajo"));
                        postulacion.setNombre_persona_referencia(rsPostulacion.getString("nombre_persona_referencia"));
                        postulacion.setCelular_persona_referencia(rsPostulacion.getString("celular_persona_referencia"));
                        postulacion.setFecha_inicio_temporal(rsPostulacion.getDate("fecha_inicio_temporal").toLocalDate());
                        postulacion.setFecha_fin_temporal(rsPostulacion.getDate("fecha_fin_temporal").toLocalDate());
                        postulacion.setFecha_hora_registro(rsPostulacion.getTimestamp("fecha_hora_registro").toLocalDateTime());
                        postulacion.setCantidad_rechazos_consecutivos(rsPostulacion.getInt("cantidad_rechazos_consecutivos"));
                        postulacion.setLlamo_al_postulante(rsPostulacion.getBoolean("llamo_al_postulante"));
                        postulacion.setFecha_visita(rsPostulacion.getDate("fecha_visita") != null ? rsPostulacion.getDate("fecha_visita").toLocalDate() : null);

                        // Mapear usuario
                        Usuario usuario = new Usuario();
                        usuario.setDni(rsPostulacion.getString("dni"));
                        usuario.setNombres_usuario_final(rsPostulacion.getString("nombres_usuario_final"));
                        usuario.setApellidos_usuario_final(rsPostulacion.getString("apellidos_usuario_final"));
                        usuario.setDireccion(rsPostulacion.getString("direccion"));
                        postulacion.setUsuario_final(usuario);

                        // Mapear distrito
                        Distrito distrito = new Distrito();
                        distrito.setId_distrito(rsPostulacion.getInt("id_distrito"));
                        distrito.setNombre_distrito(rsPostulacion.getString("nombre_distrito"));
                        usuario.setDistrito(distrito);

                        postulacion.setUsuario_final(usuario);

                        // Mapear estado
                        Estado estado = new Estado();
                        estado.setId_estado(rsPostulacion.getInt("id_estado"));
                        estado.setNombre_estado(rsPostulacion.getString("nombre_estado"));
                        postulacion.setEstado(estado);
                    }
                }
            }

            // Obtener imágenes asociadas
            if (postulacion != null) {
                try (PreparedStatement pstFotos = connection.prepareStatement(sqlFotos)) {
                    pstFotos.setInt(1, idPostulacionHogarTemporal);
                    try (ResultSet rsFotos = pstFotos.executeQuery()) {
                        while (rsFotos.next()) {
                            FotoPostulacionHogarTemporal foto = new FotoPostulacionHogarTemporal();
                            foto.setIdFotoPostulacionHogarTemporal(rsFotos.getInt("id_foto_postulacion_hogar_temporal"));
                            foto.setFotoLugarTemporal(rsFotos.getBytes("foto_lugar_temporal"));
                            foto.setNombreFotoLugarTemporal(rsFotos.getString("nombre_foto_lugar_temporal"));
                            foto.setPostulacionHogarTemporal(postulacion); // Asociar la postulación completa
                            fotos.add(foto); // Agregar cada imagen a la lista
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fotos;
    }

    public boolean actualizarEstadoPostulacion(int idPostulacionHogarTemporal, int nuevoEstado) {
        String sql = """
            UPDATE PostulacionHogarTemporal
            SET id_estado = ?
            WHERE id_postulacion_hogar_temporal = ?;
            """;

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, nuevoEstado);
            preparedStatement.setInt(2, idPostulacionHogarTemporal);

            return preparedStatement.executeUpdate() > 0; // Retorna true si al menos una fila fue afectada
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
