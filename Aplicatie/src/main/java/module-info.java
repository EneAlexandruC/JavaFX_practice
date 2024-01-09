module main.aplicatie {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.aplicatie to javafx.fxml;
    exports main.aplicatie;
}