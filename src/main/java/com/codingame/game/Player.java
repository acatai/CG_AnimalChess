package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer
{
    public int expectedOutputLines = 1;

    public void endGame() { expectedOutputLines = 0; }

    @Override
    public int getExpectedOutputLines() {
        return expectedOutputLines;
    }
}