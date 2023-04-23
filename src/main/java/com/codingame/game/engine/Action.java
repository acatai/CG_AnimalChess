package com.codingame.game.engine;
import static com.codingame.game.engine.Constants.*;

public class Action
{

  public static int parseAction(String[] splitted)
  {
    if (splitted.length < 4) return -2;

    try
    {
      int x1 = Integer.parseInt(splitted[0]);
      int y1 = Integer.parseInt(splitted[1]);
      int x2 = Integer.parseInt(splitted[2]);
      int y2 = Integer.parseInt(splitted[3]);

      return XY(x1, y1) * XYBASE*XYBASE + XY(x2, y2);

    } catch (Exception e)
    {
      return -2;
    }
  }

  public static String toString(int action)
  {
    if (action < 0) return "-1 -1 -1 -1";
    return String.format("%d %d %d %d", X1(action), Y1(action), X2(action), Y2(action));
  }
}
