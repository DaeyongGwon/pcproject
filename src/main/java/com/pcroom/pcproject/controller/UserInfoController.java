package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserInfoController {

    @FXML
    private Label nicknameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phonenumberLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Button closeButton; // 닫기 버튼 추가

    // UserService 클래스를 사용하여 사용자 정보를 가져옵니다.
    private final UserService userService = new UserService();

    public void initialize() {
        // 토큰 값을 사용하여 현재 로그인한 사용자의 정보를 가져옵니다.
        String token = SignInController.getToken(); // 로그인 컨트롤러에서 토큰을 가져오는 메서드 호출
        // UserService를 사용하여 토큰을 이용해 사용자 정보를 가져옵니다.
        UserDto username = userService.getUserByToken(token);
        // 사용자 정보를 레이블에 표시합니다.
        nicknameLabel.setText(username.getNickname());
        nameLabel.setText(username.getName());
        emailLabel.setText(username.getEmail());
        birthdayLabel.setText(username.getBirthday().toString());
        addressLabel.setText(username.getAddress());
        phonenumberLabel.setText(username.getPhonenumber());

        // 시작 시간을 표시합니다.
        startTimeLabel.setText(formatTime(username.getStartTime()));

        // 닫기 버튼에 이벤트 핸들러를 추가합니다.
        closeButton.setOnAction(this::handleCloseButtonAction);
    }

    // 닫기 버튼 이벤트 핸들러 메서드
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        // 현재 창을 닫습니다.
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private String formatTime(String startTime) {
        if (startTime == null || startTime.isEmpty()) {
            return "No start time"; // startTime이 null이거나 빈 문자열인 경우 처리
        }

        try {
            // LocalDateTime.parse를 사용하여 문자열을 LocalDateTime 객체로 변환
            LocalDateTime dateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // DateTimeFormatter를 사용하여 시간 부분만 형식화
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (DateTimeParseException e) {
            return "Invalid start time"; // 파싱 오류 발생 시 처리
        }
    }
}
