package com.friopesca.service;

import com.friopesca.model.*;
import com.friopesca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class InventarioService {

    private final LoteRepository lotes;
    private final MovimientoRepository movimientos;
    private final LecturaTemperaturaRepository lecturas;

    public InventarioService(LoteRepository lotes, MovimientoRepository movimientos, LecturaTemperaturaRepository lecturas) {
        this.lotes = lotes;
        this.movimientos = movimientos;
        this.lecturas = lecturas;
    }

    @Transactional
    public Lote crearLote(Lote lote) {
        if (lote.getKilosIniciales() == null || lote.getKilosIniciales().signum() <= 0) {
            throw new IllegalArgumentException("Cantidad inicial debe ser positiva");
        }
        if (lote.getFechaVencimiento().isBefore(lote.getFechaIngreso())) {
            throw new IllegalArgumentException("Vencimiento anterior al ingreso");
        }
        if (lote.getTemperaturaMin().compareTo(lote.getTemperaturaMax()) > 0) {
            throw new IllegalArgumentException("Rango de temperatura inválido");
        }
        if (lotes.existsByCodigo(lote.getCodigo())) {
            throw new IllegalArgumentException("Código de lote duplicado");
        }
        lote.setKilosDisponibles(lote.getKilosIniciales());
        Lote guardado = lotes.save(lote);

        Movimiento m = new Movimiento();
        m.setLote(guardado);
        m.setTipo(Movimiento.Tipo.ENTRADA);
        m.setKilos(lote.getKilosIniciales());
        m.setTotal(BigDecimal.ZERO);
        m.setObservacion("Ingreso inicial");
        movimientos.save(m);
        return guardado;
    }

    @Transactional
    public void salida(Long loteId, BigDecimal kilos, Movimiento.Tipo tipo, String observacion) {
        if (tipo == Movimiento.Tipo.ENTRADA) {
            throw new IllegalArgumentException("Tipo de salida inválido");
        }
        if (kilos == null || kilos.signum() <= 0) {
            throw new IllegalArgumentException("Los kilos deben ser positivos");
        }
        Lote lote = lotes.findById(loteId)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado"));
        if (lote.getKilosDisponibles().compareTo(kilos) < 0) {
            throw new IllegalArgumentException("Inventario insuficiente");
        }
        if (tipo == Movimiento.Tipo.VENTA && lote.getFechaVencimiento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se permite vender un lote vencido");
        }
        lote.setKilosDisponibles(lote.getKilosDisponibles().subtract(kilos));

        Movimiento m = new Movimiento();
        m.setLote(lote);
        m.setTipo(tipo);
        m.setKilos(kilos);
        m.setTotal(tipo == Movimiento.Tipo.VENTA
                ? kilos.multiply(lote.getProducto().getPrecioKg())
                : BigDecimal.ZERO);
        m.setObservacion(observacion);
        movimientos.save(m);
    }

    @Transactional
    public LecturaTemperatura registrarTemperatura(Long loteId, BigDecimal temperatura, String responsable, String observacion) {
        if (temperatura == null
                || temperatura.compareTo(new BigDecimal("-100")) < 0
                || temperatura.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Temperatura fuera de límites de registro");
        }
        Lote lote = lotes.findById(loteId)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado"));
        LecturaTemperatura t = new LecturaTemperatura();
        t.setLote(lote);
        t.setTemperatura(temperatura);
        t.setResponsable(responsable);
        t.setObservacion(observacion);
        t.setAlerta(temperatura.compareTo(lote.getTemperaturaMin()) < 0
                || temperatura.compareTo(lote.getTemperaturaMax()) > 0);
        return lecturas.save(t);
    }
}
