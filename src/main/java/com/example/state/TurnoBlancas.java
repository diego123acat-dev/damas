package com.example.state;

import com.example.model.COLOR;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;

public class TurnoBlancas implements EstadoJuego {

    @Override
    public boolean esMovimientoValido(Juego juego, Movimiento movimiento) {
        Pieza pieza = juego.getTablero()
                .getCasilla(movimiento.getOrigen())
                .getPieza();

        return pieza != null && pieza.getColor() == COLOR.BLANCA;
    }

    @Override
    public void manejarTurno(Juego juego) {
        juego.cambiarEstado(new TurnoNegras());
    }

    @Override
    public boolean esJuegoTerminado(Juego juego) {
        return false;
    }

    @Override
    public void manejarTurno() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
