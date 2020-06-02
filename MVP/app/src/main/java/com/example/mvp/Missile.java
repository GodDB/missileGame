package com.example.mvp;

import android.widget.ImageView;


//미사일
public class Missile {

    private int id; //미사일 객체 id값

    private float cur_x;  //현재 위치 x
    private float cur_y;  //현재 위치 y

    private float vector_x;  //x축 변위
    private float vector_y;  //y축 변위


    public Missile(float x, float y, float vector_x, float vector_y, int id){
        this.cur_x = x;
        this.cur_y = y;

        this.vector_x = vector_x;
        this.vector_y = vector_y;

        this.id = id;
    }

    //이동
    public void move(){
        //이동 계산
        cur_x = cur_x + vector_x;
        cur_y = cur_y - vector_y;


    }

    public int getId(){return id;}

    public float getCur_x() {
        return cur_x;
    }

    public float getCur_y() {
        return cur_y;
    }
}
