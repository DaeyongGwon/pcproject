package com.pcroom.pcproject.view;

import com.pcroom.pcproject.controller.ManagerController;
import com.pcroom.pcproject.model.dao.SeatDao;
import com.pcroom.pcproject.model.dao.TimeDao;
import com.pcroom.pcproject.model.dto.SeatDto;
import com.pcroom.pcproject.util.SeatUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ManagerPage extends Application {

    // DAO 및 Controller 객체 생성ㄹ
    private SeatDao seatDao = new SeatDao();
    private TimeDao timeDao = new TimeDao();
    private ManagerController managerController = new ManagerController(seatDao, timeDao);

    // 좌석 그리드 생성
    private GridPane seatGrid = new GridPane();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("매니저 페이지");

        // 제목 레이블 생성 및 스타일 적용
        Label titleLabel = new Label("Manager Page");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // 모든 좌석 정보를 가져옵니다.
        List<SeatDto> seatList = managerController.getAllSeats();
        boolean[] seatStatus = new boolean[seatList.size()];

        // 좌석 그리드 설정
        seatGrid.setPadding(new Insets(10));
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);

        // 좌석 상태를 업데이트합니다.
        SeatUtils.updateSeatStatus(seatList, seatStatus, seatGrid);

        // 좌석 버튼 생성 및 그리드에 추가
        for (int i = 0; i < seatList.size(); i++) {
            int seatNumber = seatList.get(i).getSeatNumber();
            Button seatButton = new Button(String.valueOf(seatNumber));
            seatButton.setPrefSize(50, 50);
            seatButton.setOnAction(event -> managerController.handleSeatClick(seatNumber, seatGrid));
            seatGrid.add(seatButton, i % 8, i / 8);  // 한 행에 8개의 좌석 버튼을 배치합니다.
        }

        // VBox를 사용하여 제목 레이블과 좌석 그리드를 레이아웃합니다.
        VBox vbox = new VBox(8, titleLabel, seatGrid);
        vbox.setPadding(new Insets(8));

        // Scene 생성 및 primaryStage에 Scene 설정
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);

        // primaryStage를 보여줍니다.
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
