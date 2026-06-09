package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.observer.Observador;
import com.example.observer.Sujeto;
import com.example.state.EstadoJuego;
import com.example.state.TurnoBlancas;
import com.example.strategy.EstrategiaJuego;

//Clase para coordinar el juego
public class Juego implements Sujeto {

    private EstadoJuego estado;
    private EstrategiaJuego estrategia;
    private Tablero tablero;
    private boolean juegoTerminado;
    private boolean empate;
    private COLOR ganador;
    private String mensajeFinJuego;
    private List<Observador> observadores;

    //Constructor para crear un juego con una estrategia
    public Juego(EstrategiaJuego estrategia) {
        this.estrategia = estrategia;
        this.tablero = new Tablero();
        this.estado = new TurnoBlancas();
        this.observadores = new ArrayList<>();

        reiniciarDatos();
        estrategia.inicializarTablero(tablero, getTurnoActual());
    }

    //Metodo para procesar un movimiento
    public void procesarMovimiento(Movimiento movimiento) {
        intentarProcesarMovimiento(movimiento);
    }

    //Metodo para intentar realizar un movimiento
    public boolean intentarProcesarMovimiento(Movimiento movimiento) {
        if (estado.esJuegoTerminado(this)) {
            notificarObservadores();
            return false;
        }

        if (!estado.esMovimientoValido(this, movimiento)) {
            System.out.println("No es el turno de esa pieza.");
            return false;
        }

        COLOR colorMovimiento = getTurnoActual();
        ResultadoMovimiento resultado = estrategia.ejecutarMovimiento(movimiento, tablero, colorMovimiento);

        if (!resultado.isRealizado()) {
            System.out.println(resultado.getMensaje());
            return false;
        }

        if (resultado.isCambiarTurno()) {
            estado.manejarTurno(this);
        }

        estrategia.registrarMovimiento(tablero, getTurnoActual(), colorMovimiento, resultado);
        estado.esJuegoTerminado(this);
        notificarObservadores();
        return true;
    }

    //Metodo para obtener los destinos legales de una pieza
    public List<Posicion> obtenerDestinosLegales(Posicion origen) {
        Casilla casillaOrigen = tablero.getCasilla(origen);
        if (casillaOrigen == null || !casillaOrigen.isOcupada()) {
            return new ArrayList<>();
        }

        Pieza pieza = casillaOrigen.getPieza();
        if (pieza.getColor() != getTurnoActual()) {
            return new ArrayList<>();
        }

        return estrategia.obtenerDestinosLegales(pieza, origen, tablero, getTurnoActual());
    }

    //Metodo para terminar el juego con ganador
    public void terminarConGanador(COLOR ganador, String razon) {
        if (juegoTerminado) {
            return;
        }

        this.juegoTerminado = true;
        this.empate = false;
        this.ganador = ganador;
        this.mensajeFinJuego = "Gana " + estrategia.getNombreColor(ganador) + ". " + razon
                + "\nPuntaje final: " + calcularPuntajeFinal(ganador);
    }

    //Metodo para terminar el juego en empate
    public void terminarEnEmpate(String razon) {
        if (juegoTerminado) {
            return;
        }

        this.juegoTerminado = true;
        this.empate = true;
        this.ganador = null;
        this.mensajeFinJuego = razon;
    }

    //Metodo para cambiar el estado del juego
    public void cambiarEstado(EstadoJuego nuevoEstado) {
        this.estado = nuevoEstado;
    }

    //Metodo para saber si el juego termino
    public boolean esFinDelJuego() {
        return juegoTerminado;
    }

    //Metodo para iniciar un nuevo juego
    public void iniciarNuevoJuego() {
        this.estado = new TurnoBlancas();
        reiniciarDatos();
        estrategia.inicializarTablero(tablero, getTurnoActual());
        notificarObservadores();
    }

    //Metodo para reiniciar los datos del juego
    private void reiniciarDatos() {
        this.juegoTerminado = false;
        this.empate = false;
        this.ganador = null;
        this.mensajeFinJuego = "";
    }

    //Metodo para agregar un observador
    @Override
    public void agregarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    //Metodo para eliminar un observador
    @Override
    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    //Metodo para notificar a los observadores
    @Override
    public void notificarObservadores() {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.actualizar(this);
        }
    }

    //Metodo para obtener la estrategia
    public EstrategiaJuego getEstrategia() {
        return estrategia;
    }

    //Metodo para obtener el tablero
    public Tablero getTablero() {
        return tablero;
    }

    //Metodo para obtener el turno actual
    public COLOR getTurnoActual() {
        return estado.getColorTurno();
    }

    //Metodo para saber si hubo empate
    public boolean isEmpate() {
        return empate;
    }

    //Metodo para obtener el ganador
    public COLOR getGanador() {
        return ganador;
    }

    //Metodo para obtener el mensaje final
    public String getMensajeFinJuego() {
        return mensajeFinJuego;
    }

    //Metodo para obtener el puntaje
    public int getPuntaje(COLOR color) {
        return estrategia.getPuntaje(color);
    }

    //Metodo para calcular el puntaje final
    public int calcularPuntajeFinal(COLOR color) {
        return estrategia.calcularPuntajeFinal(color);
    }
}
