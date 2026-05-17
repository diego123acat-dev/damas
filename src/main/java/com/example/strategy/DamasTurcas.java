package com.example.strategy;

import java.util.ArrayList;
import java.util.List;

import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Pieza;
import com.example.model.Posicion;
import com.example.model.Ruta;
import com.example.model.Tablero;

public class DamasTurcas implements EstrategiaJuego {

    @Override
    public boolean esMovimientoValido(
            Pieza pieza,
            Posicion origen,
            Posicion destino,
            Tablero tablero) {

        if (pieza == null) return false;

        Casilla casillaDestino = tablero.getCasilla(destino);
        if (casillaDestino == null || casillaDestino.isOcupada()) return false;

        int dx = destino.getFila() - origen.getFila();
        int dy = destino.getColumna() - origen.getColumna();

        // SOLO movimientos ortogonales
        if (dx != 0 && dy != 0) return false;

        int pasos = Math.abs(dx + dy);

        // movimiento simple
        if (pasos == 1) {
            return true;
        }

        // captura básica (sin validación intermedia aún)
        if (pasos == 2) {
            return true;
        }

        return false;
    }

    @Override
    public Ruta calcularMejorRuta(Pieza pieza, Tablero tablero) {

        List<Posicion> movimientos = new ArrayList<>();

        Posicion origen = buscarPosicion(pieza, tablero);

        if (origen == null) return new Ruta(movimientos, 0);

        int[][] dirs = {
                {1, 0}, {-1, 0},
                {0, 1}, {0, -1}
        };

        for (int[] d : dirs) {
            Posicion p = new Posicion(
                    origen.getFila() + d[0],
                    origen.getColumna() + d[1]
            );

            Casilla c = tablero.getCasilla(p);

            if (c != null && !c.isOcupada()) {
                movimientos.add(p);
            }
        }

        return new Ruta(movimientos, 0);
    }

    @Override
    public void inicializarTablero(Tablero tablero) {

        // ejemplo básico (lo ajustas a reglas reales después)

        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 8; c++) {
                if ((f + c) % 2 == 0) {
                    tablero.getCasilla(new Posicion(f, c))
                           .setPieza(new Pieza(COLOR.NEGRA));
                }
            }
        }

        for (int f = 5; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if ((f + c) % 2 == 0) {
                    tablero.getCasilla(new Posicion(f, c))
                           .setPieza(new Pieza(COLOR.BLANCA));
                }
            }
        }
    }

    private Posicion buscarPosicion(Pieza pieza, Tablero tablero) {

        for (Casilla c : tablero.getCasillas()) {
            if (c.isOcupada() && c.getPieza() == pieza) {
                return c.getPosicion();
            }
        }

        return null;
    }
}