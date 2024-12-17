package com.example.webapp_petlink.beans;

import java.util.Date;

public class PostulacionConPublicacionAdopcion {

    private Date fechaHoraRegistro;
    private int idEstado;
    private String nombreMascota;
    private byte[] fotoMascota;
    private int idPublicacion;

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public byte[] getFotoMascota() {
        return fotoMascota;
    }

    public void setFotoMascota(byte[] fotoMascota) {
        this.fotoMascota = fotoMascota;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }
}
