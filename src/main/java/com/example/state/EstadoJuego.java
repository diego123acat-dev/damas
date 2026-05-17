package com.example.state;

import com.example.model.Juego;
import com.example.model.Movimiento;

public interface EstadoJuego {

    boolean esMovimientoValido(Juego juego, Movimiento movimiento);

    void manejarTurno(Juego juego);

    boolean esJuegoTerminado(Juego juego);

    void manejarTurno();
}