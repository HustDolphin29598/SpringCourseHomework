package com.onemount.bmi.service;

import com.onemount.bmi.model.BMIRequest;
import com.onemount.bmi.model.BMIResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public BMIResponse calculateBMI(BMIRequest request) {
        float bmiIndex = request.getWeight() / (request.getHeight() * request.getHeight());
        String category;
        if (bmiIndex < 15) {
            category = "Ốm đói";
        } else if (bmiIndex < 16) {
            category = "Rất gầy";
        } else if (bmiIndex < 18.5) {
            category = "Thiếu cân";
        } else if (bmiIndex < 25) {
            category = "Chuẩn mực";
        } else if (bmiIndex < 30) {
            category = "Hơi béo";
        } else if (bmiIndex < 35) {
            category = "Rất béo";
        } else if (bmiIndex < 40) {
            category = "Khủng long";
        } else {
            category = "Gọi cần cẩu";
        }
        return new BMIResponse(bmiIndex, category,"");
    }
}
