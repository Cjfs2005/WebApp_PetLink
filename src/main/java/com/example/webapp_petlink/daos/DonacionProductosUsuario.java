    package com.example.webapp_petlink.daos;

    import com.example.webapp_petlink.beans.*;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.util.ArrayList;
    import java.util.Base64;
    import java.util.List;

    public class DonacionProductosUsuario extends DaoBase {

        // Método para obtener todas las solicitudes activas con imagen y detalles del albergue
        public List<SolicitudDonacionProductos> obtenerTodasSolicitudesActivas() {
            List<SolicitudDonacionProductos> solicitudes = new ArrayList<>();
            String sql = "SELECT s.id_solicitud_donacion_productos, s.descripcion_donaciones, " +
                    "u.nombre_albergue, u.foto_de_portada_albergue " +
                    "FROM SolicitudDonacionProductos s " +
                    "INNER JOIN Usuario u ON s.id_usuario_albergue = u.id_usuario " +
                    "WHERE s.es_solicitud_activa = true AND s.id_estado = 2 ";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    SolicitudDonacionProductos solicitud = new SolicitudDonacionProductos();
                    solicitud.setIdSolicitudDonacionProductos(rs.getInt("id_solicitud_donacion_productos"));
                    solicitud.setDescripcionDonaciones(rs.getString("descripcion_donaciones"));

                    // Configurar los datos del usuario/albergue asociado
                    Usuario albergue = new Usuario();
                    albergue.setNombre_albergue(rs.getString("nombre_albergue"));

                    // Convertir la imagen de portada a Base64 si está disponible
                    byte[] fotoBytes = rs.getBytes("foto_de_portada_albergue");
                    if (fotoBytes != null) {
                        System.out.println("[DEBUG DAO] Imagen asignada con tamaño: " + fotoBytes.length + " bytes");
                    } else {
                        System.out.println("[DEBUG DAO] Imagen es NULL para id_usuario: " + rs.getInt("id_usuario"));
                    }
                    albergue.setFoto_de_portada_albergue(fotoBytes);



                    solicitud.setUsuarioAlbergue(albergue);
                    solicitudes.add(solicitud);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return solicitudes;
        }


        public SolicitudDonacionProductos obtenerDetallesParaFormulario(int idSolicitud) {
            SolicitudDonacionProductos solicitud = null;
            String sql = "SELECT " +
                    "s.id_solicitud_donacion_productos, " +
                    "s.descripcion_donaciones, " +
                    "u.id_usuario, u.nombre_albergue, " +
                    "p.id_punto_acopio, p.direccion_punto_acopio, " +
                    "h.id_horario_recepcion_donacion, h.fecha_hora_inicio, h.fecha_hora_fin " +
                    "FROM SolicitudDonacionProductos s " +
                    "INNER JOIN Usuario u ON s.id_usuario_albergue = u.id_usuario " +
                    "LEFT JOIN PuntoAcopioDonacion pd ON s.id_solicitud_donacion_productos = pd.id_solicitud_donacion_productos " +
                    "LEFT JOIN PuntoAcopio p ON pd.id_punto_acopio = p.id_punto_acopio " +
                    "LEFT JOIN HorarioRecepcionDonacion h ON pd.id_punto_acopio_donacion = h.id_punto_acopio_donacion " +
                    "WHERE s.id_solicitud_donacion_productos = ?";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idSolicitud);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (solicitud == null) {
                            // Crear la solicitud solo una vez
                            solicitud = new SolicitudDonacionProductos();
                            solicitud.setIdSolicitudDonacionProductos(rs.getInt("id_solicitud_donacion_productos"));
                            solicitud.setDescripcionDonaciones(rs.getString("descripcion_donaciones"));

                            Usuario usuario = new Usuario();
                            usuario.setId_usuario(rs.getInt("id_usuario"));
                            usuario.setNombre_albergue(rs.getString("nombre_albergue"));
                            solicitud.setUsuarioAlbergue(usuario);
                        }

                        // Crear punto de acopio y asignar su horario
                        PuntoAcopio puntoAcopio = new PuntoAcopio();
                        puntoAcopio.setId_punto_acopio(rs.getInt("id_punto_acopio"));
                        puntoAcopio.setDireccion_punto_acopio(rs.getString("direccion_punto_acopio"));

                        HorarioRecepcionDonacion horario = new HorarioRecepcionDonacion();
                        horario.setIdHorarioRecepcionDonacion(rs.getInt("id_horario_recepcion_donacion"));
                        horario.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                        horario.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());

                        PuntoAcopioDonacion puntoAcopioDonacion = new PuntoAcopioDonacion();
                        puntoAcopioDonacion.setPuntoAcopio(puntoAcopio);
                        horario.setPuntoAcopioDonacion(puntoAcopioDonacion);
                        System.out.println("id_solicitud_donacion_productos");
                        System.out.println("descripcion_donaciones");
                        System.out.println("id_usuario");
                        System.out.println("nombre_albergue");
                        System.out.println("id_punto_acopio");
                        System.out.println("direccion_punto_acopio");
                        System.out.println("id_horario_recepcion_donacion");
                        System.out.println("fecha_hora_inicio");
                        System.out.println("fecha_hora_fin");
                        // Asignar punto de acopio y horario a la solicitud
                        if (solicitud.getHorarioRecepcion() == null) {
                            solicitud.setHorarioRecepcion(horario);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return solicitud;
        }
        public boolean registrarDonacion(int idUsuarioFinal, int idHorarioRecepcionDonacion, String descripcionDonacion) {
            String sql = "INSERT INTO RegistroDonacionProductos " +
                    "(id_usuario_final, id_horario_recepcion_donacion, descripciones_donaciones, fecha_hora_registro, id_estado) " +
                    "VALUES (?, ?, ?, NOW(), 2)";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idUsuarioFinal); // ID del usuario que dona
                ps.setInt(2, idHorarioRecepcionDonacion); // ID del horario relacionado
                ps.setString(3, descripcionDonacion); // Descripción de la donación

                int filasInsertadas = ps.executeUpdate();
                return filasInsertadas > 0; // Devuelve true si se insertó correctamente

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[ERROR] No se pudo registrar la donación: " + e.getMessage());
                return false;
            }
        }


    }