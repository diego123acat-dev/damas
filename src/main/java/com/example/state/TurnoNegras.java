package com.example.state;

import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;

public class TurnoNegras implements EstadoJuego {

    @Override
    public boolean esMovimientoValido(Juego juego, Movimiento movimiento) {
        Casilla casilla = juego.getTablero().getCasilla(movimiento.getOrigen());
        if (casilla == null) return false;

        Pieza pieza = casilla.getPieza();
        return pieza != null && pieza.getColor() == COLOR.NEGRA;
    }

    @Override
    public void manejarTurno(Juego juego) {
        juego.cambiarEstado(new TurnoBlancas());
    }

    @Override
    public boolean esJuegoTerminado(Juego juego) {
        return false;
    }
}
