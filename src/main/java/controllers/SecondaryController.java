package controllers;

import java.io.IOException;

import com.example.App;

import javafx.fxml.FXML;

//Clase para controlar la pantalla secundaria
public class SecondaryController {

    //Método para cambiar a la pantalla primaria
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
