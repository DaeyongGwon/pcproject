package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dto.SeatDto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainPageController {
    // 좌석 상태 배열
    private boolean[] seatStatus;

    private final SeatDao seatDao = new SeatDao();

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
            try {
                int rows = 8; // 행 수
                int cols = 8; // 열 수

                createSeats(rows, cols);
                updateSeatStatus(); // 좌석 상태 업데이트 메서드 호출

                // 좌석 상태 배열 초기화
                seatStatus = new boolean[rows * cols];
                for (int i = 0; i < seatStatus.length; i++) {
                    seatStatus[i] = Math.random() < 0.5; // 50% 확률로 예약 가능
                }

                // 현재 로그인 중인 유저의 토큰을 이용하여 사용자 이름 가져오기
                String loggedInUser = SignInController.getToken();
                // 나머지 코드는 동일
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSeatStatus() throws SQLException {
        List<SeatDto> seatList = seatDao.getAllSeats();
        seatStatus = new boolean[seatList.size()]; // 좌석 수에 맞게 배열 크기 설정
        for (int i = 0; i < seatList.size(); i++) {
            SeatDto seat = seatList.get(i);
            boolean isActive = seat.getActive() == 1;
            if (isActive) {
                seatStatus[i] = false; // 활성화되었고 예약되지 않은 좌석은 사용 가능
            } else {
                seatStatus[i] = true; // 예약되었거나 비활성화된 좌석은 사용 불가능
            }
            // 좌석 버튼 스타일 및 활성화 여부 업데이트
            Button seatButton = getSeatButton(seat.getSeatNumber());
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
    private Button getSeatButton(int seatNumber) {
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

    private void createSeats(int rows, int cols) {
        List<SeatDto> seatList = seatDao.getAllSeats();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int seatNumber = i * cols + j + 1;
                Button seatButton = new Button(String.valueOf(seatNumber));
                seatButton.setOnAction(event -> handleSeatButtonClick(seatNumber)); // 이벤트 핸들러 등록
                seatButton.setPrefSize(50, 50);

                // DB에서 해당 좌석 정보 찾기
                SeatDto seat = findSeat(seatList, seatNumber);
                if (seat != null && seat.getActive() == 1) {
                    seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                } else {
                    seatButton.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    seatButton.setDisable(true); // 비활성화된 좌석은 비활성화
                }

                seatGrid.add(seatButton, j, i);
            }
        }
    }

    // 좌석 번호에 해당하는 좌석 정보를 찾는 메서드
    private SeatDto findSeat(List<SeatDto> seatList, int seatNumber) {
        for (SeatDto seat : seatList) {
            if (seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }
        return null;
    }

    @FXML
    private void handleSeatButtonClick(int seatNumber) {
        try {
            // DB에서 해당 좌석 정보 가져오기
            SeatDto seat = seatDao.getSeatByNumber(seatNumber);
            if (seat != null && seat.getActive() == 1) {
                // 좌석이 활성화된 상태이면 좌석 상세 정보 화면으로 이동
                FXMLLoader seatDetailsLoader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/SeatDetails.fxml"));
                Parent seatDetailsRoot = seatDetailsLoader.load();
                SeatDetailsController seatDetailsController = seatDetailsLoader.getController();

                boolean isReserved = seat.getActive() == 1; // 좌석 예약 상태 가져오기
                String status = isReserved ? "좌석 사용 가능" : "사용 중인 좌석입니다.";
                String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String loggedInUser = SignInController.getToken(); // 로그인 중인 유저 이름 가져오기

                seatDetailsController.setSeatDetails(String.valueOf(seatNumber), status, startTime, loggedInUser);

                // 콘솔에 좌석 상태 출력
                System.out.println("Selected seat: " + seatNumber + " - " + status);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("좌석 상세 정보");
                stage.setScene(new Scene(seatDetailsRoot));
                stage.show();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
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

        // 좌석 버튼들 비활성화
        disableSeatButtons();
    }

    private void disableSeatButtons() {
        ObservableList<Node> seats = seatGrid.getChildren();
        for (Node node : seats) {
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                seatButton.setDisable(true);
            }
        }
    }
    // 좌석 번호를 GridPane에 추가하는 메서드
    public void addSeatLabel(int row, int col, String seatNumber) {
        Label label = new Label(seatNumber);
        seatGrid.add(label, col, row);
    }
}
