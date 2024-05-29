package com.pcroom.pcproject.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignIn extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        Parent root = loader.load();

        HBox hbox = (HBox) root;
        VBox leftVBox = (VBox) hbox.lookup("#leftVBox");
        VBox rightVBox = (VBox) hbox.lookup("#rightVBox");

        // Binding the width properties to ensure they take up half of the HBox
        leftVBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.5));
        rightVBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.5));

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("로그인");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
