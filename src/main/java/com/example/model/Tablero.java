package com.example.model;

import java.util.List;

import com.example.observer.Observador;

public class Tablero implements com.example.observer.Sujeto, com.example.observer.Observador {
    private List<Casilla> casillas;
    private List<Observador> observadores;

    public Tablero(List<Casilla> casillas, List<Observador> observadores) {
        this.casillas = casillas;
        this.observadores = observadores;
    }

    public void moverDama(Posicion origen, Posicion destino) {
        // Lógica para mover una dama de origen a destino
        // Validar movimiento, actualizar estado del tablero, notificar observadores, etc.
    }

    public void eliminarDama(Posicion posicion) {
        // Lógica para eliminar una dama en la posición dada
        // Actualizar estado del tablero, notificar observadores, etc.
    }

    public Casilla getCasilla(Posicion posicion) {
        // Lógica para obtener la casilla en la posición dada
        return null; // Placeholder
    }

    @Override
    public void agregarObservador(Observador observador) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eliminarObservador(Observador observador) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notificarObservadores() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actualizar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
