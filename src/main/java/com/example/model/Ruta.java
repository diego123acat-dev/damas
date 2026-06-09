package com.example.model;

import java.util.ArrayList;
import java.util.List;

//Clase para representar una ruta de movimientos
public class Ruta {

    private List<Posicion> movimientos;
    private int capturas;

    //Constructor para crear una ruta vacia
    public Ruta() {
        this.movimientos = new ArrayList<>();
        this.capturas = 0;
    }

    //Constructor para crear una ruta con movimientos
    public Ruta(List<Posicion> movimientos, int capturas) {
        this.movimientos = movimientos;
        this.capturas = capturas;
    }

    //Método para obtener los movimientos
    public List<Posicion> getMovimientos() {
        return new ArrayList<>(movimientos);
    }

    //Método para obtener las capturas
    public int getCapturas() {
        return capturas;
    }

    //Método para agregar un movimiento
    public void agregarMovimiento(Posicion p) {
        movimientos.add(p);
    }

    //Método para incrementar las capturas
    public void incrementarCapturas() {
        capturas++;
    }

    //Método para saber si la ruta tiene capturas
    public boolean esRutaDeCaptura() {
        return capturas > 0;
    }
}
