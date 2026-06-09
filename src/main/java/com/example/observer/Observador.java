package com.example.observer;

import com.example.model.Juego;

//Interfaz para los objetos que observan cambios
public interface Observador {

    //Metodo para actualizar el observador
    void actualizar(Juego juego);
}
