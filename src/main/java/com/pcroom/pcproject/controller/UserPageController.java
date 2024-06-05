package com.pcroom.pcproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserPageController {
    @FXML
    private Label seatNumberLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label remainingTimeLabel;

    @FXML
    private void orderMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MenuPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Menu Page");
            stage.setScene(new Scene(root));
            stage.show();

            // 현재 창 닫기
            // Stage currentStage = (Stage) seatNumberLabel.getScene().getWindow();
            // currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserPageDetails(String seatNumber, String status, String startTime) {
        seatNumberLabel.setText(seatNumber);
        usernameLabel.setText(status);
        startTimeLabel.setText(startTime);
        // 남은 시간은 별도로 계산하여 설정
    }

    private String formatTime(String startTime) {
        // LocalDateTime.parse를 사용하여 문자열을 LocalDateTime 객체로 변환
        LocalDateTime dateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // DateTimeFormatter를 사용하여 시간 부분만 형식화
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void setSeatNumber(String seatNumber) {
        seatNumberLabel.setText(seatNumber.replace("좌석번호:", ""));
    }

    @FXML
    private void handleClose(ActionEvent event) {
        // 사용 종료 확인 창 표시
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("종료 확인");
        alert.setHeaderText(null);
        alert.setContentText("정말로 사용을 종료하시겠습니까?");

        // 사용자가 확인 또는 취소를 선택할 때까지 대기
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // 사용자가 확인을 선택한 경우 현재 창 닫기
                Stage stage = (Stage) seatNumberLabel.getScene().getWindow();
                stage.close();
            }
        });
    }

    @FXML
    private void showUserInfo(ActionEvent event) {
        try {
            // 새 창을 열기 위한 FXMLLoader 생성
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/UserInfo.fxml"));
            Parent root = loader.load();

            // 새 창의 컨트롤러를 가져와서 사용자 정보를 설정
            UserInfoController controller = loader.getController();
            // 여기에서 사용자 정보를 설정할 수 있습니다.
            // 예: controller.setUserInfo(userData);

            // 새로운 스테이지 생성 및 설정
            Stage stage = new Stage();
            stage.setTitle("사용자 정보");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
