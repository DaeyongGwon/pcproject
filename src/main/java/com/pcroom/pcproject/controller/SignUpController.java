package com.pcroom.pcproject.controller;

import com.pcroom.pcproject.model.dto.UserDto;
import com.pcroom.pcproject.service.UserService;
import com.pcroom.pcproject.view.SignIn;
import com.pcroom.pcproject.view.SignUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class SignUpController {
    @FXML
    private TextField signUpId;
    @FXML
    private TextField signUpPassword;
    @FXML
    private TextField signUpEmail;
    @FXML
    private TextField signUpAddress;
    @FXML
    private TextField name;
    @FXML
    private ComboBox<Integer> yearComboBox;
    @FXML
    private ComboBox<Integer> monthComboBox;
    @FXML
    private ComboBox<Integer> dayComboBox;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Button signUpButton;

    private final UserService userService;
    private Stage primaryStage;

    // SignUp 페이지를 띄우는 코드
    public static void moveToSignUpPage() {
        SignUp signUp = new SignUp();
        Stage signUpStage = new Stage();
        try {
            signUp.start(signUpStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SignUpController() {
        this.userService = new UserService();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void initialize() {
        // 연도 초기화
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int year = 1900; year <= LocalDate.now().getYear(); year++) {
            years.add(year);
        }
        yearComboBox.setItems(years);

        // 월 초기화
        ObservableList<Integer> months = FXCollections.observableArrayList();
        for (int month = 1; month <= 12; month++) {
            months.add(month);
        }
        monthComboBox.setItems(months);

        // 일 초기화
        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int day = 1; day <= 31; day++) {
            days.add(day);
        }
        dayComboBox.setItems(days);
    }

    @FXML
    private void signUp(ActionEvent event) {
        String nickname = signUpId.getText();
        String password = signUpPassword.getText();
        String email = signUpEmail.getText();
        String address = signUpAddress.getText();
        String userName = name.getText();
        String userPhoneNumber = phoneNumber.getText();

        Integer selectedYear = yearComboBox.getValue();
        Integer selectedMonth = monthComboBox.getValue();
        Integer selectedDay = dayComboBox.getValue();

        Date userBirthday = null;
        try {
            LocalDate parsedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay);
            userBirthday = Date.valueOf(parsedDate);
        } catch (Exception e) {
            System.out.println("Invalid date selected.");
            return;
        }

        if (!nickname.isEmpty() && !password.isEmpty() && !userName.isEmpty() && !userPhoneNumber.isEmpty()) {
            UserDto newUser = new UserDto(nickname, userName, userBirthday, address, userPhoneNumber, email, password);
            boolean success = userService.addUser(newUser);
            if (success) {
                // 회원가입 성공 성공 메시지 띄우기
                showAlert("회원가입 성공", "환영합니다!");
                System.out.println("회원가입에 성공했습니다.");
                navigateToLogin();
            } else {
                System.out.println("회원가입에 실패했습니다.");
            }
        } else {
            System.out.println("필수 정보를 모두 입력해주세요.");
        }
    }

    private void navigateToLogin() {
        try {
            // 회원가입 페이지 닫기
            primaryStage.close();
            // 로그인 페이지 열기
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcroom/pcproject/view/SignIn.fxml"));
            Parent root = loader.load();
            SignInController controller = loader.getController();
            Stage signInStage = new Stage();
            controller.setPrimaryStage(signInStage);
            HBox hbox = (HBox) root;
            VBox leftVBox = (VBox) hbox.lookup("#leftVBox");
            VBox rightVBox = (VBox) hbox.lookup("#rightVBox");
            // Binding the width properties to ensure they take up half of the HBox
            leftVBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.5));
            rightVBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.5));
            Scene scene = new Scene(root, 800, 400);
            signInStage.setScene(scene);
            signInStage.setTitle("로그인");
            signInStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
