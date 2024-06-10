package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatDao;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.pcroom.pcproject.controller.SignInController.logout;

public class UserPageController {
    private final SeatDao seatDao = new SeatDao();

    @FXML
    private Label seatNumberLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label remainingTimeLabel;
    @FXML
    private UserInfoController userInfoController; // FXML에서 정의된 UserInfoController를 사용

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
        startTimeLabel.setText(startTime);
        // 남은 시간은 별도로 계산하여 설정
    }

    private String formatTime(String startTime) {
        // LocalDateTime.parse를 사용하여 문자열을 LocalDateTime 객체로 변환
        LocalDateTime dateTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // DateTimeFormatter를 사용하여 시간 부분만 형식화
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void setSeatNumber(String seatNumber) {
        seatNumberLabel.setText(seatNumber.replace("좌석번호:", ""));
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

                // UserInfoController에 시작 시간 전달
                userInfoController.setStartTime(startTimeLabel.getText());

                // 현재 창 닫기
                Stage stage = (Stage) seatNumberLabel.getScene().getWindow();
                stage.close();
                logout();

                // MainPage.fxml 다시 로드 및 표시
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/MainPage.fxml"));
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
            userInfoController = loader.getController(); // FXML에서 정의된 UserInfoController를 가져와서
            userInfoController.initialize(); // initialize 메서드 호출하여 사용자 정보 설정
            userInfoController.setStartTime(startTimeLabel.getText()); // 시작 시간 설정 추가
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

