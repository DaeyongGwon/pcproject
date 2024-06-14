package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.TimeDto;
import com.pcroom.pcproject.service.UserService;
import com.pcroom.pcproject.view.SignIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
// 사용자 로그인 기능을 제어, FXML과 연결된 UI 요소들을 관리하고, 로그인 처리 및 관련 기능을 수행, 권대용 작성
public class SignInController {
    private final UserService userService = new UserService();

    public Button signUpButton;
    public Button SignInButton;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage primaryStage; // 메인 페이지로 이동하기 위해 Stage를 저장할 변수
    private static String token; // 토큰을 저장할 변수

    // 로그인 페이지로 이동하는 정적 메서드, 권대용 작성
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
    // 주 Stage를 설정하는 메서드, 권대용 작성
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    // 로그인 버튼 클릭 시 호출되는 메서드, 입력된 사용자 정보를 확인하고, 로그인 처리를 수행, 권대용 작성
    @FXML
    private void handleSignInButtonAction() {
        String inputId = usernameField.getText();
        String inputPassword = passwordField.getText();

        // 여기서 유저 정보 확인 후 로그인 처리
        if (userService.authenticateUser(inputId, inputPassword)) {
            // 로그인 성공
            showAlert("로그인 성공", "환영합니다!");
            // 토큰 발급
            String token = generateToken(inputId); // 예시: 간단하게 사용자 이름을 토큰으로 사용
            // 토큰 저장
            userService.saveTokenToUser(inputId, token); // 수정된 부분
            saveToken(token);

            TimeDao timeDao = new TimeDao();
            UserDao userDao = new UserDao();
            int userId = userDao.getUserIdByNickname(token);
            TimeDto existingTimeDto = timeDao.getTimeByUserId(userId);
            if (existingTimeDto == null) {
                // 기존 시간이 없으면 삽입
                TimeDto newTimeDto = new TimeDto();
                newTimeDto.setId(userId);
                newTimeDto.setRemainingTime(0);
                newTimeDto.setLastChecked(Timestamp.from(Instant.now()));
                newTimeDto.setStartTime(Timestamp.from(Instant.now())); // 시작 시간은 현재로 설정
                timeDao.insertTime(newTimeDto);
            }
            // 메인 페이지로 이동
            moveToMainPage();
        } else {
            // 로그인 실패
            showAlert("로그인 실패", "아이디 또는 패스워드가 잘못되었습니다.");
        }
    }
    // Alert 창을 보여주는 메서드, 권대용 작성
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // 메인 페이지로 이동하는 메서드. 권대용 작성
    private void moveToMainPage() {
        try {
            if (primaryStage != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
                //화면 크기 400, 500으로 변경
                Scene scene = new Scene(loader.load(), 400, 500);
                Stage mainStage = new Stage();
                mainStage.setTitle("메인 페이지");
                mainStage.setScene(scene);
                mainStage.show();
                // 로그인 페이지 닫기
                primaryStage.close();
            } else {
                System.out.println("Primary stage is null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 간단한 토큰을 생성하는 메서드, 권대용 작성
    private String generateToken(String username) {
        // 간단히 사용자 이름을 토큰으로 사용
        return username;
    }

    public static String getToken() {
        return token;
    }
    // 토큰을 설정하는 메서드, 권대용 작성
    private void saveToken(String token) {
        // 토큰을 안전하게 저장 (예: 로컬 스토리지)
        // 여기서는 단순히 클래스 변수에 저장하는 예시를 사용
        // 실제 애플리케이션에서는 안전한 저장 방식을 사용해야 함
        SignInController.token = token;
    }

    // DB에 토큰 저장하는 메서드 추가, 권대용 작성
    private void saveTokenToDB(String username, String token) {
        // 사용자 이름과 토큰을 DB에 저장하는 코드를 여기에 추가
        userService.saveTokenToUser(username, token);
    }
    // 로그아웃 처리를 수행하는 정적 메서드, 권대용 작성
    public static void logout() {
        // 토큰을 null로 설정하여 로그아웃 처리
        token = null;
    }
    // 회원가입 페이지로 이동하는 메서드, 권대용 작성
    public void mobeTosignUpPage(ActionEvent actionEvent) {
        SignUpController.moveToSignUpPage();
    }
}
