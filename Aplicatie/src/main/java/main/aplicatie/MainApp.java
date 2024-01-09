package main.aplicatie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 *
 * Clasa principala de unde se initializeaza fereastra de LogIn
 *
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource("LoginWindow.fxml")));
            Scene scene = new Scene(root, 476, 297);
            stage.setTitle("Triviador - Login");
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}