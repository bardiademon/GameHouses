package com.gamehouses.Gamer;

import java.util.Random;

public final class GameBody
{
    private Gamer gamer1, gamer2;

    public static int xyGameBody;

    private Home[][] homes = new Home[xyGameBody][xyGameBody];

    private int itsYourTrue = 0;

    private final static int MAX_BOMB = 4, MIN_STAR = 4, MAX_STAR = 8, MAX_OBSTACLE = 4;

    private final int maxBom, maxStar, minStar, maxObstacle;

    public static final int MAX_XY = 20, MIN_XY = 8;

    private GameBody(final Gamer gamer1 , final Gamer gamer2)
    {
        this.gamer1 = gamer1;
        this.gamer2 = gamer2;

        final int xyGameBodyDiv2 = xyGameBody / 2;

        maxBom = MAX_BOMB + xyGameBodyDiv2;
        maxStar = MAX_STAR + xyGameBodyDiv2;
        minStar = MIN_STAR + xyGameBodyDiv2;
        maxObstacle = MAX_OBSTACLE + xyGameBodyDiv2;

    }

    public static GameBody createGameBody(final String gamerName1 , final String gamerName2)
    {
        final GameBody gameBody = new GameBody(new Gamer(gamerName1) , new Gamer(gamerName2));
        gameBody.createHomes();
        gameBody.setItsYourTrue();
        return gameBody;
    }

    private void createHomes()
    {
        final Random random = new Random();

        int counterBomb = 0, counterStar = 0, counterObstacle = 0;

        for (int i = 0; i < xyGameBody * 2; )
        {
            if (counterBomb >= maxBom && counterStar >= maxStar) break;

            final Home.HomeType value = Home.HomeType.values()[random.nextInt(4)];
            final int xGameBody = random.nextInt(xyGameBody);
            final int yGameBody = random.nextInt(xyGameBody);

            if ((xGameBody == 0 && yGameBody == (xyGameBody - 1)) || (xGameBody == (xyGameBody - 1) && yGameBody == (xyGameBody - 1)))
            {
                continue;
            }

            if (checkAround(xGameBody , yGameBody , value))
            {
                if (value != Home.HomeType.gamer)
                {
                    final Home home = new Home();

                    int score;
                    switch (value)
                    {
                        case bomb:
                            score = random.nextInt(20);
                            if (score <= 0 || counterBomb++ >= maxBom) continue;
                            home.setValue(new Bombs(xGameBody , yGameBody , score));
                            break;
                        case star:
                            score = random.nextInt(5);
                            if (score <= 0 || counterStar++ >= maxStar) continue;
                            home.setValue(new Stars(xGameBody , yGameBody , score));
                            break;
                        case obstacle:
                            if (counterObstacle++ >= maxObstacle) continue;
                        case empty:
                        default:
                            break;
                    }

                    home.setHomeType(value);

                    homes[xGameBody][yGameBody] = home;

                    if (counterStar >= minStar) i++;
                }
            }
        }

        for (int row = 0; row < homes.length; row++)
        {
            for (int column = 0; column < homes[row].length; column++)
            {
                if (homes[row][column] == null)
                {
                    final Home home = new Home();
                    home.setHomeType(Home.HomeType.empty);
                    homes[row][column] = home;
                }
            }
        }
    }

    private boolean checkAround(int x , int y , Home.HomeType homeType)
    {
        return (checkAround(getHome(x - 1 , y - 1) , homeType)
                && checkAround(getHome(x , y - 1) , homeType)
                && checkAround(getHome(x - 1 , y) , homeType)
                && checkAround(getHome(x + 1 , y + 1) , homeType)
                && checkAround(getHome(x + 1 , y) , homeType)
                && checkAround(getHome(x , y + 1) , homeType)
        );
    }

    private boolean checkAround(Home home , Home.HomeType homeType)
    {
        return (home == null || !home.getHomeType().equals(homeType));
    }

    private Home getHome(int x , int y)
    {
        try
        {
            return homes[x][y];
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Gamer getGamer1()
    {
        return gamer1;
    }

    public Gamer getGamer2()
    {
        return gamer2;
    }

    public void setItsYourTrue()
    {
        if (itsYourTrue != 1) itsYourTrue = 1;
        else itsYourTrue = 2;
    }

    public Home[][] getHomes()
    {
        return homes;
    }

    public String getItsYourTrue()
    {
        switch (itsYourTrue)
        {
            case 1:
                return gamer1.name;
            case 2:
                return gamer2.name;
            default:
                return "";
        }
    }

    public Gamer getGamerItsYourTrue()
    {
        switch (itsYourTrue)
        {
            case 1:
                return gamer1;
            case 2:
                return gamer2;
            default:
                return null;
        }
    }

    public int getNumberItsYourTrue()
    {
        return itsYourTrue;
    }

    public String getColorItsYourTrue()
    {
        switch (itsYourTrue)
        {
            case 1:
                return "#136c32";
            case 2:
                return "#6c121e";
            default:
                return "#000000";
        }
    }

    public void setGamer(final Gamer gamer)
    {
        switch (itsYourTrue)
        {
            case 1:
                this.gamer1 = gamer;
                break;
            case 2:
                this.gamer2 = gamer;
                break;
            default:
                break;
        }
    }

    public void setHomes(Home[][] homes)
    {
        this.homes = homes;
    }

}
