package com.onemount.bmi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BMIResponse {
    private float bmiIndex;
    private String category;
    private String recommendation;
}
