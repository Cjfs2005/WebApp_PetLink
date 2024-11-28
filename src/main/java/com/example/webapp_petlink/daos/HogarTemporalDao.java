package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.Rol;
import com.example.webapp_petlink.beans.Zona;
import com.example.webapp_petlink.beans.PostulacionHogarTemporal;
import com.example.webapp_petlink.beans.SolicitudHogarTemporal;
import com.example.webapp_petlink.beans.Estado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.sql.Types;

public class HogarTemporalDao extends DaoBase {

    public boolean isUsuarioBaneado(int userId) {
        String sql = "SELECT COUNT(*) FROM BaneoHogarTemporal WHERE id_usuario_final = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

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


    public Estado fetchEstadoData(int idEstado) {
        Estado estado = null;
        String sql = "SELECT * FROM Estado WHERE id_estado = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEstado);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado();
                    estado.setId_estado(rs.getInt("id_estado"));
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return estado;
    }

    public ArrayList<SolicitudHogarTemporal> obtenerSolicitudesPorPostulacion(PostulacionHogarTemporal postulacion) {
        ArrayList<SolicitudHogarTemporal> solicitudes = new ArrayList<>();
        String sql = "SELECT s.*, u.id_usuario, u.nombre_albergue, u.id_rol, e.id_estado AS estado_id, e.nombre_estado " +
                "FROM SolicitudHogarTemporal s " +
                "LEFT JOIN Usuario u ON s.id_usuario_albergue = u.id_usuario " +
                "LEFT JOIN Estado e ON s.id_estado = e.id_estado " +
                "WHERE s.id_postulacion_hogar_temporal = ? " +
                "ORDER BY s.id_solicitud_hogar_temporal DESC;";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postulacion.getId_postulacion_hogar_temporal());

            try (ResultSet rs = pstmt.executeQuery()) {
                LocalDate today = LocalDate.now();

                while (rs.next()) {
                    int idEstado = rs.getInt("estado_id");
                    Date fechaInicio = rs.getDate("fecha_inicio");
                    SolicitudHogarTemporal solicitud = new SolicitudHogarTemporal();
                    solicitud.setIdSolicitudHogarTemporal(rs.getInt("id_solicitud_hogar_temporal"));
                    solicitud.setNombreMascota(rs.getString("nombre_mascota"));
                    solicitud.setDescripcionMascota(rs.getString("descripcion_mascota"));
                    solicitud.setFechaInicio(fechaInicio);
                    solicitud.setFechaFin(rs.getDate("fecha_fin"));
                    solicitud.setNombreFotoMascota(rs.getString("nombre_foto_mascota"));
                    byte[] fotoMascota = rs.getBytes("foto_mascota");
                    if (fotoMascota != null) {
                        solicitud.setFotoMascota(fotoMascota);
                    }
                    solicitud.setPostulacionHogarTemporal(postulacion);

                    // Obtener y asignar el usuario albergue con solo los datos básicos
                    Usuario usuarioAlbergue = fetchUsuarioAlbergueData(rs);
                    solicitud.setUsuarioAlbergue(usuarioAlbergue);

                    // Obtener y asignar el estado
                    Estado estado = fetchEstadoData(idEstado);
                    solicitud.setEstado(estado);

                    // Validar si la solicitud está pendiente y la fecha ya pasó o es hoy
                    if (idEstado == 1 && (fechaInicio == null || !fechaInicio.toLocalDate().isAfter(today))) {
                        rechazarSolicitud(solicitud.getIdSolicitudHogarTemporal());
                        estado = fetchEstadoData(3);
                        solicitud.setEstado(estado);
                    }

                    solicitudes.add(solicitud);
                }
            }

            // Validar rechazos consecutivos para el postulante
            if (postulacion.getCantidad_rechazos_consecutivos() >= 3) {
                banearHogarTemporal(postulacion.getUsuario_final().getId_usuario());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return solicitudes;
    }

    public void banearHogarTemporal(int idUsuario) {
        String sql = "INSERT INTO BaneoHogarTemporal (motivo, fecha_hora_registro, id_usuario_final, tipo_de_baneo) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "Realizó 3 rechazos consecutivos");
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, idUsuario);
            pstmt.setInt(4, 0);//0 para baneos automáticos y 1 para baneos manuales

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private Usuario fetchUsuarioAlbergueData(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        // Solo asignar los campos básicos obtenidos en la consulta
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setNombre_albergue(rs.getString("nombre_albergue"));

        // Asignar el Rol solo con el ID, ya que no se obtiene `nombre_rol` en esta consulta
        if (rs.getObject("id_rol") != null) {
            Rol rol = new Rol();
            rol.setId_rol(rs.getInt("id_rol"));
            // No se asigna `nombre_rol` porque no está disponible en esta consulta
            usuario.setRol(rol);
        }

        // Se pueden asignar otros campos mínimos aquí si es necesario

        return usuario;
    }

    public ArrayList<Distrito> obtenerDistritos() {
        ArrayList<Distrito> distritos = new ArrayList<>();
        String sql = "SELECT id_distrito, nombre_distrito FROM Distrito";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Distrito distrito = new Distrito();
                distrito.setId_distrito(rs.getInt("id_distrito"));
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                distritos.add(distrito);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distritos;
    }
    public void guardarNuevaPostulacion(PostulacionHogarTemporal postulacion) {
        String sql = "INSERT INTO PostulacionHogarTemporal (id_usuario_final, edad_usuario, genero_usuario, celular_usuario, "
                + "cantidad_cuartos, metraje_vivienda, tiene_mascotas, tipo_mascotas, tiene_hijos, tiene_dependientes, "
                + "forma_trabajo, nombre_persona_referencia, celular_persona_referencia, fecha_inicio_temporal, "
                + "fecha_fin_temporal, fecha_hora_registro, cantidad_rechazos_consecutivos, id_estado, llamo_al_postulante, "
                + "fecha_visita) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postulacion.getUsuario_final().getId_usuario());
            pstmt.setString(2, postulacion.getEdad_usuario());
            pstmt.setString(3, postulacion.getGenero_usuario());
            pstmt.setString(4, postulacion.getCelular_usuario());
            pstmt.setString(5, postulacion.getCantidad_cuartos());
            pstmt.setString(6, postulacion.getMetraje_vivienda());
            pstmt.setBoolean(7, postulacion.getTiene_mascotas());
            pstmt.setString(8, postulacion.getTipo_mascotas());
            pstmt.setBoolean(9, postulacion.getTiene_hijos());
            pstmt.setBoolean(10, postulacion.getTiene_dependientes());
            pstmt.setString(11, postulacion.getForma_trabajo());
            pstmt.setString(12, postulacion.getNombre_persona_referencia());
            pstmt.setString(13, postulacion.getCelular_persona_referencia());
            pstmt.setDate(14, Date.valueOf(postulacion.getFecha_inicio_temporal()));
            pstmt.setDate(15, Date.valueOf(postulacion.getFecha_fin_temporal()));
            pstmt.setTimestamp(16, Timestamp.valueOf(postulacion.getFecha_hora_registro()));
            pstmt.setInt(17, postulacion.getCantidad_rechazos_consecutivos());
            pstmt.setInt(18, postulacion.getEstado().getId_estado());
            pstmt.setBoolean(19, postulacion.getLlamo_al_postulante());

            if (postulacion.getFecha_visita() != null) {
                pstmt.setTimestamp(20, Timestamp.valueOf(postulacion.getFecha_visita().atStartOfDay()));
            } else {
                pstmt.setNull(20, Types.TIMESTAMP);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modificarPostulacion(PostulacionHogarTemporal postulacion) {
        String sql = "UPDATE PostulacionHogarTemporal SET edad_usuario = ?, genero_usuario = ?, celular_usuario = ?, "
                + "cantidad_cuartos = ?, metraje_vivienda = ?, tiene_mascotas = ?, tipo_mascotas = ?, tiene_hijos = ?, "
                + "tiene_dependientes = ?, forma_trabajo = ?, nombre_persona_referencia = ?, celular_persona_referencia = ?, "
                + "fecha_inicio_temporal = ?, fecha_fin_temporal = ?, fecha_hora_registro = ?, cantidad_rechazos_consecutivos = ?, "
                + "id_estado = ?, llamo_al_postulante = ?, fecha_visita = ? WHERE id_postulacion_hogar_temporal = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postulacion.getEdad_usuario());
            pstmt.setString(2, postulacion.getGenero_usuario());
            pstmt.setString(3, postulacion.getCelular_usuario());
            pstmt.setString(4, postulacion.getCantidad_cuartos());
            pstmt.setString(5, postulacion.getMetraje_vivienda());
            pstmt.setBoolean(6, postulacion.getTiene_mascotas());
            pstmt.setString(7, postulacion.getTipo_mascotas());
            pstmt.setBoolean(8, postulacion.getTiene_hijos());
            pstmt.setBoolean(9, postulacion.getTiene_dependientes());
            pstmt.setString(10, postulacion.getForma_trabajo());
            pstmt.setString(11, postulacion.getNombre_persona_referencia());
            pstmt.setString(12, postulacion.getCelular_persona_referencia());
            pstmt.setDate(13, Date.valueOf(postulacion.getFecha_inicio_temporal()));
            pstmt.setDate(14, Date.valueOf(postulacion.getFecha_fin_temporal()));
            pstmt.setTimestamp(15, Timestamp.valueOf(postulacion.getFecha_hora_registro()));
            pstmt.setInt(16, postulacion.getCantidad_rechazos_consecutivos());
            pstmt.setInt(17, postulacion.getEstado().getId_estado());
            pstmt.setBoolean(18, postulacion.getLlamo_al_postulante());

            if (postulacion.getFecha_visita() != null) {
                pstmt.setTimestamp(19, Timestamp.valueOf(postulacion.getFecha_visita().atStartOfDay()));
            } else {
                pstmt.setNull(19, Types.TIMESTAMP);
            }

            pstmt.setInt(20, postulacion.getId_postulacion_hogar_temporal());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void actualizarUltimaPostulacionUsuario(Usuario usuario) {
        String selectLatestPostulacionSql =
                "SELECT MAX(id_postulacion_hogar_temporal) AS ultima_postulacion_id " +
                        "FROM PostulacionHogarTemporal WHERE id_usuario_final = ?";
        String updateUsuarioSql =
                "UPDATE Usuario SET id_ultima_postulacion_hogar_temporal = ? WHERE id_usuario = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectLatestPostulacionSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateUsuarioSql)) {

            // Obtener el ID de la última postulación del usuario
            selectStmt.setInt(1, usuario.getId_usuario());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int ultimaPostulacionId = rs.getInt("ultima_postulacion_id");

                // Actualizar el campo id_ultima_postulacion_hogar_temporal en la tabla Usuario
                updateStmt.setInt(1, ultimaPostulacionId);
                updateStmt.setInt(2, usuario.getId_usuario());
                updateStmt.executeUpdate();

                System.out.println("Última postulación actualizada con éxito para el usuario con ID: " + usuario.getId_usuario());
            } else {
                System.out.println("No se encontró ninguna postulación para el usuario con ID: " + usuario.getId_usuario());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int obtenerIdUltimaPostulacionUsuario(int idUsuario) {
        String sql = "SELECT MAX(id_postulacion_hogar_temporal) AS ultima_postulacion_id " +
                "FROM PostulacionHogarTemporal WHERE id_usuario_final = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ultima_postulacion_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indicar que no se encontró ninguna postulación si retorna -1
    }
    public void guardarFotoPostulacion(int idPostulacion, byte[] foto, String nombreFoto) {
        String sql = "INSERT INTO FotoPostulacionHogarTemporal (id_postulacion_hogar_temporal, foto_lugar_temporal, nombre_foto_lugar_temporal) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPostulacion);
            pstmt.setBytes(2, foto);
            pstmt.setString(3, nombreFoto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<PostulacionHogarTemporal> obtenerHogaresTemporalesDisponibles() {
        ArrayList<PostulacionHogarTemporal> postulacionesDisponibles = new ArrayList<>();

        String sql = """
        SELECT u.*, p.*, s.id_solicitud_hogar_temporal, s.id_estado, s.fecha_inicio, s.fecha_fin
        FROM Usuario u
        JOIN PostulacionHogarTemporal p ON u.id_ultima_postulacion_hogar_temporal = p.id_postulacion_hogar_temporal
        LEFT JOIN (
            SELECT id_postulacion_hogar_temporal, MAX(id_solicitud_hogar_temporal) AS ultima_solicitud_id
            FROM SolicitudHogarTemporal
            GROUP BY id_postulacion_hogar_temporal
        ) sub ON p.id_postulacion_hogar_temporal = sub.id_postulacion_hogar_temporal
        LEFT JOIN SolicitudHogarTemporal s ON sub.ultima_solicitud_id = s.id_solicitud_hogar_temporal
        LEFT JOIN BaneoHogarTemporal b ON u.id_usuario = b.id_usuario_final
        WHERE p.id_estado = 2
          AND b.id_usuario_final IS NULL  -- Excluir usuarios baneados
          AND (
              s.id_solicitud_hogar_temporal IS NULL OR
              (s.id_estado = 1 AND CURDATE() > s.fecha_inicio) OR
              (s.id_estado = 2 AND CURDATE() > s.fecha_fin) OR
              (s.id_estado = 3)
          );
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
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
                postulacion.setFecha_inicio_temporal(rs.getDate("fecha_inicio_temporal").toLocalDate());
                postulacion.setFecha_fin_temporal(rs.getDate("fecha_fin_temporal").toLocalDate());
                postulacion.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

                // Obtener usuario asociado a la postulación
                int idUsuario = rs.getInt("id_usuario_final");
                Usuario usuario = obtenerUsuarioPorId(idUsuario);
                postulacion.setUsuario_final(usuario);

                postulacionesDisponibles.add(postulacion);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return postulacionesDisponibles;
    }
    public void aceptarSolicitud(int idSolicitud) {
        String updateSolicitudSql = "UPDATE SolicitudHogarTemporal SET id_estado = ? WHERE id_solicitud_hogar_temporal = ?";
        String selectPostulacionSql = "SELECT id_postulacion_hogar_temporal FROM SolicitudHogarTemporal WHERE id_solicitud_hogar_temporal = ?";
        String updatePostulacionSql = "UPDATE PostulacionHogarTemporal SET cantidad_rechazos_consecutivos = 0 WHERE id_postulacion_hogar_temporal = ?";

        Connection conn = null;
        PreparedStatement pstmtSolicitud = null;
        PreparedStatement pstmtSelectPostulacion = null;
        PreparedStatement pstmtUpdatePostulacion = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Paso 1: Actualizar el estado de la solicitud a "Aprobado"
            pstmtSolicitud = conn.prepareStatement(updateSolicitudSql);
            pstmtSolicitud.setInt(1, 2); // Estado "Aprobado" (ID=2)
            pstmtSolicitud.setInt(2, idSolicitud);
            pstmtSolicitud.executeUpdate();

            // Paso 2: Obtener el id_postulacion_hogar_temporal de la solicitud aceptada
            pstmtSelectPostulacion = conn.prepareStatement(selectPostulacionSql);
            pstmtSelectPostulacion.setInt(1, idSolicitud);
            rs = pstmtSelectPostulacion.executeQuery();

            if (rs.next()) {
                int idPostulacion = rs.getInt("id_postulacion_hogar_temporal");

                // Paso 3: Establecer la cantidad de rechazos consecutivos en 0
                pstmtUpdatePostulacion = conn.prepareStatement(updatePostulacionSql);
                pstmtUpdatePostulacion.setInt(1, idPostulacion);
                pstmtUpdatePostulacion.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir cambios en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtSolicitud != null) pstmtSolicitud.close();
                if (pstmtSelectPostulacion != null) pstmtSelectPostulacion.close();
                if (pstmtUpdatePostulacion != null) pstmtUpdatePostulacion.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void rechazarSolicitud(int idSolicitud) {
        String updateSolicitudSql = "UPDATE SolicitudHogarTemporal SET id_estado = ? WHERE id_solicitud_hogar_temporal = ?";
        String selectPostulacionSql = "SELECT id_postulacion_hogar_temporal FROM SolicitudHogarTemporal WHERE id_solicitud_hogar_temporal = ?";
        String updatePostulacionSql = "UPDATE PostulacionHogarTemporal SET cantidad_rechazos_consecutivos = cantidad_rechazos_consecutivos + 1 WHERE id_postulacion_hogar_temporal = ?";

        Connection conn = null;
        PreparedStatement pstmtSolicitud = null;
        PreparedStatement pstmtSelectPostulacion = null;
        PreparedStatement pstmtUpdatePostulacion = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // Paso 1: Actualizar el estado de la solicitud a "Rechazado"
            pstmtSolicitud = conn.prepareStatement(updateSolicitudSql);
            pstmtSolicitud.setInt(1, 3); // Estado "Rechazado"
            pstmtSolicitud.setInt(2, idSolicitud);
            pstmtSolicitud.executeUpdate();

            // Paso 2: Obtener el id_postulacion_hogar_temporal de la solicitud rechazada
            pstmtSelectPostulacion = conn.prepareStatement(selectPostulacionSql);
            pstmtSelectPostulacion.setInt(1, idSolicitud);
            rs = pstmtSelectPostulacion.executeQuery();

            if (rs.next()) {
                int idPostulacion = rs.getInt("id_postulacion_hogar_temporal");

                // Paso 3: Incrementar el contador de rechazos consecutivos en la postulación
                pstmtUpdatePostulacion = conn.prepareStatement(updatePostulacionSql);
                pstmtUpdatePostulacion.setInt(1, idPostulacion);
                pstmtUpdatePostulacion.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir cambios en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtSolicitud != null) pstmtSolicitud.close();
                if (pstmtSelectPostulacion != null) pstmtSelectPostulacion.close();
                if (pstmtUpdatePostulacion != null) pstmtUpdatePostulacion.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public ArrayList<byte[]> obtenerFotosPorPostulacion(int idPostulacion) {
        ArrayList<byte[]> fotos = new ArrayList<>();
        String sql = "SELECT foto_lugar_temporal FROM FotoPostulacionHogarTemporal WHERE id_postulacion_hogar_temporal = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPostulacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fotos.add(rs.getBytes("foto_lugar_temporal"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fotos;
    }

    public PostulacionHogarTemporal obtenerPostulacionPorId(int idPostulacion) {
        PostulacionHogarTemporal postulacion = null;
        String sql = "SELECT * FROM PostulacionHogarTemporal WHERE id_postulacion_hogar_temporal = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPostulacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    postulacion = new PostulacionHogarTemporal();
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
                    postulacion.setFecha_inicio_temporal(rs.getDate("fecha_inicio_temporal").toLocalDate());
                    postulacion.setFecha_fin_temporal(rs.getDate("fecha_fin_temporal").toLocalDate());
                    postulacion.setUsuario_final(obtenerUsuarioPorId(rs.getInt("id_usuario_final")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return postulacion;
    }

    public void registrarSolicitud(SolicitudHogarTemporal solicitud) {
        String sql = "INSERT INTO SolicitudHogarTemporal (foto_mascota, nombre_foto_mascota, nombre_mascota, descripcion_mascota, "
                + "fecha_inicio, fecha_fin, fecha_hora_registro, id_postulacion_hogar_temporal, id_usuario_albergue, id_estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBytes(1, solicitud.getFotoMascota());
            pstmt.setString(2, solicitud.getNombreFotoMascota());
            pstmt.setString(3, solicitud.getNombreMascota());
            pstmt.setString(4, solicitud.getDescripcionMascota());
            pstmt.setDate(5, new java.sql.Date(solicitud.getFechaInicio().getTime()));
            pstmt.setDate(6, new java.sql.Date(solicitud.getFechaFin().getTime()));
            pstmt.setInt(7, solicitud.getPostulacionHogarTemporal().getId_postulacion_hogar_temporal());
            pstmt.setInt(8, solicitud.getUsuarioAlbergue().getId_usuario());
            pstmt.setInt(9, solicitud.getEstado().getId_estado());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Estado obtenerEstadoPorNombre(String nombreEstado) {
        String sql = "SELECT * FROM Estado WHERE nombre_estado = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreEstado);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Estado estado = new Estado();
                    estado.setId_estado(rs.getInt("id_estado"));
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                    return estado;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ArrayList<SolicitudHogarTemporal> obtenerSolicitudesRealizadas(int idUsuario) {
        ArrayList<SolicitudHogarTemporal> solicitudes = new ArrayList<>();
        String sql = """
        SELECT s.*, u.id_usuario, u.nombre_albergue, e.id_estado, e.nombre_estado
        FROM SolicitudHogarTemporal s
        LEFT JOIN Usuario u ON s.id_usuario_albergue = u.id_usuario
        LEFT JOIN Estado e ON s.id_estado = e.id_estado
        WHERE u.id_usuario = ?
        ORDER BY s.fecha_hora_registro DESC
    """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SolicitudHogarTemporal solicitud = new SolicitudHogarTemporal();
                solicitud.setIdSolicitudHogarTemporal(rs.getInt("id_solicitud_hogar_temporal"));
                solicitud.setNombreMascota(rs.getString("nombre_mascota"));
                solicitud.setDescripcionMascota(rs.getString("descripcion_mascota"));
                solicitud.setFechaInicio(rs.getDate("fecha_inicio"));
                solicitud.setFechaFin(rs.getDate("fecha_fin"));
                solicitud.setFotoMascota(rs.getBytes("foto_mascota"));
                solicitud.setNombreFotoMascota(rs.getString("nombre_foto_mascota"));
                solicitud.setPostulacionHogarTemporal(obtenerPostulacionPorId(rs.getInt("id_postulacion_hogar_temporal")));
                // Asociar usuario albergue
                Usuario usuarioAlbergue = new Usuario();
                usuarioAlbergue.setId_usuario(rs.getInt("id_usuario"));
                usuarioAlbergue.setNombre_albergue(rs.getString("nombre_albergue"));
                solicitud.setUsuarioAlbergue(usuarioAlbergue);

                // Asociar estado
                Estado estado = new Estado();
                estado.setId_estado(rs.getInt("id_estado"));
                estado.setNombre_estado(rs.getString("nombre_estado"));
                solicitud.setEstado(estado);

                solicitudes.add(solicitud);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return solicitudes;
    }
}
