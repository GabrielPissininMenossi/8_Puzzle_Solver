package com.unoeste.puzzle8solver;

public class Estado
{
    private float x,y;
    private int num;

    public Estado(float x, float y, int num) {
        this.x = x;
        this.y = y;
        this.num = num;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
