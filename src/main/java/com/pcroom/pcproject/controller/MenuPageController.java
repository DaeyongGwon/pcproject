package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.FoodModel;
import com.pcroom.pcproject.model.FoodItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuPageController {
    public TextField FoodSearch;
    @FXML
    private ScrollPane categoryScrollPane;
    @FXML
    private FlowPane menuItemsPane;
    @FXML
    private ScrollPane scrollPane;

    private List<Node> filteredItems = new ArrayList<>(); // 필터링된 상품들을 저장할 리스트
    private FoodModel foodModel;

    public MenuPageController() {
        this.foodModel = new FoodModel();
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
        List<FoodItem> items = foodModel.getFoodData();
        items.forEach(item -> menuItemsPane.getChildren().add(createMenuItemNode(item)));

        // 화면 크기에 따라 항목 수 조정
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            int newPrefColumns = Math.max(1, (int) (newVal.doubleValue() / 200));
            menuItemsPane.setPrefWrapLength(newVal.doubleValue());
        });
    }

    @FXML
    private void filterByCategory(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            String category = ((Button) event.getSource()).getText();
            filteredItems.clear();

            foodModel.getAllItems().forEach(item -> {
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
            foodModel.getAllItems().forEach(item -> {
                if (item.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredItems.add(createMenuItemNode(item));
                }
            });
            showFilteredItems();
        }
    }

    private void showAllItems() {
        menuItemsPane.getChildren().clear();
        foodModel.getAllItems().forEach(item -> menuItemsPane.getChildren().add(createMenuItemNode(item)));
    }

    private void showFilteredItems() {
        menuItemsPane.getChildren().clear();
        menuItemsPane.getChildren().addAll(filteredItems);
    }

    private Node createMenuItemNode(FoodItem item) {
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
                HBox.setHgrow(addButton, Priority.ALWAYS);
                addButton.setCursor(Cursor.HAND);
                addButton.setVisible(false);

                HBox buttonContainer = new HBox();
                buttonContainer.setAlignment(Pos.TOP_CENTER);
                buttonContainer.setStyle("-fx-padding: 0px 6px 8px 6px;");
                buttonContainer.getChildren().add(addButton);
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
