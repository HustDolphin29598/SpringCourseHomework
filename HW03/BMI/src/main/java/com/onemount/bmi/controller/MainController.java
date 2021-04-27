package com.onemount.bmi.controller;


import com.onemount.bmi.model.BMIRequest;
import com.onemount.bmi.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bmi")
public class MainController {

    @Autowired
    private HealthService healthService;

    @GetMapping()
    public ResponseEntity<Object> handleBMIPost(@RequestParam String height, @RequestParam String weight){
        try {
            float h = Float.parseFloat(height);
            float w = Float.parseFloat(weight);
            BMIRequest bmiRequest = new BMIRequest(h, w);
            return ResponseEntity.ok(healthService.calculateBMI(bmiRequest));
        } catch (NumberFormatException ex){
//            ex.printStackTrace();
            return ResponseEntity.ok("Please input number only. Thanks !");
        }
    }

}
