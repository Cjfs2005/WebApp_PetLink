package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
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
        String query = "SELECT pma.id_publicacion_mascota_adopcion, pma.nombre_mascota, pma.foto_mascota FROM PublicacionMascotaAdopcion pma WHERE pma.id_usuario_albergue=? order by pma.id_publicacion_mascota_adopcion desc";

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

    public void guardarPublicacionAdopcion(PublicacionMascotaAdopcion publicacion){
        String sql = "INSERT INTO `PublicacionMascotaAdopcion` (\n" +
                "    `tipo_raza`, \n" +
                "    `lugar_encontrado`, \n" +
                "    `descripcion_mascota`, \n" +
                "    `edad_aproximada`, \n" +
                "    `genero_mascota`, \n" +
                "    `foto_mascota`, \n" +
                "    `nombre_foto_mascota`, \n" +
                "    `esta_en_temporal`, \n" +
                "    `condiciones_adopcion`, \n" +
                "    `es_publicacion_activa`, \n" +
                "    `fecha_hora_registro`, \n" +
                "    `id_usuario_albergue`, \n" +
                "    `id_estado`, \n" +
                "    `nombre_mascota`, \n" +
                "    `id_adoptante`\n" +
                ") VALUES (\n" +
                "    ?,  -- tipo_raza\n" +
                "    ?,  -- lugar_encontrado\n" +
                "    ?,  -- descripcion_mascota\n" +
                "    ?,  -- edad_aproximada\n" +
                "    ?,  -- genero_mascota\n" +
                "    ?,  -- foto_mascota (si es NULL, puedes pasar null)\n" +
                "    ?,  -- nombre_foto_mascota\n" +
                "    ?,  -- esta_en_temporal\n" +
                "    ?,  -- condiciones_adopcion\n" +
                "    ?,  -- es_publicacion_activa\n" +
                "    NOW(), -- fecha_hora_registro (esto se toma automáticamente como la hora actual)\n" +
                "    ?,  -- id_usuario_albergue\n" +
                "    ?,  -- id_estado\n" +
                "    ?,  -- nombre_mascota\n" +
                "    null   -- id_adoptante (si no tiene adoptante aún, puedes pasar null)\n" +
                ");\n";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, publicacion.getTipoRaza());
            pstmt.setString(2, publicacion.getLugarEncontrado());
            pstmt.setString(3, publicacion.getDescripcionMascota());
            pstmt.setString(4, publicacion.getEdadAproximada());
            pstmt.setString(5, publicacion.getGeneroMascota());
            pstmt.setBytes(6, publicacion.getFotoMascota());
            pstmt.setString(7, publicacion.getNombreMascota());
            pstmt.setBoolean(8,publicacion.isEstaEnTemporal());
            pstmt.setString(9, publicacion.getCondicionesAdopcion());
            pstmt.setBoolean(10, publicacion.isEsPublicacionActiva());
            pstmt.setInt(11,publicacion.getUsuarioAlbergue().getId_usuario());
            pstmt.setInt(12,publicacion.getEstado().getId_estado());
            pstmt.setString(13,publicacion.getNombreMascota());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicacionMascotaAdopcion obtenerPublicacionPorId(int idPublicacionMascotaAdopcion) {
        PublicacionMascotaAdopcion publicacion = null;
        String sql = "SELECT pma.id_publicacion_mascota_adopcion, pma.tipo_raza, pma.lugar_encontrado, \n" +
                "       pma.descripcion_mascota, pma.edad_aproximada, pma.genero_mascota, \n" +
                "       pma.foto_mascota, pma.nombre_foto_mascota, pma.esta_en_temporal, \n" +
                "       pma.condiciones_adopcion, pma.es_publicacion_activa, pma.fecha_hora_registro, \n" +
                "       pma.id_usuario_albergue, pma.id_estado, pma.nombre_mascota, pma.id_adoptante, \n" +
                "       e.nombre_estado, u.id_usuario, u.nombres_usuario_final, u.apellidos_usuario_final\n" +
                "FROM PublicacionMascotaAdopcion pma\n" +
                "JOIN Estado e ON e.id_estado = pma.id_estado\n" +
                "LEFT JOIN Usuario u ON u.id_usuario = pma.id_adoptante\n" +
                "WHERE pma.id_publicacion_mascota_adopcion = ?\n";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPublicacionMascotaAdopcion); // Establecer el id de la publicación

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    publicacion = new PublicacionMascotaAdopcion();
                    publicacion.setIdPublicacionMascotaAdopcion(rs.getInt("id_publicacion_mascota_adopcion"));
                    publicacion.setTipoRaza(rs.getString("tipo_raza"));
                    publicacion.setLugarEncontrado(rs.getString("lugar_encontrado"));
                    publicacion.setDescripcionMascota(rs.getString("descripcion_mascota"));
                    publicacion.setEdadAproximada(rs.getString("edad_aproximada"));
                    publicacion.setGeneroMascota(rs.getString("genero_mascota"));
                    publicacion.setFotoMascota(rs.getBytes("foto_mascota"));
                    publicacion.setNombreFotoMascota(rs.getString("nombre_foto_mascota"));
                    publicacion.setEstaEnTemporal(rs.getBoolean("esta_en_temporal"));
                    publicacion.setCondicionesAdopcion(rs.getString("condiciones_adopcion"));
                    publicacion.setEsPublicacionActiva(rs.getBoolean("es_publicacion_activa"));
                    publicacion.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro"));
                    publicacion.setNombreMascota(rs.getString("nombre_mascota"));

                    Estado estado = new Estado();
                    estado.setId_estado(rs.getInt("id_estado"));
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                    publicacion.setEstado(estado);

                    Usuario adoptante = new Usuario();
                    adoptante.setId_usuario(rs.getInt("id_usuario"));
                    adoptante.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    adoptante.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                    publicacion.setAdoptante(adoptante);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la publicación", e);
        }

        return publicacion;
    }
    public ArrayList<PostulacionMascotaAdopcion> obtenerPostulacionesPorId(int idPublicacionMascotaAdopcion) {
        ArrayList<PostulacionMascotaAdopcion> postulaciones = new ArrayList<>();
        String sql = "SELECT pma.id_postulacion_mascota_adopcion, pma.fecha_hora_registro, " +
                "pma.id_publicacion_mascota_adopcion, pma.id_usuario_final, pma.id_estado, " +
                "u.nombres_usuario_final, u.apellidos_usuario_final, u.dni, d.nombre_distrito, e.nombre_estado " +
                "FROM PostulacionMascotaAdopcion pma " +
                "LEFT JOIN Usuario u ON pma.id_usuario_final = u.id_usuario " +
                "LEFT JOIN Estado e ON pma.id_estado = e.id_estado " +
                "LEFT JOIN Distrito d ON d.id_distrito = u.id_distrito " +
                "WHERE pma.id_publicacion_mascota_adopcion = ? " +
                "ORDER BY pma.fecha_hora_registro desc";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPublicacionMascotaAdopcion);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PostulacionMascotaAdopcion postulacion = new PostulacionMascotaAdopcion();
                    postulacion.setIdPostulacionMascotaAdopcion(rs.getInt("id_postulacion_mascota_adopcion"));
                    postulacion.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro"));

                    PublicacionMascotaAdopcion publicacion = new PublicacionMascotaAdopcion();
                    publicacion.setIdPublicacionMascotaAdopcion(rs.getInt("id_publicacion_mascota_adopcion"));
                    postulacion.setPublicacionMascotaAdopcion(publicacion);

                    Usuario usuarioFinal = new Usuario();
                    usuarioFinal.setId_usuario(rs.getInt("id_usuario_final"));
                    usuarioFinal.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    usuarioFinal.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                    usuarioFinal.setDni(rs.getString("dni"));
                    Distrito dit = new Distrito();
                    dit.setNombre_distrito(rs.getString("nombre_distrito"));
                    usuarioFinal.setDistrito(dit);
                    postulacion.setUsuarioFinal(usuarioFinal);

                    Estado estado = new Estado();
                    estado.setId_estado(rs.getInt("id_estado"));
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                    postulacion.setEstado(estado);

                    postulaciones.add(postulacion);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las postulaciones", e);
        }

        return postulaciones;
    }

    public void aceptarPostulacion(int idPostulacion, int idPublicacion) {
        String sqlActualizarEstado = "UPDATE PostulacionMascotaAdopcion SET id_estado = ? WHERE id_postulacion_mascota_adopcion = ?";
        String sqlObtenerPostulaciones = "SELECT id_postulacion_mascota_adopcion FROM PostulacionMascotaAdopcion WHERE id_publicacion_mascota_adopcion = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmtObtener = conn.prepareStatement(sqlObtenerPostulaciones);
             PreparedStatement pstmtActualizar = conn.prepareStatement(sqlActualizarEstado)) {

            // Obtener todas las postulaciones de la publicación
            pstmtObtener.setInt(1, idPublicacion);
            try (ResultSet rs = pstmtObtener.executeQuery()) {
                while (rs.next()) {
                    int idPostulacionActual = rs.getInt("id_postulacion_mascota_adopcion");

                    // Si la postulación es la aceptada, se actualiza su estado a 2 (aceptada)
                    if (idPostulacionActual == idPostulacion) {
                        pstmtActualizar.setInt(1, 2); // Estado 2: Aceptada
                    } else {
                        // Si no es la aceptada, se actualiza su estado a 3 (rechazada)
                        pstmtActualizar.setInt(1, 3); // Estado 3: Rechazada
                    }

                    // Actualizar el estado de la postulación actual
                    pstmtActualizar.setInt(2, idPostulacionActual);
                    pstmtActualizar.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al procesar la aceptación de la postulación", e);
        }
    }


    public void actualizarAdoptantePublicacion(int idAdoptante, int idPublicacion) {
        String sql = "UPDATE PublicacionMascotaAdopcion SET id_adoptante = ? WHERE id_publicacion_mascota_adopcion = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el nuevo adoptante a la publicación
            pstmt.setInt(1, idAdoptante);
            pstmt.setInt(2, idPublicacion);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el adoptante de la publicación", e);
        }
    }

}
