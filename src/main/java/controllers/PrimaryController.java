package controllers;

import java.io.IOException;

import com.example.App;

import javafx.fxml.FXML;

//Clase para controlar la pantalla primaria
public class PrimaryController {

    //Método para cambiar a la pantalla secundaria
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("tableroC");
    }
}
