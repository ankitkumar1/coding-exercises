package ankit.lld.teamstrength;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BalancedTeamResponse {
    private final List<Team> teams;
    private final int totalStrength;
    private final int diffStrength;

    public BalancedTeamResponse(Builder builder){
        this.teams = builder.teams;
        this.totalStrength = builder.totalStrength;
        this.diffStrength = builder.diffStrength;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public int getTotalStrength() {
        return totalStrength;
    }

    public int getDiffStrength() {
        return diffStrength;
    }

    public static class Builder{
        private List<Team> teams;
        private int totalStrength;
        private int diffStrength;

        public Builder teams(Team... teams){
            this.teams = new ArrayList<>();
            this.teams.addAll(Arrays.asList(teams));
            return this;
        }

        public Builder totalStrength(int totalStrength){
            this.totalStrength = totalStrength;
            return this;
        }

        public Builder diffStrength(int diffStrength){
            this.diffStrength = diffStrength;
            return this;
        }

        public BalancedTeamResponse build(){
            return new BalancedTeamResponse(this);
        }
    }
}
