module com.pcroom.pcproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.oracle.database.jdbc;
    requires jdk.compiler;

    opens com.pcroom.pcproject.controller to javafx.fxml;
    opens com.pcroom.pcproject.view to javafx.fxml;
    opens com.pcroom.pcproject.model to javafx.fxml;

    exports com.pcroom.pcproject.view;
    exports com.pcroom.pcproject.controller;
    exports com.pcroom.pcproject.model;

}
