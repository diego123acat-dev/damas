package com.example.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

//Clase para representar un score
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String jugador;
    private final int puntos;
    private final String fecha;

    //Constructor para crear un score
    public Score(String jugador, int puntos) {
        this.jugador = jugador;
        this.puntos = puntos;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    //Método para obtener el jugador
    public String getJugador() {
        return jugador;
    }

    //Método para obtener los puntos
    public int getPuntos() {
        return puntos;
    }

    //Método para obtener la fecha
    public String getFecha() {
        return fecha;
    }

    //Método para comparar scores
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

    //Método para generar el codigo hash
    @Override
    public int hashCode() {
        return Objects.hash(jugador, puntos, fecha);
    }
}
