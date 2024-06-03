package com.pcroom.pcproject.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainPageController {

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label loggedInUserLabel; // 로그인 중인 유저 아이디를 표시할 레이블
    @FXML
    private HBox loggedOut; // 로그아웃 상태의 버튼들
    @FXML
    private HBox loggedIn; // 로그인 상태의 버튼들

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            createSeats(8, 8);

            // 토큰을 사용하여 로그인 중인 유저 아이디를 표시
            String loggedInUser = SignInController.getToken(); // 토큰을 사용하여 로그인 중인 유저 아이디를 가져옴
            System.out.println("Initialize called. Logged in user: " + loggedInUser);
            if (loggedInUser != null && !loggedInUser.isEmpty()) {
                loggedInUserLabel.setText("현재 로그인 중인 유저: " + loggedInUser);
                loggedOut.setVisible(false);
                loggedOut.setManaged(false);  // 레이아웃에서 제외
                loggedIn.setVisible(true);
                loggedIn.setManaged(true);  // 레이아웃에 포함
            } else {
                loggedInUserLabel.setText("로그인 중인 유저 없음");
                loggedOut.setVisible(true);
                loggedOut.setManaged(true);  // 레이아웃에 포함
                loggedIn.setVisible(false);
                loggedIn.setManaged(false);  // 레이아웃에서 제외
            }
        });
    }

    private void createSeats(int rows, int cols) {
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
    }

    @FXML
    public void loginPage(ActionEvent actionEvent) {
        // 로그인 페이지로 이동하기
        SignInController.moveToSignInPage();
        // 현재 창 닫기
        seatGrid.getScene().getWindow().hide();
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        // 로그아웃 처리 (토큰 제거 등)
        SignInController.logout();
        // UI 업데이트
        loggedInUserLabel.setText("로그인 중인 유저 없음");
        loggedOut.setVisible(true);
        loggedOut.setManaged(true);  // 레이아웃에 포함
        loggedIn.setVisible(false);
        loggedIn.setManaged(false);  // 레이아웃에서 제외
    }
}
