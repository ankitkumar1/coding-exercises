package ankit.lld.teamstrength;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
    private final int teamId;
    private final List<Player> players;
    private int teamStrength;

    public Team(int teamId){
        this.teamId = teamId;
        this.players  = new ArrayList<>();
    }

    public int getTeamId() {
        return teamId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTeamStrength() {
        return teamStrength;
    }

    public void addPlayerToTeam(Player player){
        this.teamStrength += player.getStrength();
        this.players.add(player);
    }

    public void addPlayers(List<Player> players){
        players.forEach(this::addPlayerToTeam);

    }

    public boolean hasOverlap(Team other) {
        return !Collections.disjoint(players, other.getPlayers());
        //return players.stream().anyMatch(p -> other.players.stream().anyMatch(t -> t.equals(p)));
    }
}
