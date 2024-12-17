package com.example.webapp_petlink.beans;

import java.util.Date;

public class PublicacionMascotaAdopcion {
    private int idPublicacionMascotaAdopcion;
    private String tipoRaza;
    private String lugarEncontrado;
    private String descripcionMascota;
    private String edadAproximada;
    private String generoMascota;
    private byte[] fotoMascota;
    private String nombreFotoMascota;
    private boolean estaEnTemporal;
    private String condicionesAdopcion;
    private boolean esPublicacionActiva;
    private Date fechaHoraRegistro;
    private Usuario usuarioAlbergue;
    private Estado estado;
    private String nombreMascota;
    private Usuario adoptante;

    // Getters y Setters


    public int getIdPublicacionMascotaAdopcion() {
        return idPublicacionMascotaAdopcion;
    }

    public void setIdPublicacionMascotaAdopcion(int idPublicacionMascotaAdopcion) {
        this.idPublicacionMascotaAdopcion = idPublicacionMascotaAdopcion;
    }

    public String getTipoRaza() {
        return tipoRaza;
    }

    public void setTipoRaza(String tipoRaza) {
        this.tipoRaza = tipoRaza;
    }

    public String getLugarEncontrado() {
        return lugarEncontrado;
    }

    public void setLugarEncontrado(String lugarEncontrado) {
        this.lugarEncontrado = lugarEncontrado;
    }

    public String getDescripcionMascota() {
        return descripcionMascota;
    }

    public void setDescripcionMascota(String descripcionMascota) {
        this.descripcionMascota = descripcionMascota;
    }

    public String getEdadAproximada() {
        return edadAproximada;
    }

    public void setEdadAproximada(String edadAproximada) {
        this.edadAproximada = edadAproximada;
    }

    public String getGeneroMascota() {
        return generoMascota;
    }

    public void setGeneroMascota(String generoMascota) {
        this.generoMascota = generoMascota;
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

    public boolean isEstaEnTemporal() {
        return estaEnTemporal;
    }

    public void setEstaEnTemporal(boolean estaEnTemporal) {
        this.estaEnTemporal = estaEnTemporal;
    }

    public String getCondicionesAdopcion() {
        return condicionesAdopcion;
    }

    public void setCondicionesAdopcion(String condicionesAdopcion) {
        this.condicionesAdopcion = condicionesAdopcion;
    }

    public boolean isEsPublicacionActiva() {
        return esPublicacionActiva;
    }

    public void setEsPublicacionActiva(boolean esPublicacionActiva) {
        this.esPublicacionActiva = esPublicacionActiva;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
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

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public Usuario getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Usuario adoptante) {
        this.adoptante = adoptante;
    }
}
