package ankit.lld.teamstrength;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultTeamBalancer implements TeamBalancer{

    private int teamId = 1;

    @Override
    public BalancedTeamResponse balancedTeam(List<Player> players, int k) {
        var teams = generateTeams(players, k);
        if(teams.size() < 2){
            throw new IllegalArgumentException("Minimum 2 teams needed for balancing!");
        }
        int maxTotal = Integer.MIN_VALUE;
        int minDiff = Integer.MAX_VALUE;
        List<Team> finalTeams = Collections.emptyList();
        for(int i=0; i<teams.size(); i++){
            Team team1 = teams.get(i);
            for(int j=i+1; j<teams.size(); j++){
                Team team2 = teams.get(j);
                if(team1.hasOverlap(team2)){
                    continue;
                }
                int strength = team1.getTeamStrength() + team2.getTeamStrength();
                int diff = Math.abs(team1.getTeamStrength() - team2.getTeamStrength());
                if(strength > maxTotal || (strength == maxTotal && diff < minDiff)){
                    maxTotal = strength;
                    minDiff = diff;
                    finalTeams = List.of(team1, team2);
                }
            }
        }
        return new BalancedTeamResponse.Builder().totalStrength(maxTotal)
                .diffStrength(minDiff).teams(finalTeams.getFirst(), finalTeams.getLast())
                .build();
    }

    public List<Team> generateTeams(List<Player> players, int k) {
        var teams = new ArrayList<Team>();
        generateAllCombinations(0, new ArrayList<>(k), players, k, teams);
        return teams;
    }

    private void generateAllCombinations(int index, ArrayList<Player> teamPlayers, List<Player> players,
                                         int k, ArrayList<Team> teams) {
        if(teamPlayers.size() == k){
            Team team = new Team(teamId++);
            team.addPlayers(teamPlayers);
            teams.add(team);
            return;
        }
        for(int i=index; i<players.size(); i++){
            teamPlayers.add(players.get(i));
            generateAllCombinations(i+1, teamPlayers, players, k, teams);
            teamPlayers.removeLast();
        }
    }
}
