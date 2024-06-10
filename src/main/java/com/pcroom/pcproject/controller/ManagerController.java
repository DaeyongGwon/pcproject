package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.SeatDto;
import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.model.dto.SeatDto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.util.List;

public class ManagerController {

    @FXML
    private Label seatNumberLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private GridPane seatGrid;

    private SeatDao seatDao;

    private TimeDao timeDao;

    public ManagerController(SeatDao seatDao, TimeDao timeDao) {
        this.seatDao = seatDao;
        this.timeDao = timeDao;
    }

    public List<SeatDto> getAllSeats() {
        return seatDao.getAllSeats();
    }

    @FXML
    private void initialize() {
        // 좌석 상태를 업데이트하고 화면에 표시하는 메서드 호출
        updateSeatStatus();
    }

    @FXML
    private void refreshSeatStatus() {
        // 좌석 상태를 업데이트하고 화면에 표시하는 메서드 호출
        updateSeatStatus();
    }

    private void updateSeatStatus() {
        // DB에서 모든 좌석 정보 가져오기
        List<SeatDto> seatList = seatDao.getAllSeats();

        // 좌석 버튼을 동적으로 생성하여 GridPane에 추가
        for (SeatDto seat : seatList) {
            Button seatButton = new Button(Integer.toString(seat.getSeatNumber()));
            seatButton.setOnAction(event -> {
                // 좌석 버튼 클릭 시 해당 좌석의 정보 표시
                seatNumberLabel.setText(Integer.toString(seat.getSeatNumber()));
                try {
                    UserDto user = seatDao.getUserBySeatNumber(seat.getSeatNumber());
                    if (user != null) {
                        userIdLabel.setText(Integer.toString(user.getId()));
                    } else {
                        userIdLabel.setText("N/A");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            // 좌석 버튼의 스타일 설정
            if (seat.getActive() == 1) {
                seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            } else {
                seatButton.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                seatButton.setDisable(true); // 초기에는 모든 좌석이 비활성화 상태로 설정
            }

            seatGrid.add(seatButton, seat.getSeatNumber() % 5, seat.getSeatNumber() / 5);
        }
    }

    public void handleSeatClick(int seatNumber, GridPane seatGrid) {
        try {
            // 좌석 상태 확인
            SeatDto seat = seatDao.getSeatByNumber(seatNumber);
            if (seat != null && seat.getActive() == 0) {        // 1이 사용 중, 0이 빈 좌석
                // 사용 중인 좌석이면 해당 사용자 정보 가져오기
                try {
                    SeatDao.loadAssignment(seat.getSeatId());

                    // String userName = user != null ? "User " + user.getId() : "N/A";
                    // 좌석 정보 출력
                    System.out.println("Seat " + seatNumber + " is used by " + SeatDao.loadAssignment(seat.getSeatId()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // 사용 중이 아닌 좌석이면 Alert를 통해 빈 좌석임을 알림
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("빈 좌석, 사용 X");
                alert.setHeaderText(null);
                alert.setContentText("이 좌석은 비어 있습니다.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
