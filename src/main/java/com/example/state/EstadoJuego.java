package com.example.state;

import com.example.model.COLOR;
import com.example.model.Juego;
import com.example.model.Movimiento;

//Interfaz para representar un estado del juego
public interface EstadoJuego {

    //Metodo para validar un movimiento segun el turno
    boolean esMovimientoValido(Juego juego, Movimiento movimiento);

    //Metodo para cambiar al siguiente turno
    void manejarTurno(Juego juego);

    //Metodo para validar si la partida termino
    boolean esJuegoTerminado(Juego juego);

    //Metodo para obtener el color del turno
    COLOR getColorTurno();
}
