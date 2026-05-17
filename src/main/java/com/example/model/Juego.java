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
        this.estado = new TurnoBlancas(); // mejor que depender de strategy
        this.turnoActual = COLOR.BLANCA;
        this.observadores = new ArrayList<>();

        estrategia.inicializarTablero(tablero);
    }

    public void procesarMovimiento(Movimiento movimiento) {

        Casilla origen = tablero.getCasilla(movimiento.getOrigen());
        if (origen == null || origen.getPieza() == null) return;

        Pieza pieza = origen.getPieza();

        boolean valido = estrategia.esMovimientoValido(
                pieza,
                movimiento.getOrigen(),
                movimiento.getDestino(),
                tablero
        );

        if (!valido) {
            System.out.println("Movimiento inválido.");
            return;
        }

        tablero.moverPieza(
                movimiento.getOrigen(),
                movimiento.getDestino()
        );

        cambiarTurno();

        if (estado.esJuegoTerminado(this)) {
            System.out.println("¡Juego terminado! Ganador: " + turnoActual);
        }

        notificarObservadores();
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
    }

    // ================= OBSERVER =================

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

    // getters útiles
    public Tablero getTablero() {
        return tablero;
    }

    public COLOR getTurnoActual() {
        return turnoActual;
    }
}