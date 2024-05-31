package com.pcroom.pcproject.model;

import com.pcroom.pcproject.controller.MenuPageController;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Food {
    private final List<Node> allItems = new ArrayList<>(); // 모든 상품을 저장할 리스트
    private final MenuPageController menuPageController;
    private final getFood Food;

    public Food(MenuPageController menuPageController) {
        this.menuPageController = menuPageController;
        this.Food = new getFood();
    }

    // 데이터베이스에서 메뉴 아이템 로드
    public List<Node> getFoodData() {
        List<Node> items = Food.loadMenuItemsFromDatabase(menuPageController);
        // 모든 항목을 allItems 리스트에 추가
        allItems.addAll(items);
        return items;
    }

    // 모든 아이템 반환
    public List<Node> getAllItems() {
        return new ArrayList<>(allItems);
    }
}
