package com.pcroom.pcproject.service;

import com.pcroom.pcproject.model.dao.FoodDao;
import com.pcroom.pcproject.model.dto.FoodDto;

import java.util.List;

public class FoodService {
    private final FoodDao foodDao;

    public FoodService() {
        this.foodDao = new FoodDao();
    }

    // 데이터베이스에서 메뉴 아이템 로드
    public List<FoodDto> getFoodData() {
        return foodDao.loadMenuItemsFromDatabase();
    }
}
