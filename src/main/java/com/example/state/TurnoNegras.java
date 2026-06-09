package com.example.state;

import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;

//Clase para representar el turno de las negras
public class TurnoNegras implements EstadoJuego {

    //Metodo para validar si mueve una pieza negra
    @Override
    public boolean esMovimientoValido(Juego juego, Movimiento movimiento) {
        Casilla casilla = juego.getTablero().getCasilla(movimiento.getOrigen());
        if (casilla == null) return false;

        Pieza pieza = casilla.getPieza();
        return pieza != null && pieza.getColor() == COLOR.NEGRA;
    }

    //Metodo para cambiar al turno de blancas
    @Override
    public void manejarTurno(Juego juego) {
        juego.cambiarEstado(new TurnoBlancas());
    }

    //Metodo para validar si el juego termino
    @Override
    public boolean esJuegoTerminado(Juego juego) {
        if (!juego.esFinDelJuego()) {
            juego.getEstrategia().evaluarFinDelJuego(juego);
        }
        return juego.esFinDelJuego();
    }

    //Metodo para obtener el color del turno
    @Override
    public COLOR getColorTurno() {
        return COLOR.NEGRA;
    }
}
