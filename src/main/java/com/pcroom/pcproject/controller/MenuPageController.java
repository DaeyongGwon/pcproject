package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.Food;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuPageController {
    public TextField FoodSearch;
    private List<Node> filteredItems = new ArrayList<>(); // 필터링된 상품들을 저장할 리스트
    private Food food;

    @FXML
    private ScrollPane categoryScrollPane;
    @FXML
    private FlowPane menuItemsPane;
    @FXML
    private ScrollPane scrollPane;

    public MenuPageController() {
        this.food = new Food(this); // FoodModel에 현재 컨트롤러 인스턴스를 전달
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

        // 데이터베이스에서 메뉴 아이템 로드
        List<Node> items = food.getFoodData();
        menuItemsPane.getChildren().addAll(items);

        scrollPane.setFitToWidth(true);
        // 화면 크기에 따라 항목 수 조정
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            int newPrefColumns = Math.max(1, (int) (newVal.doubleValue() / 200));
            menuItemsPane.setPrefWrapLength(newVal.doubleValue());
        });
    }

    @FXML
    // 필터링 By 카테고리 버튼
    private void filterByCategory(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            String category = ((Button) event.getSource()).getText();
            filteredItems.clear();

            food.getAllItems().forEach(node -> {
                if (node instanceof BorderPane) {
                    BorderPane itemBox = (BorderPane) node;
                    VBox infoBox = (VBox) itemBox.getCenter();
                    List<Label> labels = new ArrayList<>();
                    infoBox.getChildren().forEach(child -> {
                        if (child instanceof HBox) {
                            HBox hbox = (HBox) child;
                            hbox.getChildren().forEach(grandChild -> {
                                if (grandChild instanceof Label) {
                                    labels.add((Label) grandChild);
                                }
                            });
                        }
                    });

                    boolean categoryMatch = category.equals("전체");
                    for (Label lbl : labels) {
                        if (lbl.getText().equals(category)) {
                            categoryMatch = true;
                            break;
                        }
                    }

                    if (categoryMatch) {
                        filteredItems.add(node);
                    }
                }
            });

            menuItemsPane.getChildren().clear();
            menuItemsPane.getChildren().addAll(filteredItems);
        }
    }

    // 검색옆에 X 버튼 클릭시 검색창 초기화
    @FXML
    public void clearTextField(ActionEvent actionEvent) {
        FoodSearch.clear();
    }

    @FXML // 음식 찾기
    public void searchFood(ActionEvent event) {
        String keyword = FoodSearch.getText().trim();
        filterItemsByKeyword(keyword);
    }
    // 문자열에 특정 키워드가 포함되어 있는지 확인하는 메서드
    public boolean containsKeyword(String text, String keyword) {
        return text.toLowerCase().contains(keyword.toLowerCase());
    }
    private void filterItemsByKeyword(String keyword) {
        if (keyword.isEmpty()) {
            showAllItems();
        } else {
            filteredItems.clear();
            food.getAllItems().forEach(node -> {
                if (node instanceof BorderPane) {
                    BorderPane itemBox = (BorderPane) node;
                    VBox infoBox = (VBox) itemBox.getCenter();
                    Label titleLabel = (Label) infoBox.getChildren().get(1);
                    String title = titleLabel.getText();
                    if (containsKeyword(title, keyword)) {
                        filteredItems.add(node);
                    }
                }
            });
            showFilteredItems();
        }
    }

    private void showAllItems() {
        menuItemsPane.getChildren().clear();
        menuItemsPane.getChildren().addAll(food.getAllItems());
    }

    private void showFilteredItems() {
        menuItemsPane.getChildren().clear();
        menuItemsPane.getChildren().addAll(filteredItems);
    }

    // 메뉴 아이템 노드 생성
    public Node createMenuItemNode(String title, String description, String price, String oldPrice, String[] labels, String imagePath) {
        //박스 생성
        BorderPane itemBox = new BorderPane();
        itemBox.setPrefSize(180, 260); // Set the preferred size of the item box
        // 박스 내부 항목들 왼쪽 정렬
        itemBox.setStyle("-fx-font-family: 'D2Coding'; -fx-background-color: white; -fx-background-radius: 10px; -fx-alignment: center-left; -fx-opacity: 1; -fx-transition: opacity 0.3s;");

        try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                Image image = new Image(imageStream);

                // ImageView 설정
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(180);
                imageView.setFitHeight(160);
                imageView.setPreserveRatio(true); // 비율을 유지하지 않음

                // 클립을 적용할 StackPane 생성
                StackPane imageContainer = new StackPane(imageView);
                imageContainer.setMaxSize(180, 160);

                // StackPane의 상단 모서리를 둥글게 만듦
                Rectangle clip = new Rectangle(180, 200);
                clip.setArcWidth(19);
                clip.setArcHeight(19);
                imageContainer.setClip(clip);

                VBox infoBox = new VBox(5); // Create a new VBox for the item information
                infoBox.setAlignment(Pos.TOP_LEFT); // VBox를 왼쪽 정렬로 설정
                infoBox.setStyle("-fx-padding: 6;"); // Padding 추가

                // 라벨을 수평으로 나열하도록 수정
                HBox labelContainer = new HBox();
                HBox priceBox = new HBox(5);
                Label lblDescription = new Label(description);
                labelContainer.setAlignment(Pos.CENTER_LEFT); // 라벨을 왼쪽 정렬로 설정
                labelContainer.setSpacing(4); // 라벨 사이의 간격 조정

                // 라벨 추가
                for (String label : labels) {
                    Label lblLabel = new Label(label);
                    lblLabel.setStyle("-fx-padding: 2px 3px 2px 3px; -fx-alignment: center;");
                    // 각 라벨에 대해 다른 색상 설정
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

                    labelContainer.getChildren().add(lblLabel); // 라벨을 컨테이너에 추가
                }

                infoBox.getChildren().add(labelContainer); // 라벨 컨테이너를 infoBox에 추가

                // 상품명 타이틀 텍스트
                Label lblTitle = new Label(title);
                lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-alignment: center-left;"); // Label을 왼쪽 정렬로 설정
                infoBox.getChildren().add(lblTitle);
                // 상품 설명
                if (!description.isEmpty()) {
                    lblDescription.setStyle("-fx-description: center-left;"); // Label을 왼쪽 정렬로 설정
                    infoBox.getChildren().add(lblDescription);
                }

                // 상품 가격 텍스트
                if (!oldPrice.isEmpty()) {
                    priceBox.setAlignment(Pos.CENTER_LEFT); // HBox를 왼쪽 정렬로 설정
                    // 상품 가격
                    Label lblPrice = new Label(price);
                    lblPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: red;"); // 현재 가격을 빨간색으로 설정
                    priceBox.getChildren().add(lblPrice);
                    // 상품 옛 가격
                    Text txtOldPrice = new Text(oldPrice);
                    txtOldPrice.setStyle("-fx-font-size: 14px; -fx-fill: gray;"); // 이전 가격을 회색으로 설정
                    txtOldPrice.setStrikethrough(true); // 가로 줄 추가
                    priceBox.getChildren().add(txtOldPrice);

                    infoBox.getChildren().add(priceBox); // 가격 HBox를 infoBox에 추가
                } else {
                    Label lblPrice = new Label(price);
                    lblPrice.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: center-left;");
                    infoBox.getChildren().add(lblPrice);
                }
                itemBox.setTop(imageContainer); // 이미지 컨테이너를 상단에 배치
                itemBox.setCenter(infoBox); // 정보를 중앙에 배치

                // "담기" 버튼 생성
                Button addButton = new Button("담기");
                addButton.setStyle("-fx-font-family: 'D2Coding'; -fx-padding: 8px 5px; -fx-background-color: #fbe54d; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 7px; -fx-alignment: center;");

                //넓이 딲맞게
                addButton.setPrefHeight(30);
                addButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(addButton, Priority.ALWAYS);
                addButton.setCursor(Cursor.HAND); // 마우스 커서를 손가락 모양으로 변경
                addButton.setVisible(false); // 버튼을 숨김

                // 버튼을 담을 HBox 생성
                HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.TOP_CENTER); // 버튼을 왼쪽 정렬로 설정
                // 패딩 탑 10
                buttonContainer.setStyle("-fx-padding: 0px 6px 8px 6px;");
                buttonContainer.getChildren().add(addButton);
                itemBox.setBottom(null);


                // 마우스를 itemBox 위에 올렸을 때 이벤트 처리
                itemBox.setOnMouseEntered(event -> {
                    // 배경을 투명하게 만들어줍니다.
                    itemBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 10px; -fx-alignment: center-left;");
                    imageContainer.setOpacity(0.5);
                    infoBox.setOpacity(0.5);
                    // priceBox와 lblDescription을 숨기고 addButton을 보이도록 설정합니다.
                    priceBox.setVisible(false);
                    lblDescription.setVisible(false);
                    addButton.setVisible(true); // addButton을 보이도록 설정
                    buttonContainer.setOpacity(1);
                    itemBox.setBottom(buttonContainer);
                    infoBox.getChildren().remove(priceBox);
                    infoBox.getChildren().remove(lblDescription);
                    // 사라질 때 생기는 빈 공간을 없애기 위해 높이를 고정합니다.
                    itemBox.setMaxHeight(260);
                });

                // 마우스가 itemBox를 벗어났을 때 이벤트 처리
                itemBox.setOnMouseExited(event -> {
                    // 불투명했던 배경을 원래대로 복구합니다.
                    itemBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-alignment: center-left;");
                    imageContainer.opacityProperty().setValue(1); // 이미지를 투명하게 설정
                    infoBox.opacityProperty().setValue(1);
                    // priceBox와 lblDescription이 보이지 않을 때 addButton을 숨기도록 설정합니다.
                    if (!itemBox.isHover()) {
                        priceBox.setVisible(true);
                        lblDescription.setVisible(true);
                        addButton.setVisible(false); // addButton을 숨기도록 설정
                        // addButton을 infoBox에서 제거합니다.
                        infoBox.getChildren().add(priceBox);
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


