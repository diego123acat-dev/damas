package com.example.model;

public class Casilla {

    private final Posicion posicion;
    private Pieza pieza;

    public Casilla(Posicion posicion) {
        this.posicion = posicion;
    }

    public boolean isOcupada() {
        return pieza != null;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public void vaciar() {
        this.pieza = null;
    }

    public Posicion getPosicion() {
        return posicion;
    }
}
