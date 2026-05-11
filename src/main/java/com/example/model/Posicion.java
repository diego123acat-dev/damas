package com.example.model;

public class Posicion {
    private int x;
    private int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getFila() {
        return x;
    }

    public int getColumna() {
        return y;
    }
}
