package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.Distrito;
import com.example.webapp_petlink.beans.PuntoAcopio;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoPerfilAlbergue extends DaoBase {

    // Método para obtener el perfil completo del albergue por ID de usuario
    public Usuario obtenerPerfilAlbergue(int idUsuario) {
        Usuario usuario = null;
        String query = "SELECT u.id_usuario, u.nombre_albergue, u.descripcion_perfil, u.nombres_encargado, u.apellidos_encargado, " +
                "u.anio_creacion, u.cantidad_animales, u.espacio_disponible, u.url_instagram, u.direccion_donaciones, " +
                "u.nombre_contacto_donaciones, u.numero_contacto_donaciones, u.numero_yape_plin, d.nombre_distrito, " +
                "u.foto_de_portada_albergue, u.nombre_foto_de_portada, u.logo_albergue, u.nombre_logo_albergue " +
                "FROM Usuario u " +
                "JOIN Distrito d ON u.id_distrito = d.id_distrito " +
                "WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombre_albergue(rs.getString("nombre_albergue"));
                usuario.setDescripcion_perfil(rs.getString("descripcion_perfil"));
                usuario.setNombres_encargado(rs.getString("nombres_encargado"));
                usuario.setApellidos_encargado(rs.getString("apellidos_encargado"));
                usuario.setAnio_creacion(rs.getString("anio_creacion"));
                usuario.setCantidad_animales(rs.getInt("cantidad_animales"));
                usuario.setEspacio_disponible(rs.getInt("espacio_disponible"));
                usuario.setUrl_instagram(rs.getString("url_instagram"));
                usuario.setDireccion_donaciones(rs.getString("direccion_donaciones"));
                usuario.setNombre_contacto_donaciones(rs.getString("nombre_contacto_donaciones"));
                usuario.setNumero_contacto_donaciones(rs.getString("numero_contacto_donaciones"));
                usuario.setNumero_yape_plin(rs.getString("numero_yape_plin"));

                // Cargar el distrito
                Distrito distrito = new Distrito();
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                usuario.setDistrito(distrito);

                // Cargar foto de portada y logo
                usuario.setFoto_de_portada_albergue(rs.getBytes("foto_de_portada_albergue"));
                usuario.setNombre_foto_de_portada(rs.getString("nombre_foto_de_portada"));
                usuario.setLogo_albergue(rs.getBytes("logo_albergue"));
                usuario.setNombre_logo_albergue(rs.getString("nombre_logo_albergue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    // Método para obtener los puntos de acopio asociados al usuario
    public List<PuntoAcopio> obtenerPuntosAcopio(int idUsuarioAlbergue) {
        List<PuntoAcopio> puntosAcopio = new ArrayList<>();
        String query = "SELECT pa.id_punto_acopio, pa.direccion_punto_acopio, d.nombre_distrito " +
                "FROM PuntoAcopio pa " +
                "JOIN Distrito d ON pa.id_distrito = d.id_distrito " +
                "WHERE pa.id_usuario_albergue = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuarioAlbergue);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PuntoAcopio puntoAcopio = new PuntoAcopio();
                puntoAcopio.setId_punto_acopio(rs.getInt("id_punto_acopio"));
                puntoAcopio.setDireccion_punto_acopio(rs.getString("direccion_punto_acopio"));

                // Cargar distrito del punto de acopio
                Distrito distrito = new Distrito();
                distrito.setNombre_distrito(rs.getString("nombre_distrito"));
                puntoAcopio.setDistrito(distrito);

                puntosAcopio.add(puntoAcopio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puntosAcopio;
    }


    /*
    public boolean modificarPerfilAlbergue(Usuario usuario) {
        String query = "UPDATE Usuario SET nombre_albergue = ?, nombres_encargado = ?, apellidos_encargado = ?, " +
                "anio_creacion = ?, cantidad_animales = ?, espacio_disponible = ?, url_instagram = ?, " +
                "direccion_donaciones = ?, nombre_contacto_donaciones = ?, numero_contacto_donaciones = ?, " +
                "numero_yape_plin = ?, foto_de_portada_albergue = ?, nombre_foto_de_portada = ?, logo_albergue = ?, nombre_logo_albergue = ? " +
                "WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getNombre_albergue());
            stmt.setString(2, usuario.getNombres_encargado());
            stmt.setString(3, usuario.getApellidos_encargado());
            stmt.setString(4, usuario.getAnio_creacion());
            stmt.setInt(5, usuario.getCantidad_animales());
            stmt.setInt(6, usuario.getEspacio_disponible());
            stmt.setString(7, usuario.getUrl_instagram());
            stmt.setString(8, usuario.getDireccion_donaciones());
            stmt.setString(9, usuario.getNombre_contacto_donaciones());
            stmt.setString(10, usuario.getNumero_contacto_donaciones());
            stmt.setString(11, usuario.getNumero_yape_plin());

            // Manejar imágenes
            if (usuario.getFoto_de_portada_albergue() != null) {
                stmt.setBytes(12, usuario.getFoto_de_portada_albergue());
            } else {
                stmt.setNull(12, java.sql.Types.BLOB);
            }

            stmt.setString(13, usuario.getNombre_foto_de_portada());

            if (usuario.getLogo_albergue() != null) {
                stmt.setBytes(14, usuario.getLogo_albergue());
            } else {
                stmt.setNull(14, java.sql.Types.BLOB);
            }

            stmt.setString(15, usuario.getNombre_logo_albergue());
            stmt.setInt(16, usuario.getId_usuario());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar los puntos de acopio
    public boolean actualizarPuntosAcopio(List<PuntoAcopio> puntosAcopio) {
        String query = "UPDATE PuntoAcopio SET direccion_punto_acopio = ?, id_distrito = ? WHERE id_punto_acopio = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (PuntoAcopio punto : puntosAcopio) {
                stmt.setString(1, punto.getDireccion_punto_acopio() != null ? punto.getDireccion_punto_acopio() : ""); // Verificación nula
                stmt.setInt(2, punto.getDistrito().getId_distrito());
                stmt.setInt(3, punto.getId_punto_acopio());
                stmt.addBatch();
            }
            int[] rowsAffected = stmt.executeBatch();
            return rowsAffected.length == puntosAcopio.size();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     */


    public boolean modificarPerfilAlbergue(Usuario usuario) {
        String query = "UPDATE Usuario SET nombre_albergue = ?, nombres_encargado = ?, apellidos_encargado = ?, " +
                "anio_creacion = ?, cantidad_animales = ?, espacio_disponible = ?, url_instagram = ?, " +
                "direccion_donaciones = ?, nombre_contacto_donaciones = ?, numero_contacto_donaciones = ?, " +
                "numero_yape_plin = ?, foto_perfil = ?, nombre_foto_perfil = ?, foto_de_portada_albergue = ?, " +
                "nombre_foto_de_portada = ?, imagen_qr = ?, nombre_imagen_qr = ?, descripcion_perfil = ? " +
                "WHERE id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getNombre_albergue());
            stmt.setString(2, usuario.getNombres_encargado());
            stmt.setString(3, usuario.getApellidos_encargado());
            stmt.setString(4, usuario.getAnio_creacion());
            stmt.setInt(5, usuario.getCantidad_animales());
            stmt.setInt(6, usuario.getEspacio_disponible());
            stmt.setString(7, usuario.getUrl_instagram());
            stmt.setString(8, usuario.getDireccion_donaciones());
            stmt.setString(9, usuario.getNombre_contacto_donaciones());
            stmt.setString(10, usuario.getNumero_contacto_donaciones());
            stmt.setString(11, usuario.getNumero_yape_plin());

            // Manejo de imágenes
            if (usuario.getFoto_perfil() != null) {
                stmt.setBytes(12, usuario.getFoto_perfil());
            } else {
                stmt.setNull(12, java.sql.Types.BLOB);
            }
            stmt.setString(13, usuario.getNombre_foto_perfil());

            if (usuario.getFoto_de_portada_albergue() != null) {
                stmt.setBytes(14, usuario.getFoto_de_portada_albergue());
            } else {
                stmt.setNull(14, java.sql.Types.BLOB);
            }
            stmt.setString(15, usuario.getNombre_foto_de_portada());

            if (usuario.getImagen_qr() != null) {
                stmt.setBytes(16, usuario.getImagen_qr());
            } else {
                stmt.setNull(16, java.sql.Types.BLOB);
            }
            stmt.setString(17, usuario.getNombre_imagen_qr());

            // Manejo de descripción del perfil
            stmt.setString(18, usuario.getDescripcion_perfil());

            stmt.setInt(19, usuario.getId_usuario());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Método específico para actualizar una sola imagen (perfil, portada o QR/Yape)
    public boolean actualizarImagen(int idUsuario, byte[] imagen, String nombreImagen, String tipoImagen) {
        String query = "";
        switch (tipoImagen) {
            case "perfil":
                query = "UPDATE Usuario SET foto_perfil = ?, nombre_foto_perfil = ? WHERE id_usuario = ?";
                break;
            case "portada":
                query = "UPDATE Usuario SET foto_de_portada_albergue = ?, nombre_foto_de_portada = ? WHERE id_usuario = ?";
                break;
            case "qr_o_yape":
                query = "UPDATE Usuario SET imagen_qr_o_yape = ?, nombre_imagen_qr_o_yape = ? WHERE id_usuario = ?";
                break;
            default:
                return false;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBytes(1, imagen);
            stmt.setString(2, nombreImagen);
            stmt.setInt(3, idUsuario);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar los puntos de acopio
    public boolean actualizarPuntosAcopio(List<PuntoAcopio> puntosAcopio) {
        String query = "UPDATE PuntoAcopio SET direccion_punto_acopio = ?, id_distrito = ? WHERE id_punto_acopio = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (PuntoAcopio punto : puntosAcopio) {
                stmt.setString(1, punto.getDireccion_punto_acopio() != null ? punto.getDireccion_punto_acopio() : ""); // Verificación nula
                stmt.setInt(2, punto.getDistrito().getId_distrito());
                stmt.setInt(3, punto.getId_punto_acopio());
                stmt.addBatch();
            }

            int[] rowsAffected = stmt.executeBatch();
            return rowsAffected.length == puntosAcopio.size();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
