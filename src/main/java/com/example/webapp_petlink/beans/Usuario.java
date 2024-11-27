
package com.example.webapp_petlink.beans;

public class Usuario {
    
    private int id_usuario;
    
    private String dni;
    
    private String nombres_usuario_final;
    
    private String apellidos_usuario_final;
    
    private String direccion;
    
    private Rol rol;
    
    private String correo_electronico;
    
    private byte[] foto_perfil;
    
    private String nombre_foto_perfil;
    
    private String contrasenia;

    private String contrasenia_hashed;
    
    private boolean es_contrasenia_temporal;
    
    private java.time.LocalDateTime fecha_hora_expiracion_contrasenia;
    
    private boolean es_primera_contrasenia_temporal;
    
    private boolean es_usuario_activo;
    
    private java.time.LocalDateTime fecha_hora_creacion;
    
    private java.time.LocalDateTime fecha_hora_eliminacion;
    
    private String nombre_albergue;
    
    private String nombres_encargado;
    
    private String apellidos_encargado;
    
    private String anio_creacion;
    
    private int cantidad_animales;
    
    private int espacio_disponible;
    
    private String url_instagram;
    
    private byte[] foto_de_portada_albergue;
    
    private String nombre_foto_de_portada;
    
    private byte[] logo_albergue;
    
    private String nombre_logo_albergue;
    
    private String direccion_donaciones;
    
    private String nombre_contacto_donaciones;
    
    private String numero_contacto_donaciones;
    
    private String numero_yape_plin;
    
    private String nombre_imagen_qr;
    
    private byte[] imagen_qr;
    
    private boolean tiene_registro_completo;
    
    private String nombres_coordinador;
    
    private String apellidos_coordinador;
    
    private java.time.LocalDate fecha_nacimiento;
    
    private Distrito distrito;
    
    private Zona zona;
    
    private PostulacionHogarTemporal ultima_postulacion_hogar_temporal;
    
    private String descripcion_perfil;
    

    // Getters and Setters
    
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNombres_usuario_final() {
        return nombres_usuario_final;
    }

    public void setNombres_usuario_final(String nombres_usuario_final) {
        this.nombres_usuario_final = nombres_usuario_final;
    }
    
    public String getApellidos_usuario_final() {
        return apellidos_usuario_final;
    }

