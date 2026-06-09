package com.example.model;

//Clase para guardar el resultado de un movimiento
public class ResultadoMovimiento {

    private final boolean realizado;
    private final boolean captura;
    private final boolean coronacion;
    private final boolean cambiarTurno;
    private final String mensaje;

    //Constructor para crear un resultado de movimiento
    public ResultadoMovimiento(boolean realizado, boolean captura, boolean coronacion, boolean cambiarTurno, String mensaje) {
        this.realizado = realizado;
        this.captura = captura;
        this.coronacion = coronacion;
        this.cambiarTurno = cambiarTurno;
        this.mensaje = mensaje;
    }

    //Metodo para crear un resultado invalido
    public static ResultadoMovimiento invalido(String mensaje) {
        return new ResultadoMovimiento(false, false, false, false, mensaje);
    }

    //Metodo para saber si el movimiento se realizo
    public boolean isRealizado() {
        return realizado;
    }

    //Metodo para saber si hubo captura
    public boolean isCaptura() {
        return captura;
    }

    //Metodo para saber si hubo coronacion
    public boolean isCoronacion() {
        return coronacion;
    }

    //Metodo para saber si debe cambiar el turno
    public boolean isCambiarTurno() {
        return cambiarTurno;
    }

    //Metodo para obtener el mensaje
    public String getMensaje() {
        return mensaje;
    }
}
