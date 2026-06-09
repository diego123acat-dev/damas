package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int movimientosSinProgreso;
    private int capturasBlancas;
    private int capturasNegras;
    private int coronacionesBlancas;
    private int coronacionesNegras;
    private Map<String, Integer> repeticionesPosicion;
    private List<Observador> observadores;

    //Constructor para crear un juego con una estrategia
    public Juego(EstrategiaJuego estrategia) {
        this.estrategia = estrategia;
        this.tablero = new Tablero();
        this.estado = new TurnoBlancas();
        this.repeticionesPosicion = new HashMap<>();
        this.observadores = new ArrayList<>();

        reiniciarDatos();
        estrategia.inicializarTablero(tablero);
        registrarPosicionActual();
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

        actualizarPuntajeYProgreso(resultado, colorMovimiento);

        if (resultado.isCambiarTurno()) {
            estado.manejarTurno(this);
        }

        registrarPosicionActual();
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

    //Metodo para actualizar el puntaje y el progreso
    private void actualizarPuntajeYProgreso(ResultadoMovimiento resultado, COLOR color) {
        if (resultado.isCaptura()) {
            registrarCaptura(color);
        }

        if (resultado.isCoronacion()) {
            registrarCoronacion(color);
        }

        if (resultado.isCaptura() || resultado.isCoronacion()) {
            movimientosSinProgreso = 0;
        } else {
            movimientosSinProgreso++;
        }
    }

    //Metodo para registrar una captura
    private void registrarCaptura(COLOR color) {
        if (color == COLOR.BLANCA) {
            capturasBlancas++;
        } else {
            capturasNegras++;
        }
    }

    //Metodo para registrar una coronacion
    private void registrarCoronacion(COLOR color) {
        if (color == COLOR.BLANCA) {
            coronacionesBlancas++;
        } else {
            coronacionesNegras++;
        }
    }

    //Metodo para registrar la posicion actual
    public void registrarPosicionActual() {
        String firma = crearFirmaPosicion();
        repeticionesPosicion.put(firma, repeticionesPosicion.getOrDefault(firma, 0) + 1);
    }

    //Metodo para saber si hay repeticion de posicion
    public boolean hayRepeticionDePosicion() {
        return repeticionesPosicion.getOrDefault(crearFirmaPosicion(), 0) >= 3;
    }

    //Metodo para crear la firma de la posicion
    private String crearFirmaPosicion() {
        StringBuilder firma = new StringBuilder();
        firma.append(getTurnoActual()).append('|');

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Casilla casilla = tablero.getCasilla(new Posicion(fila, columna));
                if (casilla == null || !casilla.isOcupada()) {
                    firma.append('.');
                    continue;
                }

                Pieza pieza = casilla.getPieza();
                firma.append(pieza.getColor() == COLOR.BLANCA ? 'B' : 'N');
                firma.append(pieza.isReina() ? 'R' : 'P');
            }
        }

        return firma.toString();
    }

    //Metodo para terminar el juego con ganador
    public void terminarConGanador(COLOR ganador, String razon) {
        if (juegoTerminado) {
            return;
        }

        this.juegoTerminado = true;
        this.empate = false;
        this.ganador = ganador;
        this.mensajeFinJuego = "Gana " + nombreColor(ganador) + ". " + razon
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

    //Metodo para obtener el nombre del color
    private String nombreColor(COLOR color) {
        return color == COLOR.BLANCA ? "blancas" : "negras";
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
        estrategia.inicializarTablero(tablero);
        registrarPosicionActual();
        notificarObservadores();
    }

    //Metodo para reiniciar los datos del juego
    private void reiniciarDatos() {
        this.juegoTerminado = false;
        this.empate = false;
        this.ganador = null;
        this.mensajeFinJuego = "";
        this.movimientosSinProgreso = 0;
        this.capturasBlancas = 0;
        this.capturasNegras = 0;
        this.coronacionesBlancas = 0;
        this.coronacionesNegras = 0;
        this.repeticionesPosicion.clear();
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

    //Metodo para obtener los movimientos sin progreso
    public int getMovimientosSinProgreso() {
        return movimientosSinProgreso;
    }

    //Metodo para obtener el puntaje
    public int getPuntaje(COLOR color) {
        int capturas = color == COLOR.BLANCA ? capturasBlancas : capturasNegras;
        int coronaciones = color == COLOR.BLANCA ? coronacionesBlancas : coronacionesNegras;
        return capturas * 10 + coronaciones * 15;
    }

    //Metodo para calcular el puntaje final
    public int calcularPuntajeFinal(COLOR color) {
        return getPuntaje(color) + 50;
    }
}
