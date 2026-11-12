package com.friopesca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Producto producto;

    @NotBlank
    private String codigo;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal kilosIniciales;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal kilosDisponibles;

    @NotNull
    private LocalDate fechaIngreso;

    @NotNull
    private LocalDate fechaVencimiento;

    @NotBlank
    private String ubicacion;

    @NotNull
    private BigDecimal temperaturaMin;

    @NotNull
    private BigDecimal temperaturaMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getKilosIniciales() {
        return kilosIniciales;
    }

    public void setKilosIniciales(BigDecimal kilosIniciales) {
        this.kilosIniciales = kilosIniciales;
    }

    public BigDecimal getKilosDisponibles() {
        return kilosDisponibles;
    }

    public void setKilosDisponibles(BigDecimal kilosDisponibles) {
        this.kilosDisponibles = kilosDisponibles;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public BigDecimal getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(BigDecimal temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public BigDecimal getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(BigDecimal temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }
}
