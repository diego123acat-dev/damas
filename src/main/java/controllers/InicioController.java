package controllers;

import java.io.IOException;

import com.example.App;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class InicioController {

    @FXML
    private void iniciarPartida() throws IOException {
        App.setRoot("tableroC");
    }

    @FXML
    private void salir() {
        Platform.exit();
    }
}
