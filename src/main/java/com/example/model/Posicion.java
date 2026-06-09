package com.example.model;

//Clase para representar una posicion del tablero
public class Posicion {

    private final int fila;
    private final int columna;

    //Constructor para crear una posicion
    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    //Método para obtener la fila
    public int getFila() {
        return fila;
    }

    //Método para obtener la columna
    public int getColumna() {
        return columna;
    }

}
