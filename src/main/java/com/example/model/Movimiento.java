package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Movimiento {

    private final Posicion origen;
    private final Posicion destino;
    private final List<Posicion> capturas;

    public Movimiento(Posicion origen, Posicion destino) {
        this.origen = origen;
        this.destino = destino;
        this.capturas = new ArrayList<>();
    }

    public Posicion getOrigen() {
        return origen;
    }

    public Posicion getDestino() {
        return destino;
    }

    public List<Posicion> getCapturas() {
        return new ArrayList<>(capturas); 
    }

    public void agregarCaptura(Posicion posicion) {
        capturas.add(posicion);
    }

    public boolean esCaptura() {
        return !capturas.isEmpty();
    }

    public int cantidadCapturas() {
        return capturas.size();
    }

}