package com.cadenafrio.dao;

import com.cadenafrio.model.LoteInventario;

public class LoteInventarioDAO extends GenericDAOImpl<LoteInventario, Integer> {
    public LoteInventarioDAO() {
        super(LoteInventario.class);
    }
}
