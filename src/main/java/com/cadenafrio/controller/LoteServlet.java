package com.cadenafrio.controller;

import com.cadenafrio.service.InventarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LoteServlet", urlPatterns = {"/servlet/lotes"})
public class LoteServlet extends HttpServlet {

    private final InventarioService inventarioService = new InventarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cavas", inventarioService.listarCavas());
        req.setAttribute("productos", inventarioService.listarProductos());
        req.setAttribute("lotes", inventarioService.listarLotes());
        req.getRequestDispatcher("/vistas/lote_form.jsp").forward(req, resp);
    }
}
