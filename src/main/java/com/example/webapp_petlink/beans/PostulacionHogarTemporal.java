
package com.example.webapp_petlink.beans;

public class PostulacionHogarTemporal {
    
    private int id_postulacion_hogar_temporal;
    
    private String edad_usuario;
    
    private String genero_usuario;
    
    private String celular_usuario;
    
    private String cantidad_cuartos;
    
    private String metraje_vivienda;
    
    private boolean tiene_mascotas;
    
    private String tipo_mascotas;
    
    private boolean tiene_hijos;
    
    private boolean tiene_dependientes;
    
    private String forma_trabajo;
    
    private String nombre_persona_referencia;
    
    private String celular_persona_referencia;
    
    private java.time.LocalDate fecha_inicio_temporal;
    
    private java.time.LocalDate fecha_fin_temporal;
    
    private java.time.LocalDateTime fecha_hora_registro;
    
    private int cantidad_rechazos_consecutivos;
    
    private Usuario usuario_final;
    
    private Estado estado;
    
    private boolean llamo_al_postulante;
    
    private java.time.LocalDate fecha_visita;
    

    // Getters and Setters
    
    public int getId_postulacion_hogar_temporal() {
        return id_postulacion_hogar_temporal;
    }

    public void setId_postulacion_hogar_temporal(int id_postulacion_hogar_temporal) {
        this.id_postulacion_hogar_temporal = id_postulacion_hogar_temporal;
    }
    
    public String getEdad_usuario() {
        return edad_usuario;
    }

    public void setEdad_usuario(String edad_usuario) {
        this.edad_usuario = edad_usuario;
    }
    
    public String getGenero_usuario() {
        return genero_usuario;
    }

    public void setGenero_usuario(String genero_usuario) {
        this.genero_usuario = genero_usuario;
    }
    
    public String getCelular_usuario() {
        return celular_usuario;
    }

    public void setCelular_usuario(String celular_usuario) {
        this.celular_usuario = celular_usuario;
    }
    
    public String getCantidad_cuartos() {
        return cantidad_cuartos;
    }

    public void setCantidad_cuartos(String cantidad_cuartos) {
        this.cantidad_cuartos = cantidad_cuartos;
    }
    
    public String getMetraje_vivienda() {
        return metraje_vivienda;
    }

    public void setMetraje_vivienda(String metraje_vivienda) {
        this.metraje_vivienda = metraje_vivienda;
    }
    
    public boolean getTiene_mascotas() {
        return tiene_mascotas;
    }

    public void setTiene_mascotas(boolean tiene_mascotas) {
        this.tiene_mascotas = tiene_mascotas;
    }
    
    public String getTipo_mascotas() {
        return tipo_mascotas;
    }

    public void setTipo_mascotas(String tipo_mascotas) {
        this.tipo_mascotas = tipo_mascotas;
    }
    
    public boolean getTiene_hijos() {
        return tiene_hijos;
    }

    public void setTiene_hijos(boolean tiene_hijos) {
        this.tiene_hijos = tiene_hijos;
    }
    
    public boolean getTiene_dependientes() {
        return tiene_dependientes;
    }

    public void setTiene_dependientes(boolean tiene_dependientes) {
        this.tiene_dependientes = tiene_dependientes;
    }
    
    public String getForma_trabajo() {
        return forma_trabajo;
    }

    public void setForma_trabajo(String forma_trabajo) {
        this.forma_trabajo = forma_trabajo;
    }
    
    public String getNombre_persona_referencia() {
        return nombre_persona_referencia;
    }

    public void setNombre_persona_referencia(String nombre_persona_referencia) {
        this.nombre_persona_referencia = nombre_persona_referencia;
    }
    
    public String getCelular_persona_referencia() {
        return celular_persona_referencia;
    }

    public void setCelular_persona_referencia(String celular_persona_referencia) {
        this.celular_persona_referencia = celular_persona_referencia;
    }
    
    public java.time.LocalDate getFecha_inicio_temporal() {
        return fecha_inicio_temporal;
    }

    public void setFecha_inicio_temporal(java.time.LocalDate fecha_inicio_temporal) {
        this.fecha_inicio_temporal = fecha_inicio_temporal;
    }
    
    public java.time.LocalDate getFecha_fin_temporal() {
        return fecha_fin_temporal;
    }

    public void setFecha_fin_temporal(java.time.LocalDate fecha_fin_temporal) {
        this.fecha_fin_temporal = fecha_fin_temporal;
    }
    
    public java.time.LocalDateTime getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(java.time.LocalDateTime fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }
    
    public int getCantidad_rechazos_consecutivos() {
        return cantidad_rechazos_consecutivos;
    }

    public void setCantidad_rechazos_consecutivos(int cantidad_rechazos_consecutivos) {
        this.cantidad_rechazos_consecutivos = cantidad_rechazos_consecutivos;
    }
    
    public Usuario getUsuario_final() {
        return usuario_final;
    }

    public void setUsuario_final(Usuario usuario_final) {
        this.usuario_final = usuario_final;
    }
    
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public boolean getLlamo_al_postulante() {
        return llamo_al_postulante;
    }

    public void setLlamo_al_postulante(boolean llamo_al_postulante) {
        this.llamo_al_postulante = llamo_al_postulante;
    }
    
    public java.time.LocalDate getFecha_visita() {
        return fecha_visita;
    }

    public void setFecha_visita(java.time.LocalDate fecha_visita) {
        this.fecha_visita = fecha_visita;
    }
    
}