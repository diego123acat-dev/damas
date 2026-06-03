package com.example.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Score)) {
            return false;
        }

        Score score = (Score) objeto;
        return puntos == score.puntos
                && Objects.equals(jugador, score.jugador)
                && Objects.equals(fecha, score.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jugador, puntos, fecha);
    }
}
