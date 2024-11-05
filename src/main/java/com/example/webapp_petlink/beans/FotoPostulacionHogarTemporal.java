package com.example.webapp_petlink.beans;

public class FotoPostulacionHogarTemporal {
    private int idFotoPostulacionHogarTemporal;
    private byte[] fotoLugarTemporal;
    private String nombreFotoLugarTemporal;
    private PostulacionHogarTemporal postulacionHogarTemporal;

    public int getIdFotoPostulacionHogarTemporal() {
        return idFotoPostulacionHogarTemporal;
    }

    public void setIdFotoPostulacionHogarTemporal(int idFotoPostulacionHogarTemporal) {
        this.idFotoPostulacionHogarTemporal = idFotoPostulacionHogarTemporal;
    }

    public byte[] getFotoLugarTemporal() {
        return fotoLugarTemporal;
    }

    public void setFotoLugarTemporal(byte[] fotoLugarTemporal) {
        this.fotoLugarTemporal = fotoLugarTemporal;
    }

    public String getNombreFotoLugarTemporal() {
        return nombreFotoLugarTemporal;
    }

    public void setNombreFotoLugarTemporal(String nombreFotoLugarTemporal) {
        this.nombreFotoLugarTemporal = nombreFotoLugarTemporal;
    }

    public PostulacionHogarTemporal getPostulacionHogarTemporal() {
        return postulacionHogarTemporal;
    }

    public void setPostulacionHogarTemporal(PostulacionHogarTemporal postulacionHogarTemporal) {
        this.postulacionHogarTemporal = postulacionHogarTemporal;
    }
}
