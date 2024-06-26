package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.OrderDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.OrderDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
// OrderPageController 클래스는 주문 페이지를 제어하는 역할, 권대용 작성
public class OrderPageController {
    @FXML
    private TableView<OrderDto> orderTableView;
    @FXML
    private TableColumn<OrderDto, String> itemNameColumn;
    @FXML
    private TableColumn<OrderDto, Integer> userIdColumn;
    @FXML
    private TableColumn<OrderDto, String> orderDateColumn;
    @FXML
    private TableColumn<OrderDto, Integer> totalPriceColumn;
    @FXML
    private TableColumn<OrderDto, Integer> orderIdColumn;

    @FXML
    private void initialize() throws SQLException {
        loadOrders();
        initializeTable();
    }

    private void initializeTable() {
//        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    //사용자의 주문 내역을 데이터베이스에서 가져와 TableView에 추가, 권대용 작성
    private void loadOrders() throws SQLException {
        // 로그인한 사용자의 ID 가져오기
        UserDao userDao = new UserDao();
        int userId = userDao.getUserIdByNickname(SignInController.getToken());

        // OrderDao를 통해 주문 내역 가져오기
        List<OrderDto> userOrders = OrderDao.getUserOrders(userId);

        // 가져온 주문 내역을 TableView에 추가
        ObservableList<OrderDto> orderList = FXCollections.observableArrayList(userOrders);
        orderTableView.setItems(orderList);
    }
}
