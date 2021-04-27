package com.onemount.footballteam.repository;

import com.onemount.footballteam.model.Position;
import com.onemount.footballteam.model.Player;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PlayerRepository {

    private ArrayList<Player> players;

    public PlayerRepository(){
        players = new ArrayList<>();
        players.add(new Player(1, "Marc-André ter Stegen", Position.GK));
        players.add(new Player(2, "Sergiño Dest", Position.DF));
        players.add(new Player(3, "Gerard Piqué", Position.DF));
        players.add(new Player(4, "Ronald Araújo", Position.DF));
        players.add(new Player(5, "Sergio Busquets", Position.MF));
        players.add(new Player(6, "Antoine Griezmann", Position.FW));
        players.add(new Player(7, "Miralem Pjanić", Position.MF));
        players.add(new Player(8, "Martin Braithwaite", Position.FW));
        players.add(new Player(9, "Lionel Messi", Position.FW));
        players.add(new Player(10, "Ousmane Dembélé", Position.FW));
        players.add(new Player(11, "Riqui Puig", Position.MF));
        players.add(new Player(12, "Neto", Position.GK));
        players.add(new Player(13, "Clément Lenglet", Position.DF));
        players.add(new Player(14, "Pedri", Position.MF));
        players.add(new Player(15, "Francisco Trincão", Position.FW));
        players.add(new Player(16, "Jordi Alba", Position.DF));
        players.add(new Player(17, "Matheus Fernandes", Position.MF));
        players.add(new Player(18, "Sergi Roberto", Position.DF));
        players.add(new Player(19, "Frenkie de Jong", Position.MF));
        players.add(new Player(20, "Ansu Fati", Position.FW));
        players.add(new Player(21, "Samuel Umtiti", Position.DF));
        players.add(new Player(22, "Junior Firpo", Position.DF));
    }

    public List<Player> getPlayerList(){
        return players;
    }

    public ArrayList<Player> getPlayersByPosition(Position position){
        return (ArrayList<Player>) players.stream()
                .filter(player -> player.getPosition().equals(position))
                .collect(Collectors.toList());
    }

    public List<Player> getUniquePlayersByPosition(Position position, int number) {
        ArrayList<Player> selectedPlayers = new ArrayList<>();
        List<Player> poolOfPlayers = getPlayersByPosition(position);

        if (number > poolOfPlayers.size()) {
            throw new IllegalArgumentException(number + " is more than available players");
        }

        int[] indexOfPlayer = new Random().ints(0, poolOfPlayers.size()).distinct().limit(number).toArray();

        for (int index : indexOfPlayer) {
            selectedPlayers.add(poolOfPlayers.get(index));
        }

        return selectedPlayers;
    }

    public Player getPLayerByNumber(int playerNumber){
        for (Player player: players) {
            if(player.getNum() == playerNumber)
                return player;
        }
        return null;
    }
}
