package com.example.strategy;

import com.example.model.Ruta;
import com.example.model.Tablero;

public interface EstrategiaJuego {
    boolean esMovimientoValido(int xOrigen, int yOrigen, int xDestino, int yDestino);
    Ruta calcularMejorRuta();
    void inicializarTablero(Tablero tablero);
}
