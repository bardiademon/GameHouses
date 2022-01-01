package com.gamehouses.Gamer;

public class Home
{
    private HomeType homeType;
    private Object value;

    public Home()
    {
    }

    public HomeType getHomeType()
    {
        return homeType;
    }

    public void setHomeType(HomeType homeType)
    {
        this.homeType = homeType;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public enum HomeType
    {
        gamer, bomb, star, obstacle, empty
    }

    @Override
    public String toString()
    {
        return homeType.name();
    }
}
