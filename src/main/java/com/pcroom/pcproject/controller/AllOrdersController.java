package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.OrderDto;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AllOrdersController {

    @FXML
    private TableView<OrderDto> orderTableView;

    @FXML
    private TableColumn<OrderDto, Integer> orderIdColumn;

    @FXML
    private TableColumn<OrderDto, String> itemNameColumn;

    @FXML
    private TableColumn<OrderDto, String> userNicknameColumn; // 사용자 닉네임을 보여줄 컬럼


    @FXML
    private TableColumn<OrderDto, String> orderDateColumn;

    @FXML
    private TableColumn<OrderDto, Double> totalPriceColumn;

    @FXML
    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        userNicknameColumn.setCellValueFactory(new PropertyValueFactory<>("userNickname")); // 사용자 닉네임 컬럼에 대한 cellValueFactory 설정
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    public void setOrders(List<OrderDto> orders) {
        orderTableView.getItems().setAll(orders);
    }
}
