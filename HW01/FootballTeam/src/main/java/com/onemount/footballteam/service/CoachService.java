package com.onemount.footballteam.service;

import com.onemount.footballteam.exception.TeamException;
import com.onemount.footballteam.model.*;
import com.onemount.footballteam.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoachService {

    @Autowired
    private PlayerRepository playerRepository;

    private Team team;
    private TeamStatus teamAndSubstitute;

    public CoachService() {
        team = new Team();
    }

    public Set<Player> chooseTeam(String teamPattern) throws TeamException {

        if (teamPattern.length() != 3) {
            throw new TeamException("Team pattern is not in right format");
        }

        Set<Player> teamPlayers = new HashSet<>();
//        List<Player> players = playerRepository.getPlayerList();
        List<Player> GKPlayers = playerRepository.getPlayersByPosition(Position.GK);
        List<Player> MFPlayers = playerRepository.getPlayersByPosition(Position.MF);
        List<Player> FWPlayers = playerRepository.getPlayersByPosition(Position.FW);
        List<Player> DFPlayers = playerRepository.getPlayersByPosition(Position.DF);

        Random rand = new Random();
        try{
            int GKNum = rand.nextInt(GKPlayers.size());
            int numDF = Integer.parseInt(String.valueOf(teamPattern.charAt(0)));
            int numMF = Integer.parseInt(String.valueOf(teamPattern.charAt(1)));
            int numFW = Integer.parseInt(String.valueOf(teamPattern.charAt(2)));


            if(numDF + numFW + numMF != 10){
                throw new TeamException("TeamException : Total number of players is not 11", "x + y + z must be 10");
            }

            if(numDF > DFPlayers.size()){
                throw new TeamException("TeamException : Request players more than available", "Requested: " + numDF + ", Available: "+ DFPlayers.size());
            }

            if(numMF > MFPlayers.size()){
                throw new TeamException("TeamException : Request players more than available", "Requested: " + numMF + ", Available: "+ MFPlayers.size());
            }

            if(numFW > FWPlayers.size()){
                throw new TeamException("TeamException : Request players more than available", "Requested: " + numFW + ", Available: "+ FWPlayers.size());
            }

            if (numFW == 0 || numDF == 0 || numMF == 0){
                throw new TeamException("TeamException : Invalid number format in team pattern");
            }

            // Choose GK
            teamPlayers.add(GKPlayers.get(GKNum));

            // Choose DF Players
            int[] DFNum = new Random().ints(0, DFPlayers.size()).distinct().limit(numDF).toArray();
            for (int num : DFNum) {
                teamPlayers.add(DFPlayers.get(num));
            }

            // Choose MF Players
            int[] MFNum = new Random().ints(0, MFPlayers.size()).distinct().limit(numMF).toArray();
            for (int num : MFNum) {
                teamPlayers.add(MFPlayers.get(num));
            }

            // Choose FW Players
            int[] FWNum = new Random().ints(0, FWPlayers.size()).distinct().limit(numFW).toArray();
            for (int num : FWNum) {
                teamPlayers.add(FWPlayers.get(num));
            }

        }catch (NumberFormatException ex){
            throw new TeamException("TeamException : Invalid number format in team pattern");
        }

        team.setPlayers(teamPlayers);
        teamAndSubstitute = new TeamStatus();
        teamAndSubstitute.setCurrentTeam(teamPlayers);
        return team.getPlayers();
    }

    public Team getTeam() {
        return team;
    }

    public Map<String, Set<Player>> getTeamGroup(){
        if(team == null){
            throw new TeamException("Team is not formed yet");
        }
        Map<String, Set<Player>> teamGroup = new HashMap<String, Set<Player>>();
        Set<Player> GKPlayers = team.getPlayers().stream()
                .filter(player -> player.getPosition().equals(Position.GK))
                .collect(Collectors.toSet());
        Set<Player> FWPlayers = team.getPlayers().stream()
                .filter(player -> player.getPosition().equals(Position.FW))
                .collect(Collectors.toSet());
        Set<Player> DFPlayers = team.getPlayers().stream()
                .filter(player -> player.getPosition().equals(Position.DF))
                .collect(Collectors.toSet());
        Set<Player> MFPlayers = team.getPlayers().stream()
                .filter(player -> player.getPosition().equals(Position.MF))
                .collect(Collectors.toSet());
        teamGroup.put("GK", GKPlayers);
        teamGroup.put("FW", FWPlayers);
        teamGroup.put("DF", DFPlayers);
        teamGroup.put("MF", MFPlayers);

        return teamGroup;
    }


    public TeamStatus substitute(int playerNumber, Position position) throws TeamException {

        if (team == null || teamAndSubstitute == null) {
            throw new TeamException("Team is not formed yet");
        }

        if (teamAndSubstitute.getSubstituteHistory()!= null && teamAndSubstitute.getSubstituteHistory().size()  == 5) {
            throw new TeamException("TeamException : Number of substitutions has reached 5");
        }

        Player outPlayer = team.getPlayers().stream()
                .filter(p -> p.getNum() == playerNumber)
                .findAny()
                .orElse(null);

        if(outPlayer == null){
            throw new TeamException("TeamException : No current player with that number", String.valueOf(playerNumber));
        }

        ArrayList<Player> availablePlayers = (ArrayList<Player>) playerRepository.getPlayersByPosition(position)
                .stream()
                .filter(player -> !team.getPlayers().contains(player)
                        && !isInSubstitute(teamAndSubstitute.getSubstituteHistory(), player))
                .collect(Collectors.toList());

        if(availablePlayers.size() == 0){
            throw new TeamException("TeamException : No player is available for this position", position.toString());
        }

        Random random = new Random();
        int ranNum = random.nextInt(availablePlayers.size());
        Player inPlayer = availablePlayers.get(ranNum);
        Substitute substitute = new Substitute(outPlayer, inPlayer);
        teamAndSubstitute.getSubstituteHistory().add(substitute);
        teamAndSubstitute.getCurrentTeam().remove(outPlayer);
        teamAndSubstitute.getCurrentTeam().add(inPlayer);

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
