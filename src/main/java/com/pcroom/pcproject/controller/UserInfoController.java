package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
    private Label startTimeLabel; // startTimeLabel 추가

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

        // UserDao에서 사용자의 Start_Time 값을 가져옵니다.
        String startTime = userService.getUserStartTime(username.getNickname());
        // startTimeLabel에 설정합니다.
        startTimeLabel.setText(startTime);

    }
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        // 현재 창을 닫는 로직 예시
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
