package com.onemount.footballteam.controller;

import com.onemount.footballteam.exception.TeamException;
import com.onemount.footballteam.model.Player;
import com.onemount.footballteam.model.Position;
import com.onemount.footballteam.model.TeamStatus;
import com.onemount.footballteam.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("")
public class APIController {

    @Autowired
    CoachService coachService;

    @GetMapping(value = "/chooseteam/{teamPattern}")
    public ResponseEntity<Set<Player>> chooseTeam(@PathVariable String teamPattern){
        try {
            return ResponseEntity.ok(coachService.chooseTeam(teamPattern));
        } catch (TeamException ex){
            throw ex;
        }
    }

    @GetMapping(value = "/team")
    public ResponseEntity<Set<Player>> getTeam(){
        return ResponseEntity.ok(coachService.chooseTeam("442"));
    }

    @GetMapping(value = "/teamgroup")
    public ResponseEntity<Map<String, Set<Player>>> getTeamGroup(){
        return ResponseEntity.ok(coachService.getTeamGroup());
    }

    @GetMapping(value = "/substitute/{playerNumber}/{position}")
    public ResponseEntity<TeamStatus> substitutePlayer(@PathVariable String playerNumber, @PathVariable String position) {
        try {
            int playNum = Integer.parseInt(playerNumber);
            for (Position p: Position.values()){
                if(p.toString().equals(position)){
                    return ResponseEntity.ok(coachService.substitute(playNum, p));
                }
            }
            throw new TeamException("Argument type mismatch");
        } catch (TeamException ex) {
            throw ex;
        } catch (NumberFormatException ex){
            throw new TeamException("Argument type mismatch");
        }
    }
}
