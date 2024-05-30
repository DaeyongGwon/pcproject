package com.pcroom.pcproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuPageController {
    private List<Node> allItems = new ArrayList<>(); // 모든 상품을 저장할 리스트
    private List<Node> filteredItems = new ArrayList<>(); // 필터링된 상품들을 저장할 리스트

    @FXML
    private ScrollPane categoryScrollPane;

    @FXML
    private FlowPane menuItemsPane;

    @FXML
    private ScrollPane scrollPane;

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
        // 예제 메뉴 아이템 추가
        for (int i = 0; i < 5; i++) {
            addMenuItem("미트볼 치즈 스파게티", "존맛탱", "8400", "10000", new String[]{"할인상품"}, "/images/4.jpeg");
            addMenuItem("네넴띤", "존맛탱", "3000", "4000", new String[]{"인기상품"}, "/images/5.png");
            addMenuItem("이썩는 아이스크림", "존맛탱", "1500", "2000", new String[]{"아이스크림"}, "/images/6.png");
            addMenuItem("5쥥어", "마치 널 닮음", "5000", "1억", new String[]{"과자"}, "/images/7.jpeg");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", new String[]{"라벨1", "라벨2", "라벨3"}, "/images/no_image.jpeg");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", new String[]{"라벨1", "라벨2"}, "/images/meat.jpeg");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", new String[]{"라벨1"}, "/images/2.jpeg");
            addMenuItem("메뉴 " + (i + 1), "설명", "가격", "", new String[]{"라벨1", "라벨2", "라벨3", "라벨4"}, "/images/3.jpeg");
        }
        menuItemsPane.getChildren().forEach(node -> allItems.add(node));

        scrollPane.setFitToWidth(true);
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

            // 필터링된 상품들을 찾아서 filteredItems 리스트에 추가
            filteredItems.clear();
            allItems.forEach(node -> {
                if (node instanceof BorderPane) {
                    BorderPane itemBox = (BorderPane) node;
                    VBox infoBox = (VBox) itemBox.getCenter();
                    List<Label> labels = new ArrayList<>();
                    infoBox.getChildren().forEach(child -> {
                        if (child instanceof Label) {
                            labels.add((Label) child);
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

            // FlowPane을 비우고 필터링된 상품들을 추가하며 보이게 설정
            menuItemsPane.getChildren().clear();
            menuItemsPane.getChildren().addAll(filteredItems);
        }
    }


    private void addMenuItem(String title, String description, String price, String oldPrice, String[] labels, String imagePath) {
        BorderPane itemBox = new BorderPane();
        itemBox.setPrefSize(180, 260); // Set the preferred size of the item box
        // 박스 내부 항목들 왼쪽 정렬
        itemBox.setStyle("-fx-font-family: 'D2Coding'; -fx-background-color: white; -fx-background-radius: 10px; -fx-alignment: center-left;");

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
                infoBox.setStyle("-fx-padding: 10;"); // Padding 추가

                Label lblTitle = new Label(title);
                lblTitle.setStyle("-fx-font-weight: bold; -fx-alignment: center-left;"); // Label을 왼쪽 정렬로 설정
                infoBox.getChildren().add(lblTitle);

                if (!description.isEmpty()) {
                    Label lblDescription = new Label(description);
                    lblDescription.setStyle("-fx-description: center-left;"); // Label을 왼쪽 정렬로 설정
                    infoBox.getChildren().add(lblDescription);
                }

                if (!oldPrice.isEmpty()) {
                    HBox priceBox = new HBox(5); // Create an HBox for the prices with spacing
                    priceBox.setAlignment(Pos.CENTER_LEFT); // HBox를 왼쪽 정렬로 설정

                    Label lblPrice = new Label(price);
                    lblPrice.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: red;"); // 현재 가격을 빨간색으로 설정
                    priceBox.getChildren().add(lblPrice);

                    Text txtOldPrice = new Text(oldPrice);
                    txtOldPrice.setStyle("-fx-font-size: 15px; -fx-fill: gray;"); // 이전 가격을 회색으로 설정
                    txtOldPrice.setStrikethrough(true); // 가로 줄 추가
                    priceBox.getChildren().add(txtOldPrice);

                    infoBox.getChildren().add(priceBox); // 가격 HBox를 infoBox에 추가
                } else {
                    Label lblPrice = new Label(price);
                    lblPrice.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-alignment: center-left;");
                    infoBox.getChildren().add(lblPrice);
                }

                for (String label : labels) {
                    Label lblLabel = new Label(label);// 폰트 설정
                    lblLabel.setStyle("-fx-padding: 2px; -fx-alignment: center;");

                    // 각 라벨에 대해 다른 색상 설정
                    if (label.equals("인기상품")) {
                        //배경 노란색 글자색은 검정색으로
                        lblLabel.setStyle("-fx-background-color: yellow; -fx-padding: 2px; -fx-alignment: center;");
                        lblLabel.setTextFill(Color.BLACK);
                    } else if (label.equals("추천상품")) {
                        //배경 초록색 글자색은 흰색으로
                        lblLabel.setStyle("-fx-background-color: green; -fx-padding: 2px; -fx-alignment: center;");
                        lblLabel.setTextFill(Color.WHITE);
                    } else if (label.equals("세트상품")) {
                        //배경 빨간색 글자색은 흰색으로
                        lblLabel.setStyle("-fx-background-color: red; -fx-padding: 2px; -fx-alignment: center;");
                        lblLabel.setTextFill(Color.WHITE);
                    } else if (label.equals("할인상품")) {
                        //배경을 오렌지로하고 글자색은 잘보이는것으로
                        lblLabel.setStyle("-fx-background-color: orange; -fx-padding: 2px; -fx-alignment: center;");
                        lblLabel.setTextFill(Color.BLACK);
                    } else if (label.equals("추가메뉴")) {
                        //배경을 보라색으로하고 글자색은 검정색으로
                        lblLabel.setStyle("-fx-background-color: purple; -fx-padding: 2px; -fx-alignment: center;");
                        lblLabel.setTextFill(Color.BLACK);
                    } else {
                        //배경을 회색으로하고 글자색은 검정색으로
                        lblLabel.setStyle("-fx-background-color: light gray; -fx-padding: 2px; -fx-alignment: center;");
                        lblLabel.setTextFill(Color.BLACK);
                    }

                    // 라벨을 HBox에 추가하여 가로로 정렬
                    HBox.setMargin(lblLabel, new Insets(0, 5, 0, 0)); // 라벨 간의 간격 조정
                    infoBox.getChildren().add(lblLabel);
                }


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
        menuItemsPane.setHgap(20); // 수평 간격 설정
        menuItemsPane.setVgap(20); // 수직 간격 설정
        menuItemsPane.getChildren().add(itemBox);
    }
}

