package com.pcroom.pcproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ChargePageController {
    public VBox infoBox;
    public VBox chooseBox;
    public Button closeButton;
    @FXML
    private Label chargeTimeLabel;

    @FXML
    private Label chargePriceLabel;

    // 이벤트 핸들러
    public void onChargeBoxClick(MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource();

        // 선택된 VBox에서 충전 정보를 가져와서 Label에 표시
        Label timeLabel = (Label) clickedBox.lookup("#timeLabel");
        Label priceLabel = (Label) clickedBox.lookup("#priceLabel");
        System.out.println("time label : "+ timeLabel);
        System.out.println("price label : " + priceLabel);

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
            chargePriceLabel.setText( price);
            chargeTimeLabel.setText("("+time+")");
        }
    }
    // 닫기 버튼 클릭시 페이지 닫기
    public void onCloseButtonClick(MouseEvent event) {
        // 숨기기 말고 닫기
        closeButton.getScene().getWindow().hide();
    }

}
