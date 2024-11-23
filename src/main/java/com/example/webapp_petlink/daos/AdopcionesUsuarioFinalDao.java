package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.*;
import java.sql.*;
import java.util.ArrayList;

public class AdopcionesUsuarioFinalDao extends DaoBase{

    //En general se va a recibir un idUsuario que se recepcionara desde el Login
    //pero en esta ocasion asumiremos que el idUsuario = 1 (Tony Flores)

    public Usuario obtenerDatosUsuario(int idUsuario) {
        Usuario usuario = null;
        String query = "SELECT u.id_usuario, u.nombres_usuario_final, u.apellidos_usuario_final, u.foto_perfil, u.correo_electronico FROM Usuario u  WHERE u.id_usuario = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Se ha leido correctamente la data del usuario");
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                usuario.setFoto_perfil(rs.getBytes("foto_perfil"));
                usuario.setCorreo_electronico(rs.getString("correo_electronico"));
            }
            else{
                System.out.println("No se ha encontrado el usuario");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public ArrayList<PublicacionMascotaAdopcion> obtenerListaPublicacionesAdopcion() {

        ArrayList<PublicacionMascotaAdopcion> listaPublicacionesAdopcion = new ArrayList<>();
        String query = "SELECT pma.id_publicacion_mascota_adopcion, pma.nombre_mascota, pma.foto_mascota FROM PublicacionMascotaAdopcion pma WHERE pma.id_estado = ? AND pma.id_adoptante IS NULL";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, 2); //Estado activo
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

    public PublicacionMascotaAdopcion obtenerPublicacionAdopcion(int idPublicacion) {

        PublicacionMascotaAdopcion publicacion = null;

        String query = "SELECT p.id_publicacion_mascota_adopcion, p.tipo_raza, p.lugar_encontrado, p.descripcion_mascota, p.edad_aproximada, p.genero_mascota, p.foto_mascota, p.esta_en_temporal, p.condiciones_adopcion, p.nombre_mascota FROM PublicacionMascotaAdopcion p WHERE p.id_publicacion_mascota_adopcion = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1, idPublicacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                publicacion = new PublicacionMascotaAdopcion();

                publicacion.setIdPublicacionMascotaAdopcion(rs.getInt("id_publicacion_mascota_adopcion"));
                publicacion.setTipoRaza(rs.getString("tipo_raza"));
                publicacion.setLugarEncontrado(rs.getString("lugar_encontrado"));
                publicacion.setDescripcionMascota(rs.getString("descripcion_mascota"));
                publicacion.setEdadAproximada(rs.getString("edad_aproximada"));
                publicacion.setGeneroMascota(rs.getString("genero_mascota"));
                publicacion.setFotoMascota(rs.getBytes("foto_mascota"));
                publicacion.setEstaEnTemporal(rs.getBoolean("esta_en_temporal"));
                publicacion.setCondicionesAdopcion(rs.getString("condiciones_adopcion"));
                publicacion.setNombreMascota(rs.getString("nombre_mascota"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publicacion;
    }

    public boolean registrarPostulacionMascotaAdopcion(Timestamp fechaHoraRegistro, int idPublicacionMascotaAdopcion, int idUsuarioFinal, int idEstado) {
        String query = "INSERT INTO PostulacionMascotaAdopcion (fecha_hora_registro, id_publicacion_mascota_adopcion, id_usuario_final, id_estado) VALUES (?, ?, ?, ?)";
        boolean registroExitoso = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, fechaHoraRegistro);
            stmt.setInt(2, idPublicacionMascotaAdopcion);
            stmt.setInt(3, idUsuarioFinal);
            stmt.setInt(4, idEstado);

            int filasAfectadas = stmt.executeUpdate();
            registroExitoso = filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registroExitoso;
    }

    public ArrayList<PostulacionConPublicacionAdopcion> verHistorialPostulacionMascotaAdopcion(int idUsuarioFinal){

        ArrayList<PostulacionConPublicacionAdopcion> historialPostulacionMascotaAdopcion = new ArrayList<>();

        String query = "SELECT po.id_publicacion_mascota_adopcion, po.fecha_hora_registro, po.id_estado, pu.nombre_mascota, pu.foto_mascota FROM postulacionmascotaadopcion po JOIN publicacionmascotaadopcion pu ON po.id_publicacion_mascota_adopcion = pu.id_publicacion_mascota_adopcion WHERE po.id_usuario_final = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuarioFinal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PostulacionConPublicacionAdopcion data = new PostulacionConPublicacionAdopcion();
                data.setIdPublicacion(rs.getInt("id_publicacion_mascota_adopcion"));
                data.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro"));
                data.setIdEstado(rs.getInt("id_estado"));
                data.setNombreMascota(rs.getString("nombre_mascota"));
                data.setFotoMascota(rs.getBytes("foto_mascota"));

                historialPostulacionMascotaAdopcion.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historialPostulacionMascotaAdopcion;
    }

}
