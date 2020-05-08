package com.example.myapplication;

import android.widget.ImageView;


//미사일
public class Missile {

    private ImageView imageView; // 미사일 이미지 객체

    private float cur_x;  //현재 위치 x
    private float cur_y;  //현재 위치 y

    private float variation_x;  //x축 변화량
    private float variation_y;  //y축 변화량


    public Missile(float x, float y, float variation_x, float variation_y, ImageView imageView){
        this.cur_x = x;
        this.cur_y = y;

        this.variation_x = variation_x;
        this.variation_y = variation_y;

        this.imageView = imageView;
    }

    //이동
    public void move(){
        //이동 계산

        cur_x = cur_x - variation_x;
        cur_y = cur_y - variation_y ;

        imageView.setX(cur_x); //x 이동
        imageView.setY(cur_y); //y 이동

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
