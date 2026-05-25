package com.example.strategy;

import java.util.List;

import com.example.model.Movimiento;
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

    Movimiento crearMovimiento(
            Pieza pieza,
            Posicion origen,
            Posicion destino,
            Tablero tablero
    );

    List<Movimiento> obtenerMovimientosLegales(
            Pieza pieza,
            Posicion origen,
            Tablero tablero
    );

    Ruta calcularMejorRuta(
            Pieza pieza,
            Tablero tablero
    );

    void inicializarTablero(Tablero tablero);
}
