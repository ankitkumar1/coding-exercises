package ankit.lld.teamstrength;

import java.util.ArrayList;

public class TeamStrengthNaive {

    private final TeamBalancer teamBalancer;

    TeamStrengthNaive(TeamBalancer teamBalancer){
        this.teamBalancer = teamBalancer;
    }


    public BalancedTeamResponse buildTeam(int n, int k, int[] playerStrength, int[] playerIds){
        var players = new ArrayList<Player>();
        for(int i=0; i<playerStrength.length; i++){
            players.add(new Player(playerIds[i], playerStrength[i]));
        }
        return teamBalancer.balancedTeam(players, k);
    }
}
