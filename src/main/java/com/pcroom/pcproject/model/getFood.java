package com.pcroom.pcproject.model;

import com.pcroom.pcproject.controller.MenuPageController;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class getFood {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "hr";
    private static final String PASSWORD = "hr";

    // 데이터베이스에서 메뉴 아이템 로드
    public List<Node> loadMenuItemsFromDatabase(MenuPageController menuPageController) {
        List<Node> items = new ArrayList<>();

        try {
            Class.forName("oracle.jdbc.OracleDriver"); // 드라이버 명시적 로드
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            String query = "SELECT title, description, price, oldprice, labels, IMAGEPATH FROM FOOD ORDER BY FOODID ASC";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String price = rs.getString("price");
                String oldPrice = rs.getString("oldprice");
                String labelsString = rs.getString("labels");
                // Assuming labels are stored as comma-separated values
                String[] labels = labelsString.split(",");
                String imagePath = rs.getString("IMAGEPATH");

                items.add(menuPageController.createMenuItemNode(title, description, price, oldPrice, labels, imagePath));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
