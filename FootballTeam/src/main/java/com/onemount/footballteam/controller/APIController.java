package com.onemount.footballteam.controller;

import com.onemount.footballteam.model.Team;
import com.onemount.footballteam.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    TeamService teamService;

    @GetMapping(value = "/team/{teamPattern}")
    public Team chooseTeam(@PathVariable String teamPattern){
        return teamService.chooseTeam(teamPattern);
    }
}
