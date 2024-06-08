package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dto.SeatDto;
import com.pcroom.pcproject.util.SeatUtils;
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
    private boolean[] seatStatus;

    private final SeatDao seatDao = new SeatDao();

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label loggedInUserLabel;

    @FXML
    private HBox loggedOut;

    @FXML
    private HBox loggedIn;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                int rows = 8;
                int cols = 8;

                createSeats(rows, cols);
                updateSeatStatus();

                seatStatus = new boolean[rows * cols];
                for (int i = 0; i < seatStatus.length; i++) {
                    seatStatus[i] = Math.random() < 0.5;
                }

                String loggedInUser = SignInController.getToken();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSeatStatus() throws SQLException {
        List<SeatDto> seatList = seatDao.getAllSeats();
        seatStatus = new boolean[seatList.size()];
        SeatUtils.updateSeatStatus(seatList, seatStatus, seatGrid);
    }

    private void createSeats(int rows, int cols) {
        List<SeatDto> seatList = seatDao.getAllSeats();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int seatNumber = i * cols + j + 1;
                Button seatButton = new Button(String.valueOf(seatNumber));
                seatButton.setOnAction(event -> handleSeatButtonClick(seatNumber));
                seatButton.setPrefSize(50, 50);

                SeatDto seat = findSeat(seatList, seatNumber);
                if (seat != null && seat.getActive() == 1) {
                    seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                } else {
                    seatButton.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    seatButton.setDisable(true);
                }

                seatGrid.add(seatButton, j, i);
            }
        }
    }

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
            SeatDto seat = seatDao.getSeatByNumber(seatNumber);
            if (seat != null && seat.getActive() == 1) {
                FXMLLoader seatDetailsLoader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/SeatDetails.fxml"));
                Parent seatDetailsRoot = seatDetailsLoader.load();
                SeatDetailsController seatDetailsController = seatDetailsLoader.getController();

                boolean isReserved = seat.getActive() == 1;
                String status = isReserved ? "좌석 사용 가능" : "사용 중인 좌석입니다.";
                String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String loggedInUser = SignInController.getToken();

                seatDetailsController.setSeatDetails(String.valueOf(seatNumber), status, startTime, loggedInUser);

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
        SignInController.moveToSignInPage();
        seatGrid.getScene().getWindow().hide();
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        SignInController.logout();
        loggedInUserLabel.setText("로그인 중인 유저 없음");
        loggedOut.setVisible(true);
        loggedOut.setManaged(true);
        loggedIn.setVisible(false);
        loggedIn.setManaged(false);

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

    public void addSeatLabel(int row, int col, String seatNumber) {
        Label label = new Label(seatNumber);
        seatGrid.add(label, col, row);
    }
}
