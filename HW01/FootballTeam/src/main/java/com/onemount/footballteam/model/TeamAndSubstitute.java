package com.onemount.footballteam.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamAndSubstitute {
  private Team currentTeam;
  private List<Substitute> substituteHistory;

  public TeamAndSubstitute(){
    substituteHistory = new ArrayList<>();
  }
}