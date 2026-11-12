package com.cadenafrio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer idAlerta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lectura", nullable = false)
    private LecturaTemperatura lecturaTemperatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cava", nullable = false)
    private Cava cava;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, length = 255)
    private String mensaje;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(nullable = false)
    private boolean atendida = false;

    public Alerta() {
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public LecturaTemperatura getLecturaTemperatura() {
        return lecturaTemperatura;
    }

    public void setLecturaTemperatura(LecturaTemperatura lecturaTemperatura) {
        this.lecturaTemperatura = lecturaTemperatura;
    }

    public Cava getCava() {
        return cava;
    }

    public void setCava(Cava cava) {
        this.cava = cava;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }
}
