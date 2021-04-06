package com.onemount.footballteam.service;

import com.onemount.footballteam.model.Player;
import com.onemount.footballteam.model.Team;
import com.onemount.footballteam.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TeamService {

    @Autowired
    private PlayerRepository playerRepository;

    public Team chooseTeam(String teamPattern){

        if(teamPattern.length() != 3){
            return null;
        }

        Team team = new Team("Barcelona");
        ArrayList<Player> teamPlayers = new ArrayList<>();
//        List<Player> players = playerRepository.getPlayerList();
        List<Player> GKPlayers = playerRepository.getGKPlayer();
        List<Player> MFPlayers = playerRepository.getMFPlayer();
        List<Player> FWPlayers = playerRepository.getFWPlayer();
        List<Player> DFPlayers = playerRepository.getDFPlayer();

        Random rand = new Random();

        // Choose GK
        int GKNum = rand.nextInt(GKPlayers.size());
        teamPlayers.add(GKPlayers.get(GKNum));

        // Choose DF Players
        int numDF = Integer.parseInt(String.valueOf(teamPattern.charAt(0)));
        int[] DFNum = new Random().ints(0, DFPlayers.size()).distinct().limit(numDF).toArray();
        for (int num : DFNum) {
            teamPlayers.add(DFPlayers.get(num));
        }

        // Choose MF Players
        int numMF = Integer.parseInt(String.valueOf(teamPattern.charAt(1)));
        int[] MFNum = new Random().ints(0, MFPlayers.size()).distinct().limit(numMF).toArray();
        for (int num : MFNum) {
            teamPlayers.add(MFPlayers.get(num));
        }

        // Choose FW Players
        int numFW = Integer.parseInt(String.valueOf(teamPattern.charAt(2)));
        int[] FWNum = new Random().ints(0, FWPlayers.size()).distinct().limit(numFW).toArray();
        for(int num : FWNum){
            teamPlayers.add(FWPlayers.get(num));
        }
        
        team.setPlayers(teamPlayers);
        return team;
    }
}
