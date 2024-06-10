package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dao.OrderDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dao.UserDao;
import com.pcroom.pcproject.model.dto.OrderDto;
import com.pcroom.pcproject.model.dto.TimeDto;
import com.pcroom.pcproject.service.FoodService;
import com.pcroom.pcproject.model.dto.FoodDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuPageController {
    public TextField FoodSearch;
    @FXML
    public VBox cartItems;
    @FXML
    public VBox cartVBox;
    @FXML
    public Label cartList;
    @FXML
    public Button placeOrderButton;
    @FXML
    private ScrollPane categoryScrollPane;
    @FXML
    private FlowPane menuItemsPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ScrollPane cartScrollPane;
    @FXML
    private Label totalPriceLabel;

    private List<Node> filteredItems = new ArrayList<>(); // 필터링된 상품들을 저장할 리스트
    private FoodService foodService;

    public MenuPageController() {
        this.foodService = new FoodService();
    }

    @FXML
    private void slideRight(ActionEvent event) {
        categoryScrollPane.setHvalue(categoryScrollPane.getHvalue() + 0.2);
    }

    @FXML
    private void slideLeft(ActionEvent event) {
        categoryScrollPane.setHvalue(categoryScrollPane.getHvalue() - 0.2);
    }

    @FXML
    public void initialize() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        // 데이터베이스에서 메뉴 아이템 로드
        List<FoodDto> items = foodService.getFoodData();
        items.forEach(item -> menuItemsPane.getChildren().add(createMenuItemNode(item)));

        // 화면 크기에 따라 항목 수 조정
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
//            int newPrefColumns = Math.max(1, (int) (newVal.doubleValue() / 200));
            menuItemsPane.setPrefWrapLength(newVal.doubleValue());
        });
    }

    @FXML
    private void filterByCategory(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            String category = ((Button) event.getSource()).getText();
            filteredItems.clear();

            foodService.getAllItems().forEach(item -> {
                if (item.getLabels().contains(category) || category.equals("전체")) {
                    filteredItems.add(createMenuItemNode(item));
                }
            });

            menuItemsPane.getChildren().clear();
            menuItemsPane.getChildren().addAll(filteredItems);
        }
    }

    @FXML
    public void clearTextField(ActionEvent actionEvent) {
        FoodSearch.clear();
    }

    @FXML
    public void searchFood(ActionEvent event) {
        String keyword = FoodSearch.getText().trim();
        filterItemsByKeyword(keyword);
    }

    private void filterItemsByKeyword(String keyword) {
        if (keyword.isEmpty()) {
            showAllItems();
        } else {
            filteredItems.clear();
            foodService.getAllItems().forEach(item -> {
                if (item.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredItems.add(createMenuItemNode(item));
                }
            });
            showFilteredItems();
        }
    }

    private void showAllItems() {
        menuItemsPane.getChildren().clear();
        foodService.getAllItems().forEach(item -> menuItemsPane.getChildren().add(createMenuItemNode(item)));
    }

    private void showFilteredItems() {
        menuItemsPane.getChildren().clear();
        menuItemsPane.getChildren().addAll(filteredItems);
    }

    public void closeCart(ActionEvent actionEvent) {
        cartVBox.setVisible(false);
        cartVBox.setManaged(false);
        // 장바구니 비우기 cartList을 제외한 모든 노드를 삭제
        cartItems.getChildren().removeIf(node -> !node.equals(cartList));
    }


    @FXML
    public void placeOrder(ActionEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("주문 확인");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("주문을 완료하시겠습니까?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 장바구니에 있는 상품들을 가져와서 주문 테이블에 추가
            try {
                // 주문 시간 설정
                Date orderDate = new Date(System.currentTimeMillis());

                // 장바구니에 있는 상품들을 주문 테이블에 추가
                for (Node node : cartItems.getChildren()) {
                    if (node instanceof BorderPane) {
                        int userId = UserDao.getUserIdByNickname(SignInController.getToken());
                        BorderPane cartItemBox = (BorderPane) node;
                        HBox topBox = (HBox) cartItemBox.getTop();
                        Label itemNameLabel = (Label) topBox.getChildren().get(0);
                        String itemName = itemNameLabel.getText();

                        HBox itemInfoBox = (HBox) cartItemBox.getBottom();
                        Label quantityLabel = (Label) itemInfoBox.getChildren().get(0);
                        int quantity = Integer.parseInt(quantityLabel.getText().substring(4));

                        Label priceLabel = (Label) itemInfoBox.getChildren().get(2);
                        int price = Integer.parseInt(priceLabel.getText().substring(4));

                        // 주문 테이블에 상품 정보 추가
                        OrderDto order = new OrderDto(itemName ,userId, orderDate, price);
                        OrderDao.addOrder(order);
                    }
                }
                // 결제가 가능한지 확인하고 가능하면 결제 진행
                if (isPaymentPossible()) {
                    processPayment();

                    // 주문 완료 후 장바구니 비우기
                    cartItems.getChildren().clear();
                    cartVBox.setVisible(false);
                    cartVBox.setManaged(false);
                    totalPriceLabel.setText("0원");

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("주문 완료");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("주문이 완료되었습니다.");
                    successAlert.showAndWait();
                } else {
                    Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                    warningAlert.setTitle("결제 실패");
                    warningAlert.setHeaderText(null);
                    warningAlert.setContentText("잔여 시간이 부족하여 주문을 완료할 수 없습니다. 잔여 시간을 충전해주세요.");
                    warningAlert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("오류 발생");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("주문을 처리하는 중에 오류가 발생했습니다.");
                errorAlert.showAndWait();
            }
        }
    }
    private boolean isPaymentPossible() {
        int userId = UserDao.getUserIdByNickname(SignInController.getToken());
        TimeDao timeDao = new TimeDao();
        int remainingTime = timeDao.getTimeByUserId(userId).getRemainingTime(); // 사용자의 잔여 시간을 조회합니다.
        int totalPrice = calculateTotalPrice(); // 결제할 총 가격을 계산하는 메소드, 이 부분은 별도의 메소드로 구현되어 있다고 가정합니다.
        int requiredTime = (totalPrice / 1000) * 60; // 1000원당 60분으로 계산하여 필요한 시간을 계산합니다.
        return remainingTime >= requiredTime;
    }

    private int calculateTotalPrice() {
        // 저장된 totalPrice 값을 반환합니다.
        int totalPrice = Integer.parseInt(totalPriceLabel.getText().replaceAll("[^0-9]", ""));
        System.out.println("총 가격: " + totalPrice);
        return totalPrice;
    }

    // 잔여 시간을 조회하여 결제를 처리하는 메소드
    private void processPayment() {
        int userId = UserDao.getUserIdByNickname(SignInController.getToken());
        TimeDao timeDao = new TimeDao();
        int remainingTime = timeDao.getTimeByUserId(userId).getRemainingTime(); // 사용자의 잔여 시간을 조회합니다.
        int totalPrice = calculateTotalPrice(); // 결제할 총 가격을 계산하는 메소드, 이 부분은 별도의 메소드로 구현되어 있다고 가정합니다.
        int requiredTime = (int) Math.ceil((double) totalPrice / 1000 * 60);
        if (remainingTime >= requiredTime) {
            // 잔여 시간이 충분한 경우 결제를 진행합니다.
            TimeDto newTimeDto = new TimeDto();
            newTimeDto.setId(userId);
            remainingTime -= requiredTime; // 결제에 필요한 시간만큼 잔여 시간을 차감합니다.
            newTimeDto.setRemainingTime(remainingTime);
            TimeDao.updateTime(newTimeDto); // 잔여 시간을 업데이트합니다.
            // 결제가 완료되면 잔여 시간을 갱신하고 사용자에게 결제 완료 메시지를 보여줍니다.
            System.out.println("결제가 완료되었습니다. 잔여 시간: " + remainingTime + "분");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("결제가 완료되었습니다. 잔여 시간: " + remainingTime + "분");
            alert.showAndWait();

        } else {
            // 잔여 시간이 부족한 경우 요금을 충전하도록 유도하는 메시지를 출력합니다.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("잔여 시간이 부족합니다. 요금을 충전해주세요.");
            alert.showAndWait();
        }
    }

    public void getOrders(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/OrderPage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("주문 내역");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 장바구니
    // 장바구니에 표시할 상품 정보 클래스
    private static class CartItem {
        private String itemName;
        private int quantity;
        private int price;

        public CartItem(String itemName, int quantity, int price) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPrice() {
            return price;
        }
    }

    private void addCartItemToView(CartItem cartItem) {
        // 동일한 상품이 이미 장바구니에 있는지 확인
        boolean isAlreadyAdded = false;
        for (Node node : cartItems.getChildren()) {
            if (node instanceof BorderPane) {
                BorderPane existingCartItemBox = (BorderPane) node;
                HBox topBox = (HBox) existingCartItemBox.getTop(); // HBox로 캐스팅
                Label itemNameLabel = (Label) topBox.getChildren().get(0); // HBox의 첫 번째 자식이 Label임을 가정
                if (itemNameLabel.getText().equals(cartItem.getItemName())) {
                    // 동일한 상품이 이미 장바구니에 있는 경우 수량 증가 및 가격 업데이트
                    isAlreadyAdded = true;
                    HBox itemInfoBox = (HBox) existingCartItemBox.getBottom();
                    Label quantityLabel = (Label) itemInfoBox.getChildren().get(0);
                    int currentQuantity = Integer.parseInt(quantityLabel.getText().substring(4)); // "수량: " 제외한 문자열을 정수로 변환
                    quantityLabel.setText("수량: " + (currentQuantity + 1)); // 수량 증가

                    // 가격 업데이트
                    Label priceLabel = (Label) itemInfoBox.getChildren().get(2);
                    int currentPrice = Integer.parseInt(priceLabel.getText().substring(4)); // "가격: " 제외한 문자열을 정수로 변환
                    int updatedPrice = currentPrice + cartItem.getPrice(); // 현재 가격에 새로운 아이템의 가격을 더함
                    priceLabel.setText("가격: " + updatedPrice); // 가격 업데이트
                    break;
                }
            }
        }

        if (!isAlreadyAdded) {
            // 동일한 상품이 장바구니에 없는 경우 새로운 아이템 추가
            BorderPane cartItemBox = new BorderPane();
            cartItemBox.setPrefSize(270, 60);
            cartItemBox.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-alignment: center-left; -fx-padding: 5px;");

            Label itemNameLabel = new Label(cartItem.getItemName());
            itemNameLabel.setStyle("-fx-font-weight: bold;"); // 폰트 굵게 설정

            Label quantityLabel = new Label("수량: 1");
            Label priceLabel = new Label("가격: " + cartItem.getPrice());

            HBox itemInfoBox = new HBox(quantityLabel, new Region(), priceLabel); // 수량과 가격을 담는 HBox 생성, 각각 왼쪽과 오른쪽에 위치하도록 설정
            HBox.setHgrow(itemInfoBox.getChildren().get(1), Priority.ALWAYS); // Region을 사용하여 수량과 가격 사이의 공간을 확보하여 오른쪽 정렬

            Button removeButton = new Button("X");
            removeButton.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #ff0000;"); // 배경색 투명, 폰트 굵게 설정
            removeButton.setOnAction(event -> {
                cartItems.getChildren().remove(cartItemBox); // 해당 항목 제거
                updateTotalPrice(); // 삭제 버튼을 눌렀을 때 토탈 가격 업데이트
            });

            HBox topBox = new HBox(itemNameLabel, new Region(), removeButton); // itemNameLabel과 removeButton을 담는 HBox
            topBox.setAlignment(Pos.CENTER_LEFT); // 왼쪽 정렬
            HBox.setHgrow(itemNameLabel, Priority.ALWAYS); // itemNameLabel이 좌측으로 확장되도록 설정

            cartItemBox.setTop(topBox); // 상단에 상품명과 삭제 버튼을 배치
            cartItemBox.setBottom(itemInfoBox); // 아이템 정보 HBox를 하단에 배치하여 가운데 정렬

            cartItems.getChildren().add(cartItemBox);
        }

        // 토탈 라벨 업데이트
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (Node node : cartItems.getChildren()) {
            if (node instanceof BorderPane) {
                BorderPane existingCartItemBox = (BorderPane) node;
                HBox itemInfoBox = (HBox) existingCartItemBox.getBottom();
                Label priceLabel = (Label) itemInfoBox.getChildren().get(2);
                int currentPrice = Integer.parseInt(priceLabel.getText().substring(4)); // "가격: " 제외한 문자열을 정수로 변환
                totalPrice += currentPrice; // 총 가격에 현재 아이템의 가격을 더함
            }
        }
        totalPriceLabel.setText(totalPrice + "원");
    }

    private Node createMenuItemNode(FoodDto item) {
        BorderPane itemBox = new BorderPane();
        itemBox.setPrefSize(180, 260);
        itemBox.setStyle("-fx-font-family: 'D2Coding'; -fx-background-color: white; -fx-background-radius: 10px; -fx-alignment: center-left; -fx-opacity: 1; -fx-transition: opacity 0.3s;");

        try (InputStream imageStream = getClass().getResourceAsStream(item.getImagePath())) {
            if (imageStream != null) {
                Image image = new Image(imageStream);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(180);
                imageView.setFitHeight(160);
                imageView.setPreserveRatio(true);

                StackPane imageContainer = new StackPane(imageView);
                imageContainer.setMaxSize(180, 160);
                Rectangle clip = new Rectangle(180, 200);
                clip.setArcWidth(19);
                clip.setArcHeight(19);
                imageContainer.setClip(clip);

                VBox infoBox = new VBox(5);
                infoBox.setAlignment(Pos.TOP_LEFT);
                infoBox.setStyle("-fx-padding: 6;");

                HBox labelContainer = new HBox();
                labelContainer.setAlignment(Pos.CENTER_LEFT);
                labelContainer.setSpacing(4);

                for (String label : item.getLabels()) {
                    Label lblLabel = new Label(label);
                    lblLabel.setStyle("-fx-padding: 2px 3px 2px 3px; -fx-alignment: center;");
                    if (label.equals("인기상품")) {
                        lblLabel.setStyle("-fx-font-size: 11px; -fx-background-color: #c93c62; -fx-padding: 2px; -fx-text-fill: white; -fx-alignment: center;");
                    } else if (label.equals("추천상품")) {
                        lblLabel.setStyle("-fx-font-size: 11px; -fx-background-color: #69a382; -fx-padding: 2px; -fx-text-fill: white; -fx-alignment: center;");
                    } else if (label.equals("세트상품")) {
                        lblLabel.setStyle("-fx-font-size: 11px; -fx-background-color: red; -fx-padding: 2px; -fx-text-fill: white; -fx-alignment: center;");
                    } else if (label.equals("할인상품")) {
                        lblLabel.setStyle("-fx-font-size: 11px; -fx-background-color: #4484d4; -fx-padding: 2px; -fx-text-fill: white; -fx-alignment: center;");
                    } else if (label.equals("추가메뉴")) {
                        lblLabel.setStyle("-fx-font-size: 11px; -fx-background-color: gray; -fx-padding: 2px; -fx-text-fill: white; -fx-alignment: center;");
                    } else {
                        lblLabel.setStyle("-fx-font-size: 11px; -fx-background-color: purple; -fx-padding: 2px; -fx-text-fill: white; -fx-alignment: center;");
                    }
                    labelContainer.getChildren().add(lblLabel);
                }

                infoBox.getChildren().add(labelContainer);

                Label lblTitle = new Label(item.getTitle());
                lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left;");
                infoBox.getChildren().add(lblTitle);

                final Label lblDescription = new Label(item.getDescription());
                if (!item.getDescription().isEmpty()) {
                    lblDescription.setStyle("-fx-description: center-left;");
                    infoBox.getChildren().add(lblDescription);
                }

                HBox priceBox = null;
                if (item.getOldPrice() > 0) {
                    priceBox = new HBox(5);
                    priceBox.setAlignment(Pos.CENTER_LEFT);

                    Label lblPrice = new Label(String.format("%d", item.getPrice()));
                    lblPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red;");
                    priceBox.getChildren().add(lblPrice);

                    Text txtOldPrice = new Text(String.format("%d", item.getOldPrice()));
                    txtOldPrice.setStyle("-fx-font-size: 14px; -fx-fill: gray;");
                    txtOldPrice.setStrikethrough(true);
                    priceBox.getChildren().add(txtOldPrice);

                    infoBox.getChildren().add(priceBox);
                } else {
                    Label lblPrice = new Label(String.format("%d", item.getPrice()));
                    lblPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: center-left;");
                    infoBox.getChildren().add(lblPrice);
                }

                itemBox.setTop(imageContainer);
                itemBox.setCenter(infoBox);

                Button addButton = new Button("담기");
                addButton.setStyle("-fx-font-family: 'D2Coding'; -fx-padding: 8px 5px; -fx-background-color: #fbe54d; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 7px; -fx-alignment: center;");
                addButton.setPrefHeight(30);
                addButton.setMaxWidth(Double.MAX_VALUE);
                //addButton 클릭 시 cartVBox의 visible을 true로 변경, manage를 false로 변경

                HBox.setHgrow(addButton, Priority.ALWAYS);
                addButton.setCursor(Cursor.HAND);
                addButton.setVisible(false);

                HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.TOP_CENTER);
                buttonContainer.setStyle("-fx-padding: 0px 6px 8px 6px;");
                buttonContainer.getChildren().add(addButton);
                // add 버튼 클릭 시 addCartItemToView 메소드 호출
                addButton.setOnAction(event -> {
                    CartItem cartItem = new CartItem(item.getTitle(), 1, item.getPrice());
                    addCartItemToView(cartItem);
                    cartVBox.setVisible(true);
                    cartVBox.setManaged(true);
                    System.out.println("장바구니에 추가됨");
                });
                itemBox.setBottom(null);

                HBox finalPriceBox = priceBox;
                itemBox.setOnMouseEntered(event -> {
                    itemBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 10px; -fx-alignment: center-left;");
                    imageContainer.setOpacity(0.5);
                    infoBox.setOpacity(0.5);
                    if (finalPriceBox != null) {
                        finalPriceBox.setVisible(false);
                    }
                    lblDescription.setVisible(false);
                    addButton.setVisible(true);
                    buttonContainer.setOpacity(1);
                    itemBox.setBottom(buttonContainer);
                    infoBox.getChildren().remove(finalPriceBox);
                    infoBox.getChildren().remove(lblDescription);
                    itemBox.setMaxHeight(260);
                });

                itemBox.setOnMouseExited(event -> {
                    itemBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-alignment: center-left;");
                    imageContainer.opacityProperty().setValue(1);
                    infoBox.opacityProperty().setValue(1);
                    if (!itemBox.isHover()) {
                        if (finalPriceBox != null) {
                            finalPriceBox.setVisible(true);
                        }
                        lblDescription.setVisible(true);
                        addButton.setVisible(false);
                        if (finalPriceBox != null) {
                            infoBox.getChildren().add(finalPriceBox);
                        }
                        infoBox.getChildren().add(lblDescription);
                        itemBox.setBottom(null);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemBox;
    }


}
