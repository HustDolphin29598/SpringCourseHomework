package com.onemount.footballteam.model;

import com.onemount.footballteam.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
    private int num;
    private String name;
    private Position position;
}
