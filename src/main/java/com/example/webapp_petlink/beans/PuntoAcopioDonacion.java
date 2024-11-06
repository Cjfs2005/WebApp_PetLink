package com.example.webapp_petlink.beans;

public class PuntoAcopioDonacion {
    private int idPuntoAcopioDonacion;
    private SolicitudDonacionProductos solicitudDonacionProductos;
    private PuntoAcopio puntoAcopio;

    // Getters y Setters


    public int getIdPuntoAcopioDonacion() {
        return idPuntoAcopioDonacion;
    }

    public void setIdPuntoAcopioDonacion(int idPuntoAcopioDonacion) {
        this.idPuntoAcopioDonacion = idPuntoAcopioDonacion;
    }

    public SolicitudDonacionProductos getSolicitudDonacionProductos() {
        return solicitudDonacionProductos;
    }

    public void setSolicitudDonacionProductos(SolicitudDonacionProductos solicitudDonacionProductos) {
        this.solicitudDonacionProductos = solicitudDonacionProductos;
    }

    public PuntoAcopio getPuntoAcopio() {
        return puntoAcopio;
    }

    public void setPuntoAcopio(PuntoAcopio puntoAcopio) {
        this.puntoAcopio = puntoAcopio;
    }
}
