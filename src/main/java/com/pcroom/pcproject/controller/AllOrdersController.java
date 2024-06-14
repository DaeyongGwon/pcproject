package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.OrderDto;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


//모든 주문 내역을 관리하고 표시하는 컨트롤러 클래스
public class AllOrdersController {

    @FXML
    private TableView<OrderDto> orderTableView;

    @FXML
    private TableColumn<OrderDto, Integer> orderIdColumn;

    @FXML
    private TableColumn<OrderDto, String> itemNameColumn;

    @FXML
    private TableColumn<OrderDto, String> userNicknameColumn;

    @FXML
    private TableColumn<OrderDto, Integer> seatIdColumn;

    @FXML
    private TableColumn<OrderDto, String> orderDateColumn;

    @FXML
    private TableColumn<OrderDto, Double> totalPriceColumn;


    // 컨트롤러 클래스를 초기화, 이 메서드는 FXML 파일이 로드된 후 자동으로 호출, 김진석 작성
    @FXML
    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        userNicknameColumn.setCellValueFactory(new PropertyValueFactory<>("userNickname"));
        seatIdColumn.setCellValueFactory(new PropertyValueFactory<>("seatId"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }


     // 주어진 주문 목록을 테이블에 설정, 김진석 작성
    public void setOrders(List<OrderDto> orders) {
        orderTableView.getItems().setAll(orders);
    }
}
