package com.friopesca;

import com.friopesca.model.LecturaTemperatura;
import com.friopesca.model.Lote;
import com.friopesca.model.Movimiento;
import com.friopesca.model.Producto;
import com.friopesca.repository.LecturaTemperaturaRepository;
import com.friopesca.repository.LoteRepository;
import com.friopesca.repository.MovimientoRepository;
import com.friopesca.repository.ProductoRepository;
import com.friopesca.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.admin.username=admin",
        "app.admin.password=admin"
})
@Transactional
class InventarioServiceTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private LecturaTemperaturaRepository lecturaRepository;

    @Autowired
    private InventarioService inventarioService;

    @Test
    void flujoCompletoDeLoteTemperaturasYMovimientos() {
        Producto bocachico = new Producto();
        bocachico.setNombre("Bocachico");
        bocachico.setEspecie("Prochilodus magdalenae");
        bocachico.setPrecioKg(new BigDecimal("15000"));
        bocachico = productoRepository.save(bocachico);

        Lote lote = new Lote();
        lote.setProducto(bocachico);
        lote.setCodigo("LOT-001");
        lote.setKilosIniciales(new BigDecimal("100.00"));
        lote.setFechaIngreso(LocalDate.now());
        lote.setFechaVencimiento(LocalDate.now().plusDays(4));
        lote.setUbicacion("Nevera 1");
        lote.setTemperaturaMin(new BigDecimal("0.0"));
        lote.setTemperaturaMax(new BigDecimal("4.0"));

        Lote guardado = inventarioService.crearLote(lote);
        assertEquals(new BigDecimal("100.00"), guardado.getKilosDisponibles());
        assertEquals(1, movimientoRepository.count());

        // Lectura dentro del rango (2.0 °C)
        LecturaTemperatura lecturaNormal = inventarioService.registrarTemperatura(
                guardado.getId(), new BigDecimal("2.0"), "Operador A", "Lectura de control");
        assertFalse(lecturaNormal.isAlerta());

        // Lectura fuera del rango (8.0 °C)
        LecturaTemperatura lecturaAlerta = inventarioService.registrarTemperatura(
                guardado.getId(), new BigDecimal("8.0"), "Operador A", "Revisar refrigerador");
        assertTrue(lecturaAlerta.isAlerta());

        // Venta de 20 kg -> quedan 80 kg
        inventarioService.salida(guardado.getId(), new BigDecimal("20.00"), Movimiento.Tipo.VENTA, "Venta local");
        Lote actualizado = loteRepository.findById(guardado.getId()).orElseThrow();
        assertEquals(new BigDecimal("80.00"), actualizado.getKilosDisponibles());

        // Intento de venta superior al stock disponible (90 kg > 80 kg)
        assertThrows(IllegalArgumentException.class, () ->
                inventarioService.salida(guardado.getId(), new BigDecimal("90.00"), Movimiento.Tipo.VENTA, "Exceso")
        );
    }
}
