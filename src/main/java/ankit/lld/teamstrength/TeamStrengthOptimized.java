package ankit.lld.teamstrength;

import java.util.ArrayList;
import java.util.Comparator;

public class TeamStrengthOptimized {

    private final TeamBalancer teamBalancer;

    TeamStrengthOptimized(TeamBalancer teamBalancer){
        this.teamBalancer = teamBalancer;
    }


    public BalancedTeamResponse buildTeam(int n, int k, int[] playerStrength, int[] playerIds){
        var players = new ArrayList<Player>();
        for(int i=0; i<playerStrength.length; i++){
            players.add(new Player(playerIds[i], playerStrength[i]));
        }
        players.sort(Comparator.comparing(Player::getStrength).reversed());
        return teamBalancer.balancedTeam(players.subList(0, 2*k), k);
    }
}
