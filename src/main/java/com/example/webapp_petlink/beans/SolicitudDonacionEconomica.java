
    package com.example.webapp_petlink.beans;

    public class SolicitudDonacionEconomica {

        private int id_solicitud_donacion_economica;

        private int monto_solicitado;

        private String motivo;

        private boolean es_solicitud_activa;

        private java.time.LocalDateTime fecha_hora_registro;

        private Usuario usuario_albergue;

        private Estado estado;


        // Getters and Setters

        public int getId_solicitud_donacion_economica() {
            return id_solicitud_donacion_economica;
        }

        public void setId_solicitud_donacion_economica(int id_solicitud_donacion_economica) {
            this.id_solicitud_donacion_economica = id_solicitud_donacion_economica;
        }

        public int getMonto_solicitado() {
            return monto_solicitado;
        }

        public void setMonto_solicitado(int monto_solicitado) {
            this.monto_solicitado = monto_solicitado;
        }

        public String getMotivo() {
            return motivo;
        }

        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }

        public boolean getEs_solicitud_activa() {
            return es_solicitud_activa;
        }

        public void setEs_solicitud_activa(boolean es_solicitud_activa) {
            this.es_solicitud_activa = es_solicitud_activa;
        }

        public java.time.LocalDateTime getFecha_hora_registro() {
            return fecha_hora_registro;
        }

        public void setFecha_hora_registro(java.time.LocalDateTime fecha_hora_registro) {
            this.fecha_hora_registro = fecha_hora_registro;
        }

        public Usuario getUsuario_albergue() {
            return usuario_albergue;
        }

        public void setUsuario_albergue(Usuario usuario_albergue) {
            this.usuario_albergue = usuario_albergue;
        }

        public Estado getEstado() {
            return estado;
        }

        public void setEstado(Estado estado) {
            this.estado = estado;
        }

    }