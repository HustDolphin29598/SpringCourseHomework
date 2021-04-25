package com.onemount.footballteam.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class TeamAndSubstitute {
  private Set<Player> players;
  private List<Substitute> substituteHistory;

  public TeamAndSubstitute(){
    substituteHistory = new ArrayList<>();
  }
}