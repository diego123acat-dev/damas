package com.example.strategy;

import com.example.model.Pieza;
import com.example.model.Posicion;
import com.example.model.Ruta;
import com.example.model.Tablero;

public interface EstrategiaJuego {

    boolean esMovimientoValido(
            Pieza pieza,
            Posicion origen,
            Posicion destino,
            Tablero tablero
    );

    Ruta calcularMejorRuta(
            Pieza pieza,
            Tablero tablero
    );

    void inicializarTablero(Tablero tablero);
}