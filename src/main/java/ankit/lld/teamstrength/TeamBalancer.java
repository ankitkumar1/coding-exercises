package ankit.lld.teamstrength;

import java.util.List;

public interface TeamBalancer {
    BalancedTeamResponse balancedTeam(List<Player> players, int k);
}
