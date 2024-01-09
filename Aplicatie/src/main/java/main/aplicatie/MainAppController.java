package main.aplicatie;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Controller-ul ferestrei principale a aplicatiei.
 *
 */

public class MainAppController{

    @FXML
    public RadioButton option1, option2, option3;
    @FXML
    public Label question, questionNO;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Button submitButton;
    private ToggleGroup options;
    private int correctAnswersNO = 0, questionIndex = 0, totalCorrectAnswers = 0, incorrectAnswersNO = 0, playerFinished = 0;

    private final List<String> questions = new ArrayList<>();
    private final List<String> answers = new ArrayList<>();
    private final List<String> correctAnswer = new ArrayList<>();

    /**
     *
     * Functie prin care se trece la fereastra principala a aplicatiei la fereastra de meniu.
     *
     */
    public void openMainMenuWindow(){
        Stage MainAppStage = (Stage) submitButton.getScene().getWindow();
        MainAppStage.close();
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
     * Initializarea datelor, incarcarea intrebarilor si a raspunsurilor in memorie, precum si inceperea cronometrului
     * pentru bara de progres.
     *
     */
    @FXML
    public void initialize(){
        options = new ToggleGroup();

        counter();

        RadioButton difficulty = (RadioButton) MainMenuController.difficultyButtons.getSelectedToggle();
        RadioButton genre = (RadioButton) MainMenuController.genreButtons.getSelectedToggle();
        loadQuestions(difficulty.getText(), genre.getText());

        option1.setToggleGroup(options);
        option2.setToggleGroup(options);
        option3.setToggleGroup(options);

        updatePannel();
    }

    /**
     * Functie care initializeaza interactiunea barii de progres si arunca o fereastra pop-up in cazul in care timpul
     * s-a scurs
     */
    public void counter(){
        Task<Void> task = createTask();
        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();

        task.setOnSucceeded(event -> {
            if (playerFinished == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare");
                alert.setHeaderText(null);

                alert.setContentText("Ai depasit limita de timp! Ai pierdut!");

                alert.showAndWait();
                openMainMenuWindow();
            }
        });
    }

    /**
     *
     * Functie care actualizeaza bara de progres folosind un fir de executie separat.
     *
     */
    private Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                int duration = 60;
                for (int seconds = 0; seconds <= duration; seconds++) {
                    updateProgress(seconds, duration);
                    Thread.sleep(1000);

                    if (playerFinished == 1){
                        break;
                    }
                }
                return null;
            }
        };
    }

    /**
     *
     * <p>
     *      Functie care actualizeaza panoul pe care se afla intrebarea, raspunsurile si progressbar-ul.
     * </p>
     * <p>
     *      In cazul in care se ajunge la numarul maxim de intrebari, se va afisa o fereastra Popup care comunica
     *      scorul utilizatorului si inchide aplicatia.
     * </p>
     *
     */
    public void updatePannel(){
        if (questionIndex == 5){
            questionNO.setText("Intrebarea NR: " + questionIndex);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Trivia - Rezultat");
            alert.setHeaderText(null);

            alert.setContentText("Felicitari, ai un total de " + totalCorrectAnswers + " puncte!");

            alert.showAndWait();

            // Se marcheza faptul ca player-ul a terminat pentru a opri executia thread-ului pentru bara de proges
            playerFinished = 1;
            openMainMenuWindow();
            return;
        }

        question.setText(questions.get(questionIndex++));
        option1.setText(answers.get(incorrectAnswersNO++));
        option2.setText(answers.get(incorrectAnswersNO++));
        option3.setText(correctAnswer.get(correctAnswersNO));

        questionNO.setText("Intrebarea NR: " + questionIndex);
    }

    /**
     *
     * Functie care se ocupa de tratarea evenimentului apasarii butonului "Submit", iar in cazul in care raspunsul nu
     * este valid, se va afisa o fereastra Popup de atentionare a utilizatorului.
     *
     */
    @FXML
    public void onSubmitButtonPress(){
        try {
            String userAnswer = ((RadioButton) options.getSelectedToggle()).getText();

            if (userAnswer.equals(correctAnswer.get(correctAnswersNO++))) {
                totalCorrectAnswers++;
            }

            updatePannel();
        }catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);

            alert.setContentText("Selectati o varianta!");

            alert.showAndWait();
        }
    }

    /**
     *
     * Functie care incarca intrebarile si raspunsurile din fisiere text in memorie.
     *
     * @param difficulty Comunica dificultatea aleasa de utilizator.
     * @param genre Comunica domeniul ales de utilizator.
     *
     */
    public void loadQuestions(String difficulty, String genre) {
        String FilePath = "src\\main\\resources\\questions\\" + difficulty.toLowerCase() + '\\' + genre.toLowerCase() + ".txt";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FilePath))) {
            String line;
            String[] questionAndAnswers;

            while ((line = bufferedReader.readLine()) != null) {
                questionAndAnswers = line.split(":");
                questions.add(questionAndAnswers[0]);
                answers.add(questionAndAnswers[2]);
                answers.add(questionAndAnswers[3]);
                correctAnswer.add(questionAndAnswers[1]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
