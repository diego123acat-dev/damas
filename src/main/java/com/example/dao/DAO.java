package com.example.dao;

import java.util.List;

//Interfaz para definir operaciones basicas de datos
public interface DAO<T> {

    //Método para guardar un objeto
    void guardar(T objeto);

    //Método para obtener todos los objetos
    List<T> obtenerTodos();

    //Método para eliminar un objeto
    void eliminar(T objeto);

    //Método para eliminar todos los objetos
    void eliminarTodos();
}
