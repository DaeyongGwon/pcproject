package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserInfoController {

    private final UserService userService = new UserService();

    @FXML
    private Label nicknameLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label emailLabel;

    private String targetNickname; // 비교할 대상 닉네임

    public void setTargetNickname(String targetNickname) {
        this.targetNickname = targetNickname;
    }

    public void initialize() {
        loadUserData();
    }

    private void loadUserData() {
        userService.loadUserData(); // 데이터베이스에서 사용자 정보 로드
        UserDto user = userService.getUserByNickname(targetNickname); // 닉네임으로 사용자 정보 가져오기
        if (user != null) {
            // 사용자 정보가 존재하면 화면에 표시
            nicknameLabel.setText(user.getNickname());
            nameLabel.setText(user.getName());
            birthdayLabel.setText(user.getBirthday().toString()); // 적절한 형식으로 변환 필요
            addressLabel.setText(user.getAddress());
            phoneNumberLabel.setText(user.getPhonenumber());
            emailLabel.setText(user.getEmail());
        } else {
            // 사용자 정보가 존재하지 않으면 해당하는 사용자가 없다는 메시지 표시
            nicknameLabel.setText("해당하는 사용자가 없습니다.");
        }
    }
}
