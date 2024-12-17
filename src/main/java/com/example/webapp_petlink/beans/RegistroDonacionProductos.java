    package com.example.webapp_petlink.beans;

    import java.util.Date;

    public class RegistroDonacionProductos {
        private int idRegistroDonacionProductos;
        private String descripcionesDonaciones;
        private Date fechaHoraRegistro;
        private Usuario usuarioFinal;
        private HorarioRecepcionDonacion horarioRecepcionDonacion;
        private Estado estado;

        // Getters y Setters


        public int getIdRegistroDonacionProductos() {
            return idRegistroDonacionProductos;
        }

        public void setIdRegistroDonacionProductos(int idRegistroDonacionProductos) {
            this.idRegistroDonacionProductos = idRegistroDonacionProductos;
        }

        public String getDescripcionesDonaciones() {
            return descripcionesDonaciones;
        }

        public void setDescripcionesDonaciones(String descripcionesDonaciones) {
            this.descripcionesDonaciones = descripcionesDonaciones;
        }

        public Date getFechaHoraRegistro() {
            return fechaHoraRegistro;
        }

        public void setFechaHoraRegistro(Date fechaHoraRegistro) {
            this.fechaHoraRegistro = fechaHoraRegistro;
        }

        public Usuario getUsuarioFinal() {
            return usuarioFinal;
        }

        public void setUsuarioFinal(Usuario usuarioFinal) {
            this.usuarioFinal = usuarioFinal;
        }

        public HorarioRecepcionDonacion getHorarioRecepcionDonacion() {
            return horarioRecepcionDonacion;
        }

        public void setHorarioRecepcionDonacion(HorarioRecepcionDonacion horarioRecepcionDonacion) {
            this.horarioRecepcionDonacion = horarioRecepcionDonacion;
        }

        public Estado getEstado() {
            return estado;
        }

        public void setEstado(Estado estado) {
            this.estado = estado;
        }


    }
