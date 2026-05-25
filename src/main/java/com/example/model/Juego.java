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

public class Juego implements Sujeto {

    private EstadoJuego estado;
    private EstrategiaJuego estrategia;
    private Tablero tablero;
    private COLOR turnoActual;
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

    public Juego(EstrategiaJuego estrategia) {
        this.estrategia = estrategia;
        this.tablero = new Tablero();
        this.estado = new TurnoBlancas();
        this.turnoActual = COLOR.BLANCA;
        this.juegoTerminado = false;
        this.empate = false;
        this.ganador = null;
        this.mensajeFinJuego = "";
        this.movimientosSinProgreso = 0;
        this.capturasBlancas = 0;
        this.capturasNegras = 0;
        this.coronacionesBlancas = 0;
        this.coronacionesNegras = 0;
        this.repeticionesPosicion = new HashMap<>();
        this.observadores = new ArrayList<>();

        estrategia.inicializarTablero(tablero);
        registrarPosicionActual();
    }

    public void procesarMovimiento(Movimiento movimiento) {
        intentarProcesarMovimiento(movimiento);
    }

    public boolean intentarProcesarMovimiento(Movimiento movimiento) {
        if (juegoTerminado) return false;

        Casilla origen = tablero.getCasilla(movimiento.getOrigen());
        if (origen == null || origen.getPieza() == null) return false;

        Pieza pieza = origen.getPieza();

        if (!estado.esMovimientoValido(this, movimiento)) {
            System.out.println("No es el turno de esa pieza.");
            return false;
        }

        boolean valido = estrategia.esMovimientoValido(
                pieza,
                movimiento.getOrigen(),
                movimiento.getDestino(),
                tablero
        );

        if (!valido) {
            System.out.println("Movimiento invalido.");
            return false;
        }

        if (hayCapturasDisponibles(turnoActual) && !esMovimientoDeCaptura(movimiento, pieza)) {
            System.out.println("Hay una captura obligatoria.");
            return false;
        }

        boolean huboCaptura = eliminarPiezaCapturada(movimiento, pieza);
        tablero.moverPieza(movimiento.getOrigen(), movimiento.getDestino());
        boolean huboCoronacion = coronarSiCorresponde(pieza, movimiento.getDestino());

        if (!huboCaptura) {
            estado.manejarTurno(this);
            cambiarTurno();
        }

        actualizarContadorProgreso(huboCaptura, huboCoronacion);
        evaluarFinDelJuego();

        notificarObservadores();
        return true;
    }

