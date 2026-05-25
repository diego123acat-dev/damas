package com.example.dao;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreDAO {

    private static final int LIMITE_SCORES = 10;

    private final ConexionDB conexion;

    public ScoreDAO() {
        this.conexion = new ConexionDB();
    }

    public void guardarScore(Score score) {
        List<Score> scores = obtenerScores();
        scores.add(score);
        scores.sort(Comparator.comparingInt(Score::getPuntos).reversed());

        if (scores.size() > LIMITE_SCORES) {
            scores = new ArrayList<>(scores.subList(0, LIMITE_SCORES));
        }

        escribirScores(scores);
    }

    public List<Score> obtenerScores() {
        Path ruta = conexion.obtenerRutaScores();
        if (!Files.exists(ruta)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ruta))) {
            Object objeto = entrada.readObject();
            if (objeto instanceof List<?>) {
                List<?> lista = (List<?>) objeto;
                List<Score> scores = new ArrayList<>();

                for (Object elemento : lista) {
                    if (elemento instanceof Score) {
                        scores.add((Score) elemento);
                    }
                }

                scores.sort(Comparator.comparingInt(Score::getPuntos).reversed());
                return scores;
            }
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudieron leer los scores: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    private void escribirScores(List<Score> scores) {
        Path ruta = conexion.obtenerRutaScores();

        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(ruta))) {
            salida.writeObject(scores);
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los scores: " + e.getMessage());
        }
    }
}
