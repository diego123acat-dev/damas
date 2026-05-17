package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private List<Casilla> casillas;

    public Tablero() {
        this.casillas = new ArrayList<>();
        inicializarCasillas();
    }

    private void inicializarCasillas() {
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                casillas.add(new Casilla(new Posicion(fila, col)));
            }
        }
    }

    public Casilla getCasilla(Posicion posicion) {
        return casillas.stream()
                .filter(c -> c.getPosicion().getFila() == posicion.getFila()
                        && c.getPosicion().getColumna() == posicion.getColumna())
                .findFirst()
                .orElse(null);
    }

    public void moverPieza(Posicion origen, Posicion destino) {

        Casilla casillaOrigen = getCasilla(origen);
        Casilla casillaDestino = getCasilla(destino);

        if (casillaOrigen == null || casillaDestino == null) return;

        Pieza pieza = casillaOrigen.getPieza();

        casillaDestino.setPieza(pieza);
        casillaOrigen.vaciar();
    }

    public void eliminarPieza(Posicion posicion) {
        Casilla casilla = getCasilla(posicion);
        if (casilla != null) {
            casilla.vaciar();
        }
    }

    public List<Casilla> getCasillas() {
        return casillas;
    }
}