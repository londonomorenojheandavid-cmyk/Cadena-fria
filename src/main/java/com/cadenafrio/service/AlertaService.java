package com.cadenafrio.service;

import com.cadenafrio.dao.AlertaDAO;
import com.cadenafrio.dao.LecturaTemperaturaDAO;
import com.cadenafrio.model.Alerta;
import com.cadenafrio.model.Cava;
import com.cadenafrio.model.LecturaTemperatura;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AlertaService {

    private final LecturaTemperaturaDAO lecturaDAO;
    private final AlertaDAO alertaDAO;

    public AlertaService() {
        this(new LecturaTemperaturaDAO(), new AlertaDAO());
    }

    public AlertaService(LecturaTemperaturaDAO lecturaDAO, AlertaDAO alertaDAO) {
        this.lecturaDAO = lecturaDAO;
        this.alertaDAO = alertaDAO;
    }

    public Alerta registrarLectura(Cava cava, BigDecimal temperatura) {
        LecturaTemperatura lectura = new LecturaTemperatura();
        lectura.setCava(cava);
        lectura.setTemperatura(temperatura);
        lectura.setFechaHora(LocalDateTime.now());
        lecturaDAO.crear(lectura);

        BigDecimal umbral = cava.getTempCritica() != null ? cava.getTempCritica() : new BigDecimal("-18.00");
        if (temperatura.compareTo(umbral) > 0) {
            Alerta alerta = new Alerta();
            alerta.setCava(cava);
            alerta.setLecturaTemperatura(lectura);
            alerta.setTipo("TEMPERATURA_CRITICA");
            alerta.setMensaje("Temperatura de " + temperatura + " °C supera el umbral crítico de " + umbral + " °C");
            alerta.setFechaHora(LocalDateTime.now());
            alerta.setAtendida(false);
            alertaDAO.crear(alerta);
            return alerta;
        }
        return null;
    }

    public void marcarAtendida(Integer idAlerta) {
        Alerta alerta = alertaDAO.buscarPorId(idAlerta);
        if (alerta != null) {
            alerta.setAtendida(true);
            alertaDAO.actualizar(alerta);
        }
    }

    public List<Alerta> listarAlertas() {
        return alertaDAO.listarTodos();
    }
}
