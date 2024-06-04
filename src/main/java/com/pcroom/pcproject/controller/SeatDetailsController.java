package com.pcroom.pcproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SeatDetailsController {
    @FXML
    private Label seatLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label startTimeLabel; // 시작 시간 레이블 추가

    @FXML
    private Label usernameLabel; // 사용자 이름 레이블 추가

    @FXML
    private Button moveToNewWindowButton;

    @FXML
    public void initialize() {
        // 새 창으로 이동하는 버튼의 초기 가시성 설정
        moveToNewWindowButton.setVisible(!"사용 중인 좌석 입니다.".equals(statusLabel.getText()));
    }

    @FXML
    private void moveToNewWindow(ActionEvent event) {
        if (!"사용 중인 좌석 입니다.".equals(statusLabel.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/UserPage.fxml"));
                Parent root = loader.load();

                UserPageController controller = loader.getController();
                controller.setUserPageDetails(seatLabel.getText(), usernameLabel.getText(), startTimeLabel.getText());

                Stage stage = new Stage();
                stage.setTitle("컴퓨터 사용 중");
                stage.setScene(new Scene(root));
                stage.show();

                // 현재 창 닫기
                Stage currentStage = (Stage) statusLabel.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSeatDetails(String seatNumber, String status, String startTime, String username) {
        seatLabel.setText("좌석 번호: " + seatNumber);
        statusLabel.setText("상태: " + status);
        startTimeLabel.setText(formatTime(startTime)); // 시작 시간 설정
        usernameLabel.setText(username); // 사용자 이름 설정

        // 좌석 상태가 변경될 때마다 새 창으로 이동하는 버튼의 가시성 업데이트
        moveToNewWindowButton.setVisible(!"사용 중인 좌석 입니다.".equals(status));
    }


    private String formatTime(String startTime) {
        // LocalDateTime.parse를 사용하여 문자열을 LocalDateTime 객체로 변환
        LocalDateTime dateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // DateTimeFormatter를 사용하여 시간 부분만 형식화
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }
}
