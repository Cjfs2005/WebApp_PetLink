package com.example.webapp_petlink.beans;

import java.time.LocalDateTime;

public class HorarioRecepcionDonacion {
    private int idHorarioRecepcionDonacion;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private PuntoAcopioDonacion puntoAcopioDonacion;

    // Getters y Setters


    public int getIdHorarioRecepcionDonacion() {
        return idHorarioRecepcionDonacion;
    }

    public void setIdHorarioRecepcionDonacion(int idHorarioRecepcionDonacion) {
        this.idHorarioRecepcionDonacion = idHorarioRecepcionDonacion;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public PuntoAcopioDonacion getPuntoAcopioDonacion() {
        return puntoAcopioDonacion;
    }

    public void setPuntoAcopioDonacion(PuntoAcopioDonacion puntoAcopioDonacion) {
        this.puntoAcopioDonacion = puntoAcopioDonacion;
    }


}
