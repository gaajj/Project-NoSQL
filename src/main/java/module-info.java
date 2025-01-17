module example.projectsigma6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.bson;
    requires java.xml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires jdk.compiler;


    opens org.example.projectsigma6 to javafx.fxml;
    exports org.example.projectsigma6;
    exports org.example.projectsigma6.tests;
    opens org.example.projectsigma6.controllers to javafx.fxml;
}