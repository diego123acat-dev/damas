package controllers;

import java.io.IOException;
import java.util.List;

import com.example.App;
import com.example.dao.DAO;
import com.example.dao.Score;
import com.example.dao.ScoreDAO;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class InicioController {

    @FXML
    private VBox scoresContainer;

    private final DAO<Score> scoreDAO = new ScoreDAO();

    @FXML
    private void initialize() {
        cargarScores();
    }

    @FXML
    private void iniciarPartida() throws IOException {
        App.setRoot("tableroC");
    }

    @FXML
    private void salir() {
        Platform.exit();
    }

    private void cargarScores() {
        scoresContainer.getChildren().clear();
        List<Score> scores = scoreDAO.obtenerTodos();

        if (scores.isEmpty()) {
            scoresContainer.getChildren().add(crearFilaScore("Sin partidas guardadas", "-"));
            return;
        }

        int limite = Math.min(scores.size(), 3);
        for (int i = 0; i < limite; i++) {
            Score score = scores.get(i);
            scoresContainer.getChildren().add(crearFilaScore(
                    (i + 1) + ". " + score.getJugador(),
                    String.valueOf(score.getPuntos())
            ));
        }
    }

    private HBox crearFilaScore(String jugador, String puntos) {
        HBox fila = new HBox();
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setSpacing(40);
        fila.setPrefWidth(390);

        Label jugadorLabel = crearLabelScore(jugador);
        jugadorLabel.setPrefWidth(270);

        Label puntosLabel = crearLabelScore(puntos);
        puntosLabel.setPrefWidth(80);

        fila.getChildren().addAll(jugadorLabel, puntosLabel);
        return fila;
    }

    private Label crearLabelScore(String texto) {
        Label label = new Label(texto);
        label.setTextFill(javafx.scene.paint.Color.web("#7A4E28"));
        label.setFont(Font.font(26));
        return label;
    }
}
