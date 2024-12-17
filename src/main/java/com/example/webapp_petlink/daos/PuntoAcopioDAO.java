package com.example.webapp_petlink.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PuntoAcopioDAO extends DaoBase {
    public void insertPuntoAcopio(int idUsuario, String puntoAcopio) {
        String sql = "INSERT INTO PuntoAcopio (direccion_punto_copio, id_distrito, id_usuario_albergue) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, puntoAcopio);
            pstmt.setInt(2, 2);
            pstmt.setInt(3, idUsuario);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
