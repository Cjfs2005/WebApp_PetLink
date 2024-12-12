package com.example.webapp_petlink.daos;

import com.example.webapp_petlink.beans.DenunciaMaltratoAnimal;
import com.example.webapp_petlink.beans.Estado;
import com.example.webapp_petlink.beans.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterDao extends DaoBase{

    //Valida que no exista cuenta con el mismo correo o el mismo DNI

    public boolean existeUsuario(String dni, String correo){

        String sql = "SELECT u.dni, u.correo_electronico from mydb.Usuario u where dni = ? or correo_electronico = ?";

        boolean existe = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setString(1, dni);
             stmt.setString(2, correo);

             ResultSet rs = stmt.executeQuery();

             if(rs.next()){
                 existe = true;
             }
             else{
                 existe = false;
             }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return existe;
    }

    //Crea nuevo usuario y contrasenia temporal

    public void crearUsuario(Usuario usuario){

        String sql = "INSERT INTO mydb.Usuario(dni,nombres_usuario_final, apellidos_usuario_final, direccion, id_rol, correo_electronico, id_distrito, id_zona, contrasenia, es_contrasenia_temporal, fecha_hora_expiracion_contrasenia, es_primera_contrasenia_temporal, es_usuario_activo, fecha_hora_creacion) " +
                "VALUES(?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    //Crea nuevo albergue y contrasenia temporal

    //Actualiza la contrasenia del usuario/Albergue

}
