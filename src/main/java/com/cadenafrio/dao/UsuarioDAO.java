package com.cadenafrio.dao;

import com.cadenafrio.model.Usuario;

public class UsuarioDAO extends GenericDAOImpl<Usuario, Integer> {
    public UsuarioDAO() {
        super(Usuario.class);
    }
}
