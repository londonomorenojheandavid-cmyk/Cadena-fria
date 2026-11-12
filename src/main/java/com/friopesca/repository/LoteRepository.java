package com.friopesca.repository;

import com.friopesca.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByOrderByFechaIngresoDesc();
    boolean existsByCodigo(String codigo);
}
