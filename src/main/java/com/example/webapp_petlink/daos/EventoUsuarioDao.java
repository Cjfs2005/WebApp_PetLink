package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventoUsuarioDao extends DaoBase{

    //PARA USUARIO FINAL

    //Método para obtener el nombre del usuario
    public Usuario obtenerDatoUsuario(int idUsuarioFinal) {
        Usuario usuario = null;
        String sql = "SELECT u.id_usuario, u.nombres_usuario_final, u.apellidos_usuario_final, u.foto_perfil FROM Usuario u  WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuarioFinal);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Se ha leido correctamente la data del usuario");
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                byte[] fotoUsuario = rs.getBytes("foto_perfil");
                if (fotoUsuario != null) {
                    usuario.setFoto_perfil(fotoUsuario);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public ArrayList<PublicacionEventoBenefico> listarEventos() {

        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();

        String sql = "SELECT PublicacionEventoBenefico.*, "
                + "LugarEvento.direccion_lugar_evento, LugarEvento.aforo_maximo, "
                + "Usuario.nombre_albergue, "
                + "Estado.nombre_estado, "
                + "(SELECT COUNT(*) FROM InscripcionEventoBenefico "
                + " WHERE InscripcionEventoBenefico.id_evento_benefico = PublicacionEventoBenefico.id_publicacion_evento_benefico) AS cantidad_asistentes "
                + "FROM PublicacionEventoBenefico "
                + "JOIN LugarEvento ON PublicacionEventoBenefico.id_lugar_evento = LugarEvento.id_lugar_evento "
                + "JOIN Usuario ON PublicacionEventoBenefico.id_usuario_albergue = Usuario.id_usuario "
                + "JOIN Estado ON PublicacionEventoBenefico.id_estado = Estado.id_estado "
                + "WHERE Estado.id_estado = 2 "
                + "AND PublicacionEventoBenefico.fecha_hora_fin_evento >= CURDATE() "
                + "ORDER BY PublicacionEventoBenefico.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();

                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setAforoEvento(rs.getInt("aforo_evento"));
                evento.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro"));
                evento.setNombreFoto(rs.getString("nombre_foto"));
                evento.setCantAsistentes(rs.getInt("cantidad_asistentes"));
                evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                byte[] fotoEvento = rs.getBytes("foto");
                if (fotoEvento != null) {
                    evento.setFoto(fotoEvento);
                }

                listaEventos.add(evento);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEventos;
    }

    public ArrayList<PublicacionEventoBenefico> listarEventosInscritos(int idUsuario) {
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();

        String sql = "SELECT p.*, " +
                "(SELECT COUNT(*) FROM InscripcionEventoBenefico i WHERE i.id_evento_benefico = p.id_publicacion_evento_benefico) AS cantidad_asistentes " +
                "FROM PublicacionEventoBenefico p " +
                "JOIN InscripcionEventoBenefico i ON p.id_publicacion_evento_benefico = i.id_evento_benefico " +
                "WHERE i.id_usuario_final = ? " +
                "ORDER BY p.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();
                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setAforoEvento(rs.getInt("aforo_evento"));
                evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                evento.setNombreFoto(rs.getString("nombre_foto"));
                evento.setCantAsistentes(rs.getInt("cantidad_asistentes"));
                evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                byte[] fotoEvento = rs.getBytes("foto");
                if (fotoEvento != null) {
                    evento.setFoto(fotoEvento);
                }
                listaEventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEventos;
    }
    // Mëtodo para buscar por id y mostrar todos los detalles
    public PublicacionEventoBenefico buscarPorId(int idPublicacion){
        PublicacionEventoBenefico evento = null;
        String sql = "SELECT p.id_publicacion_evento_benefico, p.nombre_evento, p.fecha_hora_registro, " +
                "p.foto, p.nombre_foto, p.razon_evento, p.descripcion_evento, p.artistas_provedores_invitados, " +
                "p.fecha_hora_inicio_evento, p.fecha_hora_fin_evento, p.es_evento_activo, " +
                "p.aforo_evento, p.entrada_evento, u.nombre_albergue, l.*, d.nombre_distrito, e.nombre_estado, " +
                "(SELECT COUNT(*) FROM InscripcionEventoBenefico i " +
                "WHERE i.id_evento_benefico = p.id_publicacion_evento_benefico) AS cantidad_asistentes " +
                "FROM PublicacionEventoBenefico p " +
                "JOIN Usuario u ON p.id_usuario_albergue = u.id_usuario " +
                "JOIN LugarEvento l ON p.id_lugar_evento = l.id_lugar_evento " +
                "JOIN Distrito d ON l.id_distrito = d.id_distrito " +
                "JOIN Estado e ON p.id_estado = e.id_estado " +
                "WHERE p.id_publicacion_evento_benefico = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPublicacion);

            try(ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    evento = new PublicacionEventoBenefico();
                    evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                    evento.setNombreEvento(rs.getString("nombre_evento"));
                    evento.setNombreFoto(rs.getString("nombre_foto"));
                    byte[] fotoEvento = rs.getBytes("foto");
                    if (fotoEvento != null) {
                        evento.setFoto(fotoEvento);
                    }
                    evento.setRazonEvento(rs.getString("razon_evento"));
                    evento.setDescripcionEvento(rs.getString("descripcion_evento"));
                    evento.setArtistasProvedoresInvitados(rs.getString("artistas_provedores_invitados"));
                    evento.setAforoEvento(rs.getInt("aforo_evento"));
                    evento.setEntradaEvento(rs.getString("entrada_evento"));
                    evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                    evento.setFechaHoraFinEvento(rs.getTimestamp("fecha_hora_fin_evento"));
                    evento.setCantAsistentes(rs.getInt("cantidad_asistentes"));

                    LugarEvento lugar = new LugarEvento();
                    lugar.setDireccion_lugar_evento(rs.getString("direccion_lugar_evento"));
                    lugar.setNombre_lugar_evento(rs.getString("nombre_lugar_evento"));
                    System.out.println(lugar.getDireccion_lugar_evento());
                    System.out.println(lugar.getNombre_lugar_evento());

                    lugar.setAforo_maximo(rs.getInt("aforo_maximo"));
                    System.out.println(lugar.getAforo_maximo());

                    Distrito distrito = new Distrito();
                    distrito.setNombre_distrito(rs.getString("nombre_distrito"));

                    lugar.setDistrito(distrito);
                    evento.setLugarEvento(lugar);

                    Usuario usuario = new Usuario();
                    usuario.setNombre_albergue(rs.getString("nombre_albergue"));

                    Estado estado = new Estado();
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                    evento.setEstado(estado);

                    evento.setUsuarioAlbergue(usuario);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return evento;
    }

    public boolean verSiEsActivo(int idEvento) {
        String sql = "SELECT COUNT(*) AS total " +
                "FROM PublicacionEventoBenefico " +
                "WHERE id_publicacion_evento_benefico = ? AND es_evento_activo = 1 AND id_estado = 2";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Configura el parámetro del ID del evento
            ps.setInt(1, idEvento);

            // Ejecuta la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si hay al menos una coincidencia, retorna true
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no cumple las condiciones o si ocurre un error
    }


    // Para buscar los eventos que coincidan en el buscador
    public ArrayList<PublicacionEventoBenefico> buscarEventoNombre(String nombre){
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();
        String sql = "SELECT p.*, " +
                "COUNT(i.id_evento_benefico) AS cantidad_asistentes " +
                "FROM PublicacionEventoBenefico p " +
                "JOIN Estado e ON p.id_estado = e.id_estado " +
                "LEFT JOIN InscripcionEventoBenefico i ON i.id_evento_benefico = p.id_publicacion_evento_benefico " +
                "WHERE e.id_estado = 2 " +
                "AND p.fecha_hora_fin_evento >= CURDATE() " +
                "AND p.nombre_evento LIKE ? " +
                "GROUP BY p.id_publicacion_evento_benefico " +
                "ORDER BY p.fecha_hora_inicio_evento ASC";


        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    PublicacionEventoBenefico evento = new PublicacionEventoBenefico();
                    evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                    evento.setNombreEvento(rs.getString("nombre_evento"));
                    evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                    evento.setNombreFoto(rs.getString("nombre_foto"));
                    evento.setCantAsistentes(rs.getInt("cantidad_asistentes"));
                    evento.setAforoEvento(rs.getInt("aforo_evento"));
                    evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                    byte[] fotoEvento = rs.getBytes("foto");
                    if (fotoEvento != null) {
                        evento.setFoto(fotoEvento);
                    }
                    listaEventos.add(evento);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return listaEventos;
    }

    public boolean inscribibirseEvento (int idUsuario, int idEvento) {
        String sql = "INSERT INTO InscripcionEventoBenefico (id_usuario_final, id_evento_benefico, fecha_hora_registro) VALUES (?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Configura los valores del usuario y del evento
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEvento);

            // Ejecuta la inserción
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean desinscribirUsuarioDeEvento(int usuarioId, int eventoId) {
        try (Connection conn = getConnection()) {
            String query = "DELETE FROM InscripcionEventoBenefico WHERE id_usuario_final = ? AND id_evento_benefico = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, eventoId);
                int filasAfectadas = stmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificarInscripcion(int idEvento, int idUsuario) {
        String sql = "SELECT COUNT(*) AS total FROM InscripcionEventoBenefico " +
                "WHERE id_evento_benefico = ? AND id_usuario_final = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEvento);
            pstmt.setInt(2, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verificarTraslape(int idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        String query = """
            SELECT EXISTS (
                SELECT 1
                FROM InscripcionEventoBenefico ie
                INNER JOIN PublicacionEventoBenefico pe ON ie.id_evento_benefico = pe.id_publicacion_evento_benefico
                WHERE ie.id_usuario_final = ?
                AND pe.es_evento_activo = 1
                AND (
                    (? BETWEEN pe.fecha_hora_inicio_evento AND pe.fecha_hora_fin_evento) OR
                    (? BETWEEN pe.fecha_hora_inicio_evento AND pe.fecha_hora_fin_evento) OR
                    (pe.fecha_hora_inicio_evento BETWEEN ? AND ?) OR
                    (pe.fecha_hora_fin_evento BETWEEN ? AND ?)
                )
            )
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idUsuario);
            statement.setTimestamp(2, Timestamp.valueOf(fechaInicio));
            statement.setTimestamp(3, Timestamp.valueOf(fechaFin));
            statement.setTimestamp(4, Timestamp.valueOf(fechaInicio));
            statement.setTimestamp(5, Timestamp.valueOf(fechaFin));
            statement.setTimestamp(6, Timestamp.valueOf(fechaInicio));
            statement.setTimestamp(7, Timestamp.valueOf(fechaFin));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1); // Retorna true si hay traslape
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Si ocurre un error o no hay traslape
    }

    // PARA ALBERGUE

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

    public ArrayList<PublicacionEventoBenefico> listarEventosAlbergue(int idUsuarioAlbergue){

        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();


        String sql = "SELECT * FROM PublicacionEventoBenefico WHERE id_usuario_albergue = ? "
                + "AND PublicacionEventoBenefico.fecha_hora_fin_evento >= CURDATE() AND id_estado = 2 "
                + "ORDER BY PublicacionEventoBenefico.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idUsuarioAlbergue);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();

                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                evento.setNombreFoto(rs.getString("nombre_foto"));
                evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                byte[] fotoEvento = rs.getBytes("foto");
                if (fotoEvento != null) {
                    evento.setFoto(fotoEvento);
                }
                listaEventos.add(evento);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listaEventos;
    }

    public ArrayList<PublicacionEventoBenefico> listarEventosRealizados(int idUsuarioAlbergue){
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();


        String sql = "SELECT * FROM PublicacionEventoBenefico WHERE id_usuario_albergue = ? "
                + "AND PublicacionEventoBenefico.fecha_hora_fin_evento < CURDATE() AND id_estado = 2 "
                + "ORDER BY PublicacionEventoBenefico.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idUsuarioAlbergue);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();
                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                evento.setNombreFoto(rs.getString("nombre_foto"));
                evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                byte[] fotoEvento = rs.getBytes("foto");
                if (fotoEvento != null) {
                    evento.setFoto(fotoEvento);
                }
                listaEventos.add(evento);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listaEventos;
    }

    public ArrayList<PublicacionEventoBenefico> listarEventosPendientes(int idUsuarioAlbergue){
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();


        String sql = "SELECT * FROM PublicacionEventoBenefico WHERE id_usuario_albergue = ? "
                + "AND PublicacionEventoBenefico.fecha_hora_fin_evento >= CURDATE() AND id_estado = 1 "
                + "ORDER BY PublicacionEventoBenefico.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idUsuarioAlbergue);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();

                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                evento.setNombreFoto(rs.getString("nombre_foto"));
                evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                byte[] fotoEvento = rs.getBytes("foto");
                if (fotoEvento != null) {
                    evento.setFoto(fotoEvento);
                }
                listaEventos.add(evento);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listaEventos;
    }

    public ArrayList<PublicacionEventoBenefico> listarEventosRechazados(int idUsuarioAlbergue){
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();


        String sql = "SELECT * FROM PublicacionEventoBenefico WHERE id_usuario_albergue = ? "
                + "AND PublicacionEventoBenefico.fecha_hora_fin_evento >= CURDATE() AND id_estado = 3 "
                + "ORDER BY PublicacionEventoBenefico.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idUsuarioAlbergue);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();

                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                evento.setNombreFoto(rs.getString("nombre_foto"));
                evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                byte[] fotoEvento = rs.getBytes("foto");
                if (fotoEvento != null) {
                    evento.setFoto(fotoEvento);
                }
                listaEventos.add(evento);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listaEventos;
    }

    public PublicacionEventoBenefico buscarPorIdDeAlbergue(int idPublicacion, int idAlbergue){
        PublicacionEventoBenefico evento = null;
        String sql = "SELECT p.id_publicacion_evento_benefico, p.nombre_evento, p.fecha_hora_registro, " +
                "p.foto, p.nombre_foto, p.razon_evento, p.descripcion_evento, p.artistas_provedores_invitados, " +
                "p.fecha_hora_inicio_evento, p.fecha_hora_fin_evento, p.es_evento_activo, " +
                "p.aforo_evento, p.entrada_evento, u.nombre_albergue, l.*, d.nombre_distrito, e.nombre_estado, " +
                "(SELECT COUNT(*) FROM InscripcionEventoBenefico i " +
                "WHERE i.id_evento_benefico = p.id_publicacion_evento_benefico) AS cantidad_asistentes " +
                "FROM PublicacionEventoBenefico p " +
                "JOIN Usuario u ON p.id_usuario_albergue = u.id_usuario " +
                "JOIN LugarEvento l ON p.id_lugar_evento = l.id_lugar_evento " +
                "JOIN Distrito d ON l.id_distrito = d.id_distrito " +
                "JOIN Estado e ON p.id_estado = e.id_estado " +
                "WHERE p.id_publicacion_evento_benefico = ? AND p.id_usuario_albergue = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPublicacion);
            pstmt.setInt(2, idAlbergue);

            try(ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    evento = new PublicacionEventoBenefico();
                    evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                    evento.setNombreEvento(rs.getString("nombre_evento"));
                    evento.setNombreFoto(rs.getString("nombre_foto"));
                    byte[] fotoEvento = rs.getBytes("foto");
                    if (fotoEvento != null) {
                        evento.setFoto(fotoEvento);
                    }
                    evento.setRazonEvento(rs.getString("razon_evento"));
                    evento.setDescripcionEvento(rs.getString("descripcion_evento"));
                    evento.setArtistasProvedoresInvitados(rs.getString("artistas_provedores_invitados"));
                    evento.setAforoEvento(rs.getInt("aforo_evento"));
                    evento.setEntradaEvento(rs.getString("entrada_evento"));
                    evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                    evento.setFechaHoraFinEvento(rs.getTimestamp("fecha_hora_fin_evento"));
                    evento.setCantAsistentes(rs.getInt("cantidad_asistentes"));

                    LugarEvento lugar = new LugarEvento();
                    lugar.setDireccion_lugar_evento(rs.getString("direccion_lugar_evento"));
                    lugar.setNombre_lugar_evento(rs.getString("nombre_lugar_evento"));
                    System.out.println(lugar.getDireccion_lugar_evento());
                    System.out.println(lugar.getNombre_lugar_evento());

                    lugar.setAforo_maximo(rs.getInt("aforo_maximo"));
                    System.out.println(lugar.getAforo_maximo());


                    Distrito distrito = new Distrito();
                    distrito.setNombre_distrito(rs.getString("nombre_distrito"));

                    lugar.setDistrito(distrito);
                    evento.setLugarEvento(lugar);

                    Usuario usuario = new Usuario();
                    usuario.setNombre_albergue(rs.getString("nombre_albergue"));

                    Estado estado = new Estado();
                    estado.setNombre_estado(rs.getString("nombre_estado"));
                    evento.setEstado(estado);

                    evento.setUsuarioAlbergue(usuario);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return evento;
    }

    // Para la búsqueda en el search de la página EventoBenefico.jsp
    public ArrayList<PublicacionEventoBenefico> buscarEventoNombre(String nombre, int idUsuarioAlbergue){
       System.out.println(nombre);
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();

        String sql = "SELECT p.* " +
                "FROM PublicacionEventoBenefico p WHERE p.nombre_evento LIKE ? AND p.id_usuario_albergue=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            pstmt.setInt(2, idUsuarioAlbergue);

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    PublicacionEventoBenefico evento = new PublicacionEventoBenefico();
                    evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                    evento.setNombreEvento(rs.getString("nombre_evento"));
                    evento.setFechaHoraRegistro(rs.getDate("fecha_hora_registro"));
                    evento.setNombreFoto(rs.getString("nombre_foto"));
                    evento.setFechaHoraInicioEvento(rs.getTimestamp("fecha_hora_inicio_evento"));
                    byte[] fotoEvento = rs.getBytes("foto");
                    if (fotoEvento != null) {
                        evento.setFoto(fotoEvento);
                    }
                    listaEventos.add(evento);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Eventos encontrados: " + listaEventos.size());
        return listaEventos;
    }

    // Para obtener a todos los usuarios que se han inscrito
    public ArrayList<InscripcionEventoBenefico> listarInscritos(int id) {
        ArrayList<InscripcionEventoBenefico> inscritos = new ArrayList<>();
        String sql = "SELECT i.id_inscripcion_evento_benefico, i.fecha_hora_registro, " +
                "u.id_usuario, u.nombres_usuario_final, u.apellidos_usuario_final, u.correo_electronico " +
                "FROM InscripcionEventoBenefico i " +
                "JOIN Usuario u ON i.id_usuario_final = u.id_usuario " +
                "WHERE i.id_evento_benefico = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // Setear el ID del evento
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Crear una instancia de InscripcionEventoBenefico
                    InscripcionEventoBenefico inscripcion = new InscripcionEventoBenefico();

                    // Configurar los datos de la inscripción
                    inscripcion.setId_inscripcion_evento_benefico(rs.getInt("id_inscripcion_evento_benefico"));
                    inscripcion.setFecha_hora_registro(rs.getTimestamp("fecha_hora_registro"));

                    // Crear y configurar un objeto Usuario
                    Usuario usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                    usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                    usuario.setCorreo_electronico(rs.getString("correo_electronico"));

                    // Asociar el usuario a la inscripción
                    inscripcion.setUsuario_final(usuario);

                    // Agregar la inscripción a la lista
                    inscritos.add(inscripcion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscritos;
    }

    public void eliminarEvento(int id){
        String sql = "DELETE FROM PublicacionEventoBenefico WHERE id_publicacion_evento_benefico = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LugarEvento> listarLugaresEventos () {
        ArrayList<LugarEvento> lugares = new ArrayList<>();
        String sql = "SELECT * FROM LugarEvento l " +
                "JOIN Distrito d ON l.id_distrito = d.id_distrito";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    LugarEvento lugar = new LugarEvento();
                    lugar.setId_lugar_evento(rs.getInt("id_lugar_evento"));
                    lugar.setNombre_lugar_evento(rs.getString("nombre_lugar_evento"));
                    lugar.setDireccion_lugar_evento(rs.getString("direccion_lugar_evento"));
                    lugar.setAforo_maximo(rs.getInt("aforo_maximo"));

                    Distrito distrito = new Distrito();
                    distrito.setId_distrito(rs.getInt("id_distrito"));
                    distrito.setNombre_distrito(rs.getString("nombre_distrito"));

                    lugar.setDistrito(distrito);
                    lugares.add(lugar);
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return lugares;
    }

    public void eliminarInscripcionEventoAlbergue(int id) {
        String sql = "DELETE FROM InscripcionEventoBenefico WHERE id_evento_benefico = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            System.out.println(filasAfectadas + " inscripciones eliminadas para el evento ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean guardarEvento(PublicacionEventoBenefico evento) {
        String sql = "INSERT INTO PublicacionEventoBenefico (nombre_evento, fecha_hora_inicio_evento, fecha_hora_fin_evento, entrada_evento, razon_evento, descripcion_evento, artistas_provedores_invitados, id_lugar_evento, id_usuario_albergue, es_evento_activo, id_estado, fecha_hora_registro, aforo_evento, foto, nombre_foto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evento.getNombreEvento());
            stmt.setTimestamp(2, new Timestamp(evento.getFechaHoraInicioEvento().getTime()));
            stmt.setTimestamp(3, new Timestamp(evento.getFechaHoraFinEvento().getTime()));

            stmt.setString(4, evento.getEntradaEvento());
            stmt.setString(5, evento.getRazonEvento());
            stmt.setString(6, evento.getDescripcionEvento());
            stmt.setString(7, evento.getArtistasProvedoresInvitados());
            stmt.setInt(8, evento.getLugarEvento().getId_lugar_evento());
            stmt.setInt(9, evento.getUsuarioAlbergue().getId_usuario());
            stmt.setBoolean(10, evento.isEsEventoActivo());
            stmt.setInt(11, evento.getEstado().getId_estado());
            stmt.setTimestamp(12, new Timestamp(evento.getFechaHoraRegistro().getTime())); // Agregar fecha de registro
            stmt.setInt(13, evento.getAforoEvento());
            stmt.setBytes(14, evento.getFoto());
            stmt.setString(15, evento.getNombreFoto());

            return stmt.executeUpdate() > 0; // Retorna true si se guardó correctamente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarEvento(PublicacionEventoBenefico evento) {
        String sql = "UPDATE PublicacionEventoBenefico SET nombre_evento = ?, fecha_hora_inicio_evento = ?, fecha_hora_fin_evento = ?, "
                + "entrada_evento = ?, razon_evento = ?, descripcion_evento = ?, artistas_provedores_invitados = ?, "
                + "id_lugar_evento = ?, aforo_evento = ?, foto = ?, nombre_foto = ? WHERE id_publicacion_evento_benefico = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar valores a los parámetros
            stmt.setString(1, evento.getNombreEvento());
            stmt.setTimestamp(2, new Timestamp(evento.getFechaHoraInicioEvento().getTime()));
            stmt.setTimestamp(3, new Timestamp(evento.getFechaHoraFinEvento().getTime()));
            stmt.setString(4, evento.getEntradaEvento());
            stmt.setString(5, evento.getRazonEvento());
            stmt.setString(6, evento.getDescripcionEvento());
            stmt.setString(7, evento.getArtistasProvedoresInvitados());
            stmt.setInt(8, evento.getLugarEvento().getId_lugar_evento());
            stmt.setInt(9, evento.getAforoEvento());

            // Manejar la imagen: si es nula, asignar NULL en la base de datos
            if (evento.getFoto() != null) {
                stmt.setBytes(10, evento.getFoto());
                stmt.setString(11, evento.getNombreFoto());
            } else {
                stmt.setNull(10, java.sql.Types.BLOB);
                stmt.setNull(11, java.sql.Types.VARCHAR);
            }

            // ID del evento
            stmt.setInt(12, evento.getIdPublicacionEventoBenefico());

            // Ejecutar la actualización
            return stmt.executeUpdate() > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
