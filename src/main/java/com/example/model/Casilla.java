package com.example.model;

public class Casilla {
    private Posicion posicion;
    private Dama dama;
    
    public Casilla(Posicion posicion) {
        this.posicion = posicion;
        this.dama = null; // Inicialmente, la casilla está vacía
    }

    public boolean isOcupada() {
        return dama != null;
    }

    public Dama getDama(){
        return dama;
    }
}
