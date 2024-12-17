
package com.example.webapp_petlink.beans;

import java.util.Date;

public class InscripcionEventoBenefico {
    
    private int id_inscripcion_evento_benefico;
    
    private Date fecha_hora_registro;
    
    private PublicacionEventoBenefico evento_benefico;
    
    private Usuario usuario_final;
    

    // Getters and Setters
    
    public int getId_inscripcion_evento_benefico() {
        return id_inscripcion_evento_benefico;
    }

    public void setId_inscripcion_evento_benefico(int id_inscripcion_evento_benefico) {
        this.id_inscripcion_evento_benefico = id_inscripcion_evento_benefico;
    }
    

    
    public PublicacionEventoBenefico getEvento_benefico() {
        return evento_benefico;
    }

    public void setEvento_benefico(PublicacionEventoBenefico evento_benefico) {
        this.evento_benefico = evento_benefico;
    }
    
    public Usuario getUsuario_final() {
        return usuario_final;
    }

    public void setUsuario_final(Usuario usuario_final) {
        this.usuario_final = usuario_final;
    }

    public Date getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(Date fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }
}
