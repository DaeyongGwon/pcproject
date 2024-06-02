package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.view.SignIn;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.pcroom.pcproject.view.MainPage;

public class SignInController {

    public Button signUpButton;
    public Button SignInButton;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage primaryStage; // 메인 페이지로 이동하기 위해 Stage를 저장할 변수
    private static String token; // 토큰을 저장할 변수

    public static void moveToSignInPage() {
        // 로그인 페이지를 띄우는 코드
        SignIn signIn = new SignIn();
        Stage signInStage = new Stage();
        try {
            signIn.start(signInStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleSignInButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 여기서 유저 정보 확인 후 로그인 처리
        if (authenticate(username, password)) {
            // 로그인 성공
            showAlert("로그인 성공", "환영합니다!");
            // 토큰 발급
            String token = generateToken(username); // 예시: 간단하게 사용자 이름을 토큰으로 사용
            // 토큰 저장
            saveToken(token);
            // 메인 페이지로 이동
            moveToMainPage();
        } else {
            // 로그인 실패
            showAlert("로그인 실패", "아이디 또는 패스워드가 잘못되었습니다.");
        }
    }

    private boolean authenticate(String username, String password) {
        // 하드코딩된 유저 정보와 비교하여 인증 처리
        return username.equals("user") && password.equals("password");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void moveToMainPage() {
        // MainPage를 띄우는 코드
        MainPage mainPage = new MainPage();
        Stage mainStage = new Stage();
        try {
            mainPage.start(mainStage);
            primaryStage.close(); // 현재 로그인 창 닫기
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String generateToken(String username) {
        // 간단히 사용자 이름을 토큰으로 사용
        return username;
    }

    public static String getToken() {
        return token;
    }

    private void saveToken(String token) {
        // 토큰을 안전하게 저장 (예: 로컬 스토리지)
        // 여기서는 단순히 클래스 변수에 저장하는 예시를 사용
        // 실제 애플리케이션에서는 안전한 저장 방식을 사용해야 함
        SignInController.token = token;
    }
}
