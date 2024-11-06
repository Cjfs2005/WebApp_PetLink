package com.example.webapp_petlink.beans;

import java.util.Date;

public class RegistroDonacionEconomica {
    private int idRegistroDonacionEconomica;
    private int montoDonacion;
    private Date fechaHoraRegistro;
    private SolicitudDonacionEconomica solicitudDonacionEconomica;
    private Usuario usuarioFinal;
    private Estado estado;
    private byte[] imagenDonacionEconomica;
    private String nombreImagenDonacionEconomica;

    // Getters y Setters


    public int getIdRegistroDonacionEconomica() {
        return idRegistroDonacionEconomica;
    }

    public void setIdRegistroDonacionEconomica(int idRegistroDonacionEconomica) {
        this.idRegistroDonacionEconomica = idRegistroDonacionEconomica;
    }

    public int getMontoDonacion() {
        return montoDonacion;
    }

    public void setMontoDonacion(int montoDonacion) {
        this.montoDonacion = montoDonacion;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public SolicitudDonacionEconomica getSolicitudDonacionEconomica() {
        return solicitudDonacionEconomica;
    }

    public void setSolicitudDonacionEconomica(SolicitudDonacionEconomica solicitudDonacionEconomica) {
        this.solicitudDonacionEconomica = solicitudDonacionEconomica;
    }

    public Usuario getUsuarioFinal() {
        return usuarioFinal;
    }

    public void setUsuarioFinal(Usuario usuarioFinal) {
        this.usuarioFinal = usuarioFinal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public byte[] getImagenDonacionEconomica() {
        return imagenDonacionEconomica;
    }

    public void setImagenDonacionEconomica(byte[] imagenDonacionEconomica) {
        this.imagenDonacionEconomica = imagenDonacionEconomica;
    }

    public String getNombreImagenDonacionEconomica() {
        return nombreImagenDonacionEconomica;
    }

    public void setNombreImagenDonacionEconomica(String nombreImagenDonacionEconomica) {
        this.nombreImagenDonacionEconomica = nombreImagenDonacionEconomica;
    }
}
