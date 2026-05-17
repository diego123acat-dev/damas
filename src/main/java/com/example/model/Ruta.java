package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Ruta {

    private List<Posicion> movimientos;
    private int capturas;

    public Ruta() {
        this.movimientos = new ArrayList<>();
        this.capturas = 0;
    }

    public Ruta(List<Posicion> movimientos, int capturas) {
        this.movimientos = movimientos;
        this.capturas = capturas;
    }

    public List<Posicion> getMovimientos() {
        return new ArrayList<>(movimientos);
    }

    public int getCapturas() {
        return capturas;
    }

    public void agregarMovimiento(Posicion p) {
        movimientos.add(p);
    }

    public void incrementarCapturas() {
        capturas++;
    }

    public boolean esRutaDeCaptura() {
        return capturas > 0;
    }
}