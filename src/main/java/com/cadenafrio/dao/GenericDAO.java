package com.cadenafrio.dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    void crear(T entidad);
    T buscarPorId(ID id);
    List<T> listarTodos();
    T actualizar(T entidad);
    void eliminar(ID id);
}
