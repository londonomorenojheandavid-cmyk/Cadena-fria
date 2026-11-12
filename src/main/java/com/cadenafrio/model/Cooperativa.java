package com.cadenafrio.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cooperativa")
public class Cooperativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cooperativa")
    private Integer idCooperativa;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String municipio;

    @Column(length = 20)
    private String telefono;

    @OneToMany(mappedBy = "cooperativa")
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "cooperativa")
    private List<Cava> cavas = new ArrayList<>();

    public Cooperativa() {
    }

    public Integer getIdCooperativa() {
        return idCooperativa;
    }

    public void setIdCooperativa(Integer idCooperativa) {
        this.idCooperativa = idCooperativa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Cava> getCavas() {
        return cavas;
    }

    public void setCavas(List<Cava> cavas) {
        this.cavas = cavas;
    }
}