    public List<Posicion> obtenerDestinosLegales(Posicion origen) {
        List<Posicion> destinos = new ArrayList<>();

        if (juegoTerminado) {
            return destinos;
        }

        Casilla casillaOrigen = tablero.getCasilla(origen);
        if (casillaOrigen == null || !casillaOrigen.isOcupada()) {
            return destinos;
        }

        Pieza pieza = casillaOrigen.getPieza();
        if (pieza.getColor() != turnoActual) {
            return destinos;
        }

        boolean debeCapturar = hayCapturasDisponibles(turnoActual);

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Posicion destino = new Posicion(fila, columna);
                Movimiento movimiento = new Movimiento(origen, destino);

                if (!estrategia.esMovimientoValido(pieza, origen, destino, tablero)) {
                    continue;
                }

                boolean esCaptura = esMovimientoDeCaptura(movimiento, pieza);
                if (!debeCapturar || esCaptura) {
                    destinos.add(destino);
                }
            }
        }

        return destinos;
    }

    private boolean eliminarPiezaCapturada(Movimiento movimiento, Pieza pieza) {
        Posicion posicionCapturada = buscarPosicionCapturada(movimiento, pieza);
        if (posicionCapturada != null) {
            tablero.eliminarPieza(posicionCapturada);
            registrarCaptura(pieza.getColor());
            return true;
        }

        return false;
    }

    private boolean hayCapturasDisponibles(COLOR color) {
        for (Casilla casilla : tablero.getCasillas()) {
            if (!casilla.isOcupada() || casilla.getPieza().getColor() != color) {
                continue;
            }

            Pieza pieza = casilla.getPieza();
            Posicion origen = casilla.getPosicion();

            for (int fila = 0; fila < 8; fila++) {
                for (int columna = 0; columna < 8; columna++) {
                    Posicion destino = new Posicion(fila, columna);
                    Movimiento movimiento = new Movimiento(origen, destino);

                    if (estrategia.esMovimientoValido(pieza, origen, destino, tablero)
                            && esMovimientoDeCaptura(movimiento, pieza)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean esMovimientoDeCaptura(Movimiento movimiento, Pieza pieza) {
        return buscarPosicionCapturada(movimiento, pieza) != null;
    }

    private Posicion buscarPosicionCapturada(Movimiento movimiento, Pieza pieza) {
        int dx = movimiento.getDestino().getFila() - movimiento.getOrigen().getFila();
        int dy = movimiento.getDestino().getColumna() - movimiento.getOrigen().getColumna();

        int pasoFila = Integer.signum(dx);
        int pasoColumna = Integer.signum(dy);

        int fila = movimiento.getOrigen().getFila() + pasoFila;
        int columna = movimiento.getOrigen().getColumna() + pasoColumna;

        while (fila != movimiento.getDestino().getFila()
                || columna != movimiento.getDestino().getColumna()) {
            Posicion posicionActual = new Posicion(fila, columna);
            Casilla casillaActual = tablero.getCasilla(posicionActual);

            if (casillaActual != null && casillaActual.isOcupada()
                    && casillaActual.getPieza().getColor() != pieza.getColor()) {
                return posicionActual;
            }

            fila += pasoFila;
            columna += pasoColumna;
        }

        return null;
    }

    private boolean coronarSiCorresponde(Pieza pieza, Posicion destino) {
        if (pieza.isReina()) {
            return false;
        }

        if ((pieza.getColor() == COLOR.BLANCA && destino.getFila() == 0)
                || (pieza.getColor() == COLOR.NEGRA && destino.getFila() == 7)) {
            pieza.coronar();
            registrarCoronacion(pieza.getColor());
            return true;
        }

        return false;
    }

    private void registrarCaptura(COLOR color) {
        if (color == COLOR.BLANCA) {
            capturasBlancas++;
        } else {
            capturasNegras++;
        }
    }

    private void registrarCoronacion(COLOR color) {
        if (color == COLOR.BLANCA) {
            coronacionesBlancas++;
        } else {
            coronacionesNegras++;
        }
    }

    private void actualizarContadorProgreso(boolean huboCaptura, boolean huboCoronacion) {
        if (huboCaptura || huboCoronacion) {
            movimientosSinProgreso = 0;
        } else {
            movimientosSinProgreso++;
        }
    }

    private void evaluarFinDelJuego() {
        int blancas = contarPiezas(COLOR.BLANCA);
        int negras = contarPiezas(COLOR.NEGRA);

        if (blancas == 0) {
            terminarConGanador(COLOR.NEGRA, "Las blancas se quedaron sin piezas.");
            return;
        }

        if (negras == 0) {
            terminarConGanador(COLOR.BLANCA, "Las negras se quedaron sin piezas.");
            return;
        }

        if (!hayMovimientosDisponibles(turnoActual)) {
            terminarConGanador(colorContrario(turnoActual),
                    nombreColor(turnoActual) + " no puede realizar movimientos.");
            return;
        }

        registrarPosicionActual();

        if (hayRepeticionDePosicion()) {
            terminarEnEmpate("Empate por repeticion de posicion.");
            return;
        }

        if (movimientosSinProgreso >= 50) {
            terminarEnEmpate("Empate por 50 movimientos sin capturas ni coronacion.");
        }
    }

    private int contarPiezas(COLOR color) {
        int total = 0;
        for (Casilla casilla : tablero.getCasillas()) {
            if (casilla.isOcupada() && casilla.getPieza().getColor() == color) {
                total++;
            }
        }
        return total;
    }

    private boolean hayMovimientosDisponibles(COLOR color) {
        for (Casilla casilla : tablero.getCasillas()) {
            if (!casilla.isOcupada() || casilla.getPieza().getColor() != color) {
                continue;
            }

            Pieza pieza = casilla.getPieza();
            Posicion origen = casilla.getPosicion();

            for (int fila = 0; fila < 8; fila++) {
                for (int columna = 0; columna < 8; columna++) {
                    Posicion destino = new Posicion(fila, columna);
                    if (estrategia.esMovimientoValido(pieza, origen, destino, tablero)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void registrarPosicionActual() {
        String firma = crearFirmaPosicion();
        repeticionesPosicion.put(firma, repeticionesPosicion.getOrDefault(firma, 0) + 1);
    }

    private boolean hayRepeticionDePosicion() {
        return repeticionesPosicion.getOrDefault(crearFirmaPosicion(), 0) >= 3;
    }

    private String crearFirmaPosicion() {
        StringBuilder firma = new StringBuilder();
        firma.append(turnoActual).append('|');

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

    private void terminarConGanador(COLOR ganador, String razon) {
        this.juegoTerminado = true;
        this.empate = false;
        this.ganador = ganador;
        this.mensajeFinJuego = "Gana " + nombreColor(ganador) + ". " + razon
                + "\nPuntaje final: " + calcularPuntajeFinal(ganador);
    }

    private void terminarEnEmpate(String razon) {
        this.juegoTerminado = true;
        this.empate = true;
        this.ganador = null;
        this.mensajeFinJuego = razon;
    }

    private COLOR colorContrario(COLOR color) {
        return color == COLOR.BLANCA ? COLOR.NEGRA : COLOR.BLANCA;
    }

    private String nombreColor(COLOR color) {
        return color == COLOR.BLANCA ? "blancas" : "negras";
    }

    public void cambiarTurno() {
        turnoActual = (turnoActual == COLOR.BLANCA)
                ? COLOR.NEGRA
                : COLOR.BLANCA;
    }

    public void cambiarEstado(EstadoJuego nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean esFinDelJuego() {
        return juegoTerminado;
    }

    public void iniciarNuevoJuego() {
        this.estado = new TurnoBlancas();
        this.turnoActual = COLOR.BLANCA;
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
        estrategia.inicializarTablero(tablero);
        registrarPosicionActual();
        notificarObservadores();
    }

    @Override
    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    @Override
    public void eliminarObservador(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notificarObservadores() {
        for (Observador o : observadores) {
            o.actualizar();
        }
    }

    public Tablero getTablero() {
        return tablero;
    }

    public COLOR getTurnoActual() {
        return turnoActual;
    }

    public boolean isEmpate() {
        return empate;
    }

    public COLOR getGanador() {
        return ganador;
    }

    public String getMensajeFinJuego() {
        return mensajeFinJuego;
    }

    public int getPuntaje(COLOR color) {
        int capturas = color == COLOR.BLANCA ? capturasBlancas : capturasNegras;
        int coronaciones = color == COLOR.BLANCA ? coronacionesBlancas : coronacionesNegras;
        return capturas * 10 + coronaciones * 15;
    }

    public int calcularPuntajeFinal(COLOR color) {
        return getPuntaje(color) + 50;
    }
}
