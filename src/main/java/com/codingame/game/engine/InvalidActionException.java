package com.codingame.game.engine;

public class InvalidActionException extends Exception
{
    private static final long serialVersionUID = -8185589153224401565L;

    public InvalidActionException(String message)
    {
        super(message);
    }
}