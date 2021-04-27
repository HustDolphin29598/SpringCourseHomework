package com.onemount.bmi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BMIRequest {
//    private String name;
//    private String email;
    private float height;
    private float weight;
}
