package main.aplicatie;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * Controller-ul meniului principal al aplicatiei.
 *
 */
public class MainMenuController{
    @FXML
    private RadioButton easyButton,hardButton,HistoryButton,GeographyButton,VideoGamesButton;

    public static ToggleGroup difficultyButtons, genreButtons;

    /**
     * <p>
     *      Aceasta functie initalizeaza butoanele pentru selectarea dificultatii si a domeniului din care sa faca parte
     *      intrebarile, totodata grupandu-le.
     * </p>
     *
     * <p>
     *      Se intializeaza butonul "Usor" si "Istorie" ca default la deschiderea ferestrei.
     * </p>
     */
    @FXML
    public void initialize(){
        difficultyButtons = new ToggleGroup();
        genreButtons = new ToggleGroup();

        easyButton.setToggleGroup(difficultyButtons);
        hardButton.setToggleGroup(difficultyButtons);

        HistoryButton.setToggleGroup(genreButtons);
        GeographyButton.setToggleGroup(genreButtons);
        VideoGamesButton.setToggleGroup(genreButtons);

        easyButton.setSelected(true);
        HistoryButton.setSelected(true);

    }

    /**
     *
     * Functie prin care se trece din fereastra de meniu a aplicatiei in aplicatia propriu-zisa. Tranzitia facandu-se
     * prin inchiderea ferestrei precedente.
     *
     */
    private void openMainAppWindow(){
        Stage MainMenuStage = (Stage) easyButton.getScene().getWindow();
        MainMenuStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainAppWindow.fxml"));
            Stage Stage = new Stage();
            Stage.setScene(new Scene(loader.load()));
            Stage.setTitle("Triviador");
            Stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *      Functie care se ocupa de tratarea evenimentului apasarii butonului de "Start" din cadrul ferestrei meniului.
     * </p>
     * <p>
     *      In cazul in care utilizatorul nu selecteaza o dificultate sau gen inainte de a apasa pe butonul "Start" este
     *      afisat un Popup pentru a-l atentiona.
     * </p>
     */
    @FXML
    public void onStartButtonClick(){
        RadioButton difficultyRadioButton = (RadioButton) difficultyButtons.getSelectedToggle();
        RadioButton genreRadioButton = (RadioButton) genreButtons.getSelectedToggle();
        if (difficultyRadioButton != null && genreRadioButton != null) {
            openMainAppWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);

            alert.setContentText("Selectati o dificultate si un gen inainte de a incepe!");

            alert.showAndWait();
        }
    }
}
