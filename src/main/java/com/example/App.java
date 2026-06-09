package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Clase principal de la aplicacion JavaFX
public class App extends Application {

    private static Scene scene;

    //Método para iniciar la ventana principal
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("inicio"), 1000, 720);
        stage.setScene(scene);
        stage.show();
    }

    //Método para cambiar la vista principal
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    //Método para cargar un archivo FXML
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //Método principal para ejecutar la aplicacion
    public static void main(String[] args) {
        launch();
    }
}
