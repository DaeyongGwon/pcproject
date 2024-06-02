package com.pcroom.pcproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MainPageController {

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label loggedInUserLabel; // 로그인 중인 유저 아이디를 표시할 레이블

    @FXML
    public void initialize() {
        int rows = 8; // 행의 수
        int cols = 8; // 열의 수

        // 좌석 상태 예시 데이터 (예약 가능 여부)
        boolean[] seatStatus = new boolean[rows * cols];
        for (int i = 0; i < seatStatus.length; i++) {
            seatStatus[i] = Math.random() < 0.5; // 50% 확률로 예약 가능
        }

        // 좌석 번호판 생성
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int seatNumber = i * cols + j + 1;
                Button seat = new Button(String.valueOf(seatNumber));
                seat.setPrefSize(50, 50);
                if (seatStatus[seatNumber - 1]) {
                    seat.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                } else {
                    seat.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                }
                seatGrid.add(seat, j, i);
            }
        }

        // 토큰을 사용하여 로그인 중인 유저 아이디를 표시
        String loggedInUser = SignInController.getToken(); // 토큰을 사용하여 로그인 중인 유저 아이디를 가져옴
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            loggedInUserLabel.setText("현재 로그인 중인 유저: " + loggedInUser);
        } else {
            loggedInUserLabel.setText("로그인 중인 유저 없음");
        }
    }

    public void loginPage(ActionEvent actionEvent) {
        // 로그인 페이지로 이동하기
        SignInController.moveToSignInPage();
        // 현재 창 닫기
        seatGrid.getScene().getWindow().hide();
    }
}
