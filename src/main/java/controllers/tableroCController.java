package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dao.Score;
import com.example.dao.ScoreDAO;
import com.example.model.COLOR;
import com.example.model.Casilla;
import com.example.model.Juego;
import com.example.model.Movimiento;
import com.example.model.Pieza;
import com.example.model.Posicion;
import com.example.strategy.DamasTurcas;

import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class tableroCController {

    private static final String COLOR_CLARO = "#F4E7D3";
    private static final String COLOR_OSCURO = "#B97A3D";
    private static final String COLOR_SELECCION = "#E8C547";
    private static final String COLOR_INVALIDO = "#D95D5D";

    @FXML
    private GridPane tableroGrid;

    @FXML
    private Label J1Score;

    @FXML
    private Label J2Score;

    private Juego juego;
    private ScoreDAO scoreDAO;
    private Pane[][] casillas;
    private Posicion seleccionada;
    private Posicion posicionInvalida;
    private List<Posicion> destinosLegales;
    private Image imagenBlanca;
    private Image imagenNegra;

    @FXML
    private void initialize() {
        juego = new Juego(new DamasTurcas());
        scoreDAO = new ScoreDAO();
        casillas = new Pane[8][8];
        destinosLegales = new ArrayList<>();
        imagenBlanca = cargarImagen("/com/Images/ficha clara.jpg");
        imagenNegra = cargarImagen("/com/Images/ficha oscura.jpg");

        prepararCasillas();
        pintarTablero();
    }

    @FXML
    private void reiniciarJuego() {
        seleccionada = null;
        posicionInvalida = null;
        destinosLegales.clear();
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
                destinosLegales = juego.obtenerDestinosLegales(posicion);
                pintarTablero();
            }
            return;
        }

        if (mismaPosicion(seleccionada, posicion)) {
            seleccionada = null;
            destinosLegales.clear();
            pintarTablero();
            return;
        }

        if (casilla != null && casilla.isOcupada()
                && casilla.getPieza().getColor() == juego.getTurnoActual()) {
            seleccionada = posicion;
            destinosLegales = juego.obtenerDestinosLegales(posicion);
            pintarTablero();
            return;
        }

        Movimiento movimiento = new Movimiento(seleccionada, posicion);
        boolean movimientoRealizado = juego.intentarProcesarMovimiento(movimiento);

        if (movimientoRealizado) {
            seleccionada = null;
            destinosLegales.clear();
            pintarTablero();

            if (juego.esFinDelJuego()) {
                mostrarFinDelJuego();
            }
        } else {
            marcarMovimientoInvalido(posicion);
        }
    }

    private void marcarMovimientoInvalido(Posicion posicion) {
        posicionInvalida = posicion;
        pintarTablero();

        PauseTransition pausa = new PauseTransition(Duration.millis(650));
        pausa.setOnFinished(event -> {
            posicionInvalida = null;
            pintarTablero();
        });
        pausa.play();
    }

    private void mostrarFinDelJuego() {
        guardarScoreSiHayGanador();

        ButtonType nuevaPartida = new ButtonType("Nueva partida");
        ButtonType salir = new ButtonType("Salir", ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Juego terminado");
        alert.setHeaderText(juego.isEmpate() ? "Empate" : "Fin de la partida");
        alert.setContentText(juego.getMensajeFinJuego());
        alert.getButtonTypes().setAll(nuevaPartida, salir);

        Optional<ButtonType> respuesta = alert.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == nuevaPartida) {
            reiniciarJuego();
        } else {
            Platform.exit();
        }
    }

    private void guardarScoreSiHayGanador() {
        if (juego.isEmpate() || juego.getGanador() == null) {
            return;
        }

        String colorGanador = juego.getGanador() == COLOR.BLANCA ? "Blancas" : "Negras";
        TextInputDialog dialog = new TextInputDialog(colorGanador);
        dialog.setTitle("Guardar score");
        dialog.setHeaderText("Ganaron las " + colorGanador.toLowerCase());
        dialog.setContentText("Nombre del jugador:");

        Optional<String> respuesta = dialog.showAndWait();
        String jugador = respuesta.orElse(colorGanador).trim();
        if (jugador.isEmpty()) {
            jugador = colorGanador;
        }

        scoreDAO.guardarScore(new Score(jugador, juego.calcularPuntajeFinal(juego.getGanador())));
    }

    private void pintarTablero() {
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
                    pane.getChildren().add(crearVistaPieza(pieza));
                } else if (esDestinoLegal(new Posicion(fila, columna))) {
                    pane.getChildren().add(crearIndicadorDestino());
                }
            }
        }

        J1Score.setText(String.valueOf(juego.getPuntaje(COLOR.NEGRA)));
        J2Score.setText(String.valueOf(juego.getPuntaje(COLOR.BLANCA)));
    }

    private String estiloCasilla(int fila, int columna) {
        boolean estaInvalida = posicionInvalida != null
                && posicionInvalida.getFila() == fila
                && posicionInvalida.getColumna() == columna;

        if (estaInvalida) {
            return "-fx-background-color: " + COLOR_INVALIDO + ";";
        }

        boolean estaSeleccionada = seleccionada != null
                && seleccionada.getFila() == fila
                && seleccionada.getColumna() == columna;

        String color = estaSeleccionada
                ? COLOR_SELECCION
                : ((fila + columna) % 2 == 0 ? COLOR_CLARO : COLOR_OSCURO);

        return "-fx-background-color: " + color + ";";
    }

    private Node crearIndicadorDestino() {
        StackPane contenedor = new StackPane();
        contenedor.setPrefSize(70, 70);

        Circle punto = new Circle(9);
        punto.setFill(Color.rgb(91, 104, 86, 0.55));
        punto.setStroke(Color.rgb(255, 255, 255, 0.65));
        punto.setStrokeWidth(2);

        contenedor.getChildren().add(punto);
        return contenedor;
    }

    private Node crearVistaPieza(Pieza pieza) {
        Image imagen = pieza.getColor() == COLOR.BLANCA ? imagenBlanca : imagenNegra;
        StackPane contenedor = new StackPane();
        contenedor.setPrefSize(70, 70);

        if (imagen != null && !imagen.isError()) {
            ImageView imageView = new ImageView(imagen);
            imageView.setFitWidth(54);
            imageView.setFitHeight(54);
            imageView.setPreserveRatio(true);
            contenedor.getChildren().add(imageView);
        } else {
            Circle ficha = new Circle(24);
            ficha.setFill(pieza.getColor() == COLOR.BLANCA ? Color.BEIGE : Color.SADDLEBROWN);
            ficha.setStroke(Color.web("#5E371E"));
            ficha.setStrokeWidth(2);
            contenedor.getChildren().add(ficha);
        }

        if (pieza.isReina()) {
            Label marcaReina = new Label("R");
            marcaReina.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #E8C547; "
                    + "-fx-effect: dropshadow(gaussian, #3A2413, 2, 0.8, 0, 0);");
            contenedor.getChildren().add(marcaReina);
        }

        return contenedor;
    }

    private Image cargarImagen(String ruta) {
        URL url = getClass().getResource(ruta);
        return url == null ? null : new Image(url.toExternalForm());
    }

    private boolean mismaPosicion(Posicion a, Posicion b) {
        return a.getFila() == b.getFila() && a.getColumna() == b.getColumna();
    }

    private boolean esDestinoLegal(Posicion posicion) {
        for (Posicion destino : destinosLegales) {
            if (mismaPosicion(destino, posicion)) {
                return true;
            }
        }

        return false;
    }
}
