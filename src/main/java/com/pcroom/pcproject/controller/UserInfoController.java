package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    }

}
