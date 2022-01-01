package com.gamehouses.Gamer;

import com.gamehouses.XY;

public final class Gamer
{
    public final String name;

    private int score;

    private XY xy;

    public Gamer(final String name)
    {
        this.name = name;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public void setXy(XY xy)
    {
        this.xy = xy;
    }

    public XY getXy()
    {
        return xy;
    }
}
