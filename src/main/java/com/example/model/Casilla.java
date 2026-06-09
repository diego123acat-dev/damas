package com.example.model;

//Clase para representar una casilla del tablero
public class Casilla {

    private final Posicion posicion;
    private Pieza pieza;

    //Constructor para crear una casilla
    public Casilla(Posicion posicion) {
        this.posicion = posicion;
    }

    //Método para saber si la casilla esta ocupada
    public boolean isOcupada() {
        return pieza != null;
    }

    //Método para obtener la pieza
    public Pieza getPieza() {
        return pieza;
    }

    //Método para colocar una pieza
    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    //Método para vaciar la casilla
    public void vaciar() {
        this.pieza = null;
    }

    //Método para obtener la posicion
    public Posicion getPosicion() {
        return posicion;
    }
}
