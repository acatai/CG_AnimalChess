package com.codingame.game.engine;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;

import java.lang.*;

import static com.codingame.game.engine.Constants.*;
import static jdk.nashorn.internal.objects.NativeString.toUpperCase;


public class Unit
{
  //public static GraphicEntityModule graphics;
  public static Viewer viewer;

  int xy;
  int faction;
  UType type;

  boolean alive = true;

  public Sprite sprite;


  public enum UType
  {
    Rat, Cat, Wolf, Dog, Panther, Tiger, Lion, Elephant
  }

  public boolean strongerThan(Unit u)
  {
    if (type== UType.Rat && u.type==UType.Elephant) return true;
    if (type== UType.Elephant && u.type==UType.Rat) return false;
    return  type.ordinal() >= u.type.ordinal();
  }



  public Unit(int xy, int faction, UType type)
  {
    this.xy = xy;
    this.faction = faction;
    this.type = type;
    viewer.initUnitSprite(this);
  }


  @Override
  public java.lang.String toString()
  {
    char c = type.name().charAt(0);
    return (faction==0 ? toUpperCase(c) : c)+"";
  }

  public java.lang.String toTextureString()
  {
    char c = type.name().charAt(0);
    return faction+""+c;
  }

  public java.lang.String toNameString()
  {
    return PLAYER_NAMES[faction]+" "+type.name();
  }

  public java.lang.String toNameStringWithStrength()
  {
    return PLAYER_NAMES[faction]+" "+type.name() + " ("+( GameState.traps.contains(xy)? 0 :type.ordinal()+1)+")";
  }

}
