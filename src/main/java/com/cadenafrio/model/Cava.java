package com.cadenafrio.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cava")
public class Cava {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cava")
    private Integer idCava;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cooperativa", nullable = false)
    private Cooperativa cooperativa;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(name = "capacidad_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidadKg;

    @Column(name = "temp_critica", nullable = false, precision = 5, scale = 2)
    private BigDecimal tempCritica = new BigDecimal("-18.00");

    @OneToMany(mappedBy = "cava")
    private List<LoteInventario> lotes = new ArrayList<>();

    @OneToMany(mappedBy = "cava")
    private List<LecturaTemperatura> lecturas = new ArrayList<>();

    public Cava() {
    }

    public Integer getIdCava() {
        return idCava;
    }

    public void setIdCava(Integer idCava) {
        this.idCava = idCava;
    }

    public Cooperativa getCooperativa() {
        return cooperativa;
    }

    public void setCooperativa(Cooperativa cooperativa) {
        this.cooperativa = cooperativa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(BigDecimal capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public BigDecimal getTempCritica() {
        return tempCritica;
    }

    public void setTempCritica(BigDecimal tempCritica) {
        this.tempCritica = tempCritica;
    }

    public List<LoteInventario> getLotes() {
        return lotes;
    }

    public void setLotes(List<LoteInventario> lotes) {
        this.lotes = lotes;
    }

    public List<LecturaTemperatura> getLecturas() {
        return lecturas;
    }

    public void setLecturas(List<LecturaTemperatura> lecturas) {
        this.lecturas = lecturas;
    }
}
