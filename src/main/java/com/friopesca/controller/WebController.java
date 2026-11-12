package com.friopesca.controller;

import com.friopesca.model.*;
import com.friopesca.repository.*;
import com.friopesca.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controlador web principal (Capa de Presentación).
 *
 * Principios SOLID aplicados:
 * 1. SRP (Responsabilidad Única): Solo gestiona peticiones HTTP y vistas Thymeleaf;
 *    delega las reglas de negocio a {@link InventarioService} y el consumo de la API a {@link ProveedorClima}.
 * 2. DIP (Inversión de Dependencias): Recibe abstracciones ({@link ProductoRepository},
 *    {@link LoteRepository}, {@link MovimientoRepository}, {@link LecturaTemperaturaRepository},
 *    {@link ProveedorClima}) inyectadas por constructor.
 */
@Controller
public class WebController {

    private final ProductoRepository productos;
    private final LoteRepository lotes;
    private final MovimientoRepository movimientos;
    private final LecturaTemperaturaRepository lecturas;
    private final InventarioService inventario;
    private final ProveedorClima clima;

    public WebController(
            ProductoRepository productos,
            LoteRepository lotes,
            MovimientoRepository movimientos,
            LecturaTemperaturaRepository lecturas,
            InventarioService inventario,
            ProveedorClima clima) {
        this.productos = productos;
        this.lotes = lotes;
        this.movimientos = movimientos;
        this.lecturas = lecturas;
        this.inventario = inventario;
        this.clima = clima;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String inicio(Model m) {
        m.addAttribute("productos", productos.count());
        m.addAttribute("lotes", lotes.count());
        m.addAttribute("alertas", lecturas.countByAlertaTrue());
        m.addAttribute("movimientos", movimientos.count());
        m.addAttribute("clima", clima.consultar());
        return "inicio";
    }

    @GetMapping("/productos")
    public String productos(@RequestParam(required = false, defaultValue = "") String q, Model m) {
        if (q != null && !q.isBlank()) {
            m.addAttribute("items", productos.findByNombreContainingIgnoreCaseOrEspecieContainingIgnoreCase(q.trim(), q.trim()));
        } else {
            m.addAttribute("items", productos.findAll());
        }
        m.addAttribute("q", q);
        return "productos";
    }

    @PostMapping("/productos")
    public String guardarProducto(@Valid @ModelAttribute Producto producto, BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            flash.addFlashAttribute("error", "Verifica nombre, especie y precio positivo");
            return "redirect:/productos";
        }
        productos.save(producto);
        flash.addFlashAttribute("ok", "Producto guardado");
        return "redirect:/productos";
    }

    @PostMapping("/productos/{id}/desactivar")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        Producto p = productos.findById(id).orElseThrow();
        p.setActivo(false);
        productos.save(p);
        flash.addFlashAttribute("ok", "Producto desactivado; historial conservado");
        return "redirect:/productos";
    }

    @GetMapping("/lotes")
    public String lotes(Model m) {
        m.addAttribute("items", lotes.findByOrderByFechaIngresoDesc());
        m.addAttribute("productos", productos.findByActivoTrueOrderByNombreAsc());
        m.addAttribute("hoy", LocalDate.now());
        return "lotes";
    }

    @PostMapping("/lotes")
    public String guardarLote(
            @RequestParam Long productoId,
            @RequestParam String codigo,
            @RequestParam BigDecimal kilos,
            @RequestParam LocalDate ingreso,
            @RequestParam LocalDate vencimiento,
            @RequestParam String ubicacion,
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            RedirectAttributes flash) {
        try {
            Lote l = new Lote();
            l.setProducto(productos.findById(productoId).orElseThrow());
            if (!l.getProducto().isActivo()) {
                throw new IllegalArgumentException("Producto inactivo");
            }
            l.setCodigo(codigo);
            l.setKilosIniciales(kilos);
            l.setFechaIngreso(ingreso);
            l.setFechaVencimiento(vencimiento);
            l.setUbicacion(ubicacion);
            l.setTemperaturaMin(min);
            l.setTemperaturaMax(max);
            inventario.crearLote(l);
            flash.addFlashAttribute("ok", "Lote registrado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lotes";
    }

    @GetMapping("/movimientos")
    public String movimientos(Model m) {
        m.addAttribute("items", movimientos.findByOrderByFechaDesc());
        m.addAttribute("lotes", lotes.findByOrderByFechaIngresoDesc());
        return "movimientos";
    }

    @PostMapping("/movimientos")
    public String salida(
            @RequestParam Long loteId,
            @RequestParam BigDecimal kilos,
            @RequestParam Movimiento.Tipo tipo,
            @RequestParam(required = false, defaultValue = "") String observacion,
            RedirectAttributes flash) {
        try {
            inventario.salida(loteId, kilos, tipo, observacion);
            flash.addFlashAttribute("ok", "Movimiento registrado");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/movimientos";
    }

    @GetMapping("/temperaturas")
    public String temperaturas(Model m) {
        m.addAttribute("items", lecturas.findByOrderByFechaDesc());
        m.addAttribute("lotes", lotes.findByOrderByFechaIngresoDesc());
        return "temperaturas";
    }

    @PostMapping("/temperaturas")
    public String temperatura(
            @RequestParam Long loteId,
            @RequestParam BigDecimal temperatura,
            @RequestParam String responsable,
            @RequestParam(required = false, defaultValue = "") String observacion,
            RedirectAttributes flash) {
        try {
            LecturaTemperatura t = inventario.registrarTemperatura(loteId, temperatura, responsable, observacion);
            flash.addFlashAttribute(
                    t.isAlerta() ? "error" : "ok",
                    t.isAlerta() ? "ALERTA: temperatura fuera del rango configurado" : "Temperatura registrada dentro del rango"
            );
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/temperaturas";
    }

    @GetMapping("/reportes")
    public String reportes(Model m) {
        m.addAttribute("lotes", lotes.findAll());
        m.addAttribute("movimientos", movimientos.findByOrderByFechaDesc());
        m.addAttribute("lecturas", lecturas.findByOrderByFechaDesc());
        return "reportes";
    }
}
