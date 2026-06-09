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

//Clase para controlar la pantalla de inicio
public class InicioController {

    @FXML
    private VBox scoresContainer;

    private final DAO<Score> scoreDAO = new ScoreDAO();

    //Método para inicializar la pantalla de inicio
    @FXML
    private void initialize() {
        cargarScores();
    }

    //Método para iniciar una partida
    @FXML
    private void iniciarPartida() throws IOException {
        App.setRoot("tableroC");
    }

    //Método para salir de la aplicacion
    @FXML
    private void salir() {
        Platform.exit();
    }

    //Método para cargar los mejores scores
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

    //Método para crear una fila de score
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

    //Método para crear un label de score
    private Label crearLabelScore(String texto) {
        Label label = new Label(texto);
        label.setTextFill(javafx.scene.paint.Color.web("#7A4E28"));
        label.setFont(Font.font(26));
        return label;
    }
}
