
package com.example.webapp_petlink.beans;

public class LugarEvento {
    
    private int id_lugar_evento;
    
    private String nombre_lugar_evento;
    
    private String direccion_lugar_evento;
    
    private int aforo_maximo;
    
    private Distrito distrito;
    

    // Getters and Setters
    
    public int getId_lugar_evento() {
        return id_lugar_evento;
    }

    public void setId_lugar_evento(int id_lugar_evento) {
        this.id_lugar_evento = id_lugar_evento;
    }
    
    public String getNombre_lugar_evento() {
        return nombre_lugar_evento;
    }

    public void setNombre_lugar_evento(String nombre_lugar_evento) {
        this.nombre_lugar_evento = nombre_lugar_evento;
    }
    
    public String getDireccion_lugar_evento() {
        return direccion_lugar_evento;
    }

    public void setDireccion_lugar_evento(String direccion_lugar_evento) {
        this.direccion_lugar_evento = direccion_lugar_evento;
    }
    
    public int getAforo_maximo() {
        return aforo_maximo;
    }

    public void setAforo_maximo(int aforo_maximo) {
        this.aforo_maximo = aforo_maximo;
    }
    
    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }
    
}