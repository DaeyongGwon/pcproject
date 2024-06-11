package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.OrderDao;
import com.pcroom.pcproject.model.dao.SeatAssignmentDAO;
import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dto.OrderDto;
import com.pcroom.pcproject.model.dto.SeatDto;
import com.pcroom.pcproject.model.dto.UserDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManagerController {
    private final SeatDao seatDao = new SeatDao();
    private final SeatAssignmentDAO seatAssignmentDAO = new SeatAssignmentDAO();

    @FXML
    private GridPane seatGrid;

    @FXML
    private Label seatNumberLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    public void initialize() {
        try {
            int rows = 8;
            int cols = 8;

            createSeats(rows, cols);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSeats(int rows, int cols) throws SQLException {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int seatId = i * cols + j + 1;
                Button seatButton = new Button(String.valueOf(seatId));
                seatButton.setOnAction(event -> handleSeatClick(seatId));
                seatButton.setPrefSize(50, 50);
                seatButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                seatGrid.add(seatButton, j, i);
            }
        }
    }

    private void handleSeatClick(int seatId) {
        List<SeatDto> seatList = seatDao.getAllSeats();
        seatNumberLabel.setText(String.valueOf(seatId));
        // 좌석 ID를 기반으로 사용자의 정보를 가져옴
        SeatAssignmentDAO seatAssignmentDAO = new SeatAssignmentDAO();
        UserDto userDto = seatAssignmentDAO.getUserBySeatNumber(seatId);

        if (userDto != null) {
            userIdLabel.setText(String.valueOf(userDto.getNickname()));
            // 유저 정보 창을 띄우는 코드 추가
            showUserDetails(seatId);
        } else {
            userIdLabel.setText("사용자 없음");
        }
    }

    private void showUserDetails(int seatId) {
        try {
            // 좌석 ID를 기반으로 사용자의 정보를 가져옴
            SeatAssignmentDAO seatAssignmentDAO = new SeatAssignmentDAO();
            UserDto userDto = seatAssignmentDAO.getUserBySeatNumber(seatId);

            if (userDto != null) {
                // UserDetails.fxml을 불러와서 컨트롤러를 설정
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/UserDetails.fxml"));
                Parent root = loader.load();
                UserDetailsController controller = loader.getController();

                // UserDetailsController에 사용자 정보를 설정하여 표시
                controller.setUserDetails(userDto.getNickname(), userDto.getName(), userDto.getBirthday(), userDto.getAddress(), userDto.getPhonenumber(), userDto.getEmail());

                // 새 창을 만들어 유저 정보 표시
                Stage stage = new Stage();
                stage.setTitle("User Details");
                stage.setScene(new Scene(root));

                // 모달 다이얼로그로 설정하여 showAndWait() 호출
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                showAlert("Error", "User details not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load user details.");
        }
    }

    @FXML
    private void handleViewAllOrders() {
        try {
            // 전체 주문 내역을 불러옴
            OrderDao orderDao = new OrderDao();
            List<OrderDto> orders = orderDao.getAllOrders();

            // AllOrders.fxml을 불러와서 컨트롤러를 설정
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/AllOrders.fxml"));
            Parent root = loader.load();
            AllOrdersController controller = loader.getController();

            // AllOrdersController에 전체 주문 내역을 설정하여 표시
            controller.setOrders(orders);

            // 새 창을 만들어 전체 주문 내역 표시
            Stage stage = new Stage();
            stage.setTitle("All Orders");
            stage.setScene(new Scene(root));

            // 모달 다이얼로그로 설정하여 showAndWait() 호출
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load all orders.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve orders from the database.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
