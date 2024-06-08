package com.pcroom.pcproject.util;

import com.pcroom.pcproject.model.dto.SeatDto;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.List;

public class SeatUtils {
    // 좌석 상태를 업데이트하는 메서드
    public static void updateSeatStatus(List<SeatDto> seatList, boolean[] seatStatus, GridPane seatGrid) {
        for (int i = 0; i < seatList.size(); i++) {
            SeatDto seat = seatList.get(i);
            boolean isActive = seat.getActive() == 1;
            if (isActive) {
                seatStatus[i] = false; // 활성화되었고 예약되지 않은 좌석은 사용 가능
            } else {
                seatStatus[i] = true; // 예약되었거나 비활성화된 좌석은 사용 불가능
            }
            // 좌석 버튼 스타일 및 활성화 여부 업데이트
            Button seatButton = getSeatButton(seat.getSeatNumber(), seatGrid);
            if (seatButton != null) {
                if (seatStatus[i]) {
                    seatButton.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    seatButton.setDisable(true);
                } else {
                    seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    seatButton.setDisable(false);
                }
            }
        }
    }

    // 좌석 번호에 해당하는 좌석 버튼을 반환하는 메서드
    public static Button getSeatButton(int seatNumber, GridPane seatGrid) {
        ObservableList<Node> seats = seatGrid.getChildren();
        for (Node node : seats) {
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                if (seatButton.getText().equals(String.valueOf(seatNumber))) {
                    return seatButton;
                }
            }
        }
        return null;
    }
}
