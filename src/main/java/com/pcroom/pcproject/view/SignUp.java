package com.pcroom.pcproject.view;

import com.pcroom.pcproject.controller.SignUpController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();

        SignUpController controller = loader.getController();
        controller.setPrimaryStage(primaryStage); // Set the primaryStage

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("회원가입");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
