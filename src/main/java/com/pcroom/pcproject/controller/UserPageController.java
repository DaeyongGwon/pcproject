package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatAssignmentDAO;
import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.TimeDto;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.pcroom.pcproject.controller.SignInController.logout;

public class UserPageController {
    private final SeatDao seatDao = new SeatDao();
    private final TimeDao timeDao = new TimeDao();
    private AnimationTimer timer;
    private TimeDto previousTimeDto;
    @FXML
    private Label seatNumberLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label remainingTimeLabel;
    @FXML
    private LocalDateTime endTime = LocalDateTime.now();

    // 이전에 가져온 시간 정보를 저장하는 변수
    @FXML
    private void initialize() {
        // 타이머를 시작
        startTimer();
    }

    private void startTimer() {
        timer = new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) { // 1초마다 실행
                    updateRemainingTime();
                    updateRemainingTimeLabel(previousTimeDto.getRemainingTime()); // UI 업데이트
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }


    private void updateRemainingTime() {
        // 사용자의 ID를 가져와서 시간을 조회
        int userId = UserDao.getUserIdByNickname(SignInController.getToken());
        TimeDto timeDto = timeDao.getTimeByUserId(userId);

        if (timeDto != null) {
            // 이전에 가져온 시간 정보가 있는 경우에만 처리
            if (previousTimeDto != null) {
                // 이전 시간과 현재 시간의 차이를 계산하여 남은 시간을 갱신
                int elapsedSeconds = (int) (timeDto.getLastChecked().getTime()
                        - previousTimeDto.getLastChecked().getTime()) / 1000;
                int remainingMinutes = previousTimeDto.getRemainingTime() - elapsedSeconds / 60;

                // 남은 시간이 음수가 되면 0으로 설정
                remainingMinutes = Math.max(0, remainingMinutes);

                // 시간이 변경되었다면 DB에 업데이트
                if (remainingMinutes != previousTimeDto.getRemainingTime()) {
                    timeDto.setRemainingTime(remainingMinutes);
                    timeDao.updateTime(timeDto);
                    System.out.println("남은 시간이 변경되었습니다: " + remainingMinutes + "분");

                    // 남은 시간을 UI에 업데이트
                    updateRemainingTimeLabel(remainingMinutes);
                }
            }

            // 이전 시간 정보 업데이트
            previousTimeDto = timeDto;
        }
    }

    private void updateRemainingTimeLabel(int remainingMinutes) {
        // 남은 시간을 시간과 분으로 분할하여 라벨에 설정
        long hours = remainingMinutes / 60;
        long minutes = remainingMinutes % 60;
        String remainingTime = String.format("%02d:%02d", hours, minutes);
        remainingTimeLabel.setText(remainingTime);

        // 남은 시간이 0분이 되면 사용 종료 처리
        if (remainingMinutes <= 0) {
            handleClose(null);
        }
    }

    @FXML
    private void charge(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/Charge.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("요금제 구매");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void orderMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MenuPage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Menu Page");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserPageDetails(String seatNumber, String status, String startTime) {
        seatNumberLabel.setText(seatNumber);
        usernameLabel.setText(status);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startDateTime = LocalTime.parse(startTime, formatter);
        String startTimeString = startDateTime.format(DateTimeFormatter.ofPattern("HH시mm분"));

        startTimeLabel.setText(startTimeString);

        // 사용자의 ID를 가져와서 시간을 조회
        int userId = UserDao.getUserIdByNickname(SignInController.getToken());
        TimeDto timeDto = timeDao.getTimeByUserId(userId);

        if (timeDto != null) {
            // 시간 정보가 있는 경우 분 단위로 변환하여 남은 시간을 계산하여 설정
            int remainingMinutes = timeDto.getRemainingTime();
            long hours = remainingMinutes / 60;
            long minutes = remainingMinutes % 60;
            String remainingTime = String.format("%02d시%02d분", hours, minutes);
            remainingTimeLabel.setText(remainingTime);
        } else {
            remainingTimeLabel.setText("00:00");
        }
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
                // 사용자가 확인을 선택한 경우 DB에서 좌석 상태를 업데이트하고 현재 창 닫기
                String seatNumberText = seatNumberLabel.getText();
                int startIndex = seatNumberText.indexOf(":") + 2; // "좌석 번호:" 뒤의 숫자 인덱스
                String seatNumber = seatNumberText.substring(startIndex); // 숫자만 추출
                int parsedSeatNumber = Integer.parseInt(seatNumber);
                seatDao.updateSeatStatus(parsedSeatNumber, 1); // active를 1로 변경
                // 타임 테이블 관련 코드 처리
                int userId = UserDao.getUserIdByNickname(SignInController.getToken());
                TimeDto timeDto = timeDao.getTimeByUserId(userId);
                if (timeDto != null) {
                    // 이전 시간을 가져와서 현재 시간과의 차이 계산
                    LocalDateTime startTime = timeDto.getStartTime().toLocalDateTime();
                    LocalDateTime endTime = LocalDateTime.now();
                    Duration duration = Duration.between(startTime, endTime);
                    int elapsedMinutes = (int) duration.toMinutes();
                    // 남은 시간 갱신
                    int remainingMinutes = timeDto.getRemainingTime() - elapsedMinutes;
                    if (remainingMinutes < 0) {
                        remainingMinutes = 0;
                    }
                    System.out.println("사용 종료: " + elapsedMinutes + "분 사용, 남은 시간: " + remainingMinutes + "분");
                    // DB에 시간 업데이트
                    timeDto.setRemainingTime(remainingMinutes);
                    timeDao.updateTime(timeDto);
                }
                Stage stage = (Stage) seatNumberLabel.getScene().getWindow();
                stage.close();
                logout();

                // MainPage.fxml 다시 로드 및 표시
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
                    Parent root = loader.load();

                    Stage mainPageStage = new Stage();
                    mainPageStage.setTitle("메인 페이지");
                    mainPageStage.setScene(new Scene(root));
                    mainPageStage.setWidth(400); // 가로 400 설정
                    mainPageStage.setHeight(500); // 세로 500 설정
                    mainPageStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void onUserInfoButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/UserInfo.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("User Information");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void moveSeat(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("자리 이동 확인");
        alert.setHeaderText(null);
        alert.setContentText("정말로 자리 이동을 하시겠습니까?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // MainPage.fxml 다시 로드 및 표시
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
                    Parent root = loader.load();
                    // 타임 테이블 관련 코드 처리
                    int id = UserDao.getUserIdByNickname(SignInController.getToken());
                    //좌석 정보
                    String seatNumberText = seatNumberLabel.getText();
                    int startIndex = seatNumberText.indexOf(":") + 2; // "좌석 번호:" 뒤의 숫자 인덱스
                    String seatNumber = seatNumberText.substring(startIndex); // 숫자만 추출
                    int parsedSeatNumber = Integer.parseInt(seatNumber);
                    seatDao.updateSeatStatus(parsedSeatNumber, 1); // active를 1로 변경
                    System.out.println("id 값 : " + id);
                    // seat_assignment에서 seat_id를 삭제
                    try {
                        SeatAssignmentDAO.unassignSeat(id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Stage mainPageStage = new Stage();
                    mainPageStage.setTitle("메인 페이지");
                    mainPageStage.setScene(new Scene(root));
                    mainPageStage.setWidth(400); // 가로 400 설정
                    mainPageStage.setHeight(500); // 세로 500 설정
                    mainPageStage.show();

                    // 현재 창 닫기
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
