package com.codingame.game.engine;

import com.codingame.game.Player;
import com.codingame.gameengine.core.GameManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static com.codingame.game.engine.Constants.*;

public class GameState
{
  public static Viewer viewer;
  public static GameManager manager;

  public static HashSet<Integer> water = new HashSet<Integer>(){{add(13);add(23);add(14);add(24);add(15);add(25);add(43);add(53);add(44);add(54);add(45);add(55);}};
  public static HashSet<Integer> traps = new HashSet<Integer>(){{add(20);add(31);add(40); add(28);add(37);add(48);}};
  public static Integer[] dens = new Integer[]{38, 30};

  public ArrayList<Unit> units = new ArrayList<>();

  public static final int[] score = new int[] {0, 0}; // todo

  public GameState() {}

  public static GameState initial()
  {
    GameState s = new GameState();

    s.units.add(new Unit( 6, 0, Unit.UType.Elephant));
    s.units.add(new Unit(68, 0, Unit.UType.Lion));
    s.units.add(new Unit( 8, 0, Unit.UType.Tiger));
    s.units.add(new Unit(46, 0, Unit.UType.Panther));
    s.units.add(new Unit(26, 0, Unit.UType.Wolf));
    s.units.add(new Unit(57, 0, Unit.UType.Dog));
    s.units.add(new Unit(17, 0, Unit.UType.Cat));
    s.units.add(new Unit(66, 0, Unit.UType.Rat));

    s.units.add(new Unit(62, 1, Unit.UType.Elephant));
    s.units.add(new Unit( 0, 1, Unit.UType.Lion));
    s.units.add(new Unit(60, 1, Unit.UType.Tiger));
    s.units.add(new Unit(22, 1, Unit.UType.Panther));
    s.units.add(new Unit(42, 1, Unit.UType.Wolf));
    s.units.add(new Unit(11, 1, Unit.UType.Dog));
    s.units.add(new Unit(51, 1, Unit.UType.Cat));
    s.units.add(new Unit( 2, 1, Unit.UType.Rat));

    return s;
  }

  public GameState copy() // todo??
  {
    GameState s = new GameState();
    return s;
  }



  public ArrayList<Integer> generateLegalActions(int player)
  {
    ArrayList<Integer> actions = new ArrayList<>();

    for (Unit u:units)
    {
      if (u.faction != player) continue;

      for (int xy2: u.type== Unit.UType.Rat ? MOVES_ALL[u.xy] :  MOVES_GROUND[u.xy])
        verifyActionTarget(actions, player, xy2, u);

      if ((u.type== Unit.UType.Tiger || u.type== Unit.UType.Lion) && JUMPS.containsKey(u.xy) )
      {
        for (Map.Entry<Integer, ArrayList<Integer>> jump:JUMPS.get(u.xy).entrySet())
        {
          //System.err.println("jump " + u.xy + " " + jump.getKey());
          if (units.stream().filter(o -> jump.getValue().contains(o.xy)).findFirst().isPresent()) continue; // cannot jump over a piece
          verifyActionTarget(actions, player, jump.getKey(), u);
        }
      }
    }
    return actions;
  }

  private void verifyActionTarget(ArrayList<Integer> actions, int player, int xy2, Unit u)
  {
    if (xy2 == dens[player]) return; // cannot move to own den
    if (units.stream().filter(o -> o.faction == player && o.xy==xy2).findFirst().isPresent()) return; // cannot move on own piece

    Optional<Unit> op = units.stream().filter(o -> o.xy==xy2).findFirst();
    if (op.isPresent() && !(u.strongerThan(op.get()) || traps.contains(xy2))) return; // cannot capture stronger piece
    if (op.isPresent() && water.contains(u.xy) && !water.contains(xy2)) return; // cannot capture from water to not water

    //System.err.println(u.xy + " " + xy2);
    int a = u.xy*XYBASE*XYBASE+xy2;
    actions.add(a);
    //System.err.println(Action.toString(a));
  }

  public boolean isWin(int player)
  {
    return units.stream().filter(o -> o.faction == player && o.xy==dens[1-player]).findFirst().isPresent();
  }


  public void applyAction(Player player, int action)
  {
    int p = player.getIndex();

    //return String.format("%d %d %d %d", X1(action), Y1(action), X2(action), Y2(action));

    int xy = action/(XYBASE*XYBASE);
    int xy2 = action%(XYBASE*XYBASE);

    Optional<Unit> op = units.stream().filter(o -> o.xy==xy2).findFirst();
    if (op.isPresent())
    {
      viewer.RemoveUnit(op.get());
      units.remove(op.get());
    }

    Optional<Unit> u = units.stream().filter(o -> o.xy==xy).findFirst();
    if (u.isPresent())
    {
      u.get().xy = xy2;
      viewer.MoveUnitToXY(u.get(), u.get().xy);
      System.err.println(Action.toString(action));
    }

    manager.addToGameSummary(player.getNicknameToken() + " moved " + u.get().toNameString()+ " from " + X(xy)+" "+Y(xy) + " to " + X(xy2)+" "+Y(xy2)+(op.isPresent()? (" (capturing "+op.get().toNameString()+")."):"."));
  }

  public static String toStr(int x, int y)
  {
    return ((char)(97 + x))+ "" + (y+1);
  }

  public static String toStr(int xy)
  {
    return ((char)(97 + xy%WIDTH))+ "" + ((xy/WIDTH)+1);
  }
}
