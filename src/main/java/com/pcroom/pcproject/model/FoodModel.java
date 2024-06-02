package com.pcroom.pcproject.model;

import java.util.ArrayList;
import java.util.List;

public class FoodModel {
    private final List<FoodItem> allItems = new ArrayList<>(); // 모든 상품을 저장할 리스트
    private final FoodDao foodDao;

    public FoodModel() {
        this.foodDao = new FoodDao();
    }

    // 데이터베이스에서 메뉴 아이템 로드
    public List<FoodItem> getFoodData() {
        List<FoodItem> items = foodDao.loadMenuItemsFromDatabase();
        // 모든 항목을 allItems 리스트에 추가
        allItems.addAll(items);
        return items;
    }

    // 모든 아이템 반환
    public List<FoodItem> getAllItems() {
        return new ArrayList<>(allItems);
    }
}
