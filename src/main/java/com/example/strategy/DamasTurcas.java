package com.example.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;
import com.example.model.Posicion;
import com.example.model.ResultadoMovimiento;
import com.example.model.Ruta;
import com.example.model.Tablero;

//Clase para aplicar las reglas de las damas turcas
public class DamasTurcas implements EstrategiaJuego {

    private int movimientosSinProgreso;
    private int capturasBlancas;
    private int capturasNegras;
    private int coronacionesBlancas;
    private int coronacionesNegras;
    private Map<String, Integer> repeticionesPosicion = new HashMap<>();

    //Metodo para validar un movimiento de damas turcas
    @Override
    public boolean esMovimientoValido(
            Pieza pieza,
            Posicion origen,
            Posicion destino,
            Tablero tablero) {
        return crearMovimiento(pieza, origen, destino, tablero) != null;
    }

    //Metodo para crear un movimiento valido
    @Override
    public Movimiento crearMovimiento(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        if (pieza == null) return null;

        Casilla casillaDestino = tablero.getCasilla(destino);
        if (casillaDestino == null || casillaDestino.isOcupada()) return null;

        int dx = destino.getFila() - origen.getFila();
        int dy = destino.getColumna() - origen.getColumna();

        if (dx != 0 && dy != 0) return null;
        if (dx == 0 && dy == 0) return null;
        if (!pieza.isReina() && esMovimientoHaciaAtras(pieza, dx)) return null;

        return pieza.isReina()
                ? crearMovimientoReina(pieza, origen, destino, tablero)
                : crearMovimientoPiezaNormal(pieza, origen, destino, tablero);
    }

    //Metodo para ejecutar un movimiento en el tablero
    @Override
    public ResultadoMovimiento ejecutarMovimiento(Movimiento movimiento, Tablero tablero, COLOR turnoActual) {
        Casilla casillaOrigen = tablero.getCasilla(movimiento.getOrigen());
        if (casillaOrigen == null || !casillaOrigen.isOcupada()) {
            return ResultadoMovimiento.invalido("Movimiento invalido.");
        }

        Pieza pieza = casillaOrigen.getPieza();
        Movimiento movimientoCompleto = crearMovimiento(
                pieza,
                movimiento.getOrigen(),
                movimiento.getDestino(),
                tablero
        );

        if (movimientoCompleto == null) {
            return ResultadoMovimiento.invalido("Movimiento invalido.");
        }

        if (hayCapturasDisponibles(tablero, turnoActual) && !movimientoCompleto.esCaptura()) {
            return ResultadoMovimiento.invalido("Hay una captura obligatoria.");
        }

        boolean huboCaptura = aplicarCapturas(movimientoCompleto, tablero);
        tablero.moverPieza(movimientoCompleto.getOrigen(), movimientoCompleto.getDestino());
        boolean huboCoronacion = coronarSiCorresponde(pieza, movimientoCompleto.getDestino());
        boolean cambiarTurno = !huboCaptura;

        return new ResultadoMovimiento(true, huboCaptura, huboCoronacion, cambiarTurno, "");
    }

    //Metodo para registrar los datos despues de un movimiento
    @Override
    public void registrarMovimiento(
            Tablero tablero,
            COLOR turnoActual,
            COLOR colorMovimiento,
            ResultadoMovimiento resultado) {
        if (resultado.isCaptura()) {
            registrarCaptura(colorMovimiento);
        }

        if (resultado.isCoronacion()) {
            registrarCoronacion(colorMovimiento);
        }

        if (resultado.isCaptura() || resultado.isCoronacion()) {
            movimientosSinProgreso = 0;
        } else {
            movimientosSinProgreso++;
        }

        registrarPosicionActual(tablero, turnoActual);
    }

