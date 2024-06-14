package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatAssignmentDAO;
import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.TimeDto;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.pcroom.pcproject.controller.SignInController.logout;
// 사용자 페이지 관련 기능을 제어, 사용자의 좌석 정보, 사용 시간, 기능 버튼 제어 등을 담당, 공동 작성
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

    private void initialize() {
        startTimer();
        // 잔여 시간 감소 기능 추가
        decrementRemainingTimePeriodically();
    }
    // AnimationTimer를 이용하여 1초마다 잔여 시간을 업데이트하는 메서드, 권대용 작성
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

    // 잔여 시간을 주기적으로 감소시키는 메서드, 권대용 작성
    private void decrementRemainingTimePeriodically() {
        Thread decrementThread = new Thread(() -> {
            while (true) {
                try {
                    // 1분마다 잔여 시간 감소
                    Thread.sleep(60_000); // 1분(60초) 대기
                    UserDao userDao = new UserDao();
                    int userId = userDao.getUserIdByNickname(SignInController.getToken());
                    TimeDao timeDao = new TimeDao();
                    TimeDao.decrementRemainingTime(userId); // 잔여 시간 감소
                    System.out.println("남은 시간이 1분 감소되었습니다." + "남은시간 : " + timeDao.getTimeByUserId(userId).getRemainingTime() + "분");

                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        decrementThread.setDaemon(true);
        decrementThread.start();
    }
    // 사용자의 잔여 시간 정보를 업데이트, 권대용 작성
    private void updateRemainingTime() {
        UserDao userDao = new UserDao();
        int userId = userDao.getUserIdByNickname(SignInController.getToken());
        TimeDto timeDto = timeDao.getTimeByUserId(userId);

        if (timeDto != null) {
            previousTimeDto = timeDto;
        }
    }
    // UI 상의 잔여 시간 라벨을 업데이트, 권대용 작성
    private void updateRemainingTimeLabel(int remainingMinutes) {
        long hours = remainingMinutes / 60;
        long minutes = remainingMinutes % 60;
        String remainingTime = String.format("%02d:%02d", hours, minutes);
        remainingTimeLabel.setText(remainingTime);

        if (remainingMinutes <= 0) {
            timer.stop();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("시간 만료");
                alert.setHeaderText(null);
                alert.setContentText("사용 시간이 만료되었습니다. 자리를 이동하거나 추가 요금을 지불해주세요.");
                alert.showAndWait();
            });
        }
    }

    @FXML
    private void charge(ActionEvent event) {
        showChargePage();
    }
    // 충전 페이지 뷰를 로드하고 새 창으로 표시, 권대용 작성
    private void showChargePage() {
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
    // 주문 메뉴 버튼 클릭 시 메뉴 페이지를 보여줌, 김진석 작성
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
    // 사용자 페이지의 세부 정보를 설정, 공동 작성
    public void setUserPageDetails(String seatNumber, String status, String startTime) {
        seatNumberLabel.setText(seatNumber);
        usernameLabel.setText(status);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startDateTime = LocalTime.parse(startTime, formatter);
        String startTimeString = startDateTime.format(DateTimeFormatter.ofPattern("HH시mm분"));

        startTimeLabel.setText(startTimeString);
        UserDao userDao = new UserDao();
        int userId = userDao.getUserIdByNickname(SignInController.getToken());
        TimeDto timeDto = timeDao.getTimeByUserId(userId);

        if (timeDto != null) {
            int remainingMinutes = timeDto.getRemainingTime();
            long hours = remainingMinutes / 60;
            long minutes = remainingMinutes % 60;
            String remainingTime = String.format("%02d시%02d분", hours, minutes);
            remainingTimeLabel.setText(remainingTime);
        } else {
            remainingTimeLabel.setText("00:00");
        }
    }
    // 사용 종료 버튼 클릭 시 호출되는 메서드, 공동 작성
    @FXML
    private void handleClose(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("종료 확인");
        alert.setHeaderText(null);
        alert.setContentText("정말로 사용을 종료하시겠습니까?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserDao userDao = new UserDao();
                String seatNumberText = seatNumberLabel.getText();
                int startIndex = seatNumberText.indexOf(":") + 2;
                String seatNumber = seatNumberText.substring(startIndex);
                int parsedSeatNumber = Integer.parseInt(seatNumber);
                seatDao.updateSeatStatus(parsedSeatNumber, 1);
                int userId = userDao.getUserIdByNickname(SignInController.getToken());
                TimeDao timeDao = new TimeDao();

                String loginTime = timeDao.getUserStartTime(SignInController.getToken());
                Timestamp logoutTime = new Timestamp(System.currentTimeMillis());
                try {
                    SeatAssignmentDAO.updateLogoutTime(userId, Timestamp.valueOf(loginTime), logoutTime);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) seatNumberLabel.getScene().getWindow();
                stage.close();
                logout();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
                    Parent root = loader.load();
                    Stage mainPageStage = new Stage();
                    mainPageStage.setTitle("메인 페이지");
                    mainPageStage.setScene(new Scene(root));
                    mainPageStage.setWidth(400);
                    mainPageStage.setHeight(500);
                    mainPageStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // 사용자 정보 버튼 클릭 시 사용자 정보 페이지를 보여줌, 권대용 작성
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

    // 자리 이동 버튼 클릭 시 사용자에게 자리 이동 여부를 확인하는 경고창을 띄움, 공동 작성
    @FXML
    private void moveSeat(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("자리 이동 확인");
        alert.setHeaderText(null);
        alert.setContentText("정말로 자리 이동을 하시겠습니까?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
                    Parent root = loader.load();
                    UserDao userDao = new UserDao();
                    int id = userDao.getUserIdByNickname(SignInController.getToken());
                    String seatNumberText = seatNumberLabel.getText();
                    int startIndex = seatNumberText.indexOf(":") + 2;
                    String seatNumber = seatNumberText.substring(startIndex);
                    int parsedSeatNumber = Integer.parseInt(seatNumber);
                    seatDao.updateSeatStatus(parsedSeatNumber, 1);
                    try {
                        SeatAssignmentDAO.unassignSeat(id);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Stage mainPageStage = new Stage();
                    mainPageStage.setTitle("Main Page");
                    mainPageStage.setScene(new Scene(root));
                    mainPageStage.setWidth(400);
                    mainPageStage.setHeight(500);
                    mainPageStage.show();
                    Node source = (Node) event.getSource();
                    Stage currentStage = (Stage) source.getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
