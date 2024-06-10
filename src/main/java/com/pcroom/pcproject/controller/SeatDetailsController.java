package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.SeatDto;
import com.pcroom.pcproject.util.SeatUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SeatDetailsController {
    private boolean[] seatStatus;
    private final SeatDao seatDao = new SeatDao();

    @FXML
    private Label seatLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button moveToNewWindowButton;

    @FXML
    public void initialize() {
        moveToNewWindowButton.setVisible(!"사용 중인 좌석 입니다.".equals(statusLabel.getText()));
    }

    public void updateSeatStatus() throws SQLException {
        List<SeatDto> seatList = seatDao.getAllSeats();
        seatStatus = new boolean[seatList.size()];
        SeatUtils.updateSeatStatus(seatList, seatStatus, seatGrid);
    }
    @FXML
    private void moveToNewWindow(ActionEvent event) {
        if (!"사용 중인 좌석 입니다.".equals(statusLabel.getText())) {
            try {
                // 좌석 번호 추출
                String seatNumberText = seatLabel.getText().replaceAll("[^\\d]", "");
                int seatNumber = Integer.parseInt(seatNumberText);

                // 좌석 상태 업데이트
                seatDao.updateSeatStatus(seatNumber, 0);

                // START_TIME 업데이트
                int userId = UserDao.getUserIdByNickname(SignInController.getToken());
                LocalDateTime startTime = LocalDateTime.now();
                TimeDao.updateStartTime(userId, Timestamp.valueOf(startTime));
                System.out.println("startTime: " + startTime);

                // 좌석 상태 업데이트 후에 좌석 목록 다시 로드
                updateSeatStatus();

                // 새 창 열기
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/UserPage.fxml"));
                Parent root = loader.load();

                UserPageController controller = loader.getController();
                controller.setUserPageDetails(seatLabel.getText(), usernameLabel.getText(), startTimeLabel.getText());

                // 좌석 버튼 찾기
                Button seatButton = SeatUtils.getSeatButton(seatNumber, seatGrid);
                if (seatButton != null) {
                    // 버튼 스타일 변경
                    seatButton.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    seatButton.setDisable(true);
                }

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
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void setSeatDetails(String seatNumber, String status, String startTime, String username) {
        seatLabel.setText("좌석 번호: " + seatNumber);
        statusLabel.setText("상태: " + status);
        startTimeLabel.setText(formatTime(startTime));
        usernameLabel.setText(username);

        moveToNewWindowButton.setVisible(!"사용 중인 좌석 입니다.".equals(status));
    }

    private String formatTime(String startTime) {
        LocalDateTime dateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }
}
