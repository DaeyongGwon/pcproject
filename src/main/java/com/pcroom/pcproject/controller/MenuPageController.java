package com.pcroom.pcproject.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;

public class MenuPageController {

    @FXML
    private FlowPane menuItemsPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    public void initialize() {
        scrollPane.setFitToHeight(true);
        // 예제 메뉴 아이템 추가
        for (int i = 0; i < 10; i++) {
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", "라벨", "/images/no_image.jpeg");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", "라벨", "/images/meat.png");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", "라벨", "/images/2.png");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", "라벨", "/images/3.jpeg");
        }
        scrollPane.setFitToWidth(true);
        // 화면 크기에 따라 항목 수 조정
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            int newPrefColumns = Math.max(1, (int) (newVal.doubleValue() / 200));
            menuItemsPane.setPrefWrapLength(newVal.doubleValue());
        });
    }

    private void addMenuItem(String title, String description, String price, String oldPrice, String label, String imagePath) {
        BorderPane itemBox = new BorderPane();
        itemBox.setPrefSize(230, 300); // Set the preferred size of the item box
        itemBox.setStyle("-fx-border-color: black; -fx-border-radius: 14px; -fx-background-color: white; -fx-background-radius: 15px;");

        try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                Image image = new Image(imageStream);

                // ImageView 설정
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(230);
                imageView.setFitHeight(180);
                imageView.setPreserveRatio(true); // 비율을 유지하지 않음

                // 클립을 적용할 StackPane 생성
                StackPane imageContainer = new StackPane(imageView);
                imageContainer.setMaxSize(230, 180);

                // StackPane의 상단 모서리를 둥글게 만듦
                Rectangle clip = new Rectangle(230, 300);
                clip.setArcWidth(26);
                clip.setArcHeight(26);
                imageContainer.setClip(clip);

                VBox infoBox = new VBox(5); // Create a new VBox for the item information
                infoBox.setAlignment(Pos.CENTER);

                Label lblTitle = new Label(title);
                lblTitle.setStyle("-fx-font-weight: bold;");
                infoBox.getChildren().add(lblTitle);

                if (!description.isEmpty()) {
                    Label lblDescription = new Label(description);
                    infoBox.getChildren().add(lblDescription);
                }

                Label lblPrice = new Label(price);
                lblPrice.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                infoBox.getChildren().add(lblPrice);

                if (!oldPrice.isEmpty()) {
                    Label lblOldPrice = new Label(oldPrice);
                    lblOldPrice.setStyle("-fx-font-size: 20px; -fx-strikethrough: true;");
                    infoBox.getChildren().add(lblOldPrice);
                }

                Label lblLabel = new Label(label);
                lblLabel.setStyle("-fx-background-color: yellow; -fx-padding: 2px;");
                infoBox.getChildren().add(lblLabel);

                itemBox.setTop(imageContainer); // 이미지 컨테이너를 상단에 배치
                itemBox.setCenter(infoBox); // 정보를 중앙에 배치
            } else {
                System.err.println("이미지를 로드하는데 실패했습니다. 이미지 경로: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("이미지를 로드하는 도중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }

        // 아이템 박스 간의 간격을 1.5배로 늘림
        menuItemsPane.setHgap(15); // 수평 간격 설정
        menuItemsPane.setVgap(15); // 수직 간격 설정
        menuItemsPane.getChildren().add(itemBox);
    }
}
