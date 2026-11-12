package com.friopesca.repository;

import com.friopesca.model.LecturaTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LecturaTemperaturaRepository extends JpaRepository<LecturaTemperatura, Long> {
    List<LecturaTemperatura> findByOrderByFechaDesc();
    long countByAlertaTrue();
}
