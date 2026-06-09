package com.example.model;

import java.util.ArrayList;
import java.util.List;

//Clase para representar un movimiento
public class Movimiento {

    private final Posicion origen;
    private final Posicion destino;
    private final List<Posicion> capturas;

    //Constructor para crear un movimiento
    public Movimiento(Posicion origen, Posicion destino) {
        this.origen = origen;
        this.destino = destino;
        this.capturas = new ArrayList<>();
    }

    //Método para obtener la posicion de origen
    public Posicion getOrigen() {
        return origen;
    }

    //Método para obtener la posicion de destino
    public Posicion getDestino() {
        return destino;
    }

    //Método para obtener las capturas
    public List<Posicion> getCapturas() {
        return new ArrayList<>(capturas); 
    }

    //Método para agregar una captura
    public void agregarCaptura(Posicion posicion) {
        capturas.add(posicion);
    }

    //Método para saber si el movimiento captura
    public boolean esCaptura() {
        return !capturas.isEmpty();
    }

    //Método para contar las capturas
    public int cantidadCapturas() {
        return capturas.size();
    }

}
