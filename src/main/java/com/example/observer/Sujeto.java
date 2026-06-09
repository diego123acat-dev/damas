package com.example.observer;

//Interfaz para los objetos que pueden ser observados
public interface Sujeto {

    //Metodo para agregar un observador
    void agregarObservador(Observador observador);

    //Metodo para eliminar un observador
    void eliminarObservador(Observador observador);

    //Metodo para notificar a los observadores
    void notificarObservadores();
}
