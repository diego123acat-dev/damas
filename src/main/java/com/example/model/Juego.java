package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.observer.Observador;
import com.example.observer.Sujeto;
import com.example.state.EstadoJuego;
import com.example.state.TurnoBlancas;
import com.example.strategy.EstrategiaJuego;

public class Juego implements Sujeto {

    private EstadoJuego estado;
    private EstrategiaJuego estrategia;
    private Tablero tablero;
    private COLOR turnoActual;

    private List<Observador> observadores;

    public Juego(EstrategiaJuego estrategia) {
        this.estrategia = estrategia;
        this.tablero = new Tablero();
        this.estado = new TurnoBlancas();
        this.turnoActual = COLOR.BLANCA;
        this.observadores = new ArrayList<>();

        estrategia.inicializarTablero(tablero);
    }

    public void procesarMovimiento(Movimiento movimiento) {
        Casilla origen = tablero.getCasilla(movimiento.getOrigen());
        if (origen == null || origen.getPieza() == null) return;

        Pieza pieza = origen.getPieza();

        if (!estado.esMovimientoValido(this, movimiento)) {
            System.out.println("No es el turno de esa pieza.");
            return;
        }

        boolean valido = estrategia.esMovimientoValido(
                pieza,
                movimiento.getOrigen(),
                movimiento.getDestino(),
                tablero
        );

        if (!valido) {
            System.out.println("Movimiento invalido.");
            return;
        }

        eliminarPiezaCapturada(movimiento, pieza);
        tablero.moverPieza(movimiento.getOrigen(), movimiento.getDestino());
        coronarSiCorresponde(pieza, movimiento.getDestino());

        estado.manejarTurno(this);
        cambiarTurno();

        if (estado.esJuegoTerminado(this)) {
            System.out.println("Juego terminado. Ganador: " + turnoActual);
        }

        notificarObservadores();
    }

    private void eliminarPiezaCapturada(Movimiento movimiento, Pieza pieza) {
        int dx = movimiento.getDestino().getFila() - movimiento.getOrigen().getFila();
        int dy = movimiento.getDestino().getColumna() - movimiento.getOrigen().getColumna();

        if (Math.abs(dx) + Math.abs(dy) != 2) {
            return;
        }

        Posicion posicionCapturada = new Posicion(
                movimiento.getOrigen().getFila() + Integer.signum(dx),
                movimiento.getOrigen().getColumna() + Integer.signum(dy)
        );

        Casilla casillaCapturada = tablero.getCasilla(posicionCapturada);
        if (casillaCapturada != null && casillaCapturada.isOcupada()
                && casillaCapturada.getPieza().getColor() != pieza.getColor()) {
            tablero.eliminarPieza(posicionCapturada);
        }
    }

    private void coronarSiCorresponde(Pieza pieza, Posicion destino) {
        if ((pieza.getColor() == COLOR.BLANCA && destino.getFila() == 0)
                || (pieza.getColor() == COLOR.NEGRA && destino.getFila() == 7)) {
            pieza.coronar();
        }
    }

    public void cambiarTurno() {
        turnoActual = (turnoActual == COLOR.BLANCA)
                ? COLOR.NEGRA
                : COLOR.BLANCA;
    }

    public void cambiarEstado(EstadoJuego nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean esFinDelJuego() {
        return estado.esJuegoTerminado(this);
    }

    public void iniciarNuevoJuego() {
        this.estado = new TurnoBlancas();
        this.turnoActual = COLOR.BLANCA;
        estrategia.inicializarTablero(tablero);
        notificarObservadores();
    }

    @Override
    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    @Override
    public void eliminarObservador(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notificarObservadores() {
        for (Observador o : observadores) {
            o.actualizar();
        }
    }

    public Tablero getTablero() {
        return tablero;
    }

    public COLOR getTurnoActual() {
        return turnoActual;
    }
}
