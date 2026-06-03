package com.example.dao;

import java.util.List;

public interface DAO<T> {

    void guardar(T objeto);

    List<T> obtenerTodos();

    void eliminar(T objeto);

    void eliminarTodos();
}
