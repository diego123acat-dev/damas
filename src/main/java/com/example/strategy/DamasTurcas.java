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

        if (dx != 0 && dy != 0) return false;

        int pasos = Math.abs(dx) + Math.abs(dy);

        if (pasos == 1) {
            return true;
        }

        if (pasos == 2) {
            Posicion intermedia = new Posicion(
                    origen.getFila() + Integer.signum(dx),
                    origen.getColumna() + Integer.signum(dy)
            );

            Casilla casillaIntermedia = tablero.getCasilla(intermedia);
            return casillaIntermedia != null
                    && casillaIntermedia.isOcupada()
                    && casillaIntermedia.getPieza().getColor() != pieza.getColor();
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

        int capturas = 0;
        for (int[] d : dirs) {
            Posicion movimientoSimple = new Posicion(
                    origen.getFila() + d[0],
                    origen.getColumna() + d[1]
            );

            if (esMovimientoValido(pieza, origen, movimientoSimple, tablero)) {
                movimientos.add(movimientoSimple);
            }

            Posicion captura = new Posicion(
                    origen.getFila() + d[0] * 2,
                    origen.getColumna() + d[1] * 2
            );

            if (esMovimientoValido(pieza, origen, captura, tablero)) {
                movimientos.add(captura);
                capturas++;
            }
        }

        return new Ruta(movimientos, capturas);
    }

    @Override
    public void inicializarTablero(Tablero tablero) {
        for (Casilla casilla : tablero.getCasillas()) {
            casilla.vaciar();
        }

        for (int fila = 1; fila <= 2; fila++) {
            colocarFila(tablero, fila, COLOR.NEGRA);
        }

        for (int fila = 5; fila <= 6; fila++) {
            colocarFila(tablero, fila, COLOR.BLANCA);
        }
    }

    private void colocarFila(Tablero tablero, int fila, COLOR color) {
        for (int columna = 0; columna < 8; columna++) {
            tablero.getCasilla(new Posicion(fila, columna)).setPieza(new Pieza(color));
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
