package com.example.strategy;

import com.example.model.Ruta;
import com.example.model.Tablero;

public class DamasTurcas implements EstrategiaJuego {
    @Override
    public boolean esMovimientoValido(int xOrigen, int yOrigen, int xDestino, int yDestino) {
        // Implementación específica para Damas Turcas
        return false;
    }

    @Override
    public Ruta calcularMejorRuta() {
        // Implementación específica para Damas Turcas
        return null;
    }

    @Override
    public void inicializarTablero(Tablero tablero) {
        // Implementación específica para Damas Turcas
    }


}
