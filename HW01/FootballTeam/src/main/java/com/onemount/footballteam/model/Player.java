package com.onemount.footballteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Player {
    private int num;
    private String name;
    private Position position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return num == player.num;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }
}
