package main.aplicatie;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 *  Controller-ul ferestrei de LogIn.
 *
 */

public class LogInController {

    @FXML
    private PasswordField Password;
    @FXML
    private TextField Username;

    /**
     *
     * Functie prin care se trece la fereastra de meniu a aplicatiei, in acelasi timp inchizandu-se fereastra de Log In.
     *
     */
    public void openMainMenuWindow(){
        Stage LoginStage = (Stage) Password.getScene().getWindow();
        LoginStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuWindow.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Triviador - Meniu");
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     *  Functie care afiseaza o fereastra Popup la introducerea datelor de utilizator.
     *
     *  @param flag Parametru transmis din functia onLogInButtonClick. In functie de valoarea acestui parametru, functia
     *             va afisa o fereastra Popup care comunica daca datele introduse de utilizator sunt sa nu corecte.
     */
    public void Validation(boolean flag) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Validare");
        alert.setHeaderText(null);

        if (flag) {
            alert.setContentText("Autentificare reușită!");
        } else {
            alert.setContentText("Date de autentificare incorecte. Încercați din nou.");
        }

        alert.showAndWait();
    }

    /**
     *
     *  Functie care se ocupa de tratarea evenimentului apasarii butonului "Log In" din fereastra de LogIn. Se
     *  efectueaza o cautare in fisierul "UserCredentials" si pe baza acestei cautari se permite accesul in aplicatie a
     *  utilizatorului apelandu-se functia "openMainMenuWindow".
     *
     */
    @FXML
    public void onLogInButtonClick() {

        String FilePath = "src\\main\\resources\\UserCredentials.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FilePath))) {
            String line;
            String[] credentials;
            boolean flag = false;

            while ((line = bufferedReader.readLine()) != null && !flag) {
                credentials = line.split("\\s+");
                if (credentials[1].equals(Password.getText()) && credentials[0].equals(Username.getText())) {
                    flag = true;
                }
            }
            Validation(flag);

            if (flag){
                openMainMenuWindow();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Functie care se ocupa de tratarea evenimentului apasarii butonului "Exit" din fereastra de LogIn.
     *
     */
    @FXML
    public void onExitButtonClick() {
        Platform.exit();
    }
}