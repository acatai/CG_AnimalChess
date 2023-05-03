package com.codingame.game.engine;

import java.util.ArrayList;
import java.util.HashMap;

/*
Game tags: Minimax, Bitboarding, Monte Carlo Tree Search, Neural network
 */

/*
TODO TEST CGBenchmark
Open your browser console et look for the request to generateSessionFromPuzzlePrettyId when the IDE is loading
In the payload, the 2nd parameter is the game name, the one you need to put in the config file
 */
public class Constants
{
  public static int WIDTH = 7;
  public static int HEIGHT = 9;


  public static int XYBASE = 10;


  public static int XY(int x, int y) {return x*XYBASE+y;}
  public static int X(int xy) {return xy/XYBASE;}
  public static int Y(int xy) {return xy%XYBASE;}

  public static int X1(int xyxy) {return X(xyxy/(XYBASE*XYBASE));}
  public static int Y1(int xyxy) {return Y(xyxy/(XYBASE*XYBASE));}

  public static int X2(int xyxy) {return X(xyxy%(XYBASE*XYBASE));}
  public static int Y2(int xyxy) {return Y(xyxy%(XYBASE*XYBASE));}


  public static int TURNLIMIT = 150;
  public static int TIMELIMIT_INIT = 1000;
  public static int TIMELIMIT_TURN = 150;

  ///////////////////////////////////////////////////
  // VISUALIZATION
  ///////////////////////////////////////////////////
  public static int VIEWER_WIDTH = 1920;
  public static int VIEWER_HEIGHT = 1080;
  public static int VIEWER_MARGIN = 10;

  public static int MAX_MESSAGE_LENGTH = 50;


  public static int VIEWER_RECTANGLE_SIZE = 104;// VIEWER_HEIGHT / -~HEIGHT;
  public static int VIEWER_UNIT_MARGIN = 7;

  final static String[] PLAYER_NAMES = new String[] {"Red","Blue", };
  final static int[] PLAYERTEXTCOLORS = new int[] {0xd0332d,0x0e36ff, };
  final static int[] BOARDCOLORS = new int[] {0xb5d2a8,0x84b073, }; //{0xD2C2A8, 0xB09673}; // {0xF0D9B5, 0x946f51};
  final static int[] HIGHLIGHTCOLOR = new int[]{0xd7cd7e, 0xf2e041, 0xe0b91b};
  final static int BACKGROUNDCOLOR = 0xceae7c; // 0x7F7F7F;
  final static int HUDCOLOR = 0x008d00;


  public static int Z_BOARD = 100;
  public static int Z_DEN = 101;
  public static int Z_UNIT = 102;
  public static int Z_UNIT_CAPTURING = 103;
  public static int Z_TRAP = 104;
  public static int Z_SQUARE_RECT = 110;



  ///////////////////////////////////////
  // OTHER CONSTANTS AND HELPER FUNCTIONS
  ///////////////////////////////////////

  public static final ArrayList<Integer>[] MOVES_GROUND = new ArrayList[10*10];
  public static final ArrayList<Integer>[] MOVES_ALL = new ArrayList[10*10];

  public static final HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> JUMPS= new HashMap<>();


  public static void pregenerateMovements()
  {
    int x1, y1;
    for (int x=0; x < WIDTH; x++)
    {
      for (int y=0; y < HEIGHT; y++)
      {
        int xy = XY(x, y);
        MOVES_GROUND[xy] = new ArrayList<>();
        if (x >0 && !GameState.water.contains(XY(x-1,y)) ) MOVES_GROUND[xy].add(XY(x-1,y));
        if (x <WIDTH-1 && !GameState.water.contains(XY(x+1,y)) ) MOVES_GROUND[xy].add(XY(x+1,y));
        if (y >0 && !GameState.water.contains(XY(x,y-1)) ) MOVES_GROUND[xy].add(XY(x,y-1));
        if (y <HEIGHT-1 && !GameState.water.contains(XY(x,y+1)) ) MOVES_GROUND[xy].add(XY(x,y+1));

        MOVES_ALL[xy] = new ArrayList<>();
        if (x >0  ) MOVES_ALL[xy].add(XY(x-1,y));
        if (x <WIDTH-1 ) MOVES_ALL[xy].add(XY(x+1,y));
        if (y >0  ) MOVES_ALL[xy].add(XY(x,y-1));
        if (y <HEIGHT-1  ) MOVES_ALL[xy].add(XY(x,y+1));
      }
    }

    HashMap<Integer, ArrayList<Integer>> tmp;

    for (int x : new int[] {1, 2, 4, 5})
    {
      tmp = new HashMap<>();
      tmp.put(XY(x, 6), new ArrayList<Integer>() {{ add(XY(x,3)); add(XY(x,4)); add(XY(x,5)); }});
      JUMPS.put(XY(x, 2), tmp);

      tmp = new HashMap<>();
      tmp.put(XY(x, 2), new ArrayList<Integer>() {{ add(XY(x,3)); add(XY(x,4)); add(XY(x,5)); }});
      JUMPS.put(XY(x, 6), tmp);
    }

    for (int y : new int[] {3, 4, 5})
    {
      tmp = new HashMap<>();
      tmp.put(XY(3, y), new ArrayList<Integer>() {{ add(XY(1,y)); add(XY(2,y)); }});
      JUMPS.put(XY(0, y), tmp);

      tmp = new HashMap<>();
      tmp.put(XY(3, y), new ArrayList<Integer>() {{ add(XY(4,y)); add(XY(5,y)); }});
      JUMPS.put(XY(6, y), tmp);

      tmp = new HashMap<>();
      tmp.put(XY(0, y), new ArrayList<Integer>() {{ add(XY(1,y)); add(XY(2,y)); }});
      tmp.put(XY(6, y), new ArrayList<Integer>() {{ add(XY(4,y)); add(XY(5,y)); }});
      JUMPS.put(XY(3, y), tmp);
    }
  }
}
