package com.onemount.footballteam.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class TeamStatus {
  private Set<Player> currentTeam;
  private List<Substitute> substituteHistory;

  public TeamStatus(){
    substituteHistory = new ArrayList<>();
  }
}