    //Metodo para obtener todos los movimientos legales
    @Override
    public List<Movimiento> obtenerMovimientosLegales(Pieza pieza, Posicion origen, Tablero tablero) {
        List<Movimiento> movimientos = new ArrayList<>();

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Movimiento movimiento = crearMovimiento(
                        pieza,
                        origen,
                        new Posicion(fila, columna),
                        tablero
                );

                if (movimiento != null) {
                    movimientos.add(movimiento);
                }
            }
        }

        return movimientos;
    }

    //Metodo para obtener los destinos legales
    @Override
    public List<Posicion> obtenerDestinosLegales(Pieza pieza, Posicion origen, Tablero tablero, COLOR turnoActual) {
        List<Posicion> destinos = new ArrayList<>();
        boolean debeCapturar = hayCapturasDisponibles(tablero, turnoActual);

        for (Movimiento movimiento : obtenerMovimientosLegales(pieza, origen, tablero)) {
            if (!debeCapturar || movimiento.esCaptura()) {
                destinos.add(movimiento.getDestino());
            }
        }

        return destinos;
    }

    //Metodo para aplicar las capturas del movimiento
    private boolean aplicarCapturas(Movimiento movimiento, Tablero tablero) {
        if (!movimiento.esCaptura()) {
            return false;
        }

        for (Posicion captura : movimiento.getCapturas()) {
            tablero.eliminarPieza(captura);
        }

        return true;
    }

    //Metodo para convertir en reina si corresponde
    private boolean coronarSiCorresponde(Pieza pieza, Posicion destino) {
        if (pieza.isReina()) {
            return false;
        }

        if ((pieza.getColor() == COLOR.BLANCA && destino.getFila() == 0)
                || (pieza.getColor() == COLOR.NEGRA && destino.getFila() == 7)) {
            pieza.coronar();
            return true;
        }

        return false;
    }

    //Metodo para revisar si hay capturas disponibles
    private boolean hayCapturasDisponibles(Tablero tablero, COLOR color) {
        for (Casilla casilla : tablero.getCasillas()) {
            if (!casilla.isOcupada() || casilla.getPieza().getColor() != color) {
                continue;
            }

            Pieza pieza = casilla.getPieza();
            Posicion origen = casilla.getPosicion();

            for (Movimiento movimiento : obtenerMovimientosLegales(pieza, origen, tablero)) {
                if (movimiento.esCaptura()) {
                    return true;
                }
            }
        }

        return false;
    }

    //Metodo para crear el movimiento de una pieza normal
    private Movimiento crearMovimientoPiezaNormal(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int dx = destino.getFila() - origen.getFila();
        int dy = destino.getColumna() - origen.getColumna();
        int pasos = Math.abs(dx) + Math.abs(dy);

        if (pasos == 1) {
            return new Movimiento(origen, destino);
        }

        if (pasos == 2) {
            Posicion intermedia = new Posicion(
                    origen.getFila() + Integer.signum(dx),
                    origen.getColumna() + Integer.signum(dy)
            );

            if (hayPiezaContraria(tablero, intermedia, pieza.getColor())) {
                Movimiento movimiento = new Movimiento(origen, destino);
                movimiento.agregarCaptura(intermedia);
                return movimiento;
            }
        }

        return null;
    }

    //Metodo para crear el movimiento de una reina
    private Movimiento crearMovimientoReina(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int pasoFila = Integer.signum(destino.getFila() - origen.getFila());
        int pasoColumna = Integer.signum(destino.getColumna() - origen.getColumna());
        Posicion posicionCapturada = null;

        int fila = origen.getFila() + pasoFila;
        int columna = origen.getColumna() + pasoColumna;

        while (fila != destino.getFila() || columna != destino.getColumna()) {
            Posicion posicionActual = new Posicion(fila, columna);
            Casilla casilla = tablero.getCasilla(posicionActual);
            if (casilla == null) return null;

            if (casilla.isOcupada()) {
                if (casilla.getPieza().getColor() == pieza.getColor()) {
                    return null;
                }

                if (posicionCapturada != null) {
                    return null;
                }

                posicionCapturada = posicionActual;
            }

            fila += pasoFila;
            columna += pasoColumna;
        }

        Movimiento movimiento = new Movimiento(origen, destino);
        if (posicionCapturada != null) {
            movimiento.agregarCaptura(posicionCapturada);
        }
        return movimiento;
    }

    //Metodo para saber si una pieza normal se mueve hacia atras
    private boolean esMovimientoHaciaAtras(Pieza pieza, int dx) {
        if (dx == 0) {
            return false;
        }

        if (pieza.getColor() == COLOR.BLANCA) {
            return dx > 0;
        }

        return dx < 0;
    }

    //Metodo para saber si hay una pieza contraria
    private boolean hayPiezaContraria(Tablero tablero, Posicion posicion, COLOR color) {
        Casilla casilla = tablero.getCasilla(posicion);
        return casilla != null
                && casilla.isOcupada()
                && casilla.getPieza().getColor() != color;
    }

    //Metodo para calcular la ruta con movimientos y capturas
    @Override
    public Ruta calcularMejorRuta(Pieza pieza, Tablero tablero) {
        List<Posicion> destinos = new ArrayList<>();
        Posicion origen = buscarPosicion(pieza, tablero);

        if (origen == null) return new Ruta(destinos, 0);

        int capturas = 0;
        for (Movimiento movimiento : obtenerMovimientosLegales(pieza, origen, tablero)) {
            destinos.add(movimiento.getDestino());
            if (movimiento.esCaptura()) {
                capturas += movimiento.cantidadCapturas();
            }
        }

        return new Ruta(destinos, capturas);
    }

    //Metodo para validar si el juego termino
    @Override
    public void evaluarFinDelJuego(Juego juego) {
        int blancas = contarPiezas(juego.getTablero(), COLOR.BLANCA);
        int negras = contarPiezas(juego.getTablero(), COLOR.NEGRA);

        if (blancas == 0) {
            juego.terminarConGanador(COLOR.NEGRA, "Las blancas se quedaron sin piezas.");
            return;
        }

        if (negras == 0) {
            juego.terminarConGanador(COLOR.BLANCA, "Las negras se quedaron sin piezas.");
            return;
        }

        if (!hayMovimientosDisponibles(juego.getTablero(), juego.getTurnoActual())) {
            COLOR ganador = colorContrario(juego.getTurnoActual());
            juego.terminarConGanador(ganador, nombreColor(juego.getTurnoActual()) + " no puede realizar movimientos.");
            return;
        }

        if (hayRepeticionDePosicion(juego.getTablero(), juego.getTurnoActual())) {
            juego.terminarEnEmpate("Empate por repeticion de posicion.");
            return;
        }

        if (movimientosSinProgreso >= 50) {
            juego.terminarEnEmpate("Empate por 50 movimientos sin capturas ni coronacion.");
        }
    }

    //Metodo para contar piezas por color
    private int contarPiezas(Tablero tablero, COLOR color) {
        int total = 0;
        for (Casilla casilla : tablero.getCasillas()) {
            if (casilla.isOcupada() && casilla.getPieza().getColor() == color) {
                total++;
            }
        }
        return total;
    }

    //Metodo para revisar si hay movimientos disponibles
    private boolean hayMovimientosDisponibles(Tablero tablero, COLOR color) {
        for (Casilla casilla : tablero.getCasillas()) {
            if (!casilla.isOcupada() || casilla.getPieza().getColor() != color) {
                continue;
            }

            Pieza pieza = casilla.getPieza();
            Posicion origen = casilla.getPosicion();

            if (!obtenerMovimientosLegales(pieza, origen, tablero).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    //Metodo para obtener el color contrario
    private COLOR colorContrario(COLOR color) {
        return color == COLOR.BLANCA ? COLOR.NEGRA : COLOR.BLANCA;
    }

    //Metodo para obtener el nombre del color
    private String nombreColor(COLOR color) {
        return color == COLOR.BLANCA ? "blancas" : "negras";
    }

    //Metodo para colocar las piezas iniciales
    @Override
    public void inicializarTablero(Tablero tablero, COLOR turnoInicial) {
        reiniciarDatos();

        for (Casilla casilla : tablero.getCasillas()) {
            casilla.vaciar();
        }

        for (int fila = 1; fila <= 2; fila++) {
            colocarFila(tablero, fila, COLOR.NEGRA);
        }

        for (int fila = 5; fila <= 6; fila++) {
            colocarFila(tablero, fila, COLOR.BLANCA);
        }

        registrarPosicionActual(tablero, turnoInicial);
    }

    //Metodo para reiniciar los datos del juego
    private void reiniciarDatos() {
        this.movimientosSinProgreso = 0;
        this.capturasBlancas = 0;
        this.capturasNegras = 0;
        this.coronacionesBlancas = 0;
        this.coronacionesNegras = 0;
        this.repeticionesPosicion.clear();
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
    private void registrarPosicionActual(Tablero tablero, COLOR turnoActual) {
        String firma = crearFirmaPosicion(tablero, turnoActual);
        repeticionesPosicion.put(firma, repeticionesPosicion.getOrDefault(firma, 0) + 1);
    }

    //Metodo para saber si hay repeticion de posicion
    private boolean hayRepeticionDePosicion(Tablero tablero, COLOR turnoActual) {
        return repeticionesPosicion.getOrDefault(crearFirmaPosicion(tablero, turnoActual), 0) >= 3;
    }

    //Metodo para crear la firma de la posicion
    private String crearFirmaPosicion(Tablero tablero, COLOR turnoActual) {
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

    //Metodo para obtener el puntaje
    @Override
    public int getPuntaje(COLOR color) {
        int capturas = color == COLOR.BLANCA ? capturasBlancas : capturasNegras;
        int coronaciones = color == COLOR.BLANCA ? coronacionesBlancas : coronacionesNegras;
        return capturas * 10 + coronaciones * 15;
    }

    //Metodo para calcular el puntaje final
    @Override
    public int calcularPuntajeFinal(COLOR color) {
        return getPuntaje(color) + 50;
    }

    //Metodo para obtener el nombre del color
    @Override
    public String getNombreColor(COLOR color) {
        return nombreColor(color);
    }

    //Metodo para colocar una fila de piezas
    private void colocarFila(Tablero tablero, int fila, COLOR color) {
        for (int columna = 0; columna < 8; columna++) {
            tablero.getCasilla(new Posicion(fila, columna)).setPieza(new Pieza(color));
        }
    }

    //Metodo para buscar la posicion de una pieza
    private Posicion buscarPosicion(Pieza pieza, Tablero tablero) {
        for (Casilla casilla : tablero.getCasillas()) {
            if (casilla.isOcupada() && casilla.getPieza() == pieza) {
                return casilla.getPosicion();
            }
        }

        return null;
    }
}
