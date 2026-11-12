package com.friopesca.repository;

import com.friopesca.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrueOrderByNombreAsc();
    List<Producto> findByNombreContainingIgnoreCaseOrEspecieContainingIgnoreCase(String nombre, String especie);
}
