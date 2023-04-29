package com.codingame.game.engine;

import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import static com.codingame.game.engine.Constants.*;
import static com.codingame.game.engine.GameState.*;

import java.util.HashMap;

public class Viewer
{

    MultiplayerGameManager<Player> gameManager;
    GraphicEntityModule graphics;
    TooltipModule tooltipModule;
    public Rectangle[][] rectangles;

    Rectangle[] actionsHighlight;

    //Sprite[] playerPiece = new Sprite[2];
    RoundedRectangle[] playerHUDFrame = new RoundedRectangle[2];
    //public Text[] playerScore = new Text[2];
    public Text[] playerAction = new Text[2];
    public Text[] playerMessage = new Text[2];

    HashMap<Integer, Sprite> bottoms = new HashMap<>();
    HashMap<Integer, Sprite> crowns = new HashMap<>();

    int CIRCLE_RADIUS;
    int GAP;

    public Viewer(GraphicEntityModule graphics, MultiplayerGameManager<Player> gameManager, ToggleModule toggleModule, TooltipModule tooltipModule)
    {
        this.graphics = graphics;
        this.gameManager = gameManager;
        this.tooltipModule = tooltipModule;

        rectangles = new Rectangle[WIDTH][HEIGHT];
        actionsHighlight = new Rectangle[3];

        this.graphics.createRectangle().setWidth(VIEWER_WIDTH).setHeight(VIEWER_HEIGHT).setFillColor(BACKGROUNDCOLOR);

        int boardsize = VIEWER_HEIGHT-2*VIEWER_MARGIN;
        graphics.createSprite().setImage("board2.png").setX((VIEWER_WIDTH-boardsize)/2).setY(VIEWER_MARGIN).setZIndex(Z_BOARD).setBaseWidth(boardsize).setBaseHeight(boardsize);
        graphics.createSprite().setImage("traps.png").setX((VIEWER_WIDTH-boardsize)/2).setY(VIEWER_MARGIN).setZIndex(Z_TRAP).setBaseWidth(boardsize).setBaseHeight(boardsize);
        graphics.createSprite().setImage("dens.png").setX((VIEWER_WIDTH-boardsize)/2).setY(VIEWER_MARGIN).setZIndex(Z_DEN).setBaseWidth(boardsize).setBaseHeight(boardsize);

        int START_X = 595;
        int START_Y = 72;
        int FONT_SIZE = VIEWER_RECTANGLE_SIZE / 3;

        for (int y = 0; y < HEIGHT; ++y)
        {
            int yG = y;// HEIGHT - y - 1;
            for (int x = 0; x < WIDTH; ++x)
            {
                int xG = x;
                rectangles[x][y] = graphics.createRectangle().setZIndex(Z_SQUARE_RECT).setWidth(VIEWER_RECTANGLE_SIZE).setHeight(VIEWER_RECTANGLE_SIZE).setX(START_X + xG * VIEWER_RECTANGLE_SIZE).setY((int) START_Y + yG * VIEWER_RECTANGLE_SIZE);
                rectangles[x][y].setAlpha(0);
                if (traps.contains(x*10+ y)) tooltipModule.setTooltipText(rectangles[x][y], x + " " + y + " Trap");
                else if (dens[0]==(x*10+ y)) tooltipModule.setTooltipText(rectangles[x][y], x + " " + y + " " + PLAYER_NAMES[0] + " Den");
                else if (dens[1]==(x*10+ y)) tooltipModule.setTooltipText(rectangles[x][y], x + " " + y + " " + PLAYER_NAMES[1] + " Den");
                else tooltipModule.setTooltipText(rectangles[x][y], x + " " + y);
            }
        }


        //Sprite t1 =graphics.createSprite().setImage("0trap.png").setZIndex(Z_TRAP).setBaseWidth(VIEWER_RECTANGLE_SIZE).setBaseHeight(VIEWER_RECTANGLE_SIZE).setX(2+START_X + 2 * VIEWER_RECTANGLE_SIZE).setY((int) -4+START_Y + 8 * VIEWER_RECTANGLE_SIZE);

        CIRCLE_RADIUS = (int)(VIEWER_RECTANGLE_SIZE * .42);
        GAP = (VIEWER_RECTANGLE_SIZE - CIRCLE_RADIUS * 2) / 2;

        for (int i = 0; i < 3; ++i)
        {
            actionsHighlight[i] = graphics.createRectangle().setFillColor(HIGHLIGHTCOLOR[0]).setWidth(VIEWER_RECTANGLE_SIZE).setHeight(VIEWER_RECTANGLE_SIZE).setX(-VIEWER_RECTANGLE_SIZE).setY(-VIEWER_RECTANGLE_SIZE).setFillAlpha(i < 2 ? 0.75 : 0.45).setZIndex(2);
            toggleModule.displayOnToggleState(actionsHighlight[i], "debugToggle", true);
        }

        int HUDHEIGHT = 4*VIEWER_RECTANGLE_SIZE;
        int HUDWIDTH = 400;
        int START_PLAYER_X = 60;
        for (int p = 0; p < 2; ++p)
        {
            int START_YP = p==1? (START_Y) : (START_Y+9*VIEWER_RECTANGLE_SIZE - HUDHEIGHT);


            graphics.createRoundedRectangle().setHeight(HUDHEIGHT).setWidth(HUDWIDTH).setX(START_PLAYER_X).setY(START_YP)
                    .setFillColor(HUDCOLOR).setAlpha(0.15).setLineWidth(4);
            playerHUDFrame[p] = graphics.createRoundedRectangle().setHeight(HUDHEIGHT).setWidth(HUDWIDTH).setX(START_PLAYER_X).setY(START_YP).setFillAlpha(0)
                    .setLineColor(PLAYERTEXTCOLORS[p]).setLineAlpha(0.25).setLineWidth(14).setVisible(false);

            graphics.createText(gameManager.getPlayer(p).getNicknameToken()).setFontSize(50).setX(START_PLAYER_X + HUDWIDTH/2).setAnchorX(0.5).setY(START_YP + 25).setFillColor(PLAYERTEXTCOLORS[p]);
            graphics.createSprite().setImage(gameManager.getPlayer(p).getAvatarToken()).setX(START_PLAYER_X + HUDWIDTH/2).setY(START_YP + 130).setAnchorX(0.5).setBaseHeight(140).setBaseWidth(140);
            //playerPiece[p] = graphics.createSprite().setImage(p +".png").setX(START_PLAYER_X + HUDWIDTH/2 - CIRCLE_RADIUS).setY(START_YP + 270).setBaseWidth(CIRCLE_RADIUS * 2).setBaseHeight(CIRCLE_RADIUS * 2);
            //playerScore[p] = graphics.createText().setText("0").setFillColor(p==0?0x000000:0xffffff).setAnchorX(0.5).setAnchorY(0.5).setX(START_PLAYER_X + HUDWIDTH/2).setY(START_YP + 270 + CIRCLE_RADIUS).setFontSize(50).setFontWeight(Text.FontWeight.BOLD);
            playerAction[p] = graphics.createText().setText("").setFillColor(PLAYERTEXTCOLORS[p]).setAnchorX(0.5).setX(START_PLAYER_X + HUDWIDTH/2).setY(START_YP + 320).setFontSize(50);

            playerMessage[p] = graphics.createText().setText("").setFillColor(PLAYERTEXTCOLORS[p]).setAnchorX(0.5).setX(VIEWER_WIDTH-START_PLAYER_X-HUDWIDTH + HUDWIDTH/2).setY(START_Y+3*VIEWER_RECTANGLE_SIZE + 40).setFontSize(36);
        }

        graphics.createRoundedRectangle().setHeight(3*VIEWER_RECTANGLE_SIZE).setWidth(HUDWIDTH).setX(VIEWER_WIDTH-START_PLAYER_X-HUDWIDTH).setY(START_Y+3*VIEWER_RECTANGLE_SIZE).setFillColor(HUDCOLOR).setAlpha(0.15).setLineWidth(4);


        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                graphics.commitEntityState(0.0, rectangles[x][y]);
            }
        }

        for (int i = 0; i < 3; ++i) {
            actionsHighlight[i].setX(-VIEWER_RECTANGLE_SIZE, Curve.IMMEDIATE);
        }
    }


    public void initUnitSprite(Unit u)
    {
        u.sprite = graphics.createSprite().setImage(u.toTextureString()+".png").setZIndex(Z_UNIT).setBaseWidth(VIEWER_RECTANGLE_SIZE-2*VIEWER_UNIT_MARGIN).setBaseHeight(VIEWER_RECTANGLE_SIZE-2*VIEWER_UNIT_MARGIN);
        MoveUnitToXY(u, u.xy);
        tooltipModule.setTooltipText(u.sprite, u.toNameStringWithStrength() );
    }

    private Rectangle getRectangle(int xy)
    {
        return rectangles[X(xy)][Y(xy)];
    }

    public void MoveUnitToXY(Unit u, int xy2)
    {
        u.sprite.setZIndex(Z_UNIT_CAPTURING);
        graphics.commitEntityState(0, u.sprite);
        tooltipModule.setTooltipText(u.sprite, u.toNameStringWithStrength() );
        u.sprite.setX(getRectangle(xy2).getX()+VIEWER_UNIT_MARGIN).setY(getRectangle(xy2).getY()+VIEWER_UNIT_MARGIN).setZIndex(Z_UNIT);
        graphics.commitEntityState(1, u.sprite);
    }

    public void RemoveUnit(Unit u)
    {
        graphics.commitEntityState(0, u.sprite);
        u.sprite.setVisible(false);
        graphics.commitEntityState(1, u.sprite);
    }

    public void showHUDFrame(int p)
    {
        playerHUDFrame[p].setVisible(true);
        playerMessage[p].setVisible(true);
        graphics.commitEntityState(0, playerHUDFrame[p]);
        graphics.commitEntityState(0, playerMessage[p]);
        playerHUDFrame[p^1].setVisible(false);
        playerMessage[p^1].setVisible(false);
        graphics.commitEntityState(0, playerHUDFrame[p^1]);
        graphics.commitEntityState(0, playerMessage[p^1]);
    }
}
