package ankit.lld.teamstrength;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TeamStrengthTest {

    @Test
    public void test1(){
        TeamStrengthNaive teamBuilder = new TeamStrengthNaive(new DefaultTeamBalancer());
        BalancedTeamResponse response = teamBuilder.buildTeam(5, 2, new int[]{4, 4, 2, 2, 5},
                new int[]{1, 2, 3, 4, 5});

        assertEquals(15, response.getTotalStrength());
        assertEquals(1, response.getDiffStrength());
    }

    @Test
    public void test2(){
        TeamStrengthOptimized teamBuilder = new TeamStrengthOptimized(new OptimizedTeamBalancer());
        BalancedTeamResponse response = teamBuilder.buildTeam(5, 2, new int[]{4, 4, 2, 2, 5},
                new int[]{1, 2, 3, 4, 5});

        assertEquals(15, response.getTotalStrength());
        assertEquals(1, response.getDiffStrength());
    }
}
