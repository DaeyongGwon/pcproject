// ManagerPage.java
package com.pcroom.pcproject.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ManagerPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManagerPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            primaryStage.setTitle("Manager Page");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // 크기 변경 금지
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
