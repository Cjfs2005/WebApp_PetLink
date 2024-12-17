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

    public int getIdPublicacionMascotaPerdida() {
        return idPublicacionMascotaPerdida;
    }

    public void setIdPublicacionMascotaPerdida(int idPublicacionMascotaPerdida) {
        this.idPublicacionMascotaPerdida = idPublicacionMascotaPerdida;
    }

    public byte[] getFotoMascota() {
        return fotoMascota;
    }

    public void setFotoMascota(byte[] fotoMascota) {
        this.fotoMascota = fotoMascota;
    }

    public String getDescripcionMascota() {
        return descripcionMascota;
    }

    public void setDescripcionMascota(String descripcionMascota) {
        this.descripcionMascota = descripcionMascota;
    }

    public String getNombreFotoMascota() {
        return nombreFotoMascota;
    }

    public void setNombreFotoMascota(String nombreFotoMascota) {
        this.nombreFotoMascota = nombreFotoMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdadMascota() {
        return edadMascota;
    }

    public void setEdadMascota(String edadMascota) {
        this.edadMascota = edadMascota;
    }

    public String getRazaMascota() {
        return razaMascota;
    }

    public void setRazaMascota(String razaMascota) {
        this.razaMascota = razaMascota;
    }

    public String getTamanioMascota() {
        return tamanioMascota;
    }

    public void setTamanioMascota(String tamanioMascota) {
        this.tamanioMascota = tamanioMascota;
    }

    public String getLugarPerdida() {
        return lugarPerdida;
    }

    public void setLugarPerdida(String lugarPerdida) {
        this.lugarPerdida = lugarPerdida;
    }

    public String getCelularContacto() {
        return celularContacto;
    }

    public void setCelularContacto(String celularContacto) {
        this.celularContacto = celularContacto;
    }

    public String getFechaPerdida() {
        return fechaPerdida;
    }

    public void setFechaPerdida(String fechaPerdida) {
        this.fechaPerdida = fechaPerdida;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public Integer getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(Integer recompensa) {
        this.recompensa = recompensa;
    }

    public boolean isEsPublicacionActiva() {
        return esPublicacionActiva;
    }

    public void setEsPublicacionActiva(boolean esPublicacionActiva) {
        this.esPublicacionActiva = esPublicacionActiva;
    }

    public String getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(String fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public TipoPublicacionMascotaPerdida getTipoPublicacionMascotaPerdida() {
        return tipoPublicacionMascotaPerdida;
    }

    public void setTipoPublicacionMascotaPerdida(TipoPublicacionMascotaPerdida tipoPublicacionMascotaPerdida) {
        this.tipoPublicacionMascotaPerdida = tipoPublicacionMascotaPerdida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public PublicacionMascotaPerdida getPublicacionUltimaActualizacion() {
        return publicacionUltimaActualizacion;
    }

    public void setPublicacionUltimaActualizacion(PublicacionMascotaPerdida publicacionUltimaActualizacion) {
        this.publicacionUltimaActualizacion = publicacionUltimaActualizacion;
    }

    public String getFechaHoraUltimaActualizacion() {
        return fechaHoraUltimaActualizacion;
    }

    public void setFechaHoraUltimaActualizacion(String fechaHoraUltimaActualizacion) {
        this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion;
    }

    public boolean isFueEncontrada() {
        return fueEncontrada;
    }

    public void setFueEncontrada(boolean fueEncontrada) {
        this.fueEncontrada = fueEncontrada;
    }
}
