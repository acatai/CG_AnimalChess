import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main
{
    public static void main(String[] args)
    {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        //Choose league level
        gameRunner.setLeagueLevel(1);

        //Add players
        gameRunner.addAgent(PlayerRandom.class, "Player One");
        gameRunner.addAgent(PlayerRandomForward.class, "Player Two");
        //gameRunner.addAgent(PlayerGreedy.class, "Player Two");

        //Set game seed
        gameRunner.setSeed(5842184981578562717L);

        //Run game and start viewer on 'http://localhost:8888/'
        gameRunner.start(8888);
    }
}
