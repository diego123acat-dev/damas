package com.example.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConexionDB {

    private static final String CARPETA_DATOS = ".damas-turcas";
    private static final String ARCHIVO_SCORES = "scores.dat";

    public Path obtenerRutaScores() {
        Path carpeta = Paths.get(System.getProperty("user.home"), CARPETA_DATOS);

        try {
            Files.createDirectories(carpeta);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo crear la carpeta de scores.", e);
        }

        return carpeta.resolve(ARCHIVO_SCORES);
    }
}
