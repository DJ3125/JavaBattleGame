module com.example.battlegame_10_22_23 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.battlegame_10_22_23 to javafx.fxml;
    exports com.example.battlegame_10_22_23;
}