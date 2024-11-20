package com.example.webapp_petlink.beans;

import java.util.Date;

public class PublicacionEventoBenefico {
    private int idPublicacionEventoBenefico;
    private String nombreEvento;
    private Date fechaHoraInicioEvento;
    private Date fechaHoraFinEvento;
    private int aforoEvento;
    private String entradaEvento;
    private String descripcionEvento;
    private String artistasProvedoresInvitados;
    private String razonEvento;
    private boolean esEventoActivo;
    private Date fechaHoraRegistro;
    private LugarEvento lugarEvento;
    private Usuario usuarioAlbergue;
    private byte[] foto;
    private String nombreFoto;
    private Estado estado;
    private int cantAsistentes;

    // Getters y Setters


    public int getIdPublicacionEventoBenefico() {
        return idPublicacionEventoBenefico;
    }

    public void setIdPublicacionEventoBenefico(int idPublicacionEventoBenefico) {
        this.idPublicacionEventoBenefico = idPublicacionEventoBenefico;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public Date getFechaHoraInicioEvento() {
        return fechaHoraInicioEvento;
    }

    public void setFechaHoraInicioEvento(Date fechaHoraInicioEvento) {
        this.fechaHoraInicioEvento = fechaHoraInicioEvento;
    }

    public Date getFechaHoraFinEvento() {
        return fechaHoraFinEvento;
    }

    public void setFechaHoraFinEvento(Date fechaHoraFinEvento) {
        this.fechaHoraFinEvento = fechaHoraFinEvento;
    }

    public int getAforoEvento() {
        return aforoEvento;
    }

    public void setAforoEvento(int aforoEvento) {
        this.aforoEvento = aforoEvento;
    }

    public String getEntradaEvento() {
        return entradaEvento;
    }

    public void setEntradaEvento(String entradaEvento) {
        this.entradaEvento = entradaEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public String getArtistasProvedoresInvitados() {
        return artistasProvedoresInvitados;
    }

    public void setArtistasProvedoresInvitados(String artistasProvedoresInvitados) {
        this.artistasProvedoresInvitados = artistasProvedoresInvitados;
    }

    public String getRazonEvento() {
        return razonEvento;
    }

    public void setRazonEvento(String razonEvento) {
        this.razonEvento = razonEvento;
    }

    public boolean isEsEventoActivo() {
        return esEventoActivo;
    }

    public void setEsEventoActivo(boolean esEventoActivo) {
        this.esEventoActivo = esEventoActivo;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public LugarEvento getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(LugarEvento lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    public Usuario getUsuarioAlbergue() {
        return usuarioAlbergue;
    }

    public void setUsuarioAlbergue(Usuario usuarioAlbergue) {
        this.usuarioAlbergue = usuarioAlbergue;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getCantAsistentes() {
        return cantAsistentes;
    }

    public void setCantAsistentes(int cantAsistentes) {
        this.cantAsistentes = cantAsistentes;
    }
}
