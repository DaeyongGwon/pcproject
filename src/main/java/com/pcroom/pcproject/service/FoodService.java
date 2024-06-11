package com.pcroom.pcproject.service;

import com.pcroom.pcproject.model.dao.FoodDao;
import com.pcroom.pcproject.model.dto.FoodDto;
import java.util.ArrayList;
import java.util.List;

public class FoodService {
    private final List<FoodDto> allItems = new ArrayList<>(); // 모든 상품을 저장할 리스트
    private final FoodDao foodDao;

    public FoodService() {
        this.foodDao = new FoodDao();
    }

    // 데이터베이스에서 메뉴 아이템 로드
    public List<FoodDto> getFoodData() {
        List<FoodDto> items = foodDao.loadMenuItemsFromDatabase();
        // 모든 항목을 allItems 리스트에 추가
        allItems.addAll(items);
        return items;
    }

    // 모든 아이템 반환
    public List<FoodDto> getAllItems() {
        return new ArrayList<>(allItems);
    }
    // 음식의 이름을 이용하여 음식의 ID를 가져오는 메소드
    public int getFoodIdByName(String foodName) {
        // 데이터베이스에서 음식의 이름을 기준으로 음식의 ID를 조회하는 쿼리를 실행하여 결과를 반환
        // 여기서는 간단한 예시를 들기 위해 가정적인 코드를 작성합니다.
        return foodDao.getFoodIdByName(foodName);
    }
}
