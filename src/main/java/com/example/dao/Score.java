package com.example.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String jugador;
    private final int puntos;
    private final String fecha;

    public Score(String jugador, int puntos) {
        this.jugador = jugador;
        this.puntos = puntos;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getJugador() {
        return jugador;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getFecha() {
        return fecha;
    }
}
