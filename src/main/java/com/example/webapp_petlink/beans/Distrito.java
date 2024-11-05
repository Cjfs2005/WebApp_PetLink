
package com.example.webapp_petlink.beans;

public class Distrito {
    
    private int id_distrito;
    
    private String nombre_distrito;
    
    private Zona zona;
    

    // Getters and Setters
    
    public int getId_distrito() {
        return id_distrito;
    }

    public void setId_distrito(int id_distrito) {
        this.id_distrito = id_distrito;
    }
    
    public String getNombre_distrito() {
        return nombre_distrito;
    }

    public void setNombre_distrito(String nombre_distrito) {
        this.nombre_distrito = nombre_distrito;
    }
    
    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }
    
}