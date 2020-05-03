package com.example.missilegame.model;

//미사일
public class Missile {

    private int speed = 40;
    private float degree;

    private int cur_x;  //현재 위치 x
    private int cur_y;  //현재 위치 y

    public Missile(int x, int y, float degree){
        this.cur_x = x;
        this.cur_y = y;
        this.degree = degree;
    }

    //이동
    public void move(){
        //이동 계산
        //http://www.devpia.com/MAEUL/Contents/Detail.aspx?BoardID=50&MAEULNo=20&no=567310&ref=567310
        double a = ((degree+90)*3.14159265359)/180;
        cur_x = (int) (cur_x - (speed * Math.cos(a)));
        cur_y = (int) (cur_y - (speed * Math.sin(a)));
    }


    public int getCur_x() {
        return cur_x;
    }

    public int getCur_y() {
        return cur_y;
    }

}
