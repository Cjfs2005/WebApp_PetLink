package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GestionCoordinadorDao extends DaoBase {
    public Usuario obtenerDatosCoordinador(int idCoordinador) {
        Usuario coordinador = null;
        String sql = "SELECT * FROM Usuario u JOIN Zona z on u.id_zona = z.id_zona WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCoordinador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Se ha leido correctamente la data del usuario");
                coordinador = new Usuario();
                coordinador.setId_usuario(rs.getInt("id_usuario"));
                coordinador.setNombres_coordinador(rs.getString("nombres_coordinador"));
                coordinador.setApellidos_coordinador(rs.getString("apellidos_coordinador"));
                coordinador.setDni(rs.getString("dni"));
                coordinador.setNumero_yape_plin(rs.getString("numero_yape_plin"));
                Zona zona = new Zona();
                zona.setId_zona(rs.getInt("id_zona"));
                zona.setNombre_zona(rs.getString("nombre_zona"));
                coordinador.setZona(zona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coordinador;
    }

    public ArrayList<PublicacionEventoBenefico> obtenerEventosPendientesPorZona(int idZona){
        ArrayList<PublicacionEventoBenefico> listaEventos = new ArrayList<>();

        String sql = "SELECT * FROM PublicacionEventoBenefico p JOIN Usuario u ON p.id_usuario_albergue = u.id_usuario " +
                "JOIN Zona z ON u.id_zona = z.id_zona JOIN LugarEvento l ON p.id_lugar_evento = l.id_lugar_evento " +
                "JOIN Distrito d ON l.id_distrito = d.id_distrito WHERE z.id_zona = ? AND id_estado = 1 " +
                "ORDER BY p.fecha_hora_inicio_evento ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idZona);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                PublicacionEventoBenefico evento = new PublicacionEventoBenefico();
                evento.setIdPublicacionEventoBenefico(rs.getInt("id_publicacion_evento_benefico"));
                evento.setNombreEvento(rs.getString("nombre_evento"));
                evento.setDescripcionEvento(rs.getString("descripcion_evento"));
                evento.setFechaHoraInicioEvento(rs.getDate("fecha_hora_inicio_evento"));


                LugarEvento lugar = new LugarEvento();
                lugar.setDireccion_lugar_evento(rs.getString("direccion_lugar_evento"));
                lugar.setNombre_lugar_evento(rs.getString("nombre_lugar_evento"));

                Distrito distrito = new Distrito();
                distrito.setId_distrito(rs.getInt("id_distrito"));
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                lugar.setDistrito(distrito);
                evento.setLugarEvento(lugar);

                Usuario albergue = new Usuario();
                albergue.setId_usuario(rs.getInt("id_usuario"));
                albergue.setNombre_albergue(rs.getString("nombre_albergue"));
                albergue.setNombres_encargado(rs.getString("nombres_encargado"));
                albergue.setApellidos_encargado(rs.getString("apellidos_encargado"));
                albergue.setCorreo_electronico(rs.getString("correo_electronico"));
                evento.setUsuarioAlbergue(albergue);

                listaEventos.add(evento);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEventos;
    }

    public PublicacionEventoBenefico obtenerEventoPendientePorId(int idEvento){
        PublicacionEventoBenefico evento = null;
        String sql = "SELECT p.id_publicacion_evento_benefico, p.nombre_evento, p.fecha_hora_registro, " +
                "p.foto, p.nombre_foto, p.razon_evento, p.descripcion_evento, p.artistas_provedores_invitados, " +
                "p.fecha_hora_inicio_evento, p.fecha_hora_fin_evento, p.es_evento_activo, " +
                "p.aforo_evento, p.entrada_evento, u.nombre_albergue, u.nombres_encargado, u.apellidos_encargado, u.correo_electronico, l.*, d.nombre_distrito, e.nombre_estado " +
                "FROM PublicacionEventoBenefico p " +
                "JOIN Usuario u ON p.id_usuario_albergue = u.id_usuario " +
                "JOIN LugarEvento l ON p.id_lugar_evento = l.id_lugar_evento " +
                "JOIN Distrito d ON l.id_distrito = d.id_distrito " +
                "JOIN Estado e ON p.id_estado = e.id_estado " +
                "WHERE p.id_publicacion_evento_benefico = ? AND p.id_estado = 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEvento);

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
                    usuario.setNombres_encargado(rs.getString("nombres_encargado"));
                    usuario.setApellidos_encargado(rs.getString("apellidos_encargado"));
                    usuario.setCorreo_electronico(rs.getString("correo_electronico"));

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

    public void aceptarEvento(int idEvento) {
        String sql = "UPDATE PublicacionEventoBenefico SET id_estado = 2 where id_publicacion_evento_benefico = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ps.executeUpdate();
            System.out.println("Se ha aceptado satisfactoriamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rechazarEvento(int idEvento) {
        String sql = "UPDATE PublicacionEventoBenefico SET id_estado = 3 where id_publicacion_evento_benefico = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            ps.executeUpdate();
            System.out.println("Se ha rechazado satisfactoriamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