    public void setApellidos_usuario_final(String apellidos_usuario_final) {
        this.apellidos_usuario_final = apellidos_usuario_final;
    }
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }
    
    public byte[] getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(byte[] foto_perfil) {
        this.foto_perfil = foto_perfil;
    }
    
    public String getNombre_foto_perfil() {
        return nombre_foto_perfil;
    }

    public void setNombre_foto_perfil(String nombre_foto_perfil) {
        this.nombre_foto_perfil = nombre_foto_perfil;
    }
    
    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    public boolean getEs_contrasenia_temporal() {
        return es_contrasenia_temporal;
    }

    public void setEs_contrasenia_temporal(boolean es_contrasenia_temporal) {
        this.es_contrasenia_temporal = es_contrasenia_temporal;
    }
    
    public java.time.LocalDateTime getFecha_hora_expiracion_contrasenia() {
        return fecha_hora_expiracion_contrasenia;
    }

    public void setFecha_hora_expiracion_contrasenia(java.time.LocalDateTime fecha_hora_expiracion_contrasenia) {
        this.fecha_hora_expiracion_contrasenia = fecha_hora_expiracion_contrasenia;
    }
    
    public boolean getEs_primera_contrasenia_temporal() {
        return es_primera_contrasenia_temporal;
    }

    public void setEs_primera_contrasenia_temporal(boolean es_primera_contrasenia_temporal) {
        this.es_primera_contrasenia_temporal = es_primera_contrasenia_temporal;
    }
    
    public boolean getEs_usuario_activo() {
        return es_usuario_activo;
    }

    public void setEs_usuario_activo(boolean es_usuario_activo) {
        this.es_usuario_activo = es_usuario_activo;
    }
    
    public java.time.LocalDateTime getFecha_hora_creacion() {
        return fecha_hora_creacion;
    }

    public void setFecha_hora_creacion(java.time.LocalDateTime fecha_hora_creacion) {
        this.fecha_hora_creacion = fecha_hora_creacion;
    }
    
    public java.time.LocalDateTime getFecha_hora_eliminacion() {
        return fecha_hora_eliminacion;
    }

    public void setFecha_hora_eliminacion(java.time.LocalDateTime fecha_hora_eliminacion) {
        this.fecha_hora_eliminacion = fecha_hora_eliminacion;
    }
    
    public String getNombre_albergue() {
        return nombre_albergue;
    }

    public void setNombre_albergue(String nombre_albergue) {
        this.nombre_albergue = nombre_albergue;
    }
    
    public String getNombres_encargado() {
        return nombres_encargado;
    }

    public void setNombres_encargado(String nombres_encargado) {
        this.nombres_encargado = nombres_encargado;
    }
    
    public String getApellidos_encargado() {
        return apellidos_encargado;
    }

    public void setApellidos_encargado(String apellidos_encargado) {
        this.apellidos_encargado = apellidos_encargado;
    }
    
    public String getAnio_creacion() {
        return anio_creacion;
    }

    public void setAnio_creacion(String anio_creacion) {
        this.anio_creacion = anio_creacion;
    }
    
    public int getCantidad_animales() {
        return cantidad_animales;
    }

    public void setCantidad_animales(int cantidad_animales) {
        this.cantidad_animales = cantidad_animales;
    }
    
    public int getEspacio_disponible() {
        return espacio_disponible;
    }

    public void setEspacio_disponible(int espacio_disponible) {
        this.espacio_disponible = espacio_disponible;
    }
    
    public String getUrl_instagram() {
        return url_instagram;
    }

    public void setUrl_instagram(String url_instagram) {
        this.url_instagram = url_instagram;
    }
    
    public byte[] getFoto_de_portada_albergue() {
        return foto_de_portada_albergue;
    }

    public void setFoto_de_portada_albergue(byte[] foto_de_portada_albergue) {
        this.foto_de_portada_albergue = foto_de_portada_albergue;
    }
    
    public String getNombre_foto_de_portada() {
        return nombre_foto_de_portada;
    }

    public void setNombre_foto_de_portada(String nombre_foto_de_portada) {
        this.nombre_foto_de_portada = nombre_foto_de_portada;
    }
    
    public byte[] getLogo_albergue() {
        return logo_albergue;
    }

    public void setLogo_albergue(byte[] logo_albergue) {
        this.logo_albergue = logo_albergue;
    }
    
    public String getNombre_logo_albergue() {
        return nombre_logo_albergue;
    }

    public void setNombre_logo_albergue(String nombre_logo_albergue) {
        this.nombre_logo_albergue = nombre_logo_albergue;
    }
    
    public String getDireccion_donaciones() {
        return direccion_donaciones;
    }

    public void setDireccion_donaciones(String direccion_donaciones) {
        this.direccion_donaciones = direccion_donaciones;
    }
    
    public String getNombre_contacto_donaciones() {
        return nombre_contacto_donaciones;
    }

    public void setNombre_contacto_donaciones(String nombre_contacto_donaciones) {
        this.nombre_contacto_donaciones = nombre_contacto_donaciones;
    }
    
    public String getNumero_contacto_donaciones() {
        return numero_contacto_donaciones;
    }

    public void setNumero_contacto_donaciones(String numero_contacto_donaciones) {
        this.numero_contacto_donaciones = numero_contacto_donaciones;
    }
    
    public String getNumero_yape_plin() {
        return numero_yape_plin;
    }

    public void setNumero_yape_plin(String numero_yape_plin) {
        this.numero_yape_plin = numero_yape_plin;
    }
    
    public String getNombre_imagen_qr() {
        return nombre_imagen_qr;
    }

    public void setNombre_imagen_qr(String nombre_imagen_qr) {
        this.nombre_imagen_qr = nombre_imagen_qr;
    }
    
    public byte[] getImagen_qr() {
        return imagen_qr;
    }

    public void setImagen_qr(byte[] imagen_qr) {
        this.imagen_qr = imagen_qr;
    }
    
    public boolean getTiene_registro_completo() {
        return tiene_registro_completo;
    }

    public void setTiene_registro_completo(boolean tiene_registro_completo) {
        this.tiene_registro_completo = tiene_registro_completo;
    }
    
    public String getNombres_coordinador() {
        return nombres_coordinador;
    }

    public void setNombres_coordinador(String nombres_coordinador) {
        this.nombres_coordinador = nombres_coordinador;
    }
    
    public String getApellidos_coordinador() {
        return apellidos_coordinador;
    }

    public void setApellidos_coordinador(String apellidos_coordinador) {
        this.apellidos_coordinador = apellidos_coordinador;
    }
    
    public java.time.LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(java.time.LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
    
    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }
    
    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }
    
    public PostulacionHogarTemporal getUltima_postulacion_hogar_temporal() {
        return ultima_postulacion_hogar_temporal;
    }

    public void setUltima_postulacion_hogar_temporal(PostulacionHogarTemporal ultima_postulacion_hogar_temporal) {
        this.ultima_postulacion_hogar_temporal = ultima_postulacion_hogar_temporal;
    }
    
    public String getDescripcion_perfil() {
        return descripcion_perfil;
    }

    public void setDescripcion_perfil(String descripcion_perfil) {
        this.descripcion_perfil = descripcion_perfil;
    }

    public String getContrasenia_hashed() {
        return contrasenia_hashed;
    }

    public void setContrasenia_hashed(String contrasenia_hashed) {
        this.contrasenia_hashed = contrasenia_hashed;
    }

}