package com.example.webapp_petlink.beans;

public class PublicacionMascotaPerdida {
    private int idPublicacionMascotaPerdida;
    private String descripcionMascota;
    private byte[] fotoMascota;
    private String nombreFotoMascota;
    private String nombre;
    private String edadMascota;
    private String razaMascota;
    private String tamanioMascota;
    private String lugarPerdida;
    private String fechaPerdida;
    private String celularContacto;
    private String nombreContacto;
    private Integer recompensa;
    private boolean esPublicacionActiva;
    private String fechaHoraRegistro;
    private TipoPublicacionMascotaPerdida tipoPublicacionMascotaPerdida;
    private Usuario usuario;
    private Estado estado;
    private PublicacionMascotaPerdida publicacionUltimaActualizacion;
    private String fechaHoraUltimaActualizacion;
    private boolean fueEncontrada;

    public int getIdPublicacionMascotaPerdida() { return idPublicacionMascotaPerdida; }
    public void setIdPublicacionMascotaPerdida(int id) { this.idPublicacionMascotaPerdida = id; }

    // Resto de getters y setters...
}
