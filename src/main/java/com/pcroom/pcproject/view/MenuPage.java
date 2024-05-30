package com.pcroom.pcproject.view;

import com.pcroom.pcproject.controller.MenuPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("MenuPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            primaryStage.setTitle("메뉴 페이지");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Get the controller instance from the FXMLLoader
            MenuPageController controller = fxmlLoader.getController();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}