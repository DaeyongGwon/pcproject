module com.pcroom.pcproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.pcroom.pcproject.controller to javafx.fxml;
    opens com.pcroom.pcproject.view to javafx.fxml;

    exports com.pcroom.pcproject.view;
    exports com.pcroom.pcproject.controller;

}
