package com.example.myapplication.model;

import android.widget.ImageView;


//미사일
public class Missile {

    private ImageView imageView;


    private int speed = 40;
    private float r;

    private float cur_x;  //현재 위치 x
    private float cur_y;  //현재 위치 y




    public Missile(float x, float y, float degree, ImageView imageView){
        this.cur_x = x;
        this.cur_y = y;
        this.r = (float)((degree+90)*Math.PI)/180;
        this.imageView = imageView;

    }

    //이동
    public void move(){
        //이동 계산
        //http://www.devpia.com/MAEUL/Contents/Detail.aspx?BoardID=50&MAEULNo=20&no=567310&ref=567310
        cur_x = (int) (cur_x - (speed * Math.cos(r)));
        cur_y = (int) (cur_y - (speed * Math.sin(r)));

        imageView.setX(cur_x);
        imageView.setY(cur_y);

    }


    public ImageView getImageView() {
        return imageView;
    }

    public float getCur_x() {
        return cur_x;
    }

    public float getCur_y() {
        return cur_y;
    }
}
