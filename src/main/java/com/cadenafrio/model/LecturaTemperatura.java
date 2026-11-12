package com.cadenafrio.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lectura_temperatura")
public class LecturaTemperatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lectura")
    private Long idLectura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cava", nullable = false)
    private Cava cava;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @OneToMany(mappedBy = "lecturaTemperatura")
    private List<Alerta> alertas = new ArrayList<>();

    public LecturaTemperatura() {
    }

    public Long getIdLectura() {
        return idLectura;
    }

    public void setIdLectura(Long idLectura) {
        this.idLectura = idLectura;
    }

    public Cava getCava() {
        return cava;
    }

    public void setCava(Cava cava) {
        this.cava = cava;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }
}
