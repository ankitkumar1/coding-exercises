package ankit.lld.teamstrength;

public class Player {
    private final int playerId;
    private final int strength;

    public Player(int playerId, int strength){
        this.playerId = playerId;
        this.strength = strength;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public String toString(){
        return ""+this.getPlayerId();
    }
}
