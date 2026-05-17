package controllers;

import java.net.URL;

import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;
import com.example.model.Posicion;
import com.example.strategy.DamasTurcas;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class tableroCController {

    private static final String COLOR_CLARO = "#F4E7D3";
    private static final String COLOR_OSCURO = "#B97A3D";
    private static final String COLOR_SELECCION = "#E8C547";

    @FXML
    private GridPane tableroGrid;

    @FXML
    private Label J1Score;

    @FXML
    private Label J2Score;

    private Juego juego;
    private Pane[][] casillas;
    private Posicion seleccionada;
    private Image imagenBlanca;
    private Image imagenNegra;

    @FXML
    private void initialize() {
        juego = new Juego(new DamasTurcas());
        casillas = new Pane[8][8];
        imagenBlanca = cargarImagen("/com/Images/ficha clara.jpg");
        imagenNegra = cargarImagen("/com/Images/ficha oscura.jpg");

        prepararCasillas();
        pintarTablero();
    }

    @FXML
    private void reiniciarJuego() {
        seleccionada = null;
        juego.iniciarNuevoJuego();
        pintarTablero();
    }

    private void prepararCasillas() {
        for (Node node : tableroGrid.getChildren()) {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                int fila = obtenerIndice(GridPane.getRowIndex(pane));
                int columna = obtenerIndice(GridPane.getColumnIndex(pane));

                casillas[fila][columna] = pane;
                Posicion posicion = new Posicion(fila, columna);
                pane.setOnMouseClicked(event -> manejarClick(posicion));
            }
        }
    }

    private int obtenerIndice(Integer indice) {
        return indice == null ? 0 : indice;
    }

    private void manejarClick(Posicion posicion) {
        Casilla casilla = juego.getTablero().getCasilla(posicion);

        if (seleccionada == null) {
            if (casilla != null && casilla.isOcupada()
                    && casilla.getPieza().getColor() == juego.getTurnoActual()) {
                seleccionada = posicion;
                pintarTablero();
            }
            return;
        }

        if (mismaPosicion(seleccionada, posicion)) {
            seleccionada = null;
            pintarTablero();
            return;
        }

        Movimiento movimiento = new Movimiento(seleccionada, posicion);
        juego.procesarMovimiento(movimiento);
        seleccionada = null;
        pintarTablero();
    }

    private void pintarTablero() {
        int blancas = 0;
        int negras = 0;

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pane pane = casillas[fila][columna];
                if (pane == null) {
                    continue;
                }

                pane.getChildren().clear();
                pane.setStyle(estiloCasilla(fila, columna));

                Casilla casilla = juego.getTablero().getCasilla(new Posicion(fila, columna));
                if (casilla != null && casilla.isOcupada()) {
                    Pieza pieza = casilla.getPieza();
                    if (pieza.getColor() == COLOR.BLANCA) {
                        blancas++;
                    } else {
                        negras++;
                    }
                    pane.getChildren().add(crearVistaPieza(pieza));
                }
            }
        }

        J1Score.setText(String.valueOf(negras));
        J2Score.setText(String.valueOf(blancas));
    }

    private String estiloCasilla(int fila, int columna) {
        boolean estaSeleccionada = seleccionada != null
                && seleccionada.getFila() == fila
                && seleccionada.getColumna() == columna;

        String color = estaSeleccionada
                ? COLOR_SELECCION
                : ((fila + columna) % 2 == 0 ? COLOR_CLARO : COLOR_OSCURO);

        return "-fx-background-color: " + color + ";";
    }

    private Node crearVistaPieza(Pieza pieza) {
        Image imagen = pieza.getColor() == COLOR.BLANCA ? imagenBlanca : imagenNegra;

        if (imagen != null && !imagen.isError()) {
            ImageView imageView = new ImageView(imagen);
            imageView.setFitWidth(54);
            imageView.setFitHeight(54);
            imageView.setPreserveRatio(true);
            imageView.setLayoutX(8);
            imageView.setLayoutY(8);
            return imageView;
        }

        Circle ficha = new Circle(35, 35, 24);
        ficha.setFill(pieza.getColor() == COLOR.BLANCA ? Color.BEIGE : Color.SADDLEBROWN);
        ficha.setStroke(Color.web("#5E371E"));
        ficha.setStrokeWidth(2);
        return ficha;
    }

    private Image cargarImagen(String ruta) {
        URL url = getClass().getResource(ruta);
        return url == null ? null : new Image(url.toExternalForm());
    }

    private boolean mismaPosicion(Posicion a, Posicion b) {
        return a.getFila() == b.getFila() && a.getColumna() == b.getColumna();
    }
}
