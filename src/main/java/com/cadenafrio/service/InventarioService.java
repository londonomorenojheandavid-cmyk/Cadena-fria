package com.cadenafrio.service;

import com.cadenafrio.dao.CavaDAO;
import com.cadenafrio.dao.LoteInventarioDAO;
import com.cadenafrio.dao.ProductoDAO;
import com.cadenafrio.model.Cava;
import com.cadenafrio.model.EstadoLote;
import com.cadenafrio.model.LoteInventario;
import com.cadenafrio.model.Producto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InventarioService {

    private final CavaDAO cavaDAO;
    private final ProductoDAO productoDAO;
    private final LoteInventarioDAO loteDAO;

    public InventarioService() {
        this(new CavaDAO(), new ProductoDAO(), new LoteInventarioDAO());
    }

    public InventarioService(CavaDAO cavaDAO, ProductoDAO productoDAO, LoteInventarioDAO loteDAO) {
        this.cavaDAO = cavaDAO;
        this.productoDAO = productoDAO;
        this.loteDAO = loteDAO;
    }

    public void registrarLote(LoteInventario lote) {
        if (lote.getCantidadKg() == null || lote.getCantidadKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad en kg debe ser mayor que cero");
        }
        Cava cava = lote.getCava();
        if (cava != null && cava.getCapacidadKg() != null
                && lote.getCantidadKg().compareTo(cava.getCapacidadKg()) > 0) {
            throw new IllegalArgumentException("La cantidad supera la capacidad máxima de la cava");
        }
        if (lote.getFechaIngreso() == null) {
            lote.setFechaIngreso(LocalDateTime.now());
        }
        if (lote.getFechaVencimiento() != null
                && !lote.getFechaVencimiento().isAfter(lote.getFechaIngreso().toLocalDate())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la fecha de ingreso");
        }
        loteDAO.crear(lote);
    }

    public List<LoteInventario> listarLotes() {
        return loteDAO.listarTodos();
    }

    public List<LoteInventario> filtrarLotes(Integer idCava, Integer idProducto, EstadoLote estado) {
        return loteDAO.listarTodos().stream()
                .filter(l -> idCava == null || (l.getCava() != null && idCava.equals(l.getCava().getIdCava())))
                .filter(l -> idProducto == null || (l.getProducto() != null && idProducto.equals(l.getProducto().getIdProducto())))
                .filter(l -> estado == null || estado == l.getEstado())
                .collect(Collectors.toList());
    }

    public List<Cava> listarCavas() {
        return cavaDAO.listarTodos();
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }
}
