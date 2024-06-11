package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.TimeDto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.time.Instant;

public class ChargePageController {
    public VBox infoBox;
    @FXML
    public VBox chooseBox;
    @FXML
    public Button closeButton;
    @FXML
    public Button chooseBoxButton;
    @FXML
    public Label chargeNameLabel;
    @FXML
    private Label chargeTimeLabel;
    @FXML
    private Label chargePriceLabel;
    private int selectedPrice;
    private String selectedTime;

    @FXML
    public void initialize() {
        // 로그인한 유저의 이름
        chargeNameLabel.setText(SignInController.getToken() + " 님 환영합니다.");
    }

    // 이벤트 핸들러
    public void onChargeBoxClick(MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource();

        // 선택된 VBox에서 충전 정보를 가져와서 Label에 표시
        Label timeLabel = (Label) clickedBox.lookup("#timeLabel");
        Label priceLabel = (Label) clickedBox.lookup("#priceLabel");
        System.out.println("time label : " + timeLabel);
        System.out.println("price label : " + priceLabel);
        selectedPrice = Integer.parseInt(priceLabel.getText().replaceAll("\\D+", ""));
        selectedTime = timeLabel.getText();
        if (timeLabel != null && priceLabel != null) {
            String time = timeLabel.getText();
            String price = priceLabel.getText();
            // 우측 Label 업데이트
            infoBox.visibleProperty().setValue(true);
            infoBox.managedProperty().setValue(true);
            // 요금제 선택 숨기기
            chooseBox.visibleProperty().setValue(false);
            chooseBox.managedProperty().setValue(false);
            // 요금제 선택 정보 표시
            chargePriceLabel.setText(price);
            chargeTimeLabel.setText("(" + time + ")");
        }
    }

    @FXML
    private void onChooseBoxClick(MouseEvent event) {
        if (selectedPrice > 0 && selectedTime != null) {
            TimeDao timeDao = new TimeDao();
            int userId = getLoggedInUserId();
            int selectedTimeInMinutes = convertTimeToMinutes(selectedTime);

            // 기존 시간 조회
            TimeDto existingTimeDto = timeDao.getTimeByUserId(userId);
            if (existingTimeDto != null) {
                // 기존 시간이 있으면 업데이트
                int updatedRemainingTime = existingTimeDto.getRemainingTime() + selectedTimeInMinutes;
                existingTimeDto.setRemainingTime(updatedRemainingTime);
                timeDao.updateTime(existingTimeDto);
            } else {
                // 기존 시간이 없으면 삽입
                TimeDto newTimeDto = new TimeDto();
                newTimeDto.setId(userId);
                newTimeDto.setRemainingTime(selectedTimeInMinutes);
                newTimeDto.setLastChecked(Timestamp.from(Instant.now()));
                newTimeDto.setStartTime(Timestamp.from(Instant.now())); // 시작 시간은 현재로 설정
                timeDao.insertTime(newTimeDto);
            }

            // 요금 충전 완료 알림 표시
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("충전 완료");
            alert.setHeaderText(null);
            alert.setContentText("요금 충전이 완료되었습니다: " + selectedPrice + "원, 시간: " + selectedTime);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // 충전 창 닫기
                    closeButton.getScene().getWindow().hide();
                }
            });

            // 요금 충전 후 처리 로직 (예: 알림 표시, 화면 갱신 등)
            System.out.println("요금 충전 완료: " + selectedPrice + "원, 시간: " + selectedTime);
        }
    }

    // 우측 라벨을 클릭하는 이벤트 핸들러
    @FXML
    private void onInfoBoxClick(MouseEvent event) {
        // onChooseBoxClick 메서드 호출
        onChooseBoxClick(event);
    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private int getLoggedInUserId() {
        // 로그인한 사용자의 ID를 가져오는 로직
        UserDao userDao = new UserDao();
        String token = SignInController.getToken();
        System.out.println("로그인한 사용자의 ID: " + token + " ID : " + userDao.getUserIdByNickname(token));
        return userDao.getUserIdByNickname(token);
    }

    // 닫기 버튼 클릭 시 페이지 닫기
    public void onCloseButtonClick(MouseEvent event) {
        // 숨기기 말고 닫기
        closeButton.getScene().getWindow().hide();
    }
}
