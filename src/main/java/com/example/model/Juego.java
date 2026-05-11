package com.example.model;

import com.example.state.EstadoJuego;
import com.example.strategy.EstrategiaJuego;

public class Juego {
    private EstadoJuego estadoActual;
    private EstrategiaJuego estrategiaJuego;

    public Juego(EstrategiaJuego estrategiaJuego) {
        this.estrategiaJuego = estrategiaJuego;
        this.estadoActual = estrategiaJuego.iniciarJuego();
    }

    public void procesarMovimiento(Posicion origen, Posicion destino) {
        if (estrategiaJuego.esMovimientoValido(estadoActual, origen, destino)) {
            estadoActual = estrategiaJuego.ejecutarMovimiento(estadoActual, origen, destino);
        } else {
            throw new IllegalArgumentException("Movimiento no válido");
        }
    }

    public void cambiarEstado(EstadoJuego nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void iniciarNuevoJuego() {
        this.estadoActual = estrategiaJuego.iniciarJuego();
    }
}
