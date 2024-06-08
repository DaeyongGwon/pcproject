package com.pcroom.pcproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Screen;
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
    private Label startTimeLabel;

    @FXML
    private Label usernameLabel;

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

                // 창 크기 조정 불가능으로 설정
                stage.setResizable(false);

                // 창을 먼저 표시
                stage.show();

                // 화면 크기 가져오기
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                // 창 크기 계산 후 위치 설정
                stage.setX(screenBounds.getWidth() - stage.getWidth());
                stage.setY(0);

                // 현재 창 닫기
                Stage currentStage = (Stage) statusLabel.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSeatDetails(String seatNumber, String status, String startTime, String username) {
        seatLabel.setText(seatNumber);
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
        // 현재 창 닫기
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();

        // 메인 페이지를 보여주는 메서드 호출
        showMainPage();
    }

    private void showMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
            Parent root = loader.load();

            // 메인 페이지 컨트롤러에서 초기화 메서드를 호출하여 페이지를 초기화
            MainPageController mainPageController = loader.getController();
            mainPageController.initialize();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
}
