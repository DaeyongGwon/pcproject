package com.pcroom.pcproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SeatChangeController {
    @FXML
    private Label currentSeatLabel;

    @FXML
    private TextField newSeatField;

    private String currentSeatNumber;

    public void setCurrentSeat(String seatNumber) {
        this.currentSeatNumber = seatNumber;
        currentSeatLabel.setText(seatNumber);
    }

    @FXML
    private void selectNewSeat(ActionEvent event) {
        String newSeatNumber = newSeatField.getText();
        if (!newSeatNumber.isEmpty()) {
            currentSeatNumber = newSeatNumber;
            currentSeatLabel.setText(newSeatNumber);
        }
    }

    @FXML
    private void confirmSeatChange(ActionEvent event) {
        // 변경된 좌석 정보를 처리하는 로직을 추가하세요.
        // 변경이 완료되면 이 창을 닫습니다.
        Stage stage = (Stage) currentSeatLabel.getScene().getWindow();
        stage.close();
    }
}
