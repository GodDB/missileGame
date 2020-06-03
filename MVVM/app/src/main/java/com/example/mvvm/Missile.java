package com.example.mvvm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


//미사일
public class Missile extends BaseObservable {

    private float cur_x;  //현재 위치 x
    private float cur_y;  //현재 위치 y

    private float vector_x;  //x축 변위
    private float vector_y;  //y축 변위


    public Missile(float x, float y, float vector_x, float vector_y){
        this.cur_x = x;
        this.cur_y = y;

        this.vector_x = vector_x;
        this.vector_y = vector_y;
    }

    public void move(){
        cur_x = cur_x + vector_x;
        cur_y = cur_y - vector_y;
        notifyPropertyChanged(BR.cur_x);
        notifyPropertyChanged(BR.cur_y);
    }


    @Bindable
    public float getCur_x() {
        return cur_x;
    }

    @Bindable
    public float getCur_y() {
        return cur_y;
    }
}
