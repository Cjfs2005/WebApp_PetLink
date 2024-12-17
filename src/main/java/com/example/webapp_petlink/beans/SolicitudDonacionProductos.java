    package com.example.webapp_petlink.beans;

    import java.util.Date;

    public class SolicitudDonacionProductos {
        private int idSolicitudDonacionProductos;
        private String descripcionDonaciones;
        private boolean esSolicitudActiva;
        private Date fechaHoraRegistro;
        private Usuario usuarioAlbergue;
        private Estado estado;
        private HorarioRecepcionDonacion horarioRecepcion; // Nuevo atributo

        // Getters y Setters
        public int getIdSolicitudDonacionProductos() {
            return idSolicitudDonacionProductos;
        }

        public void setIdSolicitudDonacionProductos(int idSolicitudDonacionProductos) {
            this.idSolicitudDonacionProductos = idSolicitudDonacionProductos;
        }

        public String getDescripcionDonaciones() {
            return descripcionDonaciones;
        }

        public void setDescripcionDonaciones(String descripcionDonaciones) {
            this.descripcionDonaciones = descripcionDonaciones;
        }

        public boolean isEsSolicitudActiva() {
            return esSolicitudActiva;
        }

        public void setEsSolicitudActiva(boolean esSolicitudActiva) {
            this.esSolicitudActiva = esSolicitudActiva;
        }

        public Date getFechaHoraRegistro() {
            return fechaHoraRegistro;
        }

        public void setFechaHoraRegistro(Date fechaHoraRegistro) {
            this.fechaHoraRegistro = fechaHoraRegistro;
        }

        public Usuario getUsuarioAlbergue() {
            return usuarioAlbergue;
        }

        public void setUsuarioAlbergue(Usuario usuarioAlbergue) {
            this.usuarioAlbergue = usuarioAlbergue;
        }

        public Estado getEstado() {
            return estado;
        }

        public void setEstado(Estado estado) {
            this.estado = estado;
        }

        public HorarioRecepcionDonacion getHorarioRecepcion() {
            return horarioRecepcion;
        }

        public void setHorarioRecepcion(HorarioRecepcionDonacion horarioRecepcion) {
            this.horarioRecepcion = horarioRecepcion;
        }
    }
