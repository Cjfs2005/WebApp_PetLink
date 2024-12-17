
package com.example.webapp_petlink.beans;

public class BaneoHogarTemporal {
    
    private int id_baneo_hogar_temporal;
    
    private String motivo;
    
    private java.time.LocalDateTime fecha_hora_registro;
    
    private Usuario usuario_final;
    
    private int tipo_de_baneo;
    

    // Getters and Setters
    
    public int getId_baneo_hogar_temporal() {
        return id_baneo_hogar_temporal;
    }

    public void setId_baneo_hogar_temporal(int id_baneo_hogar_temporal) {
        this.id_baneo_hogar_temporal = id_baneo_hogar_temporal;
    }
    
    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public java.time.LocalDateTime getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(java.time.LocalDateTime fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }
    
    public Usuario getUsuario_final() {
        return usuario_final;
    }

    public void setUsuario_final(Usuario usuario_final) {
        this.usuario_final = usuario_final;
    }
    
    public int getTipo_de_baneo() {
        return tipo_de_baneo;
    }

    public void setTipo_de_baneo(int tipo_de_baneo) {
        this.tipo_de_baneo = tipo_de_baneo;
    }
    
}