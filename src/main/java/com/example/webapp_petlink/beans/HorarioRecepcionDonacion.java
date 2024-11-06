package com.example.webapp_petlink.beans;

import java.util.Date;

public class HorarioRecepcionDonacion {
    private int idHorarioRecepcionDonacion;
    private Date fechaHoraInicio;
    private Date fechaHoraFin;
    private PuntoAcopioDonacion puntoAcopioDonacion;

    // Getters y Setters

    public int getIdHorarioRecepcionDonacion() {
        return idHorarioRecepcionDonacion;
    }

    public void setIdHorarioRecepcionDonacion(int idHorarioRecepcionDonacion) {
        this.idHorarioRecepcionDonacion = idHorarioRecepcionDonacion;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public PuntoAcopioDonacion getPuntoAcopioDonacion() {
        return puntoAcopioDonacion;
    }

    public void setPuntoAcopioDonacion(PuntoAcopioDonacion puntoAcopioDonacion) {
        this.puntoAcopioDonacion = puntoAcopioDonacion;
    }
}
