module com.ruandev.taskflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires transitive mysql.connector.j;


    opens com.ruandev.taskflow to javafx.fxml;
    exports com.ruandev.taskflow;
    exports com.ruandev.taskflow.fx;
    opens com.ruandev.taskflow.fx to javafx.fxml;
}