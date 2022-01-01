package com.gamehouses;

import java.util.Objects;

public class XY
{
    public final int x, y;

    public XY(final int x , final int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof XY)
        {
            final XY xy = (XY) o;
            return x == xy.x && y == xy.y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x , y);
    }
}
