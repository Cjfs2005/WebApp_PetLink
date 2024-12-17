package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoPerfilUsuario extends DaoBase {

    public Usuario obtenerPerfilUsuario(int idUsuario) {
        Usuario usuario = null;
        String sql = "SELECT u.*, r.id_rol, r.nombre_rol, d.id_distrito, d.nombre_distrito, z.id_zona, z.nombre_zona, "
                + "p.*, e.id_estado AS estado_id, e.nombre_estado "
                + "FROM Usuario u "
                + "LEFT JOIN Rol r ON u.id_rol = r.id_rol "
                + "LEFT JOIN Distrito d ON u.id_distrito = d.id_distrito "
                + "LEFT JOIN Zona z ON d.id_zona = z.id_zona "
                + "LEFT JOIN PostulacionHogarTemporal p ON u.id_ultima_postulacion_hogar_temporal = p.id_postulacion_hogar_temporal "
                + "LEFT JOIN Estado e ON p.id_estado = e.id_estado "
                + "WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = fetchUsuarioData(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usuario;
    }

    private Usuario fetchUsuarioData(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        // Asignación de datos del Usuario con validación
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setDni(rs.getString("dni") != null ? rs.getString("dni") : null);
        usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final") != null ? rs.getString("nombres_usuario_final") : null);
        usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final") != null ? rs.getString("apellidos_usuario_final") : null);
        usuario.setDireccion(rs.getString("direccion") != null ? rs.getString("direccion") : null);
        usuario.setCorreo_electronico(rs.getString("correo_electronico") != null ? rs.getString("correo_electronico") : null);
        usuario.setFoto_perfil(rs.getBytes("foto_perfil") != null ? rs.getBytes("foto_perfil") : null);
        usuario.setNombre_foto_perfil(rs.getString("nombre_foto_perfil") != null ? rs.getString("nombre_foto_perfil") : null);
        usuario.setContrasenia(rs.getString("contrasenia") != null ? rs.getString("contrasenia") : null);
        usuario.setEs_contrasenia_temporal(rs.getObject("es_contrasenia_temporal") != null && rs.getBoolean("es_contrasenia_temporal"));
        usuario.setFecha_hora_expiracion_contrasenia(rs.getTimestamp("fecha_hora_expiracion_contrasenia") != null
                ? rs.getTimestamp("fecha_hora_expiracion_contrasenia").toLocalDateTime() : null);
        usuario.setEs_primera_contrasenia_temporal(rs.getObject("es_primera_contrasenia_temporal") != null && rs.getBoolean("es_primera_contrasenia_temporal"));
        usuario.setEs_usuario_activo(rs.getObject("es_usuario_activo") != null && rs.getBoolean("es_usuario_activo"));
        usuario.setFecha_hora_creacion(rs.getTimestamp("fecha_hora_creacion") != null
                ? rs.getTimestamp("fecha_hora_creacion").toLocalDateTime() : null);
        usuario.setFecha_hora_eliminacion(rs.getTimestamp("fecha_hora_eliminacion") != null
                ? rs.getTimestamp("fecha_hora_eliminacion").toLocalDateTime() : null);
        usuario.setNombre_albergue(rs.getString("nombre_albergue") != null ? rs.getString("nombre_albergue") : null);
        usuario.setNombres_encargado(rs.getString("nombres_encargado") != null ? rs.getString("nombres_encargado") : null);
        usuario.setApellidos_encargado(rs.getString("apellidos_encargado") != null ? rs.getString("apellidos_encargado") : null);
        usuario.setAnio_creacion(rs.getString("anio_creacion") != null ? rs.getString("anio_creacion") : null);
        usuario.setCantidad_animales(rs.getObject("cantidad_animales") != null ? rs.getInt("cantidad_animales") : 0);
        usuario.setEspacio_disponible(rs.getObject("espacio_disponible") != null ? rs.getInt("espacio_disponible") : 0);
        usuario.setUrl_instagram(rs.getString("url_instagram") != null ? rs.getString("url_instagram") : null);
        usuario.setFoto_de_portada_albergue(rs.getBytes("foto_de_portada_albergue") != null ? rs.getBytes("foto_de_portada_albergue") : null);
        usuario.setNombre_foto_de_portada(rs.getString("nombre_foto_de_portada") != null ? rs.getString("nombre_foto_de_portada") : null);
        usuario.setLogo_albergue(rs.getBytes("logo_albergue") != null ? rs.getBytes("logo_albergue") : null);
        usuario.setNombre_logo_albergue(rs.getString("nombre_logo_albergue") != null ? rs.getString("nombre_logo_albergue") : null);
        usuario.setDireccion_donaciones(rs.getString("direccion_donaciones") != null ? rs.getString("direccion_donaciones") : null);
        usuario.setNombre_contacto_donaciones(rs.getString("nombre_contacto_donaciones") != null ? rs.getString("nombre_contacto_donaciones") : null);
        usuario.setNumero_contacto_donaciones(rs.getString("numero_contacto_donaciones") != null ? rs.getString("numero_contacto_donaciones") : null);
        usuario.setNumero_yape_plin(rs.getString("numero_yape_plin") != null ? rs.getString("numero_yape_plin") : null);
        usuario.setNombre_imagen_qr(rs.getString("nombre_imagen_qr") != null ? rs.getString("nombre_imagen_qr") : null);
        usuario.setImagen_qr(rs.getBytes("imagen_qr") != null ? rs.getBytes("imagen_qr") : null);
        usuario.setTiene_registro_completo(rs.getObject("tiene_registro_completo") != null && rs.getBoolean("tiene_registro_completo"));
        usuario.setNombres_coordinador(rs.getString("nombres_coordinador") != null ? rs.getString("nombres_coordinador") : null);
        usuario.setApellidos_coordinador(rs.getString("apellidos_coordinador") != null ? rs.getString("apellidos_coordinador") : null);
        usuario.setFecha_nacimiento(rs.getDate("fecha_nacimiento") != null
                ? rs.getDate("fecha_nacimiento").toLocalDate() : null);
        usuario.setDescripcion_perfil(rs.getString("descripcion_perfil") != null ? rs.getString("descripcion_perfil") : null);

        // Asignación del Rol
        if (rs.getObject("id_rol") != null) {
            Rol rol = new Rol();
            rol.setId_rol(rs.getInt("id_rol"));
            rol.setNombre_rol(rs.getString("nombre_rol"));
            usuario.setRol(rol);
        }

        // Asignación del Distrito y Zona
        if (rs.getObject("id_distrito") != null) {
            Distrito distrito = new Distrito();
            distrito.setId_distrito(rs.getInt("id_distrito"));
            distrito.setNombre_distrito(rs.getString("nombre_distrito"));

            if (rs.getObject("id_zona") != null) {
                Zona zona = new Zona();
                zona.setId_zona(rs.getInt("id_zona"));
                zona.setNombre_zona(rs.getString("nombre_zona"));
                distrito.setZona(zona);
            }

            usuario.setDistrito(distrito);
        }

        // Asignación de la última Postulación Hogar Temporal
        if (rs.getObject("id_postulacion_hogar_temporal") != null) {
            PostulacionHogarTemporal postulacion = new PostulacionHogarTemporal();
            postulacion.setId_postulacion_hogar_temporal(rs.getInt("id_postulacion_hogar_temporal"));
            postulacion.setEdad_usuario(rs.getString("edad_usuario"));
            postulacion.setGenero_usuario(rs.getString("genero_usuario"));
            postulacion.setCelular_usuario(rs.getString("celular_usuario"));
            postulacion.setCantidad_cuartos(rs.getString("cantidad_cuartos"));
            postulacion.setMetraje_vivienda(rs.getString("metraje_vivienda"));
            postulacion.setTiene_mascotas(rs.getBoolean("tiene_mascotas"));
            postulacion.setTipo_mascotas(rs.getString("tipo_mascotas"));
            postulacion.setTiene_hijos(rs.getBoolean("tiene_hijos"));
            postulacion.setTiene_dependientes(rs.getBoolean("tiene_dependientes"));
            postulacion.setForma_trabajo(rs.getString("forma_trabajo"));
            postulacion.setNombre_persona_referencia(rs.getString("nombre_persona_referencia"));
            postulacion.setCelular_persona_referencia(rs.getString("celular_persona_referencia"));
            postulacion.setFecha_inicio_temporal(rs.getDate("fecha_inicio_temporal") != null ?
                    rs.getDate("fecha_inicio_temporal").toLocalDate() : null);
            postulacion.setFecha_fin_temporal(rs.getDate("fecha_fin_temporal") != null ?
                    rs.getDate("fecha_fin_temporal").toLocalDate() : null);
            postulacion.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro") != null ?
                    rs.getTimestamp("fecha_hora_registro").toLocalDateTime() : null);
            postulacion.setCantidad_rechazos_consecutivos(rs.getInt("cantidad_rechazos_consecutivos"));
            postulacion.setUsuario_final(usuario);

            // Asignar el objeto Estado a la PostulacionHogarTemporal usando fetchEstadoData si no es null
            if (rs.getObject("estado_id") != null) {
                Estado estado = new Estado();
                estado.setId_estado(rs.getInt("estado_id"));
                estado.setNombre_estado(rs.getString("nombre_estado"));
                postulacion.setEstado(estado);
            }

            usuario.setUltima_postulacion_hogar_temporal(postulacion);
        }

        return usuario;
    }

    public boolean modificarPerfilUsuario(Usuario usuario) {
        String query = "UPDATE Usuario SET dni = ?, nombres_usuario_final = ?, apellidos_usuario_final = ?, " +
                "direccion = ?, correo_electronico = ?, nombre_foto_perfil = ?, descripcion_perfil = ? " +
                "WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombres_usuario_final());
            stmt.setString(3, usuario.getApellidos_usuario_final());
            stmt.setString(4, usuario.getDireccion());
            stmt.setString(5, usuario.getCorreo_electronico());
            stmt.setString(6, usuario.getNombre_foto_perfil());
            stmt.setString(7, usuario.getDescripcion_perfil());
            stmt.setInt(8, usuario.getId_usuario());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPerfilUsuario(int idUsuario, String nombres, String apellidos, byte[] foto, String nombre_foto) {
        String query = "UPDATE Usuario SET nombres_usuario_final = ?, apellidos_usuario_final = ?, foto_perfil = ?, nombre_foto_perfil = ?" +
                "WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombres);
            stmt.setString(2, apellidos);
            stmt.setBytes(3, foto);
            stmt.setString(4, nombre_foto);
            stmt.setInt(5, idUsuario);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}