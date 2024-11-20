package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdopcionesAlbergueDao extends DaoBase{

    //En esta ocasion asumiremos que el idUsuario = 6 (Albergue Esperanza)

    public Usuario obtenerUsuarioPorId(int idUsuario) {
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

        return usuario;
    }

    public ArrayList<PublicacionMascotaAdopcion> obtenerListaPublicacionesAdopcion(int idUsuario) {

        ArrayList<PublicacionMascotaAdopcion> listaPublicacionesAdopcion = new ArrayList<>();
        String query = "SELECT pma.id_publicacion_mascota_adopcion, pma.nombre_mascota, pma.foto_mascota FROM PublicacionMascotaAdopcion pma WHERE pma.id_usuario_albergue=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario); //Usuario albergue
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                PublicacionMascotaAdopcion publicacion = new PublicacionMascotaAdopcion();

                publicacion.setIdPublicacionMascotaAdopcion(rs.getInt("id_publicacion_mascota_adopcion"));

                String nombre = rs.getString("nombre_mascota");
                publicacion.setNombreMascota(nombre != null ? nombre : "");

                byte[] foto = rs.getBytes("foto_mascota");
                publicacion.setFotoMascota(foto != null ? foto : new byte[0]);

                listaPublicacionesAdopcion.add(publicacion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPublicacionesAdopcion;
    }

}
