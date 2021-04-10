package com.onemount.footballteam.service;

import com.onemount.footballteam.exception.TeamException;
import com.onemount.footballteam.model.*;
import com.onemount.footballteam.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CoachService {

    @Autowired
    private PlayerRepository playerRepository;

    private Team team;
    private TeamAndSubstitute teamAndSubstitute;

    public CoachService() {
        team = new Team("Barcelona");
    }

    public Team chooseTeam(String teamPattern) {

        if (teamPattern.length() != 3) {
            return null;
        }

        ArrayList<Player> teamPlayers = new ArrayList<>();
//        List<Player> players = playerRepository.getPlayerList();
        List<Player> GKPlayers = playerRepository.getPlayersByPosition(Position.GK, playerRepository.getPlayerList());
        List<Player> MFPlayers = playerRepository.getPlayersByPosition(Position.MF, playerRepository.getPlayerList());
        List<Player> FWPlayers = playerRepository.getPlayersByPosition(Position.FW, playerRepository.getPlayerList());
        List<Player> DFPlayers = playerRepository.getPlayersByPosition(Position.DF, playerRepository.getPlayerList());

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
        for (int num : FWNum) {
            teamPlayers.add(FWPlayers.get(num));
        }

        team.setPlayers(teamPlayers);
        teamAndSubstitute = new TeamAndSubstitute();
        teamAndSubstitute.setCurrentTeam(team);
        return team;
    }

    public Team getTeam() {
        return team;
    }

    public TeamAndSubstitute substitute(int playerNumber, Position position) throws TeamException {

        if (team == null) {
            throw new TeamException("Team is not formed yet");
        }

        if (teamAndSubstitute.getSubstituteHistory()!= null && teamAndSubstitute.getSubstituteHistory().size()  == 5) {
            throw new TeamException("Number of substitution exceeds 5");
        }


        ArrayList<Player> availablePlayers = (ArrayList<Player>) playerRepository.getPlayerList()
                .stream().filter(player -> !team.getPlayers().contains(player))
                .filter(player -> !isInSubstitute(teamAndSubstitute.getSubstituteHistory(), player))
                .collect(Collectors.toList());

        if (!availablePlayers.contains(playerRepository.getPLayerByNumber(playerNumber))){
            throw new TeamException("Player already in the match or available !");
        }

        Random random = new Random();
        ArrayList<Player> playersInTeamInPosition = playerRepository.getPlayersByPosition(position, team.getPlayers());
        int ranNum = random.nextInt(playersInTeamInPosition.size());
        Player outPlayer = playersInTeamInPosition.get(ranNum);
        Player inPlayer = playerRepository.getPLayerByNumber(playerNumber);
        Substitute substitute = new Substitute(outPlayer, inPlayer);
        teamAndSubstitute.getSubstituteHistory().add(substitute);
        teamAndSubstitute.getCurrentTeam().getPlayers().remove(outPlayer);
        teamAndSubstitute.getCurrentTeam().getPlayers().add(inPlayer);

        return teamAndSubstitute;
    }

    private boolean isInSubstitute(List<Substitute> substituteHistory, Player player) {
        if (substituteHistory != null){
            for (Substitute substitute : substituteHistory) {
                if (substitute.getOutPlayer().equals(player))
                    return true;
            }
        }
        return false;
    }
}
