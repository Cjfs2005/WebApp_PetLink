package com.example.webapp_petlink.beans;

import java.time.LocalDateTime;
import java.util.Date;

public class DenunciaMaltratoAnimal {
    private int idDenunciaMaltratoAnimal;
    private String nombreFotoAnimal;
    private byte[] fotoAnimal;
    private String tamanio;
    private String raza;
    private String descripcionMaltrato;
    private String nombreMaltratador;
    private String direccionMaltrato;
    private boolean esDenunciaActiva;
    private LocalDateTime fechaHoraRegistro;
    private Usuario usuarioFinal;
    private Estado estado;
    private boolean tieneDenunciaPolicial;

    // Getters y Setters

    public int getIdDenunciaMaltratoAnimal() {
        return idDenunciaMaltratoAnimal;
    }

    public void setIdDenunciaMaltratoAnimal(int idDenunciaMaltratoAnimal) {
        this.idDenunciaMaltratoAnimal = idDenunciaMaltratoAnimal;
    }

    public String getNombreFotoAnimal() {
        return nombreFotoAnimal;
    }

    public void setNombreFotoAnimal(String nombreFotoAnimal) {
        this.nombreFotoAnimal = nombreFotoAnimal;
    }

    public byte[] getFotoAnimal() {
        return fotoAnimal;
    }

    public void setFotoAnimal(byte[] fotoAnimal) {
        this.fotoAnimal = fotoAnimal;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getDescripcionMaltrato() {
        return descripcionMaltrato;
    }

    public void setDescripcionMaltrato(String descripcionMaltrato) {
        this.descripcionMaltrato = descripcionMaltrato;
    }

    public String getNombreMaltratador() {
        return nombreMaltratador;
    }

    public void setNombreMaltratador(String nombreMaltratador) {
        this.nombreMaltratador = nombreMaltratador;
    }

    public String getDireccionMaltrato() {
        return direccionMaltrato;
    }

    public void setDireccionMaltrato(String direccionMaltrato) {
        this.direccionMaltrato = direccionMaltrato;
    }

    public boolean isEsDenunciaActiva() {
        return esDenunciaActiva;
    }

    public void setEsDenunciaActiva(boolean esDenunciaActiva) {
        this.esDenunciaActiva = esDenunciaActiva;
    }

    public LocalDateTime getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(LocalDateTime fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
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

    public boolean isTieneDenunciaPolicial() {
        return tieneDenunciaPolicial;
    }

    public void setTieneDenunciaPolicial(boolean tieneDenunciaPolicial) {
        this.tieneDenunciaPolicial = tieneDenunciaPolicial;
    }
}
