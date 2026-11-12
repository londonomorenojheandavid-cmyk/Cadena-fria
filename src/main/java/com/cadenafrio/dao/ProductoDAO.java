package com.cadenafrio.dao;

import com.cadenafrio.model.Producto;

public class ProductoDAO extends GenericDAOImpl<Producto, Integer> {
    public ProductoDAO() {
        super(Producto.class);
    }
}
