package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.OrderDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.OrderDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
// 사용자 세부 정보를 표시하고 관리, 사용자의 주문 내역 및 남은 시간을 표시, 김진석 작성
public class UserDetailsController {
    @FXML
    private Label nicknameLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label remainingTimeLabel; // 추가: 남은 시간을 표시할 라벨, 시간

    @FXML
    private Label remainingTimeLabel1; // 추가: 남은 시간을 표시할 라벨, 분

    @FXML
    private TableView<OrderDto> orderTableView;

    @FXML
    private TableColumn<OrderDto, String> itemNameColumn;

    @FXML
    private TableColumn<OrderDto, Date> orderDateColumn;

    @FXML
    private TableColumn<OrderDto, Integer> totalPriceColumn;

    @FXML
    private TableColumn<OrderDto, Integer> orderIdColumn;

    // 초기화 메서드, TableView의 열을 초기화, 김진석 작성
    @FXML
    private void initialize() {
        initializeTable();
    }
    // TableView의 각 열을 설정하는 메서드, 김진석 작성
    private void initializeTable() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }
    // 사용자 세부 정보를 설정하여 화면에 표시하는 메서드, 공동 작성
    public void setUserDetails(String nickname, String name, Date birthday, String address, String phoneNumber, String email) {
        nicknameLabel.setText(nickname);
        nameLabel.setText(name);
        birthdayLabel.setText(String.valueOf(birthday));
        addressLabel.setText(address);
        phoneNumberLabel.setText(phoneNumber);
        emailLabel.setText(email);

        // 사용자의 주문 내역을 가져와서 표시
        showOrderHistory(nickname);

        // 사용자의 남은 시간을 가져와서 표시
        TimeDao timeDao = new TimeDao();
        int remainingTime = timeDao.getUserRemainingTime(nickname); // 인스턴스를 통해 메서드 호출

        // 시와 분으로 변환하여 표시
        int hours = remainingTime / 60;
        int minutes = remainingTime % 60;
        remainingTimeLabel.setText(String.valueOf(hours));
        remainingTimeLabel1.setText(String.valueOf(minutes));

    }
    // 주어진 사용자의 주문 내역을 가져와 TableView에 설정하는 메서드, 공동 작성
    private void showOrderHistory(String nickname) {
        try {
            // 현재 로그인한 사용자의 ID를 얻어옴
            UserDao userDao = new UserDao();
            int userId = userDao.getUserIdByNickname(nickname);

            // OrderDao를 사용하여 해당 사용자의 주문 내역을 가져옴
            List<OrderDto> userOrders = OrderDao.getUserOrders(userId);

            // 주문 내역을 TableView에 설정
            orderTableView.getItems().setAll(userOrders);
        } catch (SQLException e) {
            e.printStackTrace();
            // 에러 처리
        }
    }
}
