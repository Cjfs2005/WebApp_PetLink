
    package com.example.webapp_petlink.beans;

    public class PuntoAcopio {

        private int id_punto_acopio;

        private String direccion_punto_acopio;

        private Distrito distrito;

        private Usuario usuario_albergue;


        // Getters and Setters

        public int getId_punto_acopio() {
            return id_punto_acopio;
        }

        public void setId_punto_acopio(int id_punto_acopio) {
            this.id_punto_acopio = id_punto_acopio;
        }

        public String getDireccion_punto_acopio() {
            return direccion_punto_acopio;
        }

        public void setDireccion_punto_acopio(String direccion_punto_acopio) {
            this.direccion_punto_acopio = direccion_punto_acopio;
        }

        public Distrito getDistrito() {
            return distrito;
        }

        public void setDistrito(Distrito distrito) {
            this.distrito = distrito;
        }

        public Usuario getUsuario_albergue() {
            return usuario_albergue;
        }

        public void setUsuario_albergue(Usuario usuario_albergue) {
            this.usuario_albergue = usuario_albergue;
        }

    }