package com.example.model;

//Clase para representar una pieza
public class Pieza {

    private final COLOR color;
    private boolean reina;

    //Constructor para crear una pieza
    public Pieza(COLOR color) {
        this.color = color;
        this.reina = false;
    }

    //Método para obtener el color
    public COLOR getColor() {
        return color;
    }

    //Método para saber si es reina
    public boolean isReina() {
        return reina;
    }

    //Método para convertir en reina
    public void coronar() {
        this.reina = true;
    }

    //Método para convertir la pieza a texto
    @Override
    public String toString() {
        return "Pieza{" +
                "color=" + color +
                ", reina=" + reina +
                '}';
    }
}
