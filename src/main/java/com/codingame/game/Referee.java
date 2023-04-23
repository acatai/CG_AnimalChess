package com.codingame.game;

import com.codingame.game.engine.*;
import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Random;

import static com.codingame.game.engine.Constants.*;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphics;
    @Inject private ToggleModule toggleModule;
    @Inject TooltipModule tooltipModule;
    @Inject private EndScreenModule endScreenModule;

    Viewer viewer;
    Player currentPlayer;
    int lastAction = -1;
    Random rand;
    GameState mainState;

    @Override
    public void init()
    {
        rand = new Random(gameManager.getSeed());
        Constants.pregenerateMovements();
        viewer = new Viewer(graphics, gameManager, toggleModule, tooltipModule);
        gameManager.setMaxTurns(TURNLIMIT);
        gameManager.setFirstTurnMaxTime(TIMELIMIT_INIT);
        gameManager.setTurnMaxTime(TIMELIMIT_TURN);
        gameManager.setFrameDuration(500);
        lastAction = -1;
        currentPlayer = gameManager.getPlayer(0);

        GameState.viewer = viewer;
        GameState.manager = gameManager;

        //Unit.graphics = graphics;
        Unit.viewer = viewer;

        mainState = GameState.initial();

        gameManager.getPlayer(0).setScore(0);
        gameManager.getPlayer(1).setScore(0);
    }

    @Override
    public void gameTurn(int turn)
    {
        int p = currentPlayer.getIndex();
        Player player = gameManager.getPlayer(p);

        ArrayList<Integer> actions = mainState.generateLegalActions(p);
        viewer.showHUDFrame(p);

        try
        {
            sendInputs(turn, lastAction, player, actions);
            player.execute();

            String output = player.getOutputs().get(0).trim();
            String comment = null;

            if (output.equals("random"))
            {
                int i = rand.nextInt(actions.size());
                lastAction = actions.get(i);
                mainState.applyAction(player, actions.get(i));
            }
            else
            {
                // Split comment from output.
                String[] splitted = output.split(" ", 5);
                if (splitted.length > 4)
                {
                    if (splitted[4].length() > MAX_MESSAGE_LENGTH) splitted[4] = splitted[4].substring(0, MAX_MESSAGE_LENGTH);
                    comment = splitted[4].replaceAll("\\\\n", "\n");
                }

                if (comment != null) viewer.playerMessage[p].setText(comment);
                else viewer.playerMessage[p].setText("");

                int action = Action.parseAction(splitted);
                if (action < 0)
                    throw new InvalidActionException(String.format("Action \""+ output +"\"  was properly formatted! Expected: 'x1 y1 x2 y2 [message]'"));


                if (!actions.contains(action)) throw new InvalidActionException(String.format("Action \""+ output +"\"  was not legal!"));

                lastAction = action;
                mainState.applyAction(player, action);
            }

            viewer.playerAction[player.getIndex()].setText(Action.toString(lastAction));

        } catch (AbstractPlayer.TimeoutException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " did not output in time!"));
            player.deactivate(player.getNicknameToken() + " timeout.");
            player.setScore(-1);
            gameManager.endGame();
            return;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | InvalidActionException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " made an invalid action!"));
            player.deactivate(player.getNicknameToken() + " made an invalid action.");
            player.setScore(-1);
            gameManager.endGame();
            return;
        }

        if (mainState.isWin(p))
        {
            gameManager.getPlayer(p).setScore(1);
            gameManager.getPlayer(p ^ 1).setScore(-1);
            gameManager.endGame();
        }
        else
        {
            currentPlayer = gameManager.getPlayer(p ^ 1);
        }
    }

    void sendInputs(int turn, int lastaction, Player player, ArrayList<Integer> actions)
    {
        if (turn < 3) player.sendInputLine(player.getIndex() == 1 ? "blue" : "red");
        // Last action
        player.sendInputLine(Action.toString(lastaction));
        // Number of actions and actions themselves
        //actions.sort(Comparator.comparing(Integer::toString));
        player.sendInputLine(Integer.toString(actions.size()));
        for (int action : actions) player.sendInputLine(Action.toString(action));
        //for (int action : actions) System.err.println(Action.toString(action));
    }

    @Override
    public void onEnd()
    {
        int[] scores = { gameManager.getPlayer(0).getScore(), gameManager.getPlayer(1).getScore() };
        String[] text = new String[2];
        if(scores[0] > scores[1]) {
            gameManager.addToGameSummary(gameManager.formatSuccessMessage(gameManager.getPlayer(0).getNicknameToken() + " won!"));
            gameManager.addTooltip(gameManager.getPlayer(0), gameManager.getPlayer(0).getNicknameToken() + " won!");
            text[0] = "Won";
            text[1] = "Lost";
        } else if(scores[0] < scores[1]) {
            gameManager.addToGameSummary(gameManager.formatSuccessMessage(gameManager.getPlayer(1).getNicknameToken() + " won!"));
            gameManager.addTooltip(gameManager.getPlayer(1), gameManager.getPlayer(1).getNicknameToken() + " won!");
            text[0] = "Lost";
            text[1] = "Won";
        } else {
            gameManager.addToGameSummary(gameManager.formatErrorMessage("Game is drawn"));
            gameManager.addTooltip(gameManager.getPlayer(1), "Draw");
            text[0] = "Draw";
            text[1] = "Draw";
        }
        endScreenModule.setScores(scores, text);
   }
}
