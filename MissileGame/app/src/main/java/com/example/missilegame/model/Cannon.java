package com.example.missilegame.model;

import android.graphics.Rect;

//대포
public class Cannon {

    private float degree = 0;

    private Rect rect;
    private int pivot_x;
    private int pivot_y;

    public Cannon(int x, int y){

        rect = new Rect((int) (x*0.45),
                (int) (y*0.6),
                (int) (x*0.55),
                (int) (y*0.7));

        this.pivot_x = (int) (x/2);
        this.pivot_y = (int) (y*0.7);
    }

    //발사
    public Missile shoot(){
        Missile missile = new Missile(pivot_x, pivot_y,degree);
        return missile;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public int getPivot_x() {
        return pivot_x;
    }

    public int getPivot_y() {
        return pivot_y;
    }

    public Rect getRect() {
        return rect;
    }
}
