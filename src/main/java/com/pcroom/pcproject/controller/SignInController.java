package com.pcroom.pcproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignInController {

    @FXML
    private TextField ID;
    @FXML
    private TextField PW;

    @FXML
    private Button signUpButton;

    @FXML
    private void initialize() {
        signUpButton.setOnAction(event -> showSignUpWindow());
    }

    @FXML
    private void handleSignInButtonAction() {
        String id = ID.getText();
        String pw = PW.getText();

        if (id.isEmpty() || pw.isEmpty()) {
            showAlert(AlertType.ERROR, "로그인 에러", "아이디와 비밀번호를 입력하세요!");
            return;
        }

        if (validateLogin(id, pw)) {
            showAlert(AlertType.INFORMATION, "로그인 성공!", "환영합니다 " + id + "님");
        } else {
            showAlert(AlertType.ERROR, "로그인 실패!", "아이디나 비밀번호가 틀렸습니다.");
        }
    }

    private boolean validateLogin(String id, String pw) {
        boolean isValid = false;
        String url = "jdbc:oracle:thin:@//localhost:1521/xe"; // Oracle DB 연결 URL
        String user = "pctest"; // Oracle DB 사용자 이름
        String password = "team6"; // Oracle DB 비밀번호

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM USER_T WHERE id = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, pw);

            ResultSet resultSet = statement.executeQuery();
            isValid = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private void showSignUpWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/SignUp.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("회원가입");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
