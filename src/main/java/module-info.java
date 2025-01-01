module example.projectsigma6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens example.projectsigma6 to javafx.fxml;
    exports example.projectsigma6;
}