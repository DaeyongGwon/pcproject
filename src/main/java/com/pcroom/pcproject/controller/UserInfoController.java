package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
// 사용자 정보를 표시하고, 사용 시간을 계산하여 표시, 사용자 정보와 사용 시간을 화면에 표시, 공동 작성
public class UserInfoController {
    @FXML
    public Label usingTimeLabel;
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

    // 사용자 정보를 가져와 UI에 표시하고, 사용 시간을 계산하여 표시, 초기화 메서드, 공동 작성
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

        // UserDao의 현재시간 - 시작시간 = 사용시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalTime startDateTime = LocalTime.parse(startTime, formatter);
        Duration usingTime = Duration.between(startDateTime, currentTime);


        // 사용 시간을 시, 분, 초로 변환하여 문자열로 표시
        long hours = usingTime.toHours();
        long minutes = usingTime.minusHours(hours).toMinutes();
        String usingTimeString = String.format("%02d시%02d분", hours, minutes);
        String startTimeString = startDateTime.format(DateTimeFormatter.ofPattern("HH시mm분"));

        startTimeLabel.setText(startTimeString); // 시작 시간을 레이블에 설정합니다.
        // 사용 시간을 레이블에 설정합니다.
        usingTimeLabel.setText(usingTimeString);

    }

    // 닫기 버튼 클릭 시 호출되는 메서드, 김진석 작성
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        // 현재 창을 닫는 로직
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
