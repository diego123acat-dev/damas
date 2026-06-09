package com.example.strategy;

import java.util.List;

import com.example.model.COLOR;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;
import com.example.model.Posicion;
import com.example.model.ResultadoMovimiento;
import com.example.model.Ruta;
import com.example.model.Tablero;

//Interfaz para definir una estrategia de juego
public interface EstrategiaJuego {

    //Metodo para validar un movimiento
    boolean esMovimientoValido(
            Pieza pieza,
            Posicion origen,
            Posicion destino,
            Tablero tablero
    );

    //Metodo para crear un movimiento
    Movimiento crearMovimiento(
            Pieza pieza,
            Posicion origen,
            Posicion destino,
            Tablero tablero
    );

    //Metodo para ejecutar un movimiento en el tablero
    ResultadoMovimiento ejecutarMovimiento(
            Movimiento movimiento,
            Tablero tablero,
            COLOR turnoActual
    );

    //Metodo para obtener los movimientos legales
    List<Movimiento> obtenerMovimientosLegales(
            Pieza pieza,
            Posicion origen,
            Tablero tablero
    );

    //Metodo para obtener los destinos legales
    List<Posicion> obtenerDestinosLegales(
            Pieza pieza,
            Posicion origen,
            Tablero tablero,
            COLOR turnoActual
    );

    //Metodo para calcular la mejor ruta
    Ruta calcularMejorRuta(
            Pieza pieza,
            Tablero tablero
    );

    //Metodo para validar si el juego termino
    void evaluarFinDelJuego(Juego juego);

    //Metodo para inicializar el tablero
    void inicializarTablero(Tablero tablero);
}
