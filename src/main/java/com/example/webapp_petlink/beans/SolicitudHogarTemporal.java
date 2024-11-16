package com.example.webapp_petlink.beans;

import java.util.Date;

public class SolicitudHogarTemporal {
    private int idSolicitudHogarTemporal;
    private byte[] fotoMascota;
    private String nombreFotoMascota;
    private String nombreMascota;
    private String descripcionMascota;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaHoraRegistro;
    private PostulacionHogarTemporal postulacionHogarTemporal;
    private Usuario usuarioAlbergue;
    private Estado estado;

    // Getters y Setters


    public int getIdSolicitudHogarTemporal() {
        return idSolicitudHogarTemporal;
    }

    public void setIdSolicitudHogarTemporal(int idSolicitudHogarTemporal) {
        this.idSolicitudHogarTemporal = idSolicitudHogarTemporal;
    }

    public byte[] getFotoMascota() {
        return fotoMascota;
    }

    public void setFotoMascota(byte[] fotoMascota) {
        this.fotoMascota = fotoMascota;
    }

    public String getNombreFotoMascota() {
        return nombreFotoMascota;
    }

    public void setNombreFotoMascota(String nombreFotoMascota) {
        this.nombreFotoMascota = nombreFotoMascota;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getDescripcionMascota() {
        return descripcionMascota;
    }

    public void setDescripcionMascota(String descripcionMascota) {
        this.descripcionMascota = descripcionMascota;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public PostulacionHogarTemporal getPostulacionHogarTemporal() {
        return postulacionHogarTemporal;
    }

    public void setPostulacionHogarTemporal(PostulacionHogarTemporal postulacionHogarTemporal) {
        this.postulacionHogarTemporal = postulacionHogarTemporal;
    }

    public Usuario getUsuarioAlbergue() {
        return usuarioAlbergue;
    }

    public void setUsuarioAlbergue(Usuario usuarioAlbergue) {
        this.usuarioAlbergue = usuarioAlbergue;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
