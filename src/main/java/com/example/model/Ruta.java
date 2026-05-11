package com.example.model;

import java.util.List;

import com.example.strategy.EstrategiaJuego;

public class Ruta implements EstrategiaJuego {
    private List<Posicion> movimientos;
    private int capturas;

    public Ruta(List<Posicion> movimientos, int capturas) {
        this.movimientos = movimientos;
        this.capturas = capturas;
    }

    @Override
    public boolean esMovimientoValido(int xOrigen, int yOrigen, int xDestino, int yDestino) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Ruta calcularMejorRuta() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void inicializarTablero(Tablero tablero) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
