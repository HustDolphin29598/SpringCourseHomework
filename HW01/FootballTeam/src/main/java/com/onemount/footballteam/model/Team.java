package com.onemount.footballteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Team {
    private String name;
    private ArrayList<Player> players ;

    public Team(String name) {
        this.name = name;
    }
}
