package com.friopesca.service;

/**
 * Abstracción para consulta de clima exterior.
 * Aplica el Principio de Inversión de Dependencias (DIP - SOLID):
 * Los controladores y servicios de alto nivel dependen de esta interfaz
 * y no de detalles concretos del proveedor HTTP externo.
 */
public interface ProveedorClima {
    String consultar();
}
