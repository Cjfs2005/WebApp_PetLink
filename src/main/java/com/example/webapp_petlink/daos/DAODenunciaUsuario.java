package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAODenunciaUsuario extends DaoBase {

    // Método para obtener todas las denuncias de un usuario
    public List<DenunciaMaltratoAnimal> obtenerDenunciasPorUsuario(int idUsuario) {
        List<DenunciaMaltratoAnimal> denuncias = new ArrayList<>();
        String query = "SELECT d.*, e.nombre_estado, u.nombres_usuario_final, u.apellidos_usuario_final " +
                "FROM DenunciaMaltratoAnimal d " +
                "JOIN Estado e ON d.id_estado = e.id_estado " +
                "JOIN Usuario u ON d.id_usuario_final = u.id_usuario " +
                "WHERE d.id_usuario_final = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DenunciaMaltratoAnimal denuncia = new DenunciaMaltratoAnimal();
                denuncia.setIdDenunciaMaltratoAnimal(rs.getInt("id_denuncia"));
                denuncia.setNombreFotoAnimal(rs.getString("nombre_foto_animal"));
                denuncia.setFotoAnimal(rs.getBytes("foto_animal"));
                denuncia.setTamanio(rs.getString("tamanio"));
                denuncia.setRaza(rs.getString("raza"));
                denuncia.setDescripcionMaltrato(rs.getString("descripcion_maltrato"));
                denuncia.setNombreMaltratador(rs.getString("nombre_maltratador"));
                denuncia.setDireccionMaltrato(rs.getString("direccion_maltrato"));
                denuncia.setEsDenunciaActiva(rs.getBoolean("es_denuncia_activa"));
                denuncia.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());
                denuncia.setTieneDenunciaPolicial(rs.getBoolean("tiene_denuncia_policial"));

                // Estado
                Estado estado = new Estado();
                estado.setId_estado(rs.getInt("id_estado"));
                estado.setNombre_estado(rs.getString("nombre_estado"));
                denuncia.setEstado(estado);

                // Usuario
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario_final"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                denuncia.setUsuarioFinal(usuario);

                denuncias.add(denuncia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return denuncias;
    }

    // Método para obtener una denuncia por su ID
    public DenunciaMaltratoAnimal obtenerDenunciaPorId(int idDenuncia) {
        DenunciaMaltratoAnimal denuncia = null;
        String query = "SELECT d.*, e.nombre_estado, u.nombres_usuario_final, u.apellidos_usuario_final " +
                "FROM DenunciaMaltratoAnimal d " +
                "JOIN Estado e ON d.id_estado = e.id_estado " +
                "JOIN Usuario u ON d.id_usuario_final = u.id_usuario " +
                "WHERE d.id_denuncia = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idDenuncia);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                denuncia = new DenunciaMaltratoAnimal();
                denuncia.setIdDenunciaMaltratoAnimal(rs.getInt("id_denuncia"));
                denuncia.setNombreFotoAnimal(rs.getString("nombre_foto_animal"));
                denuncia.setFotoAnimal(rs.getBytes("foto_animal"));
                denuncia.setTamanio(rs.getString("tamanio"));
                denuncia.setRaza(rs.getString("raza"));
                denuncia.setDescripcionMaltrato(rs.getString("descripcion_maltrato"));
                denuncia.setNombreMaltratador(rs.getString("nombre_maltratador"));
                denuncia.setDireccionMaltrato(rs.getString("direccion_maltrato"));
                denuncia.setEsDenunciaActiva(rs.getBoolean("es_denuncia_activa"));
                denuncia.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());
                denuncia.setTieneDenunciaPolicial(rs.getBoolean("tiene_denuncia_policial"));

                // Estado
                Estado estado = new Estado();
                estado.setId_estado(rs.getInt("id_estado"));
                estado.setNombre_estado(rs.getString("nombre_estado"));
                denuncia.setEstado(estado);

                // Usuario
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario_final"));
                usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
                usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
                denuncia.setUsuarioFinal(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return denuncia;
    }

    // Método para registrar una nueva denuncia
    public boolean registrarDenuncia(DenunciaMaltratoAnimal denuncia) {
        String query = "INSERT INTO DenunciaMaltratoAnimal (nombre_foto_animal, foto_animal, tamanio, raza, descripcion_maltrato, " +
                "nombre_maltratador, direccion_maltrato, es_denuncia_activa, fecha_hora_registro, id_usuario_final, id_estado, tiene_denuncia_policial) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, denuncia.getNombreFotoAnimal());
            stmt.setBytes(2, denuncia.getFotoAnimal());
            stmt.setString(3, denuncia.getTamanio());
            stmt.setString(4, denuncia.getRaza());
            stmt.setString(5, denuncia.getDescripcionMaltrato());
            stmt.setString(6, denuncia.getNombreMaltratador());
            stmt.setString(7, denuncia.getDireccionMaltrato());
            stmt.setBoolean(8, denuncia.isEsDenunciaActiva());
            stmt.setInt(9, denuncia.getUsuarioFinal().getId_usuario());
            stmt.setInt(10, denuncia.getEstado().getId_estado());
            stmt.setBoolean(11, denuncia.isTieneDenunciaPolicial());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar una denuncia
    public boolean actualizarDenuncia(DenunciaMaltratoAnimal denuncia) {
        String query = "UPDATE DenunciaMaltratoAnimal SET nombre_foto_animal = ?, foto_animal = ?, tamanio = ?, raza = ?, " +
                "descripcion_maltrato = ?, nombre_maltratador = ?, direccion_maltrato = ?, es_denuncia_activa = ?, " +
                "id_estado = ?, tiene_denuncia_policial = ? WHERE id_denuncia = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, denuncia.getNombreFotoAnimal());
            stmt.setBytes(2, denuncia.getFotoAnimal());
            stmt.setString(3, denuncia.getTamanio());
            stmt.setString(4, denuncia.getRaza());
            stmt.setString(5, denuncia.getDescripcionMaltrato());
            stmt.setString(6, denuncia.getNombreMaltratador());
            stmt.setString(7, denuncia.getDireccionMaltrato());
            stmt.setBoolean(8, denuncia.isEsDenunciaActiva());
            stmt.setInt(9, denuncia.getEstado().getId_estado());
            stmt.setBoolean(10, denuncia.isTieneDenunciaPolicial());
            stmt.setInt(11, denuncia.getIdDenunciaMaltratoAnimal());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar una denuncia
    public boolean eliminarDenuncia(int idDenuncia) {
        String query = "DELETE FROM DenunciaMaltratoAnimal WHERE id_denuncia = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idDenuncia);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
