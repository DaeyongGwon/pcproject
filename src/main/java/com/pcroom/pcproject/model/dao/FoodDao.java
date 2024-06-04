package com.pcroom.pcproject.model.dao;

import com.pcroom.pcproject.model.dto.FoodDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodDao {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "pcroom";
    private static final String PASSWORD = "pcroom";

    // 데이터베이스에서 메뉴 아이템 로드
    public List<FoodDto> loadMenuItemsFromDatabase() {
        List<FoodDto> items = new ArrayList<>();
        String query = "SELECT title, description, price, oldprice, labels, IMAGEPATH FROM FOOD ORDER BY FOODID ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                int price = rs.getInt("price");
                int oldPrice = rs.getInt("oldprice");
                String labelsString = rs.getString("labels");
                List<String> labels = Arrays.asList(labelsString.split(","));
                String imagePath = rs.getString("IMAGEPATH");

                FoodDto foodDto = new FoodDto(title, description, price, oldPrice, labels, imagePath);
                items.add(foodDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
