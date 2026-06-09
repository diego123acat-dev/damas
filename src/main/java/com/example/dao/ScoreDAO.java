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

//Clase para manejar los scores guardados
public class ScoreDAO implements DAO<Score> {

    private static final int LIMITE_SCORES = 10;

    private final ConexionDB conexion;

    //Constructor para crear la conexion de scores
    public ScoreDAO() {
        this.conexion = new ConexionDB();
    }

    //Método para guardar un score
    @Override
    public void guardar(Score score) {
        List<Score> scores = obtenerTodos();
        scores.add(score);
        scores.sort(Comparator.comparingInt(Score::getPuntos).reversed());

        if (scores.size() > LIMITE_SCORES) {
            scores = new ArrayList<>(scores.subList(0, LIMITE_SCORES));
        }

        escribirScores(scores);
    }

    //Método para obtener todos los scores
    @Override
    public List<Score> obtenerTodos() {
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

    //Método para eliminar un score
    @Override
    public void eliminar(Score score) {
        List<Score> scores = obtenerTodos();
        scores.remove(score);
        escribirScores(scores);
    }

    //Método para eliminar todos los scores
    @Override
    public void eliminarTodos() {
        escribirScores(new ArrayList<>());
    }

    //Método para guardar un score desde otro nombre
    public void guardarScore(Score score) {
        guardar(score);
    }

    //Método para obtener los scores desde otro nombre
    public List<Score> obtenerScores() {
        return obtenerTodos();
    }

    //Método para escribir los scores en el archivo
    private void escribirScores(List<Score> scores) {
        Path ruta = conexion.obtenerRutaScores();

        try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(ruta))) {
            salida.writeObject(scores);
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los scores: " + e.getMessage());
        }
    }
}
