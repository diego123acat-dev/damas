package com.example.strategy;

import java.util.ArrayList;
import java.util.List;

import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Movimiento;
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
        return crearMovimiento(pieza, origen, destino, tablero) != null;
    }

    @Override
    public Movimiento crearMovimiento(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        if (pieza == null) return null;

        Casilla casillaDestino = tablero.getCasilla(destino);
        if (casillaDestino == null || casillaDestino.isOcupada()) return null;

        int dx = destino.getFila() - origen.getFila();
        int dy = destino.getColumna() - origen.getColumna();

        if (dx != 0 && dy != 0) return null;
        if (dx == 0 && dy == 0) return null;
        if (!pieza.isReina() && esMovimientoHaciaAtras(pieza, dx)) return null;

        return pieza.isReina()
                ? crearMovimientoReina(pieza, origen, destino, tablero)
                : crearMovimientoPiezaNormal(pieza, origen, destino, tablero);
    }

    @Override
    public List<Movimiento> obtenerMovimientosLegales(Pieza pieza, Posicion origen, Tablero tablero) {
        List<Movimiento> movimientos = new ArrayList<>();

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Movimiento movimiento = crearMovimiento(
                        pieza,
                        origen,
                        new Posicion(fila, columna),
                        tablero
                );

                if (movimiento != null) {
                    movimientos.add(movimiento);
                }
            }
        }

        return movimientos;
    }

    private Movimiento crearMovimientoPiezaNormal(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int dx = destino.getFila() - origen.getFila();
        int dy = destino.getColumna() - origen.getColumna();
        int pasos = Math.abs(dx) + Math.abs(dy);

        if (pasos == 1) {
            return new Movimiento(origen, destino);
        }

        if (pasos == 2) {
            Posicion intermedia = new Posicion(
                    origen.getFila() + Integer.signum(dx),
                    origen.getColumna() + Integer.signum(dy)
            );

            if (hayPiezaContraria(tablero, intermedia, pieza.getColor())) {
                Movimiento movimiento = new Movimiento(origen, destino);
                movimiento.agregarCaptura(intermedia);
                return movimiento;
            }
        }

        return null;
    }

    private Movimiento crearMovimientoReina(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int pasoFila = Integer.signum(destino.getFila() - origen.getFila());
        int pasoColumna = Integer.signum(destino.getColumna() - origen.getColumna());
        Posicion posicionCapturada = null;

        int fila = origen.getFila() + pasoFila;
        int columna = origen.getColumna() + pasoColumna;

        while (fila != destino.getFila() || columna != destino.getColumna()) {
            Posicion posicionActual = new Posicion(fila, columna);
            Casilla casilla = tablero.getCasilla(posicionActual);
            if (casilla == null) return null;

            if (casilla.isOcupada()) {
                if (casilla.getPieza().getColor() == pieza.getColor()) {
                    return null;
                }

                if (posicionCapturada != null) {
                    return null;
                }

                posicionCapturada = posicionActual;
            }

            fila += pasoFila;
            columna += pasoColumna;
        }

        Movimiento movimiento = new Movimiento(origen, destino);
        if (posicionCapturada != null) {
            movimiento.agregarCaptura(posicionCapturada);
        }
        return movimiento;
    }

    private boolean esMovimientoHaciaAtras(Pieza pieza, int dx) {
        if (dx == 0) {
            return false;
        }

        if (pieza.getColor() == COLOR.BLANCA) {
            return dx > 0;
        }

        return dx < 0;
    }

    private boolean hayPiezaContraria(Tablero tablero, Posicion posicion, COLOR color) {
        Casilla casilla = tablero.getCasilla(posicion);
        return casilla != null
                && casilla.isOcupada()
                && casilla.getPieza().getColor() != color;
    }

    @Override
    public Ruta calcularMejorRuta(Pieza pieza, Tablero tablero) {
        List<Posicion> destinos = new ArrayList<>();
        Posicion origen = buscarPosicion(pieza, tablero);

        if (origen == null) return new Ruta(destinos, 0);

        int capturas = 0;
        for (Movimiento movimiento : obtenerMovimientosLegales(pieza, origen, tablero)) {
            destinos.add(movimiento.getDestino());
            if (movimiento.esCaptura()) {
                capturas += movimiento.cantidadCapturas();
            }
        }

        return new Ruta(destinos, capturas);
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
