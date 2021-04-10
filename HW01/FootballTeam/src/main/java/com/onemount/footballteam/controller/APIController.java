package com.onemount.footballteam.controller;

import com.onemount.footballteam.exception.TeamException;
import com.onemount.footballteam.model.Position;
import com.onemount.footballteam.model.Team;
import com.onemount.footballteam.model.TeamAndSubstitute;
import com.onemount.footballteam.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    CoachService coachService;

    @GetMapping(value = "/team/{teamPattern}")
    public Team chooseTeam(@PathVariable String teamPattern){
        return coachService.chooseTeam(teamPattern);
    }

    @GetMapping(value = "team/get")
    public Team getTeam(){
        return coachService.getTeam();
    }

    @GetMapping(value = "/substitute/{playerNumber}/{newPosition}")
    public TeamAndSubstitute substitutePlayer(@PathVariable int playerNumber, @PathVariable Position newPosition) {
        try {
            return coachService.substitute(playerNumber, newPosition);
        } catch (TeamException e) {
            e.printStackTrace();
        }
        return null;
    }
}
