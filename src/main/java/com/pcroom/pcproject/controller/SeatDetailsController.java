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

public class SeatDetailsController {
    @FXML
    private Label seatLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button moveToNewWindowButton;

    @FXML
    public void initialize() {
        // 새 창으로 이동하는 버튼의 초기 가시성 설정
        moveToNewWindowButton.setVisible(!"사용 중".equals(statusLabel.getText()));
    }

    @FXML
    private void moveToNewWindow(ActionEvent event) {
        if (!"사용 중".equals(statusLabel.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/UserPage.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("새 창");
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

    public void setSeatDetails(String seatNumber, String status) {
        seatLabel.setText("좌석 번호: " + seatNumber);
        statusLabel.setText("상태: " + status);

        // 좌석 상태가 변경될 때마다 새 창으로 이동하는 버튼의 가시성 업데이트
        moveToNewWindowButton.setVisible(!"사용 중".equals(status));
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }
}
