package com.cadenafrio.controller;

import com.cadenafrio.service.AlertaService;
import com.cadenafrio.service.InventarioService;
import com.cadenafrio.util.ClimaApiClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/servlet/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final InventarioService inventarioService = new InventarioService();
    private final AlertaService alertaService = new AlertaService();
    private final ClimaApiClient climaApiClient = new ClimaApiClient();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cavas", inventarioService.listarCavas());
        req.setAttribute("lotes", inventarioService.listarLotes());
        req.setAttribute("alertas", alertaService.listarAlertas());
        req.setAttribute("climaJson", climaApiClient.obtenerResumenClimaUraba());
        req.getRequestDispatcher("/vistas/dashboard.jsp").forward(req, resp);
    }
}
