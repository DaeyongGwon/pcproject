package com.pcroom.pcproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class SignUpController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;

    @FXML
    private void handleSignUpButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "회원가입 에러!", "아이디나 패스워드를 모두 입력해 주세요.");
            return;
        }

        if (signUpUser(username, password, name, age, address, phoneNumber, email)) {
            showAlert(Alert.AlertType.INFORMATION, "회원가입 완료", "환영합니다 " + name);
        } else {
            showAlert(Alert.AlertType.ERROR, "회원가입 실패", "사용자를 등록할 수 없습니다..");
        }
    }

    private boolean signUpUser(String username, String password, String name, int age, String address, String phoneNumber, String email) {
        boolean isSignedUp = false;
        String url = "jdbc:oracle:thin:@//localhost:1521/xe";
        String user = "pctest";
        String dbPassword = "team6";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
                String sql = "INSERT INTO USER_T (USERID, PASSWORD, NAME, AGE, ADDRESS, PHONENUMBER, EMAIL, REGISTRATIONDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, name);
                statement.setInt(4, age);
                statement.setString(5, address);
                statement.setString(6, phoneNumber);
                statement.setString(7, email);
                // 등록 날짜를 현재 시간으로 설정합니다
                statement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

                int rowsInserted = statement.executeUpdate();
                isSignedUp = rowsInserted > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSignedUp;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
