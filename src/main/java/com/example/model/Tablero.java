package com.example.model;

import java.util.ArrayList;
import java.util.List;

//Clase para representar el tablero
public class Tablero {

    private List<Casilla> casillas;

    //Constructor para crear el tablero
    public Tablero() {
        this.casillas = new ArrayList<>();
        inicializarCasillas();
    }

    //Método para inicializar las casillas
    private void inicializarCasillas() {
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                casillas.add(new Casilla(new Posicion(fila, col)));
            }
        }
    }

    //Método para obtener una casilla por posicion
    public Casilla getCasilla(Posicion posicion) {
        return casillas.stream()
                .filter(c -> c.getPosicion().getFila() == posicion.getFila()
                        && c.getPosicion().getColumna() == posicion.getColumna())
                .findFirst()
                .orElse(null);
    }

    //Método para mover una pieza
    public void moverPieza(Posicion origen, Posicion destino) {

        Casilla casillaOrigen = getCasilla(origen);
        Casilla casillaDestino = getCasilla(destino);

        if (casillaOrigen == null || casillaDestino == null) return;

        Pieza pieza = casillaOrigen.getPieza();

        casillaDestino.setPieza(pieza);
        casillaOrigen.vaciar();
    }

    //Método para eliminar una pieza
    public void eliminarPieza(Posicion posicion) {
        Casilla casilla = getCasilla(posicion);
        if (casilla != null) {
            casilla.vaciar();
        }
    }

    //Método para obtener todas las casillas
    public List<Casilla> getCasillas() {
        return casillas;
    }
}
