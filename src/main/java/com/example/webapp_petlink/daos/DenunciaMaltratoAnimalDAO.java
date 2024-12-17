package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Usuario;
import com.example.webapp_petlink.beans.Estado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DenunciaMaltratoAnimalDAO extends DaoBase {

    public boolean insertarDenuncia(DenunciaMaltratoAnimal denuncia) {
        String sql = "INSERT INTO DenunciaMaltratoAnimal (nombre_foto_animal, foto_animal, tamanio, raza, descripcion_maltrato, nombre_maltratador, direccion_maltrato, es_denuncia_activa, fecha_hora_registro, id_usuario_final, id_estado, tiene_denuncia_policial) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, denuncia.getNombreFotoAnimal());
            pstmt.setBytes(2, denuncia.getFotoAnimal());
            pstmt.setString(3, denuncia.getTamanio());
            pstmt.setString(4, denuncia.getRaza());
            pstmt.setString(5, denuncia.getDescripcionMaltrato());
            pstmt.setString(6, denuncia.getNombreMaltratador());
            pstmt.setString(7, denuncia.getDireccionMaltrato());
            pstmt.setBoolean(8, denuncia.isEsDenunciaActiva());
            pstmt.setTimestamp(9, Timestamp.valueOf(denuncia.getFechaHoraRegistro()));
            pstmt.setInt(10, denuncia.getUsuarioFinal().getId_usuario());
            pstmt.setInt(11, denuncia.getEstado().getId_estado());
            pstmt.setBoolean(12, denuncia.isTieneDenunciaPolicial());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DenunciaMaltratoAnimal obtenerDenunciaPorId(int id) {
        String sql = "SELECT d.*, u.*, e.* " +
                "FROM DenunciaMaltratoAnimal d " +
                "LEFT JOIN Usuario u ON d.id_usuario_final = u.id_usuario " +
                "LEFT JOIN Estado e ON d.id_estado = e.id_estado " +
                "WHERE d.id_denuncia_maltrato_animal = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return fetchDenunciaData(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DenunciaMaltratoAnimal> obtenerTodasLasDenuncias(int idUsuario) {
        List<DenunciaMaltratoAnimal> denuncias = new ArrayList<>();
        String sql = "SELECT d.*, e.*,u.* " +
                "FROM DenunciaMaltratoAnimal d " +
                "LEFT JOIN Usuario u ON d.id_usuario_final = u.id_usuario " +
                "LEFT JOIN Estado e ON d.id_estado = e.id_estado " + // Espacio añadido al final
                "WHERE u.id_usuario = ? AND e.id_estado = 2 and d.es_denuncia_activa=1 "+
                "order by id_denuncia_maltrato_animal desc"; // Cambiar `and` a `AND` por consistencia

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario); // Configurar el parámetro correctamente
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    denuncias.add(fetchDenunciaData(rs)); // Procesar cada fila y agregarla a la lista
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener denuncias: " + e.getMessage(), e); // Mejor manejo de excepciones
        }
        return denuncias;
    }


    public boolean actualizarDenuncia(DenunciaMaltratoAnimal denuncia) {
        String sql = "UPDATE DenunciaMaltratoAnimal SET nombre_foto_animal = ?, foto_animal = ?, tamanio = ?, raza = ?, descripcion_maltrato = ?, tiene_denuncia_policial = ? " +
                "WHERE id_denuncia_maltrato_animal = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, denuncia.getNombreFotoAnimal());
            pstmt.setBytes(2, denuncia.getFotoAnimal());
            pstmt.setString(3, denuncia.getTamanio());
            pstmt.setString(4, denuncia.getRaza());
            pstmt.setString(5, denuncia.getDescripcionMaltrato());
            pstmt.setBoolean(6, denuncia.isTieneDenunciaPolicial());
            pstmt.setInt(7, denuncia.getIdDenunciaMaltratoAnimal());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminarDenuncia(int id) {
        String sql = "UPDATE DenunciaMaltratoAnimal SET es_denuncia_activa=0 WHERE id_denuncia_maltrato_animal = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DenunciaMaltratoAnimal fetchDenunciaData(ResultSet rs) throws SQLException {
        DenunciaMaltratoAnimal denuncia = new DenunciaMaltratoAnimal();
        denuncia.setIdDenunciaMaltratoAnimal(rs.getInt("id_denuncia_maltrato_animal"));
        denuncia.setNombreFotoAnimal(rs.getString("nombre_foto_animal"));
        denuncia.setFotoAnimal(rs.getBytes("foto_animal"));
        denuncia.setTamanio(rs.getString("tamanio"));
        denuncia.setRaza(rs.getString("raza"));
        denuncia.setDescripcionMaltrato(rs.getString("descripcion_maltrato"));
        denuncia.setNombreMaltratador(rs.getString("nombre_maltratador"));
        denuncia.setDireccionMaltrato(rs.getString("direccion_maltrato"));
        denuncia.setEsDenunciaActiva(rs.getBoolean("es_denuncia_activa"));
        denuncia.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());

        // Usuario Final
        Usuario usuario = new Usuario();
        usuario.setId_usuario(rs.getInt("id_usuario"));
        denuncia.setUsuarioFinal(usuario);

        // Estado
        Estado estado = new Estado();
        estado.setId_estado(rs.getInt("id_estado"));
        denuncia.setEstado(estado);

        denuncia.setTieneDenunciaPolicial(rs.getBoolean("tiene_denuncia_policial"));
        return denuncia;
    }
}
