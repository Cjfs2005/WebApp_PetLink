package com.example.webapp_petlink.daos;

import java.sql.*;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.Rol;
import com.example.webapp_petlink.beans.Zona;
import com.example.webapp_petlink.beans.PostulacionHogarTemporal;

public class LoginDao extends DaoBase{

    public Usuario obtenerUsuario(String correo, String contrasenia) {
        Usuario usuario = null;
        String sql = "SELECT u.*, r.nombre_rol, d.nombre_distrito, z.nombre_zona, p.* " +
                "FROM usuario u " +
                "LEFT JOIN rol r ON u.id_rol = r.id_rol " +
                "LEFT JOIN distrito d ON u.id_distrito = d.id_distrito " +
                "LEFT JOIN zona z ON d.id_zona = z.id_zona " +
                "LEFT JOIN postulacionhogartemporal p ON u.id_ultima_postulacion_hogar_temporal = p.id_postulacion_hogar_temporal " +
                "WHERE u.correo_electronico = ? AND u.contrasenia = ? AND u.es_usuario_activo = 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, contrasenia);

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

        // Asignación de datos del Usuario
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setDni(rs.getString("dni"));
        usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
        usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setCorreo_electronico(rs.getString("correo_electronico"));
        usuario.setFoto_perfil(rs.getBytes("foto_perfil"));
        usuario.setNombre_foto_perfil(rs.getString("nombre_foto_perfil"));
        usuario.setContrasenia(rs.getString("contrasenia"));
        usuario.setEs_contrasenia_temporal(rs.getBoolean("es_contrasenia_temporal"));
        usuario.setFecha_hora_expiracion_contrasenia(
                rs.getTimestamp("fecha_hora_expiracion_contrasenia") != null ?
                        rs.getTimestamp("fecha_hora_expiracion_contrasenia").toLocalDateTime() : null);
        usuario.setEs_primera_contrasenia_temporal(rs.getBoolean("es_primera_contrasenia_temporal"));
        usuario.setEs_usuario_activo(rs.getBoolean("es_usuario_activo"));
        usuario.setFecha_hora_creacion(
                rs.getTimestamp("fecha_hora_creacion") != null ?
                        rs.getTimestamp("fecha_hora_creacion").toLocalDateTime() : null);
        usuario.setFecha_hora_eliminacion(
                rs.getTimestamp("fecha_hora_eliminacion") != null ?
                        rs.getTimestamp("fecha_hora_eliminacion").toLocalDateTime() : null);
        usuario.setNombre_albergue(rs.getString("nombre_albergue"));
        usuario.setNombres_encargado(rs.getString("nombres_encargado"));
        usuario.setApellidos_encargado(rs.getString("apellidos_encargado"));
        usuario.setAnio_creacion(rs.getString("anio_creacion"));
        usuario.setCantidad_animales(rs.getInt("cantidad_animales"));
        usuario.setEspacio_disponible(rs.getInt("espacio_disponible"));
        usuario.setUrl_instagram(rs.getString("url_instagram"));
        usuario.setFoto_de_portada_albergue(rs.getBytes("foto_de_portada_albergue"));
        usuario.setNombre_foto_de_portada(rs.getString("nombre_foto_de_portada"));
        usuario.setLogo_albergue(rs.getBytes("logo_albergue"));
        usuario.setNombre_logo_albergue(rs.getString("nombre_logo_albergue"));
        usuario.setDireccion_donaciones(rs.getString("direccion_donaciones"));
        usuario.setNombre_contacto_donaciones(rs.getString("nombre_contacto_donaciones"));
        usuario.setNumero_contacto_donaciones(rs.getString("numero_contacto_donaciones"));
        usuario.setNumero_yape_plin(rs.getString("numero_yape_plin"));
        usuario.setNombre_imagen_qr(rs.getString("nombre_imagen_qr"));
        usuario.setImagen_qr(rs.getBytes("imagen_qr"));
        usuario.setTiene_registro_completo(rs.getBoolean("tiene_registro_completo"));
        usuario.setNombres_coordinador(rs.getString("nombres_coordinador"));
        usuario.setApellidos_coordinador(rs.getString("apellidos_coordinador"));
        usuario.setFecha_nacimiento(
                rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null);
        usuario.setDescripcion_perfil(rs.getString("descripcion_perfil"));

        // Asignación del Rol
        if (rs.getObject("id_rol") != null) { // Verificar si el rol es null
            Rol rol = new Rol();
            rol.setId_rol(rs.getInt("id_rol"));
            rol.setNombre_rol(rs.getString("nombre_rol"));
            usuario.setRol(rol);
        }

        // Asignación del Distrito y Zona
        if (rs.getObject("id_distrito") != null) { // Verificar si el distrito es null
            Distrito distrito = new Distrito();
            distrito.setId_distrito(rs.getInt("id_distrito"));
            distrito.setNombre_distrito(rs.getString("nombre_distrito"));

            if (rs.getObject("id_zona") != null) { // Verificar si la zona es null
                Zona zona = new Zona();
                zona.setId_zona(rs.getInt("id_zona"));
                zona.setNombre_zona(rs.getString("nombre_zona"));
                distrito.setZona(zona);
            }

            usuario.setDistrito(distrito);
        }

        // Asignación de la última Postulación Hogar Temporal
        if (rs.getObject("id_postulacion_hogar_temporal") != null) { // Verificar si la postulación es null
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

            usuario.setUltima_postulacion_hogar_temporal(postulacion);
        }

        return usuario;
    }
}
