package com.example.webapp_petlink.beans;

import java.util.Date;

public class PostulacionMascotaAdopcion {
    private int idPostulacionMascotaAdopcion;
    private Date fechaHoraRegistro;
    private PublicacionMascotaAdopcion publicacionMascotaAdopcion;
    private Usuario usuarioFinal;
    private Estado estado;

    // Getters y Setters


    public int getIdPostulacionMascotaAdopcion() {
        return idPostulacionMascotaAdopcion;
    }

    public void setIdPostulacionMascotaAdopcion(int idPostulacionMascotaAdopcion) {
        this.idPostulacionMascotaAdopcion = idPostulacionMascotaAdopcion;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public PublicacionMascotaAdopcion getPublicacionMascotaAdopcion() {
        return publicacionMascotaAdopcion;
    }

    public void setPublicacionMascotaAdopcion(PublicacionMascotaAdopcion publicacionMascotaAdopcion) {
        this.publicacionMascotaAdopcion = publicacionMascotaAdopcion;
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
}
