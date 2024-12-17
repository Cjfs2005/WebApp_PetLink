package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AlbergueDenunciaMaltratoDAO extends DaoBase {

    /**
     * Método para obtener todas las denuncias aceptadas y activas.
     */
    public List<DenunciaMaltratoAnimal> obtenerDenunciasAceptadas() {
        List<DenunciaMaltratoAnimal> denuncias = new ArrayList<>();
        String sql = """
                SELECT d.id_denuncia_maltrato_animal, d.nombre_foto_animal, d.foto_animal,
                       d.tamanio, d.raza, d.descripcion_maltrato, d.nombre_maltratador,
                       d.direccion_maltrato, d.es_denuncia_activa, d.fecha_hora_registro,
                       d.tiene_denuncia_policial, u.nombres_usuario_final, u.apellidos_usuario_final,
                       e.nombre_estado
                FROM DenunciaMaltratoAnimal d
                INNER JOIN Usuario u ON d.id_usuario_final = u.id_usuario
                INNER JOIN Estado e ON d.id_estado = e.id_estado
                WHERE d.es_denuncia_activa = 1
                ORDER BY d.fecha_hora_registro DESC
                """;
        // añadir  AND d.id_estado = 1 al WHERE para obtener las denuncias aceptadas

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                DenunciaMaltratoAnimal denuncia = fetchDenunciaData(rs);

                // Convertir el Blob a Base64 y almacenarlo en el atributo nombreFotoAnimal
                byte[] fotoBlob = rs.getBytes("foto_animal");
                if (fotoBlob != null) {
                    String base64Image = convertirBase64(fotoBlob);
                    denuncia.setNombreFotoAnimal(base64Image); // Sobrescribir con el Base64 temporalmente
                }

                denuncias.add(denuncia);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener denuncias aceptadas: " + e.getMessage(), e);
        }

        return denuncias;
    }

    /**
     * Método para obtener una denuncia específica por su ID.
     */
    public DenunciaMaltratoAnimal obtenerDenunciaPorId(int idDenuncia) {
        String sql = """
                SELECT d.id_denuncia_maltrato_animal, d.nombre_foto_animal, d.foto_animal,
                       d.tamanio, d.raza, d.descripcion_maltrato, d.nombre_maltratador,
                       d.direccion_maltrato, d.es_denuncia_activa, d.fecha_hora_registro,
                       d.tiene_denuncia_policial, u.nombres_usuario_final, u.apellidos_usuario_final,
                       e.nombre_estado
                FROM DenunciaMaltratoAnimal d
                INNER JOIN Usuario u ON d.id_usuario_final = u.id_usuario
                INNER JOIN Estado e ON d.id_estado = e.id_estado
                WHERE d.id_denuncia_maltrato_animal = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idDenuncia);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DenunciaMaltratoAnimal denuncia = fetchDenunciaData(rs);

                    // Convertir el Blob a Base64 y almacenarlo en el atributo nombreFotoAnimal
                    byte[] fotoBlob = rs.getBytes("foto_animal");
                    if (fotoBlob != null) {
                        String base64Image = convertirBase64(fotoBlob);
                        denuncia.setNombreFotoAnimal(base64Image); // Sobrescribir con el Base64 temporalmente
                    }

                    return denuncia;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la denuncia por ID: " + e.getMessage(), e);
        }

        return null; // Si no se encuentra la denuncia
    }

    /**
     * Método genérico para mapear los datos de ResultSet a un objeto DenunciaMaltratoAnimal.
     */
    private DenunciaMaltratoAnimal fetchDenunciaData(ResultSet rs) throws SQLException {
        DenunciaMaltratoAnimal denuncia = new DenunciaMaltratoAnimal();
        denuncia.setIdDenunciaMaltratoAnimal(rs.getInt("id_denuncia_maltrato_animal"));
        denuncia.setNombreFotoAnimal(rs.getString("nombre_foto_animal")); // Nombre de la foto
        denuncia.setFotoAnimal(rs.getBytes("foto_animal")); // Blob de la foto
        denuncia.setTamanio(rs.getString("tamanio"));
        denuncia.setRaza(rs.getString("raza"));
        denuncia.setDescripcionMaltrato(rs.getString("descripcion_maltrato"));
        denuncia.setNombreMaltratador(rs.getString("nombre_maltratador"));
        denuncia.setDireccionMaltrato(rs.getString("direccion_maltrato"));
        denuncia.setEsDenunciaActiva(rs.getBoolean("es_denuncia_activa"));
        denuncia.setFechaHoraRegistro(rs.getTimestamp("fecha_hora_registro").toLocalDateTime());
        denuncia.setTieneDenunciaPolicial(rs.getBoolean("tiene_denuncia_policial"));

        // Usuario denunciante
        Usuario usuario = new Usuario();
        usuario.setNombres_usuario_final(rs.getString("nombres_usuario_final"));
        usuario.setApellidos_usuario_final(rs.getString("apellidos_usuario_final"));
        denuncia.setUsuarioFinal(usuario);

        // Estado de la denuncia
        Estado estado = new Estado();
        estado.setNombre_estado(rs.getString("nombre_estado"));
        denuncia.setEstado(estado);

        return denuncia;
    }

    /**
     * Método para convertir un array de bytes (Blob) a Base64.
     */
    private String convertirBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
