package com.example.model;

public class Pieza {

    private final COLOR color;
    private boolean reina;

    public Pieza(COLOR color) {
        this.color = color;
        this.reina = false;
    }

    public COLOR getColor() {
        return color;
    }

    public boolean isReina() {
        return reina;
    }

    public void coronar() {
        this.reina = true;
    }

    @Override
    public String toString() {
        return "Pieza{" +
                "color=" + color +
                ", reina=" + reina +
                '}';
    }
